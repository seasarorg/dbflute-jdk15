package com.example.dbflute.spring.dbflute.allcommon;

import org.seasar.dbflute.BehaviorSelector;
import org.seasar.dbflute.Entity;
import org.seasar.dbflute.bhv.BehaviorReadable;
import org.seasar.dbflute.cbean.ConditionBean;
import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.dbmeta.DBMeta;

import com.example.dbflute.spring.dbflute.bsentity.dbmeta.MemberDbm;
import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * The test of behaviorSelector.
 * @author jflute
 * @since 0.5.8 (2007/11/28 Wednesday)
 */
public class BehaviorSelectorTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private BehaviorSelector behaviorSelector;
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                              byName
    //                                                                              ======
    public void test_BehaviorSelector_byName_and_getTablePropertyName() {
        // ## Arrange ##
        final String tableDbName = "MEMBER";

        // ## Act ##
        final BehaviorReadable bhv = behaviorSelector.byName(tableDbName);
        final DBMeta dbmeta = bhv.getDBMeta();
        final String tablePropertyName = dbmeta.getTablePropertyName();

        // ## Assert ##
        assertNotNull(tablePropertyName);
        log("/********************************");
        log("tablePropertyName=" + tablePropertyName);
        log("**********/");
        assertNotNull(MemberDbm.getInstance().getTablePropertyName(), tablePropertyName);
    }

    public void test_BehaviorSelector_byName_and_readCount() {
        // ## Arrange ##
        final String tableDbName = "MEMBER";

        // ## Act ##
        final BehaviorReadable bhv = behaviorSelector.byName(tableDbName);
        final ConditionBean cb = bhv.newConditionBean();
        final int count = bhv.readCount(cb);

        // ## Assert ##
        log("/********************************");
        log("count=" + count);
        log("**********/");
        assertEquals(memberBhv.selectCount(new MemberCB()), count);
    }

    public void test_BehaviorSelector_byName_and_readList() {
        // ## Arrange ##
        final String tableDbName = "MEMBER";

        // ## Act ##
        final BehaviorReadable bhv = behaviorSelector.byName(tableDbName);
        final ConditionBean cb = bhv.newConditionBean();
        cb.addOrderBy_PK_Desc();
        final ListResultBean<? extends Entity> entityList = bhv.readList(cb);

        // ## Assert ##
        assertNotNull(entityList);
        log("/********************************");
        for (Entity entity : entityList) {
            log("entity=" + entity);
        }
        log("**********/");
        final MemberCB expectedCB = new MemberCB();
        expectedCB.addOrderBy_PK_Desc();
        final ListResultBean<Member> expectedList = memberBhv.selectList(expectedCB);
        assertEquals(expectedList, entityList);
    }
}
