package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.seasar.dbflute.bhv.ConditionBeanSetupper;
import org.seasar.dbflute.cbean.AndQuery;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.OrQuery;
import org.seasar.dbflute.cbean.SpecifyQuery;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.cbean.coption.FromToOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.cbean.coption.RangeOfOption;
import org.seasar.dbflute.exception.OrScopeQueryAndPartAlreadySetupException;
import org.seasar.dbflute.exception.OrScopeQueryAndPartUnsupportedOperationException;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.dbflute.exentity.Purchase;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBOrScopeQueryTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_orScopeQuery_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberName_PrefixSearch("S");
                orCB.query().setMemberName_PrefixSearch("J");
                orCB.query().setMemberId_Equal(3);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            Integer memberId = member.getMemberId();
            String memberName = member.getMemberName();
            if (!memberId.equals(3) && !memberName.startsWith("S") && !memberName.startsWith("J")) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_andOr() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberStatusCode_Equal_Formalized();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberName_PrefixSearch("S");
                orCB.query().setMemberName_PrefixSearch("J");
                orCB.query().setMemberName_PrefixSearch("M");
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            String memberName = member.getMemberName();
            assertTrue(member.isMemberStatusCodeFormalized());
            if (!memberName.startsWith("S") && !memberName.startsWith("J") && !memberName.startsWith("M")) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_nothing() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        int countAll = memberBhv.selectCount(cb);
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberName_PrefixSearch(null);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        for (Member member : memberList) {
            log(member);
        }
        assertEquals(countAll, memberList.size());
    }

    public void test_orScopeQuery_onlyOne() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberName_PrefixSearch(null);
                orCB.query().setMemberId_Equal(3);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            Integer memberId = member.getMemberId();
            if (!memberId.equals(3)) {
                fail();
            }
        }
    }

    // ===================================================================================
    //                                                                          LikeSearch
    //                                                                          ==========
    public void test_orScopeQuery_with_splitBy_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                LikeSearchOption option = new LikeSearchOption().likeContain().splitBySpace();
                orCB.query().setMemberName_LikeSearch("S t", option);
                orCB.query().setMemberName_PrefixSearch("J");
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            String memberName = member.getMemberName();
            if (!((memberName.contains("S") && memberName.contains("t")) || (memberName.startsWith("J")))) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_with_splitBy_orOrSplit_with_prefixSearch() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                LikeSearchOption option = new LikeSearchOption().likePrefix().splitBySpace().asOrSplit();
                orCB.query().setMemberName_LikeSearch("S M", option);
                orCB.query().setMemberName_PrefixSearch("J");
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            String memberName = member.getMemberName();
            if (!memberName.startsWith("S") && !memberName.startsWith("J") && !memberName.startsWith("M")) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_with_splitBy_orOrSplit_and_others() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setFormalizedDatetime_IsNull();
                LikeSearchOption option = new LikeSearchOption().likePrefix().splitBySpace().asOrSplit();
                orCB.query().setMemberName_LikeSearch("M J", option);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            String memberName = member.getMemberName();
            Timestamp formalizedDatetime = member.getFormalizedDatetime();
            if (formalizedDatetime != null && !memberName.startsWith("J") && !memberName.startsWith("M")) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_with_exists() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberStatusCode_Equal_Provisional();
                orCB.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.query().setPurchaseCount_GreaterEqual(2);
                    }
                });
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        memberBhv.loadPurchaseList(memberList, new ConditionBeanSetupper<PurchaseCB>() {
            public void setup(PurchaseCB cb) {
                cb.query().addOrderBy_ProductId_Asc();
            }
        });
        boolean existsProvisional = false;
        for (Member member : memberList) {
            log(member);
            if (member.isMemberStatusCodeProvisional()) {
                existsProvisional = true;
                continue;
            } else {
                boolean existsPurchaseCount = false;
                List<Purchase> purchaseList = member.getPurchaseList();
                for (Purchase purchase : purchaseList) {
                    if (purchase.getPurchaseCount() >= 2) {
                        existsPurchaseCount = true;
                    }
                }
                if (existsPurchaseCount) {
                    continue;
                }
            }
            fail("illegal member: " + member.toStringWithRelation());
        }
        assertTrue(existsProvisional);
    }

    public void test_orScopeQuery_inline_andOr() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().inline().setMemberStatusCode_Equal_Formalized();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().inline().setMemberName_PrefixSearch("S");
                orCB.query().inline().setMemberName_PrefixSearch("J");
                orCB.query().inline().setMemberName_PrefixSearch("M");
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
            String memberName = member.getMemberName();
            assertTrue(member.isMemberStatusCodeFormalized());
            if (!memberName.startsWith("S") && !memberName.startsWith("J") && !memberName.startsWith("M")) {
                fail();
            }
        }
    }

    public void test_orScopeQuery_onClause_andOr() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().queryMemberStatus().on().setMemberStatusCode_Equal_Formalized();
                orCB.query().queryMemberStatus().on().setDisplayOrder_Equal(3);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean existsFormalized = false;
        boolean existsDisplayOrder = false;
        for (Member member : memberList) {
            log(member);
            if (member.isMemberStatusCodeFormalized()) {
                assertNotNull(member.getMemberStatus());
                existsFormalized = true;
            } else {
                MemberStatus memberStatus = member.getMemberStatus();
                if (memberStatus != null) {
                    assertTrue(memberStatus.getDisplayOrder() == 3);
                    existsDisplayOrder = true;
                }
            }
        }
        assertTrue(existsFormalized);
        assertTrue(existsDisplayOrder);
    }

    public void test_orScopeQuery_columnQuery_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.columnQuery(new SpecifyQuery<MemberCB>() {
                    public void specify(MemberCB cb) {
                        cb.specify().columnMemberId();
                    }
                }).lessEqual(new SpecifyQuery<MemberCB>() {
                    public void specify(MemberCB cb) {
                        cb.specify().specifyMemberStatus().columnDisplayOrder();
                    }
                });
                orCB.query().setMemberId_Equal(18);
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            Integer memberId = member.getMemberId();
            Integer displayOrder = member.getMemberStatus().getDisplayOrder();
            if (memberId > displayOrder && memberId != 18) {
                fail();
            }
        }
    }

    // ===================================================================================
    //                                                                              FromTo
    //                                                                              ======
    public void test_orScopeQuery_with_fromTo_basic() {
        // ## Arrange ##
        PurchaseCB cb = new PurchaseCB();
        cb.query().setPurchaseDatetime_DateFromTo(toDate("2012/03/13"), toDate("2012/03/14"));
        cb.orScopeQuery(new OrQuery<PurchaseCB>() {
            public void query(PurchaseCB orCB) {
                orCB.query().setRegisterDatetime_DateFromTo(toDate("2012/03/15"), toDate("2012/03/16"));
                orCB.orScopeQueryAndPart(new AndQuery<PurchaseCB>() {
                    public void query(PurchaseCB andCB) {
                        andCB.query().queryMember().setBirthdate_DateFromTo(toDate("2012/03/17"), toDate("2012/03/18"));
                        FromToOption optionOrIsNull = new FromToOption().orIsNull();
                        andCB.query()
                                .queryMember()
                                .setFormalizedDatetime_FromTo(toDate("2012/03/19"), toDate("2012/03/20"),
                                        optionOrIsNull);
                    }
                });
                orCB.orScopeQueryAndPart(new AndQuery<PurchaseCB>() {
                    public void query(PurchaseCB andCB) {
                        andCB.query().setUpdateDatetime_DateFromTo(toDate("2012/03/21"), toDate("2012/03/22"));
                    }
                });
            }
        });

        // ## Act ##
        String sql = cb.toDisplaySql();

        // ## Assert ##
        log(sql);
        assertTrue(sql.contains("where dfloc.PURCHASE_DATETIME >= '2012-03-13 00:00:00.000'"));
        assertTrue(sql.contains(" and dfloc.PURCHASE_DATETIME < '2012-03-15 00:00:00.000'"));
        assertTrue(sql.contains(" and ((dfloc.REGISTER_DATETIME >= '2012-03-15 00:00:00.000' and "));
        assertTrue(sql.contains(" and dfloc.REGISTER_DATETIME < '2012-03-17 00:00:00.000')"));
        assertTrue(sql.contains("   or (dfrel_0.BIRTHDATE >= '2012-03-17' and dfrel_0.BIRTHDATE < '2012-03-19' and "));
        assertTrue(sql.contains(" and (dfrel_0.FORMALIZED_DATETIME >= '2012-03-19 00:00:00.000' or "));
        assertTrue(sql.contains(" or dfrel_0.FORMALIZED_DATETIME is null) and "));
        assertTrue(sql.contains(" and (dfrel_0.FORMALIZED_DATETIME <= '2012-03-20 00:00:00.000' or "));
        assertTrue(sql.contains(" or dfrel_0.FORMALIZED_DATETIME is null))"));
        assertTrue(Srl.contains(sql, " or (dfloc.UPDATE_DATETIME >= '2012-03-21 00:00:00.000' and "));
        assertTrue(Srl.contains(sql, " and dfloc.UPDATE_DATETIME < '2012-03-23 00:00:00.000')"));
    }

    // ===================================================================================
    //                                                                             RangeOf
    //                                                                             =======
    public void test_orScopeQuery_with_rangeOf_basic() {
        // ## Arrange ##
        PurchaseCB cb = new PurchaseCB();
        cb.query().setPurchaseId_RangeOf(13L, 14L, new RangeOfOption());
        cb.orScopeQuery(new OrQuery<PurchaseCB>() {
            public void query(PurchaseCB orCB) {
                orCB.query().setPurchaseCount_RangeOf(15, 16, new RangeOfOption());
                orCB.orScopeQueryAndPart(new AndQuery<PurchaseCB>() {
                    public void query(PurchaseCB andCB) {
                        andCB.query().queryMember().setMemberId_RangeOf(17, 18, new RangeOfOption());
                        andCB.query().queryProduct().setProductId_RangeOf(19, 20, new RangeOfOption().orIsNull());
                    }
                });
                orCB.orScopeQueryAndPart(new AndQuery<PurchaseCB>() {
                    public void query(PurchaseCB andCB) {
                        andCB.query().setVersionNo_RangeOf(21L, 22L, new RangeOfOption());
                    }
                });
            }
        });

        // ## Act ##
        String sql = cb.toDisplaySql();

        // ## Assert ##
        log(sql);
        assertTrue(sql.contains("where dfloc.PURCHASE_ID >= 13"));
        assertTrue(sql.contains(" and dfloc.PURCHASE_ID <= 14"));
        assertTrue(sql.contains(" and ((dfloc.PURCHASE_COUNT >= 15 and dfloc.PURCHASE_COUNT <= 16)"));
        assertTrue(sql.contains("   or (dfrel_0.MEMBER_ID >= 17 and dfrel_0.MEMBER_ID <= 18 and "));
        assertTrue(sql.contains("and (dfrel_1.PRODUCT_ID >= 19 or dfrel_1.PRODUCT_ID is null) and "));
        assertTrue(sql.contains("and (dfrel_1.PRODUCT_ID <= 20 or dfrel_1.PRODUCT_ID is null))"));
        assertTrue(Srl.contains(sql, "or (dfloc.VERSION_NO >= 21 and dfloc.VERSION_NO <= 22)"));
    }

    // ===================================================================================
    //                                                                             AndPart
    //                                                                             =======
    public void test_orScopeQuery_andPart_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().setMemberId_Equal(3);
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberStatusCode_Equal_Formalized();
                orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                    public void query(MemberCB andCB) {
                        andCB.query().setMemberName_LikeSearch("S M",
                                new LikeSearchOption().likeContain().splitBySpace());
                        andCB.query().setBirthdate_IsNotNull();
                        andCB.query().setFormalizedDatetime_IsNotNull();
                    }
                });
                orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                    public void query(MemberCB andCB) {
                        andCB.query().setMemberId_Equal(4);
                        andCB.query().setBirthdate_IsNotNull();
                        andCB.query().setFormalizedDatetime_IsNotNull();
                    }
                });
                orCB.query().setRegisterUser_PrefixSearch("T");
                orCB.query().setBirthdate_IsNotNull(); // ignored
            }
        });

        // ## Act ##
        String sql = cb.toDisplaySql();

        // ## Assert ##
        log(ln() + sql);
        assertTrue(sql.contains("dfloc.MEMBER_ID = 4"));
        assertTrue(sql.contains("and (dfloc.MEMBER_STATUS_CODE = 'FML'"));
        assertTrue(sql.contains("(dfloc.MEMBER_NAME like '%S%'"));
        assertTrue(sql.contains(" and dfloc.MEMBER_NAME like '%M%'"));
        assertTrue(sql.contains(" and dfloc.BIRTHDATE is not null"));
        assertTrue(sql.contains(" and dfloc.FORMALIZED_DATETIME is not null)"));
        assertTrue(sql.contains(" or dfloc.REGISTER_USER like "));
        assertTrue(sql.contains(" or dfloc.BIRTHDATE is not null"));
    }

    public void test_orScopeQuery_andPart_asOrSplit() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();

        try {
            // ## Act ##
            cb.orScopeQuery(new OrQuery<MemberCB>() {
                public void query(MemberCB orCB) {
                    orCB.query().setMemberStatusCode_Equal_Formalized();
                    orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                        public void query(MemberCB andCB) {
                            andCB.query().setMemberName_LikeSearch("S M",
                                    new LikeSearchOption().likeContain().splitBySpace().asOrSplit());
                            andCB.query().setBirthdate_IsNotNull();
                        }
                    });
                    orCB.query().setRegisterUser_PrefixSearch("T");
                }
            });

            // ## Assert ##
            fail();
        } catch (OrScopeQueryAndPartUnsupportedOperationException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_orScopeQuery_andPart_orScopeQuery() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();

        try {
            // ## Act ##
            cb.orScopeQuery(new OrQuery<MemberCB>() {
                public void query(MemberCB orCB) {
                    orCB.query().setMemberStatusCode_Equal_Formalized();
                    orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                        public void query(MemberCB andCB) {
                            andCB.query().setBirthdate_IsNotNull();
                            andCB.orScopeQuery(new OrQuery<MemberCB>() {
                                public void query(MemberCB orCB) {
                                    orCB.query().setFormalizedDatetime_IsNotNull();
                                    orCB.query().setVersionNo_Equal(123L);
                                }
                            });
                        }
                    });
                    orCB.query().setRegisterUser_PrefixSearch("T");
                }
            });

            // ## Assert ##
            log(ln() + cb.toDisplaySql());
            fail();
        } catch (OrScopeQueryAndPartUnsupportedOperationException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_orScopeQuery_andPart_andPart_invalid() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();

        try {
            // ## Act ##
            cb.orScopeQuery(new OrQuery<MemberCB>() {
                public void query(MemberCB orCB) {
                    orCB.query().setMemberStatusCode_Equal_Formalized();
                    orCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                        public void query(MemberCB andCB) {
                            andCB.query().setBirthdate_IsNotNull();
                            andCB.orScopeQueryAndPart(new AndQuery<MemberCB>() {
                                public void query(MemberCB orCB) {
                                    orCB.query().setFormalizedDatetime_IsNotNull();
                                    orCB.query().setVersionNo_Equal(123L);
                                }
                            });
                        }
                    });
                    orCB.query().setRegisterUser_PrefixSearch("T");
                }
            });

            // ## Assert ##
            fail();
        } catch (OrScopeQueryAndPartAlreadySetupException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                             Various
    //                                                                             =======
    public void test_orScopeQuery_various() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.setupSelect_MemberStatus();
        cb.query().setBirthdate_GreaterThan(currentDate());
        cb.query().inline().setUpdateUser_NotEqual("UPUSER");
        cb.query().inline().setMemberName_PrefixSearch("IN");
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().queryMemberStatus().on().setMemberStatusCode_Equal_Formalized();
                orCB.query().queryMemberStatus().on().setDisplayOrder_Equal(3);
                orCB.query().queryMemberSecurityAsOne().inline().setMemberId_IsNotNull();
                orCB.query().setBirthdate_IsNotNull();
                orCB.query().setFormalizedDatetime_IsNull();
                orCB.query().setMemberName_LikeSearch("OR SPLIT",
                        new LikeSearchOption().likePrefix().splitBySpace().asOrSplit());
                orCB.orScopeQuery(new OrQuery<MemberCB>() {
                    public void query(MemberCB orCB) {
                        orCB.query().setMemberName_PrefixSearch(null);
                        orCB.query().setMemberName_PrefixSearch("S");
                        orCB.query().setMemberName_PrefixSearch("J");
                        orCB.query().setMemberName_LikeSearch("AND SPLIT",
                                new LikeSearchOption().likePrefix().splitBySpace());
                        orCB.query().setMemberId_Equal(3);
                    }
                });
                orCB.orScopeQuery(new OrQuery<MemberCB>() {
                    public void query(MemberCB orCB) {
                        orCB.query().setMemberName_PrefixSearch(null);
                        orCB.query().setMemberName_PrefixSearch("KI");
                        orCB.query().setMemberName_PrefixSearch("OP");
                    }
                });
                orCB.orScopeQuery(new OrQuery<MemberCB>() {
                    public void query(MemberCB orCB) {
                        orCB.query().setMemberName_LikeSearch("AND2 SPLIT2",
                                new LikeSearchOption().likePrefix().splitBySpace());
                    }
                });
                orCB.query().inline().setRegisterUser_NotEqual("RGUSER");
                orCB.query().existsPurchaseList(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.query().setPaymentCompleteFlg_Equal_True();
                        subCB.query().setPurchaseCount_LessEqual(12);
                        subCB.orScopeQuery(new OrQuery<PurchaseCB>() {
                            public void query(PurchaseCB orCB) {
                                orCB.query().setPurchasePrice_Equal(12345);
                                orCB.query().setPurchaseId_Equal(987L);
                            }
                        });
                        subCB.query().setRegisterUser_Equal("PRO");
                    }
                });
                orCB.query().inline().setUpdateUser_NotEqual("UPPROC");
                orCB.union(new UnionQuery<MemberCB>() {
                    public void query(MemberCB unionCB) {
                        unionCB.query().setBirthdate_GreaterEqual(currentDate());
                        unionCB.query().setBirthdate_LessEqual(currentDate());
                    }
                }); // basically unsupported
            }
        });
        cb.orScopeQuery(new OrQuery<MemberCB>() {
            public void query(MemberCB orCB) {
                orCB.query().setMemberId_InScope(Arrays.asList(1, 2));
                orCB.query().setMemberAccount_NotEqual("LAST");
                orCB.columnQuery(new SpecifyQuery<MemberCB>() {

                    public void specify(MemberCB cb) {
                        cb.specify().columnMemberName();
                    }
                }).equal(new SpecifyQuery<MemberCB>() {
                    public void specify(MemberCB cb) {
                        cb.specify().columnMemberAccount();
                    }
                });
            }
        });

        // ## Act & Assert ##
        memberBhv.selectList(cb); // expect no exception
        String displaySql = cb.toDisplaySql();
        assertTrue(displaySql.contains("'OR%'"));
        assertTrue(displaySql.contains("'AND%'"));
        assertTrue(displaySql.contains("'SPLIT%'"));
        assertTrue(displaySql.contains("'PRO'"));
        assertTrue(displaySql.contains("'UPPROC'"));
    }
}
