/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Date;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import com.example.dbflute.spring.dbflute.allcommon.EntityDefinedCommonColumn;
import com.example.dbflute.spring.dbflute.allcommon.DBMetaInstanceHandler;
import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The entity of (会員住所情報)MEMBER_ADDRESS as TABLE. <br />
 * 会員の住所に関する情報で、同時に有効期間ごとに履歴管理されている。<br />
 * 会員を基点に考えた場合、構造的にはone-to-many だが、業務的な定型条件でone-to-one になる。このような構造を「業務的one-to-one」と呼ぶ。<br />
 * 有効期間は隙間なく埋められるが、ここでは住所情報のない会員も存在し、厳密には(業務的な) "1 : 0...1" である。
 * <pre>
 * [primary-key]
 *     MEMBER_ADDRESS_ID
 * 
 * [column]
 *     MEMBER_ADDRESS_ID, MEMBER_ID, VALID_BEGIN_DATE, VALID_END_DATE, ADDRESS, REGION_ID, REGISTER_DATETIME, REGISTER_USER, UPDATE_DATETIME, UPDATE_USER, VERSION_NO
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     
 * 
 * [version-no]
 *     VERSION_NO
 * 
 * [foreign-table]
 *     MEMBER, REGION
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     member, region
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsMemberAddress implements EntityDefinedCommonColumn, Serializable, Cloneable {

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
    /** (会員住所ID)MEMBER_ADDRESS_ID: {PK, NotNull, INTEGER(10)} */
    protected Integer _memberAddressId;

    /** (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} */
    protected Integer _memberId;

    /** (有効開始日)VALID_BEGIN_DATE: {UQ+, NotNull, DATE(8)} */
    protected java.util.Date _validBeginDate;

    /** (有効終了日)VALID_END_DATE: {NotNull, DATE(8)} */
    protected java.util.Date _validEndDate;

    /** (住所)ADDRESS: {NotNull, VARCHAR(200)} */
    protected String _address;

    /** (地域ID)REGION_ID: {IX, NotNull, INTEGER(10), FK to REGION, classification=Region} */
    protected Integer _regionId;

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
        return "MEMBER_ADDRESS";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "memberAddress";
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
        if (getMemberAddressId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Set the value of regionId as the classification of Region. <br />
     * mainly region of member address
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public void setRegionIdAsRegion(CDef.Region cdef) {
        setRegionId(cdef != null ? InternalUtil.toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Get the value of regionId as the classification of Region. <br />
     * mainly region of member address
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public CDef.Region getRegionIdAsRegion() {
        return CDef.Region.codeOf(getRegionId());
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of regionId as America (1). <br />
     * AMERICA
     */
    public void setRegionId_America() {
        setRegionIdAsRegion(CDef.Region.America);
    }

    /**
     * Set the value of regionId as Canada (2). <br />
     * CANADA
     */
    public void setRegionId_Canada() {
        setRegionIdAsRegion(CDef.Region.Canada);
    }

    /**
     * Set the value of regionId as China (3). <br />
     * CHINA
     */
    public void setRegionId_China() {
        setRegionIdAsRegion(CDef.Region.China);
    }

    /**
     * Set the value of regionId as Chiba (4). <br />
     * CHIBA
     */
    public void setRegionId_Chiba() {
        setRegionIdAsRegion(CDef.Region.Chiba);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of regionId America? <br />
     * AMERICA
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isRegionIdAmerica() {
        CDef.Region cdef = getRegionIdAsRegion();
        return cdef != null ? cdef.equals(CDef.Region.America) : false;
    }

    /**
     * Is the value of regionId Canada? <br />
     * CANADA
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isRegionIdCanada() {
        CDef.Region cdef = getRegionIdAsRegion();
        return cdef != null ? cdef.equals(CDef.Region.Canada) : false;
    }

    /**
     * Is the value of regionId China? <br />
     * CHINA
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isRegionIdChina() {
        CDef.Region cdef = getRegionIdAsRegion();
        return cdef != null ? cdef.equals(CDef.Region.China) : false;
    }

    /**
     * Is the value of regionId Chiba? <br />
     * CHIBA
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isRegionIdChiba() {
        CDef.Region cdef = getRegionIdAsRegion();
        return cdef != null ? cdef.equals(CDef.Region.Chiba) : false;
    }

    // ===================================================================================
    //                                                           Classification Name/Alias
    //                                                           =========================
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

    /** (地域)REGION by my REGION_ID, named 'region'. */
    protected Region _region;

    /**
     * (地域)REGION by my REGION_ID, named 'region'.
     * @return The entity of foreign property 'region'. (NullAllowed: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public Region getRegion() {
        return _region;
    }

    /**
     * (地域)REGION by my REGION_ID, named 'region'.
     * @param region The entity of foreign property 'region'. (NullAllowed)
     */
    public void setRegion(Region region) {
        _region = region;
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
        if (other == null || !(other instanceof BsMemberAddress)) { return false; }
        BsMemberAddress otherEntity = (BsMemberAddress)other;
        if (!xSV(getMemberAddressId(), otherEntity.getMemberAddressId())) { return false; }
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
        result = xCH(result, getMemberAddressId());
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
        if (_region != null)
        { sb.append(l).append(xbRDS(_region, "region")); }
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
        sb.append(delimiter).append(getMemberAddressId());
        sb.append(delimiter).append(getMemberId());
        sb.append(delimiter).append(xfUD(getValidBeginDate()));
        sb.append(delimiter).append(xfUD(getValidEndDate()));
        sb.append(delimiter).append(getAddress());
        sb.append(delimiter).append(getRegionId());
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
    protected String xfUD(Date date) { // formatUtilDate()
        return InternalUtil.toString(date, xgDP());
    }
    protected String xgDP() { // getDatePattern
        return "yyyy-MM-dd";
    }
    protected String buildRelationString() {
        StringBuilder sb = new StringBuilder();
        String c = ",";
        if (_member != null) { sb.append(c).append("member"); }
        if (_region != null) { sb.append(c).append("region"); }
        if (sb.length() > c.length()) {
            sb.delete(0, c.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    public MemberAddress clone() {
        try {
            return (MemberAddress)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] (会員住所ID)MEMBER_ADDRESS_ID: {PK, NotNull, INTEGER(10)} <br />
     * 会員住所を識別するID。<br />
     * 履歴分も含むテーブルなので、これ自体はFKではない。
     * @return The value of the column 'MEMBER_ADDRESS_ID'. (NullAllowed)
     */
    public Integer getMemberAddressId() {
        return _memberAddressId;
    }

    /**
     * [set] (会員住所ID)MEMBER_ADDRESS_ID: {PK, NotNull, INTEGER(10)} <br />
     * 会員住所を識別するID。<br />
     * 履歴分も含むテーブルなので、これ自体はFKではない。
     * @param memberAddressId The value of the column 'MEMBER_ADDRESS_ID'. (NullAllowed)
     */
    public void setMemberAddressId(Integer memberAddressId) {
        __modifiedProperties.addPropertyName("memberAddressId");
        this._memberAddressId = memberAddressId;
    }

    /**
     * [get] (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} <br />
     * 会員を参照するID。<br />
     * 履歴分を含むため、これだけではユニークにはならない。<br />
     * 有効開始日と合わせて複合ユニーク制約となるが、<br />
     * 厳密には完全な制約にはなっていない。<br />
     * 有効期間の概念はRDBでは表現しきれないのである。
     * @return The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] (会員ID)MEMBER_ID: {UQ, IX, NotNull, INTEGER(10), FK to MEMBER} <br />
     * 会員を参照するID。<br />
     * 履歴分を含むため、これだけではユニークにはならない。<br />
     * 有効開始日と合わせて複合ユニーク制約となるが、<br />
     * 厳密には完全な制約にはなっていない。<br />
     * 有効期間の概念はRDBでは表現しきれないのである。
     * @param memberId The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        __modifiedProperties.addPropertyName("memberId");
        this._memberId = memberId;
    }

    /**
     * [get] (有効開始日)VALID_BEGIN_DATE: {UQ+, NotNull, DATE(8)} <br />
     * 一つの有効期間の開始を示す日付。<br />
     * 前の有効終了日の次の日の値が格納される。
     * @return The value of the column 'VALID_BEGIN_DATE'. (NullAllowed)
     */
    public java.util.Date getValidBeginDate() {
        return _validBeginDate;
    }

    /**
     * [set] (有効開始日)VALID_BEGIN_DATE: {UQ+, NotNull, DATE(8)} <br />
     * 一つの有効期間の開始を示す日付。<br />
     * 前の有効終了日の次の日の値が格納される。
     * @param validBeginDate The value of the column 'VALID_BEGIN_DATE'. (NullAllowed)
     */
    public void setValidBeginDate(java.util.Date validBeginDate) {
        __modifiedProperties.addPropertyName("validBeginDate");
        this._validBeginDate = validBeginDate;
    }

    /**
     * [get] (有効終了日)VALID_END_DATE: {NotNull, DATE(8)} <br />
     * 有効期間の終了日。<br />
     * 次の有効開始日の一日前の値が格納される。<br />
     * ただし、次の有効期間がない場合は 9999/12/31 となる。
     * @return The value of the column 'VALID_END_DATE'. (NullAllowed)
     */
    public java.util.Date getValidEndDate() {
        return _validEndDate;
    }

    /**
     * [set] (有効終了日)VALID_END_DATE: {NotNull, DATE(8)} <br />
     * 有効期間の終了日。<br />
     * 次の有効開始日の一日前の値が格納される。<br />
     * ただし、次の有効期間がない場合は 9999/12/31 となる。
     * @param validEndDate The value of the column 'VALID_END_DATE'. (NullAllowed)
     */
    public void setValidEndDate(java.util.Date validEndDate) {
        __modifiedProperties.addPropertyName("validEndDate");
        this._validEndDate = validEndDate;
    }

    /**
     * [get] (住所)ADDRESS: {NotNull, VARCHAR(200)} <br />
     * まるごと住所
     * @return The value of the column 'ADDRESS'. (NullAllowed)
     */
    public String getAddress() {
        return _address;
    }

    /**
     * [set] (住所)ADDRESS: {NotNull, VARCHAR(200)} <br />
     * まるごと住所
     * @param address The value of the column 'ADDRESS'. (NullAllowed)
     */
    public void setAddress(String address) {
        __modifiedProperties.addPropertyName("address");
        this._address = address;
    }

    /**
     * [get] (地域ID)REGION_ID: {IX, NotNull, INTEGER(10), FK to REGION, classification=Region} <br />
     * 地域を参照するID。<br />
     * ここでは特に住所の内容と連動しているわけではない。<br />
     * （業務的one-to-oneの親テーブルの表現したかっ...）
     * @return The value of the column 'REGION_ID'. (NullAllowed)
     */
    public Integer getRegionId() {
        return _regionId;
    }

    /**
     * [set] (地域ID)REGION_ID: {IX, NotNull, INTEGER(10), FK to REGION, classification=Region} <br />
     * 地域を参照するID。<br />
     * ここでは特に住所の内容と連動しているわけではない。<br />
     * （業務的one-to-oneの親テーブルの表現したかっ...）
     * @param regionId The value of the column 'REGION_ID'. (NullAllowed)
     */
    public void setRegionId(Integer regionId) {
        __modifiedProperties.addPropertyName("regionId");
        this._regionId = regionId;
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
}
