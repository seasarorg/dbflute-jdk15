/*
 * Copyright(c) DBFlute TestCo.,TestLtd. All Rights Reserved.
 */
package com.example.dbflute.spring.dbflute.bsentity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.Entity;
import com.example.dbflute.spring.dbflute.allcommon.DBMetaInstanceHandler;
import com.example.dbflute.spring.dbflute.exentity.*;

/**
 * The entity of VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN as TABLE. <br />
 * <pre>
 * [primary-key]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID
 * 
 * [column]
 *     THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME, SHORT_NAME, SHORT_SIZE
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     
 * 
 * [version-no]
 *     
 * 
 * [foreign-table]
 *     
 * 
 * [referrer-table]
 *     VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF
 * 
 * [foreign-property]
 *     
 * 
 * [referrer-property]
 *     vendorTheLongAndWindingTableAndColumnRefList
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsVendorTheLongAndWindingTableAndColumn implements Entity, Serializable, Cloneable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    /** THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID: {PK, NotNull, BIGINT(19)} */
    protected Long _theLongAndWindingTableAndColumnId;

    /** THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME: {UQ, NotNull, VARCHAR(200)} */
    protected String _theLongAndWindingTableAndColumnName;

    /** SHORT_NAME: {NotNull, VARCHAR(200)} */
    protected String _shortName;

    /** SHORT_SIZE: {NotNull, INTEGER(10)} */
    protected Integer _shortSize;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. (NotNull) */
    protected final EntityModifiedProperties __modifiedProperties = newModifiedProperties();

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    public String getTableDbName() {
        return "VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN";
    }

    /**
     * {@inheritDoc}
     */
    public String getTablePropertyName() { // according to Java Beans rule
        return "vendorTheLongAndWindingTableAndColumn";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /**
     * {@inheritDoc}
     */
    public DBMeta getDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(getTableDbName());
    }

    // ===================================================================================
    //                                                                         Primary Key
    //                                                                         ===========
    /**
     * {@inheritDoc}
     */
    public boolean hasPrimaryKeyValue() {
        if (getTheLongAndWindingTableAndColumnId() == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    /** VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF by your THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, named 'vendorTheLongAndWindingTableAndColumnRefList'. */
    protected List<VendorTheLongAndWindingTableAndColumnRef> _vendorTheLongAndWindingTableAndColumnRefList;

    /**
     * VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF by your THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, named 'vendorTheLongAndWindingTableAndColumnRefList'.
     * @return The entity list of referrer property 'vendorTheLongAndWindingTableAndColumnRefList'. (NotNull: If it's not loaded yet, initializes the list instance of referrer as empty and returns it.)
     */
    public List<VendorTheLongAndWindingTableAndColumnRef> getVendorTheLongAndWindingTableAndColumnRefList() {
        if (_vendorTheLongAndWindingTableAndColumnRefList == null) { _vendorTheLongAndWindingTableAndColumnRefList = newReferrerList(); }
        return _vendorTheLongAndWindingTableAndColumnRefList;
    }

    /**
     * VENDOR_THE_LONG_AND_WINDING_TABLE_AND_COLUMN_REF by your THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID, named 'vendorTheLongAndWindingTableAndColumnRefList'.
     * @param vendorTheLongAndWindingTableAndColumnRefList The entity list of referrer property 'vendorTheLongAndWindingTableAndColumnRefList'. (NullAllowed)
     */
    public void setVendorTheLongAndWindingTableAndColumnRefList(List<VendorTheLongAndWindingTableAndColumnRef> vendorTheLongAndWindingTableAndColumnRefList) {
        _vendorTheLongAndWindingTableAndColumnRefList = vendorTheLongAndWindingTableAndColumnRefList;
    }

    protected <ELEMENT> List<ELEMENT> newReferrerList() {
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                 Modified Properties
    //                                                                 ===================
    /**
     * {@inheritDoc}
     */
    public Set<String> modifiedProperties() {
        return __modifiedProperties.getPropertyNames();
    }

    /**
     * {@inheritDoc}
     */
    public void clearModifiedInfo() {
        __modifiedProperties.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasModification() {
        return !__modifiedProperties.isEmpty();
    }

    protected EntityModifiedProperties newModifiedProperties() {
        return new EntityModifiedProperties();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * Determine the object is equal with this. <br />
     * If primary-keys or columns of the other are same as this one, returns true.
     * @param other The other entity. (NullAllowed)
     * @return Comparing result.
     */
    public boolean equals(Object other) {
        if (other == null || !(other instanceof BsVendorTheLongAndWindingTableAndColumn)) { return false; }
        BsVendorTheLongAndWindingTableAndColumn otherEntity = (BsVendorTheLongAndWindingTableAndColumn)other;
        if (!xSV(getTheLongAndWindingTableAndColumnId(), otherEntity.getTheLongAndWindingTableAndColumnId())) { return false; }
        return true;
    }
    protected boolean xSV(Object value1, Object value2) { // isSameValue()
        return InternalUtil.isSameValue(value1, value2);
    }

    /**
     * Calculate the hash-code from primary-keys or columns.
     * @return The hash-code from primary-key or columns.
     */
    public int hashCode() {
        int result = 17;
        result = xCH(result, getTableDbName());
        result = xCH(result, getTheLongAndWindingTableAndColumnId());
        return result;
    }
    protected int xCH(int result, Object value) { // calculateHashcode()
        return InternalUtil.calculateHashcode(result, value);
    }

    /**
     * {@inheritDoc}
     */
    public int instanceHash() {
        return super.hashCode();
    }

    /**
     * Convert to display string of entity's data. (no relation data)
     * @return The display string of all columns and relation existences. (NotNull)
     */
    public String toString() {
        return buildDisplayString(InternalUtil.toClassTitle(this), true, true);
    }

    /**
     * {@inheritDoc}
     */
    public String toStringWithRelation() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        String l = "\n  ";
        if (_vendorTheLongAndWindingTableAndColumnRefList != null) { for (Entity e : _vendorTheLongAndWindingTableAndColumnRefList)
        { if (e != null) { sb.append(l).append(xbRDS(e, "vendorTheLongAndWindingTableAndColumnRefList")); } } }
        return sb.toString();
    }
    protected String xbRDS(Entity e, String name) { // buildRelationDisplayString()
        return e.buildDisplayString(name, true, true);
    }

    /**
     * {@inheritDoc}
     */
    public String buildDisplayString(String name, boolean column, boolean relation) {
        StringBuilder sb = new StringBuilder();
        if (name != null) { sb.append(name).append(column || relation ? ":" : ""); }
        if (column) { sb.append(buildColumnString()); }
        if (relation) { sb.append(buildRelationString()); }
        sb.append("@").append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
    protected String buildColumnString() {
        StringBuilder sb = new StringBuilder();
        String delimiter = ", ";
        sb.append(delimiter).append(getTheLongAndWindingTableAndColumnId());
        sb.append(delimiter).append(getTheLongAndWindingTableAndColumnName());
        sb.append(delimiter).append(getShortName());
        sb.append(delimiter).append(getShortSize());
        if (sb.length() > delimiter.length()) {
            sb.delete(0, delimiter.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }
    protected String buildRelationString() {
        StringBuilder sb = new StringBuilder();
        String c = ",";
        if (_vendorTheLongAndWindingTableAndColumnRefList != null && !_vendorTheLongAndWindingTableAndColumnRefList.isEmpty())
        { sb.append(c).append("vendorTheLongAndWindingTableAndColumnRefList"); }
        if (sb.length() > c.length()) {
            sb.delete(0, c.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    public VendorTheLongAndWindingTableAndColumn clone() {
        try {
            return (VendorTheLongAndWindingTableAndColumn)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: " + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID: {PK, NotNull, BIGINT(19)} <br />
     * @return The value of the column 'THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID'. (NullAllowed)
     */
    public Long getTheLongAndWindingTableAndColumnId() {
        return _theLongAndWindingTableAndColumnId;
    }

    /**
     * [set] THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID: {PK, NotNull, BIGINT(19)} <br />
     * @param theLongAndWindingTableAndColumnId The value of the column 'THE_LONG_AND_WINDING_TABLE_AND_COLUMN_ID'. (NullAllowed)
     */
    public void setTheLongAndWindingTableAndColumnId(Long theLongAndWindingTableAndColumnId) {
        __modifiedProperties.addPropertyName("theLongAndWindingTableAndColumnId");
        this._theLongAndWindingTableAndColumnId = theLongAndWindingTableAndColumnId;
    }

    /**
     * [get] THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME: {UQ, NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME'. (NullAllowed)
     */
    public String getTheLongAndWindingTableAndColumnName() {
        return _theLongAndWindingTableAndColumnName;
    }

    /**
     * [set] THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME: {UQ, NotNull, VARCHAR(200)} <br />
     * @param theLongAndWindingTableAndColumnName The value of the column 'THE_LONG_AND_WINDING_TABLE_AND_COLUMN_NAME'. (NullAllowed)
     */
    public void setTheLongAndWindingTableAndColumnName(String theLongAndWindingTableAndColumnName) {
        __modifiedProperties.addPropertyName("theLongAndWindingTableAndColumnName");
        this._theLongAndWindingTableAndColumnName = theLongAndWindingTableAndColumnName;
    }

    /**
     * [get] SHORT_NAME: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'SHORT_NAME'. (NullAllowed)
     */
    public String getShortName() {
        return _shortName;
    }

    /**
     * [set] SHORT_NAME: {NotNull, VARCHAR(200)} <br />
     * @param shortName The value of the column 'SHORT_NAME'. (NullAllowed)
     */
    public void setShortName(String shortName) {
        __modifiedProperties.addPropertyName("shortName");
        this._shortName = shortName;
    }

    /**
     * [get] SHORT_SIZE: {NotNull, INTEGER(10)} <br />
     * @return The value of the column 'SHORT_SIZE'. (NullAllowed)
     */
    public Integer getShortSize() {
        return _shortSize;
    }

    /**
     * [set] SHORT_SIZE: {NotNull, INTEGER(10)} <br />
     * @param shortSize The value of the column 'SHORT_SIZE'. (NullAllowed)
     */
    public void setShortSize(Integer shortSize) {
        __modifiedProperties.addPropertyName("shortSize");
        this._shortSize = shortSize;
    }
}
