package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.ArrayList;
import java.util.List;

import org.seasar.dbflute.cbean.ListResultBean;

import com.example.dbflute.spring.dbflute.allcommon.CDef;
import com.example.dbflute.spring.dbflute.allcommon.CDef.MemberStatus;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberLoginCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberLoginBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberLogin;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBClassificationTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberLoginBhv memberLoginBhv;

    // ===================================================================================
    //                                                                              String
    //                                                                              ======
    // -----------------------------------------------------
    //                                                 Equal
    //                                                 -----
    public void test_String_equal_classfy() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberStatusCode_Equal_Formalized();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertTrue(member.isMemberStatusCodeFormalized());
        }
    }

    public void test_String_equal_asCDef_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberStatusCode_Equal_AsMemberStatus(CDef.MemberStatus.Formalized);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertTrue(member.isMemberStatusCodeFormalized());
        }
    }

    public void test_String_equal_asCDef_nullArg() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.query().setMemberStatusCode_Equal_AsMemberStatus(null);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        assertEquals(countAll, memberList.size());
    }

    public void test_String_equal_asBoolean_basic() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        cb.query().setMobileLoginFlg_Equal_AsBoolean(true);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgTrue());
        }
    }

    // -----------------------------------------------------
    //                                              NotEqual
    //                                              --------
    public void test_String_notEqual_asCDef_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberStatusCode_NotEqual_AsMemberStatus(CDef.MemberStatus.Formalized);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertFalse(member.isMemberStatusCodeFormalized());
        }
    }

    public void test_String_notEqual_asCDef_nullArg() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.query().setMemberStatusCode_NotEqual_AsMemberStatus(null);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        assertEquals(countAll, memberList.size());
    }

    // -----------------------------------------------------
    //                                               InScope
    //                                               -------
    public void test_String_inScope_asCDef_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        List<MemberStatus> statusList = new ArrayList<CDef.MemberStatus>();
        statusList.add(CDef.MemberStatus.Formalized);
        statusList.add(CDef.MemberStatus.Provisional);
        cb.query().setMemberStatusCode_InScope_AsMemberStatus(statusList);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        boolean existsFormalized = false;
        boolean existsProvisional = false;
        for (Member member : memberList) {
            if (member.isMemberStatusCodeFormalized()) {
                existsFormalized = true;
                continue;
            }
            if (member.isMemberStatusCodeProvisional()) {
                existsProvisional = true;
                continue;
            }
            fail(member.toString());
        }
        assertTrue(existsFormalized);
        assertTrue(existsProvisional);
    }

    public void test_String_inScope_asCDef_nullElement() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        List<MemberStatus> statusList = new ArrayList<CDef.MemberStatus>();
        statusList.add(null);
        statusList.add(CDef.MemberStatus.Provisional);
        cb.query().setMemberStatusCode_InScope_AsMemberStatus(statusList);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertTrue(member.isMemberStatusCodeProvisional());
        }
    }

    public void test_String_inScope_asCDef_nullList() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.query().setMemberStatusCode_InScope_AsMemberStatus(null);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        assertEquals(countAll, memberList.size());
    }

    // -----------------------------------------------------
    //                                            NotInScope
    //                                            ----------
    public void test_String_notInScope_asCDef_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        List<MemberStatus> statusList = new ArrayList<CDef.MemberStatus>();
        statusList.add(CDef.MemberStatus.Formalized);
        statusList.add(CDef.MemberStatus.Provisional);
        cb.query().setMemberStatusCode_NotInScope_AsMemberStatus(statusList);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertTrue(member.isMemberStatusCodeWithdrawal());
        }
    }

    public void test_String_notInScope_asCDef_nullElement() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        List<MemberStatus> statusList = new ArrayList<CDef.MemberStatus>();
        statusList.add(CDef.MemberStatus.Provisional);
        statusList.add(null);
        cb.query().setMemberStatusCode_NotInScope_AsMemberStatus(statusList);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            assertFalse(member.isMemberStatusCodeProvisional());
        }
    }

    public void test_String_notInScope_asCDef_nullList() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.query().setMemberStatusCode_NotInScope_AsMemberStatus(null);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        assertEquals(countAll, memberList.size());
    }

    // ===================================================================================
    //                                                                             Integer
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 Equal
    //                                                 -----
    public void test_Integer_equal_classify() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        cb.query().setMobileLoginFlg_Equal_True();

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_equal_asCDef_basic() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        cb.query().setMobileLoginFlg_Equal_AsFlg(CDef.Flg.True);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_equal_asCDef_nullArg() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        int countAll = memberLoginBhv.selectCount(cb);
        cb.query().setMobileLoginFlg_Equal_AsFlg(null);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        assertEquals(countAll, loginList.size());
    }

    // -----------------------------------------------------
    //                                              NotEqual
    //                                              --------
    public void test_Integer_notEqual_asCDef_basic() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        cb.query().setMobileLoginFlg_NotEqual_AsFlg(CDef.Flg.True);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertFalse(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_notEqual_asCDef_nullArg() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        int countAll = memberLoginBhv.selectCount(cb);
        cb.query().setMobileLoginFlg_NotEqual_AsFlg(null);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        assertEquals(countAll, loginList.size());
    }

    // -----------------------------------------------------
    //                                               InScope
    //                                               -------
    public void test_Integer_inScope_asCDef() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        List<CDef.Flg> flgList = new ArrayList<CDef.Flg>();
        flgList.add(CDef.Flg.True);
        cb.query().setMobileLoginFlg_InScope_AsFlg(flgList);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_inScope_asCDef_nullElement() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        List<CDef.Flg> flgList = new ArrayList<CDef.Flg>();
        flgList.add(null);
        flgList.add(CDef.Flg.True);
        cb.query().setMobileLoginFlg_InScope_AsFlg(flgList);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_inScope_asCDef_nullList() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        int countAll = memberLoginBhv.selectCount(cb);
        cb.query().setMobileLoginFlg_InScope_AsFlg(null);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        assertEquals(countAll, loginList.size());
    }

    // -----------------------------------------------------
    //                                            NotInScope
    //                                            ----------
    public void test_Integer_notInScope_asCDef() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        List<CDef.Flg> flgList = new ArrayList<CDef.Flg>();
        flgList.add(CDef.Flg.True);
        cb.query().setMobileLoginFlg_NotInScope_AsFlg(flgList);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertFalse(login.isMobileLoginFlgTrue());
        }
    }

    public void test_Integer_notInScope_asCDef_nullElement() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        List<CDef.Flg> flgList = new ArrayList<CDef.Flg>();
        flgList.add(null);
        flgList.add(CDef.Flg.True);
        cb.query().setMobileLoginFlg_NotInScope_AsFlg(flgList);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        for (MemberLogin login : loginList) {
            assertTrue(login.isMobileLoginFlgFalse());
        }
    }

    public void test_Integer_notInScope_asCDef_nullList() {
        // ## Arrange ##
        MemberLoginCB cb = new MemberLoginCB();
        int countAll = memberLoginBhv.selectCount(cb);
        cb.query().setMobileLoginFlg_NotInScope_AsFlg(null);

        // ## Act ##
        ListResultBean<MemberLogin> loginList = memberLoginBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(loginList);
        assertEquals(countAll, loginList.size());
    }
}
