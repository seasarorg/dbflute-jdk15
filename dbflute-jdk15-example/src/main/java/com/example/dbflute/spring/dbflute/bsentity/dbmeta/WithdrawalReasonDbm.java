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
 * The DB meta of WITHDRAWAL_REASON. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class WithdrawalReasonDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final WithdrawalReasonDbm _instance = new WithdrawalReasonDbm();
    private WithdrawalReasonDbm() {}
    public static WithdrawalReasonDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                    Property Gateway
    //                                                                    ================
    protected final Map<String, PropertyGateway> _epgMap = newHashMap();
    {
        setupEpg(_epgMap, new EpgWithdrawalReasonCode(), "withdrawalReasonCode");
        setupEpg(_epgMap, new EpgWithdrawalReasonText(), "withdrawalReasonText");
        setupEpg(_epgMap, new EpgDisplayOrder(), "displayOrder");
    }
    public PropertyGateway findPropertyGateway(String propertyName)
    { return doFindEpg(_epgMap, propertyName); }
    public static class EpgWithdrawalReasonCode implements PropertyGateway {
        public Object read(Entity e) { return ((WithdrawalReason)e).getWithdrawalReasonCode(); }
        public void write(Entity e, Object v) { ((WithdrawalReason)e).setWithdrawalReasonCode((String)v); }
    }
    public static class EpgWithdrawalReasonText implements PropertyGateway {
        public Object read(Entity e) { return ((WithdrawalReason)e).getWithdrawalReasonText(); }
        public void write(Entity e, Object v) { ((WithdrawalReason)e).setWithdrawalReasonText((String)v); }
    }
    public static class EpgDisplayOrder implements PropertyGateway {
        public Object read(Entity e) { return ((WithdrawalReason)e).getDisplayOrder(); }
        public void write(Entity e, Object v) { ((WithdrawalReason)e).setDisplayOrder(cti(v)); }
    }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "WITHDRAWAL_REASON";
    protected final String _tablePropertyName = "withdrawalReason";
    protected final TableSqlName _tableSqlName = new TableSqlName("WITHDRAWAL_REASON", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }
    protected final String _tableAlias = "退会理由";
    public String getTableAlias() { return _tableAlias; }
    protected final String _tableComment = "会員に選ばせる定型的な退会理由のマスタ。";
    public String getTableComment() { return _tableComment; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnWithdrawalReasonCode = cci("WITHDRAWAL_REASON_CODE", "WITHDRAWAL_REASON_CODE", null, "退会理由コード", true, "withdrawalReasonCode", String.class, true, false, "CHAR", 3, 0, false, null, null, null, "memberWithdrawalList", CDef.DefMeta.WithdrawalReason);
    protected final ColumnInfo _columnWithdrawalReasonText = cci("WITHDRAWAL_REASON_TEXT", "WITHDRAWAL_REASON_TEXT", null, "退会理由テキスト", true, "withdrawalReasonText", String.class, false, false, "CLOB", 2147483647, 0, false, null, "退会理由の内容。テキスト形式なので目いっぱい書けるが、\nそうするとUI側できれいに見せるのが大変でしょうね。", null, null, null);
    protected final ColumnInfo _columnDisplayOrder = cci("DISPLAY_ORDER", "DISPLAY_ORDER", null, null, true, "displayOrder", Integer.class, false, false, "INTEGER", 10, 0, false, null, null, null, null, null);

    public ColumnInfo columnWithdrawalReasonCode() { return _columnWithdrawalReasonCode; }
    public ColumnInfo columnWithdrawalReasonText() { return _columnWithdrawalReasonText; }
    public ColumnInfo columnDisplayOrder() { return _columnDisplayOrder; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnWithdrawalReasonCode());
        ls.add(columnWithdrawalReasonText());
        ls.add(columnDisplayOrder());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    public UniqueInfo getPrimaryUniqueInfo() { return cpui(columnWithdrawalReasonCode()); }
    public boolean hasPrimaryKey() { return true; }
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
    public ReferrerInfo referrerMemberWithdrawalList() {
        Map<ColumnInfo, ColumnInfo> map = newLinkedHashMap(columnWithdrawalReasonCode(), MemberWithdrawalDbm.getInstance().columnWithdrawalReasonCode());
        return cri("memberWithdrawalList", this, MemberWithdrawalDbm.getInstance(), map, false, "withdrawalReason");
    }

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "com.example.dbflute.spring.dbflute.exentity.WithdrawalReason"; }
    public String getConditionBeanTypeName() { return "com.example.dbflute.spring.dbflute.cbean.WithdrawalReasonCB"; }
    public String getDaoTypeName() { return "${glPackageExtendedDao}.WithdrawalReasonDao"; }
    public String getBehaviorTypeName() { return "com.example.dbflute.spring.dbflute.exbhv.WithdrawalReasonBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<WithdrawalReason> getEntityType() { return WithdrawalReason.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Entity newEntity() { return newMyEntity(); }
    public WithdrawalReason newMyEntity() { return new WithdrawalReason(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptPrimaryKeyMap((WithdrawalReason)e, m); }
    public void acceptAllColumnMap(Entity e, Map<String, ? extends Object> m)
    { doAcceptAllColumnMap((WithdrawalReason)e, m); }
    public Map<String, Object> extractPrimaryKeyMap(Entity e) { return doExtractPrimaryKeyMap(e); }
    public Map<String, Object> extractAllColumnMap(Entity e) { return doExtractAllColumnMap(e); }
}
