package com.example.dbflute.spring.dbflute.whitebox.bhv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.seasar.dbflute.CallbackContext;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.exception.BatchEntityAlreadyUpdatedException;
import org.seasar.dbflute.exception.SQLFailureException;
import org.seasar.dbflute.exception.SpecifyUpdateColumnInvalidException;
import org.seasar.dbflute.jdbc.SqlLogHandler;
import org.seasar.dbflute.jdbc.SqlLogInfo;
import org.seasar.dbflute.jdbc.SqlResultHandler;
import org.seasar.dbflute.jdbc.SqlResultInfo;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberStatusCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxBhvBatchUpdateTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_batchUpdate_basic() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        int count = 0;
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        for (Member member : memberList) {
            member.setMemberName("testName" + count);
            member.setMemberAccount("testAccount" + count);
            member.setMemberStatusCode_Provisional();
            member.setFormalizedDatetime(currentTimestamp());
            member.setBirthdate(currentTimestamp());
            expectedVersionNoList.add(member.getVersionNo());
            ++count;
        }

        // ## Act ##
        int[] result = memberBhv.batchUpdate(memberList);

        // ## Assert ##
        assertEquals(3, result.length);
        List<Long> actualVersionNoList = new ArrayList<Long>();
        for (Member member : memberList) {
            actualVersionNoList.add(member.getVersionNo());
        }
        assertNotSame(expectedVersionNoList, actualVersionNoList);
        int index = 0;
        for (Long versionNo : expectedVersionNoList) {
            assertEquals(Long.valueOf(versionNo + 1L), actualVersionNoList.get(index));
            ++index;
        }
    }

    public void test_batchUpdate_emptyList() throws Exception {
        // ## Arrange ##
        List<Member> memberList = new ArrayList<Member>();

        // ## Act ##
        int[] result = memberBhv.batchUpdate(memberList);

        // ## Assert ##
        assertEquals(0, result.length);
    }

    public void test_batchUpdate_nullList() throws Exception {
        // ## Arrange ##
        List<Member> memberList = null;

        // ## Act ##
        try {
            int[] result = memberBhv.batchUpdate(memberList);

            // ## Assert ##
            fail("result=" + result);
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_batchUpdate_sqlFailure_messageSql() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        for (Member member : memberList) {
            member.setMemberName("testName");
            member.setMemberAccount("testAccount");
        }

        // ## Act ##
        try {
            int[] result = memberBhv.batchUpdate(memberList);

            // ## Assert ##
            fail("result=" + Arrays.asList(result));
        } catch (SQLFailureException e) {
            // OK
            String msg = e.getMessage();
            log(msg);
            // last record's SQL
            assertTrue(Srl.containsAll(msg, "Display SQL", "update MEMBER", " where MEMBER_ID = 7"));
            String dispRear = Srl.substringLastRear(msg, "Display SQL");
            assertTrue(Srl.containsAll(dispRear, "update MEMBER", " where MEMBER_ID = 7"));
        }
    }

    // ===================================================================================
    //                                                                 SpecifyUpdateColumn
    //                                                                 ===================
    public void test_batchUpdate_specifyUpdateColumn_basic() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        {
            int count = 0;
            for (Member member : memberList) {
                member.setMemberName("testName" + count);
                member.setMemberAccount("testAccount" + count);
                member.setMemberStatusCode_Provisional();
                member.setFormalizedDatetime(currentTimestamp());
                member.setBirthdate(currentTimestamp());
                expectedVersionNoList.add(member.getVersionNo());
                ++count;
            }
        }

        // first
        {
            // ## Act ##
            int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
                public void specify(MemberCB cb) {
                    cb.specify().columnMemberName();
                }
            });

            // ## Assert ##
            assertEquals(3, result.length);
            List<Long> afterVersionNoList = new ArrayList<Long>();
            for (Member member : memberList) {
                afterVersionNoList.add(member.getVersionNo());
            }
            ListResultBean<Member> actualList = memberBhv.selectList(cb);
            List<Long> actualVersionNoList = new ArrayList<Long>();
            for (Member member : actualList) {
                assertTrue(Srl.startsWith(member.getMemberName(), "testName"));
                assertFalse(Srl.startsWith(member.getMemberAccount(), "testAccount"));
                assertNotSame(getAccessContext().getAccessUser(), member.getRegisterUser());
                assertEquals(getAccessContext().getAccessUser(), member.getUpdateUser());
                actualVersionNoList.add(member.getVersionNo());
            }
            assertNotSame(expectedVersionNoList, afterVersionNoList);
            assertNotSame(expectedVersionNoList, actualVersionNoList);
            assertEquals(afterVersionNoList, actualVersionNoList);
            int index = 0;
            for (Long versionNo : expectedVersionNoList) {
                assertEquals(Long.valueOf(versionNo + 1L), actualVersionNoList.get(index));
                ++index;
            }
        }

        // increment version no
        {
            Member member = new Member();
            member.setMemberId(3); // only one record
            member.setBirthdate(currentDate());
            memberBhv.updateNonstrict(member);
        }

        // retry other columns
        {
            // ## Arrange ##
            memberList = memberBhv.selectList(cb);
            int count = 0;
            for (Member member : memberList) {
                member.setMemberName("retryName" + count);
                member.setMemberAccount("retryName" + count);
                member.setFormalizedDatetime(null);
                ++count;
            }

            // ## Act ##
            int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
                public void specify(MemberCB cb) {
                    cb.specify().columnMemberAccount();
                    cb.specify().columnFormalizedDatetime();
                }
            });

            // ## Assert ##
            assertEquals(3, result.length);
            List<Long> afterVersionNoList = new ArrayList<Long>();
            for (Member member : memberList) {
                afterVersionNoList.add(member.getVersionNo());
            }
            ListResultBean<Member> actualList = memberBhv.selectList(cb);
            List<Long> actualVersionNoList = new ArrayList<Long>();
            for (Member member : actualList) {
                assertTrue(Srl.startsWith(member.getMemberName(), "testName"));
                assertTrue(Srl.startsWith(member.getMemberAccount(), "retryName"));
                assertNull(member.getFormalizedDatetime());
                assertNotSame(getAccessContext().getAccessUser(), member.getRegisterUser());
                assertEquals(getAccessContext().getAccessUser(), member.getUpdateUser());
                actualVersionNoList.add(member.getVersionNo());
            }
            assertNotSame(expectedVersionNoList, afterVersionNoList);
            assertNotSame(expectedVersionNoList, actualVersionNoList);
            assertEquals(afterVersionNoList, actualVersionNoList);
            log(actualVersionNoList);
            // because only one record has been updated
            assertTrue(new HashSet<Long>(actualVersionNoList).size() > 1);
        }

        // exclusive control
        try {
            // ## Arrange ##
            for (Member member : memberList) {
                member.setVersionNo(0L);
            }

            // ## Act ##
            int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
                public void specify(MemberCB cb) {
                    cb.specify().columnMemberAccount();
                }
            });

            // ## Assert ##
            fail("result = " + result.length);
        } catch (BatchEntityAlreadyUpdatedException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_batchUpdate_specifyUpdateColumn_disableCommonColumn() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        {
            int count = 0;
            for (Member member : memberList) {
                member.setMemberName("testName" + count);
                member.setMemberAccount("testAccount" + count);
                member.setMemberStatusCode_Provisional();
                member.setUpdateUser("disable test");
                member.disableCommonColumnAutoSetup();
                expectedVersionNoList.add(member.getVersionNo());
                ++count;
            }
        }

        // ## Act ##
        int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
                cb.specify().columnMemberName();
            }
        });

        // ## Assert ##
        assertEquals(3, result.length);
        assertEquals(Long.valueOf(expectedVersionNoList.get(0) + 1L), memberList.get(0).getVersionNo());
        memberList = memberBhv.selectList(cb);
        assertEquals("disable test", memberList.get(0).getUpdateUser());
        assertEquals("disable test", memberList.get(1).getUpdateUser());
        assertEquals("disable test", memberList.get(2).getUpdateUser());
        assertEquals(Long.valueOf(expectedVersionNoList.get(0) + 1L), memberList.get(0).getVersionNo());
    }

    public void test_batchUpdate_specifyUpdateColumn_emptySpecification_existsCommonColumn() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        {
            int count = 0;
            for (Member member : memberList) {
                member.setMemberName("testName" + count);
                member.setMemberAccount("testAccount" + count);
                member.setMemberStatusCode_Provisional();
                expectedVersionNoList.add(member.getVersionNo());
                ++count;
            }
        }

        // ## Act ##
        int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
            }
        });

        // ## Assert ##
        assertEquals(3, result.length); // because common columns exist
        assertEquals(Long.valueOf(expectedVersionNoList.get(0) + 1L), memberList.get(0).getVersionNo());
        memberList = memberBhv.selectList(cb);
        assertFalse(memberList.get(0).getMemberName().startsWith("test"));
        assertFalse(memberList.get(1).getMemberName().startsWith("test"));
        assertFalse(memberList.get(2).getMemberName().startsWith("test"));
        assertEquals(Long.valueOf(expectedVersionNoList.get(0) + 1L), memberList.get(0).getVersionNo());
    }

    public void test_batchUpdate_specifyUpdateColumn_emptySpecification_noCommonColumn() throws Exception {
        // ## Arrange ##
        MemberStatusCB cb = new MemberStatusCB();
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb);
        {
            int count = 0;
            for (MemberStatus status : statusList) {
                status.setMemberStatusName("testName" + count);
                ++count;
            }
        }

        // ## Act ##
        int[] result = memberStatusBhv.batchUpdate(statusList, new SpecifyQuery<MemberStatusCB>() {
            public void specify(MemberStatusCB cb) {
            }
        });

        // ## Assert ##
        // skipped because the table does not have common columns and version no
        assertEquals(0, result.length);
    }

    public void test_batchUpdate_specifyUpdateColumn_invalidSpecification() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Act ##
        int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
                cb.specify().columnUpdateUser();
                cb.specify().columnVersionNo();
            }
        });

        // ## Assert ##
        assertEquals(3, result.length);
    }

    public void test_batchUpdate_specifyUpdateColumn_primaryKeySpecification() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Act ##
        try {
            int[] result = memberBhv.batchUpdate(memberList, new SpecifyQuery<MemberCB>() {
                public void specify(MemberCB cb) {
                    cb.specify().columnMemberId();
                }
            });

            // ## Assert ##
            fail("result=" + result.length);
        } catch (SpecifyUpdateColumnInvalidException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_batchUpdate_specifyUpdateColumn_nullSpecification() throws Exception {
        // ## Arrange ##
        List<Member> memberList = new ArrayList<Member>();

        // ## Act ##
        try {
            int[] result = memberBhv.batchUpdate(memberList, null);

            // ## Assert ##
            fail("result=" + result);
        } catch (IllegalArgumentException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                       SqlLogHandler
    //                                                                       =============
    public void test_batchUpdate_SqlLogHandler() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        int count = 0;
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        for (Member member : memberList) {
            member.setMemberName("testName" + count);
            expectedVersionNoList.add(member.getVersionNo());
            ++count;
        }

        // ## Act ##
        final List<String> executedSqlList = new ArrayList<String>();
        final List<String> displaySqlList = new ArrayList<String>();
        CallbackContext.setSqlLogHandlerOnThread(new SqlLogHandler() {
            public void handle(SqlLogInfo info) {
                executedSqlList.add(info.getExecutedSql());
                displaySqlList.add(info.getDisplaySql());
            }
        });
        CallbackContext.setSqlResultHandlerOnThread(new SqlResultHandler() {
            public void handle(SqlResultInfo info) {
                assertEquals(displaySqlList.size(), Srl.count(info.getSqlLogInfo().getDisplaySql(), "update "));
                log("[DisplaySql on ResultInfo]");
                log(ln() + info.getSqlLogInfo().getDisplaySql());
            }
        });
        try {
            int[] result = memberBhv.batchUpdate(memberList);

            // ## Assert ##
            assertEquals(3, result.length);
            assertEquals(memberIdList.size(), executedSqlList.size());
            assertEquals(memberIdList.size(), displaySqlList.size());
            log("[DisplaySql on LogHandler]");
            for (String displaySql : displaySqlList) {
                log(ln() + displaySql);
            }
        } finally {
            CallbackContext.clearSqlLogHandlerOnThread();
            CallbackContext.clearSqlResultHandlerOnThread();
        }
    }

    // ===================================================================================
    //                                                                           Nonstrict
    //                                                                           =========
    public void test_batchUpdateNonstrict_basic() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        int count = 0;
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        for (Member member : memberList) {
            member.setMemberName("testName" + count);
            member.setMemberAccount("testAccount" + count);
            member.setMemberStatusCode_Provisional();
            member.setFormalizedDatetime(currentTimestamp());
            member.setBirthdate(currentTimestamp());
            expectedVersionNoList.add(member.getVersionNo());
            member.setVersionNo(null);
            ++count;
        }

        // ## Act ##
        int[] result = memberBhv.batchUpdateNonstrict(memberList);

        // ## Assert ##
        assertEquals(3, result.length);
        for (Member member : memberList) {
            assertNull(member.getVersionNo());
        }
    }

    public void test_batchUpdateNonstrict_specifyUpdateColumn_basic() throws Exception {
        // ## Arrange ##
        List<Integer> memberIdList = new ArrayList<Integer>();
        memberIdList.add(1);
        memberIdList.add(3);
        memberIdList.add(7);
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        ListResultBean<Member> memberList = memberBhv.selectList(cb);
        List<Long> expectedVersionNoList = new ArrayList<Long>();
        {
            int count = 0;
            for (Member member : memberList) {
                member.setMemberName("testName" + count);
                member.setMemberAccount("testAccount" + count);
                member.setMemberStatusCode_Provisional();
                expectedVersionNoList.add(member.getVersionNo());
                ++count;
            }
        }

        // ## Act ##
        int[] result = memberBhv.batchUpdateNonstrict(memberList, new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
                cb.specify().columnMemberName();
            }
        });

        // ## Assert ##
        assertEquals(3, result.length);
        assertEquals(Long.valueOf(expectedVersionNoList.get(0)), memberList.get(0).getVersionNo());

        memberList = memberBhv.selectList(cb);
        assertTrue(memberList.get(0).getMemberName().startsWith("test"));
        assertTrue(memberList.get(1).getMemberName().startsWith("test"));
        assertTrue(memberList.get(2).getMemberName().startsWith("test"));
        assertFalse(memberList.get(0).getMemberAccount().startsWith("test"));
        assertFalse(memberList.get(1).getMemberAccount().startsWith("test"));
        assertFalse(memberList.get(2).getMemberAccount().startsWith("test"));
        assertEquals(Long.valueOf(expectedVersionNoList.get(0) + 1L), memberList.get(0).getVersionNo());
    }
}
