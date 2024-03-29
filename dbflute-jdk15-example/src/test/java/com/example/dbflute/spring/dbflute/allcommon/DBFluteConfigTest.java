package com.example.dbflute.spring.dbflute.allcommon;

import org.seasar.dbflute.cbean.ListResultBean;
import org.seasar.dbflute.jdbc.StatementConfig;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.dbflute.exentity.Member;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.6.2 (2008/01/26 Saturday)
 */
public class DBFluteConfigTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected MemberBhv memberBhv;

    // ===================================================================================
    //                                                                              Set up
    //                                                                              ======
    @Override
    public void setUp() throws Exception {
        DBFluteConfig.getInstance().unlock();
        final StatementConfig config = new StatementConfig();
        config.typeForwardOnly().queryTimeout(10).fetchSize(7).maxRows(3);
        DBFluteConfig.getInstance().setDefaultStatementConfig(config);

        // normally you don't need to lock
        // because the initializer of DBFlute locks when initializing
        // but a container instance is recycled in this project's test cases
        // so it locks here (for a test case special reason)
        DBFluteConfig.getInstance().lock();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DBFluteConfig.getInstance().unlock();
        DBFluteConfig.getInstance().setDefaultStatementConfig(null);
        DBFluteConfig.getInstance().lock();
    }

    // ===================================================================================
    //                                                                        Setting Test
    //                                                                        ============
    public void test_setting() throws Exception {
        assertNotNull(DBFluteConfig.getInstance().getDefaultStatementConfig());
    }

    // ===================================================================================
    //                                                                     StatementConfig
    //                                                                     ===============
    public void test_ConditionBean_configure_default_is_overridden() throws Exception {
        // ## Arrange ##
        final MemberCB cb = new MemberCB();
        final StatementConfig statementConfig = new StatementConfig();
        statementConfig.typeScrollSensitive().fetchSize(123).maxRows(1);
        cb.configure(statementConfig);

        // ## Act ##
        final ListResultBean<Member> memberList = memberBhv.selectList(cb);

        // ## Assert ##
        assertEquals(1, memberList.size()); // should be overridden
    }
}
