package com.example.dbflute.spring.dbflute.whitebox.bhv;

import java.util.ArrayList;
import java.util.List;

import org.seasar.dbflute.bhv.ConditionBeanSetupper;
import org.seasar.dbflute.bhv.EntityListSetupper;
import org.seasar.dbflute.bhv.LoadReferrerOption;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.UnionQuery;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberStatusCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberAddress;
import com.example.dbflute.spring.dbflute.exentity.MemberSecurity;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.dbflute.exentity.MemberWithdrawal;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxBhvLoadReferrerTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_loadReferrer_one_entity() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_Equal(3);

        // At first, it selects the list of Member.
        Member member = memberBhv.selectEntity(cb);

        // ## Act ##
        // And it loads the list of Purchase with its conditions.
        memberBhv.loadPurchaseList(member, new ConditionBeanSetupper<PurchaseCB>() {
            public void setup(PurchaseCB cb) {
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
        }
    }

    public void test_loadReferrer_loadReferrerReferrer() {
        // ## Arrange ##
        // A base table is MemberStatus at this test case.
        MemberStatusCB cb = new MemberStatusCB();
        cb.query().setMemberStatusCode_Equal_Formalized();
        MemberStatus memberStatus = memberStatusBhv.selectEntity(cb);

        LoadReferrerOption<MemberCB, Member> loadReferrerOption = new LoadReferrerOption<MemberCB, Member>();

        // Member
        loadReferrerOption.setConditionBeanSetupper(new ConditionBeanSetupper<MemberCB>() {
            public void setup(MemberCB cb) {
                cb.query().addOrderBy_FormalizedDatetime_Desc();
            }
        });

        // Purchase
        loadReferrerOption.setEntityListSetupper(new EntityListSetupper<Member>() {
            public void setup(List<Member> entityList) {
                memberBhv.loadPurchaseList(entityList, new ConditionBeanSetupper<PurchaseCB>() {
                    public void setup(PurchaseCB cb) {
                        cb.query().addOrderBy_PurchaseCount_Desc();
                        cb.query().addOrderBy_ProductId_Desc();
                    }
                });
            }
        });

        // ## Act ##
        memberStatusBhv.loadMemberList(memberStatus, loadReferrerOption);

        // ## Assert ##
        boolean existsPurchase = false;
        List<Member> memberList = memberStatus.getMemberList();
        log("[MEMBER_STATUS]: " + memberStatus.getMemberStatusName());
        for (Member member : memberList) {
            List<Purchase> purchaseList = member.getPurchaseList();
            log("    [MEMBER]: " + member.getMemberName() + ", " + member.getFormalizedDatetime());
            for (Purchase purchase : purchaseList) {
                log("        [PURCHASE]: " + purchase.getPurchaseId() + ", " + purchase.getPurchaseCount());
                existsPurchase = true;
            }
        }
        log("");
        assertTrue(existsPurchase);
    }

    public void test_loadReferrer_union_querySynchronization() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberName_PrefixSearch("S");
        cb.query().addOrderBy_Birthdate_Desc().withNullsLast();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        assertFalse(memberList.isEmpty());
        memberBhv.loadPurchaseList(memberList, new ConditionBeanSetupper<PurchaseCB>() {
            public void setup(PurchaseCB cb) {
                cb.query().setPurchasePrice_GreaterEqual(1800);
                cb.union(new UnionQuery<PurchaseCB>() {
                    public void query(PurchaseCB unionCB) {
                        // ## Assert ##
                        String msgWhere = "The union CB should have FK inScope: " + unionCB;
                        assertTrue(msgWhere, unionCB.hasWhereClauseOnBaseQuery());
                        String msgOrderBy = "The union CB should not have order-by clause: " + unionCB;
                        assertFalse(msgOrderBy, unionCB.hasOrderByClause());
                        assertTrue(unionCB.toDisplaySql().contains(" in ("));
                        unionCB.query().setPaymentCompleteFlg_Equal_False();
                    }
                });
                cb.unionAll(new UnionQuery<PurchaseCB>() {
                    public void query(PurchaseCB unionCB) {
                        // ## Assert ##
                        String msgWhere = "The union CB should have FK inScope: " + unionCB;
                        assertTrue(msgWhere, unionCB.hasWhereClauseOnBaseQuery());
                        String msgOrderBy = "The union CB should not have order-by clause: " + unionCB;
                        assertFalse(msgOrderBy, unionCB.hasOrderByClause());
                        assertTrue(unionCB.toDisplaySql().contains(" in ("));
                        unionCB.query().setPurchaseCount_GreaterEqual(2);
                    }
                });
                cb.query().addOrderBy_PurchaseDatetime_Desc().addOrderBy_ProductId_Asc();
            }
        });
        boolean exists = false;
        for (Member member : memberList) {
            log(member.toStringWithRelation());
            List<Purchase> purchaseList = member.getPurchaseList();
            if (!purchaseList.isEmpty()) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

    // ===================================================================================
    //                                                                    Case Insensitive
    //                                                                    ================
    public void test_loadReferrer_caseInsensitive_basic() {
        // ## Arrange ##
        List<MemberStatus> statusList = new ArrayList<MemberStatus>();
        {
            MemberStatus status = new MemberStatus();
            status.setMemberStatusCode("fml");
            statusList.add(status);
        }
        {
            MemberStatus status = new MemberStatus();
            status.setMemberStatusCode("FML");
            statusList.add(status);
        }

        // ## Act ##
        memberStatusBhv.loadMemberList(statusList, new ConditionBeanSetupper<MemberCB>() {
            public void setup(MemberCB cb) {
            }
        });

        // ## Assert ##
        assertNotSame(0, statusList.size());
        assertEquals(2, statusList.size());
        for (MemberStatus status : statusList) {
            List<Member> memberList = status.getMemberList();
            assertFalse(memberList.isEmpty()); // both can get
            log(status.getMemberStatusCode() + " : " + memberList.size());
        }
    }

    // ===================================================================================
    //                                                                             Illegal
    //                                                                             =======
    public void test_loadReferrer_null_entity() {
        // ## Arrange ##
        try {
            // ## Act ##
            memberBhv.loadPurchaseList((Member) null, new ConditionBeanSetupper<PurchaseCB>() {
                public void setup(PurchaseCB cb) {
                    cb.query().setPurchaseCount_GreaterEqual(2);
                    cb.query().addOrderBy_PurchaseCount_Desc();
                }
            });

            // ## Assert ##
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_loadReferrer_null_primaryKey() {
        // ## Arrange ##
        try {
            // ## Act ##
            memberBhv.loadPurchaseList(new Member(), new ConditionBeanSetupper<PurchaseCB>() {
                public void setup(PurchaseCB cb) {
                    cb.query().setPurchaseCount_GreaterEqual(2);
                    cb.query().addOrderBy_PurchaseCount_Desc();
                }
            });

            // ## Assert ##
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                            Pull out
    //                                                                            ========
    public void test_loadReferrer_pulloutMember() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberSecurityAsOne();
        cb.setupSelect_MemberWithdrawalAsOne();
        cb.setupSelect_MemberAddressAsValid(currentDate());
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Act & Assert ##
        assertFalse(memberList.isEmpty());

        log("[MemberStatus]");
        List<MemberStatus> memberStatusList = memberBhv.pulloutMemberStatus(memberList);
        assertNotSame(0, memberStatusList.size());
        boolean existsMemberStatusBackTo = false;
        for (MemberStatus memberStatus : memberStatusList) {
            List<Member> backToList = memberStatus.getMemberList();
            if (!backToList.isEmpty()) {
                existsMemberStatusBackTo = true;
            }
            log(memberStatus.getMemberStatusName());
            for (Member backTo : backToList) {
                log("    " + backTo);
            }
        }
        assertTrue(existsMemberStatusBackTo);

        log("[MemberSecurity(AsOne)]");
        List<MemberSecurity> memberSecurityAsOneList = memberBhv.pulloutMemberSecurityAsOne(memberList);
        assertNotSame(0, memberSecurityAsOneList.size());
        assertEquals(memberList.size(), memberSecurityAsOneList.size());
        boolean existsMemberSecurityAsOneBackTo = false;
        for (MemberSecurity memberSecurity : memberSecurityAsOneList) {
            Member backTo = memberSecurity.getMember();
            if (backTo != null) {
                existsMemberSecurityAsOneBackTo = true;
            }
            log(memberSecurity.getReminderAnswer() + ", " + backTo);
        }
        assertTrue(existsMemberSecurityAsOneBackTo);

        log("[MemberWithdrawal(AsOne)]");
        List<MemberWithdrawal> memberWithdrawalAsOneList = memberBhv.pulloutMemberWithdrawalAsOne(memberList);
        assertNotSame(0, memberWithdrawalAsOneList.size());
        assertTrue(memberList.size() > memberWithdrawalAsOneList.size());
        boolean existsMemberWithdrawalAsOneBackTo = false;
        for (MemberWithdrawal memberWithdrawal : memberWithdrawalAsOneList) {
            Member backTo = memberWithdrawal.getMember();
            if (backTo != null) {
                existsMemberWithdrawalAsOneBackTo = true;
            }
            log(memberWithdrawal.getWithdrawalReasonCode() + ", " + backTo);
        }
        assertTrue(existsMemberWithdrawalAsOneBackTo);

        log("[MemberAddress(AsValie)]");
        List<MemberAddress> memberAddressAsValieList = memberBhv.pulloutMemberAddressAsValid(memberList);
        assertNotSame(0, memberAddressAsValieList.size());
        for (MemberAddress memberAddress : memberAddressAsValieList) {
            Member backTo = memberAddress.getMember();
            log(memberAddress.getAddress() + ", " + memberAddress.getValidBeginDate() + ", "
                    + memberAddress.getValidEndDate() + ", " + backTo);
            assertNull(backTo);
        }
    }
}
