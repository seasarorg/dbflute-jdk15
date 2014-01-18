package org.seasar.dbflute.properties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.seasar.dbflute.exception.DfTableColumnNameNonCompilableConnectorException;
import org.seasar.dbflute.exception.factory.ExceptionMessageBuilder;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.dbflute.helper.StringSet;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.Srl;

/**
 * @author jflute
 */
public final class DfLittleAdjustmentProperties extends DfAbstractHelperProperties {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfLittleAdjustmentProperties(Properties prop) {
        super(prop);
    }

    // ===================================================================================
    //                                                               Little Adjustment Map
    //                                                               =====================
    public static final String KEY_littleAdjustmentMap = "littleAdjustmentMap";
    protected Map<String, Object> _littleAdjustmentMap;

    public Map<String, Object> getLittleAdjustmentMap() {
        if (_littleAdjustmentMap == null) {
            _littleAdjustmentMap = mapProp("torque." + KEY_littleAdjustmentMap, DEFAULT_EMPTY_MAP);
        }
        return _littleAdjustmentMap;
    }

    public String getProperty(String key, String defaultValue) {
        return getPropertyIfNotBuildProp(key, defaultValue, getLittleAdjustmentMap());
    }

    public boolean isProperty(String key, boolean defaultValue) {
        return isPropertyIfNotExistsFromBuildProp(key, defaultValue, getLittleAdjustmentMap());
    }

    // ===================================================================================
    //                                                     Adding Schema to Table SQL Name
    //                                                     ===============================
    public boolean isAvailableAddingSchemaToTableSqlName() {
        return isProperty("isAvailableAddingSchemaToTableSqlName", false);
    }

    public boolean isAvailableAddingCatalogToTableSqlName() {
        return isProperty("isAvailableAddingCatalogToTableSqlName", false);
    }

    // ===================================================================================
    //                                                                 Database Dependency
    //                                                                 ===================
    public boolean isAvailableDatabaseDependency() {
        return isProperty("isAvailableDatabaseDependency", false);
    }

    // ===================================================================================
    //                                                                         Native JDBC
    //                                                                         ===========
    public boolean isAvailableDatabaseNativeJDBC() {
        // for example, using oracle.sql.DATE on Oracle gives us best performances
        return isProperty("isAvailableDatabaseNativeJDBC", false);
    }

    // ===================================================================================
    //                                                                            Behavior
    //                                                                            ========
    public boolean isAvailableNonPrimaryKeyWritable() {
        return isProperty("isAvailableNonPrimaryKeyWritable", false);
    }

    // ===================================================================================
    //                                                                      Classification
    //                                                                      ==============
    public boolean isCheckSelectedClassification() {
        return isProperty("isCheckSelectedClassification", false);
    }

    public boolean isForceClassificationSetting() {
        return isProperty("isForceClassificationSetting", false);
    }

    public boolean isCDefToStringReturnsName() { // closet
        return isProperty("isCDefToStringReturnsName", false);
    }

    public boolean isMakeEntityOldStyleClassify() { // closet
        return isProperty("isMakeEntityOldStyleClassify", false);
    }

    // ===================================================================================
    //                                                                              Entity
    //                                                                              ======
    public boolean isMakeEntityChaseRelation() {
        return isProperty("isMakeEntityChaseRelation", false);
    }

    public boolean isEntityConvertEmptyStringToNull() {
        return isProperty("isEntityConvertEmptyStringToNull", false);
    }

    // ===================================================================================
    //                                                                      ConditionQuery
    //                                                                      ==============
    public boolean isMakeConditionQueryEqualEmptyString() {
        return isProperty("isMakeConditionQueryEqualEmptyString", false);
    }

    public boolean isMakeConditionQueryNotEqualAsStandard() { // closet
        // DBFlute had used tradition for a long time
        // but default value is true (uses standard) since 0.9.7.2
        return isProperty("isMakeConditionQueryNotEqualAsStandard", true);
    }

    public String getConditionQueryNotEqualDefinitionName() {
        // for AbstractConditionQuery's definition name
        return isMakeConditionQueryNotEqualAsStandard() ? "CK_NES" : "CK_NET";
    }

    // ===================================================================================
    //                                                                       Paging Select
    //                                                                       =============
    public boolean isPagingCountLater() {
        return isProperty("isPagingCountLater", true); // default true @since 0.9.9.0A
    }

    public boolean isPagingCountLeastJoin() {
        return isProperty("isPagingCountLeastJoin", true); // default true @since 0.9.9.0A
    }

    // ===================================================================================
    //                                                                          Inner Join
    //                                                                          ==========
    public boolean isInnerJoinAutoDetect() {
        return isProperty("isInnerJoinAutoDetect", false);
    }

    // ===================================================================================
    //                                                              Display Name UpperCase
    //                                                              ======================
    protected boolean isTableDispNameUpperCase() {
        return isProperty("isTableDispNameUpperCase", false);
    }

    public String filterTableDispNameIfNeeds(String tableDbName) {
        return isTableDispNameUpperCase() ? tableDbName.toUpperCase() : tableDbName;
    }

    // ===================================================================================
    //                                                                  SQL Name UpperCase
    //                                                                  ==================
    public boolean isTableSqlNameUpperCase() {
        return isProperty("isTableSqlNameUpperCase", false);
    }

    public boolean isColumnSqlNameUpperCase() {
        return isProperty("isColumnSqlNameUpperCase", false);
    }

    // ===================================================================================
    //                                                                     Make Deprecated
    //                                                                     ===============
    public boolean isMakeDeprecated() {
        return isProperty("isMakeDeprecated", false);
    }

    public boolean isMakeRecentlyDeprecated() {
        return isProperty("isMakeRecentlyDeprecated", true);
    }

    // ===================================================================================
    //                                                                  Extended Component
    //                                                                  ==================
    public String getDBFluteInitializerClass() { // Java only
        return getExtensionClass("DBFluteInitializer");
    }

    public String getImplementedInvokerAssistantClass() { // Java only
        return getExtensionClass("ImplementedInvokerAssistant");
    }

    public String getImplementedCommonColumnAutoSetupperClass() { // Java only
        return getExtensionClass("ImplementedCommonColumnAutoSetupper");
    }

    public String getS2DaoSettingClass() { // CSharp only
        final String className = "S2DaoSetting";
        if (hasExtensionClass(className)) {
            return getExtendedExtensionClass(className);
        } else {
            return getBasicProperties().getProjectPrefix() + className;
        }
    }

    protected String getExtensionClass(String className) {
        if (hasExtensionClass(className)) {
            return getExtendedExtensionClass(className);
        } else {
            final String commonPackage = getBasicProperties().getBaseCommonPackage();
            final String projectPrefix = getBasicProperties().getProjectPrefix();
            return commonPackage + "." + projectPrefix + className;
        }
    }

    protected boolean hasExtensionClass(String className) {
        String str = getExtendedExtensionClass(className);
        return str != null && str.trim().length() > 0 && !str.trim().equals("null");
    }

    protected String getExtendedExtensionClass(String className) {
        return getProperty("extended" + className + "Class", null);
    }

    // ===================================================================================
    //                                                                          Short Char
    //                                                                          ==========
    public boolean isShortCharHandlingValid() {
        return !getShortCharHandlingMode().equalsIgnoreCase("NONE");
    }

    public String getShortCharHandlingMode() {
        String property = getProperty("shortCharHandlingMode", "NONE");
        return property.toUpperCase();
    }

    public String getShortCharHandlingModeCode() {
        return getShortCharHandlingMode().substring(0, 1);
    }

    // ===================================================================================
    //                                                                               Quote
    //                                                                               =====
    // -----------------------------------------------------
    //                                                 Table
    //                                                 -----
    protected Set<String> _quoteTableNameSet;
    protected Boolean _quoteTableNameAll;

    protected Set<String> getQuoteTableNameSet() {
        if (_quoteTableNameSet != null) {
            return _quoteTableNameSet;
        }
        final Map<String, Object> littleAdjustmentMap = getLittleAdjustmentMap();
        final Object obj = littleAdjustmentMap.get("quoteTableNameList");
        if (obj != null) {
            final List<String> list = castToList(obj, "littleAdjustmentMap.quoteTableNameList");
            _quoteTableNameSet = StringSet.createAsFlexible();
            _quoteTableNameSet.addAll(list);
        } else {
            _quoteTableNameSet = new HashSet<String>();
        }
        _quoteTableNameAll = _quoteTableNameSet.contains("$$ALL$$");
        return _quoteTableNameSet;
    }

    public boolean isQuoteTable(String tableName) {
        final Set<String> quoteTableNameSet = getQuoteTableNameSet(); // also initialize
        if (_quoteTableNameAll != null && _quoteTableNameAll) { // after initialization
            return true;
        }
        return quoteTableNameSet.contains(tableName);
    }

    public String quoteTableNameIfNeeds(String tableName) {
        return doQuoteTableNameIfNeeds(tableName, false);
    }

    public String quoteTableNameIfNeedsDirectUse(String tableName) {
        return doQuoteTableNameIfNeeds(tableName, true);
    }

    protected String doQuoteTableNameIfNeeds(String tableName, boolean directUse) {
        if (tableName == null) {
            return null;
        }
        if (!isQuoteTable(tableName) && !containsNonCompilableConnector(tableName)) {
            return tableName;
        }
        return doQuoteName(tableName, directUse);
    }

    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    protected Set<String> _quoteColumnNameSet;
    protected Boolean _quoteColumnNameAll;

    protected Set<String> getQuoteColumnNameSet() {
        if (_quoteColumnNameSet != null) {
            return _quoteColumnNameSet;
        }
        final Map<String, Object> littleAdjustmentMap = getLittleAdjustmentMap();
        final Object obj = littleAdjustmentMap.get("quoteColumnNameList");
        if (obj != null) {
            final List<String> list = castToList(obj, "littleAdjustmentMap.quoteColumnNameList");
            _quoteColumnNameSet = StringSet.createAsFlexible();
            _quoteColumnNameSet.addAll(list);
        } else {
            _quoteColumnNameSet = new HashSet<String>();
        }
        _quoteColumnNameAll = _quoteColumnNameSet.contains("$$ALL$$");
        return _quoteColumnNameSet;
    }

    public boolean isQuoteColumn(String columnName) {
        final Set<String> quoteColumnNameSet = getQuoteColumnNameSet(); // also initialize
        if (_quoteColumnNameAll != null && _quoteColumnNameAll) { // after initialization
            return true;
        }
        return quoteColumnNameSet.contains(columnName);
    }

    public String quoteColumnNameIfNeeds(String columnName) {
        return doQuoteColumnNameIfNeeds(columnName, false);
    }

    public String quoteColumnNameIfNeedsDirectUse(String columnName) {
        return doQuoteColumnNameIfNeeds(columnName, true);
    }

    protected String doQuoteColumnNameIfNeeds(String columnName, boolean directUse) {
        if (columnName == null) {
            return null;
        }
        if (!isQuoteColumn(columnName) && !containsNonCompilableConnector(columnName)) {
            return columnName;
        }
        return doQuoteName(columnName, directUse);
    }

    // -----------------------------------------------------
    //                                                 Quote
    //                                                 -----
    protected String doQuoteName(String name, boolean directUse) {
        final String beginQuote;
        final String endQuote;
        if (getBasicProperties().isDatabaseMySQL()) {
            // it works in spite of ANSI_QUOTES
            beginQuote = "`";
            endQuote = beginQuote;
        } else if (getBasicProperties().isDatabaseSQLServer()) {
            beginQuote = "[";
            endQuote = "]";
        } else {
            beginQuote = directUse ? "\"" : "\\\"";
            endQuote = beginQuote;
        }
        return beginQuote + name + endQuote;
    }

    // ===================================================================================
    //                                                              Relational Null Object
    //                                                              ======================
    protected Map<String, Object> _relationalNullObjectMap;

    protected Map<String, Object> getRelationalNullObjectMap() {
        if (_relationalNullObjectMap != null) {
            return _relationalNullObjectMap;
        }
        final Map<String, Object> littleAdjustmentMap = getLittleAdjustmentMap();
        final Object obj = littleAdjustmentMap.get("relationalNullObjectMap");
        if (obj != null) {
            _relationalNullObjectMap = castToMap(obj, "littleAdjustmentMap.relationalNullObjectMap");
        } else {
            _relationalNullObjectMap = newLinkedHashMap();
        }
        return _relationalNullObjectMap;
    }

    // foreignMap is only supported now (2011/11/13)

    public boolean hasRelationalNullObjectForeign(String tableName) {
        return getRelationalNullObjectProviderForeignMap().get(tableName) != null;
    }

    public String getNullObjectProviderPackage() {
        final String pkg = (String) getRelationalNullObjectMap().get("providerPackage");
        if (pkg == null) {
            return null;
        }
        final String packageBase = getBasicProperties().getPackageBase();
        return Srl.replace(pkg, "$$packageBase$$", packageBase);
    }

    protected Map<String, String> _relationalNullObjectProviderForeignMap;

    protected Map<String, String> getRelationalNullObjectProviderForeignMap() {
        if (_relationalNullObjectProviderForeignMap != null) {
            return _relationalNullObjectProviderForeignMap;
        }
        final Map<String, Object> nullObjectMap = getRelationalNullObjectMap();
        final Object obj = nullObjectMap.get("foreignMap");
        final Map<String, String> plainMap;
        if (obj != null) {
            plainMap = castToMap(obj, "littleAdjustmentMap.relationalNullObjectMap.foreignMap");
        } else {
            plainMap = newLinkedHashMap();
        }
        _relationalNullObjectProviderForeignMap = StringKeyMap.createAsFlexibleOrdered();
        _relationalNullObjectProviderForeignMap.putAll(plainMap);
        return _relationalNullObjectProviderForeignMap;
    }

    public String getRelationalNullObjectProviderForeignExp(String tableName) {
        return getRelationalNullObjectProviderForeignMap().get(tableName);
    }

    // ===================================================================================
    //                                                                        CursorSelect
    //                                                                        ============
    public boolean isCursorSelectFetchSizeValid() {
        return getCursorSelectFetchSize() != null;
    }

    public String getCursorSelectFetchSize() {
        return getProperty("cursorSelectFetchSize", null);
    }

    // ===================================================================================
    //                                                                 PG Reservation Word
    //                                                                 ===================
    protected List<String> _pgReservColumnList;

    protected List<String> getPgReservColumnList() { // closet
        if (_pgReservColumnList != null) {
            return _pgReservColumnList;
        }
        final Map<String, Object> littleAdjustmentMap = getLittleAdjustmentMap();
        final Object obj = littleAdjustmentMap.get("pgReservColumnList");
        if (obj != null) {
            _pgReservColumnList = castToList(obj, "littleAdjustmentMap.pgReservColumnList");
        } else {
            _pgReservColumnList = new ArrayList<String>();
        }
        return _pgReservColumnList;
    }

    public boolean isPgReservColumn(String columnName) {
        final List<String> pgReservColumnList = getPgReservColumnList();
        if (pgReservColumnList.isEmpty()) {
            if (isTargetLanguageJava()) {
                return Srl.equalsIgnoreCase(columnName, getDefaultJavaPgReservColumn());
            } else if (isTargetLanguageCSharp()) {
                return Srl.equalsIgnoreCase(columnName, getDefaultCSharpPgReservColumn());
            } else {
                return false;
            }
        } else {
            return Srl.equalsIgnoreCase(columnName, pgReservColumnList.toArray(new String[] {}));
        }
    }

    protected String[] getDefaultJavaPgReservColumn() {
        // likely words only (and only can be checked at examples)
        return new String[] { "class", "case", "package", "default", "new", "native", "void", "public", "protected",
                "private", "interface", "abstract", "final", "finally", "return", "double", "float", "short" };
    }

    protected String[] getDefaultCSharpPgReservColumn() {
        // likely words only (and only can be checked at examples)
        return new String[] { "class" };
    }

    public String resolvePgReservColumn(String columnName) {
        if (isPgReservColumn(columnName)) {
            return columnName + (getBasicProperties().isColumnNameCamelCase() ? "Synonym" : "_SYNONYM");
        }
        return columnName;
    }

    // ===================================================================================
    //                                                            Non Compilable Connector
    //                                                            ========================
    public boolean isSuppressNonCompilableConnectorLimiter() { // closet
        return isProperty("isSuppressNonCompilableConnectorLimiter", false);
    }

    public String filterJavaNameNonCompilableConnector(String javaName, NonCompilableChecker checker) {
        checkNonCompilableConnector(checker.name(), checker.disp());
        final List<String> connectorList = getNonCompilableConnectorList();
        for (String connector : connectorList) {
            javaName = Srl.replace(javaName, connector, "_");
        }
        return javaName;
    }

    public static interface NonCompilableChecker {
        String name();

        String disp();
    }

    public void checkNonCompilableConnector(String name, String disp) {
        if (isSuppressNonCompilableConnectorLimiter()) {
            return;
        }
        if (containsNonCompilableConnector(name)) {
            final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
            br.addNotice("Non-compilable connectors in a table/column name were found.");
            br.addItem("Advice");
            br.addElement("Non-compilable connectors are unsupported.");
            br.addElement("For example, 'HYPHEN-TABLE' and 'SPACE COLUMN' and so on...");
            br.addElement("You should change the names like this:");
            br.addElement("  HYPHEN-TABLE -> HYPHEN_TABLE");
            br.addElement("  SPACE COLUMN -> SPACE_COLUMN");
            br.addElement("");
            br.addElement("If you cannot change by any possibility, you can suppress its limiter.");
            br.addElement(" -> isSuppressNonCompilableConnectorLimiter in littleAdjustmentMap.dfprop.");
            br.addElement("However several functions may not work. It's a restriction.");
            br.addItem("Target Object");
            br.addElement(disp);
            final String msg = br.buildExceptionMessage();
            throw new DfTableColumnNameNonCompilableConnectorException(msg);
        }
    }

    protected boolean containsNonCompilableConnector(String tableName) {
        final List<String> connectorList = getNonCompilableConnectorList();
        return Srl.containsAny(tableName, connectorList.toArray(new String[] {}));
    }

    protected List<String> getNonCompilableConnectorList() {
        return DfCollectionUtil.newArrayList("-", " "); // non property
    }

    // ===================================================================================
    //                                                                          Value Type
    //                                                                          ==========
    // S2Dao.NET does not implement ValueType attribute,
    // so this property is INVALID now. At the future,
    // DBFlute may implement ValueType Framework. 
    public boolean isUseAnsiStringTypeToNotUnicode() { // closet, CSharp only
        return isProperty("isUseAnsiStringTypeToNotUnicode", false);
    }

    // ===================================================================================
    //                                                                   Alternate Control
    //                                                                   =================
    public boolean isAlternateGenerateControlValid() {
        final String str = getAlternateGenerateControl();
        return str != null && str.trim().length() > 0 && !str.trim().equals("null");
    }

    public String getAlternateGenerateControl() { // closet
        return getProperty("alternateGenerateControl", null);
    }

    public boolean isAlternateSql2EntityControlValid() {
        final String str = getAlternateSql2EntityControl();
        return str != null && str.trim().length() > 0 && !str.trim().equals("null");
    }

    public String getAlternateSql2EntityControl() { // closet
        return getProperty("alternateSql2EntityControl", null);
    }

    // ===================================================================================
    //                                                                       Stop Generate
    //                                                                       =============
    public boolean isStopGenerateExtendedBhv() { // closet
        return isProperty("isStopGenerateExtendedBhv", false);
    }

    public boolean isStopGenerateExtendedDao() { // closet
        return isProperty("isStopGenerateExtendedDao", false);
    }

    public boolean isStopGenerateExtendedEntity() { // closet
        return isProperty("isStopGenerateExtendedEntity", false);
    }

    // ===================================================================================
    //                                                              Delete Old Table Class
    //                                                              ======================
    public boolean isDeleteOldTableClass() { // closet
        // The default value is true since 0.8.8.1.
        return isProperty("isDeleteOldTableClass", true);
    }

    // ===================================================================================
    //                                                          Skip Generate If Same File
    //                                                          ==========================
    public boolean isSkipGenerateIfSameFile() { // closet
        // The default value is true since 0.7.8.
        return isProperty("isSkipGenerateIfSameFile", true);
    }

    // ===================================================================================
    //                                              ToLower in Generator Underscore Method
    //                                              ======================================
    public boolean isAvailableToLowerInGeneratorUnderscoreMethod() { // closet
        return isProperty("isAvailableToLowerInGeneratorUnderscoreMethod", true);
    }

    // ===================================================================================
    //                                                                      Flat Expansion
    //                                                                      ==============
    public boolean isMakeFlatExpansion() { // closet, closed function permanently
        return isProperty("isMakeFlatExpansion", false);
    }

    // ===================================================================================
    //                                                                               S2Dao
    //                                                                               =====
    public boolean isMakeDaoInterface() { // closet, CSharp only
        if (isTargetLanguageCSharp()) {
            return true; // It is not implemented at CSharp yet
        }
        final boolean makeDaoInterface = booleanProp("torque.isMakeDaoInterface", false);
        if (makeDaoInterface) {
            String msg = "Dao interfaces are unsupported since DBFlute-0.8.7!";
            throw new UnsupportedOperationException(msg);
        }
        return false;
    }

    protected boolean isTargetLanguageJava() {
        return getBasicProperties().isTargetLanguageJava();
    }

    protected boolean isTargetLanguageCSharp() {
        return getBasicProperties().isTargetLanguageCSharp();
    }

    // ===================================================================================
    //                                                                          Compatible
    //                                                                          ==========
    public boolean isCompatibleAutoMappingOldStyle() { // closet
        return isProperty("isCompatibleAutoMappingOldStyle", false);
    }
}