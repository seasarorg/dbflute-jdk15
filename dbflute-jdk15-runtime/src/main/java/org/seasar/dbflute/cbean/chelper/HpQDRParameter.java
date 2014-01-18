package org.seasar.dbflute.cbean.chelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.cbean.coption.DateFromToOption;
import org.seasar.dbflute.cbean.coption.DerivedReferrerOption;
import org.seasar.dbflute.cbean.coption.FromToOption;

/**
 * The parameter of (Query)DerivedReferrer.
 * @author jflute
 * @param <CB> The type of condition-bean.
 * @param <PARAMETER> The type of parameter.
 */
public class HpQDRParameter<CB extends ConditionBean, PARAMETER> {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String _function;
    protected SubQuery<CB> _subQuery;
    protected DerivedReferrerOption _option;
    protected HpQDRSetupper<CB> _setupper;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HpQDRParameter(String function, SubQuery<CB> subQuery, DerivedReferrerOption option,
            HpQDRSetupper<CB> setupper) {
        _function = function;
        _subQuery = subQuery;
        _option = option;
        _setupper = setupper;
    }

    // ===================================================================================
    //                                                                           Condition
    //                                                                           =========
    /**
     * Set up the operand 'equal' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).equal(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void equal(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_EQUAL.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'notEqual' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).notEqual(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void notEqual(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_NOT_EQUAL_STANDARD.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'greaterThan' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).greaterThan(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void greaterThan(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_GREATER_THAN.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'lessThan' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).lessThan(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void lessThan(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_LESS_THAN.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'greaterEqual' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).greaterEqual(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void greaterEqual(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_GREATER_EQUAL.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'lessEqual' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).lessEqual(123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param value The value of parameter. (NotNull) 
     */
    public void lessEqual(PARAMETER value) {
        assertParameterNotNull(value);
        _setupper.setup(_function, _subQuery, ConditionKey.CK_LESS_EQUAL.getOperand(), value, _option);
    }

    /**
     * Set up the operand 'isNull' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. 
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice();
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).isNull(); <span style="color: #3F7E5E">// no parameter</span>
     * </pre>
     */
    public void isNull() {
        _setupper.setup(_function, _subQuery, ConditionKey.CK_IS_NULL.getOperand(), null, _option);
    }

    /**
     * Set up the operand 'isNull' and the value of parameter. <br />
     * The type of the parameter should be same as the type of target column. 
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice();
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).isNotNull(); <span style="color: #3F7E5E">// no parameter</span>
     * </pre>
     */
    public void isNotNull() {
        _setupper.setup(_function, _subQuery, ConditionKey.CK_IS_NOT_NULL.getOperand(), null, _option);
    }

    /**
     * Set up the operand 'between' and the values of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchasePrice(); <span style="color: #3F7E5E">// If the type is Integer...</span>
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).between(53, 123); <span style="color: #3F7E5E">// This parameter should be Integer!</span>
     * </pre>
     * @param fromValue The 'from' value of parameter. (NotNull) 
     * @param toValue The 'to' value of parameter. (NotNull) 
     */
    public void between(PARAMETER fromValue, PARAMETER toValue) {
        assertParameterFromNotNull(fromValue);
        assertParameterToNotNull(toValue);
        final List<PARAMETER> fromToValueList = new ArrayList<PARAMETER>();
        fromToValueList.add(fromValue);
        fromToValueList.add(toValue);
        _setupper.setup(_function, _subQuery, "between", fromToValueList, _option);
    }

    /**
     * Set up the comparison 'DateFromTo' and the values of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).dateFromTo(toTimestamp("2012/03/05"), toTimestamp("2012/03/07"));
     * <span style="color: #3F7E5E">// PURCHASE_DATE between 2012/03/05 00:00:00 and 2012/03/07 23:59:59.999</span>
     * </pre>
     * @param fromDate The 'from' date of parameter. (NullAllowed: if null, from-date condition is ignored) 
     * @param toDate The 'to' date of parameter. (NullAllowed: if null, to-date condition is ignored) 
     */
    public void dateFromTo(Date fromDate, Date toDate) {
        doFromTo(fromDate, toDate, new DateFromToOption());
    }

    /**
     * Set up the comparison 'FromTo' and the values of parameter. <br />
     * The type of the parameter should be same as the type of target column. <br />
     * If the specified column is date type and has time-parts, you should use java.sql.Timestamp type.
     * <pre>
     * cb.query().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *         subCB.query().setPaymentCompleteFlg_Equal_True();
     *     }
     * }).fromTo(toTimestamp("2012/02/05"), toTimestamp("2012/04/07"), new FromToOption().compareAsMonth());
     * <span style="color: #3F7E5E">// PURCHASE_DATE between 2012/02/01 00:00:00 and 2012/04/30 23:59:59.999</span>
     * </pre>
     * @param fromDate The 'from' date of parameter. (NullAllowed: if null, from-date condition is ignored) 
     * @param toDate The 'to' date of parameter. (NullAllowed: if null, to-date condition is ignored) 
     * @param option The option of from-to. (NotNull)
     */
    public void fromTo(Date fromDate, Date toDate, FromToOption option) {
        doFromTo(fromDate, toDate, option);
    }

    protected void doFromTo(Date fromDate, Date toDate, FromToOption option) {
        if (option == null) {
            String msg = "The argument 'option' of parameter for DerivedReferrer should not be null.";
            throw new IllegalArgumentException(msg);
        }
        if (fromDate == null && toDate == null) {
            return;
        }
        if (fromDate != null) {
            fromDate = option.filterFromDate(fromDate);
        }
        if (toDate != null) {
            toDate = option.xfilterToDateBetweenWay(toDate);
        }
        @SuppressWarnings("unchecked")
        final PARAMETER fromValue = (PARAMETER) fromDate;
        @SuppressWarnings("unchecked")
        final PARAMETER toValue = (PARAMETER) toDate;
        dispatchFromTo(fromValue, toValue);
    }

    protected void dispatchFromTo(PARAMETER fromValue, PARAMETER toValue) {
        if (fromValue != null && toValue != null) {
            between(fromValue, toValue);
        } else if (fromValue != null) {
            greaterEqual(fromValue);
        } else if (toValue != null) {
            lessEqual(toValue);
        }
    }

    // ===================================================================================
    //                                                                       Assert Helper
    //                                                                       =============
    protected void assertParameterNotNull(Object value) {
        if (value == null) {
            String msg = "The argument 'value' of parameter for DerivedReferrer should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }

    protected void assertParameterFromNotNull(Object fromValue) {
        if (fromValue == null) {
            String msg = "The argument 'fromValue' of parameter for DerivedReferrer should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }

    protected void assertParameterToNotNull(Object toValue) {
        if (toValue == null) {
            String msg = "The argument 'toValue' of parameter for DerivedReferrer should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }

    // ===================================================================================
    //                                                                       Determination
    //                                                                       =============
    public static boolean isOperandIsNull(String operand) { // basically for auto-detect of inner-join
        return ConditionKey.CK_IS_NULL.getOperand().equals(operand);
    }
}
