package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.bhv.ConditionBeanSetupper;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.exception.SpecifyColumnNotSetupSelectColumnException;
import org.seasar.dbflute.jdbc.SqlLogHandler;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.SummaryWithdrawalDbm;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.cbean.SummaryWithdrawalCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.PurchaseBhv;
import com.example.dbflute.spring.dbflute.exbhv.SummaryWithdrawalBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberAddress;
import com.example.dbflute.spring.dbflute.exentity.MemberSecurity;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.dbflute.exentity.MemberWithdrawal;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.dbflute.exentity.WithdrawalReason;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBSpecifyColumnTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private PurchaseBhv purchaseBhv;
    private SummaryWithdrawalBhv summaryWithdrawalBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_SpecifyColumn_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.specify().columnMemberName();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getMemberStatusCode());
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberName());
            assertNull(member.getMemberAccount());
            assertNotNull(member.getMemberStatusCode());
            assertNull(member.getRegisterDatetime());
            assertNull(member.getVersionNo());
        }
    }

    public void test_SpecifyColumn_normalRelation() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberWithdrawalAsOne().withWithdrawalReason();
        cb.setupSelect_MemberSecurityAsOne();
        cb.specify().columnMemberName();
        cb.specify().specifyMemberStatus().columnMemberStatusName();
        cb.specify().specifyMemberWithdrawalAsOne().specifyWithdrawalReason().columnWithdrawalReasonText();
        cb.specify().specifyMemberSecurityAsOne().columnReminderAnswer();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean existsReason = false;
        for (Member member : memberList) {
            MemberStatus status = member.getMemberStatus();
            MemberSecurity security = member.getMemberSecurityAsOne();
            MemberWithdrawal withdrawal = member.getMemberWithdrawalAsOne();
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", " + status);
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberName());
            assertNull(member.getMemberAccount());
            assertNull(member.getRegisterDatetime());
            assertNull(member.getVersionNo());
            assertNotNull(member.getMemberStatusCode());
            assertNotNull(status);
            assertNotNull(status.getMemberStatusCode());
            assertNotNull(status.getMemberStatusName());
            assertNotNull(security);
            assertNull(security.getLoginPassword());
            assertNull(security.getReminderQuestion());
            assertNotNull(security.getReminderAnswer());
            if (member.isMemberStatusCodeWithdrawal()) {
                assertNotNull(withdrawal);
                assertNotNull(withdrawal.getWithdrawalDatetime());
                assertNotNull(withdrawal.getRegisterUser());
                WithdrawalReason reason = withdrawal.getWithdrawalReason();
                if (reason != null) {
                    existsReason = true;
                    assertNotNull(reason.getWithdrawalReasonText());
                    assertNull(reason.getDisplayOrder());
                }
            }
            assertNull(status.getDisplayOrder());
            assertNull(status.getDescription());
        }
        assertTrue(existsReason);
    }

    public void test_SpecifyColumn_onlyRelation() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.specify().specifyMemberStatus().columnMemberStatusName();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            MemberStatus memberStatus = member.getMemberStatus();
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", " + memberStatus);
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberName());
            assertNotNull(member.getMemberAccount());
            assertNotNull(member.getRegisterDatetime());
            assertNotNull(member.getVersionNo());
            assertNotNull(member.getMemberStatusCode());
            assertNotNull(memberStatus);
            assertNotNull(memberStatus.getMemberStatusCode());
            assertNotNull(memberStatus.getMemberStatusName());
            assertNull(memberStatus.getDisplayOrder());
            assertNull(memberStatus.getDescription());
        }
    }

    public void test_SpecifyColumn_severalCall() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.specify().columnMemberName();
        cb.specify().columnMemberName();
        cb.specify().columnMemberName();
        cb.specify().specifyMemberStatus().columnMemberStatusName();
        cb.specify().specifyMemberStatus().columnMemberStatusName();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            MemberStatus memberStatus = member.getMemberStatus();
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", " + memberStatus);
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberName());
            assertNull(member.getMemberAccount());
            assertNull(member.getRegisterDatetime());
            assertNull(member.getVersionNo());
            assertNotNull(member.getMemberStatusCode());
            assertNotNull(memberStatus);
            assertNotNull(memberStatus.getMemberStatusCode());
            assertNotNull(memberStatus.getMemberStatusName());
            assertNull(memberStatus.getDisplayOrder());
            assertNull(memberStatus.getDescription());
        }
    }

    // ===================================================================================
    //                                                                        Select Count
    //                                                                        ============
    public void test_SpecifyColumn_selectCount_basic() {
        // ## Arrange ##
        int countAll = memberBhv.selectCount(new MemberCB());
        MemberCB cb = new MemberCB();
        cb.specify().columnMemberName();
        final Set<String> markSet = new HashSet<String>();
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                MemberDbm dbm = MemberDbm.getInstance();
                assertTrue(Srl.contains(displaySql, "count(*)"));
                assertFalse(Srl.contains(displaySql, dbm.columnMemberId().getColumnDbName()));
                assertFalse(Srl.contains(displaySql, dbm.columnMemberName().getColumnDbName()));
                assertFalse(Srl.contains(displaySql, dbm.columnMemberAccount().getColumnDbName()));
                markSet.add("handle");
            }
        });

        // ## Act ##
        CallbackContext.setCallbackContextOnThread(context);
        try {
            int count = memberBhv.selectCount(cb);

            // ## Assert ##
            assertEquals(countAll, count);
            assertTrue(markSet.contains("handle"));
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    public void test_SpecifyColumn_selectCount_with_UnionQuery_basic() {
        // ## Arrange ##
        int countAll = memberBhv.selectCount(new MemberCB());
        MemberCB cb = new MemberCB();
        cb.specify().columnMemberName();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
            }
        });
        final Set<String> markSet = new HashSet<String>();
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                MemberDbm dbm = MemberDbm.getInstance();
                assertTrue(Srl.contains(displaySql, "count(*)"));
                assertTrue(Srl.contains(displaySql, dbm.columnMemberId().getColumnDbName()));
                assertFalse(Srl.contains(displaySql, dbm.columnMemberName().getColumnDbName()));
                assertFalse(Srl.contains(displaySql, dbm.columnMemberAccount().getColumnDbName()));
                markSet.add("handle");
            }
        });

        // ## Act ##
        CallbackContext.setCallbackContextOnThread(context);
        try {
            int count = memberBhv.selectCount(cb);

            // ## Assert ##
            assertEquals(countAll, count);
            assertTrue(markSet.contains("handle"));
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    public void test_SpecifyColumn_selectCount_with_UnionQuery_noPrimaryKey() {
        // ## Arrange ##
        int countAll = summaryWithdrawalBhv.selectCount(new SummaryWithdrawalCB());
        SummaryWithdrawalCB cb = new SummaryWithdrawalCB();
        cb.specify().columnMemberName();
        cb.union(new UnionQuery<SummaryWithdrawalCB>() {
            public void query(SummaryWithdrawalCB unionCB) {
            }
        });
        final Set<String> markSet = new HashSet<String>();
        CallbackContext context = new CallbackContext();
        context.setSqlLogHandler(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                String displaySql = info.getDisplaySql();
                SummaryWithdrawalDbm dbm = SummaryWithdrawalDbm.getInstance();
                assertTrue(Srl.contains(displaySql, "count(*)"));
                assertTrue(Srl.contains(displaySql, dbm.columnMemberId().getColumnDbName()));
                assertTrue(Srl.contains(displaySql, dbm.columnMemberName().getColumnDbName()));
                assertTrue(Srl.contains(displaySql, dbm.columnWithdrawalDatetime().getColumnDbName()));
                markSet.add("handle");
            }
        });

        // ## Act ##
        CallbackContext.setCallbackContextOnThread(context);
        try {
            int count = summaryWithdrawalBhv.selectCount(cb);

            // ## Assert ##
            assertEquals(countAll, count);
            assertTrue(markSet.contains("handle"));
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    // implemented at UnionTest
    //public void test_SpecifyColumn_selectListAndPage_with_UnionQuery_basic() {
    //public void ...() {

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    public void test_loadReferrer_specifyColumn_autoResolved_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_Equal(3);

        // At first, it selects the list of Member.
        Member member = memberBhv.selectEntity(cb);

        // ## Act ##
        // And it loads the list of Purchase with its conditions.
        memberBhv.loadPurchaseList(member, new ConditionBeanSetupper<PurchaseCB>() {
            public void setup(PurchaseCB cb) {
                cb.specify().columnPurchaseDatetime();
                cb.query().setPurchaseCount_GreaterEqual(2);
                cb.query().addOrderBy_PurchaseCount_Desc();
            }
        });

        // ## Assert ##
        log("[MEMBER]: " + member.getMemberName());
        List<Purchase> purchaseList = member.getPurchaseList();// *Point!
        assertNotSame(0, purchaseList.size());
        for (Purchase purchase : purchaseList) {
            log("    [PURCHASE]: " + purchase.toString());
            assertNotNull(purchase.getPurchaseId());
            assertNotNull(purchase.getMemberId()); // auto-resolved
            assertNull(purchase.getProductId());
            assertNull(purchase.getPurchaseCount());
            assertNotNull(purchase.getPurchaseDatetime());
        }
    }

    public void test_loadReferrer_specifyColumn_notResolved_by_foreignOnlySpecify() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_Equal(3);

        // At first, it selects the list of Member.
        Member member = memberBhv.selectEntity(cb);

        // ## Act ##
        // And it loads the list of Purchase with its conditions.
        memberBhv.loadPurchaseList(member, new ConditionBeanSetupper<PurchaseCB>() {
            public void setup(PurchaseCB cb) {
                cb.setupSelect_Product();
                cb.specify().specifyProduct().columnProductName();
                cb.query().setPurchaseCount_GreaterEqual(2);
                cb.query().addOrderBy_PurchaseCount_Desc();
            }
        });

        // ## Assert ##
        log("[MEMBER]: " + member.getMemberName());
        List<Purchase> purchaseList = member.getPurchaseList();// *Point!
        assertNotSame(0, purchaseList.size());
        for (Purchase purchase : purchaseList) {
            log("    [PURCHASE]: " + purchase.toString());
            assertNotNull(purchase.getPurchaseId());
            assertNotNull(purchase.getMemberId());
            assertNotNull(purchase.getProductId());
            assertNotNull(purchase.getPurchaseCount());
            assertNotNull(purchase.getPurchaseDatetime());
            assertNotNull(purchase.getProduct().getProductId());
            assertNotNull(purchase.getProduct().getProductName());
            assertNull(purchase.getProduct().getProductStatusCode());
        }
    }

    // ===================================================================================
    //                                                                        Reverse Call
    //                                                                        ============
    public void test_specify_reverseCall_foreignSpecify() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            cb.specify().specifyMemberStatus().columnMemberStatusName();

            // ## Assert ##
            fail();
        } catch (SpecifyColumnNotSetupSelectColumnException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_specify_reverseCall_FKColumnFollow_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().columnMemberName();
        cb.setupSelect_MemberStatus();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            MemberStatus memberStatus = member.getMemberStatus();
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", " + memberStatus);
            assertNotNull(member.getMemberName());
            assertNull(member.getMemberAccount());
            assertNotNull(member.getMemberStatusCode());
            assertNotNull(memberStatus);
        }
        assertTrue(cb.toDisplaySql().contains("dfloc.MEMBER_STATUS_CODE"));
    }

    public void test_specify_reverseCall_FKColumnFollow_notGoOff() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().countDistinct(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().columnProductId();
            }
        }, Member.ALIAS_productKindCount);
        cb.setupSelect_MemberStatus();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            MemberStatus memberStatus = member.getMemberStatus();
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", " + memberStatus);
            assertNotNull(member.getMemberName());
            assertNotNull(member.getMemberAccount());
            assertNotNull(member.getMemberStatusCode());
            assertNotNull(member.getProductKindCount());
            assertNotNull(memberStatus);
        }
        assertTrue(cb.toDisplaySql().contains("dfloc.MEMBER_STATUS_CODE"));
    }

    // ===================================================================================
    //                                                                         BizOneToOne
    //                                                                         ===========
    public void test_specify_BizOneToOne() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 11, 12); // 2005/12/12
        Date targetDate = cal.getTime();

        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberAddressAsValid(targetDate);
        cb.specify().columnMemberName();
        cb.specify().specifyMemberAddressAsValid().columnAddress();
        cb.query().addOrderBy_MemberId_Asc();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean existsAddress = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        String formattedTargetDate = fmt.format(targetDate);
        log("[" + formattedTargetDate + "]");
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            assertNotNull(memberName);
            assertNull(member.getMemberAccount());
            MemberAddress memberAddressAsValid = member.getMemberAddressAsValid();
            if (memberAddressAsValid != null) {
                assertNull(memberAddressAsValid.getValidBeginDate());
                assertNull(memberAddressAsValid.getValidEndDate());
                String address = memberAddressAsValid.getAddress();
                assertNotNull(address);
                log(memberName + ", " + address);
                existsAddress = true;
            } else {
                log(memberName + ", null");
            }
        }
        assertTrue(existsAddress);
        assertFalse(cb.toDisplaySql().contains("where")); // not use where clause
    }

    public void test_specify_BizOneToOne_nestRelation() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 11, 12); // 2005/12/12
        Date targetDate = cal.getTime();

        PurchaseCB cb = new PurchaseCB();
        cb.setupSelect_Member().withMemberAddressAsValid(targetDate);
        cb.specify().columnPurchaseCount();
        cb.specify().specifyMember().columnMemberName();
        cb.specify().specifyMember().specifyMemberAddressAsValid().columnAddress();
        cb.query().addOrderBy_PurchaseDatetime_Asc();

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb);

        // ## Assert ##
        assertNotSame(0, purchaseList.size());
        boolean existsAddress = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        String formattedTargetDate = fmt.format(targetDate);
        log("[" + formattedTargetDate + "]");
        for (Purchase purchase : purchaseList) {
            Member member = purchase.getMember();
            String memberName = member.getMemberName();
            assertNotNull(memberName);
            assertNull(member.getMemberAccount());
            MemberAddress memberAddressAsValid = member.getMemberAddressAsValid();
            if (memberAddressAsValid != null) {
                assertNull(memberAddressAsValid.getValidBeginDate());
                assertNull(memberAddressAsValid.getValidEndDate());
                String address = memberAddressAsValid.getAddress();
                assertNotNull(address);
                log(memberName + ", " + address);
                existsAddress = true;
            } else {
                log(memberName + ", null");
            }
        }
        assertTrue(existsAddress);
        assertFalse(cb.toDisplaySql().contains("where")); // not use where clause
    }
}
