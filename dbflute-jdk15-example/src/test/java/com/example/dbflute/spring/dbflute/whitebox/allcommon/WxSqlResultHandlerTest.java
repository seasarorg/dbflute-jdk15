package com.example.dbflute.spring.dbflute.whitebox.allcommon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.bhv.core.BehaviorCommandMeta;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.exception.EntityAlreadyUpdatedException;
import org.seasar.dbflute.jdbc.ExecutionTimeInfo;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.jdbc.SqlResultHandler;
import org.seasar.dbflute.jdbc.SqlResultInfo;
import org.seasar.dbflute.util.DfReflectionUtil;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.8.7 (2011/07/02 Saturday)
 */
public class WxSqlResultHandlerTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

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
        assertFalse(CallbackContext.isExistSqlFireHookOnThread());
        assertFalse(CallbackContext.isExistSqlResultHandlerOnThread());
    }

    // ===================================================================================
    //                                                                       ConditionBean
    //                                                                       =============
    public void test_ConditionBean_basic() {
        // ## Arrange ##
        final List<String> displaySqlList = new ArrayList<String>();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                // basic
                Object result = info.getResult();
                BehaviorCommandMeta meta = info.getMeta();
                String tableDbName = meta.getTableDbName();
                String commandName = meta.getCommandName();
                assertNotNull(result);
                assertEquals(MemberDbm.getInstance().getTableDbName(), tableDbName);
                if (meta.isSelectCount()) {
                    assertEquals("selectCount", commandName);
                } else {
                    assertEquals("selectList", commandName);
                }

                // displaySql
                assertNull(getCachedDisplaySql(info));
                String displaySql = info.getSqlLogInfo().getDisplaySql();
                String cachedDisplaySql = getCachedDisplaySql(info);
                assertNotNull(cachedDisplaySql);
                assertEquals(displaySql, cachedDisplaySql);
                assertNotNull(displaySql);

                // time
                ExecutionTimeInfo timeInfo = info.getExecutionTimeInfo();
                Long before = timeInfo.getCommandBeforeTimeMillis();
                Long after = timeInfo.getCommandAfterTimeMillis();
                Long sqlBefore = timeInfo.getSqlBeforeTimeMillis();
                Long sqlAfter = timeInfo.getSqlAfterTimeMillis();
                assertNotNull(before);
                assertNotNull(after);
                assertNotNull(sqlBefore);
                assertNotNull(sqlAfter);
                String commandView = timeInfo.toCommandPerformanceView();
                String sqlView = timeInfo.toSqlPerformanceView();

                // logging
                StringBuilder sb = new StringBuilder();
                sb.append(ln()).append(" /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                sb.append(ln()).append(" result=").append(result);
                String secondInfo = tableDbName + "." + commandName + "(), " + commandView + ", " + sqlView;
                sb.append(ln()).append(" ").append(secondInfo);
                sb.append(ln()).append(" - - - - - - - - - -/");
                log(sb);

                displaySqlList.add(displaySql);
            }
        });

        // ## Act ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberName_PrefixSearch("S");
        memberBhv.selectCount(cb);
        cb.query().setMemberStatusCode_Equal_Formalized();
        memberBhv.selectList(cb);
        cb.query().setBirthdate_IsNotNull();
        memberBhv.selectCount(cb);
        cb.query().setMemberName_LikeSearch("c", new LikeSearchOption().likeSuffix());
        memberBhv.selectList(cb);

        // ## Assert ##
        StringBuilder sb = new StringBuilder();
        sb.append(ln()).append("[Display SQL]");
        for (String displaySql : displaySqlList) {
            assertNotNull(displaySql);
            sb.append(ln()).append(" - - - - - - - - - -").append(ln()).append(displaySql);
        }
        log(sb);
        assertEquals(4, displaySqlList.size());
    }

    // ===================================================================================
    //                                                                       Entity Update
    //                                                                       =============
    public void test_EntityUpdate_insert_basic() {
        // ## Arrange ##
        Member member = new Member();
        member.setMemberName("$name");
        member.setMemberAccount("$account");
        member.setMemberStatusCode_Formalized();
        final List<String> displaySqlList = newArrayList();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                assertFalse(info.getMeta().isConditionBean());
                assertFalse(info.getMeta().isOutsideSql());
                assertFalse(info.getMeta().isProcedure());
                assertFalse(info.getMeta().isSelect());
                assertFalse(info.getMeta().isSelectCount());
                displaySqlList.add(info.getSqlLogInfo().getDisplaySql());
            }
        });

        // ## Act ##
        memberBhv.insert(member);

        // ## Assert ##
        showDisplaySqlList(displaySqlList);
        assertEquals(1, displaySqlList.size()); // contains identity selecting
        final Iterator<String> iterator = displaySqlList.iterator();
        String firstSql = iterator.next();
        assertTrue("firstSql: " + firstSql, Srl.containsAll(firstSql, "$name", "$account"));
    }

    public void test_EntityUpdate_update_basic() {
        // ## Arrange ##
        Member member = new Member();
        member.setMemberId(3);
        member.setMemberName("$name");
        member.setMemberAccount("$account");
        member.setMemberStatusCode_Formalized();
        member.setVersionNo(-99999L);
        final List<String> displaySqlList = newArrayList();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                displaySqlList.add(info.getSqlLogInfo().getDisplaySql());
            }
        });

        // ## Act ##
        try {
            memberBhv.update(member);

            fail();
        } catch (EntityAlreadyUpdatedException e) {
            // OK
            log(e.getMessage());
        }

        // ## Assert ##
        showDisplaySqlList(displaySqlList);
        assertEquals(1, displaySqlList.size()); // contains identity selecting
    }

    public void test_EntityUpdate_insertOrUpdate_onParade_withOptimisticLock() {
        // ## Arrange ##
        Member before = memberBhv.selectByPKValueWithDeletedCheck(3);
        Member member = new Member();
        member.setMemberId(99999);
        member.setMemberName("$name");
        member.setMemberAccount("$account");
        member.setMemberStatusCode_Formalized();
        member.setVersionNo(before.getVersionNo());
        final List<String> displaySqlList = newArrayList();
        final Set<RuntimeException> causeSet = new HashSet<RuntimeException>();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                displaySqlList.add(info.getSqlLogInfo().getDisplaySql());
                ExecutionTimeInfo timeInfo = info.getExecutionTimeInfo();
                RuntimeException cause = info.getCause();
                if (cause != null) {
                    causeSet.add(cause);
                    assertNotNull(timeInfo.getCommandBeforeTimeMillis());
                    assertNull(timeInfo.getCommandAfterTimeMillis());
                    assertTrue(timeInfo.toCommandPerformanceView().contains("No time"));
                    assertNotNull(timeInfo.getSqlBeforeTimeMillis());
                    assertNotNull(timeInfo.getSqlAfterTimeMillis());
                    assertFalse(timeInfo.toSqlPerformanceView().contains("No time"));
                } else {
                    assertNotNull(timeInfo.getCommandBeforeTimeMillis());
                    assertNotNull(timeInfo.getCommandAfterTimeMillis());
                    assertNotNull(timeInfo.getSqlBeforeTimeMillis());
                    assertNotNull(timeInfo.getSqlAfterTimeMillis());
                }
            }
        });

        // ## Act ##
        memberBhv.insertOrUpdate(member);

        // ## Assert ##
        showDisplaySqlList(displaySqlList);
        assertEquals(3, displaySqlList.size()); // contains identity selecting
        final Iterator<String> iterator = displaySqlList.iterator();
        String firstSql = iterator.next();
        assertTrue("firstSql: " + firstSql, Srl.containsAll(firstSql, "update ", "$name", "$account"));
        String secondSql = iterator.next();
        assertTrue("secondSql: " + secondSql, Srl.containsAll(secondSql, "select count(*)"));
        String thirdSql = iterator.next();
        assertTrue("thirdSql: " + thirdSql, Srl.containsAll(thirdSql, "$name", "$account"));
        assertEquals(1, causeSet.size());
        assertEquals(EntityAlreadyUpdatedException.class, causeSet.iterator().next().getClass());
    }

    public void test_EntityUpdate_insertOrUpdate_onParade_withoutOptimisticLock() {
        // ## Arrange ##
        MemberStatus status = new MemberStatus();
        status.setMemberStatusCode("ZZZ");
        status.setMemberStatusName("Test");
        status.setDisplayOrder(99999);
        status.setDescription("for test");
        final List<String> displaySqlList = newArrayList();
        final Set<RuntimeException> causeSet = new HashSet<RuntimeException>();
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                displaySqlList.add(info.getSqlLogInfo().getDisplaySql());
                ExecutionTimeInfo timeInfo = info.getExecutionTimeInfo();
                RuntimeException cause = info.getCause();
                if (cause != null) {
                    causeSet.add(cause);
                    assertNotNull(timeInfo.getCommandBeforeTimeMillis());
                    assertNull(timeInfo.getCommandAfterTimeMillis());
                    assertTrue(timeInfo.toCommandPerformanceView().contains("No time"));
                    assertNotNull(timeInfo.getSqlBeforeTimeMillis());
                    assertNotNull(timeInfo.getSqlAfterTimeMillis());
                    assertFalse(timeInfo.toSqlPerformanceView().contains("No time"));
                } else {
                    assertNotNull(timeInfo.getCommandBeforeTimeMillis());
                    assertNotNull(timeInfo.getCommandAfterTimeMillis());
                    assertNotNull(timeInfo.getSqlBeforeTimeMillis());
                    assertNotNull(timeInfo.getSqlAfterTimeMillis());
                }
            }
        });

        // ## Act ##
        memberStatusBhv.insertOrUpdate(status);

        // ## Assert ##
        showDisplaySqlList(displaySqlList);
        assertEquals(3, displaySqlList.size()); // contains identity selecting
        final Iterator<String> iterator = displaySqlList.iterator();
        String firstSql = iterator.next();
        assertTrue("firstSql: " + firstSql, Srl.containsAll(firstSql, "update ", "ZZZ", "Test"));
        String secondSql = iterator.next();
        assertTrue("secondSql: " + secondSql, Srl.containsAll(secondSql, "select count(*)"));
        String thirdSql = iterator.next();
        assertTrue("thirdSql: " + thirdSql, Srl.containsAll(thirdSql, "ZZZ", "Test"));
        assertEquals(0, causeSet.size());
    }

    // ===================================================================================
    //                                                                        Batch Update
    //                                                                        ============
    public void test_batchUpdate_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(newArrayList(1, 3, 7));
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        for (Member member : memberList) {
            member.setMemberStatusCode_Withdrawal();
        }
        CallbackContext callbackContext = new CallbackContext();
        final List<SqlResultInfo> infoList = new ArrayList<SqlResultInfo>();
        callbackContext.setSqlResultHandler(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                log(info);
                assertEquals("batchUpdateNonstrict", info.getMeta().getCommandName());
                ExecutionTimeInfo timeInfo = info.getExecutionTimeInfo();
                assertNotNull(timeInfo);
                Long sqlBeforeTimeMillis = timeInfo.getSqlBeforeTimeMillis();
                assertNotNull(sqlBeforeTimeMillis);
                Long sqlAfterTimeMillis = timeInfo.getSqlAfterTimeMillis();
                assertNotNull(sqlAfterTimeMillis);
                infoList.add(info);
            }
        });
        CallbackContext.setCallbackContextOnThread(callbackContext);
        try {
            // ## Act ##
            memberBhv.batchUpdateNonstrict(memberList, new SpecifyQuery<MemberCB>() {
                public void specify(MemberCB cb) {
                    cb.specify().columnMemberStatusCode();
                }
            });

            // ## Assert ##
            assertEquals(1, infoList.size());
        } finally {
            CallbackContext.clearCallbackContextOnThread();
        }
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected String getCachedDisplaySql(SqlResultInfo info) {
        SqlLogInfo sqlLogInfo = info.getSqlLogInfo();
        Field field = DfReflectionUtil.getWholeField(sqlLogInfo.getClass(), "_cachedDisplaySql");
        return (String) DfReflectionUtil.getValueForcedly(field, sqlLogInfo);
    }

    protected void showDisplaySqlList(List<String> displaySqlList) {
        StringBuilder sb = new StringBuilder();
        for (String displaySql : displaySqlList) {
            sb.append(ln()).append(displaySql);
        }
        log(sb.toString());
    }
}
