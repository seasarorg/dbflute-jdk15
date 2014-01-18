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
package org.seasar.dbflute.logic.replaceschema.schemainitializer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.engine.database.model.UnifiedSchema;
import org.seasar.dbflute.DfBuildProperties;
import org.seasar.dbflute.exception.SQLFailureException;
import org.seasar.dbflute.helper.StringSet;
import org.seasar.dbflute.helper.jdbc.facade.DfJdbcFacade;
import org.seasar.dbflute.logic.jdbc.metadata.basic.DfForeignKeyExtractor;
import org.seasar.dbflute.logic.jdbc.metadata.basic.DfProcedureExtractor;
import org.seasar.dbflute.logic.jdbc.metadata.basic.DfTableExtractor;
import org.seasar.dbflute.logic.jdbc.metadata.info.DfForeignKeyMeta;
import org.seasar.dbflute.logic.jdbc.metadata.info.DfProcedureMeta;
import org.seasar.dbflute.logic.jdbc.metadata.info.DfTableMeta;
import org.seasar.dbflute.properties.DfLittleAdjustmentProperties;

/**
 * The schema initializer with JDBC.
 * @author jflute
 */
public class DfSchemaInitializerJdbc implements DfSchemaInitializer {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Log _log = LogFactory.getLog(DfSchemaInitializerJdbc.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected DataSource _dataSource;
    protected UnifiedSchema _unifiedSchema;
    protected boolean _useFullQualifiedTableName;
    protected List<String> _dropObjectTypeList;
    protected List<String> _initializeFirstSqlList;
    protected final StringSet _droppedPackageSet = StringSet.createAsCaseInsensitive();

    // /= = = = = = = = = = = = =
    // Detail execution handling!
    // = = = = = = = = = =/
    protected boolean _suppressTruncateTable;
    protected boolean _suppressDropForeignKey;
    protected boolean _suppressDropTable;
    protected boolean _suppressDropSequence;
    protected boolean _suppressDropProcedure;
    protected boolean _suppressDropDBLink;
    protected boolean _suppressLoggingSql;

    // ===================================================================================
    //                                                                   Initialize Schema
    //                                                                   =================
    public void initializeSchema() {
        Connection conn = null;
        try {
            conn = _dataSource.getConnection();
            final List<DfTableMeta> tableMetaList;
            try {
                final DatabaseMetaData metaData = conn.getMetaData();
                final DfTableExtractor tableExtractor = new DfTableExtractor() {
                    @Override
                    protected String[] getRealObjectTypeTargetArray(UnifiedSchema unifiedSchema) {
                        if (_dropObjectTypeList != null) {
                            return _dropObjectTypeList.toArray(new String[] {});
                        } else {
                            return super.getRealObjectTypeTargetArray(unifiedSchema);
                        }
                    }
                };
                tableExtractor.suppressExceptTarget();
                tableMetaList = tableExtractor.getTableList(metaData, _unifiedSchema);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            executeObject(conn, tableMetaList);
        } catch (SQLException e) {
            String msg = "Failed to the initialize schema: " + _unifiedSchema;
            throw new SQLFailureException(msg, e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                    _log.info("connection.close() threw the exception!", ignored);
                }
            }
        }
    }

    protected void executeObject(Connection conn, List<DfTableMeta> tableMetaList) {
        executeFirstSqlProcess(conn, tableMetaList);
        final boolean procedureBeforeTable = isDropProcedureBeforeTable();
        if (procedureBeforeTable) {
            executeProcedureProcess(conn, tableMetaList);
        }
        executeTableProcess(conn, tableMetaList);
        if (!procedureBeforeTable) { // basically here
            executeProcedureProcess(conn, tableMetaList);
        }
        executeVariousProcess(conn, tableMetaList);
    }

    protected void executeFirstSqlProcess(Connection conn, List<DfTableMeta> tableMetaList) {
        if (_initializeFirstSqlList == null || _initializeFirstSqlList.isEmpty()) {
            return;
        }
        final DfJdbcFacade jdbcFacade = new DfJdbcFacade(conn);
        for (String firstSql : _initializeFirstSqlList) {
            logReplaceSql(firstSql);
            jdbcFacade.execute(firstSql);
        }
    }

    protected void executeTableProcess(Connection conn, List<DfTableMeta> tableMetaList) {
        if (!_suppressTruncateTable) {
            truncateTableIfPossible(conn, tableMetaList);
        } else {
            _log.info("*Suppress truncating tables");
        }
        if (!_suppressDropForeignKey) {
            dropForeignKey(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping foreign keys");
        }
        if (!_suppressDropTable) {
            dropTable(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping tables");
        }
        if (!_suppressDropSequence) {
            dropSequence(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping sequences");
        }
    }

    protected void executeProcedureProcess(Connection conn, List<DfTableMeta> tableMetaList) {
        if (!_suppressDropProcedure) {
            dropProcedure(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping procedures");
        }
    }

    protected void executeVariousProcess(Connection conn, List<DfTableMeta> tableMetaList) {
        if (!_suppressDropDBLink) {
            dropDBLink(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping DB links");
        }
        if (!_suppressTruncateTable || !_suppressDropProcedure) { // belongs to the two
            dropTypeObject(conn, tableMetaList);
        } else {
            _log.info("*Suppress dropping type objectss");
        }
    }

    protected boolean isDropProcedureBeforeTable() {
        return false;
    }

    // ===================================================================================
    //                                                                      Truncate Table
    //                                                                      ==============
    protected void truncateTableIfPossible(Connection connection, List<DfTableMeta> tableMetaList) {
        final DfTruncateTableByJdbcCallback callback = new DfTruncateTableByJdbcCallback() {
            public String buildTruncateTableSql(DfTableMeta metaInfo) {
                final StringBuilder sb = new StringBuilder();
                sb.append("truncate table ").append(filterTableName(metaInfo.getTableName()));
                return sb.toString();
            }
        };
        callbackTruncateTableByJdbc(connection, tableMetaList, callback);
    }

    protected static interface DfTruncateTableByJdbcCallback {
        public String buildTruncateTableSql(DfTableMeta metaInfo);
    }

    protected void callbackTruncateTableByJdbc(Connection conn, List<DfTableMeta> tableMetaInfoList,
            DfTruncateTableByJdbcCallback callback) {
        for (DfTableMeta metaInfo : tableMetaInfoList) {
            final String truncateTableSql = callback.buildTruncateTableSql(metaInfo);
            Statement st = null;
            try {
                st = conn.createStatement();
                st.execute(truncateTableSql);
                logReplaceSql(truncateTableSql);
            } catch (Exception e) {
                continue;
            } finally {
                closeStatement(st);
            }
        }
    }

    // ===================================================================================
    //                                                                    Drop Foreign Key
    //                                                                    ================
    protected void dropForeignKey(Connection conn, List<DfTableMeta> tableMetaList) {
        final DfDropForeignKeyByJdbcCallback callback = new DfDropForeignKeyByJdbcCallback() {
            public String buildDropForeignKeySql(DfForeignKeyMeta metaInfo) {
                final String foreignKeyName = metaInfo.getForeignKeyName();
                final String localTableName = filterTableName(metaInfo.getLocalTableName());
                final StringBuilder sb = new StringBuilder();
                sb.append("alter table ").append(localTableName).append(" drop constraint ").append(foreignKeyName);
                return sb.toString();
            }
        };
        callbackDropForeignKeyByJdbc(conn, tableMetaList, callback);
    }

    protected static interface DfDropForeignKeyByJdbcCallback {
        public String buildDropForeignKeySql(DfForeignKeyMeta metaInfo);
    }

    protected void callbackDropForeignKeyByJdbc(Connection conn, List<DfTableMeta> tableMetaList,
            DfDropForeignKeyByJdbcCallback callback) {
        Statement st = null;
        try {
            st = conn.createStatement();
            for (DfTableMeta tableMeta : tableMetaList) {
                if (isSkipDropForeignKey(tableMeta)) {
                    continue;
                }
                final DfForeignKeyExtractor extractor = new DfForeignKeyExtractor();
                extractor.suppressExceptTarget();

                final DatabaseMetaData dbMetaData = conn.getMetaData();
                final Map<String, DfForeignKeyMeta> foreignKeyMetaInfoMap = extractor.getForeignKeyMap(dbMetaData,
                        tableMeta);
                final Set<String> keySet = foreignKeyMetaInfoMap.keySet();
                for (String foreignKeyName : keySet) {
                    final DfForeignKeyMeta foreignKeyMetaInfo = foreignKeyMetaInfoMap.get(foreignKeyName);
                    final String dropForeignKeySql = callback.buildDropForeignKeySql(foreignKeyMetaInfo);
                    logReplaceSql(dropForeignKeySql);
                    st.execute(dropForeignKeySql);
                }
            }
        } catch (SQLException e) {
            String msg = "Failed to drop foreign keys!";
            throw new SQLFailureException(msg, e);
        } finally {
            closeStatement(st);
        }
    }

    protected boolean isSkipDropForeignKey(DfTableMeta tableMetaInfo) { // for sub class.
        return false;
    }

    // ===================================================================================
    //                                                                          Drop Table
    //                                                                          ==========
    protected void dropTable(Connection conn, List<DfTableMeta> tableMetaList) {
        List<DfTableMeta> viewList = new ArrayList<DfTableMeta>();
        List<DfTableMeta> otherList = new ArrayList<DfTableMeta>();
        for (DfTableMeta tableMeta : tableMetaList) {
            if (tableMeta.isTableTypeView()) {
                viewList.add(tableMeta);
            } else {
                otherList.add(tableMeta);
            }
        }

        // Drop view and drop others
        final List<DfTableMeta> sortedList = new ArrayList<DfTableMeta>();
        sortedList.addAll(viewList);
        sortedList.addAll(otherList);

        callbackDropTableByJdbc(conn, sortedList, new DfDropTableByJdbcCallback() {
            public String buildDropTableSql(DfTableMeta metaInfo) {
                final StringBuilder sb = new StringBuilder();
                setupDropTable(sb, metaInfo);
                return sb.toString();
            }

            public String buildDropMaterializedViewSql(DfTableMeta metaInfo) {
                final StringBuilder sb = new StringBuilder();
                sb.append("drop materialized view ").append(metaInfo.getTableName());
                return sb.toString();
            }
        });
    }

    protected void setupDropTable(StringBuilder sb, DfTableMeta tableMeta) {
        final String tableName = filterTableName(tableMeta.getTableName());
        if (tableMeta.isTableTypeView()) {
            sb.append("drop view ").append(tableName);
        } else {
            sb.append("drop table ").append(tableName);
        }
    }

    protected static interface DfDropTableByJdbcCallback {
        public String buildDropTableSql(DfTableMeta metaInfo);

        public String buildDropMaterializedViewSql(DfTableMeta metaInfo);
    }

    protected void callbackDropTableByJdbc(Connection conn, List<DfTableMeta> tableMetaList,
            DfDropTableByJdbcCallback callback) {
        String currentSql = null;
        Statement st = null;
        try {
            st = conn.createStatement();
            for (DfTableMeta tableMeta : tableMetaList) {
                final String dropTableSql = callback.buildDropTableSql(tableMeta);
                currentSql = dropTableSql;
                logReplaceSql(dropTableSql);
                try {
                    st.execute(dropTableSql);
                } catch (SQLException e) {
                    // = = = = = = = = = = = =
                    // for materialized view!
                    // = = = = = = = = = = = =
                    final String dropMaterializedViewSql = callback.buildDropMaterializedViewSql(tableMeta);
                    try {
                        st.execute(dropMaterializedViewSql);
                        logReplaceSql("  (o) retry: " + dropMaterializedViewSql);
                    } catch (SQLException ignored) {
                        logReplaceSql("  (x) retry: " + dropMaterializedViewSql);
                        throw e;
                    }
                }
            }
        } catch (SQLException e) {
            String msg = "Failed to drop the table: " + currentSql;
            throw new SQLFailureException(msg, e);
        } finally {
            closeStatement(st);
        }
    }

    // ===================================================================================
    //                                                                       Drop Sequence
    //                                                                       =============
    protected void dropSequence(Connection conn, List<DfTableMeta> tableMetaList) {
        // override if it needs
    }

    // ===================================================================================
    //                                                                      Drop Procedure
    //                                                                      ==============
    protected void dropProcedure(Connection conn, List<DfTableMeta> tableMetaList) {
        final DfProcedureExtractor handler = new DfProcedureExtractor();
        handler.suppressAdditionalSchema();
        handler.suppressLogging();
        final List<DfProcedureMeta> procedureList;
        try {
            procedureList = handler.getPlainProcedureList(_dataSource, _unifiedSchema);
        } catch (SQLException e) {
            String msg = "Failed to get procedure meta data: " + _unifiedSchema;
            throw new SQLFailureException(msg, e);
        }
        callbackDropProcedureByJdbc(conn, procedureList, createDropProcedureByJdbcCallback());
    }

    protected DfDropProcedureByJdbcCallback createDropProcedureByJdbcCallback() {
        return new DfDropProcedureByJdbcCallback() {
            public String buildDropProcedureSql(DfProcedureMeta procedureMeta) {
                return "drop procedure " + buildProcedureSqlName(procedureMeta);
            }

            public String buildDropFunctionSql(DfProcedureMeta procedureMeta) {
                return "drop function " + buildProcedureSqlName(procedureMeta);
            }

            public String buildDropPackageSql(DfProcedureMeta procedureMeta) {
                return "drop package " + procedureMeta.getProcedurePackage();
            }
        };
    }

    protected String buildProcedureSqlName(DfProcedureMeta metaInfo) {
        // procedure has complex rule to call
        // so it uses SQL name despite whether it uses an own connection
        return metaInfo.buildProcedureSqlName();
    }

    public static interface DfDropProcedureByJdbcCallback {
        String buildDropProcedureSql(DfProcedureMeta procedureMeta);

        String buildDropFunctionSql(DfProcedureMeta procedureMeta);

        String buildDropPackageSql(DfProcedureMeta procedureMeta);
    }

    protected void callbackDropProcedureByJdbc(Connection conn, List<DfProcedureMeta> procedureMetaList,
            DfDropProcedureByJdbcCallback callback) {
        String currentSql = null;
        Statement st = null;
        try {
            st = conn.createStatement();
            for (DfProcedureMeta procedureMeta : procedureMetaList) {
                if (procedureMeta.isPackageProcdure()) {
                    currentSql = callback.buildDropPackageSql(procedureMeta);
                    handlePackageProcedure(procedureMeta, st, currentSql);
                    continue;
                }
                final String dropFirstSql = buildDropProcedureFirstSql(callback, procedureMeta);
                currentSql = dropFirstSql;
                logReplaceSql(dropFirstSql);
                try {
                    st.execute(dropFirstSql);
                } catch (SQLException e) {
                    final String dropSecondSql = buildDropProcedureSecondSql(callback, procedureMeta);
                    try {
                        st.execute(dropSecondSql);
                        logReplaceSql("  (o) retry: " + dropSecondSql);
                    } catch (SQLException ignored) {
                        logReplaceSql("  (x) retry: " + dropSecondSql);
                        throw e;
                    }
                }
            }
        } catch (SQLException e) {
            String msg = "Failed to drop the procedure: " + currentSql;
            throw new SQLFailureException(msg, e);
        } finally {
            closeStatement(st);
        }
    }

    protected String buildDropProcedureFirstSql(DfDropProcedureByJdbcCallback callback, DfProcedureMeta procedureMeta) {
        if (isDropFunctionFirst()) {
            return callback.buildDropFunctionSql(procedureMeta);
        }
        return callback.buildDropProcedureSql(procedureMeta);
    }

    protected String buildDropProcedureSecondSql(DfDropProcedureByJdbcCallback callback, DfProcedureMeta procedureMeta) {
        if (isDropFunctionFirst()) {
            return callback.buildDropProcedureSql(procedureMeta);
        }
        return callback.buildDropFunctionSql(procedureMeta);
    }

    protected boolean isDropFunctionFirst() {
        return false; // default is procedure first
    }

    protected void handlePackageProcedure(DfProcedureMeta procedureMeta, Statement st, String sql) throws SQLException {
        final String procedurePackage = procedureMeta.getProcedurePackage();
        if (_droppedPackageSet.contains(procedurePackage)) {
            return;
        }
        _log.info(sql);
        st.execute(sql);
        _droppedPackageSet.add(procedurePackage);
    }

    // ===================================================================================
    //                                                                        Drop DB Link
    //                                                                        ============
    protected void dropDBLink(Connection conn, List<DfTableMeta> tableMetaList) {
        // override if it needs
    }

    // ===================================================================================
    //                                                                    Drop Type Object
    //                                                                    ================
    protected void dropTypeObject(Connection conn, List<DfTableMeta> tableMetaList) {
        // override if it needs
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected String filterTableName(String tableName) { // not used when procedure
        // because additional drop uses an own connection
        // so it does not need to qualify names
        //if (_useFullQualifiedTableName && _unifiedSchema.hasSchema()) {
        //    tableName = _unifiedSchema.buildFullQualifiedName(tableName);
        //}
        final DfLittleAdjustmentProperties prop = getLittleAdjustmentProperties();
        tableName = prop.quoteTableNameIfNeedsDirectUse(tableName);
        return tableName;
    }

    protected DfLittleAdjustmentProperties getLittleAdjustmentProperties() {
        return DfBuildProperties.getInstance().getLittleAdjustmentProperties();
    }

    protected void closeResource(ResultSet rs, Statement st) {
        closeResultSet(rs);
        closeStatement(st);
    }

    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
                _log.info("rs.close() threw the exception!", ignored);
            }
        }
    }

    protected void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
                _log.info("statement.close() threw the exception!", ignored);
            }
        }
    }

    // ===================================================================================
    //                                                                      Logging Helper
    //                                                                      ==============
    protected void logReplaceSql(String sql) {
        if (!_suppressLoggingSql) {
            _log.info(sql);
        }
    }

    // ===================================================================================
    //                                                                      General Helper
    //                                                                      ==============
    protected String ln() {
        return "\n";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setDataSource(DataSource dataSource) {
        _dataSource = dataSource;
    }

    public void setUnifiedSchema(UnifiedSchema unifiedSchema) {
        _unifiedSchema = unifiedSchema;
    }

    public void setDropObjectTypeList(List<String> dropObjectTypeList) {
        _dropObjectTypeList = dropObjectTypeList;
    }

    public void setInitializeFirstSqlList(List<String> initializeFirstSqlList) {
        _initializeFirstSqlList = initializeFirstSqlList;
    }

    // /= = = = = = = = = = = = =
    // Detail execution handling!
    // = = = = = = = = = =/

    public boolean isSuppressTruncateTable() {
        return _suppressTruncateTable;
    }

    public void setSuppressTruncateTable(boolean suppressTruncateTable) {
        this._suppressTruncateTable = suppressTruncateTable;
    }

    public boolean isSuppressDropForeignKey() {
        return _suppressDropForeignKey;
    }

    public void setSuppressDropForeignKey(boolean suppressDropForeignKey) {
        this._suppressDropForeignKey = suppressDropForeignKey;
    }

    public boolean isSuppressDropTable() {
        return _suppressDropTable;
    }

    public void setSuppressDropTable(boolean suppressDropTable) {
        this._suppressDropTable = suppressDropTable;
    }

    public boolean isSuppressDropSequence() {
        return _suppressDropSequence;
    }

    public void setSuppressDropSequence(boolean suppressDropSequence) {
        this._suppressDropSequence = suppressDropSequence;
    }

    public boolean isSuppressDropProcedure() {
        return _suppressDropProcedure;
    }

    public void setSuppressDropProcedure(boolean suppressDropProcedure) {
        this._suppressDropProcedure = suppressDropProcedure;
    }

    public boolean isSuppressDropDBLink() {
        return _suppressDropDBLink;
    }

    public void setSuppressDropDBLink(boolean suppressDropDBLink) {
        this._suppressDropDBLink = suppressDropDBLink;
    }

    public boolean isSuppressLoggingSql() {
        return _suppressLoggingSql;
    }

    public void setSuppressLoggingSql(boolean suppressLoggingSql) {
        this._suppressLoggingSql = suppressLoggingSql;
    }
}