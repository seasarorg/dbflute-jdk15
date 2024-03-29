package com.example.dbflute.spring.dbflute.whitebox.outsidesql;

import java.util.Date;

import org.seasar.dbflute.cbean.coption.FromToOption;
import org.seasar.dbflute.cbean.coption.LikeSearchOption;
import org.seasar.dbflute.exception.CharParameterShortSizeException;
import org.seasar.dbflute.exception.IllegalOutsideSqlOperationException;
import org.seasar.dbflute.exception.RequiredOptionNotFoundException;
import org.seasar.dbflute.jdbc.ParameterUtil.ShortCharHandlingMode;
import org.seasar.dbflute.unit.core.PlainTestCase;
import org.seasar.dbflute.util.DfTypeUtil;

import com.example.dbflute.spring.dbflute.exbhv.pmbean.CompareDatePmb;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.OptionMemberPmb;

/**
 * @author jflute
 * @since 0.9.6.1 (2009/11/17 Tuesday)
 */
public class WxParameterBeanBasicTest extends PlainTestCase {

    // ===================================================================================
    //                                                                           Empty Set
    //                                                                           =========
    public void test_emptyToNull_basic() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();

        // ## Act ##
        pmb.setMemberStatusCode("");
        pmb.setStatus("");

        // ## Assert ##
        assertNull(pmb.getMemberStatusCode());
        assertNull(pmb.getStatus());
    }

    public void test_emptyToNull_opion() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isEmptyStringParameterAllowed() {
                return true;
            }
        };

        // ## Act ##
        pmb.setMemberStatusCode("");
        pmb.setStatus("");

        // ## Assert ##
        assertEquals("", pmb.getMemberStatusCode());
        assertEquals("", pmb.getStatus());
    }

    public void test_spaceRemains() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected String handleShortChar(String propertyName, String value, Integer size) {
                return value; // for not depending on shortCharHandlngMode
            }
        };

        // ## Act ##
        pmb.setMemberStatusCode(" ");
        pmb.setStatus(" ");

        // ## Assert ##
        assertEquals(" ", pmb.getMemberStatusCode());
        assertEquals(" ", pmb.getStatus());
    }

    public void test_nullRemains() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected String handleShortChar(String propertyName, String value, Integer size) {
                return value; // for not depending on shortCharHandlngMode
            }
        };

        // ## Act ##
        pmb.setMemberStatusCode(null);
        pmb.setStatus(null);

        // ## Assert ##
        assertNull(pmb.getMemberStatusCode());
        assertNull(pmb.getStatus());
    }

    // ===================================================================================
    //                                                                         Like Search
    //                                                                         ===========
    public void test_likeSearch_basic() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();
        LikeSearchOption option = new LikeSearchOption();

        // ## Act ##
        pmb.setMemberAccount("foo", option.likeContain());

        // ## Assert ##
        LikeSearchOption actual = pmb.getMemberAccountInternalLikeSearchOption();
        assertNotNull(actual);
        assertEquals(option, actual);
    }

    public void test_likeSearch_nullOption() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();

        // ## Act ##
        try {
            pmb.setMemberAccount("foo", null);

            // ## Assert ##
            fail();
        } catch (RequiredOptionNotFoundException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_likeSearch_splitOption() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();

        // ## Act ##
        try {
            pmb.setMemberAccount("foo", new LikeSearchOption().splitByPipeLine());

            // ## Assert ##
            fail();
        } catch (IllegalOutsideSqlOperationException e) {
            // OK
            log(e.getMessage());
        }
    }

    // ===================================================================================
    //                                                                          Short Char
    //                                                                          ==========
    public void test_handleShortChar_RFILL_shortChar() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
                return ShortCharHandlingMode.RFILL;
            }

            @Override
            public String toString() {
                return handleShortChar("test", "AB", 3);
            }
        };

        // ## Act ##
        String actual = pmb.toString();

        // ## Assert ##
        assertEquals("AB ", actual);
    }

    public void test_handleShortChar_LFILL_shortChar() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
                return ShortCharHandlingMode.LFILL;
            }

            @Override
            public String toString() {
                return handleShortChar("test", "AB", 3);
            }
        };

        // ## Act ##
        String actual = pmb.toString();

        // ## Assert ##
        assertEquals(" AB", actual);
    }

    public void test_handleShortChar_EXCEPTION_shortChar() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
                return ShortCharHandlingMode.EXCEPTION;
            }

            @Override
            public String toString() {
                return handleShortChar("test", "AB", 3);
            }
        };

        // ## Act ##
        try {
            pmb.toString();

            // ## Assert ##
            fail();
        } catch (CharParameterShortSizeException e) {
            // OK
            log(e.getMessage());
        }
    }

    public void test_handleShortChar_EXCEPTION_overChar() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
                return ShortCharHandlingMode.EXCEPTION;
            }

            @Override
            public String toString() {
                return handleShortChar("test", "ABCD", 3);
            }
        };

        // ## Act ##
        String actual = pmb.toString();

        // ## Assert ##
        assertEquals("ABCD", actual);

        // *The overSize is out of scope in spite of CHAR type.
    }

    // ===================================================================================
    //                                                                         Date FromTo
    //                                                                         ===========
    public void test_DateFromTo_calls_convert() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();
        pmb.setBirthdate_FromDate(toTimestamp("2008/11/21 12:34:56.123"));

        // ## Act ##
        Date birthdate = pmb.getBirthdate();

        // ## Assert ##
        assertEquals(java.util.Date.class, birthdate.getClass());
        assertEquals("2008/11/21", toString(birthdate, "yyyy/MM/dd"));
    }

    public void test_FromToScope_calls_convert() {
        // ## Arrange ##
        OptionMemberPmb pmb = new OptionMemberPmb();
        FromToOption option = new FromToOption().compareAsMonth();
        pmb.setFromFormalizedMonth_FromDate(toTimestamp("2008/11/21 12:34:56.123"), option);
        pmb.setToFormalizedMonth_ToDate(toTimestamp("2008/11/21 12:34:56.123"), option);

        // ## Act ##
        Date fromDate = pmb.getFromFormalizedMonth();
        Date toDate = pmb.getToFormalizedMonth();

        // ## Assert ##
        assertEquals("2008/11/01", toString(fromDate, "yyyy/MM/dd"));
        assertEquals("2008/12/01", toString(toDate, "yyyy/MM/dd"));
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    public void test_toString() {
        // ## Arrange ##
        CompareDatePmb pmb = new CompareDatePmb();
        pmb.setBirthdateFrom(DfTypeUtil.toDate("6789-12-24 12:34:56"));
        pmb.setFormalizedDatetimeFrom(DfTypeUtil.toTimestamp("8347-08-30 09:42:41.235"));

        // ## Act ##
        String actual = pmb.toString();

        // ## Assert ##
        log(actual);
        assertTrue(actual.contains(", 6789-12-24"));
        assertTrue(actual.contains(", 8347-08-30 09:42:41.235"));
        assertFalse(actual.contains("00:00:00"));
        assertFalse(actual.contains("12:34:56"));
    }
}
