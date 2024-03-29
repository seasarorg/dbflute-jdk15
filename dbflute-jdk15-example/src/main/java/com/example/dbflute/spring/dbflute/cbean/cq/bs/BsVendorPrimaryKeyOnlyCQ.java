/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.cbean.cq.bs;

import java.util.Map;

import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import com.example.dbflute.spring.dbflute.cbean.cq.ciq.*;
import com.example.dbflute.spring.dbflute.cbean.*;
import com.example.dbflute.spring.dbflute.cbean.cq.*;

/**
 * The base condition-query of VENDOR_PRIMARY_KEY_ONLY.
 * @author DBFlute(AutoGenerator)
 */
public class BsVendorPrimaryKeyOnlyCQ extends AbstractBsVendorPrimaryKeyOnlyCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected VendorPrimaryKeyOnlyCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsVendorPrimaryKeyOnlyCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                 InlineView/OrClause
    //                                                                 ===================
    /**
     * Prepare InlineView query. <br />
     * {select ... from ... left outer join (select * from VENDOR_PRIMARY_KEY_ONLY) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public VendorPrimaryKeyOnlyCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = xcreateCIQ(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected VendorPrimaryKeyOnlyCIQ xcreateCIQ() {
        VendorPrimaryKeyOnlyCIQ ciq = xnewCIQ();
        ciq.xsetBaseCB(_baseCB);
        return ciq;
    }

    protected VendorPrimaryKeyOnlyCIQ xnewCIQ() {
        return new VendorPrimaryKeyOnlyCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this);
    }

    /**
     * Prepare OnClause query. <br />
     * {select ... from ... left outer join VENDOR_PRIMARY_KEY_ONLY on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public VendorPrimaryKeyOnlyCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        VendorPrimaryKeyOnlyCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====

    protected ConditionValue _primaryKeyOnlyId;
    public ConditionValue getPrimaryKeyOnlyId() {
        if (_primaryKeyOnlyId == null) { _primaryKeyOnlyId = nCV(); }
        return _primaryKeyOnlyId;
    }
    protected ConditionValue getCValuePrimaryKeyOnlyId() { return getPrimaryKeyOnlyId(); }

    /** 
     * Add order-by as ascend. <br />
     * PRIMARY_KEY_ONLY_ID: {PK, NotNull, BIGINT(19)}
     * @return this. (NotNull)
     */
    public BsVendorPrimaryKeyOnlyCQ addOrderBy_PrimaryKeyOnlyId_Asc() { regOBA("PRIMARY_KEY_ONLY_ID"); return this; }

    /**
     * Add order-by as descend. <br />
     * PRIMARY_KEY_ONLY_ID: {PK, NotNull, BIGINT(19)}
     * @return this. (NotNull)
     */
    public BsVendorPrimaryKeyOnlyCQ addOrderBy_PrimaryKeyOnlyId_Desc() { regOBD("PRIMARY_KEY_ONLY_ID"); return this; }

    // ===================================================================================
    //                                                             SpecifiedDerivedOrderBy
    //                                                             =======================
    /**
     * Add order-by for specified derived column as ascend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #FD4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] asc</span>
     * cb.<span style="color: #FD4747">addSpecifiedDerivedOrderBy_Asc</span>(<span style="color: #FD4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsVendorPrimaryKeyOnlyCQ addSpecifiedDerivedOrderBy_Asc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }

    /**
     * Add order-by for specified derived column as descend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #FD4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] desc</span>
     * cb.<span style="color: #FD4747">addSpecifiedDerivedOrderBy_Desc</span>(<span style="color: #FD4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsVendorPrimaryKeyOnlyCQ addSpecifiedDerivedOrderBy_Desc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
    // ===================================================================================
    //                                                                     ScalarCondition
    //                                                                     ===============
    protected Map<String, VendorPrimaryKeyOnlyCQ> _scalarConditionMap;
    public Map<String, VendorPrimaryKeyOnlyCQ> getScalarCondition() { return _scalarConditionMap; }
    public String keepScalarCondition(VendorPrimaryKeyOnlyCQ subQuery) {
        if (_scalarConditionMap == null) { _scalarConditionMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarConditionMap.size() + 1);
        _scalarConditionMap.put(key, subQuery); return "scalarCondition." + key;
    }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    protected Map<String, VendorPrimaryKeyOnlyCQ> _myselfExistsMap;
    public Map<String, VendorPrimaryKeyOnlyCQ> getMyselfExists() { return _myselfExistsMap; }
    public String keepMyselfExists(VendorPrimaryKeyOnlyCQ subQuery) {
        if (_myselfExistsMap == null) { _myselfExistsMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfExistsMap.size() + 1);
        _myselfExistsMap.put(key, subQuery); return "myselfExists." + key;
    }

    // ===================================================================================
    //                                                                       MyselfInScope
    //                                                                       =============
    protected Map<String, VendorPrimaryKeyOnlyCQ> _myselfInScopeMap;
    public Map<String, VendorPrimaryKeyOnlyCQ> getMyselfInScope() { return _myselfInScopeMap; }
    public String keepMyselfInScope(VendorPrimaryKeyOnlyCQ subQuery) {
        if (_myselfInScopeMap == null) { _myselfInScopeMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeMap.size() + 1);
        _myselfInScopeMap.put(key, subQuery); return "myselfInScope." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return VendorPrimaryKeyOnlyCB.class.getName(); }
    protected String xCQ() { return VendorPrimaryKeyOnlyCQ.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
