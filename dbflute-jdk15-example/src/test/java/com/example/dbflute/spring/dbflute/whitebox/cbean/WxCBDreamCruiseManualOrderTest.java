package com.example.dbflute.spring.dbflute.whitebox.cbean;

import java.util.HashMap;
import java.util.Map;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.cbean.ManualOrderBean;
import org.seasar.dbflute.cbean.UnionQuery;
import org.seasar.dbflute.util.Srl;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.cbean.MemberServiceCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.MemberServiceBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.dbflute.exentity.MemberService;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.9.4C (2012/04/26 Wednesday)
 */
public class WxCBDreamCruiseManualOrderTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;
    private MemberServiceBhv memberServiceBhv;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_DreamCruise_ManualOrder_basic() throws Exception {
        // ## Arrange ##
        ListResultBean<MemberService> serviceList = memberServiceBhv.selectList(new MemberServiceCB());
        Map<Integer, MemberService> serviceMap = new HashMap<Integer, MemberService>();
        for (MemberService service : serviceList) {
            serviceMap.put(service.getMemberId(), service);
        }
        MemberCB cb = new MemberCB();
        MemberCB dreamCruiseCB = cb.dreamCruiseCB();
        ManualOrderBean mob = new ManualOrderBean();
        mob.multiply(dreamCruiseCB.specify().specifyMemberServiceAsOne().columnServicePointCount());
        cb.query().addOrderBy_MemberId_Asc().withManualOrder(mob);

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertListNotEmpty(memberList);
        Integer previousSortValue = null;
        for (Member member : memberList) {
            Integer memberId = member.getMemberId();
            Integer servicePointCount = serviceMap.get(memberId).getServicePointCount();
            Integer sortValue = memberId * servicePointCount;
            if (previousSortValue != null && previousSortValue > sortValue) {
                fail();
            }
            previousSortValue = sortValue;
            log(member.getMemberId() + ", " + servicePointCount + ", " + sortValue);
        }
    }

    // ===================================================================================
    //                                                                               Union
    //                                                                               =====
    public void test_DreamCruise_ManualOrder_union() throws Exception {
        // ## Arrange ##
        ListResultBean<MemberService> serviceList = memberServiceBhv.selectList(new MemberServiceCB());
        Map<Integer, MemberService> serviceMap = new HashMap<Integer, MemberService>();
        for (MemberService service : serviceList) {
            serviceMap.put(service.getMemberId(), service);
        }
        MemberCB cb = new MemberCB();
        //cb.setupSelect_MemberServiceAsOne(); // auto-resolved
        cb.union(new UnionQuery<MemberCB>() {
            public void query(MemberCB unionCB) {
            }
        });
        MemberCB dreamCruiseCB = cb.dreamCruiseCB();
        ManualOrderBean mob = new ManualOrderBean();
        mob.multiply(dreamCruiseCB.specify().specifyMemberServiceAsOne().columnServicePointCount());
        cb.query().addOrderBy_MemberId_Asc().withManualOrder(mob);

        // ## Act ##
        // H2 does not support ManualOrder on Union
        //ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        String sql = cb.toDisplaySql();
        log(ln() + sql);
        assertTrue(sql.contains("order by MEMBER_ID * SERVICE_POINT_COUNT_4"));
        assertEquals(2, Srl.count(sql, "left outer join"));
    }
}
