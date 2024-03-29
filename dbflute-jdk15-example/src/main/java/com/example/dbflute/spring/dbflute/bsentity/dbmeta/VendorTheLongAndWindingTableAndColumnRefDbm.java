/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.AbstractDBMeta;
import org.seasar.dbflute.dbmeta.PropertyGateway;
import org.seasar.dbflute.dbmeta.info.*;
import org.seasar.dbflute.dbmeta.name.*;
import com.example.dbflute.spring.dbflute.allcommon.*;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The DB meta of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class VendorTheLongAndWindingTableAndColumnRefDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final VendorTheLongAndWindingTableAndColumnRefDbm _instance = new VendorTheLongAndWindingTableAndColumnRefDbm();
    private VendorTheLongAndWindingTableAndColumnRefDbm() {}
    public static VendorTheLongAndWindingTableAndColumnRefDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                    Property Gateway
    //                                                                    ================
    protected final Map<String, PropertyGateway> _epgMap = newHashMap();
    {
        setupEpg(_epgMap, new EpgTheLongAndWindingTableAndColumnRefId(), "theLongAndWindingTableAndColumnRefId");
        setupEpg(_epgMap, new EpgTheLongAndWindingTableAndColumnId(), "theLongAndWindingTableAndColumnId");
        setupEpg(_epgMap, new EpgTheLongAndWindingTableAndColumnRefDate(), "theLongAndWindingTableAndColumnRefDate");
        setupEpg(_epgMap, new EpgShortDate(), "shortDate");
    }
    public PropertyGateway findPropertyGateway(String propertyName)
    { return doFindEpg(_epgMap, propertyName); }
    public static class EpgTheLongAndWindingTableAndColumnRefId implements PropertyGateway {
        public Object read(Entity e) { return ((VendorTheLongAndWindingTableAndColumnRef)e).getTheLongAndWindingTableAndColumnRefId(); }
        public void write(Entity e, Object v) { ((VendorTheLongAndWindingTableAndColumnRef)e).setTheLongAndWindingTableAndColumnRefId(ctl(v)); }
    }
    public static class EpgTheLongAndWindingTableAndColumnId implements PropertyGateway {
        public Object read(Entity e) { return ((VendorTheLongAndWindingTableAndColumnRef)e).getTheLongAndWindingTableAndColumnId(); }
        public void write(Entity e, Object v) { ((VendorTheLongAndWindingTableAndColumnRef)e).setTheLongAndWindingTableAndColumnId(ctl(v)); }
    }
    public static class EpgTheLongAndWindingTableAndColumnRefDate implements PropertyGateway {
        public Object read(Entity e) { return ((VendorTheLongAndWindingTableAndColumnRef)e).getTheLongAndWindingTableAndColumnRefDate(); }
        public void write(Entity e, Object v) { ((VendorTheLongAndWindingTableAndColumnRef)e).setTheLongAndWindingTableAndColumnRefDate((java.util.Date)v); }
    }
    public static class EpgShortDate implements PropertyGateway {
        public Object read(Entity e) { return ((VendorTheLongAndWindingTableAndColumnRef)e).getShortDate(); }
        public void write(Entity e, Object v) { ((VendorTheLongAndWindingTableAndColumnRef)e).setShortDate((java.util.Date)v); }
    }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF";
    protected final String _tablePropertyName = "vendorTheLongAndWindingTableAndColumnRef";
    protected final TableSqlName _tableSqlName = new TableSqlName("VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnTheLongAndWindingTableAndColumnRefId = cci("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID", "THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_ID", null, null, true, "theLongAndWindingTableAndColumnRefId", Long.class, true, false, "BIGINT", 19, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnTheLongAndWindingTableAndColumnId = cci("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID", "THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID", null, null, true, "theLongAndWindingTableAndColumnId", Long.class, false, false, "BIGINT", 19, 0, false, null, null, "vendorTheLongAndWindingTableAndColumn", null, null);
    protected final ColumnInfo _columnTheLongAndWindingTableAndColumnRefDate = cci("THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE", "THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF_DATE", null, null, true, "theLongAndWindingTableAndColumnRefDate", java.util.Date.class, false, false, "DATE", 8, 0, false, null, null, null, null, null);
    protected final ColumnInfo _columnShortDate = cci("SHORT_DATE", "SHORT_DATE", null, null, true, "shortDate", java.util.Date.class, false, false, "DATE", 8, 0, false, null, null, null, null, null);

    public ColumnInfo columnTheLongAndWindingTableAndColumnRefId() { return _columnTheLongAndWindingTableAndColumnRefId; }
    public ColumnInfo columnTheLongAndWindingTableAndColumnId() { return _columnTheLongAndWindingTableAndColumnId; }
    public ColumnInfo columnTheLongAndWindingTableAndColumnRefDate() { return _columnTheLongAndWindingTableAndColumnRefDate; }
    public ColumnInfo columnShortDate() { return _columnShortDate; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnTheLongAndWindingTableAndColumnRefId());
        ls.add(columnTheLongAndWindingTableAndColumnId());
        ls.add(columnTheLongAndWindingTableAndColumnRefDate());
        ls.add(columnShortDate());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    public UniqueInfo getPrimaryUniqueInfo() { return cpui(columnTheLongAndWindingTableAndColumnRefId()); }
    public boolean hasPrimaryKey() { return true; }
    public boolean hasCompoundPrimaryKey() { return false; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------
    public ForeignInfo foreignVendorTheLongAndWindingTableAndColumn() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnTheLongAndWindingTableAndColumnId(), VendorTheLongAndWindingTableAndColumnDbm.getInstance().columnTheLongAndWindingTableAndColumnId());
        return cfi("vendorTheLongAndWindingTableAndColumn", this, VendorTheLongAndWindingTableAndColumnDbm.getInstance(), map, 0, false, false, false, "vendorTheLongAndWindingTableAndColumnRefList");
    }

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "com.example.dbflute.spring.dbflute.exentity.VendorTheLongAndWindingTableAndColumnRef"; }
    public String getConditionBeanTypeName() { return "com.example.dbflute.spring.dbflute.cbean.VendorTheLongAndWindingTableAndColumnRefCB"; }
    public String getDaoTypeName() { return "${glPackageExtendedDao}.VendorTheLongAndWindingTableAndColumnRefDao"; }
    public String getBehaviorTypeName() { return "com.example.dbflute.spring.dbflute.exbhv.VendorTheLongAndWindingTableAndColumnRefBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<VendorTheLongAndWindingTableAndColumnRef> getEntityType() { return VendorTheLongAndWindingTableAndColumnRef.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public VendorTheLongAndWindingTableAndColumnRef newMyEntity() { return new VendorTheLongAndWindingTableAndColumnRef(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((VendorTheLongAndWindingTableAndColumnRef)e, m); }
    public void acceptAllColumnMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptAllColumnMap((VendorTheLongAndWindingTableAndColumnRef)e, m); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }
}
