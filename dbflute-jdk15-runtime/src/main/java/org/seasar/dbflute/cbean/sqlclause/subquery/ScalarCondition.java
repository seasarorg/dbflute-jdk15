package org.seasar.dbflute.cbean.sqlclause.subquery;

import org.seasar.dbflute.cbean.cipher.GearedCipherManager;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.dbflute.dbmeta.name.ColumnRealName;
import org.seasar.dbflute.dbmeta.name.ColumnRealNameProvider;
import org.seasar.dbflute.dbmeta.name.ColumnSqlName;
import org.seasar.dbflute.dbmeta.name.ColumnSqlNameProvider;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;

/**
 * @author jflute
 * @since 0.9.7.2 (2010/06/20 Sunday)
 */
public class ScalarCondition extends AbstractSubQuery {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String _mainSubQueryIdentity; // NotNull
    protected final String _operand; // NotNull
    protected final PartitionByProvider _partitionByProvider; // NotNull

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ScalarCondition(SubQueryPath subQueryPath, ColumnRealNameProvider localRealNameProvider,
            ColumnSqlNameProvider subQuerySqlNameProvider, int subQueryLevel, SqlClause subQuerySqlClause,
            String subQueryIdentity, DBMeta subQueryDBMeta, GearedCipherManager cipherManager,
            String mainSubQueryIdentity, String operand, PartitionByProvider partitionByProvider) {
        super(subQueryPath, localRealNameProvider, subQuerySqlNameProvider, subQueryLevel, subQuerySqlClause,
                subQueryIdentity, subQueryDBMeta, cipherManager);
        _mainSubQueryIdentity = mainSubQueryIdentity;
        _operand = operand;
        _partitionByProvider = partitionByProvider;
    }

    public static interface PartitionByProvider {
        SqlClause provideSqlClause();
    }

    // ===================================================================================
    //                                                                        Build Clause
    //                                                                        ============
    public String buildScalarCondition(String function) {
        // Get the specified column before it disappears at sub-query making.
        final ColumnRealName columnRealName;
        {
            final String columnDbName = _subQuerySqlClause.getSpecifiedColumnDbNameAsOne();
            if (columnDbName == null || columnDbName.trim().length() == 0) {
                throwScalarConditionInvalidColumnSpecificationException(function);
            }
            columnRealName = _localRealNameProvider.provide(columnDbName);
        }

        final String subQueryClause = getSubQueryClause(function);
        final String beginMark = resolveSubQueryBeginMark(_subQueryIdentity) + ln();
        final String endMark = resolveSubQueryEndMark(_subQueryIdentity);
        final String endIndent = "       ";
        final ColumnInfo columnInfo = _subQuerySqlClause.getSpecifiedColumnInfoAsOne();
        final String specifiedExp = decrypt(columnInfo, columnRealName.toString());
        return specifiedExp + " " + _operand // left and operand
                + " (" + beginMark + subQueryClause + ln() + endIndent + ") " + endMark; // right
    }

    protected String getSubQueryClause(String function) {
        if (!_subQueryDBMeta.hasPrimaryKey() || _subQueryDBMeta.hasCompoundPrimaryKey()) {
            String msg = "The scalar-condition is unsupported when no primary key or compound primary key:";
            msg = msg + " table=" + _subQueryDBMeta.getTableDbName();
            throw new IllegalConditionBeanOperationException(msg);
        }
        final String tableAliasName = getSubQueryLocalAliasName();
        final String derivedColumnDbName = _subQuerySqlClause.getSpecifiedColumnDbNameAsOne();
        if (derivedColumnDbName == null) {
            throwScalarConditionInvalidColumnSpecificationException(function);
        }
        final ColumnSqlName derivedColumnSqlName = _subQuerySqlClause.getSpecifiedColumnSqlNameAsOne();
        final ColumnRealName derivedColumnRealName = ColumnRealName.create(tableAliasName, derivedColumnSqlName);
        assertScalarConditionColumnType(function, derivedColumnDbName);
        ColumnRealName partitionByCorrelatedColumnRealName = null;
        ColumnSqlName partitionByRelatedColumnSqlName = null;
        final SqlClause partitionBySqlClause = _partitionByProvider.provideSqlClause();
        if (partitionBySqlClause != null) {
            final String partitionByColumnDbName = partitionBySqlClause.getSpecifiedColumnDbNameAsOne();
            if (partitionByColumnDbName == null) { // means empty specify or duplicate specify
                throwScalarConditionPartitionByInvalidColumnSpecificationException(function);
            }
            partitionByCorrelatedColumnRealName = _localRealNameProvider.provide(partitionByColumnDbName);
            partitionByRelatedColumnSqlName = _subQuerySqlNameProvider.provide(partitionByColumnDbName);
        }
        final String subQueryClause;
        if (_subQuerySqlClause.hasUnionQuery()) {
            subQueryClause = getUnionSubQuerySql(function, tableAliasName, derivedColumnSqlName, derivedColumnRealName,
                    partitionByCorrelatedColumnRealName, partitionByRelatedColumnSqlName);
        } else {
            final ColumnInfo columnInfo = _subQuerySqlClause.getSpecifiedColumnInfoAsOne();
            final String specifiedExp = decrypt(columnInfo, derivedColumnRealName.toString());
            final String selectClause = "select " + function + "(" + specifiedExp + ")";
            final String fromWhereClause = buildFromWhereClause(selectClause, tableAliasName,
                    partitionByCorrelatedColumnRealName, partitionByRelatedColumnSqlName);
            subQueryClause = selectClause + " " + fromWhereClause;
        }
        return resolveSubQueryLevelVariable(subQueryClause);
    }

    protected String getUnionSubQuerySql(String function, String tableAliasName // basic
            , ColumnSqlName derivedColumnSqlName, ColumnRealName derivedColumnRealName // derived
            , ColumnRealName partitionByCorrelatedColumnRealName, ColumnSqlName partitionByRelatedColumnSqlName) { // partition-by
        final String beginMark = resolveSubQueryBeginMark(_mainSubQueryIdentity) + ln();
        final String endMark = resolveSubQueryEndMark(_mainSubQueryIdentity);
        final String mainSql;
        {
            final ColumnSqlName pkSqlName = _subQueryDBMeta.getPrimaryUniqueInfo().getFirstColumn().getColumnSqlName();
            final ColumnRealName pkRealName = ColumnRealName.create(tableAliasName, pkSqlName);
            final String selectClause = "select " + pkRealName + ", " + derivedColumnRealName;
            final String fromWhereClause = buildFromWhereClause(selectClause, tableAliasName,
                    partitionByCorrelatedColumnRealName, partitionByRelatedColumnSqlName);
            mainSql = selectClause + " " + fromWhereClause;
        }
        final String mainAlias = buildSubQueryMainAliasName();
        final ColumnRealName mainDerivedColumnRealName = ColumnRealName.create(mainAlias, derivedColumnSqlName);
        final ColumnInfo columnInfo = _subQuerySqlClause.getSpecifiedColumnInfoAsOne();
        final String specifiedExp = decrypt(columnInfo, mainDerivedColumnRealName.toString());
        return "select " + function + "(" + specifiedExp + ")" + ln() // select
                + "  from (" + beginMark + mainSql + ln() + "       ) " + mainAlias + endMark; // from
    }

    protected String buildFromWhereClause(String selectClause, String tableAliasName,
            ColumnRealName partitionByCorrelatedColumnRealName, ColumnSqlName partitionByRelatedColumnSqlName) {
        final String fromWhereClause;
        if (partitionByCorrelatedColumnRealName != null) {
            fromWhereClause = buildCorrelationFromWhereClause(selectClause, tableAliasName,
                    partitionByCorrelatedColumnRealName, partitionByRelatedColumnSqlName);
        } else {
            fromWhereClause = buildPlainFromWhereClause(selectClause, tableAliasName);
        }
        return fromWhereClause;
    }

    protected void throwScalarConditionInvalidColumnSpecificationException(String function) {
        createCBExThrower().throwScalarConditionInvalidColumnSpecificationException(function);
    }

    protected void throwScalarConditionPartitionByInvalidColumnSpecificationException(String function) {
        createCBExThrower().throwScalarConditionPartitionByInvalidColumnSpecificationException(function);
    }

    protected void assertScalarConditionColumnType(String function, String derivedColumnDbName) {
        final Class<?> deriveColumnType = _subQueryDBMeta.findColumnInfo(derivedColumnDbName).getPropertyType();
        if ("sum".equalsIgnoreCase(function) || "avg".equalsIgnoreCase(function)) {
            if (!Number.class.isAssignableFrom(deriveColumnType)) {
                throwScalarConditionUnmatchedColumnTypeException(function, derivedColumnDbName, deriveColumnType);
            }
        }
    }

    protected void throwScalarConditionUnmatchedColumnTypeException(String function, String derivedColumnDbName,
            Class<?> derivedColumnType) {
        createCBExThrower().throwScalarConditionUnmatchedColumnTypeException(function, derivedColumnDbName,
                derivedColumnType);
    }
}
