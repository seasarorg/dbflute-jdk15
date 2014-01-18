package com.example.dbflute.spring.dbflute.whitebox.bhv;

import java.sql.Timestamp;
import java.util.Date;

import org.seasar.dbflute.bhv.DeleteOption;
import org.seasar.dbflute.bhv.UpdateOption;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.coption.ColumnConversionOption;
import org.seasar.dbflute.exception.NonQueryUpdateNotAllowedException;
import org.seasar.dbflute.helper.HandyDate;

import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.PurchaseBhv;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.7.2 (2010/06/20 Sunday)
 */
public class WxBhvVaryingQueryUpdateTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                         Calculation
    //                                                                         ===========
    public void test_varyingQueryUpdate_plus() throws Exception {
        // ## Arrange ##
        Purchase purchase = new Purchase();
        purchase.setPurchasePrice(99999);
        UpdateOption<PurchaseCB> option = new UpdateOption<PurchaseCB>();
        option.self(new SpecifyQuery<PurchaseCB>() {
            public void specify(PurchaseCB cb) {
                cb.specify().columnPurchaseCount();
            }
        }).plus(1);

        PurchaseCB cb = new PurchaseCB();
        cb.query().setPaymentCompleteFlg_Equal_True();
        cb.query().addOrderBy_PurchaseId_Asc();
        ListResultBean<Purchase> beforeList = purchaseBhv.selectList(cb);

        // ## Act ##
        purchaseBhv.varyingQueryUpdate(purchase, cb, option);

        // ## Assert ##
        ListResultBean<Purchase> actualList = purchaseBhv.selectList(cb);
        assertNotSame(0, actualList.size());
        assertEquals(beforeList.size(), actualList.size());
        int index = 0;
        for (Purchase actual : actualList) {
            Purchase before = beforeList.get(index);
            Integer beforeCount = before.getPurchaseCount();
            Integer actualAcount = actual.getPurchaseCount();
            log(actual.getPurchaseId() + " : " + beforeCount + " -> " + actualAcount);
            assertEquals(Integer.valueOf(beforeCount + 1), actualAcount);
            assertEquals(Integer.valueOf(99999), actual.getPurchasePrice());
            assertEquals(before.getRegisterDatetime(), actual.getRegisterDatetime());
            assertNotSame(before.getUpdateDatetime(), actual.getUpdateDatetime());
            ++index;
        }
    }

    // ===================================================================================
    //                                                                             Convert
    //                                                                             =======
    public void test_varyingQueryUpdate_convert() throws Exception {
        // ## Arrange ##
        Purchase purchase = new Purchase();
        purchase.setPurchasePrice(99999);
        UpdateOption<PurchaseCB> option = new UpdateOption<PurchaseCB>();
        option.self(new SpecifyQuery<PurchaseCB>() {
            public void specify(PurchaseCB cb) {
                cb.specify().columnPurchaseDatetime();
            }
        }).convert(new ColumnConversionOption().addDay(3));

        PurchaseCB cb = new PurchaseCB();
        cb.query().setPaymentCompleteFlg_Equal_True();
        cb.query().addOrderBy_PurchaseId_Asc();
        ListResultBean<Purchase> beforeList = purchaseBhv.selectList(cb);

        // ## Act ##
        purchaseBhv.varyingQueryUpdate(purchase, cb, option);

        // ## Assert ##
        ListResultBean<Purchase> actualList = purchaseBhv.selectList(cb);
        assertNotSame(0, actualList.size());
        assertEquals(beforeList.size(), actualList.size());
        int index = 0;
        for (Purchase actual : actualList) {
            Purchase before = beforeList.get(index);
            Timestamp beforeDatetime = before.getPurchaseDatetime();
            Timestamp actualDatetime = actual.getPurchaseDatetime();
            log(actual.getPurchaseId() + " : " + beforeDatetime + " -> " + actualDatetime);
            Date expectedDate = new HandyDate(beforeDatetime).addDay(3).getDate();
            assertEquals(expectedDate.getTime(), actualDatetime.getTime());
            ++index;
        }
    }

    // ===================================================================================
    //                                                                      NonQueryUpdate
    //                                                                      ==============
    public void test_varyingQueryUpdate_NonQueryUpdate_default() throws Exception {
        // ## Arrange ##
        Purchase purchase = new Purchase();
        purchase.setPurchasePrice(99999);
        UpdateOption<PurchaseCB> option = new UpdateOption<PurchaseCB>();
        PurchaseCB cb = new PurchaseCB();

        // ## Act ##
        try {
            int updated = purchaseBhv.varyingQueryUpdate(purchase, cb, option);

            // ## Assert ##
            fail("updated=" + updated);
        } catch (NonQueryUpdateNotAllowedException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_varyingQueryUpdate_NonQueryUpdate_allow() throws Exception {
        // ## Arrange ##
        Purchase purchase = new Purchase();
        purchase.setPurchasePrice(99999);
        UpdateOption<PurchaseCB> option = new UpdateOption<PurchaseCB>();
        option.allowNonQueryUpdate();
        PurchaseCB cb = new PurchaseCB();

        // ## Act ##
        int updated = purchaseBhv.varyingQueryUpdate(purchase, cb, option);

        // ## Assert ##
        ListResultBean<Purchase> actualList = purchaseBhv.selectList(cb);
        assertNotSame(0, actualList.size());
        assertEquals(actualList.size(), updated);
        for (Purchase actual : actualList) {
            assertEquals(purchase.getPurchasePrice(), actual.getPurchasePrice());
        }
        log("option=" + option);
    }

    public void test_varyingQueryDelete_NonQueryDelete_allow() throws Exception {
        // ## Arrange ##
        int countAll = purchaseBhv.selectCount(new PurchaseCB());
        DeleteOption<PurchaseCB> option = new DeleteOption<PurchaseCB>();
        option.allowNonQueryDelete();
        PurchaseCB cb = new PurchaseCB();

        // ## Act ##
        int deleted = purchaseBhv.varyingQueryDelete(cb, option);

        // ## Assert ##
        ListResultBean<Purchase> actualList = purchaseBhv.selectList(cb);
        assertEquals(0, actualList.size());
        assertEquals(countAll, deleted);
        log("option=" + option);
    }
}
