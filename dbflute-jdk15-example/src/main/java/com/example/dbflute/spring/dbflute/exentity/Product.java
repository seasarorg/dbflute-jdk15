/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.exentity;

import com.example.dbflute.spring.dbflute.bsentity.BsProduct;

/**
 * The entity of PRODUCT.
 * <p>
 * You can implement your original methods here.
 * This class remains when re-generating.
 * </p>
 * @author DBFlute(AutoGenerator)
 */
public class Product extends BsProduct {

    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_purchaseMemberCount = "PURCHASE_MEMBER_COUNT";
    public static final String ALIAS_purchaseCount = "PURCHASE_COUNT";

    private Integer _purchaseMemberCount;
    private Integer _purchaseCount;

    public Integer getPurchaseCount() {
        return _purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        _purchaseCount = purchaseCount;
    }

    public void setPurchaseMemberCount(Integer purchaseMemberCount) {
        _purchaseMemberCount = purchaseMemberCount;
    }

    public Integer getPurchaseMemberCount() {
        return _purchaseMemberCount;
    }
}
