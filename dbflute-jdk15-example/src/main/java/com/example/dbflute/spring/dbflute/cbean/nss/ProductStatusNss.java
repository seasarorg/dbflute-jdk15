/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.nss;

import com.example.dbflute.spring.dbflute.cbean.cq.ProductStatusCQ;

/**
 * The nest select set-upper of PRODUCT_STATUS.
 * @author DBFlute(AutoGenerator)
 */
public class ProductStatusNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected ProductStatusCQ _query;
    public ProductStatusNss(ProductStatusCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============

}
