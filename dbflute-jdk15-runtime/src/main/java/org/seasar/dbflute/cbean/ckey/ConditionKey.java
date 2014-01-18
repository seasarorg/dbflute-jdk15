/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dbflute.cbean.ckey;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.dbflute.cbean.cipher.ColumnFunctionCipher;
import org.seasar.dbflute.cbean.coption.ConditionOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.cvalue.ConditionValue.CallbackProcessor;
import org.seasar.dbflute.cbean.cvalue.ConditionValue.QueryModeProvider;
import org.seasar.dbflute.cbean.sqlclause.query.QueryClause;
import org.seasar.dbflute.cbean.sqlclause.query.StringQueryClause;
import org.seasar.dbflute.dbmeta.name.ColumnRealName;

/**
 * The abstract class of condition-key.
 * @author jflute
 */
public abstract class ConditionKey implements Serializable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    /** Log-instance. */
    private static final Log _log = LogFactory.getLog(ConditionKey.class);

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The condition key of equal. */
    public static final ConditionKey CK_EQUAL = new ConditionKeyEqual();

    /** The condition key of notEqual as standard. */
    public static final ConditionKey CK_NOT_EQUAL_STANDARD = new ConditionKeyNotEqualStandard();

    /** The condition key of notEqual as tradition. */
    public static final ConditionKey CK_NOT_EQUAL_TRADITION = new ConditionKeyNotEqualTradition();

    /** The condition key of greaterThan. */
    public static final ConditionKey CK_GREATER_THAN = new ConditionKeyGreaterThan();

    /** The condition key of greaterThan with orIsNull. */
    public static final ConditionKey CK_GREATER_THAN_OR_IS_NULL = new ConditionKeyGreaterThanOrIsNull();

    /** The condition key of lessThan. */
    public static final ConditionKey CK_LESS_THAN = new ConditionKeyLessThan();

    /** The condition key of lessThan with orIsNull. */
    public static final ConditionKey CK_LESS_THAN_OR_IS_NULL = new ConditionKeyLessThanOrIsNull();

    /** The condition key of greaterEqual. */
    public static final ConditionKey CK_GREATER_EQUAL = new ConditionKeyGreaterEqual();

    /** The condition key of greaterEqual with orIsNull. */
    public static final ConditionKey CK_GREATER_EQUAL_OR_IS_NULL = new ConditionKeyGreaterEqualOrIsNull();

    /** The condition key of lessEqual. */
    public static final ConditionKey CK_LESS_EQUAL = new ConditionKeyLessEqual();

    /** The condition key of lessEqual with orIsNull. */
    public static final ConditionKey CK_LESS_EQUAL_OR_IS_NULL = new ConditionKeyLessEqualOrIsNull();

    /** The condition key of inScope. */
    public static final ConditionKey CK_IN_SCOPE = new ConditionKeyInScope();

    /** The condition key of notInScope. */
    public static final ConditionKey CK_NOT_IN_SCOPE = new ConditionKeyNotInScope();

    /** The condition key of likeSearch. */
    public static final ConditionKey CK_LIKE_SEARCH = new ConditionKeyLikeSearch();

    /** The condition key of notLikeSearch. */
    public static final ConditionKey CK_NOT_LIKE_SEARCH = new ConditionKeyNotLikeSearch();

    /** The condition key of isNull. */
    public static final ConditionKey CK_IS_NULL = new ConditionKeyIsNull();

    /** The condition key of isNullOrEmpty. */
    public static final ConditionKey CK_IS_NULL_OR_EMPTY = new ConditionKeyIsNullOrEmpty();

    /** The condition key of isNotNull. */
    public static final ConditionKey CK_IS_NOT_NULL = new ConditionKeyIsNotNull();

    /** Dummy-object for IsNull and IsNotNull and so on... */
    protected static final Object DUMMY_OBJECT = new Object();

    /**
     * Is the condition key null-able?
     * @param key The condition key. (NotNull)
     * @return The determination, true or false.
     */
    public static boolean isNullaleConditionKey(ConditionKey key) {
        return CK_GREATER_EQUAL_OR_IS_NULL.equals(key) || CK_GREATER_THAN_OR_IS_NULL.equals(key)
                || CK_LESS_EQUAL_OR_IS_NULL.equals(key) || CK_LESS_THAN_OR_IS_NULL.equals(key)
                || CK_IS_NULL.equals(key) || CK_IS_NULL_OR_EMPTY.equals(key);
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** Condition-key. */
    protected String _conditionKey;

    /** Operand. */
    protected String _operand;

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    /**
     * Is valid registration?
     * @param provider The provider of query mode. (NotNull)
     * @param cvalue Condition value. (NotNull)
     * @param value Value. (NotNull)
     * @param callerName Caller's real name. (NotNull)
     * @return The determination, true or false.
     */
    public boolean isValidRegistration(final QueryModeProvider provider, final ConditionValue cvalue,
            final Object value, final ColumnRealName callerName) {
        return cvalue.process(new CallbackProcessor<Boolean>() {
            public Boolean process() {
                return doIsValidRegistration(cvalue, value, callerName);
            }

            public QueryModeProvider getProvider() {
                return provider;
            }
        });
    }

    protected abstract boolean doIsValidRegistration(ConditionValue cvalue, Object value, ColumnRealName callerName);

    // ===================================================================================
    //                                                                        Where Clause
    //                                                                        ============
    /**
     * Add where clause.
     * @param provider The provider of query mode. (NotNull)
     * @param conditionList Condition list. (NotNull)
     * @param columnRealName The real name of column. (NotNull)
     * @param cvalue Condition value. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)
     * @param option Condition option. (NullAllowed)
     */
    public void addWhereClause(final QueryModeProvider provider, final List<QueryClause> conditionList,
            final ColumnRealName columnRealName, final ConditionValue cvalue, // basic resources
            final ColumnFunctionCipher cipher, final ConditionOption option) { // optional resources
        cvalue.process(new CallbackProcessor<Void>() {
            public Void process() {
                if (option != null) {
                    doAddWhereClause(conditionList, columnRealName, cvalue, cipher, option);
                } else {
                    doAddWhereClause(conditionList, columnRealName, cvalue, cipher);
                }
                return null;
            }

            public QueryModeProvider getProvider() {
                return provider;
            }
        });
    }

    /**
     * Do add where clause.
     * @param conditionList Condition list. (NotNull)
     * @param columnRealName The real name of column. (NotNull)
     * @param value Condition value. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)
     */
    protected abstract void doAddWhereClause(List<QueryClause> conditionList, ColumnRealName columnRealName,
            ConditionValue value, ColumnFunctionCipher cipher);

    /**
     * Do add where clause.
     * @param conditionList Condition list. (NotNull)
     * @param columnRealName The real name of column. (NotNull)
     * @param value Condition value. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)
     * @param option Condition option. (NotNull)
     */
    protected abstract void doAddWhereClause(List<QueryClause> conditionList, ColumnRealName columnRealName,
            ConditionValue value, ColumnFunctionCipher cipher, ConditionOption option);

    // ===================================================================================
    //                                                                     Condition Value
    //                                                                     ===============
    /**
     * Setup condition value.
     * @param provider The provider of query mode. (NotNull)
     * @param conditionValue Condition value. (NotNull)
     * @param value Value. (NullAllowed)
     * @param location Location. (NullAllowed)
     */
    public void setupConditionValue(QueryModeProvider provider, ConditionValue conditionValue, Object value,
            String location) {
        setupConditionValue(provider, conditionValue, value, location, null);
    }

    /**
     * Setup condition value.
     * @param provider The provider of query mode. (NotNull)
     * @param cvalue Condition value. (NotNull)
     * @param value Value. (NullAllowed)
     * @param location Location. (NullAllowed)
     * @param option Condition option. (NullAllowed)
     */
    public void setupConditionValue(final QueryModeProvider provider, final ConditionValue cvalue, final Object value,
            final String location, final ConditionOption option) {
        cvalue.process(new CallbackProcessor<Void>() {
            public Void process() {
                if (option != null) {
                    doSetupConditionValue(cvalue, value, location, option);
                } else {
                    doSetupConditionValue(cvalue, value, location);
                }
                return null;
            }

            public QueryModeProvider getProvider() {
                return provider;
            }
        });
    }

    /**
     * Do setup condition value.
     * @param conditionValue Condition value. (NotNull)
     * @param value Value. (NotNull)
     * @param location Location. (NotNull)
     */
    protected abstract void doSetupConditionValue(ConditionValue conditionValue, Object value, String location);

    /**
     * Do setup condition value.
     * @param conditionValue Condition value. (NotNull)
     * @param value Value. (NotNull)
     * @param location Location. (NotNull)
     * @param option Condition option. (NotNull)
     */
    protected abstract void doSetupConditionValue(ConditionValue conditionValue, Object value, String location,
            ConditionOption option);

    // ===================================================================================
    //                                                                         Bind Clause
    //                                                                         ===========
    /**
     * Build bind clause.
     * @param columnRealName The real name of column. (NotNull)
     * @param location Location. (NotNull)
     * @return Bind clause. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)
     */
    protected QueryClause buildBindClause(ColumnRealName columnRealName, String location, ColumnFunctionCipher cipher) {
        return new StringQueryClause(doBuildBindMainQuery(columnRealName, location, cipher));
    }

    protected QueryClause doBuildBindClauseOrIsNull(ColumnRealName columnRealName, String location,
            ColumnFunctionCipher cipher) {
        final String mainQuery = doBuildBindMainQuery(columnRealName, location, cipher);
        final String clause = "(" + mainQuery + " or " + columnRealName + " is null)";
        return new StringQueryClause(clause);
    }

    protected String doBuildBindMainQuery(ColumnRealName columnRealName, String location, ColumnFunctionCipher cipher) {
        final String bindExpression = buildBindExpression(location, null, cipher);
        return columnRealName + " " + getOperand() + " " + bindExpression;
    }

    /**
     * Build bind clause. (basically for like-search)
     * @param columnRealName The real name of column. (NotNull)
     * @param operand Operand. (NotNull)
     * @param location Location. (NotNull)
     * @param rearOption Rear option. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)
     * @return Bind clause. (NotNull)
     */
    protected QueryClause buildBindClause(ColumnRealName columnRealName, String operand, String location,
            String rearOption, ColumnFunctionCipher cipher) {
        final String bindExpression = buildBindExpression(location, null, cipher);
        final String clause = columnRealName + " " + operand + " " + bindExpression + rearOption;
        return new StringQueryClause(clause);
    }

    /**
     * Build bind clause.
     * @param columnRealName The real name of column. (NotNull)
     * @param location Location. (NotNull)
     * @param dummyValue Dummy value. (NotNull)
     * @param cipher The cipher of column by function. (NullAllowed)  
     * @return Bind clause. (NotNull)
     */
    protected QueryClause buildBindClause(ColumnRealName columnRealName, String location, String dummyValue,
            ColumnFunctionCipher cipher) {
        final String bindExpression = buildBindExpression(location, dummyValue, cipher);
        final String clause = columnRealName + " " + getOperand() + " " + bindExpression;
        return new StringQueryClause(clause);
    }

    /**
     * Build clause without value.
     * @param columnRealName The real name of column. (NotNull)
     * @return Clause without value. (NotNull)
     */
    protected QueryClause buildClauseWithoutValue(ColumnRealName columnRealName) {
        final String clause = columnRealName + " " + getOperand();
        return new StringQueryClause(clause);
    }

    protected String buildBindExpression(String location, String dummyValue, ColumnFunctionCipher cipher) {
        final String bindExpression = "/*pmb." + location + "*/" + dummyValue;
        return cipher != null ? cipher.encrypt(bindExpression) : bindExpression;
    }

    /**
     * Get wild-card.
     * @return Wild-card.
     */
    protected String getWildCard() {
        return "%";
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected void noticeRegistered(ColumnRealName callerName, Object value) {
        if (_log.isDebugEnabled()) {
            final String target = callerName + "." + _conditionKey;
            _log.debug("*Found the duplicate query: target=" + target + " value=" + value);
        }
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * @return View-string of condition key information.
     */
    @Override
    public String toString() {
        return "ConditionKey:{" + getConditionKey() + " " + getOperand() + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * Get condition-key.
     * @return Condition-key.
     */
    public String getConditionKey() {
        return _conditionKey;
    }

    /**
     * Get operand.
     * @return Operand.
     */
    public String getOperand() {
        return _operand;
    }
}
