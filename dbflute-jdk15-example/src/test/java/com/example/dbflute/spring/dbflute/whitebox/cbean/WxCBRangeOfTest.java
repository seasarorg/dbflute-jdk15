package com.example.dbflute.spring.dbflute.whitebox.cbean;

import org.seasar.dbflute.bhv.UpdateOption;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.coption.RangeOfOption;

import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.PurchaseBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.9.2C (2011/12/09 Friday)
 */
public class WxCBRangeOfTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                               Plain
    //                                                                               =====
    public void test_RangeOf_greater() throws Exception {
        // ## Arrange ##
        int onePrice = 2000;
        preparePrice(0, onePrice);

        // ## Act & Assert ##
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(onePrice, null, new RangeOfOption());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(onePrice, onePrice, new RangeOfOption());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(onePrice, null, new RangeOfOption().greaterThan());
            assertEquals(0, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(onePrice - 1, null, new RangeOfOption().greaterThan());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
    }

    public void test_RangeOf_less() throws Exception {
        // ## Arrange ##
        int onePrice = 2000;
        preparePrice(9999999, onePrice);

        // ## Act & Assert ##
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(null, onePrice, new RangeOfOption());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(onePrice, onePrice, new RangeOfOption());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(null, onePrice, new RangeOfOption().lessThan());
            assertEquals(0, purchaseBhv.selectCount(cb));
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.query().setPurchasePrice_RangeOf(null, onePrice + 1, new RangeOfOption().lessThan());
            assertEquals(1, purchaseBhv.selectCount(cb));
        }
    }

    public void test_RangeOf_orIsNull() throws Exception {
        // ## Arrange ##
        int onePrice = 2000;
        preparePrice(9999999, onePrice);

        // ## Act & Assert ##
        {
            PurchaseCB cb = new PurchaseCB();
            cb.setupSelect_Member();
            cb.query().queryMember().inline().setMemberId_InScope(newArrayList(2, 5, 7, 11, 16, 19));
            cb.query().queryMember().setMemberId_RangeOf(5, 7, new RangeOfOption().orIsNull());
            ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb);
            boolean existsMemberId = false;
            boolean existsMemberNull = false;
            for (Purchase purchase : purchaseList) {
                Member member = purchase.getMember();
                if (member != null) {
                    Integer memberId = member.getMemberId();
                    assertTrue(memberId.equals(5) || memberId.equals(7));
                    existsMemberId = true;
                } else {
                    existsMemberNull = true;
                }
            }
            assertTrue(existsMemberId);
            assertTrue(existsMemberNull);
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.setupSelect_Member();
            cb.query().queryMember().inline().setMemberId_InScope(newArrayList(2, 5, 7, 11, 16, 19));
            RangeOfOption option = new RangeOfOption().orIsNull().greaterThan().lessThan();
            cb.query().queryMember().setMemberId_RangeOf(2, 11, option);
            ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb);
            boolean existsMemberId = false;
            boolean existsMemberNull = false;
            for (Purchase purchase : purchaseList) {
                Member member = purchase.getMember();
                if (member != null) {
                    Integer memberId = member.getMemberId();
                    assertTrue(memberId.equals(5) || memberId.equals(7));
                    existsMemberId = true;
                } else {
                    existsMemberNull = true;
                }
            }
            assertTrue(existsMemberId);
            assertTrue(existsMemberNull);
        }
    }

    protected void preparePrice(int basePrice, int onePrice) {
        {
            Purchase purchase = new Purchase();
            purchase.setPurchasePrice(basePrice);
            UpdateOption<PurchaseCB> option = new UpdateOption<PurchaseCB>().allowNonQueryUpdate();
            purchaseBhv.varyingQueryUpdate(purchase, new PurchaseCB(), option);
        }
        {
            PurchaseCB cb = new PurchaseCB();
            cb.fetchFirst(1);
            Purchase purchase = purchaseBhv.selectEntityWithDeletedCheck(cb);
            purchase.setPurchasePrice(onePrice);
            purchaseBhv.updateNonstrict(purchase);
        }
    }
}
