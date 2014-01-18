package org.seasar.dbflute.logic.replaceschema.loaddata.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.dbflute.DfBuildProperties;
import org.seasar.dbflute.exception.DfDelimiterDataRegistrationFailureException;
import org.seasar.dbflute.logic.jdbc.metadata.info.DfColumnMeta;
import org.seasar.dbflute.logic.replaceschema.loaddata.DfColumnBindTypeProvider;
import org.seasar.dbflute.properties.DfLittleAdjustmentProperties;

/**
 * @author jflute
 */
public class DfDelimiterDataWriteSqlBuilder {

    //====================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String _tableDbName;
    protected Map<String, DfColumnMeta> _columnMetaMap;
    protected List<String> _columnNameList;
    protected List<String> _valueList;
    protected Map<String, Set<String>> _notFoundColumnMap;
    protected Map<String, Map<String, String>> _convertValueMap;
    protected Map<String, String> _defaultValueMap;
    protected DfColumnBindTypeProvider _bindTypeProvider;
    protected Map<String, String> _basicColumnValueMap;
    protected Map<String, String> _allColumnConvertMap;

    // ===================================================================================
    //                                                                           Build SQL
    //                                                                           =========
    public String buildSql() {
        final Map<String, String> columnValueMap = createBasicColumnValueMap();
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sbValues = new StringBuilder();
        for (String columnDbName : columnValueMap.keySet()) {
            final String columnSqlName = quoteColumnNameIfNeeds(columnDbName);
            sb.append(", ").append(columnSqlName);
            sbValues.append(", ?");
        }
        final String tableSqlName = quoteTableNameIfNeeds(_tableDbName);
        sb.delete(0, ", ".length()).insert(0, "insert into " + tableSqlName + " (").append(")");
        sbValues.delete(0, ", ".length()).insert(0, " values(").append(")");
        sb.append(sbValues);
        return sb.toString();
    }

    public Map<String, Object> setupParameter() {
        return convertColumnValue(createBasicColumnValueMap());
    }

    protected Map<String, Object> convertColumnValue(Map<String, String> columnValueMap) {
        final DfColumnValueConverter converter = new DfColumnValueConverter(_convertValueMap, _defaultValueMap,
                _bindTypeProvider);
        @SuppressWarnings("unchecked")
        final Map<String, Object> castMap = (Map<String, Object>) ((Object) columnValueMap); // Oops
        return converter.convert(_tableDbName, castMap, _columnMetaMap);
    }

    protected String quoteTableNameIfNeeds(String tableDbName) {
        final DfLittleAdjustmentProperties prop = DfBuildProperties.getInstance().getLittleAdjustmentProperties();
        return prop.quoteTableNameIfNeedsDirectUse(tableDbName);
    }

    protected String quoteColumnNameIfNeeds(String columnDbName) {
        final DfLittleAdjustmentProperties prop = DfBuildProperties.getInstance().getLittleAdjustmentProperties();
        return prop.quoteColumnNameIfNeedsDirectUse(columnDbName);
    }

    // ===================================================================================
    //                                                                           SQL Parts
    //                                                                           =========
    protected Map<String, String> createBasicColumnValueMap() {
        if (_basicColumnValueMap != null) {
            return _basicColumnValueMap;
        }
        _basicColumnValueMap = new LinkedHashMap<String, String>();
        int columnCount = -1;
        for (String columnName : _columnNameList) {
            columnCount++;
            if (!_columnMetaMap.isEmpty() && !_columnMetaMap.containsKey(columnName)) {
                if (hasDefaultValue(columnName)) {
                    continue;
                }
                Set<String> notFoundColumnSet = _notFoundColumnMap.get(_tableDbName);
                if (notFoundColumnSet == null) {
                    notFoundColumnSet = new LinkedHashSet<String>();
                    _notFoundColumnMap.put(_tableDbName, notFoundColumnSet);
                }
                notFoundColumnSet.add(columnName);
                continue;
            }
            final String value;
            try {
                if (columnCount < _valueList.size()) {
                    value = _valueList.get(columnCount);
                } else {
                    value = null;
                }
            } catch (RuntimeException e) {
                String msg = "valueList.get(columnCount) threw the exception:";
                msg = msg + " tableName=" + _tableDbName + " columnNameList=" + _columnNameList;
                msg = msg + " valueList=" + _valueList + " columnCount=" + columnCount;
                throw new DfDelimiterDataRegistrationFailureException(msg, e);
            }
            if (!_columnMetaMap.isEmpty() && _columnMetaMap.containsKey(columnName)) {
                String realDbName = _columnMetaMap.get(columnName).getColumnName();
                _basicColumnValueMap.put(realDbName, value);
            } else {
                _basicColumnValueMap.put(columnName, value);
            }
        }
        return _basicColumnValueMap;
    }

    // ===================================================================================
    //                                                                       Default Value
    //                                                                       =============
    private boolean hasDefaultValue(String columnName) {
        return _defaultValueMap.containsKey(columnName);
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Map<String, DfColumnMeta> getColumnMetaMap() {
        return _columnMetaMap;
    }

    public void setColumnInfoMap(Map<String, DfColumnMeta> columnMetaMap) {
        this._columnMetaMap = columnMetaMap;
    }

    public List<String> getColumnNameList() {
        return _columnNameList;
    }

    public void setColumnNameList(List<String> columnNameList) {
        this._columnNameList = columnNameList;
    }

    public Map<String, Set<String>> getNotFoundColumnMap() {
        return _notFoundColumnMap;
    }

    public void setNotFoundColumnMap(Map<String, Set<String>> notFoundColumnMap) {
        this._notFoundColumnMap = notFoundColumnMap;
    }

    public String getTableDbName() {
        return _tableDbName;
    }

    public void setTableDbName(String tableDbName) {
        this._tableDbName = tableDbName;
    }

    public List<String> getValueList() {
        return _valueList;
    }

    public void setValueList(List<String> valueList) {
        this._valueList = valueList;
    }

    public Map<String, Map<String, String>> getConvertValueMap() {
        return _convertValueMap;
    }

    public void setConvertValueMap(Map<String, Map<String, String>> convertValueMap) {
        this._convertValueMap = convertValueMap;
    }

    public Map<String, String> getDefaultValueMap() {
        return _defaultValueMap;
    }

    public void setDefaultValueMap(Map<String, String> defaultValueMap) {
        this._defaultValueMap = defaultValueMap;
    }

    public DfColumnBindTypeProvider getBindTypeProvider() {
        return _bindTypeProvider;
    }

    public void setBindTypeProvider(DfColumnBindTypeProvider bindTypeProvider) {
        this._bindTypeProvider = bindTypeProvider;
    }
}
