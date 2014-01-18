package org.seasar.dbflute.mock;

import java.util.Map;

import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.OrderByBean;
import org.seasar.dbflute.cbean.PagingBean;
import org.seasar.dbflute.cbean.PagingInvoker;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.cbean.chelper.HpCBPurpose;
import org.seasar.dbflute.cbean.chelper.HpSpecifiedColumn;
import org.seasar.dbflute.cbean.coption.ScalarSelectOption;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.seasar.dbflute.cbean.sqlclause.orderby.OrderByClause;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.jdbc.StatementConfig;

public class MockConditionBean implements ConditionBean {

    public DBMeta getDBMeta() {
        return null;
    }

    public void acceptPrimaryKeyMap(Map<String, ? extends Object> primaryKeyMap) {
    }

    public void acceptPrimaryKeyMapString(String primaryKeyMapString) {
    }

    public ConditionBean addOrderBy_PK_Asc() {
        return null;
    }

    public ConditionBean addOrderBy_PK_Desc() {
        return null;
    }

    public void configure(StatementConfig statementConfig) {
    }

    public SqlClause getSqlClause() {
        return null;
    }

    public StatementConfig getStatementConfig() {
        return null;
    }

    public String getTableDbName() {
        return null;
    }

    public String getTableSqlName() {
        return null;
    }

    public boolean hasUnionQueryOrUnionAllQuery() {
        return false;
    }

    public boolean isSelectCountIgnoreFetchScope() {
        return false;
    }

    public ConditionQuery localCQ() {
        return null;
    }

    public ConditionBean lockForUpdate() {
        return null;
    }

    public String toDisplaySql() {
        return null;
    }

    public ConditionBean xafterCareSelectCountIgnoreFetchScope() {
        return null;
    }

    public ConditionBean xsetupSelectCountIgnoreFetchScope(boolean uniqueCount) {
        return null;
    }

    public void xacceptScalarSelectOption(ScalarSelectOption option) {
    }

    public boolean canPagingCountLater() {
        return false;
    }

    public void enablePagingCountLater() {
    }

    public void disablePagingCountLater() {
    }

    public boolean canPagingReSelect() {
        return false;
    }

    public void disablePagingReSelect() {
    }

    public void enablePagingReSelect() {
    }

    public PagingBean fetchFirst(int fetchSize) {
        return null;
    }

    public PagingBean fetchPage(int fetchPageNumber) {
        return null;
    }

    public PagingBean fetchScope(int fetchStartIndex, int fetchSize) {
        return null;
    }

    public <ENTITY> PagingInvoker<ENTITY> createPagingInvoker(String tableDbName) {
        return null;
    }

    public int getFetchPageNumber() {
        return 0;
    }

    public int getFetchSize() {
        return 0;
    }

    public int getFetchStartIndex() {
        return 0;
    }

    public int getPageEndIndex() {
        return 0;
    }

    public int getPageStartIndex() {
        return 0;
    }

    public boolean isFetchScopeEffective() {
        return false;
    }

    public boolean isPaging() {
        return false;
    }

    public void paging(int pageSize, int pageNumber) {
    }

    public void xsetPaging(boolean paging) {
    }

    public int getFetchNarrowingLoopCount() {
        return 0;
    }

    public int getFetchNarrowingSkipStartIndex() {
        return 0;
    }

    public int getSafetyMaxResultSize() {
        return 0;
    }

    public void ignoreFetchNarrowing() {
    }

    public boolean isFetchNarrowingEffective() {
        return false;
    }

    public boolean isFetchNarrowingLoopCountEffective() {
        return false;
    }

    public boolean isFetchNarrowingSkipStartIndexEffective() {
        return false;
    }

    public void restoreIgnoredFetchNarrowing() {
    }

    public OrderByBean clearOrderBy() {
        return null;
    }

    public String getOrderByClause() {
        return null;
    }

    public OrderByClause getOrderByComponent() {
        return null;
    }

    public OrderByBean ignoreOrderBy() {
        return null;
    }

    public OrderByBean makeOrderByEffective() {
        return null;
    }

    public void checkSafetyResult(int safetyMaxResultSize) {
    }

    public boolean hasOrderByClause() {
        return false;
    }

    public void invokeSetupSelect(String foreignPropertyNamePath) {
    }

    public Map<String, Object> getFreeParameterMap() {
        return null;
    }

    public String xregisterFreeParameter(String key, Object value) {
        return null;
    }

    public String xregisterFreeParameterToThemeList(String themeKey, Object addedValue) {
        return null;
    }

    public void xregisterUnionQuerySynchronizer(UnionQuery<ConditionBean> unionQuerySynchronizer) {
    }

    public void allowEmptyStringQuery() {
    }

    public void checkInvalidQuery() {
    }

    public HpCBPurpose getPurpose() {
        return null;
    }

    public void allowInnerJoinAutoDetect() {
    }

    public void allowStructurePossibleInnerJoin() {
    }

    public void suppressInnerJoinAutoDetect() {
    }

    public void enablePagingCountLeastJoin() {
    }

    public void disablePagingCountLeastJoin() {
    }

    public boolean hasWhereClauseOnBaseQuery() {
        return false;
    }

    public boolean hasWhereClauseOnBaseTableInline() {
        return false;
    }

    public void clearWhereClauseOnBaseQuery() {
    }

    public void clearWhereClauseOnBaseTableInline() {
    }

    public boolean hasSelectAllPossible() {
        return false;
    }

    public boolean xhasDreamCruiseTicket() {
        return false;
    }

    public void overTheWaves(HpSpecifiedColumn dreamCruiseTicket) {
    }

    public ConditionBean xcreateDreamCruiseCB() {
        return null;
    }

    public boolean xisDreamCruiseShip() {
        return false;
    }

    public ConditionBean xgetDreamCruiseDeparturePort() {
        return null;
    }

    public HpSpecifiedColumn xshowDreamCruiseTicket() {
        return null;
    }

    public void xkeepDreamCruiseJourneyLogBook(String relationPath) {
    }

    public void xsetupSelectDreamCruiseJourneyLogBook() {
    }

    public void xsetupSelectDreamCruiseJourneyLogBookIfUnionExists() {
    }
}
