/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.nss;

import org.seasar.dbflute.cbean.ConditionQuery;
import com.example.dbflute.spring.dbflute.cbean.cq.VendorTheLongAndWindingTableAndColumnRefCQ;

/**
 * The nest select set-upper of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF.
 * @author DBFlute(AutoGenerator)
 */
public class VendorTheLongAndWindingTableAndColumnRefNss {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected VendorTheLongAndWindingTableAndColumnRefCQ _query;
    public VendorTheLongAndWindingTableAndColumnRefNss(VendorTheLongAndWindingTableAndColumnRefCQ query) { _query = query; }
    public boolean hasConditionQuery() { return _query != null; }

    // ===================================================================================
    //                                                                     Nested Relation
    //                                                                     ===============
    public VendorTheLongAndWindingTableAndColumnNss withVendorTheLongAndWindingTableAndColumn() {
        _query.doNss(new VendorTheLongAndWindingTableAndColumnRefCQ.NssCall() { public ConditionQuery qf() { return _query.queryVendorTheLongAndWindingTableAndColumn(); }});
		return new VendorTheLongAndWindingTableAndColumnNss(_query.queryVendorTheLongAndWindingTableAndColumn());
    }

}
