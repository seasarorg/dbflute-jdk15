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
package org.seasar.dbflute.cbean.sqlclause;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.seasar.dbflute.cbean.ManualOrderBean;
import org.seasar.dbflute.cbean.chelper.HpCBPurpose;
import org.seasar.dbflute.cbean.chelper.HpDerivingSubQueryInfo;
import org.seasar.dbflute.cbean.chelper.HpInvalidQueryInfo;
import org.seasar.dbflute.cbean.chelper.HpSpecifiedColumn;
import org.seasar.dbflute.cbean.cipher.ColumnFunctionCipher;
import org.seasar.dbflute.cbean.cipher.GearedCipherManager;
import org.seasar.dbflute.cbean.ckey.ConditionKey;
import org.seasar.dbflute.cbean.coption.ConditionOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.cbean.coption.ScalarSelectOption;
import org.seasar.dbflute.cbean.cvalue.ConditionValue;
import org.seasar.dbflute.cbean.cvalue.ConditionValue.QueryModeProvider;
import org.seasar.dbflute.cbean.sqlclause.clause.ClauseLazyReflector;
import org.seasar.dbflute.cbean.sqlclause.join.FixedConditionResolver;
import org.seasar.dbflute.cbean.sqlclause.join.InnerJoinLazyReflector;
import org.seasar.dbflute.cbean.sqlclause.join.InnerJoinLazyReflectorBase;
import org.seasar.dbflute.cbean.sqlclause.join.LeftOuterJoinInfo;
import org.seasar.dbflute.cbean.sqlclause.orderby.OrderByClause;
import org.seasar.dbflute.cbean.sqlclause.orderby.OrderByElement;
import org.seasar.dbflute.cbean.sqlclause.query.OrScopeQueryAndPartQueryClause;
import org.seasar.dbflute.cbean.sqlclause.query.OrScopeQueryInfo;
import org.seasar.dbflute.cbean.sqlclause.query.OrScopeQueryReflector;
import org.seasar.dbflute.cbean.sqlclause.query.QueryClause;
import org.seasar.dbflute.cbean.sqlclause.query.QueryClauseFilter;
import org.seasar.dbflute.cbean.sqlclause.query.QueryUsedAliasInfo;
import org.seasar.dbflute.cbean.sqlclause.query.StringQueryClause;
import org.seasar.dbflute.cbean.sqlclause.select.SelectedRelationColumn;
import org.seasar.dbflute.cbean.sqlclause.subquery.SubQueryIndentProcessor;
import org.seasar.dbflute.cbean.sqlclause.union.UnionClauseProvider;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.info.ForeignInfo;
import org.seasar.dbflute.dbmeta.name.ColumnRealName;
import org.seasar.dbflute.dbmeta.name.ColumnSqlName;
import org.seasar.dbflute.dbmeta.name.TableSqlName;
import org.seasar.dbflute.dbway.DBWay;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;
import org.seasar.dbflute.exception.factory.ExceptionMessageBuilder;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.dbflute.resource.DBFluteSystem;
import org.seasar.dbflute.util.DfAssertUtil;
import org.seasar.dbflute.util.Srl;

/**
 * The abstract class of SQL clause.
 * @author jflute
 */
public abstract class AbstractSqlClause implements SqlClause, Serializable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    protected static final SelectClauseType DEFAULT_SELECT_CLAUSE_TYPE = SelectClauseType.COLUMNS;
    protected static final String SELECT_HINT = "/*$pmb.selectHint*/";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                                 Basic
    //                                                 -----
    /** The DB name of table. */
    protected final String _tableDbName;

    /** The DB meta of table. (basically NotNull: null only when treated as dummy) */
    protected DBMeta _dbmeta;

    /** The DB meta of target table. (basically NotNull: null only when treated as dummy) */
    protected DBMetaProvider _dbmetaProvider;

    /** The cache map of DB meta for basically related tables. */
    protected Map<String, DBMeta> _cachedDBMetaMap;

    /** The hierarchy level of sub-query. (NotMinus: if zero, not for sub-query) */
    protected int _subQueryLevel;

    // -----------------------------------------------------
    //                                       Clause Resource
    //                                       ---------------
    // /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // The resources that are not frequently used to are lazy-loaded for performance.
    // - - - - - - - - - -/
    /** The basic map of selected relation. map:{foreignRelationPath : foreignPropertyName} (NullAllowed: This is lazy-loaded) */
    protected Map<String, String> _selectedRelationBasicMap;

    /** The column map of selected relation. map:{foreignTableAliasName : map:{columnName : selectedRelationColumn}} (NullAllowed: This is lazy-loaded) */
    protected Map<String, Map<String, SelectedRelationColumn>> _selectedRelationColumnMap;

    /** Specified select column map. map:{ tableAliasName = map:{ columnName : specifiedInfo } } (NullAllowed: This is lazy-loaded) */
    protected Map<String, Map<String, HpSpecifiedColumn>> _specifiedSelectColumnMap; // [DBFlute-0.7.4]

    /** Specified select column map for backup. map:{ tableAliasName = map:{ columnName : specifiedInfo } } (NullAllowed: This is lazy-loaded) */
    protected Map<String, Map<String, HpSpecifiedColumn>> _backupSpecifiedSelectColumnMap; // [DBFlute-0.9.5.3]

    /** Specified derive sub-query map. A null key is acceptable. (NullAllowed: lazy-load) */
    protected Map<String, HpDerivingSubQueryInfo> _specifiedDerivingSubQueryMap; // [DBFlute-0.7.4]

    /** The map of real column and alias of select clause. map:{realColumnName : aliasName} */
    protected Map<String, String> _selectClauseRealColumnAliasMap;

    /** The type of select clause. (NotNull) */
    protected SelectClauseType _selectClauseType = DEFAULT_SELECT_CLAUSE_TYPE;

    /** The previous type of select clause. (NullAllowed: The default is null) */
    protected SelectClauseType _previousSelectClauseType;

    /** The map of select index. {key:selectColumnKeyName, value:selectIndex} (NullAllowed: lazy-load) */
    protected Map<String, Integer> _selectIndexMap;

    /** The reverse map of select index. {key:indexedOnQueryName, value:selectColumnKeyName} (NullAllowed: lazy-load) */
    protected Map<String, String> _selectIndexReverseMap;

    /** Is use select index? Default value is true. */
    protected boolean _useSelectIndex = true;

    /** The map of left-outer-join info. map:{ foreignAliasName : leftOuterJoinInfo } */
    protected Map<String, LeftOuterJoinInfo> _outerJoinMap;

    /** Does it allow to auto-detect joins that can be structural-possible inner-join? */
    protected boolean _structuralPossibleInnerJoinAllowed;

    /** Does it allow to auto-detect joins that can be where-used inner-join? */
    protected boolean _whereUsedInnerJoinAllowed;

    /** The list of lazy reflector for auto-detected inner-join. */
    protected List<InnerJoinLazyReflector> _innerJoinLazyReflector;

    /** The list of where clause. */
    protected List<QueryClause> _whereList;

    /** The list of in-line where clause for base table. */
    protected List<QueryClause> _baseTableInlineWhereList;

    /** The clause of order-by. (NotNull) */
    protected OrderByClause _orderByClause;

    /** The list of union clause. (NullAllowed: This is lazy-loaded) */
    protected List<UnionQueryInfo> _unionQueryInfoList;

    /** Is order-by effective? Default value is false. True when registered. */
    protected boolean _orderByEffective;

    // -----------------------------------------------------
    //                                        Fetch Property
    //                                        --------------
    /** Fetch start index. (for fetchXxx()) */
    protected int _fetchStartIndex = 0;

    /** Fetch size. (for fetchXxx()) */
    protected int _fetchSize = 0;

    /** Fetch page number. (for fetchXxx()) This value should be plus. */
    protected int _fetchPageNumber = 1;

    /** Is fetch-narrowing effective? Default value is false but true when registered. */
    protected boolean _fetchScopeEffective;

    // -----------------------------------------------------
    //                                          OrScopeQuery
    //                                          ------------
    /** Is or-scope query effective? */
    protected boolean _orScopeQueryEffective;

    /** The current temporary information of or-scope query?*/
    protected OrScopeQueryInfo _currentTmpOrScopeQueryInfo;

    /** Is or-scope query in and-part?*/
    protected boolean _orScopeQueryAndPartEffective;

    /** The identity for and-part of or-scope query */
    protected int _orScopeQueryAndPartIdentity;

    // -----------------------------------------------------
    //                                       SubQuery Indent
    //                                       ---------------
    /** The processor for sub-query indent. */
    protected SubQueryIndentProcessor _subQueryIndentProcessor;

    // -----------------------------------------------------
    //                                    Invalid Query Info
    //                                    ------------------
    /** Does it accept an empty string for query? */
    protected boolean _emptyStringQueryAllowed;

    /** Does it check an invalid query? */
    protected boolean _invalidQueryChecked;

    /** The list of invalid query info. */
    protected List<HpInvalidQueryInfo> _invalidQueryList;

    // -----------------------------------------------------
    //                               WhereClauseSimpleFilter
    //                               -----------------------
    /** The filter for where clause. */
    protected transient List<QueryClauseFilter> _whereClauseSimpleFilterList; // transient because of non-serializable

    // -----------------------------------------------------
    //                                    ColumnQuery Object
    //                                    ------------------
    /** The map for column query objects. (only for saving) (NullAllowed) */
    protected Map<String, Object> _columyQueryObjectMap;

    // -----------------------------------------------------
    //                                 ManualOrder Parameter
    //                                 ---------------------
    /** The map for ManualOrder parameters. (only for saving) (NullAllowed) */
    protected Map<String, Object> _manualOrderParameterMap;

    // -----------------------------------------------------
    //                                        Free Parameter
    //                                        --------------
    /** The map for free parameters. (only for saving) (NullAllowed) */
    protected Map<String, Object> _freeParameterMap;

    // -----------------------------------------------------
    //                                         Geared Cipher
    //                                         -------------
    /** The manager of geared cipher. (also for saving) (NullAllowed) */
    protected GearedCipherManager _gearedCipherManager;

    /** Does it suppress description for select columns? */
    protected boolean _suppressSelectColumnDecryption;

    // -----------------------------------------------------
    //                                         Scalar Select
    //                                         -------------
    /** The option for ScalarSelect. */
    protected ScalarSelectOption _scalarSelectOption;

    // -----------------------------------------------------
    //                                         Paging Select
    //                                         -------------
    /** Is the clause for paging select? */
    protected boolean _pagingAdjustment;

    /** Is the joins of count least? */
    protected boolean _pagingCountLeastJoin;

    /** Is the count executed later? */
    protected boolean _pagingCountLater;

    // -----------------------------------------------------
    //                                          Purpose Type
    //                                          ------------
    /** The purpose of condition-bean for check at condition-query. (NotNull) */
    protected HpCBPurpose _purpose = HpCBPurpose.NORMAL_USE; // as default

    // -----------------------------------------------------
    //                                        Lazy Reflector
    //                                        --------------
    protected List<ClauseLazyReflector> _clauseLazyReflectorList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor.
     * @param tableDbName The DB name of table. (NotNull)
     **/
    public AbstractSqlClause(String tableDbName) {
        if (tableDbName == null) {
            String msg = "The argument 'tableDbName' should not be null.";
            throw new IllegalArgumentException(msg);
        }
        _tableDbName = tableDbName;
    }

    /**
     * Set the provider of DB meta. <br />
     * If you want to use all functions, this method is required.
     * @param dbmetaProvider The provider of DB meta. (NotNull)
     * @return this. (NotNull)
     */
    public AbstractSqlClause dbmetaProvider(DBMetaProvider dbmetaProvider) {
        if (dbmetaProvider == null) {
            String msg = "The argument 'dbmetaProvider' should not be null:";
            msg = msg + " tableDbName=" + _tableDbName;
            throw new IllegalArgumentException(msg);
        }
        _dbmetaProvider = dbmetaProvider;
        _dbmeta = findDBMeta(_tableDbName);
        return this;
    }

    /**
     * Set the manager of geared cipher.
     * @param manager The manager of geared cipher. (NullAllowed)
     * @return this. (NotNull)
     */
    public AbstractSqlClause cipherManager(GearedCipherManager manager) {
        _gearedCipherManager = manager;
        return this;
    }

    // ===================================================================================
    //                                                                      SubQuery Level
    //                                                                      ==============
    /**
     * {@inheritDoc}
     */
    public int getSubQueryLevel() {
        return _subQueryLevel;
    }

    /**
     * {@inheritDoc}
     */
    public void setupForSubQuery(int subQueryLevel) {
        _subQueryLevel = subQueryLevel;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isForSubQuery() {
        return _subQueryLevel > 0;
    }

    // ===================================================================================
    //                                                                         Main Clause
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Complete Clause
    //                                       ---------------
    public String getClause() {
        reflectClauseLazilyIfExists();
        final StringBuilder sb = new StringBuilder(512);
        final String selectClause = getSelectClause();
        sb.append(selectClause);
        buildClauseWithoutMainSelect(sb, selectClause);
        String sql = sb.toString();
        sql = filterEnclosingClause(sql);
        sql = processSubQueryIndent(sql);
        return sql;
    }

    protected void buildClauseWithoutMainSelect(StringBuilder sb, String selectClause) {
        buildFromClause(sb);
        sb.append(getFromHint());
        buildWhereClause(sb);
        sb.append(deleteUnionWhereTemplateMark(prepareUnionClause(selectClause)));
        if (!needsUnionNormalSelectEnclosing()) {
            sb.append(prepareClauseOrderBy());
            sb.append(prepareClauseSqlSuffix());
        }
    }

    protected String deleteUnionWhereTemplateMark(String unionClause) {
        if (unionClause != null && unionClause.trim().length() > 0) {
            unionClause = replace(unionClause, getUnionWhereClauseMark(), "");
            unionClause = replace(unionClause, getUnionWhereFirstConditionMark(), "");
        }
        return unionClause;
    }

    // -----------------------------------------------------
    //                                       Fragment Clause
    //                                       ---------------
    public String getClauseFromWhereWithUnionTemplate() {
        reflectClauseLazilyIfExists();
        return buildClauseFromWhereAsTemplate(false);
    }

    public String getClauseFromWhereWithWhereUnionTemplate() {
        reflectClauseLazilyIfExists();
        return buildClauseFromWhereAsTemplate(true);
    }

    protected String buildClauseFromWhereAsTemplate(boolean template) {
        final StringBuilder sb = new StringBuilder(256);
        buildFromClause(sb);
        sb.append(getFromHint());
        buildWhereClause(sb, template);
        sb.append(prepareUnionClause(getUnionSelectClauseMark()));
        return sb.toString();
    }

    protected String prepareUnionClause(String selectClause) {
        if (!hasUnionQuery()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (UnionQueryInfo unionQueryInfo : _unionQueryInfoList) {
            final UnionClauseProvider unionClauseProvider = unionQueryInfo.getUnionClauseProvider();
            final String unionQueryClause = unionClauseProvider.provide();
            final boolean unionAll = unionQueryInfo.isUnionAll();
            sb.append(ln()).append(unionAll ? " union all " : " union ").append(ln());
            sb.append(selectClause).append(" ").append(unionQueryClause);
        }
        return sb.toString();
    }

    protected String prepareClauseOrderBy() {
        if (!_orderByEffective || !hasOrderByClause()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(getOrderByClause());
        return sb.toString();
    }

    protected String prepareClauseSqlSuffix() {
        final String sqlSuffix = getSqlSuffix();
        if (sqlSuffix == null || sqlSuffix.trim().length() == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(sqlSuffix);
        return sb.toString();
    }

    protected String filterEnclosingClause(String sql) {
        sql = filterUnionNormalSelectEnclosing(sql);
        sql = filterUnionCountOrScalarEnclosing(sql);
        return sql;
    }

    protected String filterUnionNormalSelectEnclosing(String sql) {
        if (!needsUnionNormalSelectEnclosing()) {
            return sql;
        }
        final String selectClause = "select" + SELECT_HINT + " *";
        final String inlineViewAlias = getUnionQueryInlineViewAlias();
        final String beginMark = resolveSubQueryBeginMark(inlineViewAlias) + ln();
        final String endMark = resolveSubQueryEndMark(inlineViewAlias);
        final StringBuilder sb = new StringBuilder();
        sb.append(selectClause).append(ln());
        sb.append("  from (").append(beginMark).append(sql).append(ln()).append("       ) ");
        sb.append(inlineViewAlias).append(endMark);
        sb.append(prepareClauseOrderBy()).append(prepareClauseSqlSuffix());
        return sb.toString();
    }

    protected String filterUnionCountOrScalarEnclosing(String sql) {
        if (!needsUnionCountOrScalarEnclosing()) {
            return sql;
        }
        final String inlineViewAlias = getUnionQueryInlineViewAlias();
        final String selectClause = buildSelectClauseScalar(inlineViewAlias);
        final String beginMark = resolveSubQueryBeginMark(inlineViewAlias) + ln();
        final String endMark = resolveSubQueryEndMark(inlineViewAlias);
        final StringBuilder sb = new StringBuilder();
        sb.append(selectClause).append(ln());
        sb.append("  from (").append(beginMark).append(sql).append(ln()).append("       ) ");
        sb.append(inlineViewAlias).append(endMark);
        return sb.toString();
    }

    protected boolean needsUnionNormalSelectEnclosing() {
        if (!isUnionNormalSelectEnclosingRequired()) {
            return false;
        }
        return hasUnionQuery() && !isSelectClauseTypeScalar();
    }

    protected boolean isUnionNormalSelectEnclosingRequired() { // for extension
        return false; // false as default
    }

    protected boolean needsUnionCountOrScalarEnclosing() {
        return hasUnionQuery() && isSelectClauseTypeScalar();
    }

    // ===================================================================================
    //                                                                        Clause Parts
    //                                                                        ============
    // -----------------------------------------------------
    //                                         Select Clause
    //                                         -------------
    public String getSelectClause() {
        reflectClauseLazilyIfExists();
        if (isSelectClauseNonUnionScalar()) {
            return buildSelectClauseScalar(getBasePointAliasName());
        }
        // if it's a scalar-select, it always has union-query since here
        final StringBuilder sb = new StringBuilder();

        if (_useSelectIndex) {
            _selectIndexMap = createSelectIndexMap(); // should be initialized before process
        }

        final Integer selectIndex = processSelectClauseLocal(sb);
        processSelectClauseRelation(sb, selectIndex);
        processSelectClauseDerivedReferrer(sb);

        return sb.toString();
    }

    protected Integer processSelectClauseLocal(StringBuilder sb) {
        final String basePointAliasName = getBasePointAliasName();
        final DBMeta dbmeta = getDBMeta();
        final Map<String, HpSpecifiedColumn> localSpecifiedMap;
        if (_specifiedSelectColumnMap != null) {
            localSpecifiedMap = _specifiedSelectColumnMap.get(basePointAliasName);
        } else {
            localSpecifiedMap = null;
        }
        final List<ColumnInfo> columnInfoList;
        final boolean validSpecifiedLocal;
        if (isSelectClauseTypeUniqueScalar()) {
            // it always has union-query because it's handled before this process
            if (dbmeta.hasPrimaryKey()) {
                columnInfoList = new ArrayList<ColumnInfo>();
                columnInfoList.addAll(dbmeta.getPrimaryUniqueInfo().getUniqueColumnList());
                if (isSelectClauseTypeSpecifiedScalar()) {
                    final ColumnInfo specifiedColumn = getSpecifiedColumnInfoAsOne();
                    if (specifiedColumn != null) {
                        columnInfoList.add(specifiedColumn);
                    }
                    // derivingSubQuery is handled after this process
                }
            } else {
                // all columns are target if no-PK and unique-scalar and union-query
                columnInfoList = dbmeta.getColumnInfoList();
            }
            validSpecifiedLocal = false; // because specified columns are fixed here
        } else {
            columnInfoList = dbmeta.getColumnInfoList();
            validSpecifiedLocal = localSpecifiedMap != null && !localSpecifiedMap.isEmpty();
        }

        Integer selectIndex = 0; // because 1 origin in JDBC
        boolean needsDelimiter = false;
        for (ColumnInfo columnInfo : columnInfoList) {
            final String columnDbName = columnInfo.getColumnDbName();
            final ColumnSqlName columnSqlName = columnInfo.getColumnSqlName();

            if (validSpecifiedLocal && !localSpecifiedMap.containsKey(columnDbName)) {
                // a case for scalar-select has been already resolved here
                continue;
            }

            if (needsDelimiter) {
                sb.append(", ");
            } else {
                sb.append("select");
                appendSelectHint(sb);
                sb.append(" ");
                needsDelimiter = true;
            }
            final String realColumnName = basePointAliasName + "." + columnSqlName;
            final String onQueryName;
            ++selectIndex;
            if (_useSelectIndex) {
                onQueryName = buildSelectIndexAlias(columnSqlName, null, selectIndex);
                registerSelectIndex(columnDbName, onQueryName, selectIndex);
            } else {
                onQueryName = columnSqlName.toString();
            }
            sb.append(decryptSelectColumnIfNeeds(columnInfo, realColumnName)).append(" as ").append(onQueryName);
            getSelectClauseRealColumnAliasMap().put(realColumnName, onQueryName);

            if (validSpecifiedLocal && localSpecifiedMap.containsKey(columnDbName)) {
                final HpSpecifiedColumn specifiedColumn = localSpecifiedMap.get(columnDbName);
                specifiedColumn.setOnQueryName(onQueryName); // basically for queryInsert()
            }
        }
        return selectIndex;
    }

    protected Integer processSelectClauseRelation(StringBuilder sb, Integer selectIndex) {
        for (Entry<String, Map<String, SelectedRelationColumn>> entry : getSelectedRelationColumnMap().entrySet()) {
            final String tableAliasName = entry.getKey();
            final Map<String, SelectedRelationColumn> map = entry.getValue();
            final Collection<SelectedRelationColumn> selectColumnInfoList = map.values();
            Map<String, HpSpecifiedColumn> foreginSpecifiedMap = null;
            if (_specifiedSelectColumnMap != null) {
                foreginSpecifiedMap = _specifiedSelectColumnMap.get(tableAliasName);
            }
            final boolean validSpecifiedForeign = foreginSpecifiedMap != null && !foreginSpecifiedMap.isEmpty();
            boolean finishedForeignIndent = false;
            for (SelectedRelationColumn selectColumnInfo : selectColumnInfoList) {
                final ColumnInfo columnInfo = selectColumnInfo.getColumnInfo();
                final String columnDbName = columnInfo.getColumnDbName();
                if (validSpecifiedForeign && !foreginSpecifiedMap.containsKey(columnDbName)) {
                    continue;
                }

                final String realColumnName = selectColumnInfo.buildRealColumnSqlName();
                final String columnAliasName = selectColumnInfo.getColumnAliasName();
                final String onQueryName;
                ++selectIndex;
                if (_useSelectIndex) {
                    onQueryName = buildSelectIndexAlias(columnInfo.getColumnSqlName(), columnAliasName, selectIndex);
                    registerSelectIndex(columnAliasName, onQueryName, selectIndex);
                } else {
                    onQueryName = columnAliasName;
                }
                if (!finishedForeignIndent) {
                    sb.append(ln()).append("     ");
                    finishedForeignIndent = true;
                }
                sb.append(", ");
                sb.append(decryptSelectColumnIfNeeds(columnInfo, realColumnName)).append(" as ").append(onQueryName);
                getSelectClauseRealColumnAliasMap().put(realColumnName, onQueryName);

                if (validSpecifiedForeign && foreginSpecifiedMap.containsKey(columnDbName)) {
                    final HpSpecifiedColumn specifiedColumn = foreginSpecifiedMap.get(columnDbName);
                    specifiedColumn.setOnQueryName(onQueryName); // basically for queryInsert()
                }
            }
        }
        return selectIndex;
    }

    protected void processSelectClauseDerivedReferrer(StringBuilder sb) {
        if (_specifiedDerivingSubQueryMap == null || _specifiedDerivingSubQueryMap.isEmpty()) {
            return;
        }
        for (Entry<String, HpDerivingSubQueryInfo> entry : _specifiedDerivingSubQueryMap.entrySet()) {
            final String subQueryAlias = entry.getKey();
            final String derivingSubQuery = entry.getValue().getDerivingSubQuery();
            sb.append(ln()).append("     ");
            sb.append(", ").append(derivingSubQuery);

            // for SpecifiedDerivedOrderBy
            if (subQueryAlias != null) {
                getSelectClauseRealColumnAliasMap().put(subQueryAlias, subQueryAlias);
            }
        }
    }

    protected Map<String, String> getSelectClauseRealColumnAliasMap() {
        if (_selectClauseRealColumnAliasMap == null) {
            _selectClauseRealColumnAliasMap = new HashMap<String, String>(); // order no needed
        }
        return _selectClauseRealColumnAliasMap;
    }

    // -----------------------------------------------------
    //                                       Count or Scalar
    //                                       ---------------
    protected boolean isSelectClauseTypeCount() {
        return _selectClauseType.isCount();
    }

    protected boolean isSelectClauseTypeScalar() {
        return _selectClauseType.isScalar();
    }

    protected boolean isSelectClauseTypeUniqueScalar() {
        return _selectClauseType.isUniqueScalar();
    }

    protected boolean isSelectClauseTypeSpecifiedScalar() {
        return _selectClauseType.isSpecifiedScalar();
    }

    protected boolean isSelectClauseTypeNonUnionCount() {
        return !hasUnionQuery() && isSelectClauseTypeCount();
    }

    protected boolean isSelectClauseNonUnionScalar() {
        return !hasUnionQuery() && isSelectClauseTypeScalar();
    }

    protected boolean isSelectClauseNonUnionSelect() {
        return !hasUnionQuery() && !isSelectClauseTypeScalar();
    }

    protected String buildSelectClauseScalar(String aliasName) {
        if (isSelectClauseTypeCount()) {
            return buildSelectClauseCount();
        } else if (_selectClauseType.equals(SelectClauseType.COUNT_DISTINCT)) {
            return buildSelectClauseCountDistinct(aliasName);
        } else if (_selectClauseType.equals(SelectClauseType.MAX)) {
            return buildSelectClauseMax(aliasName);
        } else if (_selectClauseType.equals(SelectClauseType.MIN)) {
            return buildSelectClauseMin(aliasName);
        } else if (_selectClauseType.equals(SelectClauseType.SUM)) {
            return buildSelectClauseSum(aliasName);
        } else if (_selectClauseType.equals(SelectClauseType.AVG)) {
            return buildSelectClauseAvg(aliasName);
        }
        String msg = "The type of select clause is not for scalar:";
        msg = msg + " type=" + _selectClauseType;
        throw new IllegalStateException(msg);
    }

    protected String buildSelectClauseCount() {
        return "select count(*)";
    }

    protected String buildSelectClauseCountDistinct(String aliasName) {
        return buildSelectClauseSpecifiedScalar(aliasName, "count(distinct");
    }

    protected String buildSelectClauseMax(String aliasName) {
        return buildSelectClauseSpecifiedScalar(aliasName, "max");
    }

    protected String buildSelectClauseMin(String aliasName) {
        return buildSelectClauseSpecifiedScalar(aliasName, "min");
    }

    protected String buildSelectClauseSum(String aliasName) {
        return buildSelectClauseSpecifiedScalar(aliasName, "sum");
    }

    protected String buildSelectClauseAvg(String aliasName) {
        return buildSelectClauseSpecifiedScalar(aliasName, "avg");
    }

    protected String buildSelectClauseSpecifiedScalar(String aliasName, String function) {
        final String columnAlias = getScalarSelectColumnAlias();
        final ColumnSqlName columnSqlName = getSpecifiedColumnSqlNameAsOne();
        final ColumnInfo columnInfo = getSpecifiedColumnInfoAsOne();
        if (columnSqlName != null) {
            final String valueExp = aliasName + "." + columnSqlName;
            final String functionExp = doBuildFunctionExp(function, decryptSelectColumnIfNeeds(columnInfo, valueExp));
            return "select " + functionExp + " as " + columnAlias;
        }
        final String subQuery = getSpecifiedDerivingSubQueryAsOne();
        if (subQuery != null) {
            if (hasUnionQuery()) {
                final String valueExp = aliasName + "." + columnAlias;
                return "select " + doBuildFunctionExp(function, decryptSelectColumnIfNeeds(columnInfo, valueExp));
            } else {
                // adjusts alias definition target (move to function's scope)
                final String aliasDef = " as " + columnAlias;
                final StringBuilder sb = new StringBuilder();
                final String pureSubQuery = Srl.substringLastFront(subQuery, aliasDef);
                final String aliasDefRear = Srl.substringLastRear(subQuery, aliasDef); // just in case
                final String functionExp = doBuildFunctionExp(function, pureSubQuery);
                sb.append("select ").append(functionExp).append(aliasDef).append(aliasDefRear);
                return sb.toString();
            }
        }
        String msg = "Not found specifed column for scalar: function=" + function;
        throw new IllegalStateException(msg); // basically no way (checked before)
    }

    protected String doBuildFunctionExp(String function, String columnExp) {
        final String frontConnector = function.contains("(") ? " " : "("; // e.g. "count(distinct"
        final String functionExp = function + frontConnector + columnExp + ")";
        return _scalarSelectOption != null ? _scalarSelectOption.filterFunction(functionExp) : functionExp;
    }

    // -----------------------------------------------------
    //                                          Select Index
    //                                          ------------
    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> getSelectIndexMap() {
        return _selectIndexMap; // NullAllowed
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getSelectIndexReverseMap() {
        return _selectIndexReverseMap; // NullAllowed
    }

    protected void registerSelectIndex(String keyName, String onQueryName, Integer selectIndex) {
        if (_selectIndexMap == null) { // lazy load
            _selectIndexMap = createSelectIndexMap();
        }
        _selectIndexMap.put(keyName, selectIndex);
        if (_selectIndexReverseMap == null) { // lazy load
            _selectIndexReverseMap = createSelectIndexMap();
        }
        _selectIndexReverseMap.put(onQueryName, keyName);
    }

    protected <VALUE> Map<String, VALUE> createSelectIndexMap() {
        // flexible for resolving non-compilable connectors and reservation words
        // (and it does not need to be ordered)
        return StringKeyMap.createAsFlexible();
    }

    protected String buildSelectIndexAlias(ColumnSqlName sqlName, String aliasName, Integer selectIndex) {
        if (sqlName.hasIrregularChar()) {
            return "c" + selectIndex; // use index only for safety
        }
        // regular case only here
        final String baseName;
        if (aliasName != null) { // relation column
            baseName = aliasName;
        } else { // local column
            baseName = sqlName.toString();
        }
        final int aliasNameLimitSize = getAliasNameLimitSize();
        if (baseName.length() > aliasNameLimitSize) {
            final int aliasNameBaseSize = aliasNameLimitSize - 10;
            return Srl.substring(baseName, 0, aliasNameBaseSize) + "_c" + selectIndex;
        } else {
            return baseName;
        }
    }

    protected int getAliasNameLimitSize() { // used only when regular case
        return 30; // default is the least limit size in DBMSs (Oracle)
    }

    public void disableSelectIndex() {
        _useSelectIndex = false;
    }

    // -----------------------------------------------------
    //                                           Select Hint
    //                                           -----------
    public String getSelectHint() {
        return createSelectHint();
    }

    protected void appendSelectHint(StringBuilder sb) { // for extension
        sb.append(SELECT_HINT);
    }

    // -----------------------------------------------------
    //                                           From Clause
    //                                           -----------
    public String getFromClause() {
        reflectClauseLazilyIfExists();
        final StringBuilder sb = new StringBuilder();
        buildFromClause(sb);
        return sb.toString();
    }

    protected void buildFromClause(StringBuilder sb) {
        sb.append(ln()).append("  ");
        sb.append("from ");
        int tablePos = 7; // basically for in-line view indent
        if (isJoinInParentheses()) {
            for (int i = 0; i < getOuterJoinMap().size(); i++) {
                sb.append("(");
                ++tablePos;
            }
        }
        final TableSqlName tableSqlName = getDBMeta().getTableSqlName();
        final String basePointAliasName = getBasePointAliasName();
        if (hasBaseTableInlineWhereClause()) {
            final List<QueryClause> baseTableInlineWhereList = getBaseTableInlineWhereList();
            sb.append(getInlineViewClause(tableSqlName, baseTableInlineWhereList, tablePos));
            sb.append(" ").append(basePointAliasName);
        } else {
            sb.append(tableSqlName).append(" ").append(basePointAliasName);
        }
        sb.append(getFromBaseTableHint());
        sb.append(getLeftOuterJoinClause());
    }

    public String getFromBaseTableHint() {
        return createFromBaseTableHint();
    }

    protected String getLeftOuterJoinClause() {
        final StringBuilder sb = new StringBuilder();
        reflectInnerJoinAutoDetectLazily();
        final boolean countLeastJoinAllowed = checkCountLeastJoinAllowed();
        final boolean structuralPossibleInnerJoinAllowed = checkStructuralPossibleInnerJoinAllowed();
        for (Entry<String, LeftOuterJoinInfo> outerJoinEntry : getOuterJoinMap().entrySet()) {
            final String foreignAliasName = outerJoinEntry.getKey();
            final LeftOuterJoinInfo joinInfo = outerJoinEntry.getValue();
            if (countLeastJoinAllowed && canBeCountLeastJoin(joinInfo)) {
                continue; // means only joined countable
            }
            buildLeftOuterJoinClause(sb, foreignAliasName, joinInfo, structuralPossibleInnerJoinAllowed);
        }
        return sb.toString();
    }

    protected void reflectInnerJoinAutoDetectLazily() {
        if (!hasInnerJoinLazyReflector()) {
            return;
        }
        final List<InnerJoinLazyReflector> reflectorList = getInnerJoinLazyReflectorList();
        for (InnerJoinLazyReflector reflector : reflectorList) {
            reflector.reflect();
        }
        reflectorList.clear();
    }

    protected boolean canBeCountLeastJoin(LeftOuterJoinInfo joinInfo) {
        return !joinInfo.isCountableJoin();
    }

    protected boolean checkCountLeastJoinAllowed() {
        if (!canPagingCountLeastJoin()) {
            return false;
        }
        if (!isSelectClauseTypeNonUnionCount()) {
            return false;
        }
        return !hasFixedConditionOverRelationJoin();
    }

    protected boolean checkStructuralPossibleInnerJoinAllowed() {
        if (!_structuralPossibleInnerJoinAllowed) {
            return false;
        }
        return !hasFixedConditionOverRelationJoin();
    }

    // to use over-relation provides very complex logic
    // so it suppresses PagingCountLeastJoin and StructuralPossibleInnerJoin
    protected boolean hasFixedConditionOverRelationJoin() {
        for (LeftOuterJoinInfo joinInfo : getOuterJoinMap().values()) {
            if (joinInfo.hasFixedConditionOverRelation()) {
                // because over-relation may have references of various relations
                return true;
            }
        }
        return false;
    }

    protected void buildLeftOuterJoinClause(StringBuilder sb, String foreignAliasName, LeftOuterJoinInfo joinInfo,
            boolean structuralPossibleInnerJoinAllowed) {
        final Map<ColumnRealName, ColumnRealName> joinOnMap = joinInfo.getJoinOnMap();
        assertJoinOnMapNotEmpty(joinOnMap, foreignAliasName);

        sb.append(ln()).append("   ");
        final String joinExp;
        final boolean canBeInnerJoin = canBeInnerJoin(joinInfo, structuralPossibleInnerJoinAllowed);
        if (canBeInnerJoin) {
            joinExp = " inner join ";
        } else {
            joinExp = " left outer join "; // is main!
        }
        sb.append(joinExp); // is main!
        buildJoinTableClause(sb, joinInfo, joinExp, canBeInnerJoin);
        sb.append(" ").append(foreignAliasName);
        if (joinInfo.hasInlineOrOnClause() || joinInfo.hasFixedCondition()) {
            sb.append(ln()).append("     "); // only when additional conditions exist
        }
        sb.append(" on ");
        buildJoinOnClause(sb, joinInfo, joinOnMap);
        if (isJoinInParentheses()) {
            sb.append(")");
        }
    }

    protected boolean canBeInnerJoin(LeftOuterJoinInfo joinInfo, boolean structuralPossibleInnerJoinAllowed) {
        if (joinInfo.isInnerJoin()) {
            return true;
        }
        if (structuralPossibleInnerJoinAllowed) {
            return joinInfo.isStructuralPossibleInnerJoin();
        }
        return false;
    }

    protected boolean isJoinInParentheses() { // for DBMS that needs to join in parentheses
        return false; // as default
    }

    protected void buildJoinTableClause(StringBuilder sb, LeftOuterJoinInfo joinInfo, String joinExp,
            boolean canBeInnerJoin) {
        final String foreignTableDbName = joinInfo.getForeignTableDbName();
        final int tablePos = 3 + joinExp.length(); // basically for in-line view indent
        final DBMeta foreignDBMeta = findDBMeta(foreignTableDbName);
        final TableSqlName foreignTableSqlName = foreignDBMeta.getTableSqlName();
        final List<QueryClause> inlineWhereClauseList = joinInfo.getInlineWhereClauseList();
        final String tableExp;
        if (inlineWhereClauseList.isEmpty()) {
            tableExp = foreignTableSqlName.toString();
        } else {
            tableExp = getInlineViewClause(foreignTableSqlName, inlineWhereClauseList, tablePos);
        }
        if (joinInfo.hasFixedCondition()) {
            sb.append(joinInfo.resolveFixedInlineView(tableExp, canBeInnerJoin));
        } else {
            sb.append(tableExp);
        }
    }

    protected String getInlineViewClause(TableSqlName inlineTableSqlName, List<QueryClause> inlineWhereClauseList,
            int tablePos) {
        final String inlineBaseAlias = getInlineViewBasePointAlias();
        final StringBuilder sb = new StringBuilder();
        sb.append("(select * from ").append(inlineTableSqlName).append(" ").append(inlineBaseAlias);
        final String baseIndent = buildSpaceBar(tablePos + 1);
        sb.append(ln()).append(baseIndent);
        sb.append(" where ");
        int count = 0;
        for (QueryClause whereClause : inlineWhereClauseList) {
            final String clauseElement = filterWhereClauseSimply(whereClause.toString());
            if (count > 0) {
                sb.append(ln()).append(baseIndent);
                sb.append("   and ");
            }
            sb.append(clauseElement);
            ++count;
        }
        sb.append(")");
        return sb.toString();
    }

    protected void buildJoinOnClause(StringBuilder sb, LeftOuterJoinInfo joinInfo,
            Map<ColumnRealName, ColumnRealName> joinOnMap) {
        int currentConditionCount = 0;
        currentConditionCount = doBuildJoinOnClauseBasic(sb, joinInfo, joinOnMap, currentConditionCount);
        currentConditionCount = doBuildJoinOnClauseFixed(sb, joinInfo, joinOnMap, currentConditionCount);
        currentConditionCount = doBuildJoinOnClauseAdditional(sb, joinInfo, joinOnMap, currentConditionCount);
    }

    protected int doBuildJoinOnClauseBasic(StringBuilder sb, LeftOuterJoinInfo joinInfo,
            Map<ColumnRealName, ColumnRealName> joinOnMap, int currentConditionCount) {
        for (Entry<ColumnRealName, ColumnRealName> joinOnEntry : joinOnMap.entrySet()) {
            final ColumnRealName localRealName = joinOnEntry.getKey();
            final ColumnRealName foreignRealName = joinOnEntry.getValue();
            sb.append(currentConditionCount > 0 ? " and " : "");
            sb.append(localRealName).append(" = ").append(foreignRealName);
            ++currentConditionCount;
        }
        return currentConditionCount;
    }

    protected int doBuildJoinOnClauseFixed(StringBuilder sb, LeftOuterJoinInfo joinInfo,
            Map<ColumnRealName, ColumnRealName> joinOnMap, int currentConditionCount) {
        if (joinInfo.hasFixedCondition()) {
            final String fixedCondition = joinInfo.getFixedCondition();
            sb.append(ln()).append("    ");
            sb.append(currentConditionCount > 0 ? " and " : "");
            sb.append(fixedCondition);
            ++currentConditionCount;
        }
        return currentConditionCount;
    }

    protected int doBuildJoinOnClauseAdditional(StringBuilder sb, LeftOuterJoinInfo joinInfo,
            Map<ColumnRealName, ColumnRealName> joinOnMap, int currentConditionCount) {
        final List<QueryClause> additionalOnClauseList = joinInfo.getAdditionalOnClauseList();
        for (QueryClause additionalOnClause : additionalOnClauseList) {
            sb.append(ln()).append("    ");
            sb.append(currentConditionCount > 0 ? " and " : "");
            sb.append(additionalOnClause);
            ++currentConditionCount;
        }
        return currentConditionCount;
    }

    // -----------------------------------------------------
    //                                             From Hint
    //                                             ---------
    public String getFromHint() {
        return createFromHint();
    }

    // -----------------------------------------------------
    //                                          Where Clause
    //                                          ------------
    public String getWhereClause() {
        reflectClauseLazilyIfExists();
        final StringBuilder sb = new StringBuilder();
        buildWhereClause(sb);
        return sb.toString();
    }

    protected void buildWhereClause(StringBuilder sb) {
        buildWhereClause(sb, false);
    }

    protected void buildWhereClause(StringBuilder sb, boolean template) {
        final List<QueryClause> whereList = getWhereList();
        if (whereList.isEmpty()) {
            if (template) {
                sb.append(" ").append(getWhereClauseMark());
            }
            return;
        }
        int count = 0;
        for (QueryClause whereClause : whereList) {
            final String clauseElement = filterWhereClauseSimply(whereClause.toString());
            if (count == 0) {
                sb.append(ln()).append(" ");
                sb.append("where ").append(template ? getWhereFirstConditionMark() : "").append(clauseElement);
            } else {
                sb.append(ln()).append("  ");
                sb.append(" and ").append(clauseElement);
            }
            ++count;
        }
    }

    // -----------------------------------------------------
    //                                        OrderBy Clause
    //                                        --------------
    public String getOrderByClause() {
        reflectClauseLazilyIfExists();
        final OrderByClause orderBy = getOrderBy();
        String orderByClause = null;
        if (hasUnionQuery()) {
            final Map<String, String> selectClauseRealColumnAliasMap = getSelectClauseRealColumnAliasMap();
            if (selectClauseRealColumnAliasMap.isEmpty()) {
                String msg = "The selectClauseColumnAliasMap should not be empty when union query exists.";
                throw new IllegalStateException(msg);
            }
            orderByClause = orderBy.getOrderByClause(selectClauseRealColumnAliasMap);
        } else {
            orderByClause = orderBy.getOrderByClause();
        }
        if (orderByClause != null && orderByClause.trim().length() > 0) {
            return ln() + " " + orderByClause;
        } else {
            return orderByClause;
        }
    }

    // -----------------------------------------------------
    //                                            SQL Suffix
    //                                            ----------
    public String getSqlSuffix() {
        reflectClauseLazilyIfExists();
        String sqlSuffix = createSqlSuffix();
        if (sqlSuffix != null && sqlSuffix.trim().length() > 0) {
            return ln() + sqlSuffix;
        } else {
            return sqlSuffix;
        }
    }

    // ===================================================================================
    //                                                                   Selected Relation
    //                                                                   =================
    /**
     * {@inheritDoc}
     */
    public void registerSelectedRelation(String foreignTableAliasName, String localTableDbName,
            String foreignPropertyName, String localRelationPath, String foreignRelationPath) {
        getSelectedRelationBasicMap().put(foreignRelationPath, foreignPropertyName);
        final Map<String, SelectedRelationColumn> columnMap = createSelectedSelectColumnInfo(foreignTableAliasName,
                localTableDbName, foreignPropertyName, localRelationPath);
        getSelectedRelationColumnMap().put(foreignTableAliasName, columnMap);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelectedRelationEmpty() {
        return _selectedRelationBasicMap == null || _selectedRelationBasicMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasSelectedRelation(String relationPath) {
        return _selectedRelationBasicMap != null && _selectedRelationBasicMap.containsKey(relationPath);
    }

    protected Map<String, SelectedRelationColumn> createSelectedSelectColumnInfo(String foreignTableAliasName,
            String localTableDbName, String foreignPropertyName, String localRelationPath) {
        final DBMeta dbmeta = findDBMeta(localTableDbName);
        final ForeignInfo foreignInfo = dbmeta.findForeignInfo(foreignPropertyName);
        final int relationNo = foreignInfo.getRelationNo();
        String nextRelationPath = "_" + relationNo;
        if (localRelationPath != null) {
            nextRelationPath = localRelationPath + nextRelationPath;
        }
        final Map<String, SelectedRelationColumn> resultMap = new LinkedHashMap<String, SelectedRelationColumn>();
        final DBMeta foreignDBMeta = foreignInfo.getForeignDBMeta();
        final List<ColumnInfo> columnInfoList = foreignDBMeta.getColumnInfoList();
        for (ColumnInfo columnInfo : columnInfoList) {
            final String columnDbName = columnInfo.getColumnDbName();
            final SelectedRelationColumn selectColumnInfo = new SelectedRelationColumn();
            selectColumnInfo.setTableAliasName(foreignTableAliasName);
            selectColumnInfo.setColumnInfo(columnInfo);
            selectColumnInfo.setColumnAliasName(columnDbName + nextRelationPath);
            resultMap.put(columnDbName, selectColumnInfo);
        }
        return resultMap;
    }

    protected Map<String, String> getSelectedRelationBasicMap() {
        if (_selectedRelationBasicMap == null) {
            _selectedRelationBasicMap = new LinkedHashMap<String, String>();
        }
        return _selectedRelationBasicMap;
    }

    public Map<String, Map<String, SelectedRelationColumn>> getSelectedRelationColumnMap() {
        if (_selectedRelationColumnMap == null) {
            _selectedRelationColumnMap = new LinkedHashMap<String, Map<String, SelectedRelationColumn>>();
        }
        return _selectedRelationColumnMap;
    }

    // ===================================================================================
    //                                                                           OuterJoin
    //                                                                           =========
    // -----------------------------------------------------
    //                                          Registration
    //                                          ------------
    /**
     * {@inheritDoc}
     */
    public void registerOuterJoin(String foreignAliasName, String foreignTableDbName, String localAliasName,
            String localTableDbName, Map<ColumnRealName, ColumnRealName> joinOnMap, ForeignInfo foreignInfo,
            String fixedCondition, FixedConditionResolver fixedConditionResolver) {
        doRegisterOuterJoin(foreignAliasName, foreignTableDbName, localAliasName, localTableDbName, joinOnMap,
                foreignInfo, fixedCondition, fixedConditionResolver);
    }

    /**
     * {@inheritDoc}
     */
    public void registerOuterJoinFixedInline(String foreignAliasName, String foreignTableDbName, String localAliasName,
            String localTableDbName, Map<ColumnRealName, ColumnRealName> joinOnMap, ForeignInfo foreignInfo,
            String fixedCondition, FixedConditionResolver fixedConditionResolver) {
        doRegisterOuterJoin(foreignAliasName, foreignTableDbName, localAliasName, localTableDbName // same as normal
                , joinOnMap, foreignInfo // normal until here
                , null, null); // null set to OnClause
        if (fixedCondition != null) { // uses it instead of null set
            if (fixedConditionResolver != null) {
                fixedCondition = fixedConditionResolver.resolveVariable(fixedCondition, true);
            }
            final String inlineBaseAlias = getInlineViewBasePointAlias();
            final String clause = Srl.replace(fixedCondition, foreignAliasName + ".", inlineBaseAlias + ".");
            registerOuterJoinInlineWhereClause(foreignAliasName, clause, false);
        }
    }

    protected void doRegisterOuterJoin(String foreignAliasName, String foreignTableDbName, String localAliasName,
            String localTableDbName, Map<ColumnRealName, ColumnRealName> joinOnMap, ForeignInfo foreignInfo,
            String fixedCondition, FixedConditionResolver fixedConditionResolver) {
        assertAlreadyOuterJoin(foreignAliasName);
        assertJoinOnMapNotEmpty(joinOnMap, foreignAliasName);
        final Map<String, LeftOuterJoinInfo> outerJoinMap = getOuterJoinMap();
        final LeftOuterJoinInfo joinInfo = new LeftOuterJoinInfo();
        joinInfo.setForeignAliasName(foreignAliasName);
        joinInfo.setForeignTableDbName(foreignTableDbName);
        joinInfo.setLocalAliasName(localAliasName);
        joinInfo.setLocalTableDbName(localTableDbName);
        joinInfo.setJoinOnMap(joinOnMap);
        final LeftOuterJoinInfo localJoinInfo = outerJoinMap.get(localAliasName);
        if (localJoinInfo != null) { // means local is also joined (not base point)
            joinInfo.setLocalJoinInfo(localJoinInfo);
        }
        joinInfo.setPureFK(foreignInfo.isPureFK());
        joinInfo.setNotNullFKColumn(foreignInfo.isNotNullFKColumn());
        joinInfo.setFixedCondition(fixedCondition);
        joinInfo.setFixedConditionResolver(fixedConditionResolver);

        // it should be resolved before registration because
        // the process may have Query(Relation) as precondition
        joinInfo.resolveFixedCondition();

        outerJoinMap.put(foreignAliasName, joinInfo);
    }

    // -----------------------------------------------------
    //                                   OuterJoin Attribute
    //                                   -------------------
    /**
     * {@inheritDoc}
     */
    public Map<String, LeftOuterJoinInfo> getOuterJoinMap() {
        if (_outerJoinMap == null) {
            _outerJoinMap = new LinkedHashMap<String, LeftOuterJoinInfo>(4);
        }
        return _outerJoinMap;
    }

    public boolean hasOuterJoin() {
        return _outerJoinMap != null && !_outerJoinMap.isEmpty();
    }

    // -----------------------------------------------------
    //                                    InnerJoin Handling
    //                                    ------------------
    /**
     * {@inheritDoc}
     */
    public void changeToInnerJoin(String foreignAliasName) {
        doChangeToInnerJoin(foreignAliasName, false);
    }

    protected void doChangeToInnerJoin(String foreignAliasName, boolean autoDetect) {
        final Map<String, LeftOuterJoinInfo> outerJoinMap = getOuterJoinMap();
        final LeftOuterJoinInfo joinInfo = outerJoinMap.get(foreignAliasName);
        if (joinInfo == null) {
            String msg = "The foreignAliasName was not found:";
            msg = msg + " " + foreignAliasName + " in " + outerJoinMap.keySet();
            throw new IllegalStateException(msg);
        }
        joinInfo.setInnerJoin(true);
        reflectUnderInnerJoinToJoin(joinInfo, autoDetect);
    }

    protected void reflectUnderInnerJoinToJoin(final LeftOuterJoinInfo foreignJoinInfo, boolean autoDetect) {
        LeftOuterJoinInfo currentJoinInfo = foreignJoinInfo.getLocalJoinInfo();
        while (true) {
            if (currentJoinInfo == null) { // means base point
                break;
            }
            // all join-info are overridden because of complex logic
            if (autoDetect) {
                currentJoinInfo.setInnerJoin(true); // be inner-join as we can if auto-detect
            } else {
                currentJoinInfo.setUnderInnerJoin(true); // manual is pinpoint setting
            }
            currentJoinInfo = currentJoinInfo.getLocalJoinInfo(); // trace back toward base point
        }
    }

    // -----------------------------------------------------
    //                                  InnerJoin AutoDetect
    //                                  --------------------
    // has several items of inner-join auto-detected
    public void allowInnerJoinAutoDetect() {
        allowStructuralPossibleInnerJoin();
        allowWhereUsedInnerJoin();
    }

    public void suppressInnerJoinAutoDetect() {
        suppressStructuralPossibleInnerJoin();
        suppressWhereUsedInnerJoin();
    }

    // -----------------------------------------------------
    //                          StructuralPossible InnerJoin
    //                          ----------------------------
    // one of inner-join auto-detect
    public void allowStructuralPossibleInnerJoin() {
        _structuralPossibleInnerJoinAllowed = true;
    }

    public void suppressStructuralPossibleInnerJoin() {
        _structuralPossibleInnerJoinAllowed = false;
    }

    public boolean isStructuralPossibleInnerJoinAllowed() {
        return _structuralPossibleInnerJoinAllowed;
    }

    // -----------------------------------------------------
    //                                   WhereUsed InnerJoin
    //                                   -------------------
    // one of inner-join auto-detect
    public void allowWhereUsedInnerJoin() {
        _whereUsedInnerJoinAllowed = true;
    }

    public void suppressWhereUsedInnerJoin() {
        _whereUsedInnerJoinAllowed = false;
    }

    public boolean isWhereUsedInnerJoinAllowed() {
        return _whereUsedInnerJoinAllowed;
    }

    // -----------------------------------------------------
    //                               InnerJoin LazyReflector
    //                               -----------------------
    protected List<InnerJoinLazyReflector> getInnerJoinLazyReflectorList() {
        if (_innerJoinLazyReflector == null) {
            _innerJoinLazyReflector = new ArrayList<InnerJoinLazyReflector>(4);
        }
        return _innerJoinLazyReflector;
    }

    protected boolean hasInnerJoinLazyReflector() {
        return _innerJoinLazyReflector != null && !_innerJoinLazyReflector.isEmpty();
    }

    // -----------------------------------------------------
    //                                         Assert Helper
    //                                         -------------
    protected void assertAlreadyOuterJoin(String foreignAliasName) {
        if (getOuterJoinMap().containsKey(foreignAliasName)) {
            String msg = "The foreign alias name have already registered in outer join: " + foreignAliasName;
            throw new IllegalStateException(msg);
        }
    }

    protected void assertJoinOnMapNotEmpty(Map<ColumnRealName, ColumnRealName> joinOnMap, String foreignAliasName) {
        if (joinOnMap.isEmpty()) {
            String msg = "The joinOnMap should not be empty: foreignAliasName=" + foreignAliasName;
            throw new IllegalStateException(msg);
        }
    }

    // ===================================================================================
    //                                                                               Where
    //                                                                               =====
    // -----------------------------------------------------
    //                                          Registration
    //                                          ------------
    /**
     * {@inheritDoc}
     */
    public void registerWhereClause(ColumnRealName columnRealName // real name of column
            , ConditionKey key, ConditionValue value // basic resources
            , ColumnFunctionCipher cipher, ConditionOption option // optional resources
            , String usedAliasName) { // table alias name used on where clause
        assertObjectNotNull("columnRealName", columnRealName);
        assertObjectNotNull("key", key);
        assertObjectNotNull("value", value);
        assertStringNotNullAndNotTrimmedEmpty("usedAliasName", usedAliasName);
        final List<QueryClause> clauseList = getWhereClauseList4Register();
        doRegisterWhereClause(clauseList, columnRealName, key, value, cipher, option, false, false);
        reflectWhereUsedToJoin(usedAliasName);
        if (!ConditionKey.isNullaleConditionKey(key)) {
            registerInnerJoinLazyReflector(usedAliasName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void registerWhereClause(String clause, String usedAliasName) {
        registerWhereClause(clause, usedAliasName, false); // possible to be inner-join as default
    }

    /**
     * {@inheritDoc}
     */
    public void registerWhereClause(String clause, String usedAliasName, boolean noWayInner) {
        assertStringNotNullAndNotTrimmedEmpty("clause", clause);
        assertStringNotNullAndNotTrimmedEmpty("usedAliasName", usedAliasName);
        final List<QueryClause> clauseList = getWhereClauseList4Register();
        doRegisterWhereClause(clauseList, clause);
        reflectWhereUsedToJoin(usedAliasName);
        if (!noWayInner) {
            registerInnerJoinLazyReflector(usedAliasName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void registerWhereClause(QueryClause clause, QueryUsedAliasInfo... usedAliasInfos) {
        assertObjectNotNull("clause", clause);
        assertObjectNotNull("usedAliasInfos", usedAliasInfos);
        if (usedAliasInfos.length == 0) {
            String msg = "The argument 'usedAliasInfos' should not be empty.";
            throw new IllegalArgumentException(msg);
        }
        final List<QueryClause> clauseList = getWhereClauseList4Register();
        doRegisterWhereClause(clauseList, clause);
        for (QueryUsedAliasInfo usedAliasInfo : usedAliasInfos) {
            final String usedAliasName = usedAliasInfo.getUsedAliasName();
            reflectWhereUsedToJoin(usedAliasName);
            registerInnerJoinLazyReflector(usedAliasInfo);
        }
    }

    // -----------------------------------------------------
    //                                        WhereUsed Join
    //                                        --------------
    protected void reflectWhereUsedToJoin(final String usedAliasName) {
        LeftOuterJoinInfo currentJoinInfo = getOuterJoinMap().get(usedAliasName);
        while (true) {
            if (currentJoinInfo == null) { // means base point
                break;
            }
            if (currentJoinInfo.isWhereUsedJoin()) { // means already traced
                break;
            }
            currentJoinInfo.setWhereUsedJoin(true);
            currentJoinInfo = currentJoinInfo.getLocalJoinInfo(); // trace back toward base point
        }
    }

    // -----------------------------------------------------
    //                                  InnerJoin Reflection
    //                                  --------------------
    protected void registerInnerJoinLazyReflector(String usedAliasName) { // without no-way speaker
        if (isOutOfWhereUsedInnerJoin()) {
            return;
        }
        final QueryUsedAliasInfo usedAliasInfo = new QueryUsedAliasInfo(usedAliasName, null);
        registerInnerJoinLazyReflector(usedAliasInfo);
    }

    protected void registerInnerJoinLazyReflector(QueryUsedAliasInfo usedAliasInfo) {
        if (isOutOfWhereUsedInnerJoin()) {
            return;
        }
        final List<InnerJoinLazyReflector> reflectorList = getInnerJoinLazyReflectorList();
        reflectorList.add(createInnerJoinLazyReflector(usedAliasInfo));
    }

    protected boolean isOutOfWhereUsedInnerJoin() {
        return !_whereUsedInnerJoinAllowed || _orScopeQueryEffective;
    }

    protected InnerJoinLazyReflectorBase createInnerJoinLazyReflector(QueryUsedAliasInfo usedAliasInfo) {
        final String usedAliasName = usedAliasInfo.getUsedAliasName();
        return new InnerJoinLazyReflectorBase(usedAliasInfo.getInnerJoinAutoDetectNoWaySpeaker()) {
            @Override
            protected void doReflect() {
                if (getOuterJoinMap().containsKey(usedAliasName)) { // checked because it may be local
                    doChangeToInnerJoin(usedAliasName, true);
                }
            }
        };
    }

    // -----------------------------------------------------
    //                                       Where Attribute
    //                                       ---------------
    protected List<QueryClause> getWhereClauseList4Register() {
        if (_orScopeQueryEffective) {
            return getTmpOrWhereList();
        } else {
            return getWhereList();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void exchangeFirstWhereClauseForLastOne() {
        final List<QueryClause> whereList = getWhereList();
        if (whereList.size() > 1) {
            final QueryClause first = whereList.get(0);
            final QueryClause last = whereList.get(whereList.size() - 1);
            whereList.set(0, last);
            whereList.set(whereList.size() - 1, first);
        }
    }

    protected List<QueryClause> getWhereList() {
        if (_whereList == null) {
            _whereList = new ArrayList<QueryClause>(8);
        }
        return _whereList;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasWhereClauseOnBaseQuery() {
        return _whereList != null && !_whereList.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public void clearWhereClauseOnBaseQuery() {
        if (_whereList != null) {
            _whereList.clear();
        }
    }

    // ===================================================================================
    //                                                                       In-line Where
    //                                                                       =============
    // -----------------------------------------------------
    //                                In-line for Base Table
    //                                ----------------------
    public void registerBaseTableInlineWhereClause(ColumnSqlName columnSqlName // SQL name of column
            , ConditionKey key, ConditionValue value // basic resources
            , ColumnFunctionCipher cipher) { // optional resources
        registerBaseTableInlineWhereClause(columnSqlName, key, value, cipher, null);
    }

    public void registerBaseTableInlineWhereClause(ColumnSqlName columnSqlName // SQL name of column
            , ConditionKey key, ConditionValue value // basic resources
            , ColumnFunctionCipher cipher, ConditionOption option) { // optional resources
        final List<QueryClause> clauseList = getBaseTableInlineWhereClauseList4Register();
        final String inlineBaseAlias = getInlineViewBasePointAlias();
        final ColumnRealName columnRealName = ColumnRealName.create(inlineBaseAlias, columnSqlName);
        doRegisterWhereClause(clauseList, columnRealName, key, value, cipher, option, true, false);
    }

    public void registerBaseTableInlineWhereClause(String value) {
        final List<QueryClause> clauseList = getBaseTableInlineWhereClauseList4Register();
        doRegisterWhereClause(clauseList, value);
    }

    protected List<QueryClause> getBaseTableInlineWhereClauseList4Register() {
        if (_orScopeQueryEffective) {
            return getTmpOrBaseTableInlineWhereList();
        } else {
            return getBaseTableInlineWhereList();
        }
    }

    protected List<QueryClause> getBaseTableInlineWhereList() {
        if (_baseTableInlineWhereList == null) {
            _baseTableInlineWhereList = new ArrayList<QueryClause>(2);
        }
        return _baseTableInlineWhereList;
    }

    public boolean hasBaseTableInlineWhereClause() {
        return _baseTableInlineWhereList != null && !_baseTableInlineWhereList.isEmpty();
    }

    public void clearBaseTableInlineWhereClause() {
        if (_baseTableInlineWhereList != null) {
            _baseTableInlineWhereList.clear();
        }
    }

    // -----------------------------------------------------
    //                                In-line for Outer Join
    //                                ----------------------
    public void registerOuterJoinInlineWhereClause(String foreignAliasName, ColumnSqlName columnSqlName,
            ConditionKey key, ConditionValue value, ColumnFunctionCipher cipher, boolean onClause) {
        registerOuterJoinInlineWhereClause(foreignAliasName, columnSqlName, key, value, cipher, null, onClause);
    }

    public void registerOuterJoinInlineWhereClause(String foreignAliasName // foreign alias of column 
            , ColumnSqlName columnSqlName // SQL name of column
            , ConditionKey key, ConditionValue value // basic resources
            , ColumnFunctionCipher cipher, ConditionOption option // optional resources
            , boolean onClause) {
        assertNotYetOuterJoin(foreignAliasName);
        final List<QueryClause> clauseList = getOuterJoinInlineWhereClauseList4Register(foreignAliasName, onClause);
        final String tableAliasName = onClause ? foreignAliasName : getInlineViewBasePointAlias();
        final ColumnRealName columnRealName = ColumnRealName.create(tableAliasName, columnSqlName);
        doRegisterWhereClause(clauseList, columnRealName, key, value, cipher, option, true, onClause);
    }

    public void registerOuterJoinInlineWhereClause(String foreignAliasName, String clause, boolean onClause) {
        assertNotYetOuterJoin(foreignAliasName);
        final List<QueryClause> clauseList = getOuterJoinInlineWhereClauseList4Register(foreignAliasName, onClause);
        doRegisterWhereClause(clauseList, clause);
    }

    protected List<QueryClause> getOuterJoinInlineWhereClauseList4Register(String foreignAliasName, boolean onClause) {
        final LeftOuterJoinInfo joinInfo = getOuterJoinMap().get(foreignAliasName);
        final List<QueryClause> clauseList;
        if (onClause) {
            if (_orScopeQueryEffective) {
                clauseList = getTmpOrAdditionalOnClauseList(foreignAliasName);
            } else {
                clauseList = joinInfo.getAdditionalOnClauseList();
            }
        } else {
            if (_orScopeQueryEffective) {
                clauseList = getTmpOrOuterJoinInlineClauseList(foreignAliasName);
            } else {
                clauseList = joinInfo.getInlineWhereClauseList();
            }
        }
        return clauseList;
    }

    protected void assertNotYetOuterJoin(String aliasName) {
        if (!getOuterJoinMap().containsKey(aliasName)) {
            String msg = "The alias name have not registered in outer join yet: " + aliasName;
            throw new IllegalStateException(msg);
        }
    }

    public boolean hasOuterJoinInlineWhereClause() {
        if (_outerJoinMap == null) {
            return false;
        }
        for (Entry<String, LeftOuterJoinInfo> entry : _outerJoinMap.entrySet()) {
            final LeftOuterJoinInfo joinInfo = entry.getValue();
            if (joinInfo.hasInlineOrOnClause()) { // contains on-clause condition
                return true;
            }
        }
        return false;
    }

    public void clearOuterJoinInlineWhereClause() {
        if (_outerJoinMap == null) {
            return;
        }
        for (Entry<String, LeftOuterJoinInfo> entry : _outerJoinMap.entrySet()) {
            final LeftOuterJoinInfo joinInfo = entry.getValue();
            if (joinInfo.hasInlineOrOnClause()) {
                joinInfo.getInlineWhereClauseList().clear();
                joinInfo.getAdditionalOnClauseList().clear(); // contains on-clause condition
            }
        }
    }

    // -----------------------------------------------------
    //                                         Assist Helper
    //                                         -------------
    protected void doRegisterWhereClause(List<QueryClause> clauseList // the list of query clause
            , ColumnRealName columnRealName, ConditionKey key, ConditionValue value // basic resources
            , ColumnFunctionCipher cipher, ConditionOption option // optional resources
            , final boolean inline, final boolean onClause) {
        key.addWhereClause(new QueryModeProvider() {
            public boolean isOrScopeQuery() {
                return isOrScopeQueryEffective();
            }

            public boolean isInline() {
                return inline;
            }

            public boolean isOnClause() {
                return onClause;
            }
        }, clauseList, columnRealName, value, cipher, option);
        markOrScopeQueryAndPart(clauseList);
    }

    protected void doRegisterWhereClause(List<QueryClause> clauseList, String clause) {
        doRegisterWhereClause(clauseList, new StringQueryClause(clause));
    }

    protected void doRegisterWhereClause(List<QueryClause> clauseList, QueryClause clause) {
        clauseList.add(clause);
        markOrScopeQueryAndPart(clauseList);
    }

    // ===================================================================================
    //                                                                        OrScopeQuery
    //                                                                        ============
    public void makeOrScopeQueryEffective() {
        final OrScopeQueryInfo tmpOrScopeQueryInfo = new OrScopeQueryInfo();
        if (_currentTmpOrScopeQueryInfo != null) {
            _currentTmpOrScopeQueryInfo.addChildInfo(tmpOrScopeQueryInfo);
        }
        _currentTmpOrScopeQueryInfo = tmpOrScopeQueryInfo;
        _orScopeQueryEffective = true;
    }

    public void closeOrScopeQuery() {
        assertCurrentTmpOrScopeQueryInfo();
        final OrScopeQueryInfo parentInfo = _currentTmpOrScopeQueryInfo.getParentInfo();
        if (parentInfo != null) {
            _currentTmpOrScopeQueryInfo = parentInfo;
        } else {
            reflectTmpOrClauseToRealObject(_currentTmpOrScopeQueryInfo);
            clearOrScopeQuery();
        }
    }

    protected void clearOrScopeQuery() {
        _currentTmpOrScopeQueryInfo = null;
        _orScopeQueryEffective = false;
        _orScopeQueryAndPartEffective = false;
    }

    protected void reflectTmpOrClauseToRealObject(OrScopeQueryInfo localInfo) {
        final OrScopeQueryReflector reflector = createOrClauseReflector();
        reflector.reflectTmpOrClauseToRealObject(localInfo);
    }

    protected OrScopeQueryReflector createOrClauseReflector() {
        return new OrScopeQueryReflector(getWhereList(), getBaseTableInlineWhereList(), getOuterJoinMap());
    }

    public boolean isOrScopeQueryEffective() {
        return _orScopeQueryEffective;
    }

    public boolean isOrScopeQueryAndPartEffective() {
        return _orScopeQueryAndPartEffective;
    }

    protected List<QueryClause> getTmpOrWhereList() {
        assertCurrentTmpOrScopeQueryInfo();
        return _currentTmpOrScopeQueryInfo.getTmpOrWhereList();
    }

    protected List<QueryClause> getTmpOrBaseTableInlineWhereList() {
        assertCurrentTmpOrScopeQueryInfo();
        return _currentTmpOrScopeQueryInfo.getTmpOrBaseTableInlineWhereList();
    }

    protected List<QueryClause> getTmpOrAdditionalOnClauseList(String aliasName) {
        assertCurrentTmpOrScopeQueryInfo();
        return _currentTmpOrScopeQueryInfo.getTmpOrAdditionalOnClauseList(aliasName);
    }

    protected List<QueryClause> getTmpOrOuterJoinInlineClauseList(String aliasName) {
        assertCurrentTmpOrScopeQueryInfo();
        return _currentTmpOrScopeQueryInfo.getTmpOrOuterJoinInlineClauseList(aliasName);
    }

    public void beginOrScopeQueryAndPart() {
        assertCurrentTmpOrScopeQueryInfo();
        ++_orScopeQueryAndPartIdentity;
        _orScopeQueryAndPartEffective = true;
    }

    public void endOrScopeQueryAndPart() {
        assertCurrentTmpOrScopeQueryInfo();
        _orScopeQueryAndPartEffective = false;
    }

    protected void markOrScopeQueryAndPart(List<QueryClause> clauseList) {
        if (_orScopeQueryEffective && _orScopeQueryAndPartEffective && !clauseList.isEmpty()) {
            final QueryClause original = clauseList.remove(clauseList.size() - 1); // as latest
            clauseList.add(new OrScopeQueryAndPartQueryClause(original, _orScopeQueryAndPartIdentity));
        }
    }

    protected void assertCurrentTmpOrScopeQueryInfo() {
        if (_currentTmpOrScopeQueryInfo == null) {
            String msg = "The attribute 'currentTmpOrScopeQueryInfo' should not be null in or-scope query:";
            msg = msg + " orScopeQueryEffective=" + _orScopeQueryEffective;
            throw new IllegalStateException(msg);
        }
    }

    // ===================================================================================
    //                                                                             OrderBy
    //                                                                             =======
    public OrderByClause getOrderByComponent() {
        return getOrderBy();
    }

    protected OrderByClause getOrderBy() {
        if (_orderByClause == null) {
            _orderByClause = new OrderByClause();
        }
        return _orderByClause;
    }

    public OrderByElement getOrderByLastElement() {
        if (_orderByClause == null) {
            return null;
        }
        return _orderByClause.getOrderByLastElement();
    }

    public void clearOrderBy() {
        _orderByEffective = false;
        getOrderBy().clear();
    }

    public void makeOrderByEffective() {
        if (hasOrderByClause()) {
            _orderByEffective = true;
        }
    }

    public void ignoreOrderBy() {
        _orderByEffective = false;
    }

    public void registerOrderBy(String orderByProperty, boolean ascOrDesc, ColumnInfo columnInfo) {
        doRegisterOrderBy(orderByProperty, ascOrDesc, columnInfo, false);
    }

    public void registerSpecifiedDerivedOrderBy(String orderByProperty, boolean ascOrDesc) {
        final HpDerivingSubQueryInfo specifiedDerivingInfo = getSpecifiedDerivingInfo(orderByProperty);
        if (specifiedDerivingInfo == null) { // basically no way because of already checked
            String msg = "The deriving column was not found by the property: " + orderByProperty;
            throw new IllegalStateException(msg);
        }
        final ColumnInfo columnInfo = getSpecifiedDerivingColumnInfo(specifiedDerivingInfo);
        doRegisterOrderBy(orderByProperty, ascOrDesc, columnInfo, true);
    }

    protected void doRegisterOrderBy(String orderByProperty, boolean ascOrDesc, ColumnInfo columnInfo, boolean derived) {
        try {
            _orderByEffective = true;
            final List<String> orderByList = new ArrayList<String>();
            {
                final StringTokenizer st = new StringTokenizer(orderByProperty, "/");
                while (st.hasMoreElements()) {
                    orderByList.add(st.nextToken());
                }
            }
            for (String orderBy : orderByList) {
                _orderByEffective = true;
                final String aliasName;
                final String columnName;
                if (orderBy.indexOf(".") < 0) {
                    aliasName = null;
                    columnName = orderBy;
                } else {
                    aliasName = orderBy.substring(0, orderBy.lastIndexOf("."));
                    columnName = orderBy.substring(orderBy.lastIndexOf(".") + 1);
                }
                final OrderByElement element = new OrderByElement(aliasName, columnName, columnInfo, derived);
                if (ascOrDesc) {
                    element.setupAsc();
                } else {
                    element.setupDesc();
                }
                element.setGearedCipherManager(_gearedCipherManager);
                getOrderBy().addOrderByElement(element);

            }
        } catch (RuntimeException e) {
            String msg = "Failed to register order-by:";
            msg = msg + " orderByProperty=" + orderByProperty + " ascOrDesc=" + ascOrDesc;
            msg = msg + " table=" + _tableDbName;
            throw new IllegalStateException(msg, e);
        }
    }

    public void addNullsFirstToPreviousOrderBy() {
        getOrderBy().addNullsFirstToPreviousOrderByElement(createOrderByNullsSetupper());
    }

    public void addNullsLastToPreviousOrderBy() {
        getOrderBy().addNullsLastToPreviousOrderByElement(createOrderByNullsSetupper());
    }

    protected OrderByClause.OrderByNullsSetupper createOrderByNullsSetupper() { // as default
        return new OrderByClause.OrderByNullsSetupper() {
            public String setup(String columnName, String orderByElementClause, boolean nullsFirst) {
                return orderByElementClause + " nulls " + (nullsFirst ? "first" : "last");
            }
        };
    }

    protected OrderByClause.OrderByNullsSetupper createOrderByNullsSetupperByCaseWhen() { // helper for nulls unsupported DBMS
        return new OrderByClause.OrderByNullsSetupper() {
            public String setup(String columnName, String orderByElementClause, boolean nullsFirst) {
                final String thenNumber = nullsFirst ? "1" : "0";
                final String elseNumber = nullsFirst ? "0" : "1";
                final String caseWhen = "case when " + columnName + " is not null then " + thenNumber + " else "
                        + elseNumber + " end asc";
                return caseWhen + ", " + orderByElementClause;
            }
        };
    }

    public void addManualOrderToPreviousOrderByElement(ManualOrderBean manualOrderBean) {
        assertObjectNotNull("manualOrderBean", manualOrderBean);
        getOrderBy().addManualOrderByElement(manualOrderBean);
    }

    public boolean hasOrderByClause() {
        return _orderByClause != null && !_orderByClause.isEmpty();
    }

    // ===================================================================================
    //                                                                          UnionQuery
    //                                                                          ==========
    public void registerUnionQuery(UnionClauseProvider unionClauseProvider, boolean unionAll) {
        assertObjectNotNull("unionClauseProvider", unionClauseProvider);
        UnionQueryInfo unionQueryInfo = new UnionQueryInfo();
        unionQueryInfo.setUnionClauseProvider(unionClauseProvider);
        unionQueryInfo.setUnionAll(unionAll);
        addUnionQueryInfo(unionQueryInfo);
    }

    protected void addUnionQueryInfo(UnionQueryInfo unionQueryInfo) {
        if (_unionQueryInfoList == null) {
            _unionQueryInfoList = new ArrayList<UnionQueryInfo>();
        }
        _unionQueryInfoList.add(unionQueryInfo);
    }

    public boolean hasUnionQuery() {
        return _unionQueryInfoList != null && !_unionQueryInfoList.isEmpty();
    }

    public void clearUnionQuery() {
        if (_unionQueryInfoList != null) {
            _unionQueryInfoList.clear();
        }
    }

    protected static class UnionQueryInfo {
        protected UnionClauseProvider _unionClauseProvider;
        protected boolean _unionAll;

        public UnionClauseProvider getUnionClauseProvider() {
            return _unionClauseProvider;
        }

        public void setUnionClauseProvider(UnionClauseProvider unionClauseProvider) {
            _unionClauseProvider = unionClauseProvider;
        }

        public boolean isUnionAll() {
            return _unionAll;
        }

        public void setUnionAll(boolean unionAll) {
            _unionAll = unionAll;
        }
    }

    // ===================================================================================
    //                                                                          FetchScope
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    public void fetchFirst(int fetchSize) {
        _fetchScopeEffective = true;
        if (fetchSize <= 0) {
            String msg = "Argument[fetchSize] should be plus: " + fetchSize;
            throw new IllegalArgumentException(msg);
        }
        _fetchStartIndex = 0;
        _fetchSize = fetchSize;
        _fetchPageNumber = 1;
        doClearFetchPageClause();
        doFetchFirst();
    }

    /**
     * {@inheritDoc}
     */
    public void fetchScope(int fetchStartIndex, int fetchSize) {
        _fetchScopeEffective = true;
        if (fetchStartIndex < 0) {
            String msg = "Argument[fetchStartIndex] must be plus or zero: " + fetchStartIndex;
            throw new IllegalArgumentException(msg);
        }
        if (fetchSize <= 0) {
            String msg = "Argument[fetchSize] should be plus: " + fetchSize;
            throw new IllegalArgumentException(msg);
        }
        _fetchStartIndex = fetchStartIndex;
        _fetchSize = fetchSize;
        fetchPage(1);
    }

    /**
     * {@inheritDoc}
     */
    public void fetchPage(int fetchPageNumber) {
        _fetchScopeEffective = true;
        if (fetchPageNumber <= 0) {
            fetchPageNumber = 1;
        }
        if (_fetchSize <= 0) {
            throwFetchSizeNotPlusException(fetchPageNumber);
        }
        _fetchPageNumber = fetchPageNumber;
        if (_fetchPageNumber == 1 && _fetchStartIndex == 0) {
            fetchFirst(_fetchSize);
        }
        doClearFetchPageClause();
        doFetchPage();
    }

    protected void throwFetchSizeNotPlusException(int fetchPageNumber) { // as system exception
        String msg = "Look! Read the message below." + ln();
        msg = msg + "/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *" + ln();
        msg = msg + "Fetch size should not be minus or zero!" + ln();
        msg = msg + ln();
        msg = msg + "[Fetch Size]" + ln();
        msg = msg + "fetchSize=" + _fetchSize + ln();
        msg = msg + ln();
        msg = msg + "[Fetch Page Number]" + ln();
        msg = msg + "fetchPageNumber=" + fetchPageNumber + ln();
        msg = msg + "* * * * * * * * * */";
        throw new IllegalStateException(msg);
    }

    abstract protected void doFetchFirst();

    abstract protected void doFetchPage();

    abstract protected void doClearFetchPageClause();

    protected class RownumPagingProcessor {
        protected String _rownumExpression;
        protected String _selectHint = "";
        protected String _sqlSuffix = "";
        protected Integer _pagingBindFrom;
        protected Integer _pagingBindTo;
        protected boolean _bind;

        public RownumPagingProcessor(String rownumExpression) {
            _rownumExpression = rownumExpression;
        }

        public void useBindVariable() {
            _bind = true;
        }

        public void processRowNumberPaging() {
            final boolean offset = isFetchStartIndexSupported();
            final boolean limit = isFetchSizeSupported();
            if (!offset && !limit) {
                return;
            }

            final StringBuilder hintSb = new StringBuilder();
            final String rownum = _rownumExpression;
            hintSb.append(" *").append(ln());
            hintSb.append("  from (").append(ln());
            hintSb.append("select plain.*, ").append(rownum).append(" as rn").append(ln());
            hintSb.append("  from (").append(ln());
            hintSb.append("select"); // main select

            final StringBuilder suffixSb = new StringBuilder();
            final String fromEnd = "       ) plain" + ln() + "       ) ext" + ln();
            if (offset) {
                final int pageStartIndex = getPageStartIndex();
                _pagingBindFrom = pageStartIndex;
                final String exp = _bind ? "/*pmb.sqlClause.pagingBindFrom*/" : String.valueOf(pageStartIndex);
                suffixSb.append(fromEnd).append(" where ext.rn > ").append(exp);
            }
            if (limit) {
                final int pageEndIndex = getPageEndIndex();
                _pagingBindTo = pageEndIndex;
                final String exp = _bind ? "/*pmb.sqlClause.pagingBindTo*/" : String.valueOf(pageEndIndex);
                if (offset) {
                    suffixSb.append(ln()).append("   and ext.rn <= ").append(exp);
                } else {
                    suffixSb.append(fromEnd).append(" where ext.rn <= ").append(exp);
                }
            }

            _selectHint = hintSb.toString();
            _sqlSuffix = suffixSb.toString();
        }

        public String getSelectHint() {
            return _selectHint;
        }

        public String getSqlSuffix() {
            return _sqlSuffix;
        }

        public Integer getPagingBindFrom() {
            return _pagingBindFrom;
        }

        public Integer getPagingBindTo() {
            return _pagingBindTo;
        }
    }

    public int getFetchStartIndex() {
        return _fetchStartIndex;
    }

    public int getFetchSize() {
        return _fetchSize;
    }

    public int getFetchPageNumber() {
        return _fetchPageNumber;
    }

    /**
     * @return Page start index. 0 origin. (NotMinus)
     */
    public int getPageStartIndex() {
        if (_fetchPageNumber <= 0) {
            String msg = "_fetchPageNumber must be plus: " + _fetchPageNumber;
            throw new IllegalStateException(msg);
        }
        return _fetchStartIndex + (_fetchSize * (_fetchPageNumber - 1));
    }

    /**
     * @return Page end index. 0 origin. (NotMinus)
     */
    public int getPageEndIndex() {
        if (_fetchPageNumber <= 0) {
            String msg = "_fetchPageNumber must be plus: " + _fetchPageNumber;
            throw new IllegalStateException(msg);
        }
        return _fetchStartIndex + (_fetchSize * _fetchPageNumber);
    }

    public boolean isFetchScopeEffective() {
        return _fetchScopeEffective;
    }

    public void ignoreFetchScope() {
        _fetchScopeEffective = false;
        doClearFetchPageClause();
    }

    public void makeFetchScopeEffective() {
        if (getFetchSize() > 0 && getFetchPageNumber() > 0) {
            fetchPage(getFetchPageNumber());
        }
    }

    public boolean isFetchStartIndexSupported() {
        return true; // as default
    }

    public boolean isFetchSizeSupported() {
        return true; // as default
    }

    abstract protected String createSelectHint();

    abstract protected String createFromBaseTableHint();

    abstract protected String createFromHint();

    abstract protected String createSqlSuffix();

    // ===================================================================================
    //                                                                     Fetch Narrowing
    //                                                                     ===============
    /**
     * {@inheritDoc}
     */
    public int getFetchNarrowingSkipStartIndex() {
        return getPageStartIndex();
    }

    /**
     * {@inheritDoc}
     */
    public int getFetchNarrowingLoopCount() {
        return getFetchSize();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFetchNarrowingEffective() {
        return _fetchScopeEffective;
    }

    // ===================================================================================
    //                                                                    Table Alias Info
    //                                                                    ================
    /**
     * {@inheritDoc}
     */
    public String getBasePointAliasName() {
        // the variable should be resolved when making a sub-query clause
        return isForSubQuery() ? "sub" + getSubQueryLevel() + "loc" : "dfloc";
    }

    /**
     * {@inheritDoc}
     */
    public String resolveJoinAliasName(String relationPath, int nestLevel) {
        return (isForSubQuery() ? "sub" + getSubQueryLevel() : "df") + "rel" + relationPath;

        // nestLevel is unused because relationPath has same role
        // (that was used long long ago)
    }

    /**
     * {@inheritDoc}
     */
    public int resolveRelationNo(String localTableName, String foreignPropertyName) {
        final DBMeta dbmeta = findDBMeta(localTableName);
        final ForeignInfo foreignInfo = dbmeta.findForeignInfo(foreignPropertyName);
        return foreignInfo.getRelationNo();
    }

    /**
     * {@inheritDoc}
     */
    public String getInlineViewBasePointAlias() {
        return "dfinlineloc";
    }

    /**
     * {@inheritDoc}
     */
    public String getUnionQueryInlineViewAlias() {
        return "dfunionview";
    }

    /**
     * {@inheritDoc}
     */
    public String getDerivedReferrerNestedAlias() {
        return "dfrefview";
    }

    /**
     * {@inheritDoc}
     */
    public String getScalarSelectColumnAlias() {
        return "dfscalar";
    }

    // ===================================================================================
    //                                                                       Template Mark
    //                                                                       =============
    public String getWhereClauseMark() {
        return "#df:whereClause#";
    }

    public String getWhereFirstConditionMark() {
        return "#df:whereFirstCondition#";
    }

    public String getUnionSelectClauseMark() {
        return "#df:unionSelectClause#";
    }

    public String getUnionWhereClauseMark() {
        return "#df:unionWhereClause#";
    }

    public String getUnionWhereFirstConditionMark() {
        return "#df:unionWhereFirstCondition#";
    }

    // ===================================================================================
    //                                                                    Sub Query Indent
    //                                                                    ================
    public String resolveSubQueryBeginMark(String subQueryIdentity) {
        return getSubQueryIndentProcessor().resolveSubQueryBeginMark(subQueryIdentity);
    }

    public String resolveSubQueryEndMark(String subQueryIdentity) {
        return getSubQueryIndentProcessor().resolveSubQueryEndMark(subQueryIdentity);
    }

    public String processSubQueryIndent(String sql) {
        return processSubQueryIndent(sql, "", sql);
    }

    protected String processSubQueryIndent(String sql, String preIndent, String originalSql) {
        return getSubQueryIndentProcessor().processSubQueryIndent(sql, preIndent, originalSql);
    }

    protected SubQueryIndentProcessor getSubQueryIndentProcessor() {
        if (_subQueryIndentProcessor == null) {
            _subQueryIndentProcessor = new SubQueryIndentProcessor();
        }
        return _subQueryIndentProcessor;
    }

    // [DBFlute-0.7.4]
    // ===================================================================================
    //                                                                       Specification
    //                                                                       =============
    // -----------------------------------------------------
    //                                        Specify Column
    //                                        --------------
    public void specifySelectColumn(HpSpecifiedColumn specifiedColumn) {
        if (_specifiedSelectColumnMap == null) {
            _specifiedSelectColumnMap = StringKeyMap.createAsFlexible(); // not needs order
        }
        final String tableAliasName = specifiedColumn.getTableAliasName();
        if (!_specifiedSelectColumnMap.containsKey(tableAliasName)) {
            final Map<String, HpSpecifiedColumn> elementMap = StringKeyMap.createAsFlexibleOrdered();
            _specifiedSelectColumnMap.put(tableAliasName, elementMap);
        }
        final String columnDbName = specifiedColumn.getColumnDbName();
        final Map<String, HpSpecifiedColumn> elementMap = _specifiedSelectColumnMap.get(tableAliasName);
        elementMap.put(columnDbName, specifiedColumn);
    }

    public boolean hasSpecifiedSelectColumn(String tableAliasName) {
        return _specifiedSelectColumnMap != null && _specifiedSelectColumnMap.containsKey(tableAliasName);
    }

    public boolean hasSpecifiedSelectColumn(String tableAliasName, String columnDbName) {
        if (_specifiedSelectColumnMap == null) {
            return false;
        }
        final Map<String, HpSpecifiedColumn> elementMap = _specifiedSelectColumnMap.get(tableAliasName);
        if (elementMap == null) {
            return false;
        }
        return elementMap.containsKey(columnDbName);
    }

    public void backupSpecifiedSelectColumn() {
        _backupSpecifiedSelectColumnMap = _specifiedSelectColumnMap;
    }

    public void restoreSpecifiedSelectColumn() {
        _specifiedSelectColumnMap = _backupSpecifiedSelectColumnMap;
        _backupSpecifiedSelectColumnMap = null;
    }

    public void clearSpecifiedSelectColumn() {
        if (_specifiedSelectColumnMap != null) {
            _specifiedSelectColumnMap.clear();
            _specifiedSelectColumnMap = null;
        }
    }

    // -----------------------------------------------------
    //                                      Specified as One
    //                                      ----------------
    public String getSpecifiedColumnDbNameAsOne() {
        final ColumnInfo columnInfo = getSpecifiedColumnInfoAsOne();
        return columnInfo != null ? columnInfo.getColumnDbName() : null;
    }

    public ColumnInfo getSpecifiedColumnInfoAsOne() {
        final Map<String, HpSpecifiedColumn> elementMap = getSpecifiedColumnElementMapAsOne();
        if (elementMap != null && elementMap.size() == 1) {
            return elementMap.values().iterator().next().getColumnInfo();
        }
        return null;
    }

    public ColumnRealName getSpecifiedColumnRealNameAsOne() {
        final ColumnSqlName columnSqlName = getSpecifiedColumnSqlNameAsOne();
        if (columnSqlName != null) {
            return ColumnRealName.create(getSpecifiedColumnTableAliasNameAsOne(), columnSqlName);
        }
        return null;
    }

    public ColumnSqlName getSpecifiedColumnSqlNameAsOne() {
        final ColumnInfo columnInfo = getSpecifiedColumnInfoAsOne();
        return columnInfo != null ? columnInfo.getColumnSqlName() : null;
    }

    protected String getSpecifiedColumnTableAliasNameAsOne() {
        if (_specifiedSelectColumnMap != null && _specifiedSelectColumnMap.size() == 1) {
            return _specifiedSelectColumnMap.keySet().iterator().next();
        }
        return null;
    }

    protected Map<String, HpSpecifiedColumn> getSpecifiedColumnElementMapAsOne() {
        if (_specifiedSelectColumnMap != null && _specifiedSelectColumnMap.size() == 1) {
            return _specifiedSelectColumnMap.values().iterator().next();
        }
        return null;
    }

    // -----------------------------------------------------
    //                                      Specify Deriving
    //                                      ----------------
    public void specifyDerivingSubQuery(HpDerivingSubQueryInfo subQueryInfo) {
        if (_specifiedDerivingSubQueryMap == null) {
            _specifiedDerivingSubQueryMap = StringKeyMap.createAsFlexibleOrdered();
        }
        final String aliasName = subQueryInfo.getAliasName(); // null allowed (treated as null key)
        _specifiedDerivingSubQueryMap.put(aliasName, subQueryInfo);
    }

    public boolean hasSpecifiedDerivingSubQuery(String aliasName) {
        return _specifiedDerivingSubQueryMap != null && _specifiedDerivingSubQueryMap.containsKey(aliasName);
    }

    public List<String> getSpecifiedDerivingAliasList() {
        if (_specifiedDerivingSubQueryMap == null) {
            @SuppressWarnings("unchecked")
            final List<String> emptyList = Collections.EMPTY_LIST;
            return emptyList;
        }
        return new ArrayList<String>(_specifiedDerivingSubQueryMap.keySet());
    }

    public HpDerivingSubQueryInfo getSpecifiedDerivingInfo(String aliasName) {
        if (_specifiedDerivingSubQueryMap == null) {
            return null;
        }
        return _specifiedDerivingSubQueryMap.get(aliasName);
    }

    protected ColumnInfo getSpecifiedDerivingColumnInfo(HpDerivingSubQueryInfo derivingInfo) {
        final SqlClause subQuerySqlClause = derivingInfo.getDerivedReferrer().getSubQuerySqlClause();
        final ColumnInfo columnInfo = subQuerySqlClause.getSpecifiedColumnInfoAsOne();
        if (columnInfo != null) {
            return columnInfo;
        }
        return subQuerySqlClause.getSpecifiedDerivingColumnInfoAsOne(); // nested
    }

    // -----------------------------------------------------
    //                                       Deriving as One
    //                                       ---------------
    public ColumnInfo getSpecifiedDerivingColumnInfoAsOne() {
        final HpDerivingSubQueryInfo derivingInfo = getSpecifiedDerivingInfoAsOne();
        return derivingInfo != null ? getSpecifiedDerivingColumnInfo(derivingInfo) : null;
    }

    public String getSpecifiedDerivingAliasNameAsOne() {
        final HpDerivingSubQueryInfo derivingInfo = getSpecifiedDerivingInfoAsOne();
        return derivingInfo != null ? derivingInfo.getAliasName() : null;
    }

    public String getSpecifiedDerivingSubQueryAsOne() {
        final HpDerivingSubQueryInfo derivingInfo = getSpecifiedDerivingInfoAsOne();
        return derivingInfo != null ? derivingInfo.getDerivingSubQuery() : null;
    }

    protected HpDerivingSubQueryInfo getSpecifiedDerivingInfoAsOne() {
        if (_specifiedDerivingSubQueryMap != null && _specifiedDerivingSubQueryMap.size() == 1) {
            return _specifiedDerivingSubQueryMap.values().iterator().next();
        }
        return null;
    }

    public void clearSpecifiedDerivingSubQuery() {
        if (_specifiedDerivingSubQueryMap != null) {
            _specifiedDerivingSubQueryMap.clear();
            _specifiedDerivingSubQueryMap = null;
        }
    }

    // ===================================================================================
    //                                                                  Invalid Query Info
    //                                                                  ==================
    public boolean isEmptyStringQueryAllowed() {
        return _emptyStringQueryAllowed;
    }

    public void allowEmptyStringQuery() {
        _emptyStringQueryAllowed = true;
    }

    public boolean isInvalidQueryChecked() {
        return _invalidQueryChecked;
    }

    public void checkInvalidQuery() {
        _invalidQueryChecked = true;
    }

    /**
     * {@inheritDoc}
     */
    public List<HpInvalidQueryInfo> getInvalidQueryList() {
        return new ArrayList<HpInvalidQueryInfo>(doGetInvalidQueryList());
    }

    public void saveInvalidQuery(HpInvalidQueryInfo invalidQueryInfo) {
        doGetInvalidQueryList().add(invalidQueryInfo);
    }

    protected List<HpInvalidQueryInfo> doGetInvalidQueryList() {
        if (_invalidQueryList == null) {
            _invalidQueryList = new ArrayList<HpInvalidQueryInfo>();
        }
        return _invalidQueryList;
    }

    // ===================================================================================
    //                                                          Where Clause Simple Filter
    //                                                          ==========================
    public void addWhereClauseSimpleFilter(QueryClauseFilter whereClauseSimpleFilter) {
        if (_whereClauseSimpleFilterList == null) {
            _whereClauseSimpleFilterList = new ArrayList<QueryClauseFilter>();
        }
        _whereClauseSimpleFilterList.add(whereClauseSimpleFilter);
    }

    protected String filterWhereClauseSimply(String clauseElement) {
        if (_whereClauseSimpleFilterList == null || _whereClauseSimpleFilterList.isEmpty()) {
            return clauseElement;
        }
        for (final Iterator<QueryClauseFilter> ite = _whereClauseSimpleFilterList.iterator(); ite.hasNext();) {
            final QueryClauseFilter filter = ite.next();
            if (filter == null) {
                String msg = "The list of filter should not have null: _whereClauseSimpleFilterList="
                        + _whereClauseSimpleFilterList;
                throw new IllegalStateException(msg);
            }
            clauseElement = filter.filterClauseElement(clauseElement);
        }
        return clauseElement;
    }

    // ===================================================================================
    //                                                                        Query Update
    //                                                                        ============
    // -----------------------------------------------------
    //                                          Query Insert
    //                                          ------------
    public String getClauseQueryInsert(Map<String, String> fixedValueQueryExpMap, SqlClause resourceSqlClause) {
        // at first, this should be called (before on-query name handling)
        // because an on-query name of mapped info are set in this process
        final String resourceViewClause = resourceSqlClause.getClause();
        if (_specifiedSelectColumnMap == null) {
            String msg = "The specified columns for query-insert are required.";
            throw new IllegalConditionBeanOperationException(msg);
        }
        final Map<String, HpSpecifiedColumn> elementMap = _specifiedSelectColumnMap.get(getBasePointAliasName());
        if (elementMap == null || elementMap.isEmpty()) {
            String msg = "The specified columns of inserted table for query-insert are required.";
            throw new IllegalConditionBeanOperationException(msg);
        }
        final DBMeta dbmeta = getDBMeta();
        final StringBuilder intoSb = new StringBuilder();
        final StringBuilder selectSb = new StringBuilder();
        final String resourceAlias = "dfres";
        int index = 0;
        final List<ColumnInfo> columnInfoList = dbmeta.getColumnInfoList();
        for (ColumnInfo columnInfo : columnInfoList) {
            final String columnDbName = columnInfo.getColumnDbName();
            final HpSpecifiedColumn specifiedColumn = elementMap.get(columnDbName);
            final String onQueryName;
            if (specifiedColumn != null) {
                onQueryName = specifiedColumn.getValidMappedOnQueryName();
            } else if (fixedValueQueryExpMap.containsKey(columnDbName)) {
                final String fixedValueQueryExp = fixedValueQueryExpMap.get(columnDbName);
                if (fixedValueQueryExp != null) {
                    onQueryName = encryptIfNeeds(columnInfo, fixedValueQueryExp);
                } else {
                    // it uses null literal on query
                    // because the SQL analyzer blocks null parameters
                    // (the analyzer should do it for condition-bean)
                    onQueryName = "null";
                }
            } else {
                continue;
            }
            if (onQueryName == null || onQueryName.trim().length() == 0) { // no way
                String msg = "The on-query name for query-insert is required: " + specifiedColumn;
                throw new IllegalConditionBeanOperationException(msg);
            }
            final ColumnSqlName columnSqlName = columnInfo.getColumnSqlName();
            if (index > 0) {
                intoSb.append(", ");
                selectSb.append(", ");
            }
            intoSb.append(columnSqlName);
            if (specifiedColumn != null) {
                selectSb.append(resourceAlias).append(".");
            }
            selectSb.append(onQueryName);
            ++index;
        }
        final String subQueryIdentity = "queryInsertResource";
        final String subQueryBeginMark = resolveSubQueryBeginMark(subQueryIdentity);
        final String subQueryEndMark = resolveSubQueryEndMark(subQueryIdentity);
        final StringBuilder mainSb = new StringBuilder();
        mainSb.append("insert into ").append(dbmeta.getTableSqlName());
        mainSb.append(" (").append(intoSb).append(")").append(ln());
        mainSb.append("select ").append(selectSb).append(ln());
        mainSb.append("  from (").append(subQueryBeginMark).append(ln());
        mainSb.append(resourceViewClause).append(ln());
        mainSb.append("       ) ").append(resourceAlias).append(subQueryEndMark);
        final String sql = mainSb.toString();
        return processSubQueryIndent(sql);
    }

    // -----------------------------------------------------
    //                                          Query Update
    //                                          ------------
    public String getClauseQueryUpdate(Map<String, String> columnParameterMap) {
        if (columnParameterMap == null) {
            String msg = "The argument 'columnParameterMap' should not be null.";
            throw new IllegalArgumentException(msg);
        }
        if (columnParameterMap.isEmpty()) {
            return null;
        }
        final DBMeta dbmeta = getDBMeta();
        final StringBuilder sb = new StringBuilder();
        sb.append("update ").append(dbmeta.getTableSqlName());
        if (canUseQueryUpdateInScope(dbmeta)) {
            buildQueryUpdateInScopeClause(columnParameterMap, dbmeta, sb);
        } else { // direct (unsupported or compound primary keys)
            buildQueryUpdateDirectClause(columnParameterMap, dbmeta, sb);
        }
        return sb.toString();
    }

    protected boolean canUseQueryUpdateInScope(final DBMeta dbmeta) {
        return isUpdateSubQueryUseLocalTableSupported() && !dbmeta.hasCompoundPrimaryKey();
    }

    protected void buildQueryUpdateInScopeClause(Map<String, String> columnParameterMap, DBMeta dbmeta, StringBuilder sb) {
        if (columnParameterMap != null) {
            buildQueryUpdateSetClause(columnParameterMap, dbmeta, sb, null);
        }
        final ColumnSqlName primaryKeyName = dbmeta.getPrimaryUniqueInfo().getFirstColumn().getColumnSqlName();
        final String selectClause = "select " + getBasePointAliasName() + "." + primaryKeyName;
        String fromWhereClause = getClauseFromWhereWithUnionTemplate();
        // Replace template marks. These are very important!
        fromWhereClause = replace(fromWhereClause, getUnionSelectClauseMark(), selectClause);
        fromWhereClause = replace(fromWhereClause, getUnionWhereClauseMark(), "");
        fromWhereClause = replace(fromWhereClause, getUnionWhereFirstConditionMark(), "");
        final String subQuery = processSubQueryIndent(selectClause + " " + fromWhereClause);
        sb.append(ln());
        sb.append(" where ").append(primaryKeyName);
        sb.append(" in (").append(ln()).append(subQuery);
        if (!subQuery.endsWith(ln())) {
            sb.append(ln());
        }
        sb.append(")");
    }

    protected void buildQueryUpdateDirectClause(Map<String, String> columnParameterMap, DBMeta dbmeta, StringBuilder sb) {
        if (hasUnionQuery()) {
            throwQueryUpdateUnavailableFunctionException("union", dbmeta);
        }
        final String whereClause = processSubQueryIndent(getWhereClause());
        boolean useAlias = false;
        if (isUpdateTableAliasNameSupported()) {
            if (hasQueryUpdateSubQueryPossible(whereClause)) {
                useAlias = true;
            }
        } else {
            if (hasQueryUpdateSubQueryPossible(whereClause)) {
                throwQueryUpdateUnavailableFunctionException("sub-query", dbmeta);
            }
        }
        boolean directJoin = false;
        if (isUpdateDirectJoinSupported()) {
            if (hasOuterJoin()) {
                useAlias = true; // use alias forcedly if direct join
                directJoin = true;
            }
        } else { // queryDelete or direct join unsupported 
            if (hasOuterJoin()) {
                throwQueryUpdateUnavailableFunctionException("outer join", dbmeta);
            }
        }
        final String basePointAliasName = useAlias ? getBasePointAliasName() : null;
        if (useAlias) {
            sb.append(" ").append(basePointAliasName);
        }
        if (directJoin) {
            sb.append(getLeftOuterJoinClause());
        }
        if (columnParameterMap != null) {
            final String setClauseAliasName = useAlias ? basePointAliasName + "." : null;
            buildQueryUpdateSetClause(columnParameterMap, dbmeta, sb, setClauseAliasName);
        }
        if (Srl.is_Null_or_TrimmedEmpty(whereClause)) {
            return;
        }
        if (useAlias) {
            sb.append(whereClause);
        } else {
            sb.append(filterQueryUpdateBasePointAliasNameLocalUnsupported(whereClause));
        }
    }

    protected boolean hasQueryUpdateSubQueryPossible(String whereClause) {
        if (Srl.is_Null_or_TrimmedEmpty(whereClause)) {
            return false;
        }
        return whereClause.contains("exists (") || whereClause.contains("(select ");
    }

    protected void buildQueryUpdateSetClause(Map<String, String> columnParameterMap, DBMeta dbmeta, StringBuilder sb,
            String aliasName) {
        if (columnParameterMap == null) {
            String msg = "The argument 'columnParameterMap' should not be null.";
            throw new IllegalArgumentException(msg);
        }
        sb.append(ln());
        int index = 0;
        final int mapSize = columnParameterMap.size();
        for (Entry<String, String> entry : columnParameterMap.entrySet()) {
            final String columnName = entry.getKey();
            final String parameter = entry.getValue();
            final ColumnInfo columnInfo = dbmeta.findColumnInfo(columnName);
            final ColumnSqlName columnSqlName = columnInfo.getColumnSqlName();
            if (index == 0) {
                sb.append("   set ");
            } else {
                sb.append("     , ");
            }
            if (aliasName != null) {
                sb.append(aliasName);
            }
            sb.append(columnSqlName).append(" = ");
            sb.append(encryptIfNeeds(columnInfo, parameter));
            if (mapSize - 1 > index) { // non last loop
                sb.append(ln());
            }
            ++index;
        }
    }

    protected void throwQueryUpdateUnavailableFunctionException(String unavailableFunction, DBMeta dbmeta) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("The queryUpdate() or queryDelete() with " + unavailableFunction + " is unavailable.");
        br.addItem("Advice");
        br.addElement("Your DB does not support QueryUpdateInScope and UpdateDirectJoin");
        br.addElement("or the table has compound primary key");
        br.addItem("Table");
        br.addElement(dbmeta.getTableDbName());
        final String msg = br.buildExceptionMessage();
        throw new IllegalConditionBeanOperationException(msg);
    }

    protected String filterQueryUpdateBasePointAliasNameLocalUnsupported(String subQuery) {
        final String basePointAliasName = getBasePointAliasName();

        // remove table alias prefix for column
        subQuery = replace(subQuery, basePointAliasName + ".", "");

        // remove table alias definition
        final String tableAliasSymbol = " " + basePointAliasName;
        subQuery = replace(subQuery, tableAliasSymbol + " ", " ");
        subQuery = replace(subQuery, tableAliasSymbol + ln(), ln());
        if (subQuery.endsWith(tableAliasSymbol)) {
            subQuery = replace(subQuery, tableAliasSymbol, "");
        }
        return subQuery;
    }

    // -----------------------------------------------------
    //                                          Query Delete
    //                                          ------------
    public String getClauseQueryDelete() {
        final DBMeta dbmeta = getDBMeta();
        final StringBuilder sb = new StringBuilder();
        sb.append("delete from ").append(dbmeta.getTableSqlName());
        if (canUseQueryUpdateInScope(dbmeta)) {
            buildQueryUpdateInScopeClause(null, dbmeta, sb);
        } else { // direct (unsupported or compound primary keys)
            buildQueryUpdateDirectClause(null, dbmeta, sb);
        }
        return sb.toString();
    }

    // -----------------------------------------------------
    //                                             Supported
    //                                             ---------
    protected boolean isUpdateSubQueryUseLocalTableSupported() {
        return true; // almost supported
    }

    protected boolean isUpdateDirectJoinSupported() {
        // used only when QueryUpdateInScope unsupported
        // so not need to be strict setting
        // (but if this returns true, it should be UpdateTableAliasNameSupported)
        return false; // almost unsupported
    }

    protected boolean isUpdateTableAliasNameSupported() {
        // used only when QueryUpdateInScope unsupported
        // so not need to be strict setting
        return false; // almost unsupported? (unknown)
    }

    // [DBFlute-0.8.6]
    // ===================================================================================
    //                                                                  Select Clause Type
    //                                                                  ==================
    public void classifySelectClauseType(SelectClauseType selectClauseType) {
        changeSelectClauseType(selectClauseType);
    }

    protected void changeSelectClauseType(SelectClauseType selectClauseType) {
        savePreviousSelectClauseType();
        _selectClauseType = selectClauseType;
    }

    protected void savePreviousSelectClauseType() {
        _previousSelectClauseType = _selectClauseType;
    }

    public void rollbackSelectClauseType() {
        _selectClauseType = _previousSelectClauseType != null ? _previousSelectClauseType : DEFAULT_SELECT_CLAUSE_TYPE;
    }

    // [DBFlute-0.9.8.6]
    // ===================================================================================
    //                                                                  ColumnQuery Object
    //                                                                  ==================
    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getColumnQueryObjectMap() {
        return _columyQueryObjectMap;
    }

    /**
     * {@inheritDoc}
     */
    public String registerColumnQueryObjectToThemeList(String themeKey, Object addedValue) {
        if (_columyQueryObjectMap == null) {
            _columyQueryObjectMap = new LinkedHashMap<String, Object>();
        }
        final int listIndex = doAddValueToThemeList(themeKey, addedValue, _columyQueryObjectMap);
        return buildColumnQueryObjectBindExp(themeKey + ".get(" + listIndex + ")");
    }

    protected String buildColumnQueryObjectBindExp(String relativePath) {
        // no end mark because the path is continued
        return "/*pmb.conditionQuery.colQyCBMap." + relativePath + ".conditionQuery.";
    }

    // [DBFlute-0.9.8.6]
    // ===================================================================================
    //                                                               ManualOrder Parameter
    //                                                               =====================
    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getManualOrderParameterMap() {
        return _manualOrderParameterMap;
    }

    /**
     * {@inheritDoc}
     */
    public String registerManualOrderParameterToThemeList(String themeKey, Object addedValue) {
        if (_manualOrderParameterMap == null) {
            _manualOrderParameterMap = new LinkedHashMap<String, Object>();
        }
        final int listIndex = doAddValueToThemeList(themeKey, addedValue, _manualOrderParameterMap);
        return buildManualOrderParameterBindExp(themeKey + ".get(" + listIndex + ")");
    }

    protected String buildManualOrderParameterBindExp(String relativePath) {
        return "/*pmb.conditionQuery.mnuOdrPrmMap." + relativePath + "*/null";
    }

    // [DBFlute-0.9.8.2]
    // ===================================================================================
    //                                                                      Free Parameter
    //                                                                      ==============
    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getFreeParameterMap() {
        return _freeParameterMap;
    }

    /**
     * {@inheritDoc}
     */
    public String registerFreeParameterToThemeList(String themeKey, Object addedValue) {
        if (_freeParameterMap == null) {
            _freeParameterMap = new LinkedHashMap<String, Object>();
        }
        final int listIndex = doAddValueToThemeList(themeKey, addedValue, _freeParameterMap);
        return buildFreeParameterBindExp(themeKey + ".get(" + listIndex + ")");
    }

    protected String buildFreeParameterBindExp(String relativePath) {
        return "/*pmb.conditionQuery.freePrmMap." + relativePath + "*/null";
    }

    protected int doAddValueToThemeList(String themeKey, Object addedValue, Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<Object> valueList = (List<Object>) map.get(themeKey);
        if (valueList == null) {
            valueList = new ArrayList<Object>();
            map.put(themeKey, valueList);
        }
        final int listIndex = valueList.size();
        valueList.add(addedValue);
        return listIndex;
    }

    // [DBFlute-0.9.8.4]
    // ===================================================================================
    //                                                                       Geared Cipher
    //                                                                       =============
    public GearedCipherManager getGearedCipherManager() {
        return _gearedCipherManager;
    }

    public ColumnFunctionCipher findColumnFunctionCipher(ColumnInfo columnInfo) {
        if (_gearedCipherManager != null) {
            final ColumnFunctionCipher cipher = _gearedCipherManager.findColumnFunctionCipher(columnInfo);
            if (cipher != null) {
                return cipher;
            }
        }
        return null;
    }

    public void makeSelectColumnDecryptionEffective() { // after suppressSelectColumnDecryption()
        _suppressSelectColumnDecryption = false;
    }

    public void suppressSelectColumnDecryption() { // basically for queryInsert()
        _suppressSelectColumnDecryption = true;
    }

    protected String encryptIfNeeds(ColumnInfo columnInfo, String valueExp) {
        final ColumnFunctionCipher cipher = findColumnFunctionCipher(columnInfo);
        return cipher != null ? cipher.encrypt(valueExp) : valueExp;
    }

    protected String decryptSelectColumnIfNeeds(ColumnInfo columnInfo, String valueExp) {
        if (_suppressSelectColumnDecryption) {
            return valueExp;
        }
        final ColumnFunctionCipher cipher = findColumnFunctionCipher(columnInfo);
        return cipher != null ? cipher.decrypt(valueExp) : valueExp;
    }

    // [DBFlute-0.9.8.4]
    // ===================================================================================
    //                                                                 ScalarSelect Option
    //                                                                 ===================
    public void acceptScalarSelectOption(ScalarSelectOption option) {
        if (option != null) {
            option.xjudgeDatabase(this);
        }
        _scalarSelectOption = option;
    }

    // [DBFlute-0.9.8.8]
    // ===================================================================================
    //                                                                       Paging Select
    //                                                                       =============
    public void makePagingAdjustmentEffective() {
        _pagingAdjustment = true;
    }

    public void ignorePagingAdjustment() {
        _pagingAdjustment = false;
    }

    // -----------------------------------------------------
    //                                           Count Later
    //                                           -----------
    public void enablePagingCountLater() {
        _pagingCountLater = true;
    }

    public void disablePagingCountLater() {
        _pagingCountLater = false;
    }

    protected boolean canPagingCountLater() {
        return _pagingAdjustment && _pagingCountLater;
    }

    // -----------------------------------------------------
    //                                       Count LeastJoin
    //                                       ---------------
    public void enablePagingCountLeastJoin() {
        _pagingCountLeastJoin = true;
    }

    public void disablePagingCountLeastJoin() {
        _pagingCountLeastJoin = false;
    }

    protected boolean canPagingCountLeastJoin() {
        return _pagingAdjustment && _pagingCountLeastJoin;
    }

    // [DBFlute-0.9.9.4C]
    // ===================================================================================
    //                                                                      Lazy Reflector
    //                                                                      ==============
    public void registerClauseLazyReflector(ClauseLazyReflector clauseLazyReflector) {
        if (_clauseLazyReflectorList == null) {
            _clauseLazyReflectorList = new ArrayList<ClauseLazyReflector>();
        }
        _clauseLazyReflectorList.add(clauseLazyReflector);
    }

    protected void reflectClauseLazilyIfExists() {
        if (_clauseLazyReflectorList == null) {
            return;
        }
        for (ClauseLazyReflector reflector : _clauseLazyReflectorList) {
            reflector.reflect();
        }
        _clauseLazyReflectorList.clear();
    }

    // [DBFlute-0.9.7.2]
    // ===================================================================================
    //                                                                        Purpose Type
    //                                                                        ============
    public HpCBPurpose getPurpose() {
        return _purpose;
    }

    public void setPurpose(HpCBPurpose purpose) {
        _purpose = purpose;
    }

    // [DBFlute-0.9.4]
    // ===================================================================================
    //                                                                       InScope Limit
    //                                                                       =============
    public int getInScopeLimit() {
        return 0; // as default
    }

    // [DBFlute-0.9.8.4]
    // ===================================================================================
    //                                                                   LikeSearch Escape
    //                                                                   =================
    public void adjustLikeSearchDBWay(LikeSearchOption option) {
        final DBWay dbway = dbway();
        option.acceptOriginalWildCardList(dbway.getOriginalWildCardList());
        option.acceptStringConnector(dbway.getStringConnector());
    }

    // ===================================================================================
    //                                                                       DBMeta Helper
    //                                                                       =============
    protected DBMeta getDBMeta() {
        if (_dbmeta == null) {
            String msg = "The DB meta of local table should not be null when using getDBMeta():";
            msg = msg + " tableDbName=" + _tableDbName;
            throw new IllegalStateException(msg);
        }
        return _dbmeta;
    }

    protected DBMeta findDBMeta(String tableDbName) {
        DBMeta dbmeta = getCachedDBMetaMap().get(tableDbName);
        if (dbmeta != null) {
            return dbmeta;
        }
        if (_dbmetaProvider == null) {
            String msg = "The DB meta provider should not be null when using findDBMeta():";
            msg = msg + " tableDbName=" + tableDbName;
            throw new IllegalStateException(msg);
        }
        dbmeta = _dbmetaProvider.provideDBMetaChecked(tableDbName);
        getCachedDBMetaMap().put(tableDbName, dbmeta);
        return dbmeta;
    }

    protected Map<String, DBMeta> getCachedDBMetaMap() {
        if (_cachedDBMetaMap == null) {
            _cachedDBMetaMap = StringKeyMap.createAsFlexible();
        }
        return _cachedDBMetaMap;
    }

    protected ColumnInfo toColumnInfo(String tableDbName, String columnDbName) {
        return findDBMeta(tableDbName).findColumnInfo(columnDbName);
    }

    protected ColumnSqlName toColumnSqlName(String tableDbName, String columnDbName) {
        return toColumnInfo(tableDbName, columnDbName).getColumnSqlName();
    }

    // ===================================================================================
    //                                                                        Space Helper
    //                                                                        ============
    protected String buildSpaceBar(int size) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // ===================================================================================
    //                                                                      General Helper
    //                                                                      ==============
    protected String replace(String text, String fromText, String toText) {
        return Srl.replace(text, fromText, toText);
    }

    protected String ln() {
        return DBFluteSystem.getBasicLn();
    }

    // -----------------------------------------------------
    //                                         Assert Object
    //                                         -------------
    protected void assertObjectNotNull(String variableName, Object value) {
        DfAssertUtil.assertObjectNotNull(variableName, value);
    }

    // -----------------------------------------------------
    //                                         Assert String
    //                                         -------------
    protected void assertStringNotNullAndNotTrimmedEmpty(String variableName, String value) {
        DfAssertUtil.assertStringNotNullAndNotTrimmedEmpty(variableName, value);
    }
}
