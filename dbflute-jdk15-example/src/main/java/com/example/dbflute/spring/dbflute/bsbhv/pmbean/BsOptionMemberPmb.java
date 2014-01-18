/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsbhv.pmbean;

import java.util.*;

import org.seasar.dbflute.outsidesql.typed.*;
import org.seasar.dbflute.jdbc.*;
import org.seasar.dbflute.jdbc.ParameterUtil.ShortCharHandlingMode;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.cbean.coption.FromToOption;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.exception.*;
import org.seasar.dbflute.util.DfTypeUtil;
import com.example.dbflute.spring.dbflute.allcommon.*;
import com.example.dbflute.spring.dbflute.exbhv.*;
import com.example.dbflute.spring.dbflute.exentity.customize.*;

/**
 * The base class for typed parameter-bean of OptionMember. <br />
 * This is related to "<span style="color: #AD4747">selectOptionMember</span>" on MemberBhv.
 * @author DBFlute(AutoGenerator)
 */
public class BsOptionMemberPmb implements ListHandlingPmb<MemberBhv, OptionMember>, EntityHandlingPmb<MemberBhv, OptionMember>, FetchBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** The parameter of memberId. */
    protected Integer _memberId;

    /** The parameter of memberName:likePrefix. */
    protected String _memberName;

    /** The option of like-search for memberName. */
    protected LikeSearchOption _memberNameInternalLikeSearchOption;

    /** The parameter of memberAccount:like. */
    protected String _memberAccount;

    /** The option of like-search for memberAccount. */
    protected LikeSearchOption _memberAccountInternalLikeSearchOption;

    /** The parameter of fromFormalizedDate:fromDate. */
    protected Date _fromFormalizedDate;

    /** The parameter of toFormalizedDate:toDate. */
    protected Date _toFormalizedDate;

    /** The parameter of fromFormalizedMonth:fromDate(option). */
    protected Date _fromFormalizedMonth;

    /** The parameter of toFormalizedMonth:toDate(option). */
    protected Date _toFormalizedMonth;

    /** The parameter of memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus}. */
    protected String _memberStatusCode;

    /** The parameter of displayOrder:ref(MEMBER_STATUS) :: refers to (表示順)DISPLAY_ORDER: {UQ, NotNull, INTEGER(10)}. */
    protected Integer _displayOrder;

    /** The parameter of birthdate:fromDate|ref(MEMBER.BIRTHDATE) :: refers to (生年月日)BIRTHDATE: {DATE(8)}. */
    protected Date _birthdate;

    /** The parameter of status:cls(MemberStatus). */
    protected String _status;

    /** The parameter of statusFormalized:cls(MemberStatus.Formalized). */
    protected String _statusFormalized = CDef.MemberStatus.Formalized.code();

    /** The parameter of statusList:ref(MEMBER.MEMBER_STATUS_CODE) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus}. */
    protected List<com.example.dbflute.spring.dbflute.allcommon.CDef.MemberStatus> _statusList;

    /** The parameter of statusFixedList:cls(MemberStatus.Formalized, Withdrawal). */
    protected List<String> _statusFixedList = newArrayList(CDef.MemberStatus.Formalized.code(), CDef.MemberStatus.Withdrawal.code());

    /** The parameter of paymentCompleteFlg:cls(Flg). */
    protected Integer _paymentCompleteFlg;

    /** The parameter of paymentCompleteTrue:cls(Flg.True). */
    protected Integer _paymentCompleteTrue = toNumber(CDef.Flg.True.code(), Integer.class);

    /** The max size of safety result. */
    protected int _safetyMaxResultSize;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor for the typed parameter-bean of OptionMember. <br />
     * This is related to "<span style="color: #AD4747">selectOptionMember</span>" on MemberBhv.
     */
    public BsOptionMemberPmb() {
    }

    // ===================================================================================
    //                                                                Typed Implementation
    //                                                                ====================
    /**
     * {@inheritDoc}
     */
    public String getOutsideSqlPath() {
        return "selectOptionMember";
    }

    /**
     * Get the type of an entity for result. (implementation)
     * @return The type instance of an entity, customize entity. (NotNull)
     */
    public Class<OptionMember> getEntityType() {
        return OptionMember.class;
    }

    // ===================================================================================
    //                                                                       Safety Result
    //                                                                       =============
    /**
     * {@inheritDoc}
     */
    public void checkSafetyResult(int safetyMaxResultSize) {
        _safetyMaxResultSize = safetyMaxResultSize;
    }

    /**
     * {@inheritDoc}
     */
    public int getSafetyMaxResultSize() {
        return _safetyMaxResultSize;
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected String filterStringParameter(String value) {
        if (isEmptyStringParameterAllowed()) {
            return value;
        }
        return convertEmptyToNull(value);
    }

    protected boolean isEmptyStringParameterAllowed() {
	    return DBFluteConfig.getInstance().isEmptyStringParameterAllowed();
    }

    protected String convertEmptyToNull(String value) {
	    return ParameterUtil.convertEmptyToNull(value);
    }

    protected String handleShortChar(String propertyName, String value, Integer size) {
        ShortCharHandlingMode mode = getShortCharHandlingMode(propertyName, value, size);
        return ParameterUtil.handleShortChar(propertyName, value, size, mode);
    }

    protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
        return ShortCharHandlingMode.NONE;
    }

    @SuppressWarnings("unchecked")
    protected <ELEMENT> ArrayList<ELEMENT> newArrayList(ELEMENT... elements) {
        Object obj = DfCollectionUtil.newArrayList(elements);
        return (ArrayList<ELEMENT>)obj; // to avoid the warning between JDK6 and JDK7
    }

    @SuppressWarnings("unchecked")
    protected <NUMBER extends Number> NUMBER toNumber(Object obj, Class<NUMBER> type) {
        return (NUMBER)DfTypeUtil.toNumber(obj, type);
    }

    protected Boolean toBoolean(Object obj) {
        return DfTypeUtil.toBoolean(obj);
    }

    protected Date toUtilDate(Date date) {
        return DfTypeUtil.toDate(date); // if sub class, re-create as pure date
    }

    protected String formatUtilDate(Date date) {
        String pattern = "yyyy-MM-dd";
        return DfTypeUtil.toString(date, pattern);
    }

    protected String formatByteArray(byte[] bytes) {
        return "byte[" + (bytes != null ? String.valueOf(bytes.length) : "null") + "]";
    }

    protected void assertLikeSearchOptionValid(String name, LikeSearchOption option) {
        if (option == null) {
            String msg = "The like-search option is required!";
            throw new RequiredOptionNotFoundException(msg);
        }
        if (option.isSplit()) {
            String msg = "The split of like-search is NOT available on parameter-bean.";
            msg = msg + " Don't use splitByXxx(): " + option;
            throw new IllegalOutsideSqlOperationException(msg);
        }
    }

    protected void assertFromToOptionValid(String name, FromToOption option) {
        if (option == null) {
            String msg = "The from-to option is required!";
            throw new RequiredOptionNotFoundException(msg);
        }
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * @return The display string of all parameters. (NotNull)
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DfTypeUtil.toClassTitle(this)).append(":");
        sb.append(xbuildColumnString());
        return sb.toString();
    }
    private String xbuildColumnString() {
        final String c = ", ";
        final StringBuilder sb = new StringBuilder();
        sb.append(c).append(_memberId);
        sb.append(c).append(_memberName);
        sb.append(c).append(_memberAccount);
        sb.append(c).append(formatUtilDate(_fromFormalizedDate));
        sb.append(c).append(formatUtilDate(_toFormalizedDate));
        sb.append(c).append(formatUtilDate(_fromFormalizedMonth));
        sb.append(c).append(formatUtilDate(_toFormalizedMonth));
        sb.append(c).append(_memberStatusCode);
        sb.append(c).append(_displayOrder);
        sb.append(c).append(formatUtilDate(_birthdate));
        sb.append(c).append(_status);
        sb.append(c).append(_statusFormalized);
        sb.append(c).append(_statusList);
        sb.append(c).append(_statusFixedList);
        sb.append(c).append(_paymentCompleteFlg);
        sb.append(c).append(_paymentCompleteTrue);
        if (sb.length() > 0) { sb.delete(0, c.length()); }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] memberId <br />
     * @return The value of memberId. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Integer getMemberId() {
        return _memberId;
    }

    /**
     * [set] memberId <br />
     * @param memberId The value of memberId. (NullAllowed)
     */
    public void setMemberId(Integer memberId) {
        _memberId = memberId;
    }

    /**
     * [get] memberName:likePrefix <br />
     * @return The value of memberName. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getMemberName() {
        return filterStringParameter(_memberName);
    }

    /**
     * [set as prefixSearch] memberName:likePrefix <br />
     * @param memberName The value of memberName. (NullAllowed)
     */
    public void setMemberName_PrefixSearch(String memberName) {
        _memberName = memberName;
        _memberNameInternalLikeSearchOption = new LikeSearchOption().likePrefix();
    }

    /**
     * Get the internal option of likeSearch for memberName. {Internal Method: Don't invoke this}
     * @return The internal option of likeSearch for memberName. (NullAllowed)
     */
    public LikeSearchOption getMemberNameInternalLikeSearchOption() {
        return _memberNameInternalLikeSearchOption;
    }

    /**
     * [get] memberAccount:like <br />
     * @return The value of memberAccount. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getMemberAccount() {
        return filterStringParameter(_memberAccount);
    }

    /**
     * [set as likeSearch] memberAccount:like <br />
     * @param memberAccount The value of memberAccount. (NullAllowed)
     * @param memberAccountOption The option of likeSearch for memberAccount which is NOT split mode. (NotNull)
     */
    public void setMemberAccount(String memberAccount, LikeSearchOption memberAccountOption) {
        assertLikeSearchOptionValid("memberAccountOption", memberAccountOption);
        _memberAccount = memberAccount;
        _memberAccountInternalLikeSearchOption = memberAccountOption;
    }

    /**
     * Get the internal option of likeSearch for memberAccount. {Internal Method: Don't invoke this}
     * @return The internal option of likeSearch for memberAccount. (NullAllowed)
     */
    public LikeSearchOption getMemberAccountInternalLikeSearchOption() {
        return _memberAccountInternalLikeSearchOption;
    }

    /**
     * [get] fromFormalizedDate:fromDate <br />
     * @return The value of fromFormalizedDate. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Date getFromFormalizedDate() {
        return toUtilDate(_fromFormalizedDate);
    }

    /**
     * [set as fromDate] fromFormalizedDate:fromDate <br />
     * @param fromFormalizedDate The value of fromFormalizedDate. (NullAllowed)
     */
    public void setFromFormalizedDate_FromDate(Date fromFormalizedDate) {
        _fromFormalizedDate = new FromToOption().compareAsDate().filterFromDate(fromFormalizedDate);
    }

    /**
     * [get] toFormalizedDate:toDate <br />
     * @return The value of toFormalizedDate. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Date getToFormalizedDate() {
        return toUtilDate(_toFormalizedDate);
    }

    /**
     * [set as toDate] toFormalizedDate:toDate <br />
     * @param toFormalizedDate The value of toFormalizedDate. (NullAllowed)
     */
    public void setToFormalizedDate_ToDate(Date toFormalizedDate) {
        _toFormalizedDate = new FromToOption().compareAsDate().filterToDate(toFormalizedDate);
    }

    /**
     * [get] fromFormalizedMonth:fromDate(option) <br />
     * @return The value of fromFormalizedMonth. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Date getFromFormalizedMonth() {
        return toUtilDate(_fromFormalizedMonth);
    }

    /**
     * [set as fromScope] fromFormalizedMonth:fromDate(option) <br />
     * @param fromFormalizedMonth The value of fromFormalizedMonth. (NullAllowed)
     * @param fromFormalizedMonthOption The option of from-to scope for fromFormalizedMonth. (NotNull)
     */
    public void setFromFormalizedMonth_FromDate(Date fromFormalizedMonth, FromToOption fromFormalizedMonthOption) {
        assertFromToOptionValid("fromFormalizedMonthOption", fromFormalizedMonthOption);
        _fromFormalizedMonth = fromFormalizedMonthOption.filterFromDate(fromFormalizedMonth);
    }

    /**
     * [get] toFormalizedMonth:toDate(option) <br />
     * @return The value of toFormalizedMonth. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Date getToFormalizedMonth() {
        return toUtilDate(_toFormalizedMonth);
    }

    /**
     * [set as toScope] toFormalizedMonth:toDate(option) <br />
     * @param toFormalizedMonth The value of toFormalizedMonth. (NullAllowed)
     * @param toFormalizedMonthOption The option of from-to scope for toFormalizedMonth. (NotNull)
     */
    public void setToFormalizedMonth_ToDate(Date toFormalizedMonth, FromToOption toFormalizedMonthOption) {
        assertFromToOptionValid("toFormalizedMonthOption", toFormalizedMonthOption);
        _toFormalizedMonth = toFormalizedMonthOption.filterToDate(toFormalizedMonth);
    }

    /**
     * [get] memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * reference option (including classification)
     * @return The value of memberStatusCode. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getMemberStatusCode() {
        return filterStringParameter(_memberStatusCode);
    }

    /**
     * [set] memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * reference option (including classification)
     * @param memberStatusCode The value of memberStatusCode. (NullAllowed)
     */
    public void setMemberStatusCode(String memberStatusCode) {
        _memberStatusCode = memberStatusCode;
    }

    /**
     * [set as Formalized] memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * reference option (including classification) <br />
     * as formal member, allowed to use all service
     */
    public void setMemberStatusCode_Formalized() {
        _memberStatusCode = CDef.MemberStatus.Formalized.code();
    }

    /**
     * [set as Withdrawal] memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * reference option (including classification) <br />
     * withdrawal is fixed, not allowed to use service
     */
    public void setMemberStatusCode_Withdrawal() {
        _memberStatusCode = CDef.MemberStatus.Withdrawal.code();
    }

    /**
     * [set as Provisional] memberStatusCode:ref(MEMBER) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * reference option (including classification) <br />
     * first status after entry, allowed to use only part of service
     */
    public void setMemberStatusCode_Provisional() {
        _memberStatusCode = CDef.MemberStatus.Provisional.code();
    }

    /**
     * [get] displayOrder:ref(MEMBER_STATUS) :: refers to (表示順)DISPLAY_ORDER: {UQ, NotNull, INTEGER(10)} <br />
     * @return The value of displayOrder. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Integer getDisplayOrder() {
        return _displayOrder;
    }

    /**
     * [set] displayOrder:ref(MEMBER_STATUS) :: refers to (表示順)DISPLAY_ORDER: {UQ, NotNull, INTEGER(10)} <br />
     * @param displayOrder The value of displayOrder. (NullAllowed)
     */
    public void setDisplayOrder(Integer displayOrder) {
        _displayOrder = displayOrder;
    }

    /**
     * [get] birthdate:fromDate|ref(MEMBER.BIRTHDATE) :: refers to (生年月日)BIRTHDATE: {DATE(8)} <br />
     * several options
     * @return The value of birthdate. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Date getBirthdate() {
        return toUtilDate(_birthdate);
    }

    /**
     * [set as fromDate] birthdate:fromDate|ref(MEMBER.BIRTHDATE) :: refers to (生年月日)BIRTHDATE: {DATE(8)} <br />
     * several options
     * @param birthdate The value of birthdate. (NullAllowed)
     */
    public void setBirthdate_FromDate(Date birthdate) {
        _birthdate = new FromToOption().compareAsDate().filterFromDate(birthdate);
    }

    /**
     * [get] status:cls(MemberStatus) <br />
     * direct classification setting
     * @return The value of status. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getStatus() {
        return filterStringParameter(_status);
    }

    /**
     * [set] status:cls(MemberStatus) <br />
     * direct classification setting
     * @param status The value of status. (NullAllowed)
     */
    public void setStatus(String status) {
        _status = status;
    }

    /**
     * [set as Formalized] status:cls(MemberStatus) <br />
     * direct classification setting <br />
     * as formal member, allowed to use all service
     */
    public void setStatus_Formalized() {
        _status = CDef.MemberStatus.Formalized.code();
    }

    /**
     * [set as Withdrawal] status:cls(MemberStatus) <br />
     * direct classification setting <br />
     * withdrawal is fixed, not allowed to use service
     */
    public void setStatus_Withdrawal() {
        _status = CDef.MemberStatus.Withdrawal.code();
    }

    /**
     * [set as Provisional] status:cls(MemberStatus) <br />
     * direct classification setting <br />
     * first status after entry, allowed to use only part of service
     */
    public void setStatus_Provisional() {
        _status = CDef.MemberStatus.Provisional.code();
    }

    /**
     * [get] statusFormalized:cls(MemberStatus.Formalized) <br />
     * fixed classification setting
     * @return The value of statusFormalized. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getStatusFormalized() {
        return filterStringParameter(_statusFormalized);
    }

    /**
     * [get] statusList:ref(MEMBER.MEMBER_STATUS_CODE) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * classification to list
     * @return The value of statusList. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public List<com.example.dbflute.spring.dbflute.allcommon.CDef.MemberStatus> getStatusList() {
        return _statusList;
    }

    /**
     * [set] statusList:ref(MEMBER.MEMBER_STATUS_CODE) :: refers to (会員ステータスコード)MEMBER_STATUS_CODE: {IX, NotNull, CHAR(3), FK to MEMBER_STATUS, classification=MemberStatus} <br />
     * classification to list
     * @param statusList The value of statusList. (NullAllowed)
     */
    public void setStatusList(List<com.example.dbflute.spring.dbflute.allcommon.CDef.MemberStatus> statusList) {
        _statusList = statusList;
    }

    /**
     * [get] statusFixedList:cls(MemberStatus.Formalized, Withdrawal) <br />
     * fixed classification list
     * @return The value of statusFixedList. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public List<String> getStatusFixedList() {
        return _statusFixedList;
    }

    /**
     * [get] paymentCompleteFlg:cls(Flg) <br />
     * direct one as Integer
     * @return The value of paymentCompleteFlg. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Integer getPaymentCompleteFlg() {
        return _paymentCompleteFlg;
    }

    /**
     * [set] paymentCompleteFlg:cls(Flg) <br />
     * direct one as Integer
     * @param paymentCompleteFlg The value of paymentCompleteFlg. (NullAllowed)
     */
    public void setPaymentCompleteFlg(Integer paymentCompleteFlg) {
        _paymentCompleteFlg = paymentCompleteFlg;
    }

    /**
     * [set as True] paymentCompleteFlg:cls(Flg) <br />
     * direct one as Integer <br />
     * means yes
     */
    public void setPaymentCompleteFlg_True() {
        _paymentCompleteFlg = toNumber(CDef.Flg.True.code(), Integer.class);
    }

    /**
     * [set as False] paymentCompleteFlg:cls(Flg) <br />
     * direct one as Integer <br />
     * means no
     */
    public void setPaymentCompleteFlg_False() {
        _paymentCompleteFlg = toNumber(CDef.Flg.False.code(), Integer.class);
    }

    /**
     * [get] paymentCompleteTrue:cls(Flg.True) <br />
     * fixed one as Integer
     * @return The value of paymentCompleteTrue. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public Integer getPaymentCompleteTrue() {
        return _paymentCompleteTrue;
    }

}