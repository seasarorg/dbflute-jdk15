package com.example.dbflute.spring.dbflute.whitebox.cbean;

import org.seasar.dbflute.exception.ConditionInvokingFailureException;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.PurchaseBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.6.7 (2010/03/16 Tuesday)
 */
public class WxCBReflectionTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                         SetupSelect
    //                                                                         ===========
    public void test_invokeSetupSelect_oneLevel() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        cb.invokeSetupSelect("memberStatus");

        // ## Assert ##
        cb.query().setMemberId_Equal(3);
        Member member = memberBhv.selectEntity(cb);
        assertNotNull(member.getMemberStatus());
    }

    public void test_invokeSetupSelect_oneLevel_notExists() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            cb.invokeSetupSelect("memberOverTheWaves");

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_invokeSetupSelect_twoLevel() {
        // ## Arrange ##
        PurchaseCB cb = new PurchaseCB();

        // ## Act ##
        cb.invokeSetupSelect("member.memberStatus");

        // ## Assert ##
        cb.query().setPurchaseId_Equal(3L);
        Purchase purchase = purchaseBhv.selectEntity(cb);
        assertNotNull(purchase.getMember());
        assertNotNull(purchase.getMember().getMemberStatus());
    }

    public void test_invokeSetupSelect_twoLevel_notExists() {
        // ## Arrange ##
        PurchaseCB cb = new PurchaseCB();

        // ## Act ##
        try {
            cb.invokeSetupSelect("member.memberBonFire");

            // ## Assert ##
            fail();
        } catch (ConditionInvokingFailureException e) {
            // OK
            log(e.getMessage());
        }
    }
}
