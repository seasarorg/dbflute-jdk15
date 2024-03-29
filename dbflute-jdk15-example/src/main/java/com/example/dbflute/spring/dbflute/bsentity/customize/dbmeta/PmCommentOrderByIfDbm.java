/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity.customize.dbmeta;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.AbstractDBMeta;
import org.seasar.dbflute.dbmeta.PropertyGateway;
import org.seasar.dbflute.dbmeta.info.*;
import org.seasar.dbflute.dbmeta.name.*;
import com.example.dbflute.spring.dbflute.allcommon.*;
import com.example.dbflute.spring.dbflute.exentity.customize.*;

/**
 * The DB meta of PmCommentOrderByIf. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class PmCommentOrderByIfDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final PmCommentOrderByIfDbm _instance = new PmCommentOrderByIfDbm();
    private PmCommentOrderByIfDbm() {}
    public static PmCommentOrderByIfDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                    Property Gateway
    //                                                                    ================
    protected final Map<String, PropertyGateway> _epgMap = newHashMap();
    {
        setupEpg(_epgMap, new EpgMemberId(), "memberId");
        setupEpg(_epgMap, new EpgMemberName(), "memberName");
        setupEpg(_epgMap, new EpgMemberAccount(), "memberAccount");
    }
    public PropertyGateway findPropertyGateway(String propertyName)
    { return doFindEpg(_epgMap, propertyName); }
    public static class EpgMemberId implements PropertyGateway {
        public Object read(Entity e) { return ((PmCommentOrderByIf)e).getMemberId(); }
        public void write(Entity e, Object v) { ((PmCommentOrderByIf)e).setMemberId(cti(v)); }
    }
    public static class EpgMemberName implements PropertyGateway {
        public Object read(Entity e) { return ((PmCommentOrderByIf)e).getMemberName(); }
        public void write(Entity e, Object v) { ((PmCommentOrderByIf)e).setMemberName((String)v); }
    }
    public static class EpgMemberAccount implements PropertyGateway {
        public Object read(Entity e) { return ((PmCommentOrderByIf)e).getMemberAccount(); }
        public void write(Entity e, Object v) { ((PmCommentOrderByIf)e).setMemberAccount((String)v); }
    }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "PmCommentOrderByIf";
    protected final String _tablePropertyName = "pmCommentOrderByIf";
    protected final TableSqlName _tableSqlName = new TableSqlName("PmCommentOrderByIf", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnMemberId = cci("MEMBER_ID", "MEMBER_ID", null, "会員ID", false, "memberId", Integer.class, false, false, "INTEGER", 10, 0, false, null, "会員を識別するID。連番として自動採番される。\n（会員IDだけに限らず）採番方法はDBMS次第。", null, null, null);
    protected final ColumnInfo _columnMemberName = cci("MEMBER_NAME", "MEMBER_NAME", null, "会員名称", false, "memberName", String.class, false, false, "VARCHAR", 200, 0, false, null, "会員のフルネームの名称。\n苗字と名前を分けて管理することも多いが、ここでは Example なので単純にひとまとめ。", null, null, null);
    protected final ColumnInfo _columnMemberAccount = cci("MEMBER_ACCOUNT", "MEMBER_ACCOUNT", null, "会員アカウント", false, "memberAccount", String.class, false, false, "VARCHAR", 50, 0, false, null, "会員がログイン時に利用するアカウントNO。\n昨今、メールアドレスをログインIDとすることが多いので、あまり見かけなくないかも。", null, null, null);

    public ColumnInfo columnMemberId() { return _columnMemberId; }
    public ColumnInfo columnMemberName() { return _columnMemberName; }
    public ColumnInfo columnMemberAccount() { return _columnMemberAccount; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnMemberId());
        ls.add(columnMemberName());
        ls.add(columnMemberAccount());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    public UniqueInfo getPrimaryUniqueInfo() {
        throw new UnsupportedOperationException("The table does not have primary key: " + getTableDbName());
    }
    public boolean hasPrimaryKey() { return false; }
    public boolean hasCompoundPrimaryKey() { return false; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "com.example.dbflute.spring.dbflute.exentity.customize.PmCommentOrderByIf"; }
    public String getConditionBeanTypeName() { return null; }
    public String getDaoTypeName() { return null; }
    public String getBehaviorTypeName() { return null; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<PmCommentOrderByIf> getEntityType() { return PmCommentOrderByIf.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public PmCommentOrderByIf newMyEntity() { return new PmCommentOrderByIf(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((PmCommentOrderByIf)e, m); }
    public void acceptAllColumnMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptAllColumnMap((PmCommentOrderByIf)e, m); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }
}
