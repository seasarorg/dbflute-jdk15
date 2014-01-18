package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.List;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.chelper.HpSpecifiedColumn;
import org.seasar.dbflute.cbean.coption.ColumnConversionOption;
import org.seasar.dbflute.helper.HandyDate;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberAddressCB;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberAddressBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberAddress;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.9.4C (2012/04/26 Wednesday)
 */
public class WxCBDreamCruiseColumnQueryTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberAddressBhv memberAddressBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_DreamCruise_ColumnQuery_basic() throws Exception {
        // ## Arrange ##
        List<Member> expectedList = selectMyOnlyProductMember();
        MemberCB cb = new MemberCB();
        cb.specify().columnBirthdate();
        final MemberCB dreamCruiseCB = cb.dreamCruiseCB();
        cb.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().queryProduct().notExistsPurchaseList(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.columnQuery(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.specify().columnMemberId();
                            }
                        }).notEqual(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.overTheWaves(dreamCruiseCB.specify().columnMemberId());
                            }
                        });
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            log(member);
        }
        assertEquals(expectedList, memberList);
    }

    public void test_DreamCruise_ColumnQuery_calculation_basic() throws Exception {
        // ## Arrange ##
        List<Member> expectedList = selectMyOnlyProductMember();
        MemberCB cb = new MemberCB();
        cb.specify().columnBirthdate();
        final MemberCB dreamCruiseCB = cb.dreamCruiseCB();
        cb.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().queryProduct().notExistsPurchaseList(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        HpSpecifiedColumn pointColumn = dreamCruiseCB.specify().specifyMemberServiceAsOne()
                                .columnServicePointCount();
                        subCB.columnQuery(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.specify().columnMemberId();
                            }
                        }).notEqual(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.overTheWaves(dreamCruiseCB.specify().columnMemberId());
                            }
                        }).multiply(pointColumn).divide(pointColumn);
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            log(member);
        }
        assertEquals(expectedList, memberList);
        String sql = cb.toDisplaySql();
        assertTrue(Srl.containsAll(sql, "*", "/"));
    }

    protected List<Member> selectMyOnlyProductMember() throws Exception {
        MemberCB cb = new MemberCB();
        cb.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().queryProduct().derivedPurchaseList().countDistinct(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.specify().columnMemberId();
                    }
                }).equal(1);
            }
        });
        return memberBhv.selectList(cb);
    }

    public void test_DreamCruise_ColumnQuery_relation_convert() throws Exception {
        // ## Arrange ##
        List<Member> expectedList = selectMyOnlyProductMember();
        MemberCB cb = new MemberCB();
        cb.specify().columnBirthdate();
        final MemberCB dreamCruiseCB = cb.dreamCruiseCB();
        cb.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.query().queryProduct().notExistsPurchaseList(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.columnQuery(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.specify().columnMemberId();
                            }
                        }).notEqual(new SpecifyQuery<PurchaseCB>() {
                            public void specify(PurchaseCB cb) {
                                cb.overTheWaves(dreamCruiseCB.specify().specifyMemberSecurityAsOne().columnMemberId());
                            }
                        }).convert(new ColumnConversionOption().trunc(1));
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            log(member);
        }
        assertEquals(expectedList, memberList);
        String sql = cb.toDisplaySql();
        assertTrue(sql.contains("trunc"));
    }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    public void test_DreamCruise_ColumnQuery_MyselfExists_basic() throws Exception {
        // ## Arrange ##
        MemberAddress first;
        {
            MemberAddressCB cb = new MemberAddressCB();
            cb.query().queryMember().derivedMemberAddressList().count(new SubQuery<MemberAddressCB>() {
                public void query(MemberAddressCB subCB) {
                    subCB.specify().columnMemberAddressId();
                }
            }).greaterEqual(2);
            cb.query().addOrderBy_MemberId_Asc();
            cb.query().addOrderBy_ValidBeginDate_Asc();
            ListResultBean<MemberAddress> addressList = memberAddressBhv.selectList(cb);
            first = addressList.get(0);
            MemberAddress second = addressList.get(1);
            assertEquals(first.getMemberId(), second.getMemberId());
            second.setValidBeginDate(new HandyDate(first.getValidEndDate()).addDay(-1).getDate());
            String fmt = "yyyy/MM/dd";
            log("member=" + first.getMemberId());
            String firstBegin = toString(first.getValidBeginDate(), fmt);
            String firstEnd = toString(first.getValidEndDate(), fmt);
            log("first=" + first.getMemberAddressId() + ", " + firstBegin + ", " + firstEnd);
            String secondBegin = toString(second.getValidBeginDate(), fmt);
            String secondEnd = toString(second.getValidEndDate(), fmt);
            log("second=" + second.getMemberAddressId() + ", " + secondBegin + ", " + secondEnd);
            memberAddressBhv.updateNonstrict(second);
        }
        MemberAddressCB cb = new MemberAddressCB();
        final MemberAddressCB dreamCruiseCB = cb.dreamCruiseCB();
        cb.query().myselfExists(new SubQuery<MemberAddressCB>() {
            public void query(MemberAddressCB subCB) {
                subCB.specify().columnMemberId();
                subCB.columnQuery(new SpecifyQuery<MemberAddressCB>() {
                    public void specify(MemberAddressCB cb) {
                        cb.specify().columnValidBeginDate();
                    }
                }).greaterThan(new SpecifyQuery<MemberAddressCB>() {
                    public void specify(MemberAddressCB cb) {
                        cb.overTheWaves(dreamCruiseCB.specify().columnValidBeginDate());
                    }
                });
                subCB.columnQuery(new SpecifyQuery<MemberAddressCB>() {
                    public void specify(MemberAddressCB cb) {
                        cb.specify().columnValidBeginDate();
                    }
                }).lessThan(new SpecifyQuery<MemberAddressCB>() {
                    public void specify(MemberAddressCB cb) {
                        cb.overTheWaves(dreamCruiseCB.specify().columnValidEndDate());
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<MemberAddress> addressList = memberAddressBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(addressList);
        assertEquals(1, addressList.size());
        MemberAddress address = addressList.get(0);
        log(ln() + address);
        assertEquals(first, address);
    }
}
