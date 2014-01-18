package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.seasar.dbflute.bhv.ConditionBeanSetupper;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.SubQuery;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.cbean.coption.DerivedReferrerOption;
import org.seasar.dbflute.exception.OrderByIllegalPurposeException;
import org.seasar.dbflute.exception.SetupSelectIllegalPurposeException;
import org.seasar.dbflute.exception.SpecifyDerivedReferrerEntityPropertyNotFoundException;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberLoginCB;
import com.example.dbflute.spring.dbflute.cbean.MemberStatusCB;
import com.example.dbflute.spring.dbflute.cbean.PurchaseCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberStatusBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberStatus;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.0 (2008/01/16 Wednesday)
 */
public class WxCBDerivedReferrerBasicTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_sepcify_derivedReferrer_max_query() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
                subCB.query().setMobileLoginFlg_Equal_False();
            }
        }, Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean existsLoginDatetime = false;
        boolean existsNullLoginDatetime = false;
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            if (latestLoginDatetime != null) {
                existsLoginDatetime = true;
            } else {
                existsNullLoginDatetime = true;
            }
            log("memberName=" + memberName + ", latestLoginDatetime=" + latestLoginDatetime);
        }
        assertTrue(existsLoginDatetime);
        assertTrue(existsNullLoginDatetime);
    }

    public void test_sepcify_derivedReferrer_min_string() throws Exception {
        // ## Arrange ##
        MemberStatusCB cb = new MemberStatusCB();
        cb.specify().derivedMemberList().min(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.specify().columnMemberAccount();
            }
        }, MemberStatus.ALIAS_anyMemberAccount); // as max

        // ## Act ##
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb);
        memberStatusBhv.loadMemberList(statusList, new ConditionBeanSetupper<MemberCB>() {
            public void setup(MemberCB cb) {
                cb.query().addOrderBy_MemberAccount_Asc();
            }
        });

        // ## Assert ##
        assertListNotEmpty(statusList);
        boolean exists = false;
        for (MemberStatus status : statusList) {
            String statusName = status.getMemberStatusName();
            String maxMemberAccount = status.getAnyMemberAccount();
            log(statusName + ", " + maxMemberAccount);
            List<Member> memberList = status.getMemberList();
            if (!memberList.isEmpty()) { // main road
                assertNotNull(statusName);
                Member firstMember = memberList.get(0);
                assertEquals(firstMember.getMemberAccount(), maxMemberAccount);
                exists = true;
            } else {
                assertNull(statusName);
            }
        }
        assertTrue(exists);
    }

    // ===================================================================================
    //                                                                  one-to-many-to-one
    //                                                                  ==================
    public void test_sepcify_derivedReferrer_OneToManyToOne_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().specifySummaryProduct().columnLatestPurchaseDatetime();
            }
        }, Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean exists = false;
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            log("memberName=" + memberName + ", latestLoginDatetime=" + latestLoginDatetime);
            if (latestLoginDatetime != null) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

    public void test_sepcify_derivedReferrer_OneToManyToOne_with_union() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().specifySummaryProduct().columnLatestPurchaseDatetime();
                subCB.union(new UnionQuery<PurchaseCB>() {
                    public void query(PurchaseCB unionCB) {
                    }
                });
            }
        }, Member.ALIAS_latestLoginDatetime);
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean exists = false;
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            log("memberName=" + memberName + ", latestLoginDatetime=" + latestLoginDatetime);
            if (latestLoginDatetime != null) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

    // ===================================================================================
    //                                                                 many-to-one-to-many
    //                                                                 ===================
    public void test_sepcify_derivedReferrer_ManyToOneToMany_self() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().specifyMemberStatus().derivedMemberList().max(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.specify().columnMemberId();
            }
        }, Member.ALIAS_productKindCount);
        cb.query().queryMemberStatus().addOrderBy_DisplayOrder_Asc();
        cb.query().addOrderBy_MemberId_Desc();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean exists = false;
        String preStatus = null;
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            String statusCode = member.getMemberStatusCode();
            boolean border = false;
            if (preStatus == null) {
                preStatus = statusCode;
            } else {
                if (!preStatus.equalsIgnoreCase(statusCode)) {
                    border = true;
                }
                preStatus = statusCode;
            }
            Integer memberId = member.getMemberId();
            Integer groupMax = member.getProductKindCount();
            log(memberName + ", " + statusCode + ", " + memberId + ", " + groupMax);
            if (groupMax != null) {
                exists = true;
            }
            if (border) {
                assertEquals(memberId, groupMax);
            }
        }
        assertTrue(exists);
    }

    // ===================================================================================
    //                                                                 one-to-many-to-many
    //                                                                 ===================
    // implemented at DerivedReferrerNestedTest
    //public void test_sepcify_derivedReferrer_OneToManyToMany_max() {
    //...

    // ===================================================================================
    //                                                                              Option
    //                                                                              ======
    public void test_sepcify_derivedReferrer_option_coalesce() throws Exception {
        // ## Arrange ##
        int countAll;
        {
            MemberCB cb = new MemberCB();
            countAll = memberBhv.selectCount(cb);
        }
        {
            MemberCB cb = new MemberCB();
            cb.query().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
                public void query(MemberLoginCB subCB) {
                    subCB.specify().columnLoginDatetime();
                }
            }).isNull();
            memberBhv.selectEntityWithDeletedCheck(cb); // expect no exception
        }
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
            }
        }, Member.ALIAS_latestLoginDatetime, new DerivedReferrerOption().coalesce("1192-01-01"));

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        assertEquals(countAll, memberList.size());
        boolean exists = false;
        for (Member member : memberList) {
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            String loginDateView = DfTypeUtil.toString(latestLoginDatetime, "yyyy-MM-dd");
            log(member.getMemberName() + ":" + loginDateView);
            if ("1192-01-01".equals(loginDateView)) {
                exists = true;
            }
        }
        assertTrue(exists);
    }

    public void test_sepcify_derivedReferrer_option_sqlInjection() throws Exception {
        // ## Arrange ##
        int countAll;
        {
            MemberCB cb = new MemberCB();
            countAll = memberBhv.selectCount(cb);
        }
        {
            MemberCB cb = new MemberCB();
            cb.query().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
                public void query(MemberLoginCB subCB) {
                    subCB.specify().columnLoginDatetime();
                }
            }).isNull();
            memberBhv.selectEntityWithDeletedCheck(cb); // expect no exception
        }
        String coalesce = "foo')); select * from MEMBER";
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().count(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginMemberStatusCode();
            }
        }, Member.ALIAS_loginCount, new DerivedReferrerOption().coalesce(coalesce));

        // ## Act ##
        // expect no exception if the value is treated as bind-parameter
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        assertEquals(countAll, memberList.size());
        for (Member member : memberList) {
            Integer loginCount = member.getLoginCount();
            log(member.getMemberName() + ":" + loginCount);
        }
    }

    // ===================================================================================
    //                                                                            Order By
    //                                                                            ========
    public void test_sepcify_derivedReferrer_orderBy_basic() {
        // ## Arrange ##
        Date defaultLoginDate = DfTypeUtil.toDate("1000/01/01");
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
                subCB.query().setMobileLoginFlg_Equal_False();
            }
        }, Member.ALIAS_latestLoginDatetime, new DerivedReferrerOption().coalesce(defaultLoginDate));
        cb.query().addSpecifiedDerivedOrderBy_Asc(Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        {
            ListResultBean<Member> memberList = memberBhv.selectList(cb);

            // ## Assert ##
            assertFalse(memberList.isEmpty());
            Date first = memberList.get(0).getLatestLoginDatetime();
            Date last = memberList.get(memberList.size() - 1).getLatestLoginDatetime();
            assertTrue(first.before(last));
        }

        // ## Arrange ##
        cb.clearOrderBy();
        cb.query().addSpecifiedDerivedOrderBy_Desc(Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        {
            ListResultBean<Member> memberList = memberBhv.selectList(cb);

            // ## Assert ##
            assertFalse(memberList.isEmpty());
            Date first = memberList.get(0).getLatestLoginDatetime();
            Date last = memberList.get(memberList.size() - 1).getLatestLoginDatetime();
            assertTrue(last.before(first));
        }
    }

    public void test_sepcify_derivedReferrer_orderBy_foreign() {
        // ## Arrange ##
        Date defaultLoginDate = DfTypeUtil.toDate("1000/01/01");
        MemberCB cb = new MemberCB();
        cb.specify().specifyMemberStatus().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
                subCB.query().setMobileLoginFlg_Equal_False();
            }
        }, Member.ALIAS_latestLoginDatetime, new DerivedReferrerOption().coalesce(defaultLoginDate));
        cb.query().addSpecifiedDerivedOrderBy_Asc(Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        {
            ListResultBean<Member> memberList = memberBhv.selectList(cb);

            // ## Assert ##
            assertFalse(memberList.isEmpty());
            Date first = memberList.get(0).getLatestLoginDatetime();
            Date last = memberList.get(memberList.size() - 1).getLatestLoginDatetime();
            assertTrue(first.before(last));
        }

        // ## Arrange ##
        cb.clearOrderBy();
        cb.query().queryMemberStatus().addSpecifiedDerivedOrderBy_Desc(Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        {
            ListResultBean<Member> memberList = memberBhv.selectList(cb);

            // ## Assert ##
            assertFalse(memberList.isEmpty());
            Date first = memberList.get(0).getLatestLoginDatetime();
            Date last = memberList.get(memberList.size() - 1).getLatestLoginDatetime();
            assertTrue(last.before(first));
        }
    }

    // ===================================================================================
    //                                                                          with Union
    //                                                                          ==========
    public void test_derivedReferrer_union_of_subQuery() {
        // ## Arrange ##
        List<Member> expectedList = selectListAllWithLatestLoginDatetime();
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
                subCB.query().setMobileLoginFlg_Equal_True();
                subCB.union(new UnionQuery<MemberLoginCB>() {
                    public void query(MemberLoginCB unionCB) {
                        unionCB.query().setMobileLoginFlg_Equal_False();
                    }
                });
            }
        }, Member.ALIAS_latestLoginDatetime);
        cb.query().addOrderBy_MemberId_Asc();

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        int index = 0;
        for (Member member : memberList) {
            Member expectedMember = expectedList.get(index);
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            log(member.getMemberName() + ", " + latestLoginDatetime);
            assertEquals(expectedMember.getLatestLoginDatetime(), latestLoginDatetime);
            ++index;
        }
    }

    protected List<Member> selectListAllWithLatestLoginDatetime() {
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
            }
        }, "LATEST_LOGIN_DATETIME");
        cb.query().addOrderBy_MemberId_Asc();
        return memberBhv.selectList(cb);
    }

    public void test_derivedReferrer_other_union() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
            }
        }, "LATEST_LOGIN_DATETIME");
        cb.query().setMemberStatusCode_Equal_Formalized();
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.query().setMemberStatusCode_Equal_Provisional();
            }
        });

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        for (Member member : memberList) {
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            log(member.getMemberName() + ", " + latestLoginDatetime);
        }
    }

    public void test_derivedReferrer_other_union_specifiedDerivedOrderBy() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
                subCB.query().setMobileLoginFlg_Equal_True();
                subCB.union(new UnionQuery<MemberLoginCB>() {
                    public void query(MemberLoginCB unionCB) {
                        unionCB.query().setMobileLoginFlg_Equal_False();
                    }
                });
            }
        }, Member.ALIAS_latestLoginDatetime);
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
                unionCB.query().setMemberStatusCode_Equal_Withdrawal();
            }
        });
        cb.query().addSpecifiedDerivedOrderBy_Asc(Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getLatestLoginDatetime());
        }
    }

    // ===================================================================================
    //                                                                  with SpecifyColumn
    //                                                                  ==================
    public void test_sepcify_derivedReferrer_with_specifyColumn() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().columnMemberName();
        cb.specify().derivedMemberLoginList().max(new SubQuery<MemberLoginCB>() {
            public void query(MemberLoginCB subCB) {
                subCB.specify().columnLoginDatetime();
            }
        }, Member.ALIAS_latestLoginDatetime);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        boolean exists = false;
        for (Member member : memberList) {
            String memberName = member.getMemberName();
            Date latestLoginDatetime = member.getLatestLoginDatetime();
            log("memberName=" + memberName + ", latestLoginDatetime=" + latestLoginDatetime);
            if (latestLoginDatetime != null) {
                exists = true;
            }
            assertNotNull(member.getMemberId());
            assertNotNull(member.getMemberName());
            assertNull(member.getMemberAccount());
        }
        assertTrue(exists);
    }

    // ===================================================================================
    //                                                                             Illegal
    //                                                                             =======
    public void test_sepcify_derivedReferrer_illegal() {
        // ## Arrange ##
        MemberStatusCB cb = new MemberStatusCB();
        cb.specify().derivedMemberList().max(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                try {
                    // ## Act ##
                    subCB.setupSelect_MemberSecurityAsOne();

                    // ## Assert ##
                    fail();
                } catch (SetupSelectIllegalPurposeException e) {
                    // OK
                    log(e.getMessage());
                }
                try {
                    // ## Act ##
                    subCB.query().addOrderBy_MemberId_Asc();

                    // ## Assert ##
                    fail();
                } catch (OrderByIllegalPurposeException e) {
                    // OK
                    log(e.getMessage());
                }
                subCB.specify().columnBirthdate(); // OK
                subCB.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() { // OK
                            public void query(PurchaseCB subCB) {
                                subCB.specify().columnPurchasePrice();
                            }
                        }).equal(123);
            }
        }, "FOO");
    }

    public void test_sepcify_derivedReferrer_invalidAlias() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().specifySummaryProduct().columnLatestPurchaseDatetime();
            }
        }, "NOT_EXIST_COLUMN");

        // ## Act ##
        try {
            memberBhv.selectList(cb);

            // ## Assert ##
            fail();
        } catch (SpecifyDerivedReferrerEntityPropertyNotFoundException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                              (Query)DerivedReferrer
    //                                                              ======================
    public void test_query_derivedReferrer_OneToManyToOne() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().specifySummaryProduct().columnLatestPurchaseDatetime();
            }
        }).lessThan(currentTimestamp());

        // ## Act ##
        // Expect no exception
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
        }
    }

    public void test_query_derivedReferrer_OneToManyToOne_with_union() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().specifySummaryProduct().columnLatestPurchaseDatetime();
                subCB.union(new UnionQuery<PurchaseCB>() {
                    public void query(PurchaseCB unionCB) {
                    }
                });
            }
        }).lessEqual(currentTimestamp());
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
            }
        });

        // ## Act ##
        // Expect no exception
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertFalse(memberList.isEmpty());
        for (Member member : memberList) {
            log(member);
        }
    }

    public void test_query_derivedReferrer_in_ExistsReferrer() {
        // ## Arrange ##
        MemberStatusCB cb = new MemberStatusCB();
        cb.query().existsMemberList(new SubQuery<MemberCB>() {
            public void query(MemberCB subCB) {
                subCB.query().setMemberId_LessThan(300);
                subCB.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
                    public void query(PurchaseCB subCB) {
                        subCB.specify().columnPurchasePrice();
                    }
                }).greaterEqual(1234);
            }
        });
        cb.query().setDisplayOrder_LessEqual(500);

        // ## Act ##
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb);

        // ## Assert ##
        assertNotSame(0, statusList.size());
        for (MemberStatus status : statusList) {
            log(status);
        }
        String sql = cb.toDisplaySql();
        assertTrue(Srl.containsAll(sql, ") >= 1234"));
    }

    public void test_query_derivedReferrer_between_Integer() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().sum(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().columnPurchaseCount();
            }
        }, Member.ALIAS_loginCount); // rental
        Integer fromCount = 6;
        Integer toCount = 7;
        cb.query().derivedPurchaseList().sum(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().columnPurchaseCount();
            }
        }).between(fromCount, toCount);

        // ## Act ##
        // Expect no exception
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            Integer loginCount = member.getLoginCount();
            log(member.getMemberName() + ", " + loginCount);
            assertTrue(fromCount <= loginCount);
            assertTrue(toCount >= loginCount);
        }
    }

    public void test_query_derivedReferrer_between_Date() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.specify().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().columnPurchaseDatetime();
            }
        }, Member.ALIAS_latestLoginDatetime); // rental
        Date fromDate = toDate("2007/11/01");
        Date toDate = toDate("2007/11/02");
        cb.query().derivedPurchaseList().max(new SubQuery<PurchaseCB>() {
            public void query(PurchaseCB subCB) {
                subCB.specify().columnPurchaseDatetime();
            }
        }).between(fromDate, toDate);

        // ## Act ##
        // Expect no exception
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        for (Member member : memberList) {
            Timestamp latestDate = member.getLatestLoginDatetime();
            log(member.getMemberName() + ", " + toString(member.getLatestLoginDatetime()));
            assertTrue(fromDate.equals(latestDate) || fromDate.before(latestDate));
            assertTrue(toDate.equals(latestDate) || toDate.after(latestDate));
        }
    }
}
