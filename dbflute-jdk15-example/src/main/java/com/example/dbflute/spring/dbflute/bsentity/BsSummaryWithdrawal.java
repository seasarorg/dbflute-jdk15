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
import com.example.dbflute.spring.dbflute.allcommon.DBMetaInstanceHandler;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The entity of SUMMARY_WITHDRAWAL as VIEW. <br />
 * <pre>
 * [primary-key]
 *     
 * 
 * [column]
 *     MEMBER_ID, MEMBER_NAME, WITHDRAWAL_REASON_CODE, WITHDRAWAL_REASON_TEXT, WITHDRAWAL_REASON_INPUT_TEXT, WITHDRAWAL_DATETIME, MEMBER_STATUS_CODE, MEMBER_STATUS_NAME, MAX_PURCHASE_PRICE
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
 *     
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsSummaryWithdrawal implements Entity, Serializable, Cloneable {

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
    /** MEMBER_ID: {INTEGER(10)} */
    protected Integer _memberId;

    /** MEMBER_NAME: {VARCHAR(200)} */
    protected String _memberName;

    /** WITHDRAWAL_REASON_CODE: {CHAR(3)} */
    protected String _withdrawalReasonCode;

    /** WITHDRAWAL_REASON_TEXT: {CLOB(2147483647)} */
    protected String _withdrawalReasonText;

    /** WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} */
    protected String _withdrawalReasonInputText;

    /** WITHDRAWAL_DATETIME: {TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _withdrawalDatetime;

    /** MEMBER_STATUS_CODE: {CHAR(3)} */
    protected String _memberStatusCode;

    /** MEMBER_STATUS_NAME: {VARCHAR(50)} */
    protected String _memberStatusName;

    /** MAX_PURCHASE_PRICE: {INTEGER(10)} */
    protected Integer _maxPurchasePrice;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. (NotNull) */
    protected final EntityModifiedProperties __modifiedProperties = newModifiedProperties();

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    public String getTableDbName() {
        return "SUMMARY_WITHDRAWAL";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "summaryWithdrawal";
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
        return false;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
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
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * Determine the object is equal with this. <br />
     * If primary-keys or columns of the other are same as this one, returns true.
     * @param other The other entity. (NullAllowed)
     * @return Comparing result.
     */
    public boolean equals(Object other) {
        if (other == null || !(other instanceof BsSummaryWithdrawal)) { return false; }
        BsSummaryWithdrawal otherEntity = (BsSummaryWithdrawal)other;
        if (!xSV(getMemberId(), otherEntity.getMemberId())) { return false; }
        if (!xSV(getMemberName(), otherEntity.getMemberName())) { return false; }
        if (!xSV(getWithdrawalReasonCode(), otherEntity.getWithdrawalReasonCode())) { return false; }
        if (!xSV(getWithdrawalReasonText(), otherEntity.getWithdrawalReasonText())) { return false; }
        if (!xSV(getWithdrawalReasonInputText(), otherEntity.getWithdrawalReasonInputText())) { return false; }
        if (!xSV(getWithdrawalDatetime(), otherEntity.getWithdrawalDatetime())) { return false; }
        if (!xSV(getMemberStatusCode(), otherEntity.getMemberStatusCode())) { return false; }
        if (!xSV(getMemberStatusName(), otherEntity.getMemberStatusName())) { return false; }
        if (!xSV(getMaxPurchasePrice(), otherEntity.getMaxPurchasePrice())) { return false; }
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
        result = xCH(result, getMemberId());
        result = xCH(result, getMemberName());
        result = xCH(result, getWithdrawalReasonCode());
        result = xCH(result, getWithdrawalReasonText());
        result = xCH(result, getWithdrawalReasonInputText());
        result = xCH(result, getWithdrawalDatetime());
        result = xCH(result, getMemberStatusCode());
        result = xCH(result, getMemberStatusName());
        result = xCH(result, getMaxPurchasePrice());
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
        return sb.toString();
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
        sb.append(delimiter).append(getMemberId());
        sb.append(delimiter).append(getMemberName());
        sb.append(delimiter).append(getWithdrawalReasonCode());
        sb.append(delimiter).append(getWithdrawalReasonText());
        sb.append(delimiter).append(getWithdrawalReasonInputText());
        sb.append(delimiter).append(getWithdrawalDatetime());
        sb.append(delimiter).append(getMemberStatusCode());
        sb.append(delimiter).append(getMemberStatusName());
        sb.append(delimiter).append(getMaxPurchasePrice());
        if (sb.length() > delimiter.length()) {
            sb.delete(0, delimiter.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }
    protected String buildRelationString() {
        return "";
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    public SummaryWithdrawal clone() {
        try {
            return (SummaryWithdrawal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] MEMBER_ID: {INTEGER(10)} <br />
     * @return The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] MEMBER_ID: {INTEGER(10)} <br />
     * @param memberId The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        __modifiedProperties.addPropertyName("memberId");
        this._memberId = memberId;
    }

    /**
     * [get] MEMBER_NAME: {VARCHAR(200)} <br />
     * @return The value of the column 'MEMBER_NAME'. (NullAllowed)
     */
    public String getMemberName() {
        return _memberName;
    }

    /**
     * [set] MEMBER_NAME: {VARCHAR(200)} <br />
     * @param memberName The value of the column 'MEMBER_NAME'. (NullAllowed)
     */
    public void setMemberName(String memberName) {
        __modifiedProperties.addPropertyName("memberName");
        this._memberName = memberName;
    }

    /**
     * [get] WITHDRAWAL_REASON_CODE: {CHAR(3)} <br />
     * @return The value of the column 'WITHDRAWAL_REASON_CODE'. (NullAllowed)
     */
    public String getWithdrawalReasonCode() {
        return _withdrawalReasonCode;
    }

    /**
     * [set] WITHDRAWAL_REASON_CODE: {CHAR(3)} <br />
     * @param withdrawalReasonCode The value of the column 'WITHDRAWAL_REASON_CODE'. (NullAllowed)
     */
    public void setWithdrawalReasonCode(String withdrawalReasonCode) {
        __modifiedProperties.addPropertyName("withdrawalReasonCode");
        this._withdrawalReasonCode = withdrawalReasonCode;
    }

    /**
     * [get] WITHDRAWAL_REASON_TEXT: {CLOB(2147483647)} <br />
     * @return The value of the column 'WITHDRAWAL_REASON_TEXT'. (NullAllowed)
     */
    public String getWithdrawalReasonText() {
        return _withdrawalReasonText;
    }

    /**
     * [set] WITHDRAWAL_REASON_TEXT: {CLOB(2147483647)} <br />
     * @param withdrawalReasonText The value of the column 'WITHDRAWAL_REASON_TEXT'. (NullAllowed)
     */
    public void setWithdrawalReasonText(String withdrawalReasonText) {
        __modifiedProperties.addPropertyName("withdrawalReasonText");
        this._withdrawalReasonText = withdrawalReasonText;
    }

    /**
     * [get] WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} <br />
     * @return The value of the column 'WITHDRAWAL_REASON_INPUT_TEXT'. (NullAllowed)
     */
    public String getWithdrawalReasonInputText() {
        return _withdrawalReasonInputText;
    }

    /**
     * [set] WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} <br />
     * @param withdrawalReasonInputText The value of the column 'WITHDRAWAL_REASON_INPUT_TEXT'. (NullAllowed)
     */
    public void setWithdrawalReasonInputText(String withdrawalReasonInputText) {
        __modifiedProperties.addPropertyName("withdrawalReasonInputText");
        this._withdrawalReasonInputText = withdrawalReasonInputText;
    }

    /**
     * [get] WITHDRAWAL_DATETIME: {TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'WITHDRAWAL_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getWithdrawalDatetime() {
        return _withdrawalDatetime;
    }

    /**
     * [set] WITHDRAWAL_DATETIME: {TIMESTAMP(23, 10)} <br />
     * @param withdrawalDatetime The value of the column 'WITHDRAWAL_DATETIME'. (NullAllowed)
     */
    public void setWithdrawalDatetime(java.sql.Timestamp withdrawalDatetime) {
        __modifiedProperties.addPropertyName("withdrawalDatetime");
        this._withdrawalDatetime = withdrawalDatetime;
    }

    /**
     * [get] MEMBER_STATUS_CODE: {CHAR(3)} <br />
     * @return The value of the column 'MEMBER_STATUS_CODE'. (NullAllowed)
     */
    public String getMemberStatusCode() {
        return _memberStatusCode;
    }

    /**
     * [set] MEMBER_STATUS_CODE: {CHAR(3)} <br />
     * @param memberStatusCode The value of the column 'MEMBER_STATUS_CODE'. (NullAllowed)
     */
    public void setMemberStatusCode(String memberStatusCode) {
        __modifiedProperties.addPropertyName("memberStatusCode");
        this._memberStatusCode = memberStatusCode;
    }

    /**
     * [get] MEMBER_STATUS_NAME: {VARCHAR(50)} <br />
     * @return The value of the column 'MEMBER_STATUS_NAME'. (NullAllowed)
     */
    public String getMemberStatusName() {
        return _memberStatusName;
    }

    /**
     * [set] MEMBER_STATUS_NAME: {VARCHAR(50)} <br />
     * @param memberStatusName The value of the column 'MEMBER_STATUS_NAME'. (NullAllowed)
     */
    public void setMemberStatusName(String memberStatusName) {
        __modifiedProperties.addPropertyName("memberStatusName");
        this._memberStatusName = memberStatusName;
    }

    /**
     * [get] MAX_PURCHASE_PRICE: {INTEGER(10)} <br />
     * @return The value of the column 'MAX_PURCHASE_PRICE'. (NullAllowed)
     */
    public Integer getMaxPurchasePrice() {
        return _maxPurchasePrice;
    }

    /**
     * [set] MAX_PURCHASE_PRICE: {INTEGER(10)} <br />
     * @param maxPurchasePrice The value of the column 'MAX_PURCHASE_PRICE'. (NullAllowed)
     */
    public void setMaxPurchasePrice(Integer maxPurchasePrice) {
        __modifiedProperties.addPropertyName("maxPurchasePrice");
        this._maxPurchasePrice = maxPurchasePrice;
    }
}
