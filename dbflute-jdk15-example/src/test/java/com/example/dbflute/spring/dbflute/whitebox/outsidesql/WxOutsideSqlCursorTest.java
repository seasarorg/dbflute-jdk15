package com.example.dbflute.spring.dbflute.whitebox.outsidesql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.cbean.ListResultBean;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberStatusCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursor;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursorHandler;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PurchaseSummaryMemberPmb;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 */
public class WxOutsideSqlCursorTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                  Insert with Cursor
    //                                                                  ==================
    public void test_selectCursor_insertWithCursor_sameTable() throws Exception {
        // ## Arrange ##
        PurchaseSummaryMemberPmb pmb = new PurchaseSummaryMemberPmb();
        pmb.setMemberStatusCode_Formalized();
        final List<Integer> memberIdList = new ArrayList<Integer>();
        final PurchaseSummaryMemberCursorHandler handler = new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(PurchaseSummaryMemberCursor cursor) throws SQLException {
                int count = 0;
                while (cursor.next()) {
                    final String memberName = cursor.getMemberName();
                    Member member = new Member();
                    member.setMemberName(memberName + count);
                    member.setMemberAccount(memberName + count);
                    member.setMemberStatusCode_Formalized();
                    memberBhv.insert(member);
                    memberIdList.add(member.getMemberId());
                    ++count;
                }
                return null;
            }
        };

        // ## Act ##
        memberBhv.outsideSql().cursorHandling().selectCursor(pmb, handler);

        // ## Assert ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_InScope(memberIdList);
        assertNotSame(0, memberBhv.selectCount(cb));
    }

    public void test_selectCursor_insertWithCursor_diffTable() throws Exception {
        // ## Arrange ##
        String path = MemberBhv.PATH_selectPurchaseSummaryMember;
        PurchaseSummaryMemberPmb pmb = new PurchaseSummaryMemberPmb();
        pmb.setMemberStatusCode_Formalized();
        final List<String> codeList = new ArrayList<String>();
        final PurchaseSummaryMemberCursorHandler handler = new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(PurchaseSummaryMemberCursor cursor) throws SQLException {
                int count = 0;
                while (cursor.next()) {
                    final String memberName = cursor.getMemberName();
                    MemberStatus memberStatus = new MemberStatus();
                    String memberStatusCode;
                    if (count >= 100) {
                        memberStatusCode = String.valueOf(count);
                    } else if (count >= 10) {
                        memberStatusCode = "0" + count;
                    } else {
                        memberStatusCode = "00" + count;
                    }
                    memberStatus.setMemberStatusCode(memberStatusCode);
                    memberStatus.setMemberStatusName(memberName + count);
                    memberStatus.setDescription("foo");
                    memberStatus.setDisplayOrder(99999 + count);
                    memberStatusBhv.insert(memberStatus);
                    codeList.add(memberStatus.getMemberStatusCode());
                    ++count;
                }
                return null;
            }
        };

        // ## Act ##
        memberBhv.outsideSql().cursorHandling().selectCursor(path, pmb, handler);

        // ## Assert ##
        MemberStatusCB cb = new MemberStatusCB();
        cb.query().setMemberStatusCode_InScope(codeList);
        assertNotSame(0, memberStatusBhv.selectCount(cb));
    }

    // ===================================================================================
    //                                                                       Nested Cursor
    //                                                                       =============
    public void test_selectCursor_nestedCursor_basic() throws Exception {
        // ## Arrange ##
        int countAll = memberBhv.selectCount(new MemberCB());
        {
            Member member = new Member();
            member.setMemberStatusCode_Withdrawal();
            MemberCB cb = new MemberCB();
            cb.query().setMemberStatusCode_Equal_Provisional();
            memberBhv.queryUpdate(member, cb);
        }
        int withdrawalCountAll;
        {
            MemberCB cb = new MemberCB();
            cb.query().setMemberStatusCode_Equal_Withdrawal();
            withdrawalCountAll = memberBhv.selectCount(cb);
        }
        final Map<Integer, Member> memberMap = new HashMap<Integer, Member>();
        {
            MemberCB cb = new MemberCB();
            ListResultBean<Member> beforeList = memberBhv.selectList(cb);
            for (Member member : beforeList) {
                memberMap.put(member.getMemberId(), member);
            }
        }
        PurchaseSummaryMemberPmb pmbFirst = new PurchaseSummaryMemberPmb();
        pmbFirst.setMemberStatusCode_Formalized();
        final List<Member> memberList = new ArrayList<Member>();

        // ## Act ##
        memberBhv.outsideSql().cursorHandling().selectCursor(pmbFirst, new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(final PurchaseSummaryMemberCursor firstCursor) throws SQLException {
                PurchaseSummaryMemberPmb pmbSecond = new PurchaseSummaryMemberPmb();
                pmbSecond.setMemberStatusCode_Withdrawal();
                memberBhv.outsideSql().cursorHandling()
                        .selectCursor(pmbSecond, new PurchaseSummaryMemberCursorHandler() {
                            protected Object fetchCursor(PurchaseSummaryMemberCursor secondCursor) throws SQLException {
                                while (firstCursor.next()) {
                                    // first process
                                    memberList.add(memberMap.get(firstCursor.getMemberId()));

                                    // second process
                                    if (secondCursor.next()) {
                                        memberList.add(memberMap.get(secondCursor.getMemberId()));
                                    }
                                }
                                return null;
                            }
                        });
                return null;
            }
        });

        // ## Assert ##
        assertListNotEmpty(memberList);
        boolean first = true;
        int currentWithdrawalCount = 0;
        boolean existsWithdrawal = false;
        boolean existsOverFormalized = false;
        for (Member member : memberList) {
            log(member.getMemberId() + ", " + member.getMemberStatusCode());
            if (first) {
                assertTrue(member.isMemberStatusCodeFormalized());
                first = false;
            } else {
                ++currentWithdrawalCount;
                if (currentWithdrawalCount <= withdrawalCountAll) {
                    assertTrue(member.isMemberStatusCodeWithdrawal());
                    existsWithdrawal = true;
                } else {
                    assertTrue(member.isMemberStatusCodeFormalized());
                    existsOverFormalized = true;
                }
                first = true;
            }
        }
        assertTrue(existsWithdrawal);
        assertTrue(existsOverFormalized);
        assertEquals(countAll, memberList.size());
    }
}
