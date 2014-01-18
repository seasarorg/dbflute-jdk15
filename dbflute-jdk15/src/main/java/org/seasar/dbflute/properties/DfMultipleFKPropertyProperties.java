package org.seasar.dbflute.properties;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.seasar.dbflute.helper.StringKeyMap;

/**
 * @author jflute
 */
public final class DfMultipleFKPropertyProperties extends DfAbstractHelperProperties {
    // /- - - - - - - - - - - - - - - - - - - 
    // It's closet until it becomes to need!
    // - - - - - - - - - -/

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfMultipleFKPropertyProperties(Properties prop) {
        super(prop);
    }

    // ===================================================================================
    //                                                                 MultipleFK Property
    //                                                                 ===================
    // map:{
    //     ; [tableName] = map:{
    //         ; [columnName]/[columnName] = map:{
    //             ; columnAliasName = [aliasName]
    //         }
    //     }
    // }
    public static final String KEY_multipleFKPropertyMap = "multipleFKPropertyMap";
    protected Map<String, Map<String, Map<String, String>>> _multipleFKPropertyMap;

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Map<String, String>>> getMultipleFKPropertyMap() { // It's closet!
        if (_multipleFKPropertyMap == null) {
            final Object obj = mapProp("torque." + KEY_multipleFKPropertyMap, DEFAULT_EMPTY_MAP);
            _multipleFKPropertyMap = (Map<String, Map<String, Map<String, String>>>) obj;
        }
        return _multipleFKPropertyMap;
    }

    public String getMultipleFKPropertyColumnAliasName(String tableName, java.util.List<String> multipleFKColumnNameList) {
        final Map<String, Map<String, String>> foreignKeyMap = asFlexible().get(tableName);
        if (foreignKeyMap == null) {
            return "";
        }
        final String columnKey = createMultipleFKPropertyColumnKey(multipleFKColumnNameList);
        final Map<String, Map<String, String>> foreignKeyFxMap = asFlexible(foreignKeyMap);
        final Map<String, String> foreignPropertyElement = foreignKeyFxMap.get(columnKey);
        if (foreignPropertyElement == null) {
            return "";
        }
        final String columnAliasName = foreignPropertyElement.get("columnAliasName");
        return columnAliasName;
    }

    protected String createMultipleFKPropertyColumnKey(List<String> multipleFKColumnNameList) {
        final StringBuilder sb = new StringBuilder();
        for (String columnName : multipleFKColumnNameList) {
            sb.append("/").append(columnName);
        }
        sb.delete(0, "/".length());
        return sb.toString();
    }

    protected Map<String, Map<String, Map<String, String>>> asFlexible() {
        Map<String, Map<String, Map<String, String>>> map = StringKeyMap.createAsFlexible();
        map.putAll(getMultipleFKPropertyMap());
        return map;
    }

    protected Map<String, Map<String, String>> asFlexible(final Map<String, Map<String, String>> foreignKeyMap) {
        Map<String, Map<String, String>> map = StringKeyMap.createAsFlexible();
        map.putAll(foreignKeyMap);
        return map;
    }
}