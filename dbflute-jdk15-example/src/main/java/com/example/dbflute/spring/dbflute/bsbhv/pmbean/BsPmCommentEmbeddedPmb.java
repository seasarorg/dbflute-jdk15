/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsbhv.pmbean;

import java.util.*;

import org.seasar.dbflute.outsidesql.typed.*;
import org.seasar.dbflute.jdbc.*;
import org.seasar.dbflute.jdbc.ParameterUtil.ShortCharHandlingMode;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import com.example.dbflute.spring.dbflute.allcommon.*;
import com.example.dbflute.spring.dbflute.exbhv.*;
import com.example.dbflute.spring.dbflute.exentity.customize.*;

/**
 * The base class for typed parameter-bean of PmCommentEmbedded. <br />
 * This is related to "<span style="color: #AD4747">whitebox:pmcomment:selectPmCommentEmbedded</span>" on MemberBhv.
 * @author DBFlute(AutoGenerator)
 */
public class BsPmCommentEmbeddedPmb implements ListHandlingPmb<MemberBhv, PmCommentEmbedded>, EntityHandlingPmb<MemberBhv, PmCommentEmbedded>, FetchBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** The parameter of schema. */
    protected String _schema;

    /** The parameter of schemaDot. */
    protected String _schemaDot;

    /** The max size of safety result. */
    protected int _safetyMaxResultSize;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor for the typed parameter-bean of PmCommentEmbedded. <br />
     * This is related to "<span style="color: #AD4747">whitebox:pmcomment:selectPmCommentEmbedded</span>" on MemberBhv.
     */
    public BsPmCommentEmbeddedPmb() {
    }

    // ===================================================================================
    //                                                                Typed Implementation
    //                                                                ====================
    /**
     * {@inheritDoc}
     */
    public String getOutsideSqlPath() {
        return "whitebox:pmcomment:selectPmCommentEmbedded";
    }

    /**
     * Get the type of an entity for result. (implementation)
     * @return The type instance of an entity, customize entity. (NotNull)
     */
    public Class<PmCommentEmbedded> getEntityType() {
        return PmCommentEmbedded.class;
    }

    // ===================================================================================
    //                                                                       Safety Result
    //                                                                       =============
    /**
     * {@inheritDoc}
     */
    public void checkSafetyResult(int safetyMaxResultSize) {
        _safetyMaxResultSize = safetyMaxResultSize;
    }

    /**
     * {@inheritDoc}
     */
    public int getSafetyMaxResultSize() {
        return _safetyMaxResultSize;
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected String filterStringParameter(String value) {
        if (isEmptyStringParameterAllowed()) {
            return value;
        }
        return convertEmptyToNull(value);
    }

    protected boolean isEmptyStringParameterAllowed() {
	    return DBFluteConfig.getInstance().isEmptyStringParameterAllowed();
    }

    protected String convertEmptyToNull(String value) {
	    return ParameterUtil.convertEmptyToNull(value);
    }

    protected String handleShortChar(String propertyName, String value, Integer size) {
        ShortCharHandlingMode mode = getShortCharHandlingMode(propertyName, value, size);
        return ParameterUtil.handleShortChar(propertyName, value, size, mode);
    }

    protected ShortCharHandlingMode getShortCharHandlingMode(String propertyName, String value, Integer size) {
        return ShortCharHandlingMode.NONE;
    }

    @SuppressWarnings("unchecked")
    protected <ELEMENT> ArrayList<ELEMENT> newArrayList(ELEMENT... elements) {
        Object obj = DfCollectionUtil.newArrayList(elements);
        return (ArrayList<ELEMENT>)obj; // to avoid the warning between JDK6 and JDK7
    }

    @SuppressWarnings("unchecked")
    protected <NUMBER extends Number> NUMBER toNumber(Object obj, Class<NUMBER> type) {
        return (NUMBER)DfTypeUtil.toNumber(obj, type);
    }

    protected Boolean toBoolean(Object obj) {
        return DfTypeUtil.toBoolean(obj);
    }

    protected Date toUtilDate(Date date) {
        return DfTypeUtil.toDate(date); // if sub class, re-create as pure date
    }

    protected String formatUtilDate(Date date) {
        String pattern = "yyyy-MM-dd";
        return DfTypeUtil.toString(date, pattern);
    }

    protected String formatByteArray(byte[] bytes) {
        return "byte[" + (bytes != null ? String.valueOf(bytes.length) : "null") + "]";
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * @return The display string of all parameters. (NotNull)
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DfTypeUtil.toClassTitle(this)).append(":");
        sb.append(xbuildColumnString());
        return sb.toString();
    }
    private String xbuildColumnString() {
        final String c = ", ";
        final StringBuilder sb = new StringBuilder();
        sb.append(c).append(_schema);
        sb.append(c).append(_schemaDot);
        if (sb.length() > 0) { sb.delete(0, c.length()); }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] schema <br />
     * @return The value of schema. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getSchema() {
        return filterStringParameter(_schema);
    }

    /**
     * [set] schema <br />
     * @param schema The value of schema. (NullAllowed)
     */
    public void setSchema(String schema) {
        _schema = schema;
    }

    /**
     * [get] schemaDot <br />
     * @return The value of schemaDot. (Nullable, NotEmptyString(when String): if empty string, returns null)
     */
    public String getSchemaDot() {
        return filterStringParameter(_schemaDot);
    }

    /**
     * [set] schemaDot <br />
     * @param schemaDot The value of schemaDot. (NullAllowed)
     */
    public void setSchemaDot(String schemaDot) {
        _schemaDot = schemaDot;
    }

}
