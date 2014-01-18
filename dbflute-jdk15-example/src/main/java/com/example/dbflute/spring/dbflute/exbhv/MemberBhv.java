/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.exbhv;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.example.dbflute.spring.dbflute.bsbhv.BsMemberBhv;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursor;
import com.example.dbflute.spring.dbflute.exbhv.cursor.PurchaseSummaryMemberCursorHandler;
import com.example.dbflute.spring.dbflute.exbhv.pmbean.PurchaseSummaryMemberPmb;

/**
 * The behavior of MEMBER.
 * @author DBFlute(AutoGenerator)
 */
public class MemberBhv extends BsMemberBhv {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Log instance for sub class. */
    private static final Log _log = LogFactory.getLog(MemberBhv.class);

    // ===================================================================================
    //                                                                          CSV Output
    //                                                                          ==========
    public void makeCsvPurchaseSummaryMember(PurchaseSummaryMemberPmb pmb) {
        String path = PATH_selectPurchaseSummaryMember;
        outsideSql().cursorHandling().selectCursor(path, pmb, new PurchaseSummaryMemberCursorHandler() {
            public Object fetchCursor(PurchaseSummaryMemberCursor cursor) throws SQLException {
                while (cursor.next()) {
                    final Integer memberId = cursor.getMemberId();
                    final String memberName = cursor.getMemberName();
                    final Date birthdate = cursor.getBirthdate();
                    final Timestamp formalizedDatetime = cursor.getFormalizedDatetime();
                    final Long purchaseSummary = cursor.getPurchaseSummary();

                    // logging only here because of example
                    final String c = ", ";
                    StringBuilder sb = new StringBuilder();
                    sb.append(memberId).append(c).append(memberName).append(c);
                    sb.append(birthdate).append(c).append(formalizedDatetime).append(c);
                    sb.append(purchaseSummary);
                    _log.debug(sb.toString());
                }
                return null;
            }
        });
    }
}
