package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.List;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.jdbc.SqlResultHandler;
import org.seasar.dbflute.jdbc.SqlResultInfo;
import org.seasar.dbflute.util.DfTraceViewUtil;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.9.3D (2012/03/26 Monday)
 */
public class WxCBPerformanceTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                          After Care
    //                                                                          ==========
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        clearSqlResultHandler();
    }

    protected void clearSqlResultHandler() {
        CallbackContext.clearSqlResultHandlerOnThread();
        assertFalse(CallbackContext.isExistCallbackContextOnThread());
        assertFalse(CallbackContext.isExistBehaviorCommandHookOnThread());
        assertFalse(CallbackContext.isExistSqlLogHandlerOnThread());
        assertFalse(CallbackContext.isExistSqlResultHandlerOnThread());
    }

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_selectList_relation() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberSecurityAsOne();
        cb.setupSelect_MemberServiceAsOne().withServiceRank();
        cb.setupSelect_MemberWithdrawalAsOne().withWithdrawalReason();
        memberBhv.selectList(cb); // initialize

        // ## Act ##
        final List<SqlResultInfo> resultInfoList = newArrayList();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                resultInfoList.add(info);
            }
        });
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        assertListHasOneElement(resultInfoList);
        SqlResultInfo resultInfo = resultInfoList.get(0);
        Long commandBeforeTimeMillis = resultInfo.getExecutionTimeInfo().getCommandBeforeTimeMillis();
        Long commandAfterTimeMillis = resultInfo.getExecutionTimeInfo().getCommandAfterTimeMillis();
        Long sqlBeforeTimeMillis = resultInfo.getExecutionTimeInfo().getSqlBeforeTimeMillis();
        Long sqlAfterTimeMillis = resultInfo.getExecutionTimeInfo().getSqlAfterTimeMillis();
        String commandCost = DfTraceViewUtil.convertToPerformanceView(commandAfterTimeMillis - commandBeforeTimeMillis);
        String sqlCost = DfTraceViewUtil.convertToPerformanceView(sqlAfterTimeMillis - sqlBeforeTimeMillis);
        log("command=" + commandCost + ", sql=" + sqlCost);
    }
}
