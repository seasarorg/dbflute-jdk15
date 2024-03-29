package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.cbean.coption.FromToOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.cbean.coption.RangeOfOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.exception.ConditionInvokingFailureException;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;

import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberStatusDbm;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberWithdrawalDbm;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.WithdrawalReasonDbm;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.cq.MemberStatusCQ;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.6.6 (2010/03/08 Monday)
 */
public class WxCQInvokeQueryTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                       invokeQuery()
    //                                                                       =============
    // -----------------------------------------------------
    //                                                 Basic
    //                                                 -----
    public void test_invokeQuery_basic() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnMemberId().getColumnDbName();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        cb.localCQ().invokeQuery(columnDbName, keyName, 3);
        Entity entity = memberBhv.readEntityWithDeletedCheck(cb);

        // ## Assert ##
        log(entity);
        assertTrue(entity instanceof Member);
        assertEquals(3, ((Member) entity).getMemberId().intValue());
    }

    // -----------------------------------------------------
    //                                                String
    //                                                ------
    public void test_invokeQuery_String() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnMemberName().getColumnDbName();

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_EQUAL.getConditionKey(), "foo");
        cb.localCQ().invokeQuery(name, ConditionKey.CK_NOT_EQUAL_STANDARD.getConditionKey(), "foo");
        cb.localCQ().invokeQuery(name, ConditionKey.CK_LIKE_SEARCH.getConditionKey(), "foo",
                new LikeSearchOption().likePrefix());

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" = 'foo'"));
        assertTrue(sql.contains(" <> 'foo'"));
        assertTrue(sql.contains(" like 'foo%'"));
    }

    public void test_invokeQuery_String_inScope() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String propertyName = MemberDbm.getInstance().columnMemberName().getPropertyName();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");

        // ## Act ##
        cb.query().invokeQuery(propertyName, "inScope", list);

        // ## Assert ##
        final ConditionValue value = cb.query().getMemberName();
        log("conditionValue=" + value);
        assertEquals(list, value.getVarying().get("inScope").get("inScope0"));
    }

    // -----------------------------------------------------
    //                                                FromTo
    //                                                ------
    public void test_invokeQuery_FromTo() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnBirthdate().getColumnDbName();
        Date date = DfTypeUtil.toDate("2011/08/23");

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_EQUAL.getConditionKey(), date);
        List<Date> bothList = DfCollectionUtil.newArrayList(date, date);
        List<Date> fromOnlyList = DfCollectionUtil.newArrayList(date, null);
        List<Date> toOnlyList = DfCollectionUtil.newArrayList(null, date);
        List<Date> nullNullList = DfCollectionUtil.newArrayList(null, null);
        cb.localCQ().invokeQuery(name, "FromTo", bothList, new FromToOption().compareAsDate());
        cb.localCQ().invokeQuery(name, "FromTo", fromOnlyList, new FromToOption().compareAsDate());
        cb.localCQ().invokeQuery(name, "FromTo", toOnlyList, new FromToOption().compareAsDate());
        cb.localCQ().invokeQuery(name, "FromTo", nullNullList, new FromToOption().compareAsDate());

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" = '2011-08-23'"));
        assertTrue(sql.contains(" >= '2011-08-23'"));
        assertTrue(sql.contains(" < '2011-08-24'"));
    }

    public void test_invokeQuery_DateFromTo() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnBirthdate().getColumnDbName();
        Date date = DfTypeUtil.toDate("2011/08/23");

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_EQUAL.getConditionKey(), date);
        List<Date> bothList = newArrayList(date, date);
        List<Date> fromOnlyList = newArrayList(date, null);
        List<Date> toOnlyList = newArrayList(null, date);
        List<Date> nullNullList = newArrayList(null, null);
        cb.localCQ().invokeQuery(name, "DateFromTo", bothList);
        cb.localCQ().invokeQuery(name, "DateFromTo", fromOnlyList);
        cb.localCQ().invokeQuery(name, "DateFromTo", toOnlyList);
        cb.localCQ().invokeQuery(name, "DateFromTo", nullNullList);

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" = '2011-08-23'"));
        assertTrue(sql.contains(" >= '2011-08-23'"));
        assertTrue(sql.contains(" < '2011-08-24'"));
    }

    // -----------------------------------------------------
    //                                               RangeOf
    //                                               -------
    public void test_invokeQuery_RangeOf() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnMemberId().getColumnDbName();
        Integer min = 10;
        Integer max = 20;

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_EQUAL.getConditionKey(), min);
        List<Integer> bothList = newArrayList(min, max);
        List<Integer> fromOnlyList = newArrayList(min, null);
        List<Integer> toOnlyList = newArrayList(null, max);
        List<Integer> nullNullList = newArrayList(null, null);
        cb.localCQ().invokeQuery(name, "RangeOf", bothList, new RangeOfOption());
        cb.localCQ().invokeQuery(name, "RangeOf", fromOnlyList, new RangeOfOption().greaterThan());
        cb.localCQ().invokeQuery(name, "RangeOf", toOnlyList, new RangeOfOption().lessThan());
        cb.localCQ().invokeQuery(name, "RangeOf", nullNullList, new RangeOfOption());

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" = 10"));
        assertTrue(sql.contains(" >= 10"));
        assertTrue(sql.contains(" <= 20"));
        assertTrue(sql.contains(" > 10"));
        assertTrue(sql.contains(" < 20"));
    }

    // -----------------------------------------------------
    //                                                IsNull
    //                                                ------
    public void test_invokeQuery_isNull_isNotNull_basic() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnBirthdate().getColumnDbName();

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_IS_NULL.getConditionKey(), new Object());
        cb.localCQ().invokeQuery(name, ConditionKey.CK_IS_NOT_NULL.getConditionKey(), new Object());

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" is null"));
        assertTrue(sql.contains(" is not null"));
    }

    public void test_invokeQuery_isNull_isNotNull_null() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnBirthdate().getColumnDbName();

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_IS_NULL.getConditionKey(), null);
        cb.localCQ().invokeQuery(name, ConditionKey.CK_IS_NOT_NULL.getConditionKey(), null);

        // ## Assert ##
        assertFalse(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertFalse(sql.contains(" is null"));
        assertFalse(sql.contains(" is not null"));
    }

    // -----------------------------------------------------
    //                                           EmptyString
    //                                           -----------
    public void test_invokeQuery_emptyString() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnMemberName().getColumnDbName();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        cb.localCQ().invokeQuery(columnDbName, keyName, "");

        // ## Assert ##
        log(cb.toDisplaySql());
        assertNull(cb.localCQ().invokeValue(columnDbName).getFixed());
        assertFalse(cb.toDisplaySql().contains("where "));
    }

    // -----------------------------------------------------
    //                                         Null Argument
    //                                         -------------
    // null has special meaning (no invoking) 
    public void test_invokeQuery_null() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnMemberName().getColumnDbName();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        cb.localCQ().invokeQuery(columnDbName, keyName, null);

        // ## Assert ##
        log(ln() + cb.toDisplaySql());
        assertNull(cb.localCQ().invokeValue(columnDbName).getFixed());
        assertFalse(cb.toDisplaySql().contains("where "));

        // ## Act ##
        cb.localCQ().invokeQuery(columnDbName, keyName, "foo");
        cb.localCQ().invokeQuery(columnDbName, keyName, null);

        // ## Assert ##
        log(ln() + cb.toDisplaySql());
        assertNotNull(cb.localCQ().invokeValue(columnDbName).getFixed());
        assertTrue(cb.toDisplaySql().contains("where "));
    }

    // -----------------------------------------------------
    //                                           Column Name
    //                                           -----------
    public void test_invokeQuery_by_propertyName() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String propertyName = MemberDbm.getInstance().columnMemberName().getPropertyName();

        // ## Act ##
        cb.query().invokeQuery(propertyName, "Equal", "testValue");

        // ## Assert ##
        final ConditionValue value = cb.query().getMemberName();
        log("conditionValue=" + value);
        assertEquals("testValue", value.getFixedQuery().get("equal"));
    }

    public void test_invokeQuery_by_columnName() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String propertyName = MemberDbm.getInstance().columnMemberName().getColumnDbName();

        // ## Act ##
        cb.query().invokeQuery(propertyName, "Equal", "testValue");

        // ## Assert ##
        final ConditionValue value = cb.query().getMemberName();
        log("conditionValue=" + value);
        assertEquals("testValue", value.getFixedQuery().get("equal"));
    }

    // -----------------------------------------------------
    //                                              Relation
    //                                              --------
    public void test_invokeQuery_resolveRelation() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String foreingPropertyName = MemberDbm.getInstance().foreignMemberStatus().getForeignPropertyName();
        final String propertyName = MemberStatusDbm.getInstance().columnMemberStatusName().getPropertyName();

        // ## Act ##
        cb.query().invokeQuery(foreingPropertyName + "." + propertyName, "Equal", "testValue");

        // ## Assert ##
        final ConditionValue value = cb.query().queryMemberStatus().getMemberStatusName();
        log("conditionValue=" + value);
        assertEquals("testValue", value.getFixedQuery().get("equal"));
    }

    public void test_invokeQuery_resolveNestedRelation() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String targetName;
        {
            final String name1 = MemberDbm.getInstance().foreignMemberWithdrawalAsOne().getForeignPropertyName();
            final String name2 = MemberWithdrawalDbm.getInstance().foreignWithdrawalReason().getForeignPropertyName();
            final String prop = WithdrawalReasonDbm.getInstance().columnWithdrawalReasonText().getPropertyName();
            targetName = name1 + "." + name2 + "." + prop;
        }

        // ## Act ##
        cb.query().invokeQuery(targetName, "Equal", "testValue");

        // ## Assert ##
        final ConditionValue value = cb.query().queryMemberWithdrawalAsOne().queryWithdrawalReason()
                .getWithdrawalReasonText();
        log("conditionValue=" + value);
        assertEquals("testValue", value.getFixedQuery().get("equal"));
    }

    // -----------------------------------------------------
    //                                        Classification
    //                                        --------------
    public void test_invokeQuery_Classification() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String name = MemberDbm.getInstance().columnMemberStatusCode().getColumnDbName();

        // ## Act ##
        cb.localCQ().invokeQuery(name, ConditionKey.CK_EQUAL.getConditionKey(), CDef.MemberStatus.Formalized);

        // ## Assert ##
        assertTrue(cb.hasWhereClauseOnBaseQuery());
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains(" = 'FML'"));
    }

    // -----------------------------------------------------
    //                                               Illegal
    //                                               -------
    public void test_invokeQuery_IllegalColumn() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        try {
            cb.localCQ().invokeQuery("noexist", keyName, 3);

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
            log(e.getCause().getMessage());
        }
    }

    public void test_invokeQuery_IllegalKey() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnMemberId().getColumnDbName();

        // ## Act ##
        try {
            cb.localCQ().invokeQuery(columnDbName, "noexist", 3);

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
            assertNull(e.getCause());
        }
    }

    public void test_invokeQuery_IllegalValue_integer() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnMemberId().getColumnDbName();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        try {
            cb.localCQ().invokeQuery(columnDbName, keyName, "foo");

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
            log(e.getCause().getMessage());
        }
    }

    public void test_invokeQuery_IllegalValue_date() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        String columnDbName = MemberDbm.getInstance().columnBirthdate().getColumnDbName();
        String keyName = ConditionKey.CK_EQUAL.getConditionKey();

        // ## Act ##
        try {
            cb.localCQ().invokeQuery(columnDbName, keyName, "foo");

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
            log(e.getCause().getMessage());
        }
    }

    // ===================================================================================
    //                                                                     invokeOrderBy()
    //                                                                     ===============
    public void test_invokeOrderBy_by_propertyName() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String propertyName = MemberDbm.getInstance().columnMemberName().getPropertyName();
        final String columnName = MemberDbm.getInstance().columnMemberName().getColumnDbName();

        // ## Act ##
        cb.query().invokeOrderBy(propertyName, true);

        // ## Assert ##
        final String orderByClause = cb.query().xgetSqlClause().getOrderByClause();
        log("orderByClause=" + orderByClause);
        assertTrue(orderByClause.contains(columnName));
        assertTrue(orderByClause.contains("asc"));
    }

    public void test_invokeOrderBy_by_columnName() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String columnName = MemberDbm.getInstance().columnMemberName().getColumnDbName();

        // ## Act ##
        cb.query().invokeOrderBy(columnName, true);

        // ## Assert ##
        final String orderByClause = cb.query().xgetSqlClause().getOrderByClause();
        log("orderByClause=" + orderByClause);
        assertTrue(orderByClause.contains(columnName));
        assertTrue(orderByClause.contains("asc"));
    }

    public void test_invokeOrderBy_resolveRelation() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String foreingPropertyName = MemberDbm.getInstance().foreignMemberStatus().getForeignPropertyName();
        final String propertyName = MemberStatusDbm.getInstance().columnMemberStatusName().getPropertyName();
        final String columnName = MemberStatusDbm.getInstance().columnMemberStatusName().getColumnDbName();

        // ## Act ##
        cb.query().invokeOrderBy(foreingPropertyName + "." + propertyName, false);

        // ## Assert ##
        final String orderByClause = cb.query().xgetSqlClause().getOrderByClause();
        log("orderByClause=" + orderByClause);
        assertTrue(orderByClause.contains(columnName));
        assertTrue(orderByClause.contains("desc"));
    }

    public void test_invokeOrderBy_resolveNestedRelation() {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final String foreingPropertyName1 = MemberDbm.getInstance().foreignMemberWithdrawalAsOne()
                .getForeignPropertyName();
        final String foreingPropertyName2 = MemberWithdrawalDbm.getInstance().foreignWithdrawalReason()
                .getForeignPropertyName();
        final String propertyName = WithdrawalReasonDbm.getInstance().columnWithdrawalReasonText().getPropertyName();
        final String targetName = foreingPropertyName1 + "." + foreingPropertyName2 + "." + propertyName;
        final String columnName = WithdrawalReasonDbm.getInstance().columnWithdrawalReasonText().getColumnDbName();

        // ## Act ##
        cb.query().invokeOrderBy(targetName, false);

        // ## Assert ##
        final String orderByClause = cb.query().xgetSqlClause().getOrderByClause();
        log("orderByClause=" + orderByClause);
        assertTrue(orderByClause.contains(columnName));
        assertTrue(orderByClause.contains("desc"));
    }

    // ===================================================================================
    //                                                                   invokeForeignCQ()
    //                                                                   =================
    public void test_invokeForeignCQ_basic() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        MemberDbm dbm = MemberDbm.getInstance();
        String foreignPropertyName = dbm.foreignMemberStatus().getForeignPropertyName();

        // ## Act ##
        ConditionQuery foreignCQ = cb.localCQ().invokeForeignCQ(foreignPropertyName);

        // ## Assert ##
        log(foreignCQ);
        assertTrue(foreignCQ instanceof MemberStatusCQ);
    }

    public void test_invokeForeignCQ_bizOneToOne_hasParameter() {
        // ## Arrange ##
        ConditionBean cb = memberBhv.newConditionBean();
        MemberDbm dbm = MemberDbm.getInstance();
        String foreignPropertyName = dbm.foreignMemberAddressAsValid().getForeignPropertyName();

        // ## Act ##
        try {
            cb.localCQ().invokeForeignCQ(foreignPropertyName);

            // ## Assert ##
            fail("BizOneToOne that has parameters cannot be invoked");
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
        }
    }
}
