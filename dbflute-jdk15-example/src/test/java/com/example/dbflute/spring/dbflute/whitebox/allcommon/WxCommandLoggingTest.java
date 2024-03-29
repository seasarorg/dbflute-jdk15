package com.example.dbflute.spring.dbflute.whitebox.allcommon;

import com.example.dbflute.spring.dbflute.cbean.MemberCB;
import com.example.dbflute.spring.dbflute.exbhv.MemberBhv;
import com.example.dbflute.spring.unit.AppContainerTestCase;

/**
 * @author jflute
 * @since 0.9.5 (2009/04/08 Wednesday)
 */
public class WxCommandLoggingTest extends AppContainerTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                       LogInvocation
    //                                                                       =============
    public void test_LogInvocation_Page_to_Service_and_Facade() {
        // ## Arrange ##
        AaaPage aaaPage = new AaaPage();

        // ## Act & Assert ##
        // Confirm the log.
        aaaPage.service();
        aaaPage.facade();
    }

    public void test_LogInvocation_Action_to_Service_and_Facade() {
        // ## Arrange ##
        CccAction cccAction = new CccAction();

        // ## Act & Assert ##
        // Confirm the log.
        cccAction.service();
        cccAction.facade();
    }

    public void test_LogInvocation_Service() {
        // ## Arrange ##
        BbbService bbbService = new BbbService();

        // ## Act & Assert ##
        // Confirm the log.
        bbbService.bbb();
    }

    public void test_LogInvocation_Facade() {
        // ## Arrange ##
        DddFacade dddFacade = new DddFacade();

        // ## Act & Assert ##
        // Confirm the log.
        dddFacade.ddd();
    }

    public void test_LogInvocation_Service_to_Page_but_the_Service_is_invalid() {
        // ## Arrange ##
        BbbService bbbService = new BbbService();

        // ## Act & Assert ##
        // Confirm the log.
        bbbService.page();
    }

    public void test_LogInvocation_Facade_to_Action_but_the_Facade_is_invalid() {
        // ## Arrange ##
        DddFacade dddFacade = new DddFacade();

        // ## Act & Assert ##
        // Confirm the log.
        dddFacade.action();
    }

    // ===================================================================================
    //                                                                        Helper Class
    //                                                                        ============
    protected class AaaPage {
        public void service() {
            new BbbService().bbb();
        }

        public void facade() {
            new DddFacade().ddd();
        }

        public void aaa() {
            memberBhv.selectList(new MemberCB());
        }
    }

    protected class BbbService {
        public void bbb() {
            memberBhv.selectList(new MemberCB());
        }

        public void page() {
            new AaaPage().aaa();
        }
    }

    protected class CccAction {
        public void service() {
            new BbbService().bbb();
        }

        public void facade() {
            new DddFacade().ddd();
        }

        public void ccc() {
            memberBhv.selectList(new MemberCB());
        }
    }

    protected class DddFacade {
        public void ddd() {
            memberBhv.selectList(new MemberCB());
        }

        public void action() {
            new CccAction().ccc();
        }
    }
}
