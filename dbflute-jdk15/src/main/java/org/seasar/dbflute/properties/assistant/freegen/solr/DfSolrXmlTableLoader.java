package org.seasar.dbflute.properties.assistant.freegen.solr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.seasar.dbflute.exception.factory.ExceptionMessageBuilder;
import org.seasar.dbflute.properties.assistant.freegen.DfFreeGenResource;
import org.seasar.dbflute.properties.assistant.freegen.DfFreeGenTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author jflute
 */
public class DfSolrXmlTableLoader {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static SAXParserFactory _saxFactory;
    static {
        _saxFactory = SAXParserFactory.newInstance();
        _saxFactory.setValidating(true);
    }

    // ===================================================================================
    //                                                                          Load Table
    //                                                                          ==========
    // ; resourceMap = map:{
    //     ; resourceType = PROP
    //     ; resourceFile = ../../.../schema.xml
    // }
    // ; outputMap = map:{
    //     ; templateFile = unused
    //     ; outputDirectory = ../src/main/java
    //     ; package = org.seasar.dbflute...
    //     ; className = unused
    // }
    // ; tableMap = map:{
    //     ; mappingMap = map:{
    //         ; type = map:{
    //             ; INTEGER = Integer
    //             ; VARCHAR = String
    //         }
    //     }
    // }
    public DfFreeGenTable loadTable(String requestName, DfFreeGenResource resource, Map<String, Object> tableMap,
            Map<String, Map<String, String>> mappingMap) {
        final String resourceFile = resource.getResourceFile();
        final String encoding = resource.hasEncoding() ? resource.getEncoding() : "UTF-8";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(resourceFile), encoding));
            final SAXParser saxParser = _saxFactory.newSAXParser();
            final InputSource is = new InputSource(br);
            final DfSolrXmlParserHandler parserHandler = new DfSolrXmlParserHandler(tableMap, mappingMap);
            saxParser.parse(is, parserHandler);
            return new DfFreeGenTable(tableMap, requestName, parserHandler.getColumnList());
        } catch (IOException e) {
            String msg = "Failed to read the properties:";
            msg = msg + " requestName=" + requestName + " resourceFile=" + resourceFile;
            throw new IllegalStateException(msg, e);
        } catch (ParserConfigurationException e) {
            handleException(resourceFile, encoding, e);
            return null; // unreachable
        } catch (SAXException e) {
            handleException(resourceFile, encoding, e);
            return null; // unreachable
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    protected void handleException(String xmlFile, String encoding, Exception e) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to parse the XML file.");
        br.addItem("XML File");
        br.addElement(xmlFile);
        br.addItem("Encoding");
        br.addElement(encoding);
        br.addItem("Exception");
        br.addElement(e.getClass().getName());
        br.addElement(e.getMessage());
        final String msg = br.buildExceptionMessage();
        throw new IllegalStateException(msg, e);
    }
}
