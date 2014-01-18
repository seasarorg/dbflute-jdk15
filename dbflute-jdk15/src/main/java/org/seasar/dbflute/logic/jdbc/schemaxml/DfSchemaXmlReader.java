package org.seasar.dbflute.logic.jdbc.schemaxml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.torque.engine.database.model.AppData;
import org.apache.torque.engine.database.model.UnifiedSchema;
import org.apache.torque.engine.database.transform.XmlToAppData;
import org.apache.torque.engine.database.transform.XmlToAppData.XmlReadingFilter;
import org.seasar.dbflute.DfBuildProperties;
import org.seasar.dbflute.properties.DfBasicProperties;
import org.seasar.dbflute.properties.DfDatabaseProperties;
import org.seasar.dbflute.properties.assistant.DfAdditionalSchemaInfo;
import org.seasar.dbflute.properties.facade.DfDatabaseTypeFacadeProp;
import org.seasar.dbflute.properties.facade.DfSchemaXmlFacadeProp;
import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfNameHintUtil;

/**
 * @author jflute
 */
public class DfSchemaXmlReader {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String _schemaXml;
    protected final String _databaseType;
    protected final XmlReadingFilter _tableFilter;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * @param schemaXml The path of SchemaXML file relative to DBFlute client. (NotNull)
     * @param databaseType The type of database for the application.
     * @param tableFilter The filter of table by name when reading XML. (NullAllowed)
     */
    protected DfSchemaXmlReader(String schemaXml, String databaseType, XmlReadingFilter tableFilter) {
        _schemaXml = schemaXml;
        _databaseType = databaseType;
        _tableFilter = tableFilter;
    }

    public static DfSchemaXmlReader createAsCoreToGenerate() {
        final DfDatabaseProperties databaseProp = DfBuildProperties.getInstance().getDatabaseProperties();
        return doCreateAsCoreTo(new DfGenetateXmlReadingFilter(databaseProp));
    }

    public static DfSchemaXmlReader createAsCoreToManage() {
        return doCreateAsCoreTo(null);
    }

    public static DfSchemaXmlReader doCreateAsCoreTo(DfGenetateXmlReadingFilter tableFilter) {
        final DfBasicProperties basicProp = DfBuildProperties.getInstance().getBasicProperties();
        final DfSchemaXmlFacadeProp facadeProp = basicProp.getSchemaXmlFacadeProp();
        final String schemaXml = facadeProp.getProejctSchemaXMLFile();
        return doCreateAs(schemaXml, tableFilter);
    }

    public static DfSchemaXmlReader createAsFlexibleToManage(String schemaXml) {
        return doCreateAs(schemaXml, null);
    }

    public static DfSchemaXmlReader doCreateAs(String schemaXml, DfGenetateXmlReadingFilter tableFilter) {
        final DfBasicProperties basicProp = DfBuildProperties.getInstance().getBasicProperties();
        final DfDatabaseTypeFacadeProp facadeProp = basicProp.getDatabaseTypeFacadeProp();
        final String databaseType = facadeProp.getTargetDatabase();
        return new DfSchemaXmlReader(schemaXml, databaseType, tableFilter);
    }

    protected static class DfGenetateXmlReadingFilter implements XmlReadingFilter {
        protected DfDatabaseProperties _databaseProp;

        public DfGenetateXmlReadingFilter(DfDatabaseProperties databaseProp) {
            _databaseProp = databaseProp;
        }

        public boolean isTableExcept(UnifiedSchema unifiedSchema, String tableName) {
            final DfAdditionalSchemaInfo additional = _databaseProp.getAdditionalSchemaInfo(unifiedSchema);
            final List<String> tableExceptGenOnlyList;
            if (additional != null) {
                tableExceptGenOnlyList = additional.getTableExceptGenOnlyList();
            } else {
                tableExceptGenOnlyList = _databaseProp.getTableExceptGenOnlyList();
            }
            final List<String> targetEmptyList = DfCollectionUtil.emptyList();
            return !isTargetByHint(tableName, targetEmptyList, tableExceptGenOnlyList);
        }

        public boolean isColumnExcept(UnifiedSchema unifiedSchema, String tableName, String columnName) {
            final DfAdditionalSchemaInfo additional = _databaseProp.getAdditionalSchemaInfo(unifiedSchema);
            final Map<String, List<String>> tableExceptGenOnlyMap;
            if (additional != null) {
                tableExceptGenOnlyMap = additional.getColumnExceptGenOnlyMap();
            } else {
                tableExceptGenOnlyMap = _databaseProp.getColumnExceptGenOnlyMap();
            }
            final List<String> targetEmptyList = DfCollectionUtil.emptyList();
            final List<String> columnExceptGenOnlyList = tableExceptGenOnlyMap.get(tableName);
            if (columnExceptGenOnlyList != null) {
                return !isTargetByHint(columnName, targetEmptyList, columnExceptGenOnlyList);
            } else {
                return false;
            }
        }

        protected boolean isTargetByHint(String name, List<String> targetList, List<String> exceptList) {
            return DfNameHintUtil.isTargetByHint(name, targetList, exceptList);
        }
    }

    /**
     * @param schemaXml The path of SchemaXML file relative to DBFlute client. (NotNull)
     * @param databaseType The type of database for the application.
     * @param tableFilter The filter of table by name when reading XML. (NullAllowed)
     * @return The instance of this. (NotNull)
     */
    public static DfSchemaXmlReader createAsPlain(String schemaXml, String databaseType, XmlReadingFilter tableFilter) {
        return new DfSchemaXmlReader(schemaXml, databaseType, tableFilter);
    }

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====
    public AppData read() {
        if (_schemaXml == null) {
            String msg = "The property 'schemaXml' should not be null.";
            throw new IllegalStateException(msg);
        }
        final AppData schemaData;
        try {
            schemaData = createXmlToAppData().parseFile(_schemaXml);
        } catch (IOException e) {
            String msg = "Failed to read the SchemaXML: " + _schemaXml;
            throw new IllegalStateException(msg);
        }
        schemaData.setName(grokName(_schemaXml));
        return schemaData;
    }

    public boolean exists() {
        return new File(_schemaXml).exists();
    }

    protected String grokName(String xmlFile) {
        final String name;
        int fileSeparatorLastIndex = xmlFile.lastIndexOf("/");
        if (fileSeparatorLastIndex > -1) { // basically true
            fileSeparatorLastIndex++;
            final int dotLastIndex = xmlFile.lastIndexOf('.');
            if (fileSeparatorLastIndex < dotLastIndex) { // mainly (removing extension)
                // ./schema/project-schema-exampledb.xml to project-schema-exampledb
                name = xmlFile.substring(fileSeparatorLastIndex, dotLastIndex);
            } else {
                name = xmlFile.substring(fileSeparatorLastIndex);
            }
        } else {
            name = "schema"; // as default
        }
        return name;
    }

    protected XmlToAppData createXmlToAppData() {
        return new XmlToAppData(_databaseType, _tableFilter);
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getSchemaXml() {
        return _schemaXml;
    }
}
