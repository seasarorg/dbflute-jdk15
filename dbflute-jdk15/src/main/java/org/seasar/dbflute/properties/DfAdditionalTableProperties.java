package org.seasar.dbflute.properties;

import java.util.Map;
import java.util.Properties;

/**
 * @author jflute
 * @since 0.8.0 (2008/09/20 Saturday)
 */
public final class DfAdditionalTableProperties extends DfAbstractHelperProperties {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor.
     * @param prop Properties. (NotNull)
     */
    public DfAdditionalTableProperties(Properties prop) {
        super(prop);
    }

    // ===================================================================================
    //                                                                  additionalTableMap
    //                                                                  ==================
    // # map: {
    // #     [table-name] = map:{
    // #         columnMap = map:{
    // #             [column-name] = map:{
    // #                 type = [column JDBC type] ; dbType = [column DB type]
    // #                 ; required = [true or false] ; size = [column size: e.g. 60 / 5, 3]
    // #                 ; primaryKey = [true or false] ; pkName = [PK constraint name]
    // #                 ; autoIncrement = [true or false]
    // #                 ; default = [default value] ; comment = [column comment]
    // #             }
    // #         }
    // #         ; comment = [table comment]
    // #     }
    // # }
    public static final String KEY_additionalTableMap = "additionalTableMap";
    protected Map<String, Object> _additionalTableMap;

    public Map<String, Object> getAdditionalTableMap() {
        if (_additionalTableMap == null) {
            _additionalTableMap = mapProp("torque." + KEY_additionalTableMap, DEFAULT_EMPTY_MAP);
        }
        return _additionalTableMap;
    }

    // ===================================================================================
    //                                                                      Finding Helper
    //                                                                      ==============
    @SuppressWarnings("unchecked")
    public String findTableComment(String tableName) {
        final Map<String, Object> componentMap = (Map<String, Object>) getAdditionalTableMap().get(tableName);
        return (String) componentMap.get("comment");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, String>> findColumnMap(String tableName) {
        final Map<String, Object> componentMap = (Map<String, Object>) getAdditionalTableMap().get(tableName);
        return (Map<String, Map<String, String>>) componentMap.get("columnMap");
    }

    public String findColumnType(String tableName, String columnName) { // required
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("type");
    }

    public String findColumnDbType(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("dbType");
    }

    public String findColumnSize(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("size");
    }

    public boolean isColumnRequired(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return "true".equalsIgnoreCase(elementMap.get("required"));
    }

    public boolean isColumnPrimaryKey(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return "true".equalsIgnoreCase(elementMap.get("primaryKey"));
    }

    public String findColumnPKName(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("pkName");
    }

    public boolean isColumnAutoIncrement(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return "true".equalsIgnoreCase(elementMap.get("autoIncrement"));
    }

    public String findColumnDefault(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("default");
    }

    public String findColumnComment(String tableName, String columnName) {
        final Map<String, Map<String, String>> columnMap = findColumnMap(tableName);
        final Map<String, String> elementMap = columnMap.get(columnName);
        return elementMap.get("comment");
    }
}