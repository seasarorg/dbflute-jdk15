package com.example.dbflute.spring.dbflute.whitebox.allcommon;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.exception.IllegalDBFluteConfigAccessException;
import org.seasar.dbflute.exception.InvalidQueryRegisteredException;
import org.seasar.dbflute.jdbc.StatementConfig;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.allcommon.DBFluteConfig;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.SimpleMemberPmb;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 */
public class WxDBFluteConfigTest extends AppContainerTestCase {

    protected MemberBhv memberBhv;

    // ===================================================================================
    //                                                                              Set up
    //                                                                              ======
    @Override
    public void setUp() throws Exception {
        DBFluteConfig.getInstance().unlock();
        final StatementConfig config = new StatementConfig();
        DBFluteConfig.getInstance().setEmptyStringQueryAllowed(true);
        DBFluteConfig.getInstance().setEmptyStringParameterAllowed(true);
        DBFluteConfig.getInstance().setInvalidQueryChecked(true);
        config.typeForwardOnly().queryTimeout(10).fetchSize(7).maxRows(3);
        DBFluteConfig.getInstance().setDefaultStatementConfig(config);
        DBFluteConfig.getInstance().setInternalDebug(true);

        // normally you don't need to lock
        // because the initializer of DBFlute locks when initializing
        // but a container instance is recycled in this project's test cases
        // so it locks here (for a test case special reason)
        DBFluteConfig.getInstance().lock();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DBFluteConfig.getInstance().unlock();
        DBFluteConfig.getInstance().setEmptyStringQueryAllowed(false);
        DBFluteConfig.getInstance().setEmptyStringParameterAllowed(false);
        DBFluteConfig.getInstance().setInvalidQueryChecked(false);
        DBFluteConfig.getInstance().setDefaultStatementConfig(null);
        DBFluteConfig.getInstance().setInternalDebug(false);
        DBFluteConfig.getInstance().lock();
    }

    // ===================================================================================
    //                                                                       Invalid Query
    //                                                                       =============
    public void test_invalidQuery_emptyStringQueryAllowed_basic() throws Exception {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.checkInvalidQuery();

        // ## Act ##
        cb.query().setMemberName_Equal(""); // expect no exception

        // ## Assert ##
        assertTrue(Srl.contains(cb.toDisplaySql(), "MEMBER_NAME = ''"));
        cb.allowEmptyStringQuery(); // expect no exception
    }

    public void test_invalidQuery_emptyStringParameterAllowed_basic() throws Exception {
        // ## Arrange ##
        SimpleMemberPmb pmb = new SimpleMemberPmb();

        // ## Act ##
        pmb.setMemberName_PrefixSearch("");
        String memberName = pmb.getMemberName();

        // ## Assert ##
        assertEquals("", memberName);
    }

    public void test_invalidQuery_invalidQueryChecked_basic() throws Exception {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            cb.query().setMemberName_Equal(null);

            // ## Assert ##
            fail();
        } catch (InvalidQueryRegisteredException e) {
            // OK
            log(e.getMessage());
        }
        cb.checkInvalidQuery(); // expect no exception
    }

    // ===================================================================================
    //                                                                     StatementConfig
    //                                                                     ===============
    public void test_ConditionBean_configure_Config_is_Request() throws Exception {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final StatementConfig statementConfig = new StatementConfig();
        statementConfig.typeScrollSensitive().fetchSize(123).maxRows(1);
        cb.configure(statementConfig);

        // ## Act ##
        final ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertEquals(1, memberList.size()); // should be overridden
    }

    public void test_StatementConfig_check_insert_update_delete() throws Exception {
        // ## Arrange ##
        Member member = new Member();
        member.setMemberName("testName");
        member.setMemberAccount("testAccount");
        member.setMemberStatusCode_Formalized();

        // ## Act & Assert ##
        // expect no exception
        memberBhv.insert(member);
        memberBhv.updateNonstrict(member);
        memberBhv.deleteNonstrict(member);
    }

    // ===================================================================================
    //                                                                          Log Format
    //                                                                          ==========
    public void test_LogDateFormat_basic() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 5, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 123);
        MemberCB cb = new MemberCB();
        cb.query().setBirthdate_GreaterEqual(new Date(cal.getTimeInMillis()));
        String beforeSql = cb.toDisplaySql();
        log(beforeSql);
        assertTrue(beforeSql.contains("'2008-06-15'"));
        try {
            DBFluteConfig.getInstance().unlock();
            DBFluteConfig.getInstance().setLogDateFormat("yyyy/MM/dd HH$mm$ss.SSS");
            // ## Act & Assert ##
            String sql = cb.toDisplaySql();
            log(sql);
            assertTrue("sql:\n" + sql, sql.contains("'2008/06/15 12$34$56.123'"));
        } finally {
            DBFluteConfig.getInstance().setLogDateFormat(null);
            DBFluteConfig.getInstance().lock();
        }
    }

    public void test_LogDateFormat_prefixSuffix() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 5, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 123);
        MemberCB cb = new MemberCB();
        cb.query().setBirthdate_GreaterEqual(new Date(cal.getTimeInMillis()));
        String beforeSql = cb.toDisplaySql();
        log(beforeSql);
        assertTrue(beforeSql.contains("'2008-06-15'"));
        try {
            DBFluteConfig.getInstance().unlock();
            DBFluteConfig.getInstance().setLogDateFormat("date $df:{yyyy/MM/dd HH$mm$ss.SSS}");
            // ## Act & Assert ##
            String sql = cb.toDisplaySql();
            log(sql);
            assertTrue("sql:\n" + sql, sql.contains("date '2008/06/15 12$34$56.123'"));
        } finally {
            DBFluteConfig.getInstance().setLogDateFormat(null);
            DBFluteConfig.getInstance().lock();
        }
    }

    public void test_LogTimestampFormat_basic() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 5, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 123);
        MemberCB cb = new MemberCB();
        cb.query().setRegisterDatetime_GreaterEqual(new Timestamp(cal.getTimeInMillis()));
        String beforeSql = cb.toDisplaySql();
        log(beforeSql);
        assertTrue(beforeSql.contains("'2008-06-15 12:34:56.123'"));
        try {
            DBFluteConfig.getInstance().unlock();
            DBFluteConfig.getInstance().setLogTimestampFormat("yyyy/MM/dd HH-mm-ss.SSS");
            // ## Act & Assert ##
            String sql = cb.toDisplaySql();
            log(sql);
            assertTrue(sql.contains("'2008/06/15 12-34-56.123'"));
        } finally {
            DBFluteConfig.getInstance().setLogTimestampFormat(null);
            DBFluteConfig.getInstance().lock();
        }
    }

    public void test_LogTimestampFormat_prefixSuffix() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 5, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 123);
        MemberCB cb = new MemberCB();
        cb.query().setRegisterDatetime_GreaterEqual(new Timestamp(cal.getTimeInMillis()));
        String beforeSql = cb.toDisplaySql();
        log(beforeSql);
        assertTrue(beforeSql.contains("'2008-06-15 12:34:56.123'"));
        try {
            DBFluteConfig.getInstance().unlock();
            DBFluteConfig.getInstance().setLogTimestampFormat("timestamp $df:{yyyy/MM/dd HH-mm-ss.SSS}");
            // ## Act & Assert ##
            String sql = cb.toDisplaySql();
            log(sql);
            assertTrue(sql.contains("timestamp '2008/06/15 12-34-56.123'"));
        } finally {
            DBFluteConfig.getInstance().setLogTimestampFormat(null);
            DBFluteConfig.getInstance().lock();
        }
    }

    // ===================================================================================
    //                                                                                Lock
    //                                                                                ====
    public void test_always_locked() {
        assertTrue(DBFluteConfig.getInstance().isLocked());
    }

    public void test_locked_setting() {
        // ## Arrange & Act ##
        try {
            DBFluteConfig.getInstance().setLogDateFormat(null);

            // ## Assert ##
            fail();
        } catch (IllegalDBFluteConfigAccessException e) {
            // OK
            log(e.getMessage());
        }
    }
}
