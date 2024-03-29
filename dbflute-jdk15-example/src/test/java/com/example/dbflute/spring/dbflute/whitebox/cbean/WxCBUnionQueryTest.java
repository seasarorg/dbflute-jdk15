package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.HashSet;
import java.util.Set;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.exception.OrderByIllegalPurposeException;
import org.seasar.dbflute.exception.SetupSelectIllegalPurposeException;
import org.seasar.dbflute.exception.SpecifyIllegalPurposeException;
import org.seasar.dbflute.jdbc.SqlLogHandler;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.SummaryWithdrawalDbm;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.cbean.SummaryWithdrawalCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.SummaryWithdrawalBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.SummaryWithdrawal;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 */
public class WxCBUnionQueryTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private SummaryWithdrawalBhv summaryWithdrawalBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_UnionQuery_union_basic() {
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().setMemberStatusCode_Equal_Provisional();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.query().setMemberName_PrefixSearch("S");
            }
        });
        cb.query().addOrderBy_MemberName_Desc();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        boolean existsProv = false;
        boolean existsStart = false;
        boolean existsBoth = false;
        final Set<String> memberSet = new HashSet<String>();
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            if (memberSet.contains(memberName)) {
                fail("Duplicate: " + memberName);
            }
            memberSet.add(memberName);
            String memberStatusName = member.getMemberStatus().getMemberStatusName();
            if (!memberName.startsWith("S")) {
                log("[Provisional]: " + memberName + ", " + memberStatusName);
                assertTrue(member.isMemberStatusCodeProvisional());
                existsProv = true;
            } else if (!member.isMemberStatusCodeProvisional()) {
                log("[Starts with S]: " + memberName + ", " + memberStatusName);
                assertTrue(memberName.startsWith("S"));
                existsStart = true;
            } else {
                log("[Both]: " + memberName + ", " + memberStatusName);
                existsBoth = true;
            }
        }
        assertTrue(existsProv);
        assertTrue(existsStart);
        assertTrue(existsBoth);
        assertTrue(cb.toDisplaySql().contains(" union"));
        assertFalse(cb.toDisplaySql().contains(" union all"));
    }

    public void test_UnionQuery_unionAll_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().setMemberStatusCode_Equal_Provisional();
        cb.unionAll(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.query().setMemberName_PrefixSearch("S");
            }
        });
        cb.query().addOrderBy_MemberName_Desc();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        boolean exsitsDuplicate = false;
        boolean existsProv = false;
        boolean existsStart = false;
        boolean existsBoth = false;
        final Set<String> memberSet = new HashSet<String>();
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            if (memberSet.contains(memberName)) {
                exsitsDuplicate = true;
            }
            memberSet.add(memberName);
            String memberStatusName = member.getMemberStatus().getMemberStatusName();
            if (!memberName.startsWith("S")) {
                log("[Provisional]: " + memberName + ", " + memberStatusName);
                assertTrue(member.isMemberStatusCodeProvisional());
                existsProv = true;
            } else if (!member.isMemberStatusCodeProvisional()) {
                log("[Starts with S]: " + memberName + ", " + memberStatusName);
                assertTrue(memberName.startsWith("S"));
                existsStart = true;
            } else {
                log("[Both]: " + memberName + ", " + memberStatusName);
                existsBoth = true;
            }
        }
        assertTrue(exsitsDuplicate);
        assertTrue(existsProv);
        assertTrue(existsStart);
        assertTrue(existsBoth);
        assertTrue(cb.toDisplaySql().contains(" union all"));
    }

    // ===================================================================================
    //                                                                       SpecifyColumn
    //                                                                       =============
    // implemented at SpecifyColumnTest
    //public void test_UnionQuery_with_SpecifyColumn_selectCount_basic() {
    //public void ...() {

    public void test_UnionQuery_with_SpecifyColumn_selectListAndPage_basic() throws Exception {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.specify().columnMemberName();
        cb.query().setMemberStatusCode_Equal_Provisional();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.query().setMemberName_PrefixSearch("S");
            }
        });
        cb.query().addOrderBy_MemberName_Desc();

        final String pkCol = MemberDbm.getInstance().columnMemberId().getColumnDbName();
        final String specifiedCol = MemberDbm.getInstance().columnMemberName().getColumnDbName();
        final String implicitCol = MemberDbm.getInstance().columnMemberStatusCode().getColumnDbName();
        final String notCol = MemberDbm.getInstance().columnMemberAccount().getColumnDbName();

        final Set<String> markSet = new HashSet<String>();
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                if (displaySql.contains("count(*)")) {
                    assertTrue(Srl.containsAll(displaySql, "count(*)", "union"));
                    assertTrue(Srl.contains(displaySql, pkCol));
                    assertTrue(Srl.contains(displaySql, specifiedCol));
                    assertTrue(Srl.contains(displaySql, implicitCol));
                    assertFalse(Srl.contains(displaySql, notCol));
                    markSet.add("handle");
                }
            }
        });

        CallbackContext.setCallbackContextOnThread(context);
        try {
            // ## Act ##
            ListResultBean<Member> memberList = memberBhv.selectList(cb);

            // ## Assert ##
            assertTrue(Srl.containsAll(cb.toDisplaySql(), pkCol, specifiedCol, implicitCol));
            assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
            assertFalse(memberList.isEmpty());
            for (Member member : memberList) {
                assertNotNull(member.getMemberName());
                assertNull(member.getMemberAccount());
            }

            // ## Act ##
            cb.paging(5, 2);
            PagingResultBean<Member> memberPage = memberBhv.selectPage(cb);

            // ## Assert ##
            assertTrue(Srl.containsAll(cb.toDisplaySql(), pkCol, specifiedCol, implicitCol));
            assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
            assertNotSame(0, memberPage.size());
            for (Member member : memberPage) {
                assertNotNull(member.getMemberName());
                assertNull(member.getMemberAccount());
            }
            assertTrue(markSet.contains("handle")); // for count-select assert
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    public void test_UnionQuery_with_SpecifyColumn_selectListAndPage_noPrimaryKey_basic() throws Exception {
        // ## Arrange ##
        SummaryWithdrawalCB cb = new SummaryWithdrawalCB();
        int countAll = summaryWithdrawalBhv.selectCount(cb);
        cb.specify().columnMemberName(); // all members have own different names
        cb.union(new UnionQuery<SummaryWithdrawalCB>() {
            public void query(SummaryWithdrawalCB unionCB) {
            }
        });
        cb.query().addOrderBy_MemberName_Asc();

        final String specifiedCol = SummaryWithdrawalDbm.getInstance().columnMemberName().getColumnDbName();
        final String notCol = SummaryWithdrawalDbm.getInstance().columnWithdrawalDatetime().getColumnDbName();

        final Set<String> markSet = new HashSet<String>();
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                if (displaySql.contains("count(*)")) {
                    assertTrue(Srl.containsAll(displaySql, "count(*)", "union"));
                    assertTrue(Srl.contains(displaySql, specifiedCol));
                    assertFalse(Srl.contains(displaySql, notCol));
                    markSet.add("handle");
                }
            }
        });

        CallbackContext.setCallbackContextOnThread(context);
        try {
            // ## Act ##
            ListResultBean<SummaryWithdrawal> withdrawalList = summaryWithdrawalBhv.selectList(cb);

            // ## Assert ##
            assertEquals(withdrawalList.size(), countAll);
            assertTrue(Srl.contains(cb.toDisplaySql(), specifiedCol));
            assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
            assertNotSame(0, withdrawalList.size());
            for (SummaryWithdrawal withdrawal : withdrawalList) {
                assertNotNull(withdrawal.getMemberName());
                assertNull(withdrawal.getWithdrawalDatetime());
            }

            // ## Act ##
            cb.paging(2, 1);
            PagingResultBean<SummaryWithdrawal> withdrawalPage = summaryWithdrawalBhv.selectPage(cb);

            // ## Assert ##
            assertEquals(withdrawalPage.getAllRecordCount(), withdrawalList.size());
            assertTrue(Srl.contains(cb.toDisplaySql(), specifiedCol));
            assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
            assertNotSame(0, withdrawalPage.size());
            for (SummaryWithdrawal withdrawal : withdrawalPage) {
                assertNotNull(withdrawal.getMemberName());
                assertNull(withdrawal.getWithdrawalDatetime());
            }
            assertTrue(markSet.contains("handle")); // for count-select assert
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    public void test_UnionQuery_with_SpecifyColumn_selectListAndPage_noPrimaryKey_distinct() throws Exception {
        // ## Arrange ##
        SummaryWithdrawalCB cb = new SummaryWithdrawalCB();
        int countAll = summaryWithdrawalBhv.selectCount(cb);
        cb.specify().columnMemberStatusCode(); // all members have the same status here
        cb.union(new UnionQuery<SummaryWithdrawalCB>() {
            public void query(SummaryWithdrawalCB unionCB) {
            }
        });
        cb.query().addOrderBy_MemberStatusCode_Asc();

        // ## Act ##
        ListResultBean<SummaryWithdrawal> withdrawalList = summaryWithdrawalBhv.selectList(cb);

        // ## Assert ##
        assertTrue(withdrawalList.size() < countAll); // should have no duplicated record
        String specifiedCol = SummaryWithdrawalDbm.getInstance().columnMemberStatusCode().getColumnDbName();
        String notCol = SummaryWithdrawalDbm.getInstance().columnWithdrawalDatetime().getColumnDbName();
        assertTrue(Srl.contains(cb.toDisplaySql(), specifiedCol));
        assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
        assertNotSame(0, withdrawalList.size());
        for (SummaryWithdrawal withdrawal : withdrawalList) {
            assertNotNull(withdrawal.getMemberStatusCode());
            assertNull(withdrawal.getWithdrawalDatetime());
        }

        // ## Act ##
        cb.paging(2, 1);
        PagingResultBean<SummaryWithdrawal> withdrawalPage = summaryWithdrawalBhv.selectPage(cb);

        // ## Assert ##
        // count-select in paging should get a corresponding count
        assertEquals(withdrawalPage.getAllRecordCount(), withdrawalList.size());
        assertTrue(Srl.contains(cb.toDisplaySql(), specifiedCol));
        assertFalse(Srl.contains(cb.toDisplaySql(), notCol));
        assertNotSame(0, withdrawalPage.size());
        for (SummaryWithdrawal withdrawal : withdrawalPage) {
            assertNotNull(withdrawal.getMemberStatusCode());
            assertNull(withdrawal.getWithdrawalDatetime());
        }
    }

    // ===================================================================================
    //                                                                             Illegal
    //                                                                             =======
    public void test_UnionQuery_illegal() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                try {
                    // ## Act ##
                    unionCB.setupSelect_MemberSecurityAsOne();

                    // ## Assert ##
                    fail();
                } catch (SetupSelectIllegalPurposeException e) {
                    // OK
                    log(e.getMessage());
                }
                try {
                    // ## Act ##
                    unionCB.specify();

                    // ## Assert ##
                    fail();
                } catch (SpecifyIllegalPurposeException e) {
                    // OK
                    log(e.getMessage());
                }
                try {
                    // ## Act ##
                    unionCB.query().addOrderBy_MemberId_Asc();

                    // ## Assert ##
                    fail();
                } catch (OrderByIllegalPurposeException e) {
                    // OK
                    log(e.getMessage());
                }
                unionCB.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() { // OK
                            public void query(PurchaseCB subCB) {
                                subCB.specify().columnPurchasePrice();
                            }
                        }).equal(123);
            }
        });
    }
}
