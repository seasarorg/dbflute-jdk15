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

import java.util.ArrayList;
import java.util.List;

import org.seasar.dbflute.cbean.chelper.HpSpecifiedColumn;
import org.seasar.dbflute.cbean.cipher.ColumnFunctionCipher;
import org.seasar.dbflute.cbean.coption.ConditionOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.sqlclause.query.QueryClause;
import org.seasar.dbflute.cbean.sqlclause.query.QueryClauseArranger;
import org.seasar.dbflute.cbean.sqlclause.query.StringQueryClause;
import org.seasar.dbflute.dbmeta.name.ColumnRealName;
import org.seasar.dbflute.dbmeta.name.ColumnSqlName;
import org.seasar.dbflute.dbway.ExtensionOperand;
import org.seasar.dbflute.dbway.StringConnector;

/**
 * The condition-key of likeSearch.
 * @author jflute
 */
public class ConditionKeyLikeSearch extends ConditionKey {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor.
     */
    protected ConditionKeyLikeSearch() {
        _conditionKey = defineConditionKey();
        _operand = defineOperand();
    }

    protected String defineConditionKey() {
        return "likeSearch";
    }

    protected String defineOperand() {
        return "like";
    }

    // ===================================================================================
    //                                                                      Implementation
    //                                                                      ==============
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doIsValidRegistration(ConditionValue cvalue, Object value, ColumnRealName callerName) {
        return value != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAddWhereClause(List<QueryClause> conditionList, ColumnRealName columnRealName,
            ConditionValue value, ColumnFunctionCipher cipher) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAddWhereClause(List<QueryClause> conditionList, ColumnRealName columnRealName,
            ConditionValue value, ColumnFunctionCipher cipher, ConditionOption option) {
        assertWhereClauseArgument(columnRealName, value, option);
        final String location = getLocation(value);
        final LikeSearchOption myOption = (LikeSearchOption) option;
        final String rearOption = myOption.getRearOption();
        final String realOperand = getRealOperand(myOption);
        final ColumnRealName realRealName;
        if (myOption.hasCompoundColumn()) {
            if (!myOption.hasStringConnector()) { // basically no way
                String msg = "The option should have string connector when compound column is specified: " + myOption;
                throw new IllegalStateException(msg);
            }
            final List<HpSpecifiedColumn> compoundColumnList = myOption.getCompoundColumnList();
            final List<ColumnRealName> realNameList = new ArrayList<ColumnRealName>();
            realNameList.add(columnRealName);
            for (HpSpecifiedColumn specifiedColumn : compoundColumnList) {
                realNameList.add(specifiedColumn.toColumnRealName());
            }
            final StringConnector stringConnector = myOption.getStringConnector();
            final String connected = stringConnector.connect(realNameList.toArray());
            realRealName = ColumnRealName.create(null, new ColumnSqlName(connected));
        } else {
            realRealName = columnRealName;
        }
        final QueryClauseArranger arranger = myOption.getWhereClauseArranger();
        final QueryClause clause;
        if (arranger != null) {
            final String bindExpression = buildBindExpression(location, null, cipher);
            final String arranged = arranger.arrange(realRealName, realOperand, bindExpression, rearOption);
            clause = new StringQueryClause(arranged);
        } else {
            clause = buildBindClause(realRealName, realOperand, location, rearOption, cipher);
        }
        conditionList.add(clause);
    }

    protected void assertWhereClauseArgument(ColumnRealName columnRealName, ConditionValue value, ConditionOption option) {
        if (option == null) {
            String msg = "The argument 'option' should not be null:";
            msg = msg + " columnName=" + columnRealName + " value=" + value;
            throw new IllegalArgumentException(msg);
        }
        if (!(option instanceof LikeSearchOption)) {
            String msg = "The argument 'option' should be LikeSearchOption:";
            msg = msg + " columnName=" + columnRealName + " value=" + value;
            msg = msg + " option=" + option;
            throw new IllegalArgumentException(msg);
        }
    }

    protected String getLocation(ConditionValue value) {
        return value.getLikeSearchLatestLocation();
    }

    protected String getRealOperand(LikeSearchOption option) {
        final ExtensionOperand extOperand = option.getExtensionOperand();
        final String operand = extOperand != null ? extOperand.operand() : null;
        return operand != null ? operand : getOperand();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSetupConditionValue(ConditionValue conditionValue, Object value, String location) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSetupConditionValue(ConditionValue conditionValue, Object value, String location,
            ConditionOption option) {
        conditionValue.setupLikeSearch((String) value, (LikeSearchOption) option, location);
    }
}
