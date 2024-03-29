package com.example.dbflute.spring.dbflute.whitebox.cbean;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.exception.SpecifyColumnTwoOrMoreColumnException;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberServiceCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 */
public class WxCBMyselfInScopeTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_myselfInScope_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().setMemberStatusCode_Equal_Formalized();
        cb.query().myselfInScope(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.query().setMemberName_PrefixSearch("S");
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getMemberStatus().getMemberStatusName());
            assertTrue(member.isMemberStatusCodeFormalized());
            assertTrue(member.getMemberName().startsWith("S"));
        }
    }

    public void test_myselfInScope_OneToOne() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberServiceAsOne().withServiceRank();
        cb.query().setMemberStatusCode_Equal_Formalized();
        cb.query().queryMemberServiceAsOne().myselfInScope(new SubQuery<MemberServiceCB>() {
            public void query(MemberServiceCB subCB) {
                subCB.query().setServiceRankCode_Equal_Gold();
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getMemberStatusCode() + ", "
                    + member.getMemberServiceAsOne().getServiceRank().getServiceRankName());
            assertTrue(member.isMemberStatusCodeFormalized());
            assertTrue(member.getMemberServiceAsOne().isServiceRankCodeGold());
        }
    }

    // ===================================================================================
    //                                                                             Specify
    //                                                                             =======
    public void test_myselfInScope_specify_basic() {
        // ## Arrange ##
        String memberStatusCode = memberBhv.selectByPKValueWithDeletedCheck(3).getMemberStatusCode();
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().myselfInScope(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.specify().columnMemberStatusCode();
                subCB.query().setMemberId_Equal(3);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getMemberStatus().getMemberStatusName());
            assertEquals(memberStatusCode, member.getMemberStatusCode());
        }
    }

    public void test_myselfInScope_specify_nested() {
        // ## Arrange ##
        String memberStatusCode = memberBhv.selectByPKValueWithDeletedCheck(3).getMemberStatusCode();
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().myselfInScope(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.specify().columnMemberStatusCode();
                subCB.query().myselfInScope(new SubQuery<MemberCB>() {
                    public void query(MemberCB subCB) {
                        subCB.query().setMemberId_Equal(3);
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getMemberStatus().getMemberStatusName());
            assertEquals(memberStatusCode, member.getMemberStatusCode());
        }
    }

    public void test_myselfInScope_specify_duplicated() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();

        try {
            // ## Act ##
            cb.query().myselfInScope(new SubQuery<MemberCB>() {
                public void query(MemberCB subCB) {
                    subCB.specify().columnFormalizedDatetime();
                    subCB.specify().columnMemberStatusCode();
                    subCB.query().setMemberId_Equal(3);
                }
            });

            // ## Assert ##
            fail();
        } catch (SpecifyColumnTwoOrMoreColumnException e) {
            // OK
            log(e.getMessage());
        }
    }
}
