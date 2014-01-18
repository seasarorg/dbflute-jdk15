/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.nss;

import org.seasar.dbflute.cbean.ConditionQuery;
import com.example.dbflute.spring.dbflute.cbean.cq.ProductCQ;

/**
 * The nest select set-upper of PRODUCT.
 * @author DBFlute(AutoGenerator)
 */
public class ProductNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected ProductCQ _query;
    public ProductNss(ProductCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============
    public ProductCategoryNss withProductCategory() {
        _query.doNss(new ProductCQ.NssCall() { public ConditionQuery qf() { return _query.queryProductCategory(); }});
		return new ProductCategoryNss(_query.queryProductCategory());
    }
    public ProductStatusNss withProductStatus() {
        _query.doNss(new ProductCQ.NssCall() { public ConditionQuery qf() { return _query.queryProductStatus(); }});
		return new ProductStatusNss(_query.queryProductStatus());
    }

}
