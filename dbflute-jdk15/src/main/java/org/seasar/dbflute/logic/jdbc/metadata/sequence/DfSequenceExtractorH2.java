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
package org.seasar.dbflute.logic.jdbc.metadata.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.engine.database.model.UnifiedSchema;
import org.seasar.dbflute.helper.StringKeyMap;
import org.seasar.dbflute.helper.jdbc.facade.DfJdbcFacade;
import org.seasar.dbflute.logic.jdbc.metadata.info.DfSequenceMeta;

/**
 * @author jflute
 * @since 0.9.6.4 (2010/01/16 Saturday)
 */
public class DfSequenceExtractorH2 extends DfSequenceExtractorBase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Log _log = LogFactory.getLog(DfSequenceExtractorH2.class);

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfSequenceExtractorH2(DataSource dataSource, List<UnifiedSchema> unifiedSchemaList) {
        super(dataSource, unifiedSchemaList);
    }

    // ===================================================================================
    //                                                                        Sequence Map
    //                                                                        ============
    protected Map<String, DfSequenceMeta> doGetSequenceMap() {
        _log.info("...Loading sequence informations");
        final Map<String, DfSequenceMeta> resultMap = StringKeyMap.createAsFlexibleOrdered();
        final DfJdbcFacade facade = new DfJdbcFacade(_dataSource);
        final String sql = buildMetaSelectSql();
        _log.info(sql);
        final List<String> columnList = new ArrayList<String>();
        columnList.add("SEQUENCE_CATALOG");
        columnList.add("SEQUENCE_SCHEMA");
        columnList.add("SEQUENCE_NAME");
        // no column on H2 (why?)
        //columnList.add("MINIMUM_VALUE");
        //columnList.add("MAXIMUM_VALUE");
        columnList.add("INCREMENT");
        final List<Map<String, String>> resultList = facade.selectStringList(sql, columnList);
        final StringBuilder logSb = new StringBuilder();
        logSb.append(ln()).append("[SEQUENCE]");
        for (Map<String, String> recordMap : resultList) {
            final DfSequenceMeta info = new DfSequenceMeta();
            final String sequenceCatalog = recordMap.get("SEQUENCE_CATALOG");
            info.setSequenceCatalog(sequenceCatalog);
            final String sequenceSchema = recordMap.get("SEQUENCE_SCHEMA");
            info.setSequenceSchema(sequenceSchema);
            final String sequenceName = recordMap.get("SEQUENCE_NAME");
            info.setSequenceName(sequenceName);
            //final String minimumValue = recordMap.get("MINIMUM_VALUE");
            //info.setMinimumValue(minimumValue != null ? new BigDecimal(minimumValue) : null);
            //final String maximumValue = recordMap.get("MAXIMUM_VALUE");
            //nfo.setMaximumValue(maximumValue != null ? new BigDecimal(maximumValue) : null);
            final String incrementSize = recordMap.get("INCREMENT");
            info.setIncrementSize(incrementSize != null ? Integer.valueOf(incrementSize) : null);
            final String key = buildSequenceMapKey(sequenceCatalog, sequenceSchema, sequenceName);
            resultMap.put(key, info);
            logSb.append(ln()).append(" ").append(key).append(" = ").append(info.toString());
        }
        _log.info(logSb.toString());
        return resultMap;
    }

    protected String buildMetaSelectSql() {
        final String schemaCondition;
        if (!_unifiedSchemaList.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (UnifiedSchema unifiedSchema : _unifiedSchemaList) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append("'").append(unifiedSchema.getPureSchema()).append("'");
            }
            schemaCondition = sb.toString();
        } else {
            schemaCondition = "'PUBLIC'";
        }
        // it allowed to exist unused sequences so it does not use catalog condition
        return "select * from INFORMATION_SCHEMA.SEQUENCES where SEQUENCE_SCHEMA in (" + schemaCondition + ")";
    }
}