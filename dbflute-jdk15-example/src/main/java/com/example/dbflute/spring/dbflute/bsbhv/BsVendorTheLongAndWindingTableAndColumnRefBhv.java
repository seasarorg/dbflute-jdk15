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
 * The behavior of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF as TABLE. <br />
 * <pre>
 * [primary-key]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID
 * 
 * [column]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID, THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE, SHORT_DATE
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
 *     VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     vendorTheLongAndWindingTableAndColumn
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsVendorTheLongAndWindingTableAndColumnRefBhv extends AbstractBehaviorWritable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /*df:beginQueryPath*/
    /*df:endQueryPath*/

    // ===================================================================================
    //                                                                          Table name
    //                                                                          ==========
    /** @return The name on database of table. (NotNull) */
    public String getTableDbName() { return "VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF"; }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /** @return The instance of DBMeta. (NotNull) */
    public DBMeta getDBMeta() { return VendorTheLongAndWindingTableAndColumnRefDbm.getInstance(); }

    /** @return The instance of DBMeta as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumnRefDbm getMyDBMeta() { return VendorTheLongAndWindingTableAndColumnRefDbm.getInstance(); }

    // ===================================================================================
    //                                                                        New Instance
    //                                                                        ============
    /** {@inheritDoc} */
    public Entity newEntity() { return newMyEntity(); }

    /** {@inheritDoc} */
    public ConditionBean newConditionBean() { return newMyConditionBean(); }

    /** @return The instance of new entity as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumnRef newMyEntity() { return new VendorTheLongAndWindingTableAndColumnRef(); }

    /** @return The instance of new condition-bean as my table type. (NotNull) */
    public VendorTheLongAndWindingTableAndColumnRefCB newMyConditionBean() { return new VendorTheLongAndWindingTableAndColumnRefCB(); }

    // ===================================================================================
    //                                                                        Count Select
    //                                                                        ============
    /**
     * Select the count of uniquely-selected records by the condition-bean. {IgnorePagingCondition, IgnoreSpecifyColumn}<br />
     * SpecifyColumn is ignored but you can use it only to remove text type column for union's distinct.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * int count = vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectCount</span>(cb);
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The selected count.
     */
    public int selectCount(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doSelectCountUniquely(cb);
    }

    protected int doSelectCountUniquely(VendorTheLongAndWindingTableAndColumnRefCB cb) { // called by selectCount(cb) 
        assertCBStateValid(cb);
        return delegateSelectCountUniquely(cb);
    }

    protected int doSelectCountPlainly(VendorTheLongAndWindingTableAndColumnRefCB cb) { // called by selectPage(cb)
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
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectCursor</span>(cb, new EntityRowHandler&lt;VendorTheLongAndWindingTableAndColumnRef&gt;() {
     *     public void handle(VendorTheLongAndWindingTableAndColumnRef entity) {
     *         ... = entity.getFoo...();
     *     }
     * });
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @param entityRowHandler The handler of entity row of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     */
    public void selectCursor(VendorTheLongAndWindingTableAndColumnRefCB cb, EntityRowHandler<VendorTheLongAndWindingTableAndColumnRef> entityRowHandler) {
        doSelectCursor(cb, entityRowHandler, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> void doSelectCursor(VendorTheLongAndWindingTableAndColumnRefCB cb, EntityRowHandler<ENTITY> entityRowHandler, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityRowHandler<VendorTheLongAndWindingTableAndColumnRef>", entityRowHandler); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        delegateSelectCursor(cb, entityRowHandler, entityType);
    }

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    /**
     * Select the entity by the condition-bean.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectEntity</span>(cb);
     * if (vendorTheLongAndWindingTableAndColumnRef != null) {
     *     ... = vendorTheLongAndWindingTableAndColumnRef.get...();
     * } else {
     *     ...
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The selected entity. (NullAllowed: If the condition has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumnRef selectEntity(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doSelectEntity(cb, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> ENTITY doSelectEntity(final VendorTheLongAndWindingTableAndColumnRefCB cb, final Class<ENTITY> entityType) {
        assertCBStateValid(cb);
        return helpSelectEntityInternally(cb, new InternalSelectEntityCallback<ENTITY, VendorTheLongAndWindingTableAndColumnRefCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntity(ConditionBean cb) {
        return selectEntity(downcast(cb));
    }

    /**
     * Select the entity by the condition-bean with deleted check.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectEntityWithDeletedCheck</span>(cb);
     * ... = vendorTheLongAndWindingTableAndColumnRef.get...(); <span style="color: #3F7E5E">// the entity always be not null</span>
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumnRef selectEntityWithDeletedCheck(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doSelectEntityWithDeletedCheck(cb, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> ENTITY doSelectEntityWithDeletedCheck(final VendorTheLongAndWindingTableAndColumnRefCB cb, final Class<ENTITY> entityType) {
        assertCBStateValid(cb);
        return helpSelectEntityWithDeletedCheckInternally(cb, new InternalSelectEntityWithDeletedCheckCallback<ENTITY, VendorTheLongAndWindingTableAndColumnRefCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb) { return doSelectList(cb, entityType); } });
    }

    @Override
    protected Entity doReadEntityWithDeletedCheck(ConditionBean cb) {
        return selectEntityWithDeletedCheck(downcast(cb));
    }

    /**
     * Select the entity by the primary-key value.
     * @param theLongAndWindingTableAndColumnRefId The one of primary key. (NotNull)
     * @return The selected entity. (NullAllowed: If the primary-key value has no data, it returns null)
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumnRef selectByPKValue(Long theLongAndWindingTableAndColumnRefId) {
        return doSelectByPKValue(theLongAndWindingTableAndColumnRefId, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> ENTITY doSelectByPKValue(Long theLongAndWindingTableAndColumnRefId, Class<ENTITY> entityType) {
        return doSelectEntity(buildPKCB(theLongAndWindingTableAndColumnRefId), entityType);
    }

    /**
     * Select the entity by the primary-key value with deleted check.
     * @param theLongAndWindingTableAndColumnRefId The one of primary key. (NotNull)
     * @return The selected entity. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.SelectEntityConditionNotFoundException When the condition for selecting an entity is not found.
     */
    public VendorTheLongAndWindingTableAndColumnRef selectByPKValueWithDeletedCheck(Long theLongAndWindingTableAndColumnRefId) {
        return doSelectByPKValueWithDeletedCheck(theLongAndWindingTableAndColumnRefId, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> ENTITY doSelectByPKValueWithDeletedCheck(Long theLongAndWindingTableAndColumnRefId, Class<ENTITY> entityType) {
        return doSelectEntityWithDeletedCheck(buildPKCB(theLongAndWindingTableAndColumnRefId), entityType);
    }

    private VendorTheLongAndWindingTableAndColumnRefCB buildPKCB(Long theLongAndWindingTableAndColumnRefId) {
        assertObjectNotNull("theLongAndWindingTableAndColumnRefId", theLongAndWindingTableAndColumnRefId);
        VendorTheLongAndWindingTableAndColumnRefCB cb = newMyConditionBean();
        cb.query().setTheLongAndWindingTableAndColumnRefId_Equal(theLongAndWindingTableAndColumnRefId);
        return cb;
    }

    // ===================================================================================
    //                                                                         List Select
    //                                                                         ===========
    /**
     * Select the list as result bean.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * ListResultBean&lt;VendorTheLongAndWindingTableAndColumnRef&gt; vendorTheLongAndWindingTableAndColumnRefList = vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectList</span>(cb);
     * for (VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef : vendorTheLongAndWindingTableAndColumnRefList) {
     *     ... = vendorTheLongAndWindingTableAndColumnRef.get...();
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The result bean of selected list. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public ListResultBean<VendorTheLongAndWindingTableAndColumnRef> selectList(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doSelectList(cb, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> ListResultBean<ENTITY> doSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityType", entityType);
        assertSpecifyDerivedReferrerEntityProperty(cb, entityType);
        return helpSelectListInternally(cb, entityType, new InternalSelectListCallback<ENTITY, VendorTheLongAndWindingTableAndColumnRefCB>() {
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb, Class<ENTITY> entityType) { return delegateSelectList(cb, entityType); } });
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
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * cb.query().addOrderBy_Bar...();
     * cb.<span style="color: #FD4747">paging</span>(20, 3); <span style="color: #3F7E5E">// 20 records per a page and current page number is 3</span>
     * PagingResultBean&lt;VendorTheLongAndWindingTableAndColumnRef&gt; page = vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">selectPage</span>(cb);
     * int allRecordCount = page.getAllRecordCount();
     * int allPageCount = page.getAllPageCount();
     * boolean isExistPrePage = page.isExistPrePage();
     * boolean isExistNextPage = page.isExistNextPage();
     * ...
     * for (VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef : page) {
     *     ... = vendorTheLongAndWindingTableAndColumnRef.get...();
     * }
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The result bean of selected page. (NotNull)
     * @exception org.seasar.dbflute.exception.DangerousResultSizeException When the result size is over the specified safety size.
     */
    public PagingResultBean<VendorTheLongAndWindingTableAndColumnRef> selectPage(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doSelectPage(cb, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> PagingResultBean<ENTITY> doSelectPage(VendorTheLongAndWindingTableAndColumnRefCB cb, Class<ENTITY> entityType) {
        assertCBStateValid(cb); assertObjectNotNull("entityType", entityType);
        return helpSelectPageInternally(cb, entityType, new InternalSelectPageCallback<ENTITY, VendorTheLongAndWindingTableAndColumnRefCB>() {
            public int callbackSelectCount(VendorTheLongAndWindingTableAndColumnRefCB cb) { return doSelectCountPlainly(cb); }
            public List<ENTITY> callbackSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb, Class<ENTITY> entityType) { return doSelectList(cb, entityType); }
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
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">scalarSelect</span>(Date.class).max(new ScalarQuery() {
     *     public void query(VendorTheLongAndWindingTableAndColumnRefCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooDatetime()</span>; <span style="color: #3F7E5E">// required for a function</span>
     *         cb.query().setBarName_PrefixSearch("S");
     *     }
     * });
     * </pre>
     * @param <RESULT> The type of result.
     * @param resultType The type of result. (NotNull)
     * @return The scalar value derived by a function. (NullAllowed)
     */
    public <RESULT> SLFunction<VendorTheLongAndWindingTableAndColumnRefCB, RESULT> scalarSelect(Class<RESULT> resultType) {
        return doScalarSelect(resultType, newMyConditionBean());
    }

    protected <RESULT, CB extends VendorTheLongAndWindingTableAndColumnRefCB> SLFunction<CB, RESULT> doScalarSelect(Class<RESULT> resultType, CB cb) {
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
    //                                                                    Pull out Foreign
    //                                                                    ================
    /**
     * Pull out the list of foreign table 'VendorTheLongAndWindingTableAndColumn'.
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of vendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The list of foreign table. (NotNull)
     */
    public List<VendorTheLongAndWindingTableAndColumn> pulloutVendorTheLongAndWindingTableAndColumn(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList) {
        return helpPulloutInternally(vendorTheLongAndWindingTableAndColumnRefList, new InternalPulloutCallback<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumn>() {
            public VendorTheLongAndWindingTableAndColumn getFr(VendorTheLongAndWindingTableAndColumnRef e) { return e.getVendorTheLongAndWindingTableAndColumn(); }
            public boolean hasRf() { return true; }
            public void setRfLs(VendorTheLongAndWindingTableAndColumn e, List<VendorTheLongAndWindingTableAndColumnRef> ls)
            { e.setVendorTheLongAndWindingTableAndColumnRefList(ls); }
        });
    }

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    /**
     * Insert the entity.
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * vendorTheLongAndWindingTableAndColumnRef.setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnRef.setBar...(value);
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.set...;</span>
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">insert</span>(vendorTheLongAndWindingTableAndColumnRef);
     * ... = vendorTheLongAndWindingTableAndColumnRef.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of insert target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insert(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef) {
        doInsert(vendorTheLongAndWindingTableAndColumnRef, null);
    }

    protected void doInsert(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRef", vendorTheLongAndWindingTableAndColumnRef);
        prepareInsertOption(option);
        delegateInsert(vendorTheLongAndWindingTableAndColumnRef, option);
    }

    protected void prepareInsertOption(InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
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
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * vendorTheLongAndWindingTableAndColumnRef.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * vendorTheLongAndWindingTableAndColumnRef.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.set...;</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumnRef.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">update</span>(vendorTheLongAndWindingTableAndColumnRef);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void update(final VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef) {
        doUpdate(vendorTheLongAndWindingTableAndColumnRef, null);
    }

    protected void doUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, final UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRef", vendorTheLongAndWindingTableAndColumnRef);
        prepareUpdateOption(option);
        helpUpdateInternally(vendorTheLongAndWindingTableAndColumnRef, new InternalUpdateCallback<VendorTheLongAndWindingTableAndColumnRef>() {
            public int callbackDelegateUpdate(VendorTheLongAndWindingTableAndColumnRef entity) { return delegateUpdate(entity, option); } });
    }

    protected void prepareUpdateOption(UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        if (option == null) { return; }
        assertUpdateOptionStatus(option);
        if (option.hasSelfSpecification()) {
            option.resolveSelfSpecification(createCBForVaryingUpdate());
        }
        if (option.hasSpecifiedUpdateColumn()) {
            option.resolveUpdateColumnSpecification(createCBForSpecifiedUpdate());
        }
    }

    protected VendorTheLongAndWindingTableAndColumnRefCB createCBForVaryingUpdate() {
        VendorTheLongAndWindingTableAndColumnRefCB cb = newMyConditionBean();
        cb.xsetupForVaryingUpdate();
        return cb;
    }

    protected VendorTheLongAndWindingTableAndColumnRefCB createCBForSpecifiedUpdate() {
        VendorTheLongAndWindingTableAndColumnRefCB cb = newMyConditionBean();
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
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of insert or update target. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void insertOrUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef) {
        doInesrtOrUpdate(vendorTheLongAndWindingTableAndColumnRef, null, null);
    }

    protected void doInesrtOrUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, final InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> insertOption, final UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> updateOption) {
        helpInsertOrUpdateInternally(vendorTheLongAndWindingTableAndColumnRef, new InternalInsertOrUpdateCallback<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB>() {
            public void callbackInsert(VendorTheLongAndWindingTableAndColumnRef entity) { doInsert(entity, insertOption); }
            public void callbackUpdate(VendorTheLongAndWindingTableAndColumnRef entity) { doUpdate(entity, updateOption); }
            public VendorTheLongAndWindingTableAndColumnRefCB callbackNewMyConditionBean() { return newMyConditionBean(); }
            public int callbackSelectCount(VendorTheLongAndWindingTableAndColumnRefCB cb) { return selectCount(cb); }
        });
    }

    @Override
    protected void doCreateOrModify(Entity entity, InsertOption<? extends ConditionBean> insertOption,
            UpdateOption<? extends ConditionBean> updateOption) {
        if (insertOption == null && updateOption == null) { insertOrUpdate(downcast(entity)); }
        else {
            insertOption = insertOption == null ? new InsertOption<VendorTheLongAndWindingTableAndColumnRefCB>() : insertOption;
            updateOption = updateOption == null ? new UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB>() : updateOption;
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
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * vendorTheLongAndWindingTableAndColumnRef.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumnRef.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">delete</span>(vendorTheLongAndWindingTableAndColumnRef);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * } 
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void delete(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef) {
        doDelete(vendorTheLongAndWindingTableAndColumnRef, null);
    }

    protected void doDelete(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, final DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRef", vendorTheLongAndWindingTableAndColumnRef);
        prepareDeleteOption(option);
        helpDeleteInternally(vendorTheLongAndWindingTableAndColumnRef, new InternalDeleteCallback<VendorTheLongAndWindingTableAndColumnRef>() {
            public int callbackDelegateDelete(VendorTheLongAndWindingTableAndColumnRef entity) { return delegateDelete(entity, option); } });
    }

    protected void prepareDeleteOption(DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
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
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @return The array of inserted count.
     */
    public int[] batchInsert(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList) {
        return doBatchInsert(vendorTheLongAndWindingTableAndColumnRefList, null);
    }

    protected int[] doBatchInsert(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRefList", vendorTheLongAndWindingTableAndColumnRefList);
        prepareInsertOption(option);
        return delegateBatchInsert(vendorTheLongAndWindingTableAndColumnRefList, option);
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
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdate(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList) {
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnRefList, null);
    }

    protected int[] doBatchUpdate(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRefList", vendorTheLongAndWindingTableAndColumnRefList);
        prepareUpdateOption(option);
        return delegateBatchUpdate(vendorTheLongAndWindingTableAndColumnRefList, option);
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
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @param updateColumnSpec The specification of update columns. (NotNull)
     * @return The array of updated count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchUpdate(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, SpecifyQuery<VendorTheLongAndWindingTableAndColumnRefCB> updateColumnSpec) {
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnRefList, createSpecifiedUpdateOption(updateColumnSpec));
    }

    @Override
    protected int[] doLumpModifyNonstrict(List<Entity> ls, UpdateOption<? extends ConditionBean> option) {
        return doLumpModify(ls, option);
    }

    /**
     * Batch-delete the list. <br />
     * This method uses 'Batch Update' of java.sql.PreparedStatement.
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @return The array of deleted count.
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     */
    public int[] batchDelete(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList) {
        return doBatchDelete(vendorTheLongAndWindingTableAndColumnRefList, null);
    }

    protected int[] doBatchDelete(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRefList", vendorTheLongAndWindingTableAndColumnRefList);
        prepareDeleteOption(option);
        return delegateBatchDelete(vendorTheLongAndWindingTableAndColumnRefList, option);
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
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">queryInsert</span>(new QueryInsertSetupper&lt;VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB&gt;() {
     *     public ConditionBean setup(vendorTheLongAndWindingTableAndColumnRef entity, VendorTheLongAndWindingTableAndColumnRefCB intoCB) {
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
    public int queryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB> setupper) {
        return doQueryInsert(setupper, null);
    }

    protected int doQueryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB> setupper, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("setupper", setupper);
        prepareInsertOption(option);
        VendorTheLongAndWindingTableAndColumnRef entity = new VendorTheLongAndWindingTableAndColumnRef();
        VendorTheLongAndWindingTableAndColumnRefCB intoCB = createCBForQueryInsert();
        ConditionBean resourceCB = setupper.setup(entity, intoCB);
        return delegateQueryInsert(entity, intoCB, resourceCB, option);
    }

    protected VendorTheLongAndWindingTableAndColumnRefCB createCBForQueryInsert() {
        VendorTheLongAndWindingTableAndColumnRefCB cb = newMyConditionBean();
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
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setPK...(value);</span>
     * vendorTheLongAndWindingTableAndColumnRef.setFoo...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set values of common columns</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setRegisterUser(value);</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.set...;</span>
     * <span style="color: #3F7E5E">// you don't need to set a value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setVersionNo(value);</span>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">queryUpdate</span>(vendorTheLongAndWindingTableAndColumnRef, cb);
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity that contains update values. (NotNull, PrimaryKeyNullAllowed)
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The updated count.
     * @exception org.seasar.dbflute.exception.NonQueryUpdateNotAllowedException When the query has no condition.
     */
    public int queryUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doQueryUpdate(vendorTheLongAndWindingTableAndColumnRef, cb, null);
    }

    protected int doQueryUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertObjectNotNull("vendorTheLongAndWindingTableAndColumnRef", vendorTheLongAndWindingTableAndColumnRef); assertCBStateValid(cb);
        prepareUpdateOption(option);
        return delegateQueryUpdate(vendorTheLongAndWindingTableAndColumnRef, cb, option);
    }

    @Override
    protected int doRangeModify(Entity entity, ConditionBean cb, UpdateOption<? extends ConditionBean> option) {
        if (option == null) { return queryUpdate(downcast(entity), (VendorTheLongAndWindingTableAndColumnRefCB)cb); }
        else { return varyingQueryUpdate(downcast(entity), (VendorTheLongAndWindingTableAndColumnRefCB)cb, downcast(option)); }
    }

    /**
     * Delete the several entities by query. {NonExclusiveControl}
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">queryDelete</span>(vendorTheLongAndWindingTableAndColumnRef, cb);
     * </pre>
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @return The deleted count.
     * @exception org.seasar.dbflute.exception.NonQueryDeleteNotAllowedException When the query has no condition.
     */
    public int queryDelete(VendorTheLongAndWindingTableAndColumnRefCB cb) {
        return doQueryDelete(cb, null);
    }

    protected int doQueryDelete(VendorTheLongAndWindingTableAndColumnRefCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertCBStateValid(cb);
        prepareDeleteOption(option);
        return delegateQueryDelete(cb, option);
    }

    @Override
    protected int doRangeRemove(ConditionBean cb, DeleteOption<? extends ConditionBean> option) {
        if (option == null) { return queryDelete((VendorTheLongAndWindingTableAndColumnRefCB)cb); }
        else { return varyingQueryDelete((VendorTheLongAndWindingTableAndColumnRefCB)cb, downcast(option)); }
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
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * <span style="color: #3F7E5E">// if auto-increment, you don't need to set the PK value</span>
     * vendorTheLongAndWindingTableAndColumnRef.setFoo...(value);
     * vendorTheLongAndWindingTableAndColumnRef.setBar...(value);
     * InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option = new InsertOption<VendorTheLongAndWindingTableAndColumnRefCB>();
     * <span style="color: #3F7E5E">// you can insert by your values for common columns</span>
     * option.disableCommonColumnAutoSetup();
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">varyingInsert</span>(vendorTheLongAndWindingTableAndColumnRef, option);
     * ... = vendorTheLongAndWindingTableAndColumnRef.getPK...(); <span style="color: #3F7E5E">// if auto-increment, you can get the value after</span>
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of insert target. (NotNull)
     * @param option The option of insert for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingInsert(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertInsertOptionNotNull(option);
        doInsert(vendorTheLongAndWindingTableAndColumnRef, option);
    }

    /**
     * Update the entity with varying requests modified-only. {UpdateCountZeroException, ExclusiveControl} <br />
     * For example, self(selfCalculationSpecification), specify(updateColumnSpecification), disableCommonColumnAutoSetup(). <br />
     * Other specifications are same as update(entity).
     * <pre>
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * vendorTheLongAndWindingTableAndColumnRef.setPK...(value); <span style="color: #3F7E5E">// required</span>
     * vendorTheLongAndWindingTableAndColumnRef.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// if exclusive control, the value of exclusive control column is required</span>
     * vendorTheLongAndWindingTableAndColumnRef.<span style="color: #FD4747">setVersionNo</span>(value);
     * try {
     *     <span style="color: #3F7E5E">// you can update by self calculation values</span>
     *     UpdateOption&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt; option = new UpdateOption&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt;();
     *     option.self(new SpecifyQuery&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt;() {
     *         public void specify(VendorTheLongAndWindingTableAndColumnRefCB cb) {
     *             cb.specify().<span style="color: #FD4747">columnXxxCount()</span>;
     *         }
     *     }).plus(1); <span style="color: #3F7E5E">// XXX_COUNT = XXX_COUNT + 1</span>
     *     vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">varyingUpdate</span>(vendorTheLongAndWindingTableAndColumnRef, option);
     * } catch (EntityAlreadyUpdatedException e) { <span style="color: #3F7E5E">// if concurrent update</span>
     *     ...
     * }
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of update target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertUpdateOptionNotNull(option);
        doUpdate(vendorTheLongAndWindingTableAndColumnRef, option);
    }

    /**
     * Insert or update the entity with varying requests. {ExclusiveControl(when update)}<br />
     * Other specifications are same as insertOrUpdate(entity).
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of insert or update target. (NotNull)
     * @param insertOption The option of insert for varying requests. (NotNull)
     * @param updateOption The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     * @exception org.seasar.dbflute.exception.EntityAlreadyExistsException When the entity already exists. (Unique Constraint Violation)
     */
    public void varyingInsertOrUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> insertOption, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> updateOption) {
        assertInsertOptionNotNull(insertOption); assertUpdateOptionNotNull(updateOption);
        doInesrtOrUpdate(vendorTheLongAndWindingTableAndColumnRef, insertOption, updateOption);
    }

    /**
     * Delete the entity with varying requests. {UpdateCountZeroException, ExclusiveControl} <br />
     * Now a valid option does not exist. <br />
     * Other specifications are same as delete(entity).
     * @param vendorTheLongAndWindingTableAndColumnRef The entity of delete target. (NotNull) {PrimaryKeyRequired, ConcurrencyColumnRequired}
     * @param option The option of update for varying requests. (NotNull)
     * @exception org.seasar.dbflute.exception.EntityAlreadyDeletedException When the entity has already been deleted.
     * @exception org.seasar.dbflute.exception.EntityDuplicatedException When the entity has been duplicated.
     */
    public void varyingDelete(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertDeleteOptionNotNull(option);
        doDelete(vendorTheLongAndWindingTableAndColumnRef, option);
    }

    // -----------------------------------------------------
    //                                          Batch Update
    //                                          ------------
    /**
     * Batch-insert the list with varying requests. <br />
     * For example, disableCommonColumnAutoSetup()
     * , disablePrimaryKeyIdentity(), limitBatchInsertLogging(). <br />
     * Other specifications are same as batchInsert(entityList).
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @param option The option of insert for varying requests. (NotNull)
     * @return The array of inserted count.
     */
    public int[] varyingBatchInsert(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertInsertOptionNotNull(option);
        return doBatchInsert(vendorTheLongAndWindingTableAndColumnRefList, option);
    }

    /**
     * Batch-update the list with varying requests. <br />
     * For example, self(selfCalculationSpecification), specify(updateColumnSpecification)
     * , disableCommonColumnAutoSetup(), limitBatchUpdateLogging(). <br />
     * Other specifications are same as batchUpdate(entityList).
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @param option The option of update for varying requests. (NotNull)
     * @return The array of updated count.
     */
    public int[] varyingBatchUpdate(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertUpdateOptionNotNull(option);
        return doBatchUpdate(vendorTheLongAndWindingTableAndColumnRefList, option);
    }

    /**
     * Batch-delete the list with varying requests. <br />
     * For example, limitBatchDeleteLogging(). <br />
     * Other specifications are same as batchDelete(entityList).
     * @param vendorTheLongAndWindingTableAndColumnRefList The list of the entity. (NotNull)
     * @param option The option of delete for varying requests. (NotNull)
     * @return The array of deleted count.
     */
    public int[] varyingBatchDelete(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertDeleteOptionNotNull(option);
        return doBatchDelete(vendorTheLongAndWindingTableAndColumnRefList, option);
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
    public int varyingQueryInsert(QueryInsertSetupper<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB> setupper, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
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
     * VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef = new VendorTheLongAndWindingTableAndColumnRef();
     * <span style="color: #3F7E5E">// you don't need to set PK value</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setPK...(value);</span>
     * vendorTheLongAndWindingTableAndColumnRef.setOther...(value); <span style="color: #3F7E5E">// you should set only modified columns</span>
     * <span style="color: #3F7E5E">// you don't need to set a value of exclusive control column</span>
     * <span style="color: #3F7E5E">// (auto-increment for version number is valid though non-exclusive control)</span>
     * <span style="color: #3F7E5E">//vendorTheLongAndWindingTableAndColumnRef.setVersionNo(value);</span>
     * VendorTheLongAndWindingTableAndColumnRefCB cb = new VendorTheLongAndWindingTableAndColumnRefCB();
     * cb.query().setFoo...(value);
     * UpdateOption&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt; option = new UpdateOption&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt;();
     * option.self(new SpecifyQuery&lt;VendorTheLongAndWindingTableAndColumnRefCB&gt;() {
     *     public void specify(VendorTheLongAndWindingTableAndColumnRefCB cb) {
     *         cb.specify().<span style="color: #FD4747">columnFooCount()</span>;
     *     }
     * }).plus(1); <span style="color: #3F7E5E">// FOO_COUNT = FOO_COUNT + 1</span>
     * vendorTheLongAndWindingTableAndColumnRefBhv.<span style="color: #FD4747">varyingQueryUpdate</span>(vendorTheLongAndWindingTableAndColumnRef, cb, option);
     * </pre>
     * @param vendorTheLongAndWindingTableAndColumnRef The entity that contains update values. (NotNull) {PrimaryKeyNotRequired}
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @param option The option of update for varying requests. (NotNull)
     * @return The updated count.
     * @exception org.seasar.dbflute.exception.NonQueryUpdateNotAllowedException When the query has no condition (if not allowed).
     */
    public int varyingQueryUpdate(VendorTheLongAndWindingTableAndColumnRef vendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
        assertUpdateOptionNotNull(option);
        return doQueryUpdate(vendorTheLongAndWindingTableAndColumnRef, cb, option);
    }

    /**
     * Delete the several entities by query with varying requests non-strictly. <br />
     * For example, allowNonQueryDelete(). <br />
     * Other specifications are same as batchUpdateNonstrict(entityList).
     * @param cb The condition-bean of VendorTheLongAndWindingTableAndColumnRef. (NotNull)
     * @param option The option of delete for varying requests. (NotNull)
     * @return The deleted count.
     * @exception org.seasar.dbflute.exception.NonQueryDeleteNotAllowedException When the query has no condition (if not allowed).
     */
    public int varyingQueryDelete(VendorTheLongAndWindingTableAndColumnRefCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> option) {
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
    public OutsideSqlBasicExecutor<VendorTheLongAndWindingTableAndColumnRefBhv> outsideSql() {
        return doOutsideSql();
    }

    // ===================================================================================
    //                                                                     Delegate Method
    //                                                                     ===============
    // [Behavior Command]
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    protected int delegateSelectCountUniquely(VendorTheLongAndWindingTableAndColumnRefCB cb) { return invoke(createSelectCountCBCommand(cb, true)); }
    protected int delegateSelectCountPlainly(VendorTheLongAndWindingTableAndColumnRefCB cb) { return invoke(createSelectCountCBCommand(cb, false)); }
    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> void delegateSelectCursor(VendorTheLongAndWindingTableAndColumnRefCB cb, EntityRowHandler<ENTITY> erh, Class<ENTITY> et)
    { invoke(createSelectCursorCBCommand(cb, erh, et)); }
    protected <ENTITY extends VendorTheLongAndWindingTableAndColumnRef> List<ENTITY> delegateSelectList(VendorTheLongAndWindingTableAndColumnRefCB cb, Class<ENTITY> et)
    { return invoke(createSelectListCBCommand(cb, et)); }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    protected int delegateInsert(VendorTheLongAndWindingTableAndColumnRef e, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeInsert(e, op)) { return 0; }
      return invoke(createInsertEntityCommand(e, op)); }
    protected int delegateUpdate(VendorTheLongAndWindingTableAndColumnRef e, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeUpdate(e, op)) { return 0; }
      return delegateUpdateNonstrict(e, op); }
    protected int delegateUpdateNonstrict(VendorTheLongAndWindingTableAndColumnRef e, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeUpdate(e, op)) { return 0; }
      return invoke(createUpdateNonstrictEntityCommand(e, op)); }
    protected int delegateDelete(VendorTheLongAndWindingTableAndColumnRef e, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeDelete(e, op)) { return 0; }
      return delegateDeleteNonstrict(e, op); }
    protected int delegateDeleteNonstrict(VendorTheLongAndWindingTableAndColumnRef e, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeDelete(e, op)) { return 0; }
      return invoke(createDeleteNonstrictEntityCommand(e, op)); }

    protected int[] delegateBatchInsert(List<VendorTheLongAndWindingTableAndColumnRef> ls, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchInsertCommand(processBatchInternally(ls, op), op)); }
    protected int[] delegateBatchUpdate(List<VendorTheLongAndWindingTableAndColumnRef> ls, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return delegateBatchUpdateNonstrict(ls, op); }
    protected int[] delegateBatchUpdateNonstrict(List<VendorTheLongAndWindingTableAndColumnRef> ls, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchUpdateNonstrictCommand(processBatchInternally(ls, op, true), op)); }
    protected int[] delegateBatchDelete(List<VendorTheLongAndWindingTableAndColumnRef> ls, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return delegateBatchDeleteNonstrict(ls, op); }
    protected int[] delegateBatchDeleteNonstrict(List<VendorTheLongAndWindingTableAndColumnRef> ls, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (ls.isEmpty()) { return new int[]{}; }
      return invoke(createBatchDeleteNonstrictCommand(processBatchInternally(ls, op, true), op)); }

    protected int delegateQueryInsert(VendorTheLongAndWindingTableAndColumnRef e, VendorTheLongAndWindingTableAndColumnRefCB inCB, ConditionBean resCB, InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeQueryInsert(e, inCB, resCB, op)) { return 0; } return invoke(createQueryInsertCBCommand(e, inCB, resCB, op));  }
    protected int delegateQueryUpdate(VendorTheLongAndWindingTableAndColumnRef e, VendorTheLongAndWindingTableAndColumnRefCB cb, UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
    { if (!processBeforeQueryUpdate(e, cb, op)) { return 0; } return invoke(createQueryUpdateCBCommand(e, cb, op));  }
    protected int delegateQueryDelete(VendorTheLongAndWindingTableAndColumnRefCB cb, DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> op)
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
    protected VendorTheLongAndWindingTableAndColumnRef downcast(Entity entity) {
        return helpEntityDowncastInternally(entity, VendorTheLongAndWindingTableAndColumnRef.class);
    }

    protected VendorTheLongAndWindingTableAndColumnRefCB downcast(ConditionBean cb) {
        return helpConditionBeanDowncastInternally(cb, VendorTheLongAndWindingTableAndColumnRefCB.class);
    }

    @SuppressWarnings("unchecked")
    protected List<VendorTheLongAndWindingTableAndColumnRef> downcast(List<? extends Entity> entityList) {
        return (List<VendorTheLongAndWindingTableAndColumnRef>)entityList;
    }

    @SuppressWarnings("unchecked")
    protected InsertOption<VendorTheLongAndWindingTableAndColumnRefCB> downcast(InsertOption<? extends ConditionBean> option) {
        return (InsertOption<VendorTheLongAndWindingTableAndColumnRefCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB> downcast(UpdateOption<? extends ConditionBean> option) {
        return (UpdateOption<VendorTheLongAndWindingTableAndColumnRefCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB> downcast(DeleteOption<? extends ConditionBean> option) {
        return (DeleteOption<VendorTheLongAndWindingTableAndColumnRefCB>)option;
    }

    @SuppressWarnings("unchecked")
    protected QueryInsertSetupper<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB> downcast(QueryInsertSetupper<? extends Entity, ? extends ConditionBean> option) {
        return (QueryInsertSetupper<VendorTheLongAndWindingTableAndColumnRef, VendorTheLongAndWindingTableAndColumnRefCB>)option;
    }
}
