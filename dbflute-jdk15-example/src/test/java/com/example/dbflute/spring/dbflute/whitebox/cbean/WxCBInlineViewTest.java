package com.example.dbflute.spring.dbflute.whitebox.cbean;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.exception.IllegalConditionBeanOperationException;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberLoginCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBInlineViewTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    public void test_inline_local_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().inline().setMemberName_PrefixSearch("S");

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            assertTrue(member.getMemberName().startsWith("S"));
        }
        assertTrue(cb.toDisplaySql().contains(" dfinlineloc.MEMBER_NAME like 'S%' escape '|'"));
    }

    public void test_inline_local_InScopeRelation_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().inline().setMemberName_PrefixSearch("S");
        cb.query().inline().inScopePurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().setPaymentCompleteFlg_Equal_True();
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            assertTrue(member.getMemberName().startsWith("S"));
        }
        assertTrue(cb.toDisplaySql().contains(" dfinlineloc.MEMBER_NAME like 'S%' escape '|'"));
    }

    public void test_inline_local_InScopeRelation_withVarious() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.setupSelect_MemberAddressAsValid(currentDate());
        cb.query().inline().setMemberName_PrefixSearch("S");
        cb.query().inline().inScopePurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().setPaymentCompleteFlg_Equal_True();
            }
        });
        cb.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().setPaymentCompleteFlg_Equal_True();
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            assertTrue(member.getMemberName().startsWith("S"));
        }
        assertTrue(cb.toDisplaySql().contains(" dfinlineloc.MEMBER_NAME like 'S%' escape '|'"));
    }

    public void test_inline_local_ExistsReferrer() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().inline().setMemberName_PrefixSearch("S");

        // ## Act ##
        try {
            cb.query().inline().existsPurchaseList(new SubQuery<PurchaseCB>() {
                public void query(PurchaseCB subCB) {
                    subCB.query().setPaymentCompleteFlg_Equal_True();
                }
            });

            // ## Assert ##
            fail();
        } catch (IllegalConditionBeanOperationException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_inline_relation_InScopeRelation_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.query().queryMemberStatus().inline().setDisplayOrder_GreaterEqual(2);
        cb.query().queryMemberStatus().inline().inScopeMemberLoginList(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.query().setMobileLoginFlg_Equal_True();
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        assertEquals(countAll, memberList.size());
        assertTrue(cb.toDisplaySql().contains("where dfinlineloc.DISPLAY_ORDER >= 2"));
        assertTrue(cb.toDisplaySql().contains(".MOBILE_LOGIN_FLG = 1"));
    }
}
