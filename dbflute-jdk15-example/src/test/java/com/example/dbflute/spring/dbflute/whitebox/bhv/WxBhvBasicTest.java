package com.example.dbflute.spring.dbflute.whitebox.bhv;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.dbflute.bhv.core.BehaviorCommandInvoker;
import org.seasar.dbflute.cbean.ScalarQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.exception.DangerousResultSizeException;
import org.seasar.dbflute.exception.EntityDuplicatedException;
import org.seasar.dbflute.exception.SelectEntityConditionNotFoundException;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursor;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursorHandler;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PurchaseSummaryMemberPmb;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxBhvBasicTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private BehaviorCommandInvoker behaviorCommandInvoker;

    // ===================================================================================
    //                                                                       Entity Select
    //                                                                       =============
    public void test_selectEntity_duplicateResult() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(Arrays.asList(new Integer[] { 3, 5 }));

        // ## Act ##
        try {
            memberBhv.selectEntity(cb);

            // ## Assert ##
            fail();
        } catch (EntityDuplicatedException e) {
            // OK
            log(e.getMessage());
            Throwable cause = e.getCause();
            assertEquals(cause.getClass(), DangerousResultSizeException.class);
        }
    }

    public void test_selectEntity_conditionNotFound() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            memberBhv.selectEntity(cb);

            // ## Assert ##
            fail();
        } catch (SelectEntityConditionNotFoundException e) {
            // OK
            log(e.getMessage());
            assertFalse(Srl.contains(e.getMessage(), "MEMBER_ID equal"));
        }

        // ## Act ##
        try {
            memberBhv.selectEntityWithDeletedCheck(cb);

            // ## Assert ##
            fail();
        } catch (SelectEntityConditionNotFoundException e) {
            // OK
            log(e.getMessage());
            assertFalse(Srl.contains(e.getMessage(), "MEMBER_ID equal"));
        }

        // ## Act ##
        try {
            cb.query().setMemberId_Equal(null);
            memberBhv.selectEntity(cb);

            // ## Assert ##
            fail();
        } catch (SelectEntityConditionNotFoundException e) {
            // OK
            log(e.getMessage());
            assertTrue(Srl.containsAll(e.getMessage(), "MEMBER_ID equal", "query()"));
        }

        // ## Act ##
        try {
            cb.fetchFirst(1983);
            memberBhv.selectEntity(cb);

            // ## Assert ##
            fail();
        } catch (SelectEntityConditionNotFoundException e) {
            // OK
            log(e.getMessage());
            assertTrue(Srl.contains(e.getMessage(), "1983"));
        }

        // ## Act ##
        cb.fetchFirst(1);
        Member member = memberBhv.selectEntity(cb);

        // ## Assert ##
        assertNotNull(member);
    }

    public void test_selectByPKValue_nullArgument() {
        // ## Arrange ##
        try {
            // ## Act ##
            memberBhv.selectByPKValue(null);

            // ## Assert ##
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }

        // ## Arrange ##
        try {
            // ## Act ##
            memberBhv.selectByPKValueWithDeletedCheck(null);

            // ## Assert ##
            fail();
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                       Cursor Select
    //                                                                       =============
    public void test_outsideSql_selectCursor_handling() throws Exception {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseSummaryMember;
        PurchaseSummaryMemberPmb pmb = new PurchaseSummaryMemberPmb();
        pmb.setMemberStatusCode_Formalized();
        final Set<String> markSet = new HashSet<String>();
        PurchaseSummaryMemberCursorHandler handler = new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(PurchaseSummaryMemberCursor cursor) throws SQLException {
                while (cursor.next()) {
                    Integer memberId = cursor.getMemberId();
                    String memberName = cursor.getMemberName();
                    Timestamp formalizedDatetime = cursor.getFormalizedDatetime();
                    assertNotNull(memberId);
                    assertNotNull(memberName);
                    assertNotNull(formalizedDatetime); // because status is 'formalized'
                    markSet.add("cursor.next()");
                    log(memberId + ", " + memberName + ", " + formalizedDatetime);
                }
                markSet.add("fetchCursor");
                return null;
            }
        };

        // ## Act ##
        memberBhv.outsideSql().cursorHandling().selectCursor(path, pmb, handler);

        // ## Assert ##
        assertTrue(markSet.contains("cursor.next()"));
        assertTrue(markSet.contains("fetchCursor"));
    }

    public void test_outsideSql_selectCursor_initialized() throws Exception {
        // ## Arrange ##
        behaviorCommandInvoker.clearExecutionCache();
        String path = MemberBhv.PATH_selectPurchaseSummaryMember;
        PurchaseSummaryMemberPmb pmb = new PurchaseSummaryMemberPmb();
        pmb.setMemberStatusCode_Formalized();
        PurchaseSummaryMemberCursorHandler handler = new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(PurchaseSummaryMemberCursor cursor) throws SQLException {
                while (cursor.next()) {
                }
                return null;
            }
        };

        // ## Act & Assert ##
        memberBhv.outsideSql().cursorHandling().selectCursor(path, pmb, handler);
        assertEquals(1, behaviorCommandInvoker.getExecutionCacheSize());
        memberBhv.outsideSql().cursorHandling().selectCursor(path, pmb, handler);
        assertEquals(1, behaviorCommandInvoker.getExecutionCacheSize()); // should be reused!
    }

    // ===================================================================================
    //                                                                       Scalar Select
    //                                                                       =============
    public void test_scalarSelect_max_union() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().columnRegisterDatetime();
        cb.query().setMemberStatusCode_Equal_Formalized();
        cb.query().addOrderBy_RegisterDatetime_Desc();
        cb.fetchFirst(1);
        Timestamp expected1 = memberBhv.selectEntityWithDeletedCheck(cb).getRegisterDatetime();
        cb.query().setMemberStatusCode_Equal_Withdrawal();
        Timestamp expected2 = memberBhv.selectEntityWithDeletedCheck(cb).getRegisterDatetime();
        Timestamp expected = expected1.compareTo(expected2) > 0 ? expected1 : expected2;

        // ## Act ##
        Timestamp registerDatetime = memberBhv.scalarSelect(Timestamp.class).max(new ScalarQuery<MemberCB>() {
            public void query(MemberCB cb) {
                cb.specify().columnRegisterDatetime(); // *Point!
                cb.query().setMemberStatusCode_Equal_Formalized();
                cb.union(new UnionQuery<MemberCB>() {
                    public void query(MemberCB unionCB) {
                        unionCB.query().setMemberStatusCode_Equal_Withdrawal();
                    }
                });
            }
        });

        // ## Assert ##
        assertEquals(expected, registerDatetime);
    }

    // ===================================================================================
    //                                                                            Sequence
    //                                                                            ========
    public void test_readNextVal_unsupported() {
        try {
            memberBhv.readNextVal();

            fail("this project does not use sequence!");
        } catch (UnsupportedOperationException e) {
            // OK
            log(e.getMessage());
        }
    }
}
