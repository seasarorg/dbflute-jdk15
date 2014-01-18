/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.exentity;

import com.example.dbflute.spring.dbflute.bsentity.BsMemberStatus;

/**
 * The entity of MEMBER_STATUS.
 * @author DBFlute(AutoGenerator)
 */
public class MemberStatus extends BsMemberStatus {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_anyMemberAccount = "ANY_MEMBER_ACCOUNT";
    public static final String ALIAS_maxPurchasePrice = "MAX_PURCHASE_PRICE";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String _anyMemberAccount;
    protected Integer _maxPurchasePrice;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getAnyMemberAccount() {
        return _anyMemberAccount;
    }

    public void setAnyMemberAccount(String anyMemberAccount) {
        this._anyMemberAccount = anyMemberAccount;
    }

    public Integer getMaxPurchasePrice() {
        return _maxPurchasePrice;
    }

    public void setMaxPurchasePrice(Integer maxPurchasePrice) {
        this._maxPurchasePrice = maxPurchasePrice;
    }
}
