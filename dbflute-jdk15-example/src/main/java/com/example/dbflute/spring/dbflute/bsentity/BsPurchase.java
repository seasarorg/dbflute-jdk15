/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import com.example.dbflute.spring.dbflute.allcommon.EntityDefinedCommonColumn;
import com.example.dbflute.spring.dbflute.allcommon.DBMetaInstanceHandler;
import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The entity of (購入)PURCHASE as TABLE. <br />
 * 一つの商品に対する購入を表現する。<br />
 * 実業務であれば購入詳細というテーブルを作り、「購入という行為」と「その中身（詳細）」を違う粒度のデータとしてそれぞれ管理するのが一般的と言えるでしょう。というか、注文とか請求とかそういうことを考え始めたらもっと複雑になるはずですが、ExampleDBということで割り切っています。
 * <pre>
 * [primary-key]
 *     PURCHASE_ID
 * 
 * [column]
 *     PURCHASE_ID, MEMBER_ID, PRODUCT_ID, PURCHASE_DATETIME, PURCHASE_COUNT, PURCHASE_PRICE, PAYMENT_COMPLETE_FLG, REGISTER_DATETIME, REGISTER_USER, UPDATE_DATETIME, UPDATE_USER, VERSION_NO
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     PURCHASE_ID
 * 
 * [version-no]
 *     VERSION_NO
 * 
 * [foreign-table]
 *     MEMBER, PRODUCT, SUMMARY_PRODUCT
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     member, product, summaryProduct
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsPurchase implements EntityDefinedCommonColumn, Serializable, Cloneable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    /** PURCHASE_ID: {PK, ID, NotNull, BIGINT(19)} */
    protected Long _purchaseId;

    /** (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} */
    protected Integer _memberId;

    /** (商品ID)PRODUCT_ID: {UQ+, IX, NotNull, INTEGER(10), FK to PRODUCT} */
    protected Integer _productId;

    /** (購入日時)PURCHASE_DATETIME: {UQ+, IX, NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _purchaseDatetime;

    /** (購入数量)PURCHASE_COUNT: {NotNull, INTEGER(10)} */
    protected Integer _purchaseCount;

    /** (購入価格)PURCHASE_PRICE: {IX, NotNull, INTEGER(10)} */
    protected Integer _purchasePrice;

    /** (支払完了フラグ)PAYMENT_COMPLETE_FLG: {NotNull, INTEGER(10), classification=Flg} */
    protected Integer _paymentCompleteFlg;

    /** REGISTER_DATETIME: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _registerDatetime;

    /** REGISTER_USER: {NotNull, VARCHAR(200)} */
    protected String _registerUser;

    /** UPDATE_DATETIME: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _updateDatetime;

    /** UPDATE_USER: {NotNull, VARCHAR(200)} */
    protected String _updateUser;

    /** VERSION_NO: {NotNull, BIGINT(19)} */
    protected Long _versionNo;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. (NotNull) */
    protected final EntityModifiedProperties __modifiedProperties = newModifiedProperties();

    /** Is common column auto set up effective? */
    protected boolean __canCommonColumnAutoSetup = true;

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    public String getTableDbName() {
        return "PURCHASE";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "purchase";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /**
     * {@inheritDoc}
     */
    public DBMeta getDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(getTableDbName());
    }

    // ===================================================================================
    //                                                                         Primary Key
    //                                                                         ===========
    /**
     * {@inheritDoc}
     */
    public boolean hasPrimaryKeyValue() {
        if (getPurchaseId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Set the value of paymentCompleteFlg as the classification of Flg. <br />
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public void setPaymentCompleteFlgAsFlg(CDef.Flg cdef) {
        setPaymentCompleteFlg(cdef != null ? InternalUtil.toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Set the value of paymentCompleteFlg as boolean. <br />
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (NullAllowed)
     */
    public void setPaymentCompleteFlgAsBoolean(Boolean determination) {
        setPaymentCompleteFlgAsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Get the value of paymentCompleteFlg as the classification of Flg. <br />
     * general boolean classification for every flg-column
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public CDef.Flg getPaymentCompleteFlgAsFlg() {
        return CDef.Flg.codeOf(getPaymentCompleteFlg());
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of paymentCompleteFlg as True (1). <br />
     * Checked: means yes
     */
    public void setPaymentCompleteFlg_True() {
        setPaymentCompleteFlgAsFlg(CDef.Flg.True);
    }

    /**
     * Set the value of paymentCompleteFlg as False (0). <br />
     * Unchecked: means no
     */
    public void setPaymentCompleteFlg_False() {
        setPaymentCompleteFlgAsFlg(CDef.Flg.False);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of paymentCompleteFlg True? <br />
     * Checked: means yes
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isPaymentCompleteFlgTrue() {
        CDef.Flg cdef = getPaymentCompleteFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.True) : false;
    }

    /**
     * Is the value of paymentCompleteFlg False? <br />
     * Unchecked: means no
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isPaymentCompleteFlgFalse() {
        CDef.Flg cdef = getPaymentCompleteFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.False) : false;
    }

    // ===================================================================================
    //                                                           Classification Name/Alias
    //                                                           =========================
    /**
     * Get the value of the column 'paymentCompleteFlg' as classification name.
     * @return The string of classification name. (NullAllowed)
     */
    public String getPaymentCompleteFlgName() {
        CDef.Flg cdef = getPaymentCompleteFlgAsFlg();
        return cdef != null ? cdef.name() : null;
    }

    /**
     * Get the value of the column 'paymentCompleteFlg' as classification alias.
     * @return The string of classification alias. (NullAllowed)
     */
    public String getPaymentCompleteFlgAlias() {
        CDef.Flg cdef = getPaymentCompleteFlgAsFlg();
        return cdef != null ? cdef.alias() : null;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    /** (会員)MEMBER by my MEMBER_ID, named 'member'. */
    protected Member _member;

    /**
     * (会員)MEMBER by my MEMBER_ID, named 'member'.
     * @return The entity of foreign property 'member'. (NullAllowed: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public Member getMember() {
        return _member;
    }

    /**
     * (会員)MEMBER by my MEMBER_ID, named 'member'.
     * @param member The entity of foreign property 'member'. (NullAllowed)
     */
    public void setMember(Member member) {
        _member = member;
    }

    /** (商品)PRODUCT by my PRODUCT_ID, named 'product'. */
    protected Product _product;

    /**
     * (商品)PRODUCT by my PRODUCT_ID, named 'product'.
     * @return The entity of foreign property 'product'. (NullAllowed: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public Product getProduct() {
        return _product;
    }

    /**
     * (商品)PRODUCT by my PRODUCT_ID, named 'product'.
     * @param product The entity of foreign property 'product'. (NullAllowed)
     */
    public void setProduct(Product product) {
        _product = product;
    }

    /** SUMMARY_PRODUCT by my PRODUCT_ID, named 'summaryProduct'. */
    protected SummaryProduct _summaryProduct;

    /**
     * SUMMARY_PRODUCT by my PRODUCT_ID, named 'summaryProduct'.
     * @return The entity of foreign property 'summaryProduct'. (NullAllowed: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public SummaryProduct getSummaryProduct() {
        return _summaryProduct;
    }

    /**
     * SUMMARY_PRODUCT by my PRODUCT_ID, named 'summaryProduct'.
     * @param summaryProduct The entity of foreign property 'summaryProduct'. (NullAllowed)
     */
    public void setSummaryProduct(SummaryProduct summaryProduct) {
        _summaryProduct = summaryProduct;
    }

    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    protected <ELEMENT> List<ELEMENT> newReferrerList() {
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                 Modified Properties
    //                                                                 ===================
    /**
     * {@inheritDoc}
     */
    public Set<String> modifiedProperties() {
        return __modifiedProperties.getPropertyNames();
    }

    /**
     * {@inheritDoc}
     */
    public void clearModifiedInfo() {
        __modifiedProperties.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasModification() {
        return !__modifiedProperties.isEmpty();
    }

    protected EntityModifiedProperties newModifiedProperties() {
        return new EntityModifiedProperties();
    }
    
    // ===================================================================================
    //                                                              Common Column Handling
    //                                                              ======================
    /**
     * {@inheritDoc}
     */
    public void enableCommonColumnAutoSetup() {
        __canCommonColumnAutoSetup = true;
    }

    /**
     * {@inheritDoc}
     */
    public void disableCommonColumnAutoSetup() {
        __canCommonColumnAutoSetup = false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean canCommonColumnAutoSetup() {
        return __canCommonColumnAutoSetup;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * Determine the object is equal with this. <br />
     * If primary-keys or columns of the other are same as this one, returns true.
     * @param other The other entity. (NullAllowed)
     * @return Comparing result.
     */
    public boolean equals(Object other) {
        if (other == null || !(other instanceof BsPurchase)) { return false; }
        BsPurchase otherEntity = (BsPurchase)other;
        if (!xSV(getPurchaseId(), otherEntity.getPurchaseId())) { return false; }
        return true;
    }
    protected boolean xSV(Object value1, Object value2) { // isSameValue()
        return InternalUtil.isSameValue(value1, value2);
    }

    /**
     * Calculate the hash-code from primary-keys or columns.
     * @return The hash-code from primary-key or columns.
     */
    public int hashCode() {
        int result = 17;
        result = xCH(result, getTableDbName());
        result = xCH(result, getPurchaseId());
        return result;
    }
    protected int xCH(int result, Object value) { // calculateHashcode()
        return InternalUtil.calculateHashcode(result, value);
    }

    /**
     * {@inheritDoc}
     */
    public int instanceHash() {
        return super.hashCode();
    }

    /**
     * Convert to display string of entity's data. (no relation data)
     * @return The display string of all columns and relation existences. (NotNull)
     */
    public String toString() {
        return buildDisplayString(InternalUtil.toClassTitle(this), true, true);
    }

    /**
     * {@inheritDoc}
     */
    public String toStringWithRelation() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        String l = "\n  ";
        if (_member != null)
        { sb.append(l).append(xbRDS(_member, "member")); }
        if (_product != null)
        { sb.append(l).append(xbRDS(_product, "product")); }
        if (_summaryProduct != null)
        { sb.append(l).append(xbRDS(_summaryProduct, "summaryProduct")); }
        return sb.toString();
    }
    protected String xbRDS(Entity e, String name) { // buildRelationDisplayString()
        return e.buildDisplayString(name, true, true);
    }

    /**
     * {@inheritDoc}
     */
    public String buildDisplayString(String name, boolean column, boolean relation) {
        StringBuilder sb = new StringBuilder();
        if (name != null) { sb.append(name).append(column || relation ? ":" : ""); }
        if (column) { sb.append(buildColumnString()); }
        if (relation) { sb.append(buildRelationString()); }
        sb.append("@").append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
    protected String buildColumnString() {
        StringBuilder sb = new StringBuilder();
        String delimiter = ", ";
        sb.append(delimiter).append(getPurchaseId());
        sb.append(delimiter).append(getMemberId());
        sb.append(delimiter).append(getProductId());
        sb.append(delimiter).append(getPurchaseDatetime());
        sb.append(delimiter).append(getPurchaseCount());
        sb.append(delimiter).append(getPurchasePrice());
        sb.append(delimiter).append(getPaymentCompleteFlg());
        sb.append(delimiter).append(getRegisterDatetime());
        sb.append(delimiter).append(getRegisterUser());
        sb.append(delimiter).append(getUpdateDatetime());
        sb.append(delimiter).append(getUpdateUser());
        sb.append(delimiter).append(getVersionNo());
        if (sb.length() > delimiter.length()) {
            sb.delete(0, delimiter.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }
    protected String buildRelationString() {
        StringBuilder sb = new StringBuilder();
        String c = ",";
        if (_member != null) { sb.append(c).append("member"); }
        if (_product != null) { sb.append(c).append("product"); }
        if (_summaryProduct != null) { sb.append(c).append("summaryProduct"); }
        if (sb.length() > c.length()) {
            sb.delete(0, c.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    public Purchase clone() {
        try {
            return (Purchase)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] PURCHASE_ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @return The value of the column 'PURCHASE_ID'. (NullAllowed)
     */
    public Long getPurchaseId() {
        return _purchaseId;
    }

    /**
     * [set] PURCHASE_ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @param purchaseId The value of the column 'PURCHASE_ID'. (NullAllowed)
     */
    public void setPurchaseId(Long purchaseId) {
        __modifiedProperties.addPropertyName("purchaseId");
        this._purchaseId = purchaseId;
    }

    /**
     * [get] (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} <br />
     * 会員を参照するID。<br />
     * 購入を識別する自然キー（複合ユニーク制約）の筆頭要素。
     * @return The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} <br />
     * 会員を参照するID。<br />
     * 購入を識別する自然キー（複合ユニーク制約）の筆頭要素。
     * @param memberId The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        __modifiedProperties.addPropertyName("memberId");
        this._memberId = memberId;
    }

    /**
     * [get] (商品ID)PRODUCT_ID: {UQ+, IX, NotNull, INTEGER(10), FK to PRODUCT} <br />
     * 商品を参照するID。
     * @return The value of the column 'PRODUCT_ID'. (NullAllowed)
     */
    public Integer getProductId() {
        return _productId;
    }

    /**
     * [set] (商品ID)PRODUCT_ID: {UQ+, IX, NotNull, INTEGER(10), FK to PRODUCT} <br />
     * 商品を参照するID。
     * @param productId The value of the column 'PRODUCT_ID'. (NullAllowed)
     */
    public void setProductId(Integer productId) {
        __modifiedProperties.addPropertyName("productId");
        this._productId = productId;
    }

    /**
     * [get] (購入日時)PURCHASE_DATETIME: {UQ+, IX, NotNull, TIMESTAMP(23, 10)} <br />
     * 購入した瞬間の日時。
     * @return The value of the column 'PURCHASE_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getPurchaseDatetime() {
        return _purchaseDatetime;
    }

    /**
     * [set] (購入日時)PURCHASE_DATETIME: {UQ+, IX, NotNull, TIMESTAMP(23, 10)} <br />
     * 購入した瞬間の日時。
     * @param purchaseDatetime The value of the column 'PURCHASE_DATETIME'. (NullAllowed)
     */
    public void setPurchaseDatetime(java.sql.Timestamp purchaseDatetime) {
        __modifiedProperties.addPropertyName("purchaseDatetime");
        this._purchaseDatetime = purchaseDatetime;
    }

    /**
     * [get] (購入数量)PURCHASE_COUNT: {NotNull, INTEGER(10)} <br />
     * 購入した商品の（一回の購入における）数量。
     * @return The value of the column 'PURCHASE_COUNT'. (NullAllowed)
     */
    public Integer getPurchaseCount() {
        return _purchaseCount;
    }

    /**
     * [set] (購入数量)PURCHASE_COUNT: {NotNull, INTEGER(10)} <br />
     * 購入した商品の（一回の購入における）数量。
     * @param purchaseCount The value of the column 'PURCHASE_COUNT'. (NullAllowed)
     */
    public void setPurchaseCount(Integer purchaseCount) {
        __modifiedProperties.addPropertyName("purchaseCount");
        this._purchaseCount = purchaseCount;
    }

    /**
     * [get] (購入価格)PURCHASE_PRICE: {IX, NotNull, INTEGER(10)} <br />
     * 購入によって実際に会員が支払った（支払う予定の）価格。<br />
     * 基本は商品の定価に購入数量を掛けたものになるが、<br />
     * ポイント利用や割引があったりと必ずしもそうはならない。
     * @return The value of the column 'PURCHASE_PRICE'. (NullAllowed)
     */
    public Integer getPurchasePrice() {
        return _purchasePrice;
    }

    /**
     * [set] (購入価格)PURCHASE_PRICE: {IX, NotNull, INTEGER(10)} <br />
     * 購入によって実際に会員が支払った（支払う予定の）価格。<br />
     * 基本は商品の定価に購入数量を掛けたものになるが、<br />
     * ポイント利用や割引があったりと必ずしもそうはならない。
     * @param purchasePrice The value of the column 'PURCHASE_PRICE'. (NullAllowed)
     */
    public void setPurchasePrice(Integer purchasePrice) {
        __modifiedProperties.addPropertyName("purchasePrice");
        this._purchasePrice = purchasePrice;
    }

    /**
     * [get] (支払完了フラグ)PAYMENT_COMPLETE_FLG: {NotNull, INTEGER(10), classification=Flg} <br />
     * この購入に関しての支払いが完了しているか否か。
     * @return The value of the column 'PAYMENT_COMPLETE_FLG'. (NullAllowed)
     */
    public Integer getPaymentCompleteFlg() {
        return _paymentCompleteFlg;
    }

    /**
     * [set] (支払完了フラグ)PAYMENT_COMPLETE_FLG: {NotNull, INTEGER(10), classification=Flg} <br />
     * この購入に関しての支払いが完了しているか否か。
     * @param paymentCompleteFlg The value of the column 'PAYMENT_COMPLETE_FLG'. (NullAllowed)
     */
    public void setPaymentCompleteFlg(Integer paymentCompleteFlg) {
        checkImplicitSet("PAYMENT_COMPLETE_FLG", CDef.DefMeta.Flg, paymentCompleteFlg);
        __modifiedProperties.addPropertyName("paymentCompleteFlg");
        this._paymentCompleteFlg = paymentCompleteFlg;
    }

    /**
     * [get] REGISTER_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'REGISTER_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getRegisterDatetime() {
        return _registerDatetime;
    }

    /**
     * [set] REGISTER_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param registerDatetime The value of the column 'REGISTER_DATETIME'. (NullAllowed)
     */
    public void setRegisterDatetime(java.sql.Timestamp registerDatetime) {
        __modifiedProperties.addPropertyName("registerDatetime");
        this._registerDatetime = registerDatetime;
    }

    /**
     * [get] REGISTER_USER: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'REGISTER_USER'. (NullAllowed)
     */
    public String getRegisterUser() {
        return _registerUser;
    }

    /**
     * [set] REGISTER_USER: {NotNull, VARCHAR(200)} <br />
     * @param registerUser The value of the column 'REGISTER_USER'. (NullAllowed)
     */
    public void setRegisterUser(String registerUser) {
        __modifiedProperties.addPropertyName("registerUser");
        this._registerUser = registerUser;
    }

    /**
     * [get] UPDATE_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'UPDATE_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getUpdateDatetime() {
        return _updateDatetime;
    }

    /**
     * [set] UPDATE_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param updateDatetime The value of the column 'UPDATE_DATETIME'. (NullAllowed)
     */
    public void setUpdateDatetime(java.sql.Timestamp updateDatetime) {
        __modifiedProperties.addPropertyName("updateDatetime");
        this._updateDatetime = updateDatetime;
    }

    /**
     * [get] UPDATE_USER: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'UPDATE_USER'. (NullAllowed)
     */
    public String getUpdateUser() {
        return _updateUser;
    }

    /**
     * [set] UPDATE_USER: {NotNull, VARCHAR(200)} <br />
     * @param updateUser The value of the column 'UPDATE_USER'. (NullAllowed)
     */
    public void setUpdateUser(String updateUser) {
        __modifiedProperties.addPropertyName("updateUser");
        this._updateUser = updateUser;
    }

    /**
     * [get] VERSION_NO: {NotNull, BIGINT(19)} <br />
     * @return The value of the column 'VERSION_NO'. (NullAllowed)
     */
    public Long getVersionNo() {
        return _versionNo;
    }

    /**
     * [set] VERSION_NO: {NotNull, BIGINT(19)} <br />
     * @param versionNo The value of the column 'VERSION_NO'. (NullAllowed)
     */
    public void setVersionNo(Long versionNo) {
        __modifiedProperties.addPropertyName("versionNo");
        this._versionNo = versionNo;
    }

    protected void checkImplicitSet(String columnDbName, CDef.DefMeta meta, Object value) {
        InternalUtil.checkImplicitSet(this, columnDbName, meta, value);
    }
}
