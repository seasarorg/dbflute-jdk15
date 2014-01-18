/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.exentity;

import java.sql.Timestamp;

import com.example.dbflute.spring.dbflute.bsentity.BsMember;

/**
 * The entity of MEMBER.
 * @author DBFlute(AutoGenerator)
 */
public class Member extends BsMember {

    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String ALIAS_latestLoginDatetime = "LATEST_LOGIN_DATETIME";
    public static final String ALIAS_loginCount = "LOGIN_COUNT";
    public static final String ALIAS_productKindCount = "PRODUCT_KIND_COUNT";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected Timestamp _latestLoginDatetime;
    protected Integer _loginCount;
    protected Integer _productKindCount;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Timestamp getLatestLoginDatetime() {
        return _latestLoginDatetime;
    }

    public void setLatestLoginDatetime(Timestamp latestLoginDatetime) {
        _latestLoginDatetime = latestLoginDatetime;
    }

    public Integer getLoginCount() {
        return _loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this._loginCount = loginCount;
    }

    public Integer getProductKindCount() {
        return _productKindCount;
    }

    public void setProductKindCount(Integer productKindCount) {
        this._productKindCount = productKindCount;
    }
}
