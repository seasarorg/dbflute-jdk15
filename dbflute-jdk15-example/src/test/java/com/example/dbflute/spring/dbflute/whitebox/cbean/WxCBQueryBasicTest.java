package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.seasar.dbflute.cbean.AndQuery;
import org.seasar.dbflute.cbean.OrQuery;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.coption.FromToOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBQueryBasicTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_query_bindValue() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_Equal(1);
        cb.query().setMemberId_NotEqual(2);
        cb.query().setMemberId_GreaterThan(3);
        cb.query().setMemberId_LessThan(4);
        cb.query().setMemberId_GreaterEqual(5);
        cb.query().setMemberId_LessEqual(99);
        cb.query().setMemberId_LessEqual(6); // override
        cb.query().setMemberId_IsNotNull();
        cb.query().setMemberId_IsNull();
        cb.query().setMemberId_IsNotNull(); // ignored
        cb.query().setMemberId_IsNull(); // ignored
        cb.query().setMemberId_InScope(DfCollectionUtil.newArrayList(1, 2, 3));
        cb.query().setMemberId_InScope(DfCollectionUtil.newArrayList(4, 5, 6)); // append
        cb.query().setMemberId_NotInScope(DfCollectionUtil.newArrayList(7, 8, 9));
        cb.query().setMemberName_Equal("Foo");
        cb.query().setMemberName_PrefixSearch("A");
        cb.query().setMemberName_LikeSearch("B%|B", new LikeSearchOption().likeSuffix());
        cb.query().setMemberName_LikeSearch("C", new LikeSearchOption().likeContain()); // append
        cb.query().inline().setBirthdate_Equal(DfTypeUtil.toDate("2099/07/08"));
        cb.query().inline().setBirthdate_Equal(DfTypeUtil.toDate("2010/07/08")); // override
        cb.query().inline().setMemberId_NotEqual(72); // independent
        cb.query().inline().setMemberId_GreaterEqual(75); // independent
        cb.query().setBirthdate_Equal(DfTypeUtil.toDate("2010/07/08")); // ignored
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberId_Equal(11);
                orCB.query().setMemberId_Equal(12);
                orCB.query().setMemberId_Equal(13);
                orCB.query().setMemberId_NotEqual(21);
                orCB.query().setMemberId_NotEqual(22);
                orCB.query().setMemberId_NotEqual(23);
                orCB.query().setMemberName_PrefixSearch("X");
                orCB.query().setMemberName_LikeSearch("Y", new LikeSearchOption().likeSuffix());
                orCB.query().setMemberName_LikeSearch("Z%|Z", new LikeSearchOption().likeContain()); // append
                orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                    public void query(MemberCB andCB) {
                        andCB.query().setMemberId_Equal(31);
                        andCB.query().setMemberId_Equal(32);
                    }
                });
                orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                    public void query(MemberCB andCB) {
                        andCB.query().setMemberId_Equal(33);
                        andCB.query().setMemberId_Equal(34);
                    }
                });
                orCB.query().setMemberId_GreaterThan(41);
                orCB.query().setMemberId_LessThan(42);
                orCB.query().setMemberId_GreaterEqual(43);
                orCB.query().setMemberId_LessEqual(44);
                orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                    public void query(MemberCB andCB) {
                        andCB.query().setMemberId_Equal(35);
                        andCB.query().setMemberId_Equal(36);
                        andCB.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
                            public void query(PurchaseCB subCB) {
                                subCB.query().setPurchaseId_Equal(99L);
                                subCB.query().setPurchaseId_Equal(3L); // override
                                subCB.orScopeQuery(new OrQuery<PurchaseCB>() {
                                    public void query(PurchaseCB orCB) {
                                        orCB.query().setPurchaseCount_Equal(81);
                                        orCB.query().setPurchaseCount_Equal(82);
                                    }
                                });
                            }
                        });
                        andCB.query().setMemberId_Equal(37);
                        andCB.query().setMemberId_Equal(38);
                    }
                });
                orCB.query().setBirthdate_Equal(DfTypeUtil.toDate("2010/08/01"));
                orCB.query().inline().setBirthdate_Equal(DfTypeUtil.toDate("2010/07/09"));
                orCB.query().inline().setBirthdate_Equal(DfTypeUtil.toDate("2010/07/10"));
            }
        });
        cb.query().queryMemberStatus().setDisplayOrder_Equal(50);
        cb.query().queryMemberStatus().on().setDisplayOrder_Equal(99); // independent
        cb.query().queryMemberStatus().on().setDisplayOrder_Equal(60); // override
        cb.columnQuery(new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
                cb.specify().columnMemberId();
            }
        }).lessThan(new SpecifyQuery<MemberCB>() {
            public void specify(MemberCB cb) {
                cb.specify().specifyMemberStatus().columnDisplayOrder();
            }
        }).plus(3);
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberAccount_Equal("O");
                orCB.query().setMemberAccount_Equal("P");
                orCB.query().setMemberAccount_Equal("P"); // duplicate (by or-scope's specification)
                orCB.query().setFormalizedDatetime_IsNotNull();
                orCB.query().setFormalizedDatetime_IsNotNull(); // duplicate (by or-scope's specification)
                orCB.query().setMemberId_Equal(88);
            }
        });

        // ## Act ##
        String sql = cb.toDisplaySql();

        // ## Assert ##
        log(ln() + sql);
        assertTrue(sql.contains(" where dfloc.MEMBER_ID = 1"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID <> 2"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID > 3"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID < 4"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID >= 5"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID <= 6"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID is not null"));
        assertTrue(Srl.count(sql, "   and dfloc.MEMBER_ID is not null") == 1);
        assertTrue(Srl.count(sql, "   and dfloc.MEMBER_ID is null") == 1);
        assertTrue(sql.contains("   and dfloc.MEMBER_ID is null"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID in (1, 2, 3)"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID in (4, 5, 6)"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID not in (7, 8, 9)"));
        assertTrue(sql.contains("   and dfloc.MEMBER_NAME = 'Foo'"));
        assertTrue(sql.contains("   and dfloc.MEMBER_NAME like 'A%' escape '|'"));
        assertTrue(sql.contains("   and dfloc.MEMBER_NAME like '%B|%||B' escape '|'"));
        assertTrue(sql.contains("   and dfloc.MEMBER_NAME like '%C%' escape '|'"));
        assertTrue(sql.contains("   and (dfloc.MEMBER_ID = 11"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID = 12"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID = 13"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID <> 21"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID <> 22"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID <> 23"));
        assertTrue(sql.contains("     or dfloc.MEMBER_NAME like 'X%' escape '|'"));
        assertTrue(sql.contains("     or dfloc.MEMBER_NAME like '%Y' escape '|'"));
        assertTrue(sql.contains("     or dfloc.MEMBER_NAME like '%Z|%||Z%' escape '|'"));
        assertTrue(sql.contains("     or (dfloc.MEMBER_ID = 31 and dfloc.MEMBER_ID = 32)"));
        assertTrue(sql.contains("     or (dfloc.MEMBER_ID = 33 and dfloc.MEMBER_ID = 34)"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID > 41"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID < 42"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID >= 43"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ID <= 44"));
        assertTrue(sql.contains("     and exists (select sub1loc.MEMBER_ID"));
        assertTrue(sql.contains("     where sub1loc.MEMBER_ID = dfloc.MEMBER_ID"));
        assertTrue(sql.contains("     and sub1loc.PURCHASE_ID = 3"));
        assertTrue(sql.contains("     and (sub1loc.PURCHASE_COUNT = 81"));
        assertTrue(sql.contains("       or sub1loc.PURCHASE_COUNT = 82"));
        assertTrue(sql.contains("     or dfloc.BIRTHDATE = '2010-08-01'"));
        assertTrue(sql.contains("   and dfrel_0.DISPLAY_ORDER = 50"));
        assertTrue(sql.contains("   and dfloc.MEMBER_ID < dfrel_0.DISPLAY_ORDER + 3"));
        assertTrue(sql.contains("   and (dfloc.MEMBER_ACCOUNT = 'O'"));
        assertTrue(sql.contains("     or dfloc.MEMBER_ACCOUNT = 'P'"));
        assertTrue(Srl.count(sql, "     or dfloc.MEMBER_ACCOUNT = 'P'") == 2);
        assertTrue(sql.contains("     or dfloc.FORMALIZED_DATETIME is not null"));
        assertTrue(Srl.count(sql, "     or dfloc.FORMALIZED_DATETIME is not null") == 2);
        assertTrue(sql.contains("     or dfloc.MEMBER_ID = 88"));
        assertTrue(sql.contains(" where dfinlineloc.BIRTHDATE = '2010-07-08'"));
        assertTrue(sql
                .contains("   and (dfinlineloc.BIRTHDATE = '2010-07-09' or dfinlineloc.BIRTHDATE = '2010-07-10')"));
        assertTrue(sql.contains("   and dfinlineloc.MEMBER_ID <> 72"));
        assertTrue(sql.contains("   and dfinlineloc.MEMBER_ID >= 75"));
        assertTrue(sql.contains("   and dfrel_0.DISPLAY_ORDER = 60"));
        assertFalse(sql.contains("99"));
    }

    public void test_query_keptLocation() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberName_PrefixSearch("A");
        cb.query().inline().setMemberName_PrefixSearch("B");
        cb.query().setMemberName_PrefixSearch("C");

        // ## Act ##
        String actual = cb.toDisplaySql();

        // ## Assert ##
        log(ln() + actual);
        assertTrue(actual.contains(" where dfinlineloc.MEMBER_NAME like 'B%'"));
        assertTrue(actual.contains(" where dfloc.MEMBER_NAME like 'A%'"));
        assertTrue(actual.contains("   and dfloc.MEMBER_NAME like 'C%'"));
    }

    public void test_query_nullOrEmpty() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        cb.query().setMemberId_Equal(null);
        cb.query().setMemberId_GreaterEqual(null);
        cb.query().setMemberId_GreaterThan(null);
        cb.query().setMemberId_LessThan(null);
        cb.query().setMemberId_LessEqual(null);
        cb.query().setMemberId_InScope(null);
        cb.query().setMemberId_InScope(new ArrayList<Integer>());
        cb.query().setMemberId_InScope(new HashSet<Integer>());
        cb.query().setMemberId_InScope(Arrays.asList(new Integer[] { null }));
        cb.query().setMemberName_Equal(null);
        cb.query().setMemberName_Equal("");
        cb.query().setMemberName_NotEqual(null);
        cb.query().setMemberName_NotEqual("");
        cb.query().setMemberName_InScope(null);
        cb.query().setMemberName_InScope(new ArrayList<String>());
        cb.query().setMemberName_InScope(new HashSet<String>());
        cb.query().setMemberName_InScope(Arrays.asList(new String[] { "", null, "" }));
        cb.query().setMemberName_LikeSearch(null, new LikeSearchOption());
        cb.query().setMemberName_LikeSearch("", new LikeSearchOption());
        cb.query().setBirthdate_Equal(null);
        cb.query().setBirthdate_GreaterEqual(null);
        cb.query().setBirthdate_GreaterThan(null);
        cb.query().setBirthdate_LessThan(null);
        cb.query().setBirthdate_LessEqual(null);
        cb.query().setBirthdate_FromTo(null, null, new FromToOption());

        // ## Assert ##
        String actual = cb.toDisplaySql();
        log(ln() + actual);
        assertFalse(actual.contains("where"));
        assertFalse(actual, cb.hasWhereClauseOnBaseQuery());
    }

    // ===================================================================================
    //                                                                                Date
    //                                                                                ====
    public void test_query_Date_keepPlain_UtilDate() {
        // ## Arrange ##
        String expected = "2010/07/08 12:34:56.123";
        MemberCB cb = new MemberCB();
        Date current = DfTypeUtil.toDate(expected);

        // ## Act ##
        cb.query().setBirthdate_Equal(current);

        // ## Assert ##
        String actual = DfTypeUtil.toString(current, "yyyy/MM/dd HH:mm:ss.SSS");
        log(actual);
        assertEquals(expected, actual);
    }

    public void test_query_Date_keepPlain_Timestamp() {
        // ## Arrange ##
        String expected = "2010/07/08 12:34:56.123";
        MemberCB cb = new MemberCB();
        Date current = DfTypeUtil.toTimestamp(expected);

        // ## Act ##
        cb.query().setBirthdate_Equal(current);
        memberBhv.selectList(cb);

        // ## Assert ##
        String actual = DfTypeUtil.toString(current, "yyyy/MM/dd HH:mm:ss.SSS");
        log(actual);
        assertEquals(expected, actual);
    }

    public void test_query_Date_DateFromTo_keepPlain() {
        // ## Arrange ##
        String expected = "2010/07/08 12:34:56.123";
        MemberCB cb = new MemberCB();
        Date fromDate = DfTypeUtil.toDate(expected);
        Date toDate = DfTypeUtil.toDate(expected);

        // ## Act ##
        cb.query().setBirthdate_DateFromTo(fromDate, toDate);
        memberBhv.selectList(cb);

        // ## Assert ##
        String fromActual = DfTypeUtil.toString(fromDate, "yyyy/MM/dd HH:mm:ss.SSS");
        log(fromActual);
        assertEquals(expected, fromActual);
        String toActual = DfTypeUtil.toString(toDate, "yyyy/MM/dd HH:mm:ss.SSS");
        log(toActual);
        assertEquals(expected, toActual);
    }
}
