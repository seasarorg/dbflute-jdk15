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
 * The entity of (会員退会情報)MEMBER_WITHDRAWAL as TABLE. <br />
 * 退会会員の退会に関する詳細な情報。<br />
 * 退会会員のみデータが存在する。（"1 : 0...1" のパターンの one-to-one）<br />
 * 共通カラムがあってバージョンNOがないパターン。基本的に更新が入ることはないが、登録ユーザを保持したいのと、重要なデータなので更新系の情報も。
 * <pre>
 * [primary-key]
 *     MEMBER_ID
 * 
 * [column]
 *     MEMBER_ID, WITHDRAWAL_REASON_CODE, WITHDRAWAL_REASON_INPUT_TEXT, WITHDRAWAL_DATETIME, REGISTER_DATETIME, REGISTER_USER, UPDATE_DATETIME, UPDATE_USER, VERSION_NO
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
 *     MEMBER, WITHDRAWAL_REASON
 * 
 * [referrer-table]
 *     
 * 
 * [foreign-property]
 *     member, withdrawalReason
 * 
 * [referrer-property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsMemberWithdrawal implements EntityDefinedCommonColumn, Serializable, Cloneable {

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
    /** MEMBER_ID: {PK, NotNull, INTEGER(10), FK to MEMBER} */
    protected Integer _memberId;

    /** (退会理由コード)WITHDRAWAL_REASON_CODE: {IX, CHAR(3), FK to WITHDRAWAL_REASON, classification=WithdrawalReason} */
    protected String _withdrawalReasonCode;

    /** (退会理由入力テキスト)WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} */
    protected String _withdrawalReasonInputText;

    /** (退会日時)WITHDRAWAL_DATETIME: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _withdrawalDatetime;

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
        return "MEMBER_WITHDRAWAL";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "memberWithdrawal";
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
        if (getMemberId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Set the value of withdrawalReasonCode as the classification of WithdrawalReason. <br />
     * reason for member withdrawal
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public void setWithdrawalReasonCodeAsWithdrawalReason(CDef.WithdrawalReason cdef) {
        setWithdrawalReasonCode(cdef != null ? cdef.code() : null);
    }

    /**
     * Get the value of withdrawalReasonCode as the classification of WithdrawalReason. <br />
     * reason for member withdrawal
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed)
     */
    public CDef.WithdrawalReason getWithdrawalReasonCodeAsWithdrawalReason() {
        return CDef.WithdrawalReason.codeOf(getWithdrawalReasonCode());
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of withdrawalReasonCode as Sit (SIT). <br />
     * SIT: サイトが使いにくいから
     */
    public void setWithdrawalReasonCode_Sit() {
        setWithdrawalReasonCodeAsWithdrawalReason(CDef.WithdrawalReason.Sit);
    }

    /**
     * Set the value of withdrawalReasonCode as Prd (PRD). <br />
     * PRD: 商品に魅力がないから
     */
    public void setWithdrawalReasonCode_Prd() {
        setWithdrawalReasonCodeAsWithdrawalReason(CDef.WithdrawalReason.Prd);
    }

    /**
     * Set the value of withdrawalReasonCode as Frt (FRT). <br />
     * FRT: フリテンだから
     */
    public void setWithdrawalReasonCode_Frt() {
        setWithdrawalReasonCodeAsWithdrawalReason(CDef.WithdrawalReason.Frt);
    }

    /**
     * Set the value of withdrawalReasonCode as Oth (OTH). <br />
     * OTH: その他理由
     */
    public void setWithdrawalReasonCode_Oth() {
        setWithdrawalReasonCodeAsWithdrawalReason(CDef.WithdrawalReason.Oth);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of withdrawalReasonCode Sit? <br />
     * SIT: サイトが使いにくいから
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isWithdrawalReasonCodeSit() {
        CDef.WithdrawalReason cdef = getWithdrawalReasonCodeAsWithdrawalReason();
        return cdef != null ? cdef.equals(CDef.WithdrawalReason.Sit) : false;
    }

    /**
     * Is the value of withdrawalReasonCode Prd? <br />
     * PRD: 商品に魅力がないから
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isWithdrawalReasonCodePrd() {
        CDef.WithdrawalReason cdef = getWithdrawalReasonCodeAsWithdrawalReason();
        return cdef != null ? cdef.equals(CDef.WithdrawalReason.Prd) : false;
    }

    /**
     * Is the value of withdrawalReasonCode Frt? <br />
     * FRT: フリテンだから
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isWithdrawalReasonCodeFrt() {
        CDef.WithdrawalReason cdef = getWithdrawalReasonCodeAsWithdrawalReason();
        return cdef != null ? cdef.equals(CDef.WithdrawalReason.Frt) : false;
    }

    /**
     * Is the value of withdrawalReasonCode Oth? <br />
     * OTH: その他理由
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isWithdrawalReasonCodeOth() {
        CDef.WithdrawalReason cdef = getWithdrawalReasonCodeAsWithdrawalReason();
        return cdef != null ? cdef.equals(CDef.WithdrawalReason.Oth) : false;
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

    /** (退会理由)WITHDRAWAL_REASON by my WITHDRAWAL_REASON_CODE, named 'withdrawalReason'. */
    protected WithdrawalReason _withdrawalReason;

    /**
     * (退会理由)WITHDRAWAL_REASON by my WITHDRAWAL_REASON_CODE, named 'withdrawalReason'.
     * @return The entity of foreign property 'withdrawalReason'. (NullAllowed: If the foreign key does not have 'NotNull' constraint, please check null.)
     */
    public WithdrawalReason getWithdrawalReason() {
        return _withdrawalReason;
    }

    /**
     * (退会理由)WITHDRAWAL_REASON by my WITHDRAWAL_REASON_CODE, named 'withdrawalReason'.
     * @param withdrawalReason The entity of foreign property 'withdrawalReason'. (NullAllowed)
     */
    public void setWithdrawalReason(WithdrawalReason withdrawalReason) {
        _withdrawalReason = withdrawalReason;
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
        if (other == null || !(other instanceof BsMemberWithdrawal)) { return false; }
        BsMemberWithdrawal otherEntity = (BsMemberWithdrawal)other;
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
        String l = "\n  ";
        if (_member != null)
        { sb.append(l).append(xbRDS(_member, "member")); }
        if (_withdrawalReason != null)
        { sb.append(l).append(xbRDS(_withdrawalReason, "withdrawalReason")); }
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
        sb.append(delimiter).append(getMemberId());
        sb.append(delimiter).append(getWithdrawalReasonCode());
        sb.append(delimiter).append(getWithdrawalReasonInputText());
        sb.append(delimiter).append(getWithdrawalDatetime());
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
        if (_withdrawalReason != null) { sb.append(c).append("withdrawalReason"); }
        if (sb.length() > c.length()) {
            sb.delete(0, c.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    public MemberWithdrawal clone() {
        try {
            return (MemberWithdrawal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] MEMBER_ID: {PK, NotNull, INTEGER(10), FK to MEMBER} <br />
     * @return The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] MEMBER_ID: {PK, NotNull, INTEGER(10), FK to MEMBER} <br />
     * @param memberId The value of the column 'MEMBER_ID'. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        __modifiedProperties.addPropertyName("memberId");
        this._memberId = memberId;
    }

    /**
     * [get] (退会理由コード)WITHDRAWAL_REASON_CODE: {IX, CHAR(3), FK to WITHDRAWAL_REASON, classification=WithdrawalReason} <br />
     * 退会した定型理由を参照するコード。<br />
     * 何も言わずに退会する会員もいるので必須項目ではない。
     * @return The value of the column 'WITHDRAWAL_REASON_CODE'. (NullAllowed)
     */
    public String getWithdrawalReasonCode() {
        return _withdrawalReasonCode;
    }

    /**
     * [set] (退会理由コード)WITHDRAWAL_REASON_CODE: {IX, CHAR(3), FK to WITHDRAWAL_REASON, classification=WithdrawalReason} <br />
     * 退会した定型理由を参照するコード。<br />
     * 何も言わずに退会する会員もいるので必須項目ではない。
     * @param withdrawalReasonCode The value of the column 'WITHDRAWAL_REASON_CODE'. (NullAllowed)
     */
    public void setWithdrawalReasonCode(String withdrawalReasonCode) {
        __modifiedProperties.addPropertyName("withdrawalReasonCode");
        this._withdrawalReasonCode = withdrawalReasonCode;
    }

    /**
     * [get] (退会理由入力テキスト)WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} <br />
     * 会員がフリーテキストで入力できる退会理由。<br />
     * もう言いたいこと言ってもらう感じ。<br />
     * サイト運営側としては真摯に受け止めて改善していきたい。
     * @return The value of the column 'WITHDRAWAL_REASON_INPUT_TEXT'. (NullAllowed)
     */
    public String getWithdrawalReasonInputText() {
        return _withdrawalReasonInputText;
    }

    /**
     * [set] (退会理由入力テキスト)WITHDRAWAL_REASON_INPUT_TEXT: {CLOB(2147483647)} <br />
     * 会員がフリーテキストで入力できる退会理由。<br />
     * もう言いたいこと言ってもらう感じ。<br />
     * サイト運営側としては真摯に受け止めて改善していきたい。
     * @param withdrawalReasonInputText The value of the column 'WITHDRAWAL_REASON_INPUT_TEXT'. (NullAllowed)
     */
    public void setWithdrawalReasonInputText(String withdrawalReasonInputText) {
        __modifiedProperties.addPropertyName("withdrawalReasonInputText");
        this._withdrawalReasonInputText = withdrawalReasonInputText;
    }

    /**
     * [get] (退会日時)WITHDRAWAL_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * 退会した瞬間の日時。<br />
     * 正式会員日時と違い、こっちはone-to-oneの別テーブルで。
     * @return The value of the column 'WITHDRAWAL_DATETIME'. (NullAllowed)
     */
    public java.sql.Timestamp getWithdrawalDatetime() {
        return _withdrawalDatetime;
    }

    /**
     * [set] (退会日時)WITHDRAWAL_DATETIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * 退会した瞬間の日時。<br />
     * 正式会員日時と違い、こっちはone-to-oneの別テーブルで。
     * @param withdrawalDatetime The value of the column 'WITHDRAWAL_DATETIME'. (NullAllowed)
     */
    public void setWithdrawalDatetime(java.sql.Timestamp withdrawalDatetime) {
        __modifiedProperties.addPropertyName("withdrawalDatetime");
        this._withdrawalDatetime = withdrawalDatetime;
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
