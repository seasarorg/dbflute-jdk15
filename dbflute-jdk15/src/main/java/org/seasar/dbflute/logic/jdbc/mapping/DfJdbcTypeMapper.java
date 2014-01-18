/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dbflute.logic.jdbc.mapping;

import java.sql.Types;
import java.util.Map;

import org.apache.torque.engine.database.model.TypeMap;
import org.seasar.dbflute.util.Srl;

/**
 * @author jflute
 */
public class DfJdbcTypeMapper {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected Map<String, String> _nameToJdbcTypeMap;
    protected Resource _resource;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public DfJdbcTypeMapper(Map<String, String> nameToJdbcTypeMap, Resource resource) {
        _nameToJdbcTypeMap = nameToJdbcTypeMap;
        _resource = resource;
    }

    public static interface Resource {
        boolean isLangJava();

        boolean isDbmsPostgreSQL();

        boolean isDbmsOracle();

        boolean isDbmsSQLServer();
    }

    // ===================================================================================
    //                                                                 Torque Type Getting
    //                                                                 ===================
    /**
     * Get the JDBC type of the column. <br /> 
     * The priority of mapping is as follows:
     * <pre>
     * 1. The specified type mapping by DB type name (typeMappingMap.dfprop)
     * 2. The fixed type mapping (PostgreSQL's OID and Oracle's Date and so on...)
     * 3. The standard type mapping by JDBC type if the type is not 'OTHER' (typeMappingMap.dfprop)
     * 4. The auto type mapping by DB type name
     * 5. String finally
     * </pre>
     * @param jdbcDefType The definition type of JDBC.
     * @param dbTypeName The name of DB data type. (NullAllowed: If null, the mapping using this is invalid)
     * @return The JDBC type of the column. (NotNull)
     */
    public String getColumnJdbcType(int jdbcDefType, String dbTypeName) {
        final String jdbcType = doGetColumnJdbcType(jdbcDefType, dbTypeName);
        if (jdbcType == null) {
            // * * * * * *
            // Priority 5
            // * * * * * *
            return getVarcharJdbcType();
        }
        return jdbcType;
    }

    /**
     * Does it have a mapping about the type?
     * @param jdbcDefType The definition type of JDBC.
     * @param dbTypeName The name of DB data type. (NullAllowed: If null, the mapping using this is invalid)
     * @return The JDBC type of the column. (NotNull)
     */
    public boolean hasMappingJdbcType(int jdbcDefType, String dbTypeName) {
        return doGetColumnJdbcType(jdbcDefType, dbTypeName) != null;
    }

    public String doGetColumnJdbcType(int jdbcDefType, String dbTypeName) {
        // * * * * * *
        // Priority 1
        // * * * * * *
        if (dbTypeName != null) {
            if (_nameToJdbcTypeMap != null && !_nameToJdbcTypeMap.isEmpty()) {
                final String torqueType = _nameToJdbcTypeMap.get(dbTypeName);
                if (torqueType != null) {
                    return (String) torqueType;
                }
            }
        }

        // * * * * * *
        // Priority 2
        // * * * * * *
        final String adjustment = processForcedAdjustment(jdbcDefType, dbTypeName);
        if (adjustment != null) {
            return adjustment;
        }

        // * * * * * *
        // Priority 3
        // * * * * * *
        if (!isOtherType(jdbcDefType)) {
            final String jdbcType = getJdbcType(jdbcDefType);
            if (Srl.is_NotNull_and_NotEmpty(jdbcType)) {
                return jdbcType;
            }
        }
        // here means that it cannot determine by jdbcDefValue

        // * * * * * *
        // Priority 4
        // * * * * * *
        // /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        // Here is coming if the JDBC type is OTHER or is not found in TypeMap.
        // - - - - - - - - - -/
        if (containsIgnoreCase(dbTypeName, "varchar")) {
            return getVarcharJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "char")) {
            return getCharJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "numeric", "number", "decimal")) {
            return getNumericJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "timestamp", "datetime")) {
            return getTimestampJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "date")) {
            return getDateJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "time")) {
            return getTimeJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "clob")) {
            return getClobJdbcType();
        } else if (containsIgnoreCase(dbTypeName, "blob")) {
            return getBlobJdbcType();
        } else {
            return null;
        }
    }

    protected String processForcedAdjustment(int jdbcDefValue, String dbTypeName) {
        if (isConceptTypeUUID(dbTypeName) && _resource.isLangJava()) {
            // /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            // This is for Java only because the type has not been checked yet on C#.
            // - - - - - - - - - -/
            // [UUID Headache]: The reason why UUID type has not been supported yet on JDBC.
            return TypeMap.UUID;
        }
        if (isConceptTypeBytesOid(dbTypeName)) {
            return getBlobJdbcType();
        }
        if (isPostgreSQLInterval(dbTypeName)) {
            return getTimeJdbcType();
        }
        if (isOracleCompatibleDate(jdbcDefValue, dbTypeName)) {
            // for compatibility to Oracle's JDBC driver
            return getDateJdbcType();
        }
        return null;
    }

    // -----------------------------------------------------
    //                                          Concept Type
    //                                          ------------
    public boolean isConceptTypeUUID(final String dbTypeName) { // mapped by UUID
        if (isPostgreSQLUuid(dbTypeName)) {
            return true;
        }
        if (isSQLServerUniqueIdentifier(dbTypeName)) {
            return true;
        }
        return false;
    }

    public boolean isConceptTypeStringClob(final String dbTypeName) {
        // basically types needs to be handled as stream on JDBC
        // now only Oracle's CLOB
        return isOracleClob(dbTypeName);
    }

    public boolean isConceptTypeBytesOid(final String dbTypeName) {
        // now only PostgreSQL's oid
        return isPostgreSQLOid(dbTypeName);
    }

    public boolean isConceptTypeFixedLengthString(final String dbTypeName) {
        return isPostgreSQLBpChar(dbTypeName); // procedure only
    }

    public boolean isConceptTypeObjectBindingBigDecimal(final String dbTypeName) {
        return isPostgreSQLNumeric(dbTypeName); // procedure only
    }

    // -----------------------------------------------------
    //                                         Pinpoint Type
    //                                         -------------
    public boolean isPostgreSQLBpChar(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && matchIgnoreCase(dbTypeName, "bpchar");
    }

    public boolean isPostgreSQLNumeric(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && matchIgnoreCase(dbTypeName, "numeric");
    }

    public boolean isPostgreSQLUuid(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && matchIgnoreCase(dbTypeName, "uuid");
    }

    public boolean isPostgreSQLOid(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && matchIgnoreCase(dbTypeName, "oid");
    }

    public boolean isPostgreSQLInterval(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && matchIgnoreCase(dbTypeName, "interval");
    }

    public boolean isPostgreSQLCursor(final String dbTypeName) {
        return _resource.isDbmsPostgreSQL() && containsIgnoreCase(dbTypeName, "cursor");
    }

    public boolean isOracleClob(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "clob");
    }

    public boolean isOracleNCharOrNVarchar(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "nchar", "nvarchar");
    }

    public boolean isOracleNCharOrNVarcharOrNClob(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "nchar", "nvarchar", "nclob");
    }

    public boolean isOracleNumber(final String dbTypeName) {
        return _resource.isDbmsOracle() && matchIgnoreCase(dbTypeName, "number");
    }

    public boolean isOracleDate(final String dbTypeName) {
        return _resource.isDbmsOracle() && matchIgnoreCase(dbTypeName, "date");
    }

    public boolean isOracleCompatibleDate(final int jdbcType, final String dbTypeName) {
        return _resource.isDbmsOracle() && java.sql.Types.TIMESTAMP == jdbcType && matchIgnoreCase(dbTypeName, "date");
    }

    public boolean isOracleBinaryFloatDouble(final String dbTypeName) {
        return _resource.isDbmsOracle() && matchIgnoreCase(dbTypeName, "binary_float", "binary_double");
    }

    public boolean isOracleCursor(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "cursor");
    }

    public boolean isOracleTable(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "table");
    }

    public boolean isOracleVArray(final String dbTypeName) {
        return _resource.isDbmsOracle() && containsIgnoreCase(dbTypeName, "varray");
    }

    public boolean isSQLServerUniqueIdentifier(final String dbTypeName) {
        return _resource.isDbmsSQLServer() && matchIgnoreCase(dbTypeName, "uniqueidentifier");
    }

    // -----------------------------------------------------
    //                                             JDBC Type
    //                                             ---------
    protected boolean isOtherType(final int jdbcDefValue) {
        return Types.OTHER == jdbcDefValue;
    }

    protected String getJdbcType(int jdbcDefValue) {
        return TypeMap.findJdbcTypeByJdbcDefValue(jdbcDefValue);
    }

    protected String getVarcharJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.VARCHAR);
    }

    protected String getCharJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.CHAR);
    }

    protected String getNumericJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.NUMERIC);
    }

    protected String getTimestampJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.TIMESTAMP);
    }

    protected String getTimeJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.TIME);
    }

    protected String getDateJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.DATE);
    }

    protected String getClobJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.CLOB);
    }

    protected String getBlobJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.BLOB);
    }

    protected String getBinaryJdbcType() {
        return TypeMap.findJdbcTypeByJdbcDefValue(java.sql.Types.BINARY);
    }

    // ===================================================================================
    //                                                                     Matching Helper
    //                                                                     ===============
    protected boolean matchIgnoreCase(String dbTypeName, String... types) {
        if (dbTypeName == null) {
            return false;
        }
        for (String type : types) {
            if (dbTypeName.trim().equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsIgnoreCase(String dbTypeName, String... types) {
        if (dbTypeName == null) {
            return false;
        }
        final String trimmedLowerName = dbTypeName.toLowerCase().trim();
        for (String type : types) {
            if (trimmedLowerName.contains(type.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return _nameToJdbcTypeMap + ":" + _resource;
    }
}