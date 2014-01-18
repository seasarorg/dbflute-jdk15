/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsbhv;

import java.util.List;

import org.seasar.dbflute.*;
import org.seasar.dbflute.bhv.*;
import org.seasar.dbflute.cbean.*;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.outsidesql.executor.*;
import com.example.dbflute.spring.dbflute.exbhv.*;
import com.example.dbflute.spring.dbflute.exentity.*;
import com.example.dbflute.spring.dbflute.bsentity.dbmeta.*;
import com.example.dbflute.spring.dbflute.cbean.*;

/**
 * The behavior of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN as TABLE. <br />
 * <pre>
 * [primary-key]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID
 * 
 * [column]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME, SHORT_NAME, SHORT_SIZE
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     
 * 
 * [version-no]
 *     
 * 
 * [foreign-table]
 *     
 * 
 * [referrer-table]
 *     VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 *     vendorTheLongAndWindingTableAndColumnRefList
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsVendorTheLongAndWindingTableAndColumnBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:beginQueryPath*/
    /*df:endQueryPath*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return VendorTheLongAndWindingTableAndColumnDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumnDbm getMyDBMeta() { return VendorTheLongAndWindingTableAndColumnDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumn newMyEntity() { return new VendorTheLongAndWindingTableAndColumn(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumnCB newMyConditionBean() { return new VendorTheLongAndWindingTableAndColumnCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count of uniquely-selected records by the condition-bean. {IgnorePagingCondition, IgnoreSpecifyColumn}<br />
     * SpecifyColumn is ignored but you can use it only to remove text type column for union's distinct.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * int count = vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The selected count.
     */
    public int selectCount(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doSelectCountUniquely(cb);
    }

    protected int doSelectCountUniquely(VendorTheLongAndWindingTableAndColumnCB cb) { // called by selectCount(cb) 
        assertCBStateValid(cb);
        return delegateSelectCountUniquely(cb);
    }

    protected int doSelectCountPlainly(VendorTheLongAndWindingTableAndColumnCB cb) { // called by selectPage(cb)
        assertCBStateValid(cb);
        return delegateSelectCountPlainly(cb);
    }

    @Override
    protected int doReadCount(ConditionBean cb) {
        return selectCount(downcast(cb));
    }

    // ===================================================================================
    //                                                                       Cursor Select
    //                                                                       =============
    /**
     * Select the cursor by the condition-bean.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;VendorTheLongAndWindingTableAndColumn&gt;() {
     *     public void handle(VendorTheLongAndWindingTableAndColumn entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param entityRowHandler The handler of entity row of VendorTheLongAndWindingTableAndColumn. (NotNull)
     */
    public void selectCursor(VendorTheLongAndWindingTableAndColumnCB cb, EntityRowHandler<VendorTheLongAndWindingTableAndColumn> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> void doSelectCursor(VendorTheLongAndWindingTableAndColumnCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityRowHandler<VendorTheLongAndWindingTableAndColumn>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (vendorTheLongAndWindingTableAndColumn != null) {
     *     ... = vendorTheLongAndWindingTableAndColumn.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The selected entity. (NullAllowed: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumn selectEntity(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doSelectEntity(cb, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> ENTITY doSelectEntity(final VendorTheLongAndWindingTableAndColumnCB cb, final Class<ENTITY> entityType) {
        assertCBStateValid(cb);
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, VendorTheLongAndWindingTableAndColumnCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = vendorTheLongAndWindingTableAndColumn.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumn selectEntityWithDeletedCheck(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doSelectEntityWithDeletedCheck(cb, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> ENTITY doSelectEntityWithDeletedCheck(final VendorTheLongAndWindingTableAndColumnCB cb, final Class<ENTITY> entityType) {
        assertCBStateValid(cb);
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, VendorTheLongAndWindingTableAndColumnCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntityWithDeletedCheck(ConditionBean cb) {
        return selectEntityWithDeletedCheck(downcast(cb));
    }

    /**
     * Select the entity by the primary-key value.
     * @param theLongAndWindingTableAndColumnId The one of primary key. (NotNull)
     * @return The selected entity. (NullAllowed: If the primary-key value has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumn selectByPKValue(Long theLongAndWindingTableAndColumnId) {
        return doSelectByPKValue(theLongAndWindingTableAndColumnId, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> ENTITY doSelectByPKValue(Long theLongAndWindingTableAndColumnId, Class<ENTITY> entityType) {
        return doSelectEntity(buildPKCB(theLongAndWindingTableAndColumnId), entityType);
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param theLongAndWindingTableAndColumnId The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumn selectByPKValueWithDeletedCheck(Long theLongAndWindingTableAndColumnId) {
        return doSelectByPKValueWithDeletedCheck(theLongAndWindingTableAndColumnId, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> ENTITY doSelectByPKValueWithDeletedCheck(Long theLongAndWindingTableAndColumnId, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(theLongAndWindingTableAndColumnId), entityType);
    }

    private VendorTheLongAndWindingTableAndColumnCB buildPKCB(Long theLongAndWindingTableAndColumnId) {
        assertObjectNotNull("theLongAndWindingTableAndColumnId", theLongAndWindingTableAndColumnId);
        VendorTheLongAndWindingTableAndColumnCB cb = newMyConditionBean();
        cb.query().setTheLongAndWindingTableAndColumnId_Equal(theLongAndWindingTableAndColumnId);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;VendorTheLongAndWindingTableAndColumn&gt; vendorTheLongAndWindingTableAndColumnList = vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn : vendorTheLongAndWindingTableAndColumnList) {
     *     ... = vendorTheLongAndWindingTableAndColumn.get...();
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<VendorTheLongAndWindingTableAndColumn> selectList(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doSelectList(cb, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> ListResultBean<ENTITY> doSelectList(VendorTheLongAndWindingTableAndColumnCB cb, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, VendorTheLongAndWindingTableAndColumnCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
    }

    @Override
    protected ListResultBean<? extends Entity> doReadList(ConditionBean cb) {
        return selectList(downcast(cb));
    }

    // ===================================================================================
    //                                                                         Page Select
    //                                                                         ===========
    /**
     * Select the page as result bean. <br />
     * (both count-select and paging-select are executed)
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;VendorTheLongAndWindingTableAndColumn&gt; page = vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn : page) {
     *     ... = vendorTheLongAndWindingTableAndColumn.get...();
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<VendorTheLongAndWindingTableAndColumn> selectPage(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doSelectPage(cb, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> PagingResultBean<ENTITY> doSelectPage(VendorTheLongAndWindingTableAndColumnCB cb, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, VendorTheLongAndWindingTableAndColumnCB>() {
            public int callbackSelectCount(VendorTheLongAndWindingTableAndColumnCB cb) { return doSelectCountPlainly(cb); }
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
        });
    }

    @Override
    protected PagingResultBean<? extends Entity> doReadPage(ConditionBean cb) {
        return selectPage(downcast(cb));
    }

    // ===================================================================================
    //                                                                       Scalar Select
    //                                                                       =============
    /**
     * Select the scalar value derived by a function from uniquely-selected records. <br />
     * You should call a function method after this method called like as follows:
     * <pre>
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(VendorTheLongAndWindingTableAndColumnCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (NullAllowed)
     */
    public <RESULT> SLFunction<VendorTheLongAndWindingTableAndColumnCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends VendorTheLongAndWindingTableAndColumnCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
        assertObjectNotNull("resultType", resultType); assertCBStateValid(cb);
        cb.xsetupForScalarSelect(); cb.getSqlClause().disableSelectIndex(); // for when you use union
        return new SLFunction<CB, RESULT>(cb, resultType);
    }

    // ===================================================================================
    //                                                                            Sequence
    //                                                                            ========
    @Override
    protected Number doReadNextVal() {
        String msg = "This table is NOT related to sequence: " + getTableDbName();
        throw new UnsupportedOperationException(msg);
    }

    // ===================================================================================
    //                                                                       Load Referrer
    //                                                                       =============
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param vendorTheLongAndWindingTableAndColumn The entity of vendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadVendorTheLongAndWindingTableAndColumnRefList(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, ConditionBeanSetupper<VendorTheLongAndWindingTableAndColumnRefCB> conditionBeanSetupper) {
        xassLRArg(vendorTheLongAndWindingTableAndColumn, conditionBeanSetupper);
        loadVendorTheLongAndWindingTableAndColumnRefList(xnewLRLs(vendorTheLongAndWindingTableAndColumn), conditionBeanSetupper);
    }
    /**
     * Load referrer of vendorTheLongAndWindingTableAndColumnRefList with the set-upper for condition-bean of referrer. <br />
     * VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF by your THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, named 'vendorTheLongAndWindingTableAndColumnRefList'.
     * <pre>
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">loadVendorTheLongAndWindingTableAndColumnRefList</span>(vendorTheLongAndWindingTableAndColumnList, new ConditionBeanSetupper&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt;() {
     *     public void setup(VendorTheLongAndWindingTableAndColumnRefCB cb) {
     *         cb.setupSelect...();
     *         cb.query().setFoo...(value);
     *         cb.query().addOrderBy_Bar...(); <span style="color: #3F7E5E">// basically you should order referrer list</span>
     *     }
     * });
     * for (VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn : vendorTheLongAndWindingTableAndColumnList) {
     *     ... = vendorTheLongAndWindingTableAndColumn.<span style="color: #FD4747">getVendorTheLongAndWindingTableAndColumnRefList()</span>;
     * }
     * </pre>
     * About internal policy, the value of primary key(and others too) is treated as case-insensitive. <br />
     * The condition-bean that the set-upper provides have settings before you touch it. It is as follows:
     * <pre>
     * cb.query().setTheLongAndWindingTableAndColumnId_InScope(pkList);
     * cb.query().addOrderBy_TheLongAndWindingTableAndColumnId_Asc();
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnList The entity list of vendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param conditionBeanSetupper The instance of referrer condition-bean set-upper for registering referrer condition. (NotNull)
     */
    public void loadVendorTheLongAndWindingTableAndColumnRefList(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, ConditionBeanSetupper<VendorTheLongAndWindingTableAndColumnRefCB> conditionBeanSetupper) {
        xassLRArg(vendorTheLongAndWindingTableAndColumnList, conditionBeanSetupper);
        loadVendorTheLongAndWindingTableAndColumnRefList(vendorTheLongAndWindingTableAndColumnList, new LoadReferrerOption<VendorTheLongAndWindingTableAndColumnRefCB, VendorTheLongAndWindingTableAndColumnRef>().xinit(conditionBeanSetupper));
    }
    /**
     * {Refer to overload method that has an argument of the list of entity.}
     * @param vendorTheLongAndWindingTableAndColumn The entity of vendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadVendorTheLongAndWindingTableAndColumnRefList(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, LoadReferrerOption<VendorTheLongAndWindingTableAndColumnRefCB, VendorTheLongAndWindingTableAndColumnRef> loadReferrerOption) {
        xassLRArg(vendorTheLongAndWindingTableAndColumn, loadReferrerOption);
        loadVendorTheLongAndWindingTableAndColumnRefList(xnewLRLs(vendorTheLongAndWindingTableAndColumn), loadReferrerOption);
    }
    /**
     * {Refer to overload method that has an argument of condition-bean setupper.}
     * @param vendorTheLongAndWindingTableAndColumnList The entity list of vendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param loadReferrerOption The option of load-referrer. (NotNull)
     */
    public void loadVendorTheLongAndWindingTableAndColumnRefList(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, LoadReferrerOption<VendorTheLongAndWindingTableAndColumnRefCB, VendorTheLongAndWindingTableAndColumnRef> loadReferrerOption) {
        xassLRArg(vendorTheLongAndWindingTableAndColumnList, loadReferrerOption);
        if (vendorTheLongAndWindingTableAndColumnList.isEmpty()) { return; }
        final VendorTheLongAndWindingTableAndColumnRefBhv referrerBhv = xgetBSFLR().select(VendorTheLongAndWindingTableAndColumnRefBhv.class);
        helpLoadReferrerInternally(vendorTheLongAndWindingTableAndColumnList, loadReferrerOption, new InternalLoadReferrerCallback<VendorTheLongAndWindingTableAndColumn, Long, VendorTheLongAndWindingTableAndColumnRefCB, VendorTheLongAndWindingTableAndColumnRef>() {
            public Long getPKVal(VendorTheLongAndWindingTableAndColumn e)
            { return e.getTheLongAndWindingTableAndColumnId(); }
            public void setRfLs(VendorTheLongAndWindingTableAndColumn e, List<VendorTheLongAndWindingTableAndColumnRef> ls)
            { e.setVendorTheLongAndWindingTableAndColumnRefList(ls); }
            public VendorTheLongAndWindingTableAndColumnRefCB newMyCB() { return referrerBhv.newMyConditionBean(); }
            public void qyFKIn(VendorTheLongAndWindingTableAndColumnRefCB cb, List<Long> ls)
            { cb.query().setTheLongAndWindingTableAndColumnId_InScope(ls); }
            public void qyOdFKAsc(VendorTheLongAndWindingTableAndColumnRefCB cb) { cb.query().addOrderBy_TheLongAndWindingTableAndColumnId_Asc(); }
            public void spFKCol(VendorTheLongAndWindingTableAndColumnRefCB cb) { cb.specify().columnTheLongAndWindingTableAndColumnId(); }
            public List<VendorTheLongAndWindingTableAndColumnRef> selRfLs(VendorTheLongAndWindingTableAndColumnRefCB cb) { return referrerBhv.selectList(cb); }
            public Long getFKVal(VendorTheLongAndWindingTableAndColumnRef e) { return e.getTheLongAndWindingTableAndColumnId(); }
            public void setlcEt(VendorTheLongAndWindingTableAndColumnRef re, VendorTheLongAndWindingTableAndColumn le)
            { re.setVendorTheLongAndWindingTableAndColumn(le); }
        });
    }

    // ===================================================================================
    //                                                                    Pull out Foreign
    //                                                                    ================

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    /**
     * Insert the entity.
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * vendorTheLongAndWindingTableAndColumn.setFoo...(value);
     * vendorTheLongAndWindingTableAndColumn.setBar...(value);
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.set...;</span>
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">insert</span>(vendorTheLongAndWindingTableAndColumn);
     * ... = vendorTheLongAndWindingTableAndColumn.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn) {
        doInsert(vendorTheLongAndWindingTableAndColumn, null);
    }

    protected void doInsert(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumn", vendorTheLongAndWindingTableAndColumn);
        prepareInsertOption(option);
        delegateInsert(vendorTheLongAndWindingTableAndColumn, option);
    }

    protected void prepareInsertOption(InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        if (option == null) { return; }
        assertInsertOptionStatus(option);
    }

    @Override
    protected void doCreate(Entity entity, InsertOption<? extends ConditionBean> option) {
        if (option == null) { insert(downcast(entity)); }
        else { varyingInsert(downcast(entity), downcast(option)); }
    }

    /**
     * Update the entity modified-only. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * vendorTheLongAndWindingTableAndColumn.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * vendorTheLongAndWindingTableAndColumn.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.set...;</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumn.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">update</span>(vendorTheLongAndWindingTableAndColumn);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn) {
        doUpdate(vendorTheLongAndWindingTableAndColumn, null);
    }

    protected void doUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, final UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumn", vendorTheLongAndWindingTableAndColumn);
        prepareUpdateOption(option);
        helpUpdateInternally(vendorTheLongAndWindingTableAndColumn, new InternalUpdateCallback<VendorTheLongAndWindingTableAndColumn>() {
            public int callbackDelegateUpdate(VendorTheLongAndWindingTableAndColumn entity) { return delegateUpdate(entity, option); } });
    }

    protected void prepareUpdateOption(UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        if (option == null) { return; }
        assertUpdateOptionStatus(option);
        if (option.hasSelfSpecification()) {
            option.resolveSelfSpecification(createCBForVaryingUpdate());
        }
        if (option.hasSpecifiedUpdateColumn()) {
            option.resolveUpdateColumnSpecification(createCBForSpecifiedUpdate());
        }
    }

    protected VendorTheLongAndWindingTableAndColumnCB createCBForVaryingUpdate() {
        VendorTheLongAndWindingTableAndColumnCB cb = newMyConditionBean();
        cb.xsetupForVaryingUpdate();
        return cb;
    }

    protected VendorTheLongAndWindingTableAndColumnCB createCBForSpecifiedUpdate() {
        VendorTheLongAndWindingTableAndColumnCB cb = newMyConditionBean();
        cb.xsetupForSpecifiedUpdate();
        return cb;
    }

    @Override
    protected void doModify(Entity entity, UpdateOption<? extends ConditionBean> option) {
        if (option == null) { update(downcast(entity)); }
        else { varyingUpdate(downcast(entity), downcast(option)); }
    }

    @Override
    protected void doModifyNonstrict(Entity entity, UpdateOption<? extends ConditionBean> option) {
        doModify(entity, option);
    }

    /**
     * Insert or update the entity modified-only. {ExclusiveControl(when update)}
     * @param vendorTheLongAndWindingTableAndColumn The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn) {
        doInesrtOrUpdate(vendorTheLongAndWindingTableAndColumn, null, null);
    }

    protected void doInesrtOrUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, final InsertOption<VendorTheLongAndWindingTableAndColumnCB> insertOption, final UpdateOption<VendorTheLongAndWindingTableAndColumnCB> updateOption) {
        helpInsertOrUpdateInternally(vendorTheLongAndWindingTableAndColumn, new InternalInsertOrUpdateCallback<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB>() {
            public void callbackInsert(VendorTheLongAndWindingTableAndColumn entity) { doInsert(entity, insertOption); }
            public void callbackUpdate(VendorTheLongAndWindingTableAndColumn entity) { doUpdate(entity, updateOption); }
            public VendorTheLongAndWindingTableAndColumnCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(VendorTheLongAndWindingTableAndColumnCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrModify(Entity entity, InsertOption<? extends ConditionBean> insertOption,
            UpdateOption<? extends ConditionBean> updateOption) {
        if (insertOption == null && updateOption == null) { insertOrUpdate(downcast(entity)); }
        else {
            insertOption = insertOption == null ? new InsertOption<VendorTheLongAndWindingTableAndColumnCB>() : insertOption;
            updateOption = updateOption == null ? new UpdateOption<VendorTheLongAndWindingTableAndColumnCB>() : updateOption;
            varyingInsertOrUpdate(downcast(entity), downcast(insertOption), downcast(updateOption));
        }
    }

    @Override
    protected void doCreateOrModifyNonstrict(Entity entity, InsertOption<? extends ConditionBean> insertOption,
            UpdateOption<? extends ConditionBean> updateOption) {
        doCreateOrModify(entity, insertOption, updateOption);
    }

    /**
     * Delete the entity. {UpdateCountZeroException, ExclusiveControl}
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * vendorTheLongAndWindingTableAndColumn.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumn.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">delete</span>(vendorTheLongAndWindingTableAndColumn);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn) {
        doDelete(vendorTheLongAndWindingTableAndColumn, null);
    }

    protected void doDelete(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, final DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumn", vendorTheLongAndWindingTableAndColumn);
        prepareDeleteOption(option);
        helpDeleteInternally(vendorTheLongAndWindingTableAndColumn, new InternalDeleteCallback<VendorTheLongAndWindingTableAndColumn>() {
            public int callbackDelegateDelete(VendorTheLongAndWindingTableAndColumn entity) { return delegateDelete(entity, option); } });
    }

    protected void prepareDeleteOption(DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        if (option == null) { return; }
        assertDeleteOptionStatus(option);
    }

    @Override
    protected void doRemove(Entity entity, DeleteOption<? extends ConditionBean> option) {
        if (option == null) { delete(downcast(entity)); }
        else { varyingDelete(downcast(entity), downcast(option)); }
    }

    @Override
    protected void doRemoveNonstrict(Entity entity, DeleteOption<? extends ConditionBean> option) {
        doRemove(entity, option);
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    /**
     * Batch-insert the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement. <br />
     * All columns are insert target. (so default constraints are not available) <br />
     * And if the table has an identity, entities after the process do not have incremented values.
     * (When you use the (normal) insert(), an entity after the process has an incremented value)
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList) {
        return doBatchInsert(vendorTheLongAndWindingTableAndColumnList, null);
    }

    protected int[] doBatchInsert(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnList", vendorTheLongAndWindingTableAndColumnList);
        prepareInsertOption(option);
        return delegateBatchInsert(vendorTheLongAndWindingTableAndColumnList, option);
    }

    @Override
    protected int[] doLumpCreate(List<Entity> ls, InsertOption<? extends ConditionBean> option) {
        if (option == null) { return batchInsert(downcast(ls)); }
        else { return varyingBatchInsert(downcast(ls), downcast(option)); }
    }

    /**
     * Batch-update the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement. <br />
     * All columns are update target. {NOT modified only}
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdate(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList) {
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnList, null);
    }

    protected int[] doBatchUpdate(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnList", vendorTheLongAndWindingTableAndColumnList);
        prepareUpdateOption(option);
        return delegateBatchUpdate(vendorTheLongAndWindingTableAndColumnList, option);
    }

    @Override
    protected int[] doLumpModify(List<Entity> ls, UpdateOption<? extends ConditionBean> option) {
        if (option == null) { return batchUpdate(downcast(ls)); }
        else { return varyingBatchUpdate(downcast(ls), downcast(option)); }
    }

    /**
     * Batch-update the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement. <br />
     * You can specify update columns used on set clause of update statement.
     * However you do not need to specify common columns for update
     * and an optimistick lock column because they are specified implicitly.
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @param updateColumnSpec The specification of update columns. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdate(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, SpecifyQuery<VendorTheLongAndWindingTableAndColumnCB> updateColumnSpec) {
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnList, createSpecifiedUpdateOption(updateColumnSpec));
    }

    @Override
    protected int[] doLumpModifyNonstrict(List<Entity> ls, UpdateOption<? extends ConditionBean> option) {
        return doLumpModify(ls, option);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDelete(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList) {
        return doBatchDelete(vendorTheLongAndWindingTableAndColumnList, null);
    }

    protected int[] doBatchDelete(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnList", vendorTheLongAndWindingTableAndColumnList);
        prepareDeleteOption(option);
        return delegateBatchDelete(vendorTheLongAndWindingTableAndColumnList, option);
    }

    @Override
    protected int[] doLumpRemove(List<Entity> ls, DeleteOption<? extends ConditionBean> option) {
        if (option == null) { return batchDelete(downcast(ls)); }
        else { return varyingBatchDelete(downcast(ls), downcast(option)); }
    }

    @Override
    protected int[] doLumpRemoveNonstrict(List<Entity> ls, DeleteOption<? extends ConditionBean> option) {
        return doLumpRemove(ls, option);
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    /**
     * Insert the several entities by query (modified-only for fixed value).
     * <pre>
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">queryInsert</span>(new QueryInsertSetupper&lt;VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB&gt;() {
     *     public ConditionBean setup(vendorTheLongAndWindingTableAndColumn entity, VendorTheLongAndWindingTableAndColumnCB intoCB) {
     *         FooCB cb = FooCB();
     *         cb.setupSelect_Bar();
     * 
     *         <span style="color: #3F7E5E">// mapping</span>
     *         intoCB.specify().columnMyName().mappedFrom(cb.specify().columnFooName());
     *         intoCB.specify().columnMyCount().mappedFrom(cb.specify().columnFooCount());
     *         intoCB.specify().columnMyDate().mappedFrom(cb.specify().specifyBar().columnBarDate());
     *         entity.setMyFixedValue("foo"); <span style="color: #3F7E5E">// fixed value</span>
     *         <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     *         <span style="color: #3F7E5E">//entity.setRegisterUser(value);</span>
     *         <span style="color: #3F7E5E">//entity.set...;</span>
     *         <span style="color: #3F7E5E">// you don't need to set a value of exclusive control column</span>
     *         <span style="color: #3F7E5E">//entity.setVersionNo(value);</span>
     * 
     *         return cb;
     *     }
     * });
     * </pre>
     * @param setupper The setup-per of query-insert. (NotNull)
     * @return The inserted count.
     */
    public int queryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB> setupper) {
        return doQueryInsert(setupper, null);
    }

    protected int doQueryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB> setupper, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("setupper", setupper);
        prepareInsertOption(option);
        VendorTheLongAndWindingTableAndColumn entity = new VendorTheLongAndWindingTableAndColumn();
        VendorTheLongAndWindingTableAndColumnCB intoCB = createCBForQueryInsert();
        ConditionBean resourceCB = setupper.setup(entity, intoCB);
        return delegateQueryInsert(entity, intoCB, resourceCB, option);
    }

    protected VendorTheLongAndWindingTableAndColumnCB createCBForQueryInsert() {
        VendorTheLongAndWindingTableAndColumnCB cb = newMyConditionBean();
        cb.xsetupForQueryInsert();
        return cb;
    }

    @Override
    protected int doRangeCreate(QueryInsertSetupper<? extends Entity, ? extends ConditionBean> setupper, InsertOption<? extends ConditionBean> option) {
        if (option == null) { return queryInsert(downcast(setupper)); }
        else { return varyingQueryInsert(downcast(setupper), downcast(option)); }
    }

    /**
     * Update the several entities by query non-strictly modified-only. {NonExclusiveControl}
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setPK...(value);</span>
     * vendorTheLongAndWindingTableAndColumn.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.set...;</span>
     * <span style="color: #3F7E5E">// you don't need to set a value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setVersionNo(value);</span>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">queryUpdate</span>(vendorTheLongAndWindingTableAndColumn, cb);
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity that contains update values. (NotNull, PrimaryKeyNullAllowed)
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The updated count.
     * @exception org.seasar.dbflute.exception.NonQueryUpdateNotAllowedException When the query has no condition.
     */
    public int queryUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB cb) {
        return doQueryUpdate(vendorTheLongAndWindingTableAndColumn, cb, null);
    }

    protected int doQueryUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumn", vendorTheLongAndWindingTableAndColumn); assertCBStateValid(cb);
        prepareUpdateOption(option);
        return delegateQueryUpdate(vendorTheLongAndWindingTableAndColumn, cb, option);
    }

    @Override
    protected int doRangeModify(Entity entity, ConditionBean cb, UpdateOption<? extends ConditionBean> option) {
        if (option == null) { return queryUpdate(downcast(entity), (VendorTheLongAndWindingTableAndColumnCB)cb); }
        else { return varyingQueryUpdate(downcast(entity), (VendorTheLongAndWindingTableAndColumnCB)cb, downcast(option)); }
    }

    /**
     * Delete the several entities by query. {NonExclusiveControl}
     * <pre>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">queryDelete</span>(vendorTheLongAndWindingTableAndColumn, cb);
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @return The deleted count.
     * @exception org.seasar.dbflute.exception.NonQueryDeleteNotAllowedException When the query has no condition.
     */
    public int queryDelete(VendorTheLongAndWindingTableAndColumnCB cb) {
        return doQueryDelete(cb, null);
    }

    protected int doQueryDelete(VendorTheLongAndWindingTableAndColumnCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertCBStateValid(cb);
        prepareDeleteOption(option);
        return delegateQueryDelete(cb, option);
    }

    @Override
    protected int doRangeRemove(ConditionBean cb, DeleteOption<? extends ConditionBean> option) {
        if (option == null) { return queryDelete((VendorTheLongAndWindingTableAndColumnCB)cb); }
        else { return varyingQueryDelete((VendorTheLongAndWindingTableAndColumnCB)cb, downcast(option)); }
    }

    // ===================================================================================
    //                                                                      Varying Update
    //                                                                      ==============
    // -----------------------------------------------------
    //                                         Entity Update
    //                                         -------------
    /**
     * Insert the entity with varying requests. <br />
     * For example, disableCommonColumnAutoSetup(), disablePrimaryKeyIdentity(). <br />
     * Other specifications are same as insert(entity).
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * vendorTheLongAndWindingTableAndColumn.setFoo...(value);
     * vendorTheLongAndWindingTableAndColumn.setBar...(value);
     * InsertOption<VendorTheLongAndWindingTableAndColumnCB> option = new InsertOption<VendorTheLongAndWindingTableAndColumnCB>();
     * <span style="color: #3F7E5E">// you can insert by your values for common columns</span>
     * option.disableCommonColumnAutoSetup();
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">varyingInsert</span>(vendorTheLongAndWindingTableAndColumn, option);
     * ... = vendorTheLongAndWindingTableAndColumn.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity of insert target. (NotNull)
     * @param option The option of insert for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingInsert(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertInsertOptionNotNull(option);
        doInsert(vendorTheLongAndWindingTableAndColumn, option);
    }

    /**
     * Update the entity with varying requests modified-only. {UpdateCountZeroException, ExclusiveControl} <br />
     * For example, self(selfCalculationSpecification), specify(updateColumnSpecification), disableCommonColumnAutoSetup(). <br />
     * Other specifications are same as update(entity).
     * <pre>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * vendorTheLongAndWindingTableAndColumn.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * vendorTheLongAndWindingTableAndColumn.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumn.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     <span style="color: #3F7E5E">// you can update by self calculation values</span>
     *     UpdateOption&lt;VendorTheLongAndWindingTableAndColumnCB&gt; option = new UpdateOption&lt;VendorTheLongAndWindingTableAndColumnCB&gt;();
     *     option.self(new SpecifyQuery&lt;VendorTheLongAndWindingTableAndColumnCB&gt;() {
     *         public void specify(VendorTheLongAndWindingTableAndColumnCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">varyingUpdate</span>(vendorTheLongAndWindingTableAndColumn, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertUpdateOptionNotNull(option);
        doUpdate(vendorTheLongAndWindingTableAndColumn, option);
    }

    /**
     * Insert or update the entity with varying requests. {ExclusiveControl(when update)}<br />
     * Other specifications are same as insertOrUpdate(entity).
     * @param vendorTheLongAndWindingTableAndColumn The entity of insert or update target. (NotNull)
     * @param insertOption The option of insert for varying requests. (NotNull)
     * @param updateOption The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingInsertOrUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, InsertOption<VendorTheLongAndWindingTableAndColumnCB> insertOption, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> updateOption) {
        assertInsertOptionNotNull(insertOption); assertUpdateOptionNotNull(updateOption);
        doInesrtOrUpdate(vendorTheLongAndWindingTableAndColumn, insertOption, updateOption);
    }

    /**
     * Delete the entity with varying requests. {UpdateCountZeroException, ExclusiveControl} <br />
     * Now a valid option does not exist. <br />
     * Other specifications are same as delete(entity).
     * @param vendorTheLongAndWindingTableAndColumn The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void varyingDelete(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertDeleteOptionNotNull(option);
        doDelete(vendorTheLongAndWindingTableAndColumn, option);
    }

    // -----------------------------------------------------
    //                                          Batch Update
    //                                          ------------
    /**
     * Batch-insert the list with varying requests. <br />
     * For example, disableCommonColumnAutoSetup()
     * , disablePrimaryKeyIdentity(), limitBatchInsertLogging(). <br />
     * Other specifications are same as batchInsert(entityList).
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @param option The option of insert for varying requests. (NotNull)
     * @return The array of inserted count.
     */
    public int[] varyingBatchInsert(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertInsertOptionNotNull(option);
        return doBatchInsert(vendorTheLongAndWindingTableAndColumnList, option);
    }

    /**
     * Batch-update the list with varying requests. <br />
     * For example, self(selfCalculationSpecification), specify(updateColumnSpecification)
     * , disableCommonColumnAutoSetup(), limitBatchUpdateLogging(). <br />
     * Other specifications are same as batchUpdate(entityList).
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @param option The option of update for varying requests. (NotNull)
     * @return The array of updated count.
     */
    public int[] varyingBatchUpdate(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertUpdateOptionNotNull(option);
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnList, option);
    }

    /**
     * Batch-delete the list with varying requests. <br />
     * For example, limitBatchDeleteLogging(). <br />
     * Other specifications are same as batchDelete(entityList).
     * @param vendorTheLongAndWindingTableAndColumnList The list of the entity. (NotNull)
     * @param option The option of delete for varying requests. (NotNull)
     * @return The array of deleted count.
     */
    public int[] varyingBatchDelete(List<VendorTheLongAndWindingTableAndColumn> vendorTheLongAndWindingTableAndColumnList, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertDeleteOptionNotNull(option);
        return doBatchDelete(vendorTheLongAndWindingTableAndColumnList, option);
    }

    // -----------------------------------------------------
    //                                          Query Update
    //                                          ------------
    /**
     * Insert the several entities by query with varying requests (modified-only for fixed value). <br />
     * For example, disableCommonColumnAutoSetup(), disablePrimaryKeyIdentity(). <br />
     * Other specifications are same as queryInsert(entity, setupper). 
     * @param setupper The setup-per of query-insert. (NotNull)
     * @param option The option of insert for varying requests. (NotNull)
     * @return The inserted count.
     */
    public int varyingQueryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB> setupper, InsertOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertInsertOptionNotNull(option);
        return doQueryInsert(setupper, option);
    }

    /**
     * Update the several entities by query with varying requests non-strictly modified-only. {NonExclusiveControl} <br />
     * For example, self(selfCalculationSpecification), specify(updateColumnSpecification)
     * , disableCommonColumnAutoSetup(), allowNonQueryUpdate(). <br />
     * Other specifications are same as queryUpdate(entity, cb). 
     * <pre>
     * <span style="color: #3F7E5E">// ex) you can update by self calculation values</span>
     * VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn = new VendorTheLongAndWindingTableAndColumn();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setPK...(value);</span>
     * vendorTheLongAndWindingTableAndColumn.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set a value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumn.setVersionNo(value);</span>
     * VendorTheLongAndWindingTableAndColumnCB cb = new VendorTheLongAndWindingTableAndColumnCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;VendorTheLongAndWindingTableAndColumnCB&gt; option = new UpdateOption&lt;VendorTheLongAndWindingTableAndColumnCB&gt;();
     * option.self(new SpecifyQuery&lt;VendorTheLongAndWindingTableAndColumnCB&gt;() {
     *     public void specify(VendorTheLongAndWindingTableAndColumnCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * vendorTheLongAndWindingTableAndColumnBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(vendorTheLongAndWindingTableAndColumn, cb, option);
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumn The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param option The option of update for varying requests. (NotNull)
     * @return The updated count.
     * @exception org.seasar.dbflute.exception.NonQueryUpdateNotAllowedException When the query has no condition (if not allowed).
     */
    public int varyingQueryUpdate(VendorTheLongAndWindingTableAndColumn vendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertUpdateOptionNotNull(option);
        return doQueryUpdate(vendorTheLongAndWindingTableAndColumn, cb, option);
    }

    /**
     * Delete the several entities by query with varying requests non-strictly. <br />
     * For example, allowNonQueryDelete(). <br />
     * Other specifications are same as batchUpdateNonstrict(entityList).
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumn. (NotNull)
     * @param option The option of delete for varying requests. (NotNull)
     * @return The deleted count.
     * @exception org.seasar.dbflute.exception.NonQueryDeleteNotAllowedException When the query has no condition (if not allowed).
     */
    public int varyingQueryDelete(VendorTheLongAndWindingTableAndColumnCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> option) {
        assertDeleteOptionNotNull(option);
        return doQueryDelete(cb, option);
    }

    // ===================================================================================
    //                                                                          OutsideSql
    //                                                                          ==========
    /**
     * Prepare the basic executor of outside-SQL to execute it. <br />
     * The invoker of behavior command should be not null when you call this method.
     * <pre>
     * You can use the methods for outside-SQL are as follows:
     * {Basic}
     *   o selectList()
     *   o execute()
     *   o call()
     * 
     * {Entity}
     *   o entityHandling().selectEntity()
     *   o entityHandling().selectEntityWithDeletedCheck()
     * 
     * {Paging}
     *   o autoPaging().selectList()
     *   o autoPaging().selectPage()
     *   o manualPaging().selectList()
     *   o manualPaging().selectPage()
     * 
     * {Cursor}
     *   o cursorHandling().selectCursor()
     * 
     * {Option}
     *   o dynamicBinding().selectList()
     *   o removeBlockComment().selectList()
     *   o removeLineComment().selectList()
     *   o formatSql().selectList()
     * </pre>
     * @return The basic executor of outside-SQL. (NotNull) 
     */
    public OutsideSqlBasicExecutor<VendorTheLongAndWindingTableAndColumnBhv> outsideSql() {
        return doOutsideSql();
    }

    // ===================================================================================
    //                                                                     Delegate Method
    //                                                                     ===============
    // [Behavior Command]
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    protected int delegateSelectCountUniquely(VendorTheLongAndWindingTableAndColumnCB cb) { return invoke(createSelectCountCBCommand(cb, true)); }
    protected int delegateSelectCountPlainly(VendorTheLongAndWindingTableAndColumnCB cb) { return invoke(createSelectCountCBCommand(cb, false)); }
    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> void delegateSelectCursor(VendorTheLongAndWindingTableAndColumnCB cb, EntityRowHandler<ENTITY> erh, Class<ENTITY> et)
    { invoke(createSelectCursorCBCommand(cb, erh, et)); }
    protected <ENTITY extends VendorTheLongAndWindingTableAndColumn> List<ENTITY> delegateSelectList(VendorTheLongAndWindingTableAndColumnCB cb, Class<ENTITY> et)
    { return invoke(createSelectListCBCommand(cb, et)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(VendorTheLongAndWindingTableAndColumn e, InsertOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeInsert(e, op)) { return 0; }
      return invoke(createInsertEntityCommand(e, op)); }
    protected int delegateUpdate(VendorTheLongAndWindingTableAndColumn e, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeUpdate(e, op)) { return 0; }
      return delegateUpdateNonstrict(e, op); }
    protected int delegateUpdateNonstrict(VendorTheLongAndWindingTableAndColumn e, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeUpdate(e, op)) { return 0; }
      return invoke(createUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateDelete(VendorTheLongAndWindingTableAndColumn e, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeDelete(e, op)) { return 0; }
      return delegateDeleteNonstrict(e, op); }
    protected int delegateDeleteNonstrict(VendorTheLongAndWindingTableAndColumn e, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeDelete(e, op)) { return 0; }
      return invoke(createDeleteNonstrictEntityCommand(e, op)); }

    protected int[] delegateBatchInsert(List<VendorTheLongAndWindingTableAndColumn> ls, InsertOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchInsertCommand(processBatchInternally(ls, op), op)); }
    protected int[] delegateBatchUpdate(List<VendorTheLongAndWindingTableAndColumn> ls, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return delegateBatchUpdateNonstrict(ls, op); }
    protected int[] delegateBatchUpdateNonstrict(List<VendorTheLongAndWindingTableAndColumn> ls, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchUpdateNonstrictCommand(processBatchInternally(ls, op, true), op)); }
    protected int[] delegateBatchDelete(List<VendorTheLongAndWindingTableAndColumn> ls, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return delegateBatchDeleteNonstrict(ls, op); }
    protected int[] delegateBatchDeleteNonstrict(List<VendorTheLongAndWindingTableAndColumn> ls, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchDeleteNonstrictCommand(processBatchInternally(ls, op, true), op)); }

    protected int delegateQueryInsert(VendorTheLongAndWindingTableAndColumn e, VendorTheLongAndWindingTableAndColumnCB inCB, ConditionBean resCB, InsertOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeQueryInsert(e, inCB, resCB, op)) { return 0; } return invoke(createQueryInsertCBCommand(e, inCB, resCB, op));  }
    protected int delegateQueryUpdate(VendorTheLongAndWindingTableAndColumn e, VendorTheLongAndWindingTableAndColumnCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeQueryUpdate(e, cb, op)) { return 0; } return invoke(createQueryUpdateCBCommand(e, cb, op));  }
    protected int delegateQueryDelete(VendorTheLongAndWindingTableAndColumnCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnCB> op)
    { if (!processBeforeQueryDelete(cb, op)) { return 0; } return invoke(createQueryDeleteCBCommand(cb, op));  }

    // ===================================================================================
    //                                                                Optimistic Lock Info
    //                                                                ====================
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasVersionNoValue(Entity entity) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasUpdateDateValue(Entity entity) {
        return false;
    }

    // ===================================================================================
    //                                                                     Downcast Helper
    //                                                                     ===============
    protected VendorTheLongAndWindingTableAndColumn downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, VendorTheLongAndWindingTableAndColumn.class);
    }

    protected VendorTheLongAndWindingTableAndColumnCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, VendorTheLongAndWindingTableAndColumnCB.class);
    }

    @SuppressWarnings("unchecked")
    protected List<VendorTheLongAndWindingTableAndColumn> downcast(List<? extends Entity> entityList) {
        return (List<VendorTheLongAndWindingTableAndColumn>)entityList;
    }

    @SuppressWarnings("unchecked")
    protected InsertOption<VendorTheLongAndWindingTableAndColumnCB> downcast(InsertOption<? extends ConditionBean> option) {
        return (InsertOption<VendorTheLongAndWindingTableAndColumnCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected UpdateOption<VendorTheLongAndWindingTableAndColumnCB> downcast(UpdateOption<? extends ConditionBean> option) {
        return (UpdateOption<VendorTheLongAndWindingTableAndColumnCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected DeleteOption<VendorTheLongAndWindingTableAndColumnCB> downcast(DeleteOption<? extends ConditionBean> option) {
        return (DeleteOption<VendorTheLongAndWindingTableAndColumnCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected QueryInsertSetupper<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB> downcast(QueryInsertSetupper<? extends Entity, ? extends ConditionBean> option) {
        return (QueryInsertSetupper<VendorTheLongAndWindingTableAndColumn, VendorTheLongAndWindingTableAndColumnCB>)option;
    }
}
