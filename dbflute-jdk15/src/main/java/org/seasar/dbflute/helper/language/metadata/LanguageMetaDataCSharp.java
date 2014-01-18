package org.seasar.dbflute.helper.language.metadata;

import java.util.List;
import java.util.Map;

import org.seasar.dbflute.util.DfCollectionUtil;

/**
 * @author jflute
 */
public class LanguageMetaDataCSharp implements LanguageMetaData {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final List<String> _stringList = newArrayList("String");
    protected final List<String> _numberList = newArrayList("decimal?", "int?", "long?");
    protected final List<String> _dateList = newArrayList("DateTime?");
    protected final List<String> _booleanList = newArrayList("bool?");
    protected final List<String> _binaryList = newArrayList("byte[]");

    protected <ELEMENT> List<ELEMENT> newArrayList(ELEMENT... elements) {
        return DfCollectionUtil.newArrayList(elements);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    public Map<String, Object> getJdbcToJavaNativeMap() {
        final Map<String, Object> map = DfCollectionUtil.newLinkedHashMap();
        map.put("CHAR", "String");
        map.put("VARCHAR", "String");
        map.put("LONGVARCHAR", "String");
        map.put("NUMERIC", "decimal?");
        map.put("DECIMAL", "decimal?");
        map.put("BIT", "bool?");
        map.put("TINYINT", "int?");
        map.put("SMALLINT", "int?");
        map.put("INTEGER", "int?");
        map.put("BIGINT", "long?");
        map.put("REAL", "decimal?");
        map.put("FLOAT", "decimal?");
        map.put("DOUBLE", "decimal?");
        map.put("DATE", "DateTime?");
        map.put("TIME", "DateTime?");
        map.put("TIMESTAMP", "DateTime?");
        return map;
    }

    // ===================================================================================
    //                                                                         Suffix List
    //                                                                         ===========
    public List<String> getStringList() {
        return _stringList;
    }

    public List<String> getNumberList() {
        return _numberList;
    }

    public List<String> getDateList() {
        return _dateList;
    }

    public List<String> getBooleanList() {
        return _booleanList;
    }

    public List<String> getBinaryList() {
        return _binaryList;
    }
}
