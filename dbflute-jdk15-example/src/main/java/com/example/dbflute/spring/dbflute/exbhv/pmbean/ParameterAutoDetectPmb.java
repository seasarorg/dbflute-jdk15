/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.exbhv.pmbean;

import com.example.dbflute.spring.dbflute.bsbhv.pmbean.BsParameterAutoDetectPmb;

/**
 * <!-- df:beginClassDescription -->
 * The typed parameter-bean of ParameterAutoDetect. <span style="color: #AD4747">(typed to execute)</span><br />
 * This is related to "<span style="color: #AD4747">whitebox:pmbean:selectParameterAutoDetect</span>" on MemberBhv, <br />
 * described as "The test for parameter auto-detect".
 * <!-- df:endClassDescription -->
 * <p>
 * You can implement your original methods here.
 * This class remains when re-generating.
 * </p>
 * @author DBFlute(AutoGenerator)
 */
public class ParameterAutoDetectPmb extends BsParameterAutoDetectPmb {

    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isFirstAlternate() {
        return false;
    }

    @Override
    public boolean isNotUseAlternate() {
        return false;
    }

    @Override
    public boolean isDuplicateAlternate() {
        return false;
    }

    @Override
    public boolean hasDefinedBoolean() { // getPropertyName() and isPropertyName() are filtered
        return false;
    }

    @SuppressWarnings("unused")
    private boolean isPaging() { // is filtered as a reservation method
        return false;
    }

    @SuppressWarnings("unused")
    private boolean isEscapeStatement() { // is filtered as a reservation method
        return false;
    }
}
