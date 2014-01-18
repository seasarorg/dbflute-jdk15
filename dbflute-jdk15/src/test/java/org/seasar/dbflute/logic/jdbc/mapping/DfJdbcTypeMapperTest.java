package org.seasar.dbflute.logic.jdbc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.torque.engine.database.model.TypeMap;
import org.junit.Test;
import org.seasar.dbflute.DfBuildProperties;
import org.seasar.dbflute.logic.jdbc.mapping.DfJdbcTypeMapper.Resource;

/**
 * @author jflute
 * @since 0.9.5 (2009/04/21 Tuesday)
 */
public class DfJdbcTypeMapperTest {

    @Test
    public void test_getColumnTorqueType_NameToTorqueTypeMap() {
        initializeEmptyProperty();
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        nameToTorqueTypeMap.put("foo", "bar");
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().oracle());
        // ## Act & Assert ##
        assertEquals("bar", mapper.getColumnJdbcType(Types.TIMESTAMP, "foo"));
        assertEquals(TypeMap.TIMESTAMP, mapper.getColumnJdbcType(Types.TIMESTAMP, "bar"));
    }

    @Test
    public void test_getColumnTorqueType_Java_Oracle() {
        initializeEmptyProperty();
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().oracle());

        // ## Act & Assert ##
        assertEquals(TypeMap.TIMESTAMP, mapper.getColumnJdbcType(Types.TIMESTAMP, "timestamp"));
        assertEquals(TypeMap.DATE, mapper.getColumnJdbcType(Types.TIMESTAMP, "date"));
        assertEquals(TypeMap.DATE, mapper.getColumnJdbcType(Types.DATE, "date"));
        assertEquals(TypeMap.VARCHAR, mapper.getColumnJdbcType(Types.VARCHAR, "varchar"));
        assertEquals(TypeMap.VARCHAR, mapper.getColumnJdbcType(Types.OTHER, "nvarchar"));
    }

    @Test
    public void test_getColumnTorqueType_Java_PostgreSQL() {
        initializeEmptyProperty();
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().postgreSQL());

        // ## Act & Assert ##
        assertEquals(TypeMap.TIMESTAMP, mapper.getColumnJdbcType(Types.TIMESTAMP, "timestamp"));
        assertEquals(TypeMap.TIMESTAMP, mapper.getColumnJdbcType(Types.TIMESTAMP, "date"));
        assertEquals(TypeMap.DATE, mapper.getColumnJdbcType(Types.DATE, "date"));
        assertEquals(TypeMap.VARCHAR, mapper.getColumnJdbcType(Types.VARCHAR, "varchar"));
        assertEquals(TypeMap.VARCHAR, mapper.getColumnJdbcType(Types.OTHER, "nvarchar"));
        assertEquals(TypeMap.BLOB, mapper.getColumnJdbcType(Types.OTHER, "oid"));
        assertEquals(TypeMap.UUID, mapper.getColumnJdbcType(Types.OTHER, "uuid"));
    }

    @Test
    public void test_getColumnTorqueType_OriginalMapping() {
        // ## Arrange ##
        Properties prop = new Properties();
        prop.setProperty("torque.typeMappingMap", "map:{FOO=java.bar.Tender}");
        initializeTestProperty(prop);
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        nameToTorqueTypeMap.put("__int4", "FOO");
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().oracle());

        // ## Act & Assert ##
        assertEquals("FOO", mapper.getColumnJdbcType(Types.TIMESTAMP, "__int4"));
        assertEquals("java.bar.Tender", TypeMap.findJavaNativeByJdbcType("FOO", 0, 0));
    }

    @Test
    public void test_isOracleNCharOrNVarchar_basic() {
        // ## Arrange ##
        initializeEmptyProperty();
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        nameToTorqueTypeMap.put("foo", "bar");
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().oracle());

        // ## Act & Assert ##
        assertTrue(mapper.isOracleNCharOrNVarchar("NVARCHAR2"));
        assertTrue(mapper.isOracleNCharOrNVarchar("NCHAR2"));
        assertFalse(mapper.isOracleNCharOrNVarchar("VARCHAR"));
        assertFalse(mapper.isOracleNCharOrNVarchar("VARCHAR2"));
        assertFalse(mapper.isOracleNCharOrNVarchar("CHAR"));
        assertFalse(mapper.isOracleNCharOrNVarchar("CLOB"));
        assertFalse(mapper.isOracleNCharOrNVarchar("NCLOB"));
    }

    @Test
    public void test_isOracleNCharOrNVarcharOrNClob_basic() {
        // ## Arrange ##
        initializeEmptyProperty();
        Map<String, String> nameToTorqueTypeMap = new LinkedHashMap<String, String>();
        nameToTorqueTypeMap.put("foo", "bar");
        DfJdbcTypeMapper mapper = new DfJdbcTypeMapper(nameToTorqueTypeMap, new TestResource().java().oracle());

        // ## Act & Assert ##
        assertTrue(mapper.isOracleNCharOrNVarcharOrNClob("NVARCHAR2"));
        assertTrue(mapper.isOracleNCharOrNVarcharOrNClob("NCHAR2"));
        assertTrue(mapper.isOracleNCharOrNVarcharOrNClob("NCLOB"));
        assertFalse(mapper.isOracleNCharOrNVarcharOrNClob("VARCHAR"));
        assertFalse(mapper.isOracleNCharOrNVarcharOrNClob("VARCHAR2"));
        assertFalse(mapper.isOracleNCharOrNVarcharOrNClob("CHAR"));
        assertFalse(mapper.isOracleNCharOrNVarcharOrNClob("CLOB"));
    }

    protected void initializeEmptyProperty() {
        DfBuildProperties.getInstance().setProperties(new Properties());
        DfBuildProperties.getInstance().getHandler().reload();
        TypeMap.reload();
    }

    protected void initializeTestProperty(Properties prop) {
        DfBuildProperties.getInstance().setProperties(prop);
        DfBuildProperties.getInstance().getHandler().reload();
        TypeMap.reload();
    }

    protected static class TestResource implements Resource {
        protected boolean _targetLanguageJava;
        protected boolean _databaseOracle;
        protected boolean _databasePostgreSQL;
        protected boolean _databaseSQLServer;

        public TestResource java() {
            _targetLanguageJava = true;
            return this;
        }

        public TestResource oracle() {
            _databaseOracle = true;
            return this;
        }

        public TestResource postgreSQL() {
            _databasePostgreSQL = true;
            return this;
        }

        public TestResource sqlServer() {
            _databaseSQLServer = true;
            return this;
        }

        public boolean isLangJava() {
            return _targetLanguageJava;
        }

        public boolean isDbmsOracle() {
            return _databaseOracle;
        }

        public boolean isDbmsPostgreSQL() {
            return _databasePostgreSQL;
        }

        public boolean isDbmsSQLServer() {
            return _databaseSQLServer;
        }
    }
}
