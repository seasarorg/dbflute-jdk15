/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity.customize;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.seasar.dbflute.dbmeta.DBMeta;
import com.example.dbflute.spring.dbflute.allcommon.EntityDefinedCommonColumn;
import com.example.dbflute.spring.dbflute.exentity.customize.*;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The entity of PrimaryKeyComment. <br />
 * <pre>
 * [primary-key]
 *     MEMBER_ID
 * 
 * [column]
 *     MEMBER_ID, MEMBER_NAME, REGISTER_DATETIME, REGISTER_USER, UPDATE_DATETIME, UPDATE_USER
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
public abstract class BsPrimaryKeyComment implements EntityDefinedCommonColumn, Serializable, Cloneable {

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
    /** (会員ID)MEMBER_ID: {PK, INTEGER(10), refers to MEMBER.MEMBER_ID} */
    protected Integer _memberId;

    /** (会員名称)MEMBER_NAME: {VARCHAR(200), refers to MEMBER.MEMBER_NAME} */
    protected String _memberName;

    /** (登録日時)REGISTER_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.REGISTER_DATETIME} */
    protected java.sql.Timestamp _registerDatetime;

    /** (登録ユーザ)REGISTER_USER: {VARCHAR(200), refers to MEMBER.REGISTER_USER} */
    protected String _registerUser;

    /** (更新日時)UPDATE_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.UPDATE_DATETIME} */
    protected java.sql.Timestamp _updateDatetime;

    /** (更新ユーザ)UPDATE_USER: {VARCHAR(200), refers to MEMBER.UPDATE_USER} */
    protected String _updateUser;

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
        return "PrimaryKeyComment";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "primaryKeyComment";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /**
     * {@inheritDoc}
     */
    public DBMeta getDBMeta() {
        return com.example.dbflute.spring.dbflute.bsentity.customize.dbmeta.PrimaryKeyCommentDbm.getInstance();
    }

    // ===================================================================================
    //                                                                         Primary Key
    //                                                                         ===========
    /**
     * {@inheritDoc}
     */
    public boolean hasPrimaryKeyValue() {
        if (getMemberId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    protected Member __innerDomain;

    protected Member innerDomain() {
        if (__innerDomain == null) {
            __innerDomain = new Member();
        }
        return __innerDomain;
    }

    /**
     * Prepare the inner instance of domain entity (basically for LoadReferrer).
     * <pre>
     * List&lt;UnpaidSummaryMember&gt; memberList = memberBhv.outsideSql()...;
     * List&lt;Member&gt; domainList = new ArrayList&lt;Member&gt;();
     * for (UnpaidSummaryMember member : memberList) {
     *     domainList.add(member.<span style="color: #FD4747">prepareDomain()</span>);
     * }
     * memberBhv.<span style="color: #FD4747">loadPurchaseList</span>(domainList, new ConditionBeanSetupper...);
     * for (UnpaidSummaryMember member : memberList) {
     *     Purchase purchase = member.<span style="color: #FD4747">getPurchaseList()</span>; <span style="color: #3F7E5E">// you can get it</span>
     *     ...
     * }
     * </pre>
     * @return The domain entity for this customize entity.
     */
    public Member prepareDomain() {
        innerDomain().setMemberId(getMemberId());
        return innerDomain();
    }

    /**
     * (会員住所情報)MEMBER_ADDRESS by your MEMBER_ID, named 'memberAddressList'.
     * @return The entity list of referrer property 'memberAddressList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<MemberAddress> getMemberAddressList() {
        return innerDomain().getMemberAddressList();
    }

    /**
     * (会員ログイン)MEMBER_LOGIN by your MEMBER_ID, named 'memberLoginList'.
     * @return The entity list of referrer property 'memberLoginList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<MemberLogin> getMemberLoginList() {
        return innerDomain().getMemberLoginList();
    }

    /**
     * (購入)PURCHASE by your MEMBER_ID, named 'purchaseList'.
     * @return The entity list of referrer property 'purchaseList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<Purchase> getPurchaseList() {
        return innerDomain().getPurchaseList();
    }

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
        if (other == null || !(other instanceof BsPrimaryKeyComment)) { return false; }
        BsPrimaryKeyComment otherEntity = (BsPrimaryKeyComment)other;
        if (!xSV(getMemberId(), otherEntity.getMemberId())) { return false; }
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
        sb.append(delimiter).append(getRegisterDatetime());
        sb.append(delimiter).append(getRegisterUser());
        sb.append(delimiter).append(getUpdateDatetime());
        sb.append(delimiter).append(getUpdateUser());
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
    public PrimaryKeyComment clone() {
        try {
            return (PrimaryKeyComment)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] (会員ID)MEMBER_ID: {PK, INTEGER(10), refers to MEMBER.MEMBER_ID} <br />
     * 会員を識別するID。連番として自動採番される。<br />
     * （会員IDだけに限らず）採番方法はDBMS次第。
     * @return The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] (会員ID)MEMBER_ID: {PK, INTEGER(10), refers to MEMBER.MEMBER_ID} <br />
     * 会員を識別するID。連番として自動採番される。<br />
     * （会員IDだけに限らず）採番方法はDBMS次第。
     * @param memberId The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        __modifiedProperties.addPropertyName("memberId");
        this._memberId = memberId;
    }

    /**
     * [get] (会員名称)MEMBER_NAME: {VARCHAR(200), refers to MEMBER.MEMBER_NAME} <br />
     * 会員のフルネームの名称。<br />
     * 苗字と名前を分けて管理することも多いが、ここでは Example なので単純にひとまとめ。
     * @return The value of the column 'MEMBER_NAME'. (NullAllowed)
     */
    public String getMemberName() {
        return _memberName;
    }

    /**
     * [set] (会員名称)MEMBER_NAME: {VARCHAR(200), refers to MEMBER.MEMBER_NAME} <br />
     * 会員のフルネームの名称。<br />
     * 苗字と名前を分けて管理することも多いが、ここでは Example なので単純にひとまとめ。
     * @param memberName The value of the column 'MEMBER_NAME'. (NullAllowed)
     */
    public void setMemberName(String memberName) {
        __modifiedProperties.addPropertyName("memberName");
        this._memberName = memberName;
    }

    /**
     * [get] (登録日時)REGISTER_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.REGISTER_DATETIME} <br />
     * レコードが登録された日時。<br />
     * 会員が登録された日時とほぼ等しいが、そういった業務的な役割を兼務させるのはあまり推奨されない。<br />
     * どのテーブルでも同じなので、共通カラムの説明はこのテーブルでしか書かない。
     * @return The value of the column 'REGISTER_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getRegisterDatetime() {
        return _registerDatetime;
    }

    /**
     * [set] (登録日時)REGISTER_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.REGISTER_DATETIME} <br />
     * レコードが登録された日時。<br />
     * 会員が登録された日時とほぼ等しいが、そういった業務的な役割を兼務させるのはあまり推奨されない。<br />
     * どのテーブルでも同じなので、共通カラムの説明はこのテーブルでしか書かない。
     * @param registerDatetime The value of the column 'REGISTER_DATETIME'. (NullAllowed)
     */
    public void setRegisterDatetime(java.sql.Timestamp registerDatetime) {
        __modifiedProperties.addPropertyName("registerDatetime");
        this._registerDatetime = registerDatetime;
    }

    /**
     * [get] (登録ユーザ)REGISTER_USER: {VARCHAR(200), refers to MEMBER.REGISTER_USER} <br />
     * レコードを登録したユーザ。<br />
     * 会員テーブルであれば当然、会員自身であるはずだが、他のテーブルの場合では管理画面から運用者による登録など考えられるので、しっかり保持しておく。
     * @return The value of the column 'REGISTER_USER'. (NullAllowed)
     */
    public String getRegisterUser() {
        return _registerUser;
    }

    /**
     * [set] (登録ユーザ)REGISTER_USER: {VARCHAR(200), refers to MEMBER.REGISTER_USER} <br />
     * レコードを登録したユーザ。<br />
     * 会員テーブルであれば当然、会員自身であるはずだが、他のテーブルの場合では管理画面から運用者による登録など考えられるので、しっかり保持しておく。
     * @param registerUser The value of the column 'REGISTER_USER'. (NullAllowed)
     */
    public void setRegisterUser(String registerUser) {
        __modifiedProperties.addPropertyName("registerUser");
        this._registerUser = registerUser;
    }

    /**
     * [get] (更新日時)UPDATE_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.UPDATE_DATETIME} <br />
     * レコードが（最後に）更新された日時。<br />
     * 業務的な利用はあまり推奨されないと別項目で説明したが、このカラムはソートの要素としてよく利用される。
     * @return The value of the column 'UPDATE_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getUpdateDatetime() {
        return _updateDatetime;
    }

    /**
     * [set] (更新日時)UPDATE_DATETIME: {TIMESTAMP(23, 10), refers to MEMBER.UPDATE_DATETIME} <br />
     * レコードが（最後に）更新された日時。<br />
     * 業務的な利用はあまり推奨されないと別項目で説明したが、このカラムはソートの要素としてよく利用される。
     * @param updateDatetime The value of the column 'UPDATE_DATETIME'. (NullAllowed)
     */
    public void setUpdateDatetime(java.sql.Timestamp updateDatetime) {
        __modifiedProperties.addPropertyName("updateDatetime");
        this._updateDatetime = updateDatetime;
    }

    /**
     * [get] (更新ユーザ)UPDATE_USER: {VARCHAR(200), refers to MEMBER.UPDATE_USER} <br />
     * レコードを更新したユーザ。<br />
     * システムは誰が何をしたのかちゃんと覚えている。
     * @return The value of the column 'UPDATE_USER'. (NullAllowed)
     */
    public String getUpdateUser() {
        return _updateUser;
    }

    /**
     * [set] (更新ユーザ)UPDATE_USER: {VARCHAR(200), refers to MEMBER.UPDATE_USER} <br />
     * レコードを更新したユーザ。<br />
     * システムは誰が何をしたのかちゃんと覚えている。
     * @param updateUser The value of the column 'UPDATE_USER'. (NullAllowed)
     */
    public void setUpdateUser(String updateUser) {
        __modifiedProperties.addPropertyName("updateUser");
        this._updateUser = updateUser;
    }
}