package com.example.dbflute.spring.dbflute.vendor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.util.DfTypeUtil;

import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberStatusCB;
import com.example.dbflute.spring.dbflute.cbean.cq.MemberStatusCQ;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.6.1 (2009/11/17 Tuesday)
 */
public class VendorPlainTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                             BC Date
    //                                                                             =======
    public void test_BC_date() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setBirthdate_IsNotNull();
        cb.fetchFirst(1);
        cb.addOrderBy_PK_Asc();
        Member member = memberBhv.selectEntity(cb);
        member.setBirthdate(DfTypeUtil.toSqlDate("BC1234/12/25"));

        // ## Act ##
        memberBhv.update(member);

        // ## Assert ##
        Member actual = memberBhv.selectByPKValue(member.getMemberId());
        DateFormat df = new SimpleDateFormat("Gyyyy/MM/dd");
        log(df.format(actual.getBirthdate()));
        assertTrue(DfTypeUtil.isDateBC(actual.getBirthdate()));
        assertTrue(df.format(actual.getBirthdate()).contains("1234/12/25"));
    }

    public void test_BC_datetime() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setFormalizedDatetime_IsNotNull();
        cb.fetchFirst(1);
        cb.addOrderBy_PK_Asc();
        Member member = memberBhv.selectEntity(cb);
        member.setFormalizedDatetime(DfTypeUtil.toTimestamp("BC1234/12/25 12:34:56.147"));

        // ## Act ##
        memberBhv.update(member);

        // ## Assert ##
        Member actual = memberBhv.selectByPKValue(member.getMemberId());
        DateFormat df = new SimpleDateFormat("Gyyyy/MM/dd HH:mm:dd.SSS");
        log(df.format(actual.getFormalizedDatetime()));
        assertTrue(DfTypeUtil.isDateBC(actual.getFormalizedDatetime()));
        assertTrue(df.format(actual.getFormalizedDatetime()).contains("1234/12/25"));
    }

    public void test_BC_test_precondition_also_JDK_test() {
        // ## Arrange ##
        String beforeExp = "BC0001/12/31 23:59:59.999";
        String afterExp = "0001/01/01 00:00:00.000";

        // ## Act ##
        Date before = DfTypeUtil.toDate(beforeExp);
        Date after = DfTypeUtil.toDate(afterExp);
        int beforeEra = DfTypeUtil.toCalendar(before).get(Calendar.ERA);
        int afterEra = DfTypeUtil.toCalendar(after).get(Calendar.ERA);

        // ## Assert ##
        log("before time = " + before.getTime());
        log("after  time = " + after.getTime());
        log("before era  = " + beforeEra);
        log("after  era  = " + afterEra);
        assertEquals(GregorianCalendar.BC, beforeEra);
        assertEquals(GregorianCalendar.AD, afterEra);
        assertTrue("before=" + before.getTime(), DfTypeUtil.isDateBC(before));
        assertFalse("after=" + after.getTime(), DfTypeUtil.isDateBC(after));
        DfTypeUtil.addDateMillisecond(before, 1);
        assertEquals(after.getTime(), before.getTime());
    }

    // ===================================================================================
    //                                                                          Short Char
    //                                                                          ==========
    public void test_shortChar_inout_trimmed_value() {
        // *This test does not depend on shortCharHandlingMode of DBFlute 
        // ## Arrange ##
        String code = "AB";
        String name = "ShortTest";
        Integer order = 96473;
        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStatusCode(code); // char
        memberStatus.setMemberStatusName(name); // varchar
        memberStatus.setDescription("foo");
        memberStatus.setDisplayOrder(order);
        memberStatusBhv.insert(memberStatus);

        MemberStatusCB cb = new MemberStatusCB();
        cb.query().setMemberStatusCode_Equal(code + " ");

        // ## Act ##
        MemberStatus actual = memberStatusBhv.selectEntityWithDeletedCheck(cb);

        // ## Assert ##
        assertEquals(code, actual.getMemberStatusCode()); // DB trims it
        assertEquals(name, actual.getMemberStatusName());
    }

    public void test_shortChar_inout_filled_value() {
        // *This test does not depend on shortCharHandlingMode of DBFlute 
        // ## Arrange ##
        String code = "AB ";
        String name = "ShortTest";
        Integer order = 96473;
        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStatusCode(code); // char
        memberStatus.setMemberStatusName(name); // varchar
        memberStatus.setDescription("foo");
        memberStatus.setDisplayOrder(order);
        memberStatusBhv.insert(memberStatus);

        MemberStatusCB cb = new MemberStatusCB();
        cb.query().setMemberStatusCode_Equal(code);

        // ## Act ##
        MemberStatus actual = memberStatusBhv.selectEntityWithDeletedCheck(cb);

        // ## Assert ##
        assertEquals(code.trim(), actual.getMemberStatusCode()); // DB trims it
        assertEquals(name, actual.getMemberStatusName());
    }

    public void test_shortChar_condition() {
        // *This test does not depend on shortCharHandlingMode of DBFlute 
        // ## Arrange ##
        String code = "AB ";
        String name = "ShortTest";
        Integer order = 96473; // should be unique order
        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStatusCode(code); // char
        memberStatus.setMemberStatusName(name); // varchar
        memberStatus.setDescription("foo");
        memberStatus.setDisplayOrder(order);
        memberStatusBhv.insert(memberStatus);

        MemberStatusCB cb = new MemberStatusCB() {
            // internal manipulation (Don't mimic it)
            @Override
            protected MemberStatusCQ xcreateCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName,
                    int nestLevel) {
                return new MemberStatusCQ(childQuery, sqlClause, aliasName, nestLevel) {
                    @Override
                    protected String hSC(String propertyName, String value, Integer size, String modeCode) {
                        return value; // do nothing for not depending on shortCharHandlingMode
                    }
                };
            }
        };
        cb.query().setMemberStatusCode_Equal(code.trim());
        assertTrue(cb.toDisplaySql().contains("'AB'"));

        // ## Act ##
        MemberStatus actual = memberStatusBhv.selectEntityWithDeletedCheck(cb);

        // ## Assert ##
        assertEquals(code.trim(), actual.getMemberStatusCode());
        assertEquals(name, actual.getMemberStatusName());
    }

    // ===================================================================================
    //                                                                  Plain ENUM Binding
    //                                                                  ==================
    public void test_plain_enum_binding() throws SQLException {
        // ## Arrange ##
        List<Integer> prepared = executeEnumBindSql(CDef.MemberStatus.Formalized.code());
        assertFalse(prepared.isEmpty());
        log(prepared);

        // ## Act ##
        try {
            executeEnumBindSql(TestPlainStatus.FML);

            // ## Assert ##
            fail();
        } catch (SQLException e) {
            // OK
            log(e.getMessage());
        }
    }

    private List<Integer> executeEnumBindSql(Object value) throws SQLException {
        DataSource ds = getDataSource();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ds.getConnection();
            String sql = "select * from MEMBER where MEMBER_STATUS_CODE = ?";
            log(sql);
            ps = conn.prepareStatement(sql);
            ps.setObject(1, value);
            rs = ps.executeQuery();
            List<Integer> idList = new ArrayList<Integer>();
            while (rs.next()) {
                idList.add(rs.getInt("MEMBER_ID"));
            }
            return idList;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static enum TestPlainStatus {
        FML, PRV, WDL
    }

    // ===================================================================================
    //                                                                     Cursor Handling
    //                                                                     ===============
    public void test_insert_in_plainCursor() throws Exception {
        // ## Arrange ##
        DataSource ds = getDataSource();
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps4insert = null;
        ResultSet rs = null;
        try {
            // ## Act ##
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String selectSql = "select * from MEMBER_STATUS";
            log(selectSql);
            ps = conn.prepareStatement(selectSql);
            rs = ps.executeQuery();
            assertTrue(rs.next());
            assertNotNull(rs.getString("MEMBER_STATUS_NAME"));
            String insertSql = "insert into MEMBER_STATUS values('FOO', 'BAR', 'DES', 999)";
            log(insertSql);
            ps4insert = conn.prepareStatement(insertSql);
            ps4insert.executeUpdate();

            // ## Assert ##
            assertTrue(rs.next());
            assertNotNull(rs.getString("MEMBER_STATUS_NAME"));
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (ps4insert != null) {
                ps4insert.close();
            }
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
    }

    // ===================================================================================
    //                                                                   PreparedStatement
    //                                                                   =================
    public void test_PreparedStatement_insert() throws Exception {
        // ## Arrange ##
        DataSource ds = getDataSource();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // ## Act ##
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder();
            sb.append("insert into MEMBER");
            sb.append(" (MEMBER_NAME, MEMBER_ACCOUNT, MEMBER_STATUS_CODE");
            sb.append(", FORMALIZED_DATETIME, BIRTHDATE");
            sb.append(", REGISTER_DATETIME, REGISTER_USER");
            sb.append(", UPDATE_DATETIME, UPDATE_USER, VERSION_NO)");
            sb.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            String sql = sb.toString();
            log(sql);
            ps = conn.prepareStatement(sql);
            ps.setString(1, "JFlute");
            ps.setString(2, "jflute");
            ps.setString(3, "FML");
            ps.setTimestamp(4, DfTypeUtil.toTimestamp(currentTimestamp()));
            ps.setDate(5, DfTypeUtil.toSqlDate("1965/03/03"));
            ps.setTimestamp(6, DfTypeUtil.toTimestamp(currentTimestamp()));
            ps.setString(7, "foo");
            ps.setTimestamp(8, DfTypeUtil.toTimestamp(currentTimestamp()));
            ps.setString(9, "foo");
            ps.setInt(10, 3);

            // ## Assert ##
            ps.execute(); // expect no exception
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
    }

    // ===================================================================================
    //                                                                           ResultSet
    //                                                                           =========
    public void test_ResultSet_sameColumn_twiceGetting() throws SQLException {
        String sql = "select MEMBER_ID, MEMBER_NAME from MEMBER";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getDataSource().getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            boolean exists = false;
            while (rs.next()) {
                exists = true;
                String first = rs.getString("MEMBER_NAME");
                String second = rs.getString("MEMBER_NAME");
                assertEquals(first, second);
            }
            assertTrue(exists);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void test_ResultSet_defaultResultSetType() throws SQLException {
        String sql = "select * from VENDOR_CHECK";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getDataSource().getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            int resultSetType = rs.getType();
            log("/* * * * * * * * * * * * * * * * * *");
            if (resultSetType == ResultSet.TYPE_FORWARD_ONLY) {
                log("resultSetType=TYPE_FORWARD_ONLY");
            } else if (resultSetType == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                log("resultSetType=TYPE_SCROLL_INSENSITIVE");
            } else if (resultSetType == ResultSet.TYPE_SCROLL_SENSITIVE) {
                log("resultSetType=TYPE_SCROLL_SENSITIVE");
            } else {
                log("resultSetType=UNKNOWN:" + resultSetType);
            }
            log("* * * * * * * * * */");
            assertEquals(ResultSet.TYPE_FORWARD_ONLY, resultSetType);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
