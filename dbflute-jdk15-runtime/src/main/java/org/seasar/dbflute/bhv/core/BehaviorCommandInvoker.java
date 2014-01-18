/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dbflute.bhv.core;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.XLog;
import org.seasar.dbflute.bhv.BehaviorReadable;
import org.seasar.dbflute.bhv.BehaviorWritable;
import org.seasar.dbflute.bhv.core.InvokerAssistant.DisposableProcess;
import org.seasar.dbflute.bhv.core.supplement.SequenceCacheHandler;
import org.seasar.dbflute.cbean.FetchAssistContext;
import org.seasar.dbflute.cbean.FetchNarrowingBean;
import org.seasar.dbflute.cbean.PagingInvoker;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.exception.SQLFailureException;
import org.seasar.dbflute.exception.thrower.BehaviorExceptionThrower;
import org.seasar.dbflute.helper.stacktrace.InvokeNameExtractingResource;
import org.seasar.dbflute.helper.stacktrace.InvokeNameExtractor;
import org.seasar.dbflute.helper.stacktrace.InvokeNameResult;
import org.seasar.dbflute.jdbc.ExecutionTimeInfo;
import org.seasar.dbflute.jdbc.SQLExceptionDigger;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.jdbc.SqlResultHandler;
import org.seasar.dbflute.jdbc.SqlResultInfo;
import org.seasar.dbflute.jdbc.StatementConfig;
import org.seasar.dbflute.outsidesql.OutsideSqlContext;
import org.seasar.dbflute.outsidesql.executor.OutsideSqlBasicExecutor;
import org.seasar.dbflute.outsidesql.factory.OutsideSqlExecutorFactory;
import org.seasar.dbflute.resource.DBFluteSystem;
import org.seasar.dbflute.resource.InternalMapContext;
import org.seasar.dbflute.resource.ResourceContext;
import org.seasar.dbflute.util.DfTraceViewUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;

/**
 * The invoker of behavior command.
 * <pre>
 * public interface methods are as follows:
 *   o clearExecutionCache();
 *   o isExecutionCacheEmpty();
 *   o getExecutionCacheSize();
 *   o injectComponentProperty(BehaviorCommandComponentSetup behaviorCommand);
 *   o invoke(BehaviorCommand behaviorCommand);
 *   o createOutsideSqlBasicExecutor(String tableDbName);
 *   o createBehaviorExceptionThrower();
 *   o getSequenceCacheHandler();
 * </pre>
 * @author jflute
 */
public class BehaviorCommandInvoker {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                      Injection Target
    //                                      ----------------
    protected InvokerAssistant _invokerAssistant;

    // -----------------------------------------------------
    //                                       Execution Cache
    //                                       ---------------
    /** The map of SQL execution. (dispose target, synchronized manually) */
    protected final Map<String, SqlExecution> _executionMap = newHashMap();

    // -----------------------------------------------------
    //                                    Disposable Process
    //                                    ------------------
    protected final DisposableProcess _disposableProcess = new DisposableProcess() {
        public void dispose() {
            clearExecutionCache();
        }
    };

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BehaviorCommandInvoker() {
    }

    // ===================================================================================
    //                                                                     Execution Cache
    //                                                                     ===============
    public void clearExecutionCache() {
        // basically should be called only for special case (e.g. HotDeploy)
        synchronized (_executionMap) {
            _executionMap.clear();
        }
    }

    public boolean isExecutionCacheEmpty() {
        return _executionMap.isEmpty();
    }

    public int getExecutionCacheSize() {
        return _executionMap.size();
    }

    // ===================================================================================
    //                                                                      Command Set up
    //                                                                      ==============
    /**
     * Inject the properties of component to the command of behavior. {Public Interface}
     * @param behaviorCommand The command of behavior. (NotNull)
     */
    public void injectComponentProperty(BehaviorCommandComponentSetup behaviorCommand) {
        assertInvokerAssistant();
        behaviorCommand.setDataSource(_invokerAssistant.assistDataSource());
        behaviorCommand.setStatementFactory(_invokerAssistant.assistStatementFactory());
        behaviorCommand.setBeanMetaDataFactory(_invokerAssistant.assistBeanMetaDataFactory());
        behaviorCommand.setSqlFileEncoding(getSqlFileEncoding());
    }

    protected String getSqlFileEncoding() {
        assertInvokerAssistant();
        return _invokerAssistant.assistSqlFileEncoding();
    }

    // ===================================================================================
    //                                                                      Command Invoke
    //                                                                      ==============
    /**
     * Invoke the command of behavior. {Public Interface}
     * This method is an entry point!
     * @param <RESULT> The type of result.
     * @param behaviorCommand The command of behavior. (NotNull)
     * @return The result object. (NullAllowed)
     */
    public <RESULT> RESULT invoke(BehaviorCommand<RESULT> behaviorCommand) {
        RuntimeException cause = null;
        RESULT result = null;
        try {
            final ResourceContext parentContext = getParentContext();
            initializeContext();
            setupResourceContext(behaviorCommand, parentContext);
            processBeforeHook(behaviorCommand);
            result = dispatchInvoking(behaviorCommand);
        } catch (RuntimeException e) {
            cause = e;
        } finally {
            processFinallyHook(behaviorCommand, cause);
            closeContext();
        }
        if (cause != null) {
            throw cause;
        } else {
            return result;
        }
    }

    protected <RESULT> void setupResourceContext(BehaviorCommand<RESULT> behaviorCommand, ResourceContext parentContext) {
        assertInvokerAssistant();
        final ResourceContext resourceContext = new ResourceContext();
        resourceContext.setParentContext(parentContext); // not null only when recursive call
        resourceContext.setBehaviorCommand(behaviorCommand);
        resourceContext.setCurrentDBDef(_invokerAssistant.assistCurrentDBDef());
        resourceContext.setDBMetaProvider(_invokerAssistant.assistDBMetaProvider());
        resourceContext.setSqlClauseCreator(_invokerAssistant.assistSqlClauseCreator());
        resourceContext.setSqlAnalyzerFactory(_invokerAssistant.assistSqlAnalyzerFactory());
        resourceContext.setSQLExceptionHandlerFactory(_invokerAssistant.assistSQLExceptionHandlerFactory());
        resourceContext.setGearedCipherManager(_invokerAssistant.assistGearedCipherManager());
        resourceContext.setResourceParameter(_invokerAssistant.assistResourceParameter());
        ResourceContext.setResourceContextOnThread(resourceContext);
    }

    protected <RESULT> void processBeforeHook(BehaviorCommand<RESULT> behaviorCommand) {
        if (!CallbackContext.isExistBehaviorCommandHookOnThread()) {
            return;
        }
        final BehaviorCommandHook hook = CallbackContext.getCallbackContextOnThread().getBehaviorCommandHook();
        hook.hookBefore(behaviorCommand);
    }

    protected <RESULT> void processFinallyHook(BehaviorCommand<RESULT> behaviorCommand, RuntimeException cause) {
        if (!CallbackContext.isExistBehaviorCommandHookOnThread()) {
            return;
        }
        final BehaviorCommandHook hook = CallbackContext.getCallbackContextOnThread().getBehaviorCommandHook();
        hook.hookFinally(behaviorCommand, cause);
    }

    /**
     * @param <RESULT> The type of result.
     * @param behaviorCommand The command of behavior. (NotNull)
     * @return The result object. (NullAllowed)
     */
    protected <RESULT> RESULT dispatchInvoking(BehaviorCommand<RESULT> behaviorCommand) {
        final boolean logEnabled = isLogEnabled();

        // - - - - - - - - - - - - -
        // Initialize SQL Execution
        // - - - - - - - - - - - - -
        if (behaviorCommand.isInitializeOnly()) {
            initializeSqlExecution(behaviorCommand);
            return null; // The end! (Initialize Only)
        }
        behaviorCommand.beforeGettingSqlExecution();
        SqlExecution execution = findSqlExecution(behaviorCommand);

        // - - - - - - - - - - -
        // Execute SQL Execution
        // - - - - - - - - - - -
        final SqlResultHandler sqlResultHander = getSqlResultHander();
        final boolean hasSqlResultHandler = sqlResultHander != null;
        final long before = deriveCommandBeforeAfterTimeIfNeeds(logEnabled, hasSqlResultHandler);
        Long after = null;
        Object ret = null;
        RuntimeException cause = null;
        try {
            final Object[] args = behaviorCommand.getSqlExecutionArgument();
            ret = executeSql(execution, args);

            final Class<?> retType = behaviorCommand.getCommandReturnType();
            assertRetType(retType, ret);

            after = deriveCommandBeforeAfterTimeIfNeeds(logEnabled, hasSqlResultHandler);
            if (logEnabled) {
                logReturn(behaviorCommand, retType, ret, before, after);
            }

            ret = convertReturnValueIfNeeds(ret, retType);
        } catch (RuntimeException e) {
            try {
                handleExecutionException(e); // always throw
            } catch (RuntimeException handled) {
                cause = handled;
                throw handled;
            }
        } finally {
            behaviorCommand.afterExecuting();

            // - - - - - - - - - - - -
            // Call the handler back!
            // - - - - - - - - - - - -
            if (hasSqlResultHandler) {
                callbackSqlResultHanler(behaviorCommand, sqlResultHander, ret, before, after, cause);
            }
        }

        // - - - - - - - - -
        // Cast and Return!
        // - - - - - - - - -
        @SuppressWarnings("unchecked")
        final RESULT result = (RESULT) ret;
        return result;
    }

    protected long deriveCommandBeforeAfterTimeIfNeeds(boolean logEnabled, boolean hasSqlResultHandler) {
        long time = 0;
        if (logEnabled || hasSqlResultHandler) {
            time = systemTime();
        }
        return time;
    }

    protected long systemTime() {
        return DBFluteSystem.currentTimeMillis(); // for calculating performance
    }

    protected Object convertReturnValueIfNeeds(Object ret, Class<?> retType) {
        if (retType.isPrimitive()) {
            return convertPrimitiveWrapper(ret, retType);
        } else if (Number.class.isAssignableFrom(retType)) {
            return convertNumber(ret, retType);
        }
        return ret;
    }

    protected void handleExecutionException(RuntimeException cause) {
        if (cause instanceof SQLFailureException) {
            throw cause;
        }
        final SQLExceptionDigger digger = getSQLExceptionDigger();
        final SQLException sqlEx = digger.digUp(cause);
        if (sqlEx != null) {
            handleSQLException(sqlEx);
        } else {
            throw cause;
        }
    }

    protected void handleSQLException(SQLException e) {
        ResourceContext.createSQLExceptionHandler().handleSQLException(e);
    }

    protected <RESULT> void callbackSqlResultHanler(BehaviorCommand<RESULT> behaviorCommand,
            SqlResultHandler sqlResultHander, Object ret, Long commandBefore, Long commandAfter, RuntimeException cause) {
        final SqlLogInfo sqlLogInfo = getResultSqlLogInfo(behaviorCommand);
        final Long sqlBefore = InternalMapContext.getSqlBeforeTimeMillis();
        final Long sqlAfter = InternalMapContext.getSqlAfterTimeMillis();
        final ExecutionTimeInfo timeInfo = new ExecutionTimeInfo(commandBefore, commandAfter, sqlBefore, sqlAfter);
        final SqlResultInfo info = new SqlResultInfo(behaviorCommand, ret, sqlLogInfo, timeInfo, cause);
        sqlResultHander.handle(info);
    }

    protected <RESULT> SqlLogInfo getResultSqlLogInfo(BehaviorCommand<RESULT> behaviorCommand) {
        final SqlLogInfo sqlLogInfo = InternalMapContext.getResultSqlLogInfo();
        if (sqlLogInfo != null) {
            return sqlLogInfo;
        }
        return new SqlLogInfo(behaviorCommand, null, new Object[] {}, new Class<?>[] {},
                new SqlLogInfo.SqlLogDisplaySqlBuilder() {
                    public String build(String executedSql, Object[] bindArgs, Class<?>[] bindArgTypes) {
                        return null;
                    }
                }); // as dummy
    }

    // ===================================================================================
    //                                                                       SQL Execution
    //                                                                       =============
    protected <RESULT> SqlExecution findSqlExecution(BehaviorCommand<RESULT> behaviorCommand) {
        final boolean logEnabled = isLogEnabled();
        SqlExecution execution = null;
        try {
            final String key = behaviorCommand.buildSqlExecutionKey();
            execution = getSqlExecution(key);
            if (execution == null) {
                long beforeCmd = 0;
                if (logEnabled) {
                    beforeCmd = systemTime();
                }
                SqlExecutionCreator creator = behaviorCommand.createSqlExecutionCreator();
                execution = getOrCreateSqlExecution(key, creator);
                if (logEnabled) {
                    final long afterCmd = systemTime();
                    if (beforeCmd != afterCmd) {
                        logSqlExecution(behaviorCommand, execution, beforeCmd, afterCmd);
                    }
                }
            }
            return execution;
        } finally {
            if (logEnabled) {
                logInvocation(behaviorCommand);
            }
        }
    }

    protected <RESULT> void initializeSqlExecution(BehaviorCommand<RESULT> behaviorCommand) {
        final String key = behaviorCommand.buildSqlExecutionKey();
        final SqlExecutionCreator creator = behaviorCommand.createSqlExecutionCreator();
        final SqlExecution execution = getSqlExecution(key);
        if (execution != null) {
            return; // already initialized
        }
        getOrCreateSqlExecution(key, creator); // initialize
    }

    /**
     * Get SQL-execution if it exists.
     * @param key The key of SQL execution. (NotNull)
     * @return The SQL execution that may be created then. (NullAllowed)
     */
    protected SqlExecution getSqlExecution(String key) {
        return _executionMap.get(key);
    }

    /**
     * Get SQL-execution that may be created if it does not exist.
     * @param key The key of SQL-execution. (NotNull)
     * @param executionCreator The creator of SQL-execution. (NotNull)
     * @return The SQL-execution that may be created then. (NotNull)
     */
    protected SqlExecution getOrCreateSqlExecution(String key, SqlExecutionCreator executionCreator) {
        SqlExecution execution = null;
        synchronized (_executionMap) {
            execution = getSqlExecution(key);
            if (execution != null) {
                // previous thread might have initialized
                // or reading might failed by same-time writing
                return execution;
            }
            if (isLogEnabled()) {
                log("...Initializing sqlExecution for the key '" + key + "'");
            }
            execution = executionCreator.createSqlExecution();
            _executionMap.put(key, execution);
        }
        if (execution == null) {
            String msg = "sqlExecutionCreator.createSqlCommand() should not return null:";
            msg = msg + " sqlExecutionCreator=" + executionCreator + " key=" + key;
            throw new IllegalStateException(msg);
        }
        toBeDisposable(); // for HotDeploy
        return execution;
    }

    protected Object executeSql(SqlExecution execution, Object[] args) {
        return execution.execute(args);
    }

    // ===================================================================================
    //                                                                      Log SqlCommand
    //                                                                      ==============
    protected <RESULT> void logSqlExecution(BehaviorCommand<RESULT> behaviorCommand, SqlExecution execution,
            long beforeCmd, long afterCmd) {
        final String view = DfTraceViewUtil.convertToPerformanceView(afterCmd - beforeCmd);
        log("SqlExecution Initialization Cost: [" + view + "]");
    }

    // ===================================================================================
    //                                                                      Log Invocation
    //                                                                      ==============
    protected <RESULT> void logInvocation(BehaviorCommand<RESULT> behaviorCommand) {
        final StackTraceElement[] stackTrace = new Exception().getStackTrace();
        final List<InvokeNameResult> behaviorResultList = extractBehaviorInvoke(stackTrace);
        filterBehaviorResult(behaviorCommand, behaviorResultList);

        final InvokeNameResult headBehaviorResult;
        final String invokeClassName;
        final String invokeMethodName;
        if (!behaviorResultList.isEmpty()) {
            headBehaviorResult = findHeadInvokeResult(behaviorResultList);
            invokeClassName = headBehaviorResult.getSimpleClassName();
            invokeMethodName = headBehaviorResult.getMethodName();
        } else {
            headBehaviorResult = null;
            invokeClassName = behaviorCommand.getTableDbName();
            invokeMethodName = behaviorCommand.getCommandName();
        }
        final String expWithoutKakko = buildInvocationExpressionWithoutKakko(behaviorCommand, invokeClassName,
                invokeMethodName);

        // Save behavior invoke name for error message.
        InternalMapContext.setBehaviorInvokeName(expWithoutKakko + "()");

        final String equalBorder = buildFitBorder("", "=", expWithoutKakko, false);
        final String callerExpression = expWithoutKakko + "()";

        final String frameBase = "/=====================================================";
        final String spaceBase = "                                                      ";
        log(frameBase + equalBorder + "==");
        log(spaceBase + callerExpression);
        log(spaceBase + equalBorder + "=/");
        final String invokePath = buildInvokePath(behaviorCommand, stackTrace, headBehaviorResult);
        if (Srl.is_NotNull_and_NotTrimmedEmpty(invokePath)) {
            log(invokePath);
        }

        if (behaviorCommand.isOutsideSql() && !behaviorCommand.isProcedure()) {
            final OutsideSqlContext outsideSqlContext = getOutsideSqlContext();
            if (outsideSqlContext != null) {
                log("path: " + behaviorCommand.getOutsideSqlPath());
                log("option: " + behaviorCommand.getOutsideSqlOption());
            }
        }
    }

    protected <RESULT> void filterBehaviorResult(BehaviorCommand<RESULT> behaviorCommand,
            List<InvokeNameResult> behaviorResultList) {
        for (InvokeNameResult behaviorResult : behaviorResultList) {
            final String simpleClassName = behaviorResult.getSimpleClassName();
            if (simpleClassName == null) {
                return;
            }
            if (simpleClassName.contains("Behavior") && simpleClassName.endsWith("$SLFunction")) {
                final String behaviorClassName = findBehaviorClassNameFromDBMeta(behaviorCommand.getTableDbName());
                behaviorResult.setSimpleClassName(behaviorClassName);
                behaviorResult.setMethodName("scalarSelect()." + behaviorResult.getMethodName());
            }
        }
    }

    protected <RESULT> String buildInvokePath(BehaviorCommand<RESULT> behaviorCommand, StackTraceElement[] stackTrace,
            InvokeNameResult behaviorResult) {
        final int bhvNextIndex = behaviorResult != null ? behaviorResult.getNextStartIndex() : -1;

        // Extract client result
        final List<InvokeNameResult> clientResultList = extractClientInvoke(stackTrace, bhvNextIndex);
        final InvokeNameResult headClientResult = findHeadInvokeResult(clientResultList);

        // Extract by-pass result
        final int clientFirstIndex = headClientResult != null ? headClientResult.getFoundFirstIndex() : -1;
        final int byPassLoopSize = clientFirstIndex - bhvNextIndex;
        final List<InvokeNameResult> byPassResultList = extractByPassInvoke(stackTrace, bhvNextIndex, byPassLoopSize);
        final InvokeNameResult headByPassResult = findHeadInvokeResult(byPassResultList);

        if (headClientResult == null && headByPassResult == null) { // when both are not found
            return null;
        }
        final boolean useTestShortName;
        if (isClientResultMainExists(clientResultList)) {
            useTestShortName = true;
        } else {
            useTestShortName = headClientResult != null && headByPassResult != null;
        }

        final String clientInvokeName = buildInvokeName(headClientResult, useTestShortName);
        final String byPassInvokeName = buildInvokeName(headByPassResult, useTestShortName);

        // Save client invoke name for error message.
        if (clientInvokeName.trim().length() > 0) {
            InternalMapContext.setClientInvokeName(clientInvokeName);
        }

        // Save by-pass invoke name for error message.
        if (byPassInvokeName.trim().length() > 0) {
            InternalMapContext.setByPassInvokeName(byPassInvokeName);
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(clientInvokeName);
        sb.append(findTailInvokeName(clientResultList, useTestShortName));
        sb.append(byPassInvokeName);
        sb.append(findTailInvokeName(byPassResultList, useTestShortName));
        sb.append("...");
        return sb.toString();
    }

    protected boolean isClientResultMainExists(List<InvokeNameResult> clientResultList) {
        boolean mainExists = false;
        for (InvokeNameResult invokeNameResult : clientResultList) {
            if (!invokeNameResult.hasTestSuffix()) {
                mainExists = true;
                break;
            }
        }
        return mainExists;
    }

    protected InvokeNameResult findHeadInvokeResult(List<InvokeNameResult> resultList) {
        if (!resultList.isEmpty()) {
            // The latest element is the very head invoking.
            return resultList.get(resultList.size() - 1);
        }
        return null;
    }

    protected String buildInvokeName(InvokeNameResult invokeNameResult, boolean useTestShortName) {
        return invokeNameResult != null ? invokeNameResult.buildInvokeName(useTestShortName) : "";
    }

    protected String findTailInvokeName(List<InvokeNameResult> resultList, boolean hasBoth) {
        if (resultList.size() > 1) {
            return resultList.get(0).buildInvokeName(hasBoth);
        }
        return "";
    }

    protected <RESULT> String buildInvocationExpressionWithoutKakko(BehaviorCommand<RESULT> behaviorCommand,
            String invokeClassName, String invokeMethodName) {
        if (invokeClassName.contains("OutsideSql") && invokeClassName.endsWith("Executor")) { // OutsideSql Executor Handling
            try {
                final String originalName = invokeClassName;
                if (behaviorCommand.isOutsideSql()) {
                    final OutsideSqlContext outsideSqlContext = getOutsideSqlContext();
                    final String tableDbName = outsideSqlContext.getTableDbName();
                    final String behaviorClassName = findBehaviorClassNameFromDBMeta(tableDbName);
                    invokeClassName = behaviorClassName + ".outsideSql()";
                    if (originalName.contains("Entity")) {
                        invokeClassName = invokeClassName + ".entityHandling()";
                    } else if (originalName.contains("Paging")) {
                        if (outsideSqlContext.isAutoPagingLogging()) {
                            invokeClassName = invokeClassName + ".autoPaging()";
                        } else {
                            invokeClassName = invokeClassName + ".manualPaging()";
                        }
                    } else if (originalName.contains("Cursor")) {
                        invokeClassName = invokeClassName + ".cursorHandling()";
                    }
                } else {
                    invokeClassName = "OutsideSql";
                }
            } catch (RuntimeException ignored) {
                log("Ignored exception occurred: msg=" + ignored.getMessage());
            }
        }
        String callerExpressionWithoutKakko = invokeClassName + "." + invokeMethodName;
        if ("selectPage".equals(invokeMethodName)) { // Special Handling!
            boolean resultTypeInteger = false;
            if (behaviorCommand.isOutsideSql()) {
                final OutsideSqlContext outsideSqlContext = getOutsideSqlContext();
                final Class<?> resultType = outsideSqlContext.getResultType();
                if (resultType != null) {
                    if (Integer.class.isAssignableFrom(resultType)) {
                        resultTypeInteger = true;
                    }
                }
            }
            if (resultTypeInteger || behaviorCommand.isSelectCount()) {
                callerExpressionWithoutKakko = callerExpressionWithoutKakko + "():count";
            } else {
                callerExpressionWithoutKakko = callerExpressionWithoutKakko + "():paging";
            }
        }
        return callerExpressionWithoutKakko;
    }

    protected String buildFitBorder(String prefix, String element, String lengthTargetString, boolean space) {
        final int length = space ? lengthTargetString.length() / 2 : lengthTargetString.length();
        final StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        for (int i = 0; i < length; i++) {
            sb.append(element);
            if (space) {
                sb.append(" ");
            }
        }
        if (space) {
            sb.append(element);
        }
        return sb.toString();
    }

    protected List<InvokeNameResult> extractClientInvoke(StackTraceElement[] stackTrace, final int startIndex) {
        final String[] names = _invokerAssistant.assistClientInvokeNames();
        final List<String> suffixList = Arrays.asList(names);
        final InvokeNameExtractingResource resource = new InvokeNameExtractingResource() {
            public boolean isTargetElement(String className, String methodName) {
                return isClassNameEndsWith(className, suffixList);
            }

            public String filterSimpleClassName(String simpleClassName) {
                return simpleClassName;
            }

            public boolean isUseAdditionalInfo() {
                return true;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public int getLoopSize() {
                return 25;
            }
        };
        return extractInvokeName(resource, stackTrace);
    }

    protected List<InvokeNameResult> extractByPassInvoke(StackTraceElement[] stackTrace, final int startIndex,
            final int loopSize) {
        final String[] names = _invokerAssistant.assistByPassInvokeNames();
        final List<String> suffixList = Arrays.asList(names);
        final InvokeNameExtractingResource resource = new InvokeNameExtractingResource() {
            public boolean isTargetElement(String className, String methodName) {
                return isClassNameEndsWith(className, suffixList);
            }

            public String filterSimpleClassName(String simpleClassName) {
                return simpleClassName;
            }

            public boolean isUseAdditionalInfo() {
                return true;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public int getLoopSize() {
                return loopSize >= 0 ? loopSize : 25;
            }
        };
        return extractInvokeName(resource, stackTrace);
    }

    protected List<InvokeNameResult> extractBehaviorInvoke(StackTraceElement[] stackTrace) {
        final String readableName = DfTypeUtil.toClassTitle(BehaviorReadable.class);
        final String writableName = DfTypeUtil.toClassTitle(BehaviorWritable.class);
        final String pagingInvokerName = DfTypeUtil.toClassTitle(PagingInvoker.class);
        final String[] names = new String[] { "Bhv", "BhvAp", readableName, writableName, pagingInvokerName };
        final List<String> suffixList = Arrays.asList(names);
        final List<String> keywordList = Arrays.asList(new String[] { "Bhv$", readableName + "$", writableName + "$" });
        final List<String> ousideSql1List = Arrays.asList(new String[] { "OutsideSql" });
        final List<String> ousideSql2List = Arrays.asList(new String[] { "Executor" });
        final List<String> ousideSql3List = Arrays.asList(new String[] { "Executor$" });
        final InvokeNameExtractingResource resource = new InvokeNameExtractingResource() {
            public boolean isTargetElement(String className, String methodName) {
                if (isClassNameEndsWith(className, suffixList)) {
                    return true;
                }
                if (isClassNameContains(className, keywordList)) {
                    return true;
                }
                if (isClassNameContains(className, ousideSql1List)
                        && (isClassNameEndsWith(className, ousideSql2List) || isClassNameContains(className,
                                ousideSql3List))) {
                    return true;
                }
                return false;
            }

            public String filterSimpleClassName(String simpleClassName) {
                if (simpleClassName.endsWith(readableName)) {
                    return readableName;
                } else if (simpleClassName.endsWith(writableName)) {
                    return writableName;
                } else {
                    return removeBasePrefixFromSimpleClassName(simpleClassName);
                }
            }

            public boolean isUseAdditionalInfo() {
                return false;
            }

            public int getStartIndex() {
                return 0;
            }

            public int getLoopSize() {
                return 25;
            }
        };
        return extractInvokeName(resource, stackTrace);
    }

    protected boolean isClassNameEndsWith(String className, List<String> suffixList) {
        for (String suffix : suffixList) {
            if (className.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isClassNameContains(String className, List<String> keywordList) {
        for (String keyword : keywordList) {
            if (className.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param resource the call-back resource for invoke-name-extracting. (NotNull)
     * @param stackTrace Stack log. (NotNull)
     * @return The list of result of invoke name. (NotNull: If not found, returns empty string.)
     */
    protected List<InvokeNameResult> extractInvokeName(InvokeNameExtractingResource resource,
            StackTraceElement[] stackTrace) {
        final InvokeNameExtractor extractor = new InvokeNameExtractor(stackTrace);
        return extractor.extractInvokeName(resource);
    }

    /**
     * @param simpleClassName The simple class name. (NotNull)
     * @return The simple class name removed the base prefix. (NotNull)
     */
    protected String removeBasePrefixFromSimpleClassName(String simpleClassName) {
        if (!simpleClassName.startsWith("Bs")) {
            return simpleClassName;
        }
        final int prefixLength = "Bs".length();
        if (!Character.isUpperCase(simpleClassName.substring(prefixLength).charAt(0))) {
            return simpleClassName;
        }
        if (simpleClassName.length() <= prefixLength) {
            return simpleClassName;
        }
        return "" + simpleClassName.substring(prefixLength);
    }

    protected String findBehaviorClassNameFromDBMeta(String tableDbName) {
        final DBMeta dbmeta = ResourceContext.provideDBMetaChecked(tableDbName);
        final String behaviorTypeName = dbmeta.getBehaviorTypeName();
        final String behaviorClassName = behaviorTypeName.substring(behaviorTypeName.lastIndexOf(".") + ".".length());
        return removeBasePrefixFromSimpleClassName(behaviorClassName);
    }

    // ===================================================================================
    //                                                                          Log Return
    //                                                                          ==========
    protected <RESULT> void logReturn(BehaviorCommand<RESULT> behaviorCommand, Class<?> retType, Object ret,
            long before, long after) {
        try {
            final String prefix = "===========/ [" + DfTraceViewUtil.convertToPerformanceView(after - before) + " ";
            if (List.class.isAssignableFrom(retType)) {
                if (ret == null) {
                    log(prefix + "(null)]");
                } else {
                    final List<?> ls = (java.util.List<?>) ret;
                    if (ls.isEmpty()) {
                        log(prefix + "(0)]");
                    } else if (ls.size() == 1) {
                        log(prefix + "(1) result=" + buildResultString(ls.get(0)) + "]");
                    } else {
                        log(prefix + "(" + ls.size() + ") first=" + buildResultString(ls.get(0)) + "]");
                    }
                }
            } else if (Entity.class.isAssignableFrom(retType)) {
                if (ret == null) {
                    log(prefix + "(null)" + "]");
                } else {
                    final Entity entity = (Entity) ret;
                    log(prefix + "(1) result=" + buildResultString(entity) + "]");
                }
            } else if (int[].class.isAssignableFrom(retType)) {
                if (ret == null) { // basically not come here
                    log(prefix + "(null)" + "]");
                } else {
                    final int[] resultArray = (int[]) ret;
                    if (resultArray.length == 0) {
                        log(prefix + "all-updated=(0)]");
                    } else {
                        final StringBuilder sb = new StringBuilder();
                        boolean resultExpressionScope = true;
                        int resultCount = 0;
                        int loopCount = 0;
                        for (int element : resultArray) {
                            resultCount = resultCount + element;
                            if (resultExpressionScope) {
                                if (loopCount <= 10) {
                                    if (sb.length() == 0) {
                                        sb.append(element);
                                    } else {
                                        sb.append(",").append(element);
                                    }
                                } else {
                                    sb.append(",").append("...");
                                    resultExpressionScope = false;
                                }
                            }
                            ++loopCount;
                        }
                        sb.insert(0, "{").append("}");
                        if (resultCount >= 0) {
                            log(prefix + "all-updated=(" + resultCount + ") result=" + sb + "]");
                        } else { // minus
                            log(prefix + "result=" + sb + "]"); // for example, Oracle
                        }
                    }
                }
            } else {
                log(prefix + "result=" + ret + "]");
            }
            log(" ");
        } catch (RuntimeException e) {
            String msg = "Result object debug threw the exception: behaviorCommand=";
            msg = msg + behaviorCommand + " retType=" + retType;
            msg = msg + " ret=" + ret;
            throw e;
        }
    }

    protected String buildResultString(Object obj) {
        if (obj instanceof Entity) {
            Entity entity = (Entity) obj;

            // The name for display is null
            // because you can know it other execute status logs.
            return entity.buildDisplayString(null, true, true);
        } else {
            return obj != null ? obj.toString() : "null";
        }
    }

    // ===================================================================================
    //                                                                      Context Helper
    //                                                                      ==============
    protected ResourceContext getParentContext() {
        if (isRecursiveInvoking()) {
            return ResourceContext.getResourceContextOnThread();
        }
        return null;
    }

    protected void initializeContext() {
        if (isRecursiveInvoking()) {
            saveAllContextOnThread();
        }
        clearAllCurrentContext();
    }

    protected boolean isRecursiveInvoking() { // should be called before initialization
        return ResourceContext.isExistResourceContextOnThread();
    }

    protected void closeContext() {
        if (FetchAssistContext.isExistFetchNarrowingBeanOnThread()) {
            // /- - - - - - - - - - - - - - - - - - - - - - - - - - - -
            // Because there is possible that fetch narrowing has been
            // ignored for manualPaging of outsideSql.
            // - - - - - - - - - -/
            final FetchNarrowingBean fnbean = FetchAssistContext.getFetchNarrowingBeanOnThread();
            fnbean.restoreIgnoredFetchNarrowing();
        }
        clearAllCurrentContext();
        restoreAllContextOnThreadIfExists();
    }

    protected void saveAllContextOnThread() {
        ContextStack.saveAllContextOnThread();
    }

    protected void restoreAllContextOnThreadIfExists() {
        ContextStack.restoreAllContextOnThreadIfExists();
    }

    protected void clearAllCurrentContext() {
        ContextStack.clearAllCurrentContext();
    }

    protected OutsideSqlContext getOutsideSqlContext() {
        if (!OutsideSqlContext.isExistOutsideSqlContextOnThread()) {
            return null;
        }
        return OutsideSqlContext.getOutsideSqlContextOnThread();
    }

    protected SqlResultHandler getSqlResultHander() {
        if (!CallbackContext.isExistCallbackContextOnThread()) {
            return null;
        }
        return CallbackContext.getCallbackContextOnThread().getSqlResultHandler();
    }

    // ===================================================================================
    //                                                                  Execute Status Log
    //                                                                  ==================
    protected void log(String msg) {
        XLog.log(msg);
    }

    protected boolean isLogEnabled() {
        return XLog.isLogEnabled();
    }

    // ===================================================================================
    //                                                                             Dispose
    //                                                                             =======
    protected void toBeDisposable() {
        assertInvokerAssistant();
        _invokerAssistant.toBeDisposable(_disposableProcess);
    }

    // ===================================================================================
    //                                                                          OutsideSql
    //                                                                          ==========
    /**
     * @param <BEHAVIOR> The type of behavior.
     * @param tableDbName The DB name of table. (NotNull)
     * @return The basic executor of outside SQL. (NotNull) 
     */
    public <BEHAVIOR> OutsideSqlBasicExecutor<BEHAVIOR> createOutsideSqlBasicExecutor(String tableDbName) {
        final OutsideSqlExecutorFactory factory = _invokerAssistant.assistOutsideSqlExecutorFactory();
        final DBDef dbdef = _invokerAssistant.assistCurrentDBDef();
        final StatementConfig config = _invokerAssistant.assistDefaultStatementConfig();
        return factory.createBasic(this, tableDbName, dbdef, config, null); // for an entry instance
    }

    // ===================================================================================
    //                                                                 SQLException Digger
    //                                                                 ===================
    /**
     * @return The digger of SQLException. (NotNull)
     */
    public SQLExceptionDigger getSQLExceptionDigger() {
        return _invokerAssistant.assistSQLExceptionDigger();
    }

    // ===================================================================================
    //                                                                      Sequence Cache
    //                                                                      ==============
    /**
     * Get the handler of sequence cache.
     * @return The handler of sequence cache. (NotNull)
     */
    public SequenceCacheHandler getSequenceCacheHandler() {
        return _invokerAssistant.assistSequenceCacheHandler();
    }

    // ===================================================================================
    //                                                                   Exception Thrower
    //                                                                   =================
    /**
     * @return The thrower of behavior exception. (NotNull)
     */
    public BehaviorExceptionThrower createBehaviorExceptionThrower() {
        return _invokerAssistant.assistBehaviorExceptionThrower();
    }

    // ===================================================================================
    //                                                                      Convert Helper
    //                                                                      ==============
    protected Object convertPrimitiveWrapper(Object ret, Class<?> retType) {
        return DfTypeUtil.toWrapper(ret, retType);
    }

    protected Object convertNumber(Object ret, Class<?> retType) {
        return DfTypeUtil.toNumber(ret, retType);
    }

    // ===================================================================================
    //                                                                       Assert Helper
    //                                                                       =============
    protected void assertRetType(Class<?> retType, Object ret) {
        if (List.class.isAssignableFrom(retType)) {
            if (ret != null && !(ret instanceof List<?>)) {
                String msg = "The retType is difference from actual return: ";
                msg = msg + "retType=" + retType + " ret.getClass()=" + ret.getClass() + " ref=" + ret;
                throw new IllegalStateException(msg);
            }
        } else if (Entity.class.isAssignableFrom(retType)) {
            if (ret != null && !(ret instanceof Entity)) {
                String msg = "The retType is difference from actual return: ";
                msg = msg + "retType=" + retType + " ret.getClass()=" + ret.getClass() + " ref=" + ret;
                throw new IllegalStateException(msg);
            }
        }
    }

    protected void assertInvokerAssistant() {
        if (_invokerAssistant == null) {
            String msg = "The attribute 'invokerAssistant' should not be null!";
            throw new IllegalStateException(msg);
        }
    }

    // ===================================================================================
    //                                                                      General Helper
    //                                                                      ==============
    protected <KEY, VALUE> HashMap<KEY, VALUE> newHashMap() {
        return new HashMap<KEY, VALUE>();
    }

    protected String ln() {
        return DBFluteSystem.getBasicLn();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setInvokerAssistant(InvokerAssistant invokerAssistant) {
        _invokerAssistant = invokerAssistant;
    }
}
