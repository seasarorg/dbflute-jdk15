/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.allcommon;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.lang.reflect.Method;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.DBMetaProvider;
import org.seasar.dbflute.exception.DBMetaNotFoundException;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.dbflute.util.DfAssertUtil;

/**
 * The handler of the instance of DB meta.
 * @author DBFlute(AutoGenerator)
 */
public class DBMetaInstanceHandler implements DBMetaProvider {

    // ===================================================================================
    //                                                                        Resource Map
    //                                                                        ============
    /** Table DB-name instance map. */
    protected static final Map<String, DBMeta> _tableDbNameInstanceMap = newHashMap();

    /** The map of table DB name and class name. This is for initialization. */
    protected static final Map<String, String> _tableDbNameClassNameMap;
    static {
        final Map<String, String> tmpMap = newHashMap();
        tmpMap.put("MEMBER", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm");
        tmpMap.put("MEMBER_ADDRESS", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberAddressDbm");
        tmpMap.put("MEMBER_LOGIN", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberLoginDbm");
        tmpMap.put("MEMBER_SECURITY", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberSecurityDbm");
        tmpMap.put("MEMBER_SERVICE", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberServiceDbm");
        tmpMap.put("MEMBER_STATUS", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberStatusDbm");
        tmpMap.put("MEMBER_WITHDRAWAL", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberWithdrawalDbm");
        tmpMap.put("PRODUCT", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.ProductDbm");
        tmpMap.put("PRODUCT_CATEGORY", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.ProductCategoryDbm");
        tmpMap.put("PRODUCT_STATUS", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.ProductStatusDbm");
        tmpMap.put("PURCHASE", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.PurchaseDbm");
        tmpMap.put("REGION", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.RegionDbm");
        tmpMap.put("SERVICE_RANK", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.ServiceRankDbm");
        tmpMap.put("VENDOR_$_DOLLAR", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.Vendor$DollarDbm");
        tmpMap.put("VENDOR_CHECK", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.VendorCheckDbm");
        tmpMap.put("VENDOR_IDENTITY_ONLY", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.VendorIdentityOnlyDbm");
        tmpMap.put("VENDOR_PRIMARY_KEY_ONLY", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.VendorPrimaryKeyOnlyDbm");
        tmpMap.put("VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.VendorTheLongAndWindingTableAndColumnDbm");
        tmpMap.put("VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.VendorTheLongAndWindingTableAndColumnRefDbm");
        tmpMap.put("WITHDRAWAL_REASON", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.WithdrawalReasonDbm");
        tmpMap.put("SUMMARY_PRODUCT", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.SummaryProductDbm");
        tmpMap.put("SUMMARY_WITHDRAWAL", "com.example.dbflute.spring.dbflute.bsentity.dbmeta.SummaryWithdrawalDbm");
        _tableDbNameClassNameMap = Collections.unmodifiableMap(tmpMap);
    }

    /** The flexible map of table DB name. This is for conversion at finding. */
    protected static final Map<String, String> _tableDbNameFlexibleMap = StringKeyMap.createAsFlexible();
    static {
        final Set<String> tableDbNameSet = _tableDbNameClassNameMap.keySet();
        for (String tableDbName : tableDbNameSet) {
            _tableDbNameFlexibleMap.put(tableDbName, tableDbName);
        }
    }

    /**
     * Get the unmodifiable map of DB meta.
     * @return The unmodifiable map that contains all instances of DB meta. (NotNull & NotEmpty)
     */
    public static Map<String, DBMeta> getUnmodifiableDBMetaMap() {
        initializeDBMetaMap();
        synchronized (_tableDbNameInstanceMap) {
            return Collections.unmodifiableMap(_tableDbNameInstanceMap);
        }
    }

    /**
     * Initialize the map of DB meta.
     */
    protected static void initializeDBMetaMap() {
        if (isInitialized()) {
            return;
        }
        synchronized (_tableDbNameInstanceMap) {
            final Set<String> tableDbNameSet = _tableDbNameClassNameMap.keySet();
            for (String tableDbName : tableDbNameSet) {
                findDBMeta(tableDbName); // Initialize!
            }
            if (!isInitialized()) {
                String msg = "Failed to initialize tableDbNameInstanceMap:";
                msg = msg + " tableDbNameInstanceMap=" + _tableDbNameInstanceMap;
                throw new IllegalStateException(msg);
            }
        }
    }

    protected static boolean isInitialized() {
        return _tableDbNameInstanceMap.size() == _tableDbNameClassNameMap.size();
    }

    // ===================================================================================
    //                                                                  Provider Singleton
    //                                                                  ==================
    protected static final DBMetaProvider _provider = new DBMetaInstanceHandler();

    public static DBMetaProvider getProvider() {
        return _provider;
    }

    /**
     * @param tableFlexibleName The flexible name of table. (NotNull)
     * @return The instance of DB meta. (NullAllowed: If the DB meta is not found, it returns null)
     */
    public DBMeta provideDBMeta(String tableFlexibleName) {
        return byTableFlexibleName(tableFlexibleName);
    }

    /**
     * @param tableFlexibleName The flexible name of table. (NotNull)
     * @return The instance of DB meta. (NotNull)
     * @exception org.seasar.dbflute.exception.DBMetaNotFoundException When the DB meta is not found.
     */
    public DBMeta provideDBMetaChecked(String tableFlexibleName) {
        return findDBMeta(tableFlexibleName);
    }

    // ===================================================================================
    //                                                                         Find DBMeta
    //                                                                         ===========
    /**
     * Find DB meta by table flexible name. (accept quoted name and schema prefix)
     * @param tableFlexibleName The flexible name of table. (NotNull)
     * @return The instance of DB meta. (NotNull)
     * @exception org.seasar.dbflute.exception.DBMetaNotFoundException When the DB meta is not found.
     */
    public static DBMeta findDBMeta(String tableFlexibleName) {
        DBMeta dbmeta = byTableFlexibleName(tableFlexibleName);
        if (dbmeta == null) {
            String msg = "The DB meta was not found by the table flexible name: " + tableFlexibleName;
            msg = msg + " key=" + tableFlexibleName + " instanceMap=" + _tableDbNameInstanceMap;
            throw new DBMetaNotFoundException(msg);
        }
        return dbmeta;
    }

    // ===================================================================================
    //                                                                       By Table Name
    //                                                                       =============
    /**
     * @param tableFlexibleName The flexible name of table. (NotNull)
     * @return The instance of DB meta. (NullAllowed: If the DB meta is not found, it returns null)
     */
    protected static DBMeta byTableFlexibleName(String tableFlexibleName) {
        assertStringNotNullAndNotTrimmedEmpty("tableFlexibleName", tableFlexibleName);
        final String name = normalizeTableFlexibleName(tableFlexibleName);
        final String tableDbName = _tableDbNameFlexibleMap.get(name);
        if (tableDbName != null) {
            return byTableDbName(tableDbName);
        }
        return null;
    }

    protected static String normalizeTableFlexibleName(final String tableFlexibleName) {
        return removeQuoteIfExists(removeSchemaIfExists(tableFlexibleName));
    }

    protected static String removeSchemaIfExists(String name) {
        final int dotLastIndex = name.lastIndexOf(".");
        if (dotLastIndex >= 0) {
            return name.substring(dotLastIndex + ".".length());
        }
        return name; 
    }

    protected static String removeQuoteIfExists(String name) {
        if (name.startsWith("\"") && name.endsWith("\"")) {
            return strip(name);
        } else if (name.startsWith("[") && name.endsWith("]")) {
            return strip(name);
        }
        return name;
    }

    protected static String strip(String name) {
        return name.substring(1, name.length() - 1);
    }

    /**
     * @param tableDbName The DB name of table. (NotNull)
     * @return The instance of DB meta. (NullAllowed: If the DB meta is not found, it returns null)
     */
    protected static DBMeta byTableDbName(String tableDbName) {
        assertStringNotNullAndNotTrimmedEmpty("tableDbName", tableDbName);
        return getCachedDBMeta(tableDbName);
    }

    // ===================================================================================
    //                                                                       Cached DBMeta
    //                                                                       =============
    protected static DBMeta getCachedDBMeta(String tableDbName) { // lazy-load (thank you koyak!)
        DBMeta dbmeta = _tableDbNameInstanceMap.get(tableDbName);
        if (dbmeta != null) {
            return dbmeta;
        }
        synchronized (_tableDbNameInstanceMap) {
            dbmeta = _tableDbNameInstanceMap.get(tableDbName);
            if (dbmeta != null) {
                // an other thread might have initialized
                // or reading might failed by same-time writing
                return dbmeta;
            }
            String entityName = _tableDbNameClassNameMap.get(tableDbName);
            _tableDbNameInstanceMap.put(tableDbName, getDBMeta(entityName));
            return _tableDbNameInstanceMap.get(tableDbName);
        }
    }

    protected static DBMeta getDBMeta(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method methoz = clazz.getMethod("getInstance", (Class[])null);
            Object result = methoz.invoke(null, (Object[])null);
            return (DBMeta)result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===================================================================================
    //                                                                      General Helper
    //                                                                      ==============
    protected static <KEY, VALUE> HashMap<KEY, VALUE> newHashMap() {
        return new HashMap<KEY, VALUE>();
    }

    // -----------------------------------------------------
    //                                         Assert Object
    //                                         -------------
    protected static void assertObjectNotNull(String variableName, Object value) {
        DfAssertUtil.assertObjectNotNull(variableName, value);
    }

    // -----------------------------------------------------
    //                                         Assert String
    //                                         -------------
    protected static void assertStringNotNullAndNotTrimmedEmpty(String variableName, String value) {
        DfAssertUtil.assertStringNotNullAndNotTrimmedEmpty(variableName, value);
    }
}
