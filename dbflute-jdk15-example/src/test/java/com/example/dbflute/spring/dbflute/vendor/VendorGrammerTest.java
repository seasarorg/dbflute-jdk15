package com.example.dbflute.spring.dbflute.vendor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.AccessContext;
import org.seasar.dbflute.bhv.QueryInsertSetupper;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.coption.DerivedReferrerOption;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberLoginCB;
import com.example.dbflute.spring.dbflute.cbean.MemberWithdrawalCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.cbean.WithdrawalReasonCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberWithdrawalBhv;
import com.example.dbflute.spring.dbflute.exbhv.PurchaseBhv;
import com.example.dbflute.spring.dbflute.exbhv.WithdrawalReasonBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberSecurity;
import com.example.dbflute.spring.dbflute.exentity.MemberWithdrawal;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.dbflute.exentity.WithdrawalReason;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.7.9 (2011/01/09 Sunday)
 */
public class VendorGrammerTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private PurchaseBhv purchaseBhv;
    private WithdrawalReasonBhv withdrawalReasonBhv;
    private MemberWithdrawalBhv memberWithdrawalBhv;

    // ===================================================================================
    //                                                                           InnerJoin
    //                                                                           =========
    public void test_innerJoin_nested_basic() {
        // ## Arrange ##
        int countAll;
        {
            MemberCB cb = new MemberCB();
            cb.query().setMemberStatusCode_Equal_Withdrawal();
            cb.query().queryMemberWithdrawalAsOne().setWithdrawalReasonCode_IsNotNull();
            countAll = memberBhv.selectCount(cb);
        }

        MemberCB cb = new MemberCB();
        cb.query().queryMemberWithdrawalAsOne().queryWithdrawalReason().innerJoin();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertTrue(cb.toDisplaySql().contains("inner join"));
        assertTrue(cb.toDisplaySql().contains("left outer join"));
        assertFalse(memberList.isEmpty());
        assertEquals(countAll, memberList.size());
        for (Member member : memberList) {
            log(member.toString());
        }
    }

    public void test_innerJoin_nested_branch() {
        // ## Arrange ##
        int countAll;
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().queryMember().queryMemberWithdrawalAsOne().queryWithdrawalReason()
                    .setWithdrawalReasonCode_IsNotNull();
            countAll = purchaseBhv.selectCount(cb);
        }

        PurchaseCB cb = new PurchaseCB();
        cb.setupSelect_Member().withMemberSecurityAsOne();
        cb.setupSelect_Member().withMemberWithdrawalAsOne().withWithdrawalReason();
        cb.query().queryMember().queryMemberSecurityAsOne().inline().setMemberId_LessThan(10);
        cb.query().queryMember().queryMemberWithdrawalAsOne().queryWithdrawalReason().innerJoin();

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb);

        // ## Assert ##
        assertTrue(cb.toDisplaySql().contains("inner join"));
        assertTrue(cb.toDisplaySql().contains("left outer join"));
        assertFalse(purchaseList.isEmpty());
        assertEquals(countAll, purchaseList.size());
        boolean existsSecurity = false;
        boolean notExistsSecurity = false;
        for (Purchase purchase : purchaseList) {
            Member member = purchase.getMember();
            log(purchase.getPurchaseId() + ", " + member.getMemberId() + ", " + member.getMemberName() + ", "
                    + member.getMemberSecurityAsOne());
            MemberSecurity security = member.getMemberSecurityAsOne();
            if (security != null) {
                assertTrue(member.getMemberId() < 10);
                existsSecurity = true;
            } else {
                assertTrue(member.getMemberId() >= 10);
                notExistsSecurity = true;
            }
        }
        assertTrue(existsSecurity);
        assertTrue(notExistsSecurity);
    }

    // ===================================================================================
    //                                                                         QueryInsert
    //                                                                         ===========
    public void test_queryInsert_basic() { // and fixed value, common column, exclusive control column
        // ## Arrange ##
        int countAll = memberWithdrawalBhv.selectCount(new MemberWithdrawalCB());
        Map<Integer, Member> formalizedMemberMap = new LinkedHashMap<Integer, Member>();
        {
            MemberCB cb = new MemberCB();
            cb.query().setMemberStatusCode_Equal_Formalized();
            ListResultBean<Member> memberList = memberBhv.selectList(cb);
            for (Member member : memberList) {
                formalizedMemberMap.put(member.getMemberId(), member);
            }
        }
        final WithdrawalReason firstReason;
        {
            WithdrawalReasonCB cb = new WithdrawalReasonCB();
            cb.fetchFirst(1);
            firstReason = withdrawalReasonBhv.selectEntityWithDeletedCheck(cb);
        }

        // ## Act ##
        int inserted = memberWithdrawalBhv.queryInsert(new QueryInsertSetupper<MemberWithdrawal, MemberWithdrawalCB>() {
            public ConditionBean setup(MemberWithdrawal entity, MemberWithdrawalCB intoCB) {
                entity.setWithdrawalReasonCode(firstReason.getWithdrawalReasonCode());
                MemberCB cb = new MemberCB();

                intoCB.specify().columnMemberId().mappedFrom(cb.specify().columnMemberId());
                intoCB.specify().columnWithdrawalDatetime().mappedFrom(cb.specify().columnFormalizedDatetime());
                intoCB.specify().columnWithdrawalReasonInputText().mappedFrom(cb.specify().columnMemberName());

                cb.query().setMemberStatusCode_Equal_Formalized();
                return cb;
            }
        });

        // ## Assert ##
        assertNotSame(0, inserted);
        int actualCountAll = memberWithdrawalBhv.selectCount(new MemberWithdrawalCB());
        assertNotSame(countAll, actualCountAll);
        assertTrue(countAll < actualCountAll);
        assertEquals(actualCountAll - countAll, inserted);

        MemberWithdrawalCB cb = new MemberWithdrawalCB();
        List<Integer> memberIdList = DfCollectionUtil.newArrayList();
        for (Member member : formalizedMemberMap.values()) {
            memberIdList.add(member.getMemberId());
        }
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<MemberWithdrawal> actualList = memberWithdrawalBhv.selectList(cb);
        assertNotSame(0, actualList.size());
        assertEquals(memberIdList.size(), actualList.size());
        String fmt = "yyyy-MM-dd HH:mm:ss.SSS";
        for (MemberWithdrawal actual : actualList) {
            String withdrawalReasonCode = actual.getWithdrawalReasonCode();
            assertNotNull(withdrawalReasonCode);
            assertEquals(firstReason.getWithdrawalReasonCode(), withdrawalReasonCode);
            Member member = formalizedMemberMap.get(actual.getMemberId());
            assertEquals(member.getMemberName(), actual.getWithdrawalReasonInputText());

            // common columns
            AccessContext accessContext = AccessContext.getAccessContextOnThread();
            String registerTimestamp = DfTypeUtil.toString(accessContext.getAccessTimestamp(), fmt);
            assertEquals(registerTimestamp, DfTypeUtil.toString(actual.getRegisterDatetime(), fmt));
            assertEquals(accessContext.getAccessUser(), actual.getRegisterUser());
            assertEquals(registerTimestamp, DfTypeUtil.toString(actual.getUpdateDatetime(), fmt));
            assertEquals(accessContext.getAccessUser(), actual.getUpdateUser());

            // exclusive control column
            assertEquals(Long.valueOf(0), actual.getVersionNo());
        }
    }

    public void test_queryInsert_with_DerivedReferrer() { // and relation, order-by, no specified
        // ## Arrange ##
        Map<Integer, Member> formalizedMemberMap = new LinkedHashMap<Integer, Member>();
        {
            MemberCB cb = new MemberCB();
            cb.setupSelect_MemberStatus();
            cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
                public void query(MemberLoginCB subCB) {
                    subCB.specify().columnLoginDatetime();
                    subCB.query().setMobileLoginFlg_Equal_True();
                }
            }, Member.ALIAS_latestLoginDatetime);
            cb.query().setMemberStatusCode_Equal_Formalized();
            ListResultBean<Member> memberList = memberBhv.selectList(cb);
            for (Member member : memberList) {
                formalizedMemberMap.put(member.getMemberId(), member);
            }
        }
        final Timestamp coalesce = DfTypeUtil.toTimestamp("1234-10-24 12:34:56.147");

        // ## Act ##
        memberWithdrawalBhv.queryInsert(new QueryInsertSetupper<MemberWithdrawal, MemberWithdrawalCB>() {
            public ConditionBean setup(MemberWithdrawal entity, MemberWithdrawalCB intoCB) {
                MemberCB cb = new MemberCB();
                cb.setupSelect_MemberStatus();
                cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
                    public void query(MemberLoginCB subCB) {
                        subCB.specify().columnLoginDatetime();
                        subCB.query().setMobileLoginFlg_Equal_True();
                    }
                }, Member.ALIAS_latestLoginDatetime, new DerivedReferrerOption().coalesce(coalesce));

                intoCB.specify().columnMemberId().mappedFrom(cb.specify().columnMemberId());
                intoCB.specify().columnWithdrawalDatetime().mappedFromDerived(Member.ALIAS_latestLoginDatetime);
                intoCB.specify().columnWithdrawalReasonInputText()
                        .mappedFrom(cb.specify().specifyMemberStatus().columnMemberStatusName());

                cb.query().setMemberStatusCode_Equal_Formalized();
                cb.query().addOrderBy_Birthdate_Desc().withNullsLast();
                cb.query().addOrderBy_MemberId_Asc();
                return cb;
            }
        });

        // ## Assert ##
        MemberWithdrawalCB cb = new MemberWithdrawalCB();
        List<Integer> memberIdList = DfCollectionUtil.newArrayList();
        for (Member member : formalizedMemberMap.values()) {
            memberIdList.add(member.getMemberId());
        }
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<MemberWithdrawal> actualList = memberWithdrawalBhv.selectList(cb);
        assertNotSame(0, actualList.size());
        String fmt = "yyyy-MM-dd HH:mm:ss.SSS";
        Set<String> existsSet = new HashSet<String>();
        for (MemberWithdrawal actual : actualList) {
            String withdrawalDatetime = DfTypeUtil.toString(actual.getWithdrawalDatetime(), fmt);
            String coalesceDatetime = DfTypeUtil.toString(coalesce, fmt);
            Member member = formalizedMemberMap.get(actual.getMemberId());
            assertNotNull(member);
            if (withdrawalDatetime.equals(coalesceDatetime)) {
                assertNull(member.getLatestLoginDatetime());
                existsSet.add("coalesce");
            } else {
                String latestLoginDatetime = DfTypeUtil.toString(member.getLatestLoginDatetime(), fmt);
                assertNotNull(latestLoginDatetime);
                assertEquals(latestLoginDatetime, withdrawalDatetime);
                existsSet.add("latest");
            }
            assertNull(actual.getWithdrawalReasonCode());
            assertEquals(member.getMemberStatus().getMemberStatusName(), actual.getWithdrawalReasonInputText());
        }
        assertEquals(2, existsSet.size());
    }
}
