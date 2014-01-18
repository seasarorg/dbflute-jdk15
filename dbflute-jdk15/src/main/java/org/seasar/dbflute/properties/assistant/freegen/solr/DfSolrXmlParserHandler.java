package org.seasar.dbflute.properties.assistant.freegen.solr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.dbflute.util.DfCollectionUtil;
import org.seasar.dbflute.util.DfTypeUtil;
import org.seasar.dbflute.util.Srl;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author jflute
 */
public class DfSolrXmlParserHandler extends DefaultHandler {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final Map<String, Object> _tableMap;
    protected final Map<String, Map<String, String>> _mappingMap;
    protected final List<Map<String, Object>> _columnList = new ArrayList<Map<String, Object>>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfSolrXmlParserHandler(Map<String, Object> tableMap, Map<String, Map<String, String>> mappingMap) {
        _tableMap = tableMap;
        if (mappingMap != null) {
            final Map<String, String> typeMapping = mappingMap.get("type");
            final Map<String, String> mergedTypeMapping = getDefaultTypeMapping();
            if (typeMapping != null) {
                mergedTypeMapping.putAll(typeMapping);
            }
            mappingMap.put("type", mergedTypeMapping);
        }
        _mappingMap = mappingMap;
    }

    protected Map<String, String> getDefaultTypeMapping() {
        final Map<String, String> typeMapping = new LinkedHashMap<String, String>();
        typeMapping.put("string", "String");
        typeMapping.put("int", "Integer");
        typeMapping.put("long", "Long");
        typeMapping.put("float", "java.math.BigDecimal");
        typeMapping.put("double", "java.math.BigDecimal");
        typeMapping.put("date", "java.util.Date");
        typeMapping.put("boolean", "Boolean");
        typeMapping.put("binary", "byte[]");
        typeMapping.put("tint", "Integer");
        typeMapping.put("tlong", "Long");
        typeMapping.put("tfloat", "java.math.BigDecimal");
        typeMapping.put("tdouble", "java.math.BigDecimal");
        typeMapping.put("tdate", "java.util.Date");
        typeMapping.put("pint", "Integer");
        typeMapping.put("plong", "Long");
        typeMapping.put("pfloat", "java.math.BigDecimal");
        typeMapping.put("pdouble", "java.math.BigDecimal");
        typeMapping.put("pdate", "java.util.Date");
        // 'text*' has a special process
        return typeMapping;
    }

    // ===================================================================================
    //                                                                    Handler Override
    //                                                                    ================
    @Override
    public void startElement(String uri, String localName, String rawName, Attributes attributes) {
        try {
            if (rawName.equals("field")) { // basically only one on DBFlute
                final int length = attributes.getLength();
                final Map<String, Object> columnMap = new LinkedHashMap<String, Object>();
                for (int i = 0; i < length; i++) {
                    final String attrName = attributes.getQName(i);
                    final String attrValue;
                    {
                        final String plainValue = attributes.getValue(i);
                        final Map<String, String> mapping = getMapping(attrName);
                        final String mappedValue = mapping.get(plainValue);
                        if (mappedValue != null) {
                            attrValue = mappedValue;
                        } else {
                            if ("type".equals(attrName)) {
                                if (plainValue != null && plainValue.startsWith("text")) {
                                    attrValue = "String";
                                } else {
                                    attrValue = plainValue;
                                }
                            } else {
                                attrValue = plainValue;
                            }
                        }
                        if ("type".equals(attrName)) {
                            columnMap.put("nativeType", plainValue);
                        }
                    }
                    final Object registeredValue;
                    if ("name".equals(attrName)) {
                        final String camelizedName = Srl.camelize(attrValue);
                        columnMap.put("camelizedName", camelizedName);
                        columnMap.put("capCamelName", Srl.initCap(camelizedName));
                        columnMap.put("uncapCamelName", Srl.initUncap(camelizedName));
                        registeredValue = attrValue;
                    } else {
                        if (attrValue != null && (Srl.equalsIgnoreCase(attrValue, "true", "false"))) {
                            registeredValue = DfTypeUtil.toBoolean(attrValue);
                        } else {
                            registeredValue = attrValue;
                        }
                    }
                    columnMap.put(attrName, registeredValue);
                }
                final Object multiValued = columnMap.get("multiValued");
                if (multiValued != null && multiValued instanceof Boolean && (Boolean) multiValued) {
                    final Object type = columnMap.get("type");
                    columnMap.put("type", type + "[]");
                }
                _columnList.add(columnMap);
            }
        } catch (Exception e) {
            String msg = "Failed to analyze schema data of the XML:";
            msg = msg + " uri=" + uri + " localName=" + localName + " rawName=" + rawName;
            throw new IllegalStateException(msg, e);
        }
    }

    protected Map<String, String> getMapping(String key) {
        if (_mappingMap == null) {
            return DfCollectionUtil.emptyMap();
        }
        final Map<String, String> map = _mappingMap.get(key);
        if (map == null) {
            return DfCollectionUtil.emptyMap();
        }
        return map;
    }

    @Override
    public void endElement(String uri, String localName, String rawName) {
        // *commented out because of too many logging
        //if (log.isDebugEnabled()) {
        //    log.debug("endElement(" + uri + ", " + localName + ", " + rawName + ") called");
        //}
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public List<Map<String, Object>> getColumnList() {
        return _columnList;
    }
}
