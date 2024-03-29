package com.example.dbflute.spring.dbflute.whitebox.outsidesql;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.jdbc.SqlLogHandler;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.twowaysql.exception.IfCommentMethodInvocationFailureException;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PmCommentCollectionPmb;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PmCommentEmbeddedPmb;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PmCommentOrderByIfPmb;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PmCommentPossiblePmb;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PurchaseMaxPriceMemberPmb;
import com.example.dbflute.spring.dbflute.exentity.customize.PmCommentCollection;
import com.example.dbflute.spring.dbflute.exentity.customize.PmCommentOrderByIf;
import com.example.dbflute.spring.dbflute.exentity.customize.PurchaseMaxPriceMember;
import com.example.dbflute.spring.dbflute.whitebox.outsidesql.WxOutsideSqlBasicTest.MySimpleMember;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.7.0 (2010/05/23 Sunday)
 */
public class WxParameterCommentTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                           Supported
    //                                                                           =========
    public void test_outsideSql_supportedExpression() {
        // ## Arrange ##
        String path = MemberBhv.PATH_whitebox_pmcomment_selectPmCommentPossible;
        PmCommentPossiblePmb pmb = new PmCommentPossiblePmb();
        pmb.setString("Pixy");
        pmb.setInteger(3);
        pmb.setBigDecimal(new BigDecimal("2.3"));
        pmb.setDate(DfTypeUtil.toDate("2009/11/22"));
        pmb.setTimestamp(DfTypeUtil.toTimestamp("2009/11/22"));
        pmb.setExists(true);
        pmb.setList(DfCollectionUtil.newArrayList("a", "b"));
        Map<String, Integer> map = DfCollectionUtil.newHashMap();
        map.put("fooKey", 99);
        pmb.setMap(map);
        pmb.setCdef(CDef.MemberStatus.Formalized);
        Class<MySimpleMember> entityType = MySimpleMember.class;

        // ## Act & Assert ##
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                assertTrue(displaySql.contains("where"));
                assertTrue(displaySql.contains("member.MEMBER_NAME"));
                assertTrue(displaySql.contains("and member.MEMBER_ACCOUNT like 'a'"));
                assertFalse(displaySql.contains("and member.MEMBER_ACCOUNT like 'b'")); // IF in FOR
                assertTrue(displaySql.contains("member.BIRTHDATE"));
                assertTrue(displaySql.contains("and member.MEMBER_STATUS_CODE"));
                assertTrue(displaySql.contains("order"));
                assertTrue(displaySql.contains("member.MEMBER_ID asc"));
            }
        });
        try {
            CallbackContext.setCallbackContextOnThread(context);
            memberBhv.outsideSql().selectList(path, pmb, entityType); // no exception
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    // ===================================================================================
    //                                                                         FOR Comment
    //                                                                         ===========
    public void test_outsideSql_forComment_secondCondition_toFirst() {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseMaxPriceMember;

        PurchaseMaxPriceMemberPmb pmb = new PurchaseMaxPriceMemberPmb();
        pmb.setMemberNameList_PrefixSearch(DfCollectionUtil.newArrayList("S", "M"));

        Class<PurchaseMaxPriceMember> entityType = PurchaseMaxPriceMember.class;

        // ## Act ##
        int pageSize = 3;
        pmb.paging(pageSize, 1); // 1st page
        PagingResultBean<PurchaseMaxPriceMember> page1 = memberBhv.outsideSql().manualPaging()
                .selectPage(path, pmb, entityType);

        // ## Assert ##
        assertNotSame(0, page1.size());
        for (PurchaseMaxPriceMember member : page1) {
            log(member);
            String memberName = member.getMemberName();

            if (!memberName.contains("S") && !memberName.contains("M")) {
                fail("memberName should have S or M: " + memberName);
            }
        }
    }

    public void test_outsideSql_forComment_secondCondition_toSecond() {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseMaxPriceMember;

        PurchaseMaxPriceMemberPmb pmb = new PurchaseMaxPriceMemberPmb();
        pmb.setMemberId(3);
        pmb.setMemberNameList_PrefixSearch(DfCollectionUtil.newArrayList("S", "M"));

        Class<PurchaseMaxPriceMember> entityType = PurchaseMaxPriceMember.class;

        // ## Act ##
        int pageSize = 3;
        pmb.paging(pageSize, 1); // 1st page
        PagingResultBean<PurchaseMaxPriceMember> page1 = memberBhv.outsideSql().manualPaging()
                .selectPage(path, pmb, entityType);

        // ## Assert ##
        assertNotSame(0, page1.size());
        for (PurchaseMaxPriceMember member : page1) {
            log(member);
            String memberName = member.getMemberName();

            if (!memberName.contains("S") && !memberName.contains("M")) {
                fail("memberName should have S or M: " + memberName);
            }
        }
    }

    public void test_outsideSql_forComment_formatSql() {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseMaxPriceMember;

        PurchaseMaxPriceMemberPmb pmb = new PurchaseMaxPriceMemberPmb();
        pmb.setMemberId(3);
        pmb.setMemberNameList_PrefixSearch(DfCollectionUtil.newArrayList("S", "M"));

        Class<PurchaseMaxPriceMember> entityType = PurchaseMaxPriceMember.class;

        // ## Act ##
        int pageSize = 3;
        pmb.paging(pageSize, 1); // 1st page
        PagingResultBean<PurchaseMaxPriceMember> page1 = memberBhv.outsideSql().formatSql().manualPaging()
                .selectPage(path, pmb, entityType);

        // ## Assert ##
        assertNotSame(0, page1.size());
        for (PurchaseMaxPriceMember member : page1) {
            log(member);
            String memberName = member.getMemberName();

            if (!memberName.contains("S") && !memberName.contains("M")) {
                fail("memberName should have S or M: " + memberName);
            }
        }
    }

    public void test_outsideSql_forComment_binding() {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseMaxPriceMember;
        PurchaseMaxPriceMemberPmb pmb = new PurchaseMaxPriceMemberPmb();
        pmb.setMemberNameList_PrefixSearch(DfCollectionUtil.newArrayList(";", "'"));
        Class<PurchaseMaxPriceMember> entityType = PurchaseMaxPriceMember.class;

        // ## Act & Assert ##
        int pageSize = 3;
        pmb.paging(pageSize, 1); // 1st page
        memberBhv.outsideSql().manualPaging().selectPage(path, pmb, entityType); // expect no exception
        pmb.setMemberNameList_PrefixSearch(DfCollectionUtil.newArrayList(";select * from PURCHASE", "''"));
        memberBhv.outsideSql().manualPaging().selectPage(path, pmb, entityType); // expect no exception
    }

    // ===================================================================================
    //                                                                   Embedded Variable
    //                                                                   =================
    public void test_EmbeddedVariable_noTestValue() throws Exception {
        // ## Arrange ##
        PmCommentEmbeddedPmb pmb = new PmCommentEmbeddedPmb();
        pmb.setSchema("public");
        pmb.setSchemaDot("public.");

        final Set<String> markSet = new HashSet<String>();
        CallbackContext.setSqlLogHandlerOnThread(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                // ## Assert ##
                markSet.add("handle");
                String displaySql = info.getDisplaySql();
                assertTrue(displaySql.contains("public."));
                assertEquals(2, Srl.count(displaySql, "public."));
            }
        });

        try {
            // ## Act ##
            memberBhv.outsideSql().selectList(pmb); // expect no exception
            assertTrue(markSet.contains("handle"));
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    // ===================================================================================
    //                                                                          Collection
    //                                                                          ==========
    // -----------------------------------------------------
    //                                               HashSet
    //                                               -------
    public void test_Collection_HashSet_basic() {
        // ## Arrange ##
        PmCommentCollectionPmb pmb = new PmCommentCollectionPmb();
        pmb.setStatusList(newHashSet(CDef.MemberStatus.Formalized, CDef.MemberStatus.Withdrawal));

        // ## Act ##
        ListResultBean<PmCommentCollection> selectList = memberBhv.outsideSql().selectList(pmb);

        // ## Assert ##
        assertListNotEmpty(selectList);
    }

    public void test_Collection_HashSet_emptyMethodCheck() {
        // ## Arrange ##
        PmCommentCollectionPmb pmb = new PmCommentCollectionPmb();
        pmb.setStatusList(newHashSet(CDef.MemberStatus.Formalized, CDef.MemberStatus.Withdrawal));
        pmb.setEmptyMethodCheck(true);

        // ## Act ##
        ListResultBean<PmCommentCollection> selectList = memberBhv.outsideSql().selectList(pmb);

        // ## Assert ##
        assertListNotEmpty(selectList);
    }

    // -----------------------------------------------------
    //                                               EnumSet
    //                                               -------
    public void test_Collection_EnumSet_basic() {
        // ## Arrange ##
        PmCommentCollectionPmb pmb = new PmCommentCollectionPmb();
        pmb.setStatusList(EnumSet.of(CDef.MemberStatus.Formalized, CDef.MemberStatus.Withdrawal));

        // ## Act ##
        ListResultBean<PmCommentCollection> selectList = memberBhv.outsideSql().selectList(pmb);

        // ## Assert ##
        assertListNotEmpty(selectList);
    }

    public void test_Collection_EnumSet_emptyMethodCheck() {
        // ## Arrange ##
        PmCommentCollectionPmb pmb = new PmCommentCollectionPmb();
        pmb.setStatusList(EnumSet.of(CDef.MemberStatus.Formalized, CDef.MemberStatus.Withdrawal));
        pmb.setEmptyMethodCheck(true);

        // ## Act ##
        try {
            memberBhv.outsideSql().selectList(pmb);

            // ## Assert ##
            fail();
        } catch (IfCommentMethodInvocationFailureException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                           OrderByIf
    //                                                                           =========
    public void test_OrderByIf_firstValid() throws Exception {
        // ## Arrange ##
        PmCommentOrderByIfPmb pmb = new PmCommentOrderByIfPmb();
        pmb.setOrderByMemberId(true);

        // ## Act ##
        ListResultBean<PmCommentOrderByIf> orderByIfList = memberBhv.outsideSql().selectList(pmb);

        // ## Assert ##
        assertListNotEmpty(orderByIfList);
        Integer previousMemberId = null;
        boolean exists = false;
        for (PmCommentOrderByIf orderByIf : orderByIfList) {
            Integer memberId = orderByIf.getMemberId();
            if (previousMemberId != null && previousMemberId >= memberId) {
                fail();
            } else {
                exists = true;
            }
            previousMemberId = memberId;
            log(memberId);
        }
        assertNotNull(previousMemberId); // just in case
        assertTrue(exists);
    }

    public void test_OrderByIf_secondValid() throws Exception {
        // ## Arrange ##
        PmCommentOrderByIfPmb pmb = new PmCommentOrderByIfPmb();
        pmb.setOrderByMemberAccount(true);

        // ## Act ##
        ListResultBean<PmCommentOrderByIf> orderByIfList = memberBhv.outsideSql().selectList(pmb);

        // ## Assert ##
        assertListNotEmpty(orderByIfList);
        String previousMemberAccount = null;
        boolean exists = false;
        for (PmCommentOrderByIf orderByIf : orderByIfList) {
            String memberAccount = orderByIf.getMemberAccount();
            if (previousMemberAccount != null && previousMemberAccount.compareTo(memberAccount) >= 0) {
                fail();
            } else {
                exists = true;
            }
            previousMemberAccount = memberAccount;
            log(memberAccount);
        }
        assertNotNull(previousMemberAccount); // just in case
        assertTrue(exists);
    }

    public void test_OrderByIf_bothValid() throws Exception {
        // ## Arrange ##
        PmCommentOrderByIfPmb pmb = new PmCommentOrderByIfPmb();
        pmb.setOrderByMemberAccount(true);
        pmb.setOrderByMemberId(true);

        // ## Act ##
        final List<String> sqlList = newArrayList();
        try {
            CallbackContext.setSqlLogHandlerOnThread(new SqlLogHandler() {
                public void handle(SqlLogInfo info) {
                    sqlList.add(info.getDisplaySql());
                }
            });
            memberBhv.outsideSql().selectList(pmb);
        } finally {
            CallbackContext.clearSqlLogHandlerOnThread();
        }

        // ## Assert ##
        assertListHasOneElement(sqlList);
        String displaySql = sqlList.get(0);
        assertTrue(displaySql.contains("order by"));
        assertTrue(displaySql.contains("member.MEMBER_ID asc"));
        assertTrue(displaySql.contains(", member.MEMBER_Account asc"));
    }

    public void test_OrderByIf_noOrder() throws Exception {
        // ## Arrange ##
        PmCommentOrderByIfPmb pmb = new PmCommentOrderByIfPmb();

        // ## Act ##
        final List<String> sqlList = newArrayList();
        try {
            CallbackContext.setSqlLogHandlerOnThread(new SqlLogHandler() {
                public void handle(SqlLogInfo info) {
                    sqlList.add(info.getDisplaySql());
                }
            });
            memberBhv.outsideSql().selectList(pmb);
        } finally {
            CallbackContext.clearSqlLogHandlerOnThread();
        }

        // ## Assert ##
        assertListHasOneElement(sqlList);
        String displaySql = sqlList.get(0);
        assertFalse(displaySql.contains("order by"));
        assertFalse(displaySql.contains("member.MEMBER_ID asc"));
        assertFalse(displaySql.contains(", member.MEMBER_Account asc"));
    }
}
