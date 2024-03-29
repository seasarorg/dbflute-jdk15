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
 * The base condition-query of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF.
 * @author DBFlute(AutoGenerator)
 */
public class BsVendorTheLongAndWindingTableAndColumnRefCQ extends AbstractBsVendorTheLongAndWindingTableAndColumnRefCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected VendorTheLongAndWindingTableAndColumnRefCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsVendorTheLongAndWindingTableAndColumnRefCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                 InlineView/OrClause
    //                                                                 ===================
    /**
     * Prepare InlineView query. <br />
     * {select ... from ... left outer join (select * from VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public VendorTheLongAndWindingTableAndColumnRefCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = xcreateCIQ(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected VendorTheLongAndWindingTableAndColumnRefCIQ xcreateCIQ() {
        VendorTheLongAndWindingTableAndColumnRefCIQ ciq = xnewCIQ();
        ciq.xsetBaseCB(_baseCB);
        return ciq;
    }

    protected VendorTheLongAndWindingTableAndColumnRefCIQ xnewCIQ() {
        return new VendorTheLongAndWindingTableAndColumnRefCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this);
    }

    /**
     * Prepare OnClause query. <br />
     * {select ... from ... left outer join VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #FD4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public VendorTheLongAndWindingTableAndColumnRefCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        VendorTheLongAndWindingTableAndColumnRefCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====

    protected ConditionValue _theLongAndWindingTableAndColumnRefId;
    public ConditionValue getTheLongAndWindingTableAndColumnRefId() {
        if (_theLongAndWindingTableAndColumnRefId == null) { _theLongAndWindingTableAndColumnRefId = nCV(); }
        return _theLongAndWindingTableAndColumnRefId;
    }
    protected ConditionValue getCValueTheLongAndWindingTableAndColumnRefId() { return getTheLongAndWindingTableAndColumnRefId(); }

    /** 
     * Add order-by as ascend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID: {PK, NotNull, BIGINT(19)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnRefId_Asc() { regOBA("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID"); return this; }

    /**
     * Add order-by as descend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID: {PK, NotNull, BIGINT(19)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnRefId_Desc() { regOBD("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID"); return this; }

    protected ConditionValue _theLongAndWindingTableAndColumnId;
    public ConditionValue getTheLongAndWindingTableAndColumnId() {
        if (_theLongAndWindingTableAndColumnId == null) { _theLongAndWindingTableAndColumnId = nCV(); }
        return _theLongAndWindingTableAndColumnId;
    }
    protected ConditionValue getCValueTheLongAndWindingTableAndColumnId() { return getTheLongAndWindingTableAndColumnId(); }

    protected Map<String, VendorTheLongAndWindingTableAndColumnCQ> _theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap;
    public Map<String, VendorTheLongAndWindingTableAndColumnCQ> getTheLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumn() { return _theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap; }
    public String keepTheLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumn(VendorTheLongAndWindingTableAndColumnCQ subQuery) {
        if (_theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap == null) { _theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap.size() + 1);
        _theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumnMap.put(key, subQuery); return "theLongAndWindingTableAndColumnId_InScopeRelation_VendorTheLongAndWindingTableAndColumn." + key;
    }

    protected Map<String, VendorTheLongAndWindingTableAndColumnCQ> _theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap;
    public Map<String, VendorTheLongAndWindingTableAndColumnCQ> getTheLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumn() { return _theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap; }
    public String keepTheLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumn(VendorTheLongAndWindingTableAndColumnCQ subQuery) {
        if (_theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap == null) { _theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap.size() + 1);
        _theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumnMap.put(key, subQuery); return "theLongAndWindingTableAndColumnId_NotInScopeRelation_VendorTheLongAndWindingTableAndColumn." + key;
    }

    /** 
     * Add order-by as ascend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID: {IX, NotNull, BIGINT(19), FK to VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnId_Asc() { regOBA("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID"); return this; }

    /**
     * Add order-by as descend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID: {IX, NotNull, BIGINT(19), FK to VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnId_Desc() { regOBD("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID"); return this; }

    protected ConditionValue _theLongAndWindingTableAndColumnRefDate;
    public ConditionValue getTheLongAndWindingTableAndColumnRefDate() {
        if (_theLongAndWindingTableAndColumnRefDate == null) { _theLongAndWindingTableAndColumnRefDate = nCV(); }
        return _theLongAndWindingTableAndColumnRefDate;
    }
    protected ConditionValue getCValueTheLongAndWindingTableAndColumnRefDate() { return getTheLongAndWindingTableAndColumnRefDate(); }

    /** 
     * Add order-by as ascend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE: {NotNull, DATE(8)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnRefDate_Asc() { regOBA("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE"); return this; }

    /**
     * Add order-by as descend. <br />
     * THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE: {NotNull, DATE(8)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_TheLongAndWindingTableAndColumnRefDate_Desc() { regOBD("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE"); return this; }

    protected ConditionValue _shortDate;
    public ConditionValue getShortDate() {
        if (_shortDate == null) { _shortDate = nCV(); }
        return _shortDate;
    }
    protected ConditionValue getCValueShortDate() { return getShortDate(); }

    /** 
     * Add order-by as ascend. <br />
     * SHORT_DATE: {NotNull, DATE(8)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_ShortDate_Asc() { regOBA("SHORT_DATE"); return this; }

    /**
     * Add order-by as descend. <br />
     * SHORT_DATE: {NotNull, DATE(8)}
     * @return this. (NotNull)
     */
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addOrderBy_ShortDate_Desc() { regOBD("SHORT_DATE"); return this; }

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
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addSpecifiedDerivedOrderBy_Asc(String aliasName)
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
    public BsVendorTheLongAndWindingTableAndColumnRefCQ addSpecifiedDerivedOrderBy_Desc(String aliasName)
    { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    protected void reflectRelationOnUnionQuery(ConditionQuery baseQueryAsSuper, ConditionQuery unionQueryAsSuper) {
        VendorTheLongAndWindingTableAndColumnRefCQ baseQuery = (VendorTheLongAndWindingTableAndColumnRefCQ)baseQueryAsSuper;
        VendorTheLongAndWindingTableAndColumnRefCQ unionQuery = (VendorTheLongAndWindingTableAndColumnRefCQ)unionQueryAsSuper;
        if (baseQuery.hasConditionQueryVendorTheLongAndWindingTableAndColumn()) {
            unionQuery.queryVendorTheLongAndWindingTableAndColumn().reflectRelationOnUnionQuery(baseQuery.queryVendorTheLongAndWindingTableAndColumn(), unionQuery.queryVendorTheLongAndWindingTableAndColumn());
        }
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
    /**
     * Get the condition-query for relation table. <br />
     * VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN by my THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, named 'vendorTheLongAndWindingTableAndColumn'.
     * @return The instance of condition-query. (NotNull)
     */
    public VendorTheLongAndWindingTableAndColumnCQ queryVendorTheLongAndWindingTableAndColumn() {
        return getConditionQueryVendorTheLongAndWindingTableAndColumn();
    }
    protected VendorTheLongAndWindingTableAndColumnCQ _conditionQueryVendorTheLongAndWindingTableAndColumn;
    public VendorTheLongAndWindingTableAndColumnCQ getConditionQueryVendorTheLongAndWindingTableAndColumn() {
        if (_conditionQueryVendorTheLongAndWindingTableAndColumn == null) {
            _conditionQueryVendorTheLongAndWindingTableAndColumn = xcreateQueryVendorTheLongAndWindingTableAndColumn();
            xsetupOuterJoinVendorTheLongAndWindingTableAndColumn();
        }
        return _conditionQueryVendorTheLongAndWindingTableAndColumn;
    }
    protected VendorTheLongAndWindingTableAndColumnCQ xcreateQueryVendorTheLongAndWindingTableAndColumn() {
        String nrp = resolveNextRelationPath("VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF", "vendorTheLongAndWindingTableAndColumn");
        String jan = resolveJoinAliasName(nrp, xgetNextNestLevel());
        VendorTheLongAndWindingTableAndColumnCQ cq = new VendorTheLongAndWindingTableAndColumnCQ(this, xgetSqlClause(), jan, xgetNextNestLevel());
        cq.xsetBaseCB(_baseCB);
        cq.xsetForeignPropertyName("vendorTheLongAndWindingTableAndColumn");
        cq.xsetRelationPath(nrp); return cq;
    }
    protected void xsetupOuterJoinVendorTheLongAndWindingTableAndColumn() {
        VendorTheLongAndWindingTableAndColumnCQ cq = getConditionQueryVendorTheLongAndWindingTableAndColumn();
        Map<String, String> joinOnMap = newLinkedHashMap();
        joinOnMap.put("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID", "THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID");
        registerOuterJoin(cq, joinOnMap, "vendorTheLongAndWindingTableAndColumn");
    }
    public boolean hasConditionQueryVendorTheLongAndWindingTableAndColumn() {
        return _conditionQueryVendorTheLongAndWindingTableAndColumn != null;
    }

    // ===================================================================================
    //                                                                     ScalarCondition
    //                                                                     ===============
    protected Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> _scalarConditionMap;
    public Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> getScalarCondition() { return _scalarConditionMap; }
    public String keepScalarCondition(VendorTheLongAndWindingTableAndColumnRefCQ subQuery) {
        if (_scalarConditionMap == null) { _scalarConditionMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_scalarConditionMap.size() + 1);
        _scalarConditionMap.put(key, subQuery); return "scalarCondition." + key;
    }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    protected Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> _myselfExistsMap;
    public Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> getMyselfExists() { return _myselfExistsMap; }
    public String keepMyselfExists(VendorTheLongAndWindingTableAndColumnRefCQ subQuery) {
        if (_myselfExistsMap == null) { _myselfExistsMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfExistsMap.size() + 1);
        _myselfExistsMap.put(key, subQuery); return "myselfExists." + key;
    }

    // ===================================================================================
    //                                                                       MyselfInScope
    //                                                                       =============
    protected Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> _myselfInScopeMap;
    public Map<String, VendorTheLongAndWindingTableAndColumnRefCQ> getMyselfInScope() { return _myselfInScopeMap; }
    public String keepMyselfInScope(VendorTheLongAndWindingTableAndColumnRefCQ subQuery) {
        if (_myselfInScopeMap == null) { _myselfInScopeMap = newLinkedHashMap(); }
        String key = "subQueryMapKey" + (_myselfInScopeMap.size() + 1);
        _myselfInScopeMap.put(key, subQuery); return "myselfInScope." + key;
    }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return VendorTheLongAndWindingTableAndColumnRefCB.class.getName(); }
    protected String xCQ() { return VendorTheLongAndWindingTableAndColumnRefCQ.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
