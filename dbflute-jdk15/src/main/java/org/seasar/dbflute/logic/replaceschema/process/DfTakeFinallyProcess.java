package org.seasar.dbflute.logic.replaceschema.process;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.engine.database.model.UnifiedSchema;
import org.seasar.dbflute.exception.DfTakeFinallyAssertionFailureException;
import org.seasar.dbflute.helper.jdbc.DfRunnerInformation;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileFireMan;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileFireResult;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileRunner;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileRunnerDispatcher;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileRunnerExecute;
import org.seasar.dbflute.helper.jdbc.sqlfile.DfSqlFileRunnerExecute.DfRunnerDispatchResult;
import org.seasar.dbflute.logic.replaceschema.dataassert.DfDataAssertHandler;
import org.seasar.dbflute.logic.replaceschema.dataassert.DfDataAssertProvider;
import org.seasar.dbflute.logic.replaceschema.finalinfo.DfTakeFinallyFinalInfo;
import org.seasar.dbflute.logic.replaceschema.takefinally.sequence.DfSequenceHandler;
import org.seasar.dbflute.logic.replaceschema.takefinally.sequence.factory.DfSequenceHandlerFactory;
import org.seasar.dbflute.properties.DfReplaceSchemaProperties;
import org.seasar.dbflute.properties.DfSequenceIdentityProperties;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;

/**
 * @author jflute
 * @since 0.9.8.3 (2011/04/29 Friday)
 */
public class DfTakeFinallyProcess extends DfAbstractReplaceSchemaProcess {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Log instance. */
    private static final Log _log = LogFactory.getLog(DfTakeFinallyProcess.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                        Basic Resource
    //                                        --------------
    protected final String _sqlRootDir;
    protected final DataSource _dataSource;
    protected final UnifiedSchema _mainSchema;
    protected final boolean _takeAssert;

    /** The list of assertion failure exception for take-assert. */
    protected final List<DfTakeFinallyAssertionFailureException> _takeAssertExList = DfCollectionUtil.newArrayList();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    protected DfTakeFinallyProcess(String sqlRootDir, DataSource dataSource, UnifiedSchema mainSchema,
            boolean takeAssert) {
        _sqlRootDir = sqlRootDir;
        _dataSource = dataSource;
        _mainSchema = mainSchema;
        _takeAssert = takeAssert;
    }

    public static DfTakeFinallyProcess createAsCore(String sqlRootDir, DataSource dataSource) {
        final UnifiedSchema mainSchema = getDatabaseProperties().getDatabaseSchema();
        return new DfTakeFinallyProcess(sqlRootDir, dataSource, mainSchema, false);
    }

    public static DfTakeFinallyProcess createAsTakeAssert(String sqlRootDir, DataSource dataSource) {
        final UnifiedSchema mainSchema = getDatabaseProperties().getDatabaseSchema();
        return new DfTakeFinallyProcess(sqlRootDir, dataSource, mainSchema, true);
    }

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    public DfTakeFinallyFinalInfo execute() {
        final DfRunnerInformation runInfo = createRunnerInformation();
        DfSqlFileFireResult fireResult = null;
        DfTakeFinallyAssertionFailureException assertionEx = null;
        try {
            fireResult = takeFinally(runInfo);
            if (_takeAssert && !_takeAssertExList.isEmpty()) {
                // override result with saved exceptions
                // this message uses the first exception
                fireResult = createFailureFireResult(_takeAssertExList.get(0));
            }
        } catch (DfTakeFinallyAssertionFailureException e) {
            // if take-assert, the exception does not thrown
            fireResult = createFailureFireResult(e);
            assertionEx = e;
        }
        final DfTakeFinallyFinalInfo finalInfo = createFinalInfo(fireResult, assertionEx);
        incrementSequenceToDataMax();
        return finalInfo;
    }

    protected DfSqlFileFireResult createFailureFireResult(DfTakeFinallyAssertionFailureException e) {
        final DfSqlFileFireResult fireResult = new DfSqlFileFireResult();
        fireResult.setExistsError(true);
        fireResult.setResultMessage("{Take Finally}: *asserted");
        final StringBuilder sb = new StringBuilder();
        sb.append(" >> ").append(DfTypeUtil.toClassTitle(e));
        sb.append(ln()).append(" (Look at the exception message: console or dbflute.log)");
        fireResult.setDetailMessage(sb.toString());
        return fireResult;
    }

    @Override
    protected boolean isRollbackTransaction() {
        // take-assert task should not update data
        // the task cannot execute update statement basically
        // but it uses a safety connection the task uses just in case
        return _takeAssert;
    }

    // -----------------------------------------------------
    //                                          Take Finally
    //                                          ------------
    protected DfSqlFileFireResult takeFinally(DfRunnerInformation runInfo) {
        _log.info("");
        _log.info("* * * * * * * **");
        _log.info("*              *");
        _log.info("* Take Finally *");
        _log.info("*              *");
        _log.info("* * * * * * * **");
        final DfSqlFileFireMan fireMan = new DfSqlFileFireMan();
        fireMan.setExecutorName("Take Finally");
        return fireMan.fire(getSqlFileRunner4TakeFinally(runInfo), getTakeFinallySqlFileList());
    }

    protected DfSqlFileRunner getSqlFileRunner4TakeFinally(final DfRunnerInformation runInfo) {
        final DfReplaceSchemaProperties prop = getReplaceSchemaProperties();
        final DfSqlFileRunnerExecute runnerExecute = new DfSqlFileRunnerExecute(runInfo, _dataSource) {
            @Override
            protected String filterSql(String sql) {
                sql = super.filterSql(sql);
                sql = prop.resolveFilterVariablesIfNeeds(sql);
                return sql;
            }

            @Override
            protected boolean isSqlTrimAndRemoveLineSeparator() {
                return false; // for looks
            }

            @Override
            protected boolean isHandlingCommentOnLineSeparator() {
                return true;
            }

            @Override
            protected boolean isDbCommentLine(String line) {
                final boolean commentLine = super.isDbCommentLine(line);
                if (commentLine) {
                    return commentLine;
                }
                // for irregular pattern
                return isDbCommentLineForIrregularPattern(line);
            }

            @Override
            protected String getTerminater4Tool() {
                return resolveTerminater4Tool();
            }

            @Override
            protected boolean isTargetFile(String sql) {
                return getReplaceSchemaProperties().isTargetRepsFile(sql);
            }
        };
        runnerExecute.setDispatcher(new DfSqlFileRunnerDispatcher() {
            public DfRunnerDispatchResult dispatch(File sqlFile, Statement st, String sql) throws SQLException {
                final String loadType = getReplaceSchemaProperties().getRepsEnvType();
                final DfDataAssertProvider dataAssertProvider = new DfDataAssertProvider(loadType);
                final DfDataAssertHandler dataAssertHandler = dataAssertProvider.provideDataAssertHandler(sql);
                if (dataAssertHandler == null) {
                    if (_takeAssert) {
                        _log.info("*Skipped the statement because of not assertion SQL");
                        return DfRunnerDispatchResult.SKIPPED;
                    } else {
                        return DfRunnerDispatchResult.NONE;
                    }
                }
                if (st == null) {
                    String msg = "The statement was null: sqlFile=" + sqlFile;
                    throw new IllegalStateException(msg);
                }
                try {
                    dataAssertHandler.handle(sqlFile, st, sql);
                } catch (DfTakeFinallyAssertionFailureException e) {
                    handleAssertionFailureException(e);
                }
                return DfRunnerDispatchResult.DISPATCHED;
            }
        });
        return runnerExecute;
    }

    protected void handleAssertionFailureException(DfTakeFinallyAssertionFailureException e) {
        if (_takeAssert) { // save for final message
            _takeAssertExList.add(e);
        } else {
            throw e;
        }
    }

    protected List<File> getTakeFinallySqlFileList() {
        final List<File> fileList = new ArrayList<File>();
        fileList.addAll(getReplaceSchemaProperties().getTakeFinallySqlFileList(_sqlRootDir));
        fileList.addAll(getReplaceSchemaProperties().getAppcalitionTakeFinallySqlFileList());
        return fileList;
    }

    // -----------------------------------------------------
    //                                    Increment Sequence
    //                                    ------------------
    protected void incrementSequenceToDataMax() {
        if (!getReplaceSchemaProperties().isIncrementSequenceToDataMax()) {
            return;
        }
        if (_takeAssert) {
            return;
        }
        _log.info("");
        _log.info("* * * * * * * * * * **");
        _log.info("*                    *");
        _log.info("* Increment Sequence *");
        _log.info("*                    *");
        _log.info("* * * * * * * * * * **");
        final DfSequenceIdentityProperties sequenceProp = getProperties().getSequenceIdentityProperties();
        final Map<String, String> tableSequenceMap = sequenceProp.getTableSequenceMap();
        final DfSequenceHandlerFactory factory = new DfSequenceHandlerFactory(_dataSource, getDatabaseTypeFacadeProp(),
                getDatabaseProperties());
        final DfSequenceHandler sequenceHandler = factory.createSequenceHandler();
        if (sequenceHandler == null) {
            String databaseType = getDatabaseTypeFacadeProp().getTargetDatabase();
            String msg = "Unsupported isIncrementSequenceToDataMax at " + databaseType;
            throw new UnsupportedOperationException(msg);
        }
        sequenceHandler.incrementSequenceToDataMax(tableSequenceMap);
    }

    // ===================================================================================
    //                                                                          Final Info
    //                                                                          ==========
    protected DfTakeFinallyFinalInfo createFinalInfo(DfSqlFileFireResult fireResult,
            DfTakeFinallyAssertionFailureException assertionEx) {
        final DfTakeFinallyFinalInfo finalInfo = new DfTakeFinallyFinalInfo();
        if (fireResult != null) {
            finalInfo.setResultMessage(fireResult.getResultMessage());
            final List<String> detailMessageList = extractDetailMessageList(fireResult);
            for (String detailMessage : detailMessageList) {
                finalInfo.addDetailMessage(detailMessage);
            }
            finalInfo.setBreakCause(fireResult.getBreakCause());
            finalInfo.setFailure(fireResult.isExistsError());
        }
        finalInfo.setAssertionEx(assertionEx);
        return finalInfo;
    }

    // ===================================================================================
    //                                                                     Batch Assertion
    //                                                                     ===============
    public List<DfTakeFinallyAssertionFailureException> getTakeAssertExList() {
        return _takeAssertExList;
    }
}
