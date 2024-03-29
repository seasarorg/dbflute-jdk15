package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.OrQuery;
import org.seasar.dbflute.cbean.UnionQuery;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberAddress;
import com.example.dbflute.spring.dbflute.exentity.MemberSecurity;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.dbflute.exentity.MemberWithdrawal;
import com.example.dbflute.spring.dbflute.exentity.WithdrawalReason;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBSetupSelectTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_setupSelect_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberWithdrawalAsOne().withWithdrawalReason();
        cb.setupSelect_MemberSecurityAsOne();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertNotSame(0, memberList);
        boolean existsReason = false;
        for (Member member : memberList) {
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberStatusCode());
            MemberStatus memberStatus = member.getMemberStatus();
            assertEquals(member.getMemberStatusCode(), memberStatus.getMemberStatusCode());
            MemberWithdrawal withdrawal = member.getMemberWithdrawalAsOne();
            if (withdrawal != null) {
                assertEquals(member.getMemberId(), withdrawal.getMemberId());
                WithdrawalReason reason = withdrawal.getWithdrawalReason();
                if (reason != null) {
                    existsReason = true;
                    assertEquals(withdrawal.getWithdrawalReasonCode(), reason.getWithdrawalReasonCode());
                }
            }
            MemberSecurity security = member.getMemberSecurityAsOne();
            assertEquals(member.getMemberId(), security.getMemberId());
        }
        assertTrue(existsReason);
    }

    // ===================================================================================
    //                                                                         BizOneToOne
    //                                                                         ===========
    public void test_setupSelect_BizOneToOne() {
        // ## Arrange ##
        Calendar cal = Calendar.getInstance();
        cal.set(2005, 11, 12); // 2005/12/12
        Date targetDate = cal.getTime();

        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberAddressAsValid(targetDate);
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
            MemberAddress memberAddressAsValid = member.getMemberAddressAsValid();
            if (memberAddressAsValid != null) {
                assertNotNull(memberAddressAsValid.getValidBeginDate());
                assertNotNull(memberAddressAsValid.getValidEndDate());
                String validBeginDate = fmt.format(memberAddressAsValid.getValidBeginDate());
                String validEndDate = fmt.format(memberAddressAsValid.getValidEndDate());
                assertTrue(validBeginDate.compareTo(formattedTargetDate) <= 0);
                assertTrue(validEndDate.compareTo(formattedTargetDate) >= 0);
                String address = memberAddressAsValid.getAddress();
                log(memberName + ", " + validBeginDate + ", " + validEndDate + ", " + address);
                existsAddress = true;
            } else {
                log(memberName + ", null");
            }
        }
        assertTrue(existsAddress);
        assertFalse(cb.toDisplaySql().contains("where")); // not use where clause
    }

    // ===================================================================================
    //                                                                              Tricky
    //                                                                              ======
    public void test_setupSelect_after_union() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.orScopeQuery(new OrQuery<MemberCB>() {
                    public void query(MemberCB orCB) {
                        orCB.query().setMemberStatusCode_Equal_Provisional();
                        orCB.query().setMemberStatusCode_Equal_Withdrawal();
                    }
                });
            }
        });
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberWithdrawalAsOne().withWithdrawalReason();
        cb.setupSelect_MemberSecurityAsOne();
        cb.query().setMemberStatusCode_Equal_Formalized();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertNotSame(0, memberList);
        boolean existsReason = false;
        for (Member member : memberList) {
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberStatusCode());
            MemberStatus memberStatus = member.getMemberStatus();
            assertEquals(member.getMemberStatusCode(), memberStatus.getMemberStatusCode());
            MemberWithdrawal withdrawal = member.getMemberWithdrawalAsOne();
            if (withdrawal != null) {
                assertEquals(member.getMemberId(), withdrawal.getMemberId());
                WithdrawalReason reason = withdrawal.getWithdrawalReason();
                if (reason != null) {
                    existsReason = true;
                    assertEquals(withdrawal.getWithdrawalReasonCode(), reason.getWithdrawalReasonCode());
                }
            }
            MemberSecurity security = member.getMemberSecurityAsOne();
            assertEquals(member.getMemberId(), security.getMemberId());
        }
        assertTrue(existsReason);
    }
}
