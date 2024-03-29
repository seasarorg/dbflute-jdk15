package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.HashSet;
import java.util.Set;

import org.seasar.dbflute.cbean.PagingBean;
import org.seasar.dbflute.cbean.PagingHandler;
import org.seasar.dbflute.cbean.PagingInvoker;
import org.seasar.dbflute.cbean.PagingResultBean;
import org.seasar.dbflute.cbean.ResultBeanBuilder;
import org.seasar.dbflute.exception.PagingPageSizeNotPlusException;
import org.seasar.dbflute.exception.PagingStatusInvalidException;
import org.seasar.dbflute.util.DfCollectionUtil;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.7.6 (2010/11/15 Monday)
 */
public class WxCBPagingTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_paging_basic() {
        // ## Arrange ##
        int countAll = memberBhv.selectCount(new MemberCB());
        MemberCB cb = new MemberCB();
        cb.query().addOrderBy_MemberName_Asc();
        cb.paging(4, 3);

        // ## Act ##
        PagingResultBean<Member> page3 = memberBhv.selectPage(cb);

        // ## Assert ##
        assertNotSame(0, page3.size());
        for (Member member : page3) {
            log(member.toString());
        }
        assertEquals(countAll, page3.getAllRecordCount());
        assertEquals((countAll / 4) + (countAll % 4 > 0 ? 1 : 0), page3.getAllPageCount());
        assertEquals(3, page3.getCurrentPageNumber());
        assertEquals(9, page3.getCurrentStartRecordNumber());
        assertEquals(12, page3.getCurrentEndRecordNumber());
        assertTrue(page3.isExistPrePage());
        assertTrue(page3.isExistNextPage());
    }

    public void test_paging_zeroResult() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberName_Equal("noexist");
        cb.query().addOrderBy_MemberName_Asc();
        cb.paging(4, 3);

        // ## Act ##
        PagingResultBean<Member> page1 = memberBhv.selectPage(cb);

        // ## Assert ##
        assertEquals(0, page1.size());
        assertEquals(0, page1.getAllRecordCount());
        assertTrue(page1.getOrderByClause().isSameAsFirstElementColumnName("MEMBER_NAME"));
        log("page: " + page1);
    }

    // ===================================================================================
    //                                                                            ReSelect
    //                                                                            ========
    public void test_paging_ReSelect() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();
        cb.query().setMemberId_Equal(3);
        cb.query().addOrderBy_MemberId_Asc();
        cb.paging(4, 3);

        // ## Act ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb); // re-select

        // ## Assert ##
        assertNotSame(0, page.size());
        assertEquals(1, page.size());
        log("PagingResultBean.toString():" + ln() + " " + page);
        assertEquals(1, page.getAllRecordCount());
        assertEquals(1, page.getAllPageCount());
        assertEquals(Integer.valueOf(3), page.get(0).getMemberId());
    }

    // ===================================================================================
    //                                                                         Count Later
    //                                                                         ===========
    public void test_paging_CountLater_basic() {
        // ## Arrange ##
        MemberCB cb = new MemberCB() {
            @Override
            public <ENTITY> PagingInvoker<ENTITY> createPagingInvoker(String tableDbName) {
                return new PagingInvoker<ENTITY>(tableDbName) {
                    @Override
                    protected int executeCount(PagingHandler<ENTITY> handler) {
                        fail();
                        return super.executeCount(handler);
                    }
                };
            }
        };
        cb.query().setMemberId_InScope(DfCollectionUtil.newArrayList(1, 2, 3, 4));
        cb.query().addOrderBy_MemberId_Asc();
        cb.enablePagingCountLater();
        cb.paging(3, 2);

        // ## Act ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb); // omit count

        // ## Assert ##
        assertNotSame(0, page.size());
        assertEquals(4, page.getAllRecordCount());
        assertEquals(2, page.getAllPageCount());
    }

    public void test_paging_CountLater_nodata_firstPage() {
        // ## Arrange ##
        MemberCB cb = new MemberCB() {
            @Override
            public <ENTITY> PagingInvoker<ENTITY> createPagingInvoker(String tableDbName) {
                return new PagingInvoker<ENTITY>(tableDbName) {
                    @Override
                    protected int executeCount(PagingHandler<ENTITY> handler) {
                        fail();
                        return super.executeCount(handler);
                    }

                    @Override
                    protected PagingResultBean<ENTITY> reselect(PagingHandler<ENTITY> handler, PagingBean pagingBean,
                            ResultBeanBuilder<ENTITY> builder, PagingResultBean<ENTITY> rb) {
                        fail();
                        return super.reselect(handler, pagingBean, builder, rb);
                    }
                };
            }
        };
        cb.query().setMemberId_Equal(99999);
        cb.query().addOrderBy_MemberId_Asc();
        cb.enablePagingCountLater();
        cb.paging(4, 1);

        // ## Act ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb); // omit count

        // ## Assert ##
        assertEquals(0, page.size());
    }

    public void test_paging_CountLater_nodata_thirdPage() {
        // ## Arrange ##
        final Set<String> markSet = new HashSet<String>();
        MemberCB cb = new MemberCB() {
            @Override
            public <ENTITY> PagingInvoker<ENTITY> createPagingInvoker(String tableDbName) {
                return new PagingInvoker<ENTITY>(tableDbName) {
                    @Override
                    protected int executeCount(PagingHandler<ENTITY> handler) {
                        markSet.add("executeCount");
                        return super.executeCount(handler);
                    }

                    @Override
                    protected PagingResultBean<ENTITY> reselect(PagingHandler<ENTITY> handler, PagingBean pagingBean,
                            ResultBeanBuilder<ENTITY> builder, PagingResultBean<ENTITY> rb) {
                        fail();
                        return super.reselect(handler, pagingBean, builder, rb);
                    }
                };
            }
        };
        cb.query().setMemberId_Equal(99999);
        cb.query().addOrderBy_MemberId_Asc();
        cb.enablePagingCountLater();
        cb.paging(4, 3);

        // ## Act ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb);

        // ## Assert ##
        assertEquals(0, page.size());
        assertTrue(markSet.contains("executeCount"));
        assertFalse(markSet.contains("reselect"));
    }

    public void test_paging_CountLater_ReSelect_basic() {
        // ## Arrange ##
        final Set<String> markSet = new HashSet<String>();
        MemberCB cb = new MemberCB() {
            @Override
            public <ENTITY> PagingInvoker<ENTITY> createPagingInvoker(String tableDbName) {
                return new PagingInvoker<ENTITY>(tableDbName) {
                    @Override
                    protected PagingResultBean<ENTITY> reselect(PagingHandler<ENTITY> handler, PagingBean pagingBean,
                            ResultBeanBuilder<ENTITY> builder, PagingResultBean<ENTITY> rb) {
                        markSet.add("reselect");
                        return super.reselect(handler, pagingBean, builder, rb);
                    }
                };
            }
        };
        cb.query().setMemberId_Equal(3);
        cb.query().addOrderBy_MemberId_Asc();
        cb.enablePagingCountLater();
        cb.paging(4, 3);

        // ## Act ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb); // re-select

        // ## Assert ##
        assertNotSame(0, page.size());
        assertEquals(1, page.size());
        log("PagingResultBean.toString():" + ln() + " " + page);
        assertEquals(1, page.getAllRecordCount());
        assertEquals(1, page.getAllPageCount());
        assertEquals(Integer.valueOf(3), page.get(0).getMemberId());
        assertTrue(markSet.contains("reselect"));
    }

    // ===================================================================================
    //                                                                     Illegal Pattern
    //                                                                     ===============
    public void test_paging_pageSize_notPlus() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            cb.paging(0, 1);

            // ## Assert ##
            fail();
        } catch (PagingPageSizeNotPlusException e) {
            // OK
            log(e.getMessage());
        }

        // ## Act ##
        try {
            cb.paging(-1, 1);

            // ## Assert ##
            fail();
        } catch (PagingPageSizeNotPlusException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_selectPage_invalidStatus() {
        // ## Arrange ##
        MemberCB cb = new MemberCB();

        // ## Act ##
        try {
            memberBhv.selectPage(cb);

            // ## Assert ##
            fail();
        } catch (PagingStatusInvalidException e) {
            // OK
            log(e.getMessage());
        }
    }
}
