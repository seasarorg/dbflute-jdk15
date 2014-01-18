package org.seasar.dbflute.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.seasar.dbflute.cbean.chelper.HpFixedConditionQueryResolver;
import org.seasar.dbflute.properties.filereader.DfMapStringFileReader;
import org.seasar.dbflute.util.Srl;

/**
 * @author jflute
 */
public final class DfAdditionalForeignKeyProperties extends DfAbstractHelperProperties {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String KEY_LOCAL_TABLE_NAME = "localTableName";
    public static final String KEY_FOREIGN_TABLE_NAME = "foreignTableName";
    public static final String KEY_LOCAL_COLUMN_NAME = "localColumnName";
    public static final String KEY_FOREIGN_COLUMN_NAME = "foreignColumnName";
    public static final String KEY_FIXED_CONDITION = "fixedCondition";
    public static final String KEY_FIXED_SUFFIX = "fixedSuffix";
    public static final String KEY_FIXED_INLINE = "fixedInline";
    public static final String KEY_COMMENT = "comment";

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor.
     * @param prop Properties. (NotNull)
     */
    public DfAdditionalForeignKeyProperties(Properties prop) {
        super(prop);
    }

    // ===================================================================================
    //                                                             additionalForeignKeyMap
    //                                                             =======================
    public static final String KEY_additionalForeignKeyMap = "additionalForeignKeyMap";
    protected Map<String, Map<String, String>> _additionalForeignKeyMap;

    public Map<String, Map<String, String>> getAdditionalForeignKeyMap() {
        if (_additionalForeignKeyMap == null) {
            _additionalForeignKeyMap = newLinkedHashMap();
            final Map<String, Object> generatedMap = mapProp("torque." + KEY_additionalForeignKeyMap, DEFAULT_EMPTY_MAP);
            final Set<String> fisrtKeySet = generatedMap.keySet();
            for (Object foreignName : fisrtKeySet) {// FK Loop!
                final Object firstValue = generatedMap.get(foreignName);
                if (!(firstValue instanceof Map<?, ?>)) {
                    String msg = "The value type should be Map: tableName=" + foreignName + " property=CustomizeDao";
                    msg = msg + " actualType=" + firstValue.getClass() + " actualValue=" + firstValue;
                    throw new IllegalStateException(msg);
                }
                final Map<?, ?> foreignDefinitionMap = (Map<?, ?>) firstValue;
                final Set<?> secondKeySet = foreignDefinitionMap.keySet();
                final Map<String, String> genericForeignDefinitiontMap = newLinkedHashMap();
                for (Object componentName : secondKeySet) { // FK component loop!
                    final Object secondValue = foreignDefinitionMap.get(componentName);
                    if (secondValue == null) {
                        continue;
                    }
                    if (!(componentName instanceof String)) {
                        String msg = "The key type should be String: foreignName=" + foreignName;
                        msg = msg + " property=AdditionalForeignKey";
                        msg = msg + " actualType=" + componentName.getClass() + " actualKey=" + componentName;
                        throw new IllegalStateException(msg);
                    }
                    if (!(secondValue instanceof String)) {
                        String msg = "The value type should be String: foreignName=" + foreignName;
                        msg = msg + " property=AdditionalForeignKey";
                        msg = msg + " actualType=" + secondValue.getClass() + " actualValue=" + secondValue;
                        throw new IllegalStateException(msg);
                    }
                    genericForeignDefinitiontMap.put((String) componentName, (String) secondValue);
                }
                _additionalForeignKeyMap.put((String) foreignName, genericForeignDefinitiontMap);
            }
        }
        return _additionalForeignKeyMap;
    }

    @Override
    protected DfMapStringFileReader createMapStringFileReader() {
        return super.createMapStringFileReader().saveLine();
    }

    // ===================================================================================
    //                                                                      Finding Helper
    //                                                                      ==============
    public String findLocalTableName(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_LOCAL_TABLE_NAME);
    }

    public String findForeignTableName(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_FOREIGN_TABLE_NAME);
    }

    protected String findLocalColumnName(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_LOCAL_COLUMN_NAME);
    }

    protected String findForeignColumnName(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_FOREIGN_COLUMN_NAME);
    }

    public String findFixedCondition(String foreignKeyName) {
        String fixedCondition = doFindAttributeValue(foreignKeyName, KEY_FIXED_CONDITION);
        if (fixedCondition != null && fixedCondition.trim().length() > 0) {
            // adjust a little about camel case
            final String foreignAliasMark = HpFixedConditionQueryResolver.FOREIGN_ALIAS_MARK;
            final String localAliasMark = HpFixedConditionQueryResolver.LOCAL_ALIAS_MARK;
            fixedCondition = Srl.replace(fixedCondition, "$$ALIAS$$", "$$alias$$");
            fixedCondition = Srl.replace(fixedCondition, "$$ForeignAlias$$", foreignAliasMark);
            fixedCondition = Srl.replace(fixedCondition, "$$LocalAlias$$", localAliasMark);
            fixedCondition = Srl.replace(fixedCondition, "\n", "\\n");
        }
        return fixedCondition;
    }

    public String findFixedSuffix(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_FIXED_SUFFIX);
    }

    public String findFixedInline(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_FIXED_INLINE);
    }

    public String findComment(String foreignKeyName) {
        return doFindAttributeValue(foreignKeyName, KEY_COMMENT);
    }

    protected String doFindAttributeValue(String foreignKeyName, String optionKey) {
        final Map<String, String> attributeMap = getAdditionalForeignKeyMap().get(foreignKeyName);
        return attributeMap.get(optionKey);
    }

    public boolean isSuppressImplicitReverseFK(String foreignKeyName) { // closet (for emergency)
        final Map<String, String> componentMap = getAdditionalForeignKeyMap().get(foreignKeyName);
        String value = componentMap.get("isSuppressImplicitReverseFK");
        return value != null && value.equalsIgnoreCase("true");
    }

    public List<String> findLocalColumnNameList(String foreignKeyName) {
        final String property = findLocalColumnName(foreignKeyName);
        if (property == null || property.trim().length() == 0) {
            return null;
        }
        final List<String> localColumnNameList = new ArrayList<String>();
        final StringTokenizer st = new StringTokenizer(property, "/");
        while (st.hasMoreElements()) {
            localColumnNameList.add(st.nextToken());
        }
        return localColumnNameList;
    }

    public List<String> findForeignColumnNameList(String foreignKeyName) {
        final String property = findForeignColumnName(foreignKeyName);
        if (property == null || property.trim().length() == 0) {
            return null;
        }
        final List<String> foreignColumnNameList = new ArrayList<String>();
        final StringTokenizer st = new StringTokenizer(property, "/");
        while (st.hasMoreElements()) {
            foreignColumnNameList.add(st.nextToken());
        }
        return foreignColumnNameList;
    }
}