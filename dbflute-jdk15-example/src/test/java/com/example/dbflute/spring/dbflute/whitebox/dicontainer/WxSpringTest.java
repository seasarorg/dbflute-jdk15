package com.example.dbflute.spring.dbflute.whitebox.dicontainer;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.seasar.dbflute.exception.SQLFailureException;
import org.seasar.dbflute.jdbc.DataSourceHandler;
import org.seasar.dbflute.jdbc.NotClosingConnectionWrapper;
import org.seasar.dbflute.jdbc.PhysicalConnectionDigger;
import org.seasar.dbflute.jdbc.SQLExceptionDigger;
import org.seasar.dbflute.util.DfReflectionUtil;

import com.example.dbflute.spring.dbflute.allcommon.DBFluteConfig;
import com.example.dbflute.spring.dbflute.allcommon.DBFluteInitializer;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.5 (2009/04/08 Wednesday)
 */
public class WxSpringTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    //private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                  DBFluteInitializer
    //                                                                  ==================
    public void test_DBFluteInitializer_initializing() {
        // ## Arrange ##
        final Set<String> markSet = new HashSet<String>();

        final DBFluteConfig config = DBFluteConfig.getInstance();
        final DataSourceHandler dataSourceHandler = config.getDataSourceHandler();
        config.unlock();

        try {
            // ## Act ##
            new DBFluteInitializer(getDataSource()) {
                @Override
                protected void setupDataSourceHandler(String dataSourceFqcn) {
                    markSet.add("setupDataSourceHandler");
                    super.setupDataSourceHandler(dataSourceFqcn);
                }
            };

            // ## Assert ##
            assertTrue(config.isLocked());
            assertTrue(markSet.contains("setupDataSourceHandler"));
        } finally {
            config.unlock();
            config.setDataSourceHandler(dataSourceHandler);
            config.lock();
        }
    }

    // ===================================================================================
    //                                                                          DataSource
    //                                                                          ==========
    public void test_DataSource_Connection_always_close() throws SQLException {
        Connection conn = null;
        DataSource dataSource = getDataSource();

        conn = dataSource.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from MEMBER");
            ps.execute();
            ps.close();
        } finally {
            conn.close();
        }
        conn = dataSource.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("update MEMBER set MEMBER_NAME = ? where MEMBER_ID = 3");
            ps.setString(1, "aaa");
            ps.execute();
            ps.close();
        } finally {
            conn.close();
        }
        conn = dataSource.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from MEMBER");
            ps.execute();
            ps.close();
        } finally {
            conn.close();
        }
    }

    public void test_DataSource_same_Connection() throws SQLException {
        Connection conn = null;
        DataSource dataSource = getDataSource();

        conn = dataSource.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("select * from MEMBER");
            ps.execute();
            ps.close();
            ps = conn.prepareStatement("update MEMBER set MEMBER_NAME = ? where MEMBER_ID = 3");
            ps.setString(1, "aaa");
            ps.execute();
            ps.close();
            ps = conn.prepareStatement("select * from MEMBER");
            ps.execute();
            ps.close();
        } finally {
            conn.close();
        }
    }

    public void test_DataSource_getPhysicalConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = getDataSource().getConnection();
            Connection actual = ((NotClosingConnectionWrapper) conn).getActualConnection();
            Field delegateField = DfReflectionUtil.getWholeField(actual.getClass(), "delegate");
            assertNotNull(delegateField);
            Connection delegated = (Connection) DfReflectionUtil.getValueForcedly(delegateField, actual);
            Field pooledField = DfReflectionUtil.getWholeField(delegated.getClass(), "_conn");
            assertNotNull(pooledField);
            Connection pooled = (Connection) DfReflectionUtil.getValueForcedly(pooledField, delegated);
            log("/= = = = = = = = = = = = = = = = = = = = = = = = ");
            log("actualConnection    = " + actual.getClass());
            log("delegatedConnection = " + delegated.getClass());
            log("pooledConnection = " + pooled.getClass());
            PhysicalConnectionDigger digger = DBFluteConfig.getInstance().getPhysicalConnectionDigger();
            Connection digged = digger.digUp(conn);
            log("diggedConnection = " + pooled.getClass());
            log("= = = = = = = = = =/");
            assertEquals(pooled.getClass(), digged.getClass());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    // ===================================================================================
    //                                                                        SQLException
    //                                                                        ============
    public void test_SQLExceptionDigger_digUp() {
        // ## Arrange ##
        SQLExceptionDigger digger = DBFluteConfig.getInstance().getSQLExceptionDigger();
        assertNotNull(digger);

        // ## Act & Assert ##
        // do nothing when Spring
        assertNull(digger.digUp(new Exception()));
        assertNull(digger.digUp(new RuntimeException()));
        assertNotNull(digger.digUp(new RuntimeException(new SQLException())));
        assertNotNull(digger.digUp(new SQLFailureException("msg", new SQLException())));
        assertNull(digger.digUp(new SQLException()));
    }
}
