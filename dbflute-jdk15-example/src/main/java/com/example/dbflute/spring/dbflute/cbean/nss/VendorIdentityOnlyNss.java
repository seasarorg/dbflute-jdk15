/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.nss;

import com.example.dbflute.spring.dbflute.cbean.cq.VendorIdentityOnlyCQ;

/**
 * The nest select set-upper of VENDOR_IDENTITY_ONLY.
 * @author DBFlute(AutoGenerator)
 */
public class VendorIdentityOnlyNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected VendorIdentityOnlyCQ _query;
    public VendorIdentityOnlyNss(VendorIdentityOnlyCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============

}
