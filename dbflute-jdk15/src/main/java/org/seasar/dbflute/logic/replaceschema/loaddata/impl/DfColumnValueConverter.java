package org.seasar.dbflute.logic.replaceschema.loaddata.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.seasar.dbflute.logic.jdbc.metadata.info.DfColumnMeta;
import org.seasar.dbflute.logic.replaceschema.loaddata.DfColumnBindTypeProvider;
import org.seasar.dbflute.util.DfNameHintUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;

public class DfColumnValueConverter {

    protected final Map<String, Map<String, String>> _convertValueMap;
    protected final Map<String, String> _defaultValueMap;
    protected final DfColumnBindTypeProvider _bindTypeProvider;
    protected Map<String, String> _allColumnConvertMap; // derived lazily

    public DfColumnValueConverter(Map<String, Map<String, String>> convertValueMap,
            Map<String, String> defaultValueMap, DfColumnBindTypeProvider bindTypeProvider) {
        _convertValueMap = convertValueMap;
        _defaultValueMap = defaultValueMap;
        _bindTypeProvider = bindTypeProvider;
    }

    public Map<String, Object> convert(String tableName, Map<String, Object> columnValueMap,
            Map<String, DfColumnMeta> columnMetaMap) {
        final Map<String, Object> resolvedColumnValueMap = new LinkedHashMap<String, Object>();
        final Set<Entry<String, Object>> entrySet = columnValueMap.entrySet();
        final Set<String> convertedSet = new HashSet<String>(1);
        for (Entry<String, Object> entry : entrySet) {
            final String columnName = entry.getKey();
            final Object plainValue = entry.getValue();
            Object resolvedValue = resolveConvertValue(tableName, columnName, plainValue, convertedSet, columnMetaMap);
            if (convertedSet.isEmpty()) { // if no convert
                resolvedValue = filterEmptyAsNull(resolvedValue); // treated as null if empty string
                resolvedValue = resolveDefaultValue(columnName, resolvedValue);
            } else {
                convertedSet.clear(); // recycle
            }
            resolvedColumnValueMap.put(columnName, resolvedValue);
        }
        return resolvedColumnValueMap;
    }

    protected Object filterEmptyAsNull(Object value) {
        if (value instanceof String && Srl.isEmpty((String) value)) {
            return null;
        }
        return value;
    }

    // ===================================================================================
    //                                                                       Convert Value
    //                                                                       =============
    protected Object resolveConvertValue(String tableName, String columnName, Object plainValue,
            Set<String> convertedSet, Map<String, DfColumnMeta> columnMetaMap) {
        if (_convertValueMap == null || _convertValueMap.isEmpty()) {
            return plainValue;
        }
        final Map<String, String> valueMapping = findConvertValueMapping(columnName);
        if (valueMapping == null || valueMapping.isEmpty()) {
            return plainValue;
        }
        String filteredValue = prepareStringPlainValue(plainValue);
        boolean converted = false;
        final String containMark = DfNameHintUtil.CONTAIN_MARK;
        for (Entry<String, String> entry : valueMapping.entrySet()) {
            final String before = entry.getKey();
            final String after = resolveVariable(entry.getValue());

            final String typed = processType(tableName, columnName, columnMetaMap, filteredValue, before, after);
            if (typed != null) {
                filteredValue = typed;
                converted = true;
                continue;
            }

            if (Srl.startsWithIgnoreCase(before, containMark)) {
                final String realBefore = resolveVariable(Srl.substringFirstRear(before, containMark));
                if (filteredValue != null && filteredValue.contains(realBefore)) {
                    filteredValue = Srl.replace(filteredValue, realBefore, (after != null ? after : ""));
                    converted = true;
                }
            } else {
                final String realBefore = resolveVariable(before);
                if (filteredValue != null && filteredValue.equals(realBefore)) {
                    filteredValue = after;
                    converted = true;
                } else if (filteredValue == null && realBefore == null) {
                    filteredValue = after;
                    converted = true;
                }
            }
        }
        if (converted) {
            convertedSet.add("converted");
            return filteredValue;
        } else {
            return plainValue;
        }
    }

    protected String prepareStringPlainValue(Object plainValue) {
        if (plainValue == null) {
            return null;
        }
        if (plainValue instanceof Time) {
            return DfTypeUtil.toString(plainValue, "HH:mm:ss");
        } else if (plainValue instanceof Date) {
            return DfTypeUtil.toString(plainValue, "yyyy-MM-dd HH:mm:ss.SSS");
        }
        return plainValue.toString();
    }

    protected String processType(String tableName, String columnName, Map<String, DfColumnMeta> columnMetaMap,
            String filteredValue, String before, String after) {
        String processed = null;

        processed = processTimestamp(tableName, columnName, columnMetaMap, filteredValue, before, after);
        if (processed != null) {
            return processed;
        }

        return null;
    }

    protected String processTimestamp(String tableName, String columnName, Map<String, DfColumnMeta> columnMetaMap,
            String filteredValue, String before, String after) {
        if (!"$$timestamp$$".equalsIgnoreCase(before)) {
            return null;
        }
        final DfColumnMeta columnMeta = columnMetaMap.get(columnName);
        final Class<?> boundType = _bindTypeProvider.provideBindType(tableName, columnMeta);
        if (!Timestamp.class.isAssignableFrom(boundType)) {
            return null;
        }
        // process target here
        if (after.equalsIgnoreCase("$$ZeroPrefixMillis$$")) { // DBFlute default
            if (filteredValue != null && filteredValue.contains(".")) {
                final String front = Srl.substringLastFront(filteredValue, ".");
                final String millis = Srl.substringLastRear(filteredValue, ".");
                if (millis.length() == 1) {
                    filteredValue = front + ".00" + millis;
                } else if (millis.length() == 2) {
                    filteredValue = front + ".0" + millis;
                }
                return filteredValue; // processed
            }
        } else if (after.equalsIgnoreCase("$$ZeroSuffixMillis$$")) {
            if (filteredValue != null && filteredValue.contains(".")) {
                final String millis = Srl.substringLastRear(filteredValue, ".");
                if (millis.length() == 1) {
                    filteredValue = filteredValue + "00";
                } else if (millis.length() == 2) {
                    filteredValue = filteredValue + "0";
                }
                return filteredValue; // processed
            }
        }
        return null; // no converted
    }

    protected String resolveVariable(String value) {
        if ("$$empty$$".equalsIgnoreCase(value)) {
            return "";
        }
        if ("$$null$$".equalsIgnoreCase(value)) {
            return null;
        }
        return value;
    }

    protected Map<String, String> findConvertValueMapping(String columnName) {
        if (_allColumnConvertMap == null) { // initialize
            _allColumnConvertMap = _convertValueMap.get("$$ALL$$");
            if (_allColumnConvertMap == null) {
                _allColumnConvertMap = new HashMap<String, String>();
            }
        }
        // convertValueMap should be case insensitive (or flexible) map
        // (must be already resolved here)
        final Map<String, String> resultMap = _convertValueMap.get(columnName);
        if (resultMap != null && !resultMap.isEmpty()) {
            if (!_allColumnConvertMap.isEmpty()) {
                final Map<String, String> mergedMap = new HashMap<String, String>();
                mergedMap.putAll(_allColumnConvertMap);
                mergedMap.putAll(resultMap); // override if same value
                return mergedMap;
            } else {
                return resultMap;
            }
        } else {
            return !_allColumnConvertMap.isEmpty() ? _allColumnConvertMap : null;
        }
    }

    // ===================================================================================
    //                                                                       Default Value
    //                                                                       =============
    protected Object resolveDefaultValue(String columnName, Object plainValue) {
        if (_defaultValueMap == null || _defaultValueMap.isEmpty()) {
            return plainValue;
        }
        if (plainValue != null) {
            // empty string has already been resolved here
            return plainValue;
        }
        if (!hasDefaultValue(columnName)) {
            return plainValue;
        }
        Object resolvedValue = plainValue;
        final String defaultValue = findDefaultValue(columnName);
        if (Srl.is_Null_or_Empty(defaultValue)) {
            return null; // empty is treated as null
        }
        if (defaultValue.equalsIgnoreCase("sysdate")) {
            resolvedValue = new Timestamp(System.currentTimeMillis());
        } else {
            resolvedValue = defaultValue;
        }
        return resolvedValue;
    }

    private boolean hasDefaultValue(String columnName) {
        return _defaultValueMap.containsKey(columnName);
    }

    private String findDefaultValue(String columnName) {
        // defaultValueMap should be case insensitive (or flexible) map
        // (must be already resolved here)
        return _defaultValueMap.get(columnName);
    }
}
