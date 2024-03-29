/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.nss;

import com.example.dbflute.spring.dbflute.cbean.cq.VendorPrimaryKeyOnlyCQ;

/**
 * The nest select set-upper of VENDOR_PRIMARY_KEY_ONLY.
 * @author DBFlute(AutoGenerator)
 */
public class VendorPrimaryKeyOnlyNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected VendorPrimaryKeyOnlyCQ _query;
    public VendorPrimaryKeyOnlyNss(VendorPrimaryKeyOnlyCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============

}
