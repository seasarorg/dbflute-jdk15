package org.seasar.dbflute.helper.dataset.writers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.engine.database.model.UnifiedSchema;
import org.seasar.dbflute.helper.dataset.DfDataTable;

/**
 * {Created with reference to S2Container's utility and extended for DBFlute}
 * @author jflute
 * @since 0.8.3 (2008/10/28 Tuesday)
 */
public class DfDtsSQLServerSqlTableWriter extends DfDtsSqlTableWriter {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Log instance. */
    private static final Log _log = LogFactory.getLog(DfDtsSQLServerSqlTableWriter.class);

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfDtsSQLServerSqlTableWriter(final DataSource dataSource, UnifiedSchema unifiedSchema) {
        super(dataSource, unifiedSchema);
    }

    // ===================================================================================
    //                                                                       Main Override
    //                                                                       =============
    protected void doWrite(final DfDataTable dataTable) {
        boolean hasIdentity = hasIdentityColumn(dataTable);
        if (hasIdentity) {
            turnOnIdentityInsert(dataTable);
        }
        super.doWrite(dataTable);
        if (hasIdentity) {
            turnOffIdentityInsert(dataTable);
        }
    }

    private void turnOnIdentityInsert(final DfDataTable dataTable) {
        setIdentityInsert(dataTable, "ON");
    }

    private void turnOffIdentityInsert(final DfDataTable dataTable) {
        setIdentityInsert(dataTable, "OFF");
    }

    private void setIdentityInsert(final DfDataTable dataTable, final String command) {
        final String sql = "SET IDENTITY_INSERT " + dataTable.getTableDbName() + " " + command;
        if (_log.isDebugEnabled()) {
            _log.debug(sql);
        }
        final Connection conn = getConnection(getDataSource());
        try {
            final Statement stmt = createStatement(conn);
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        } finally {
            close(conn);
        }
    }

    private boolean hasIdentityColumn(final DfDataTable dataTable) {
        final String sql = "SELECT IDENT_CURRENT ('" + dataTable.getTableDbName() + "') AS IDENT_CURRENT";
        final Connection conn = getConnection(getDataSource());
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                final Object value = rs.getObject(1);
                return value != null;
            }
            return true;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignored) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static Connection getConnection(DataSource dataSource) {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Statement createStatement(Connection conn) {
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void close(Connection conn) {
        if (conn == null)
            return;
        try {
            conn.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}