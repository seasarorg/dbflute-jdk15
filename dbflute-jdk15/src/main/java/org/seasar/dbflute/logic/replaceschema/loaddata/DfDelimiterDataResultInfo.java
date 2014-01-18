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
package org.seasar.dbflute.logic.replaceschema.loaddata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jflute
 */
public class DfDelimiterDataResultInfo {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final Map<String, Set<String>> _notFoundColumnMap = new LinkedHashMap<String, Set<String>>();
    protected final Map<String, List<String>> _warinngFileMap = new LinkedHashMap<String, List<String>>();

    // ===================================================================================
    //                                                                         Easy-to-Use
    //                                                                         ===========
    public void registerWarningFile(String fileName, String message) {
        List<String> messageList = _warinngFileMap.get(fileName);
        if (messageList == null) {
            messageList = new ArrayList<String>();
            _warinngFileMap.put(fileName, messageList);
        }
        messageList.add(message);
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Map<String, Set<String>> getNotFoundColumnMap() {
        return _notFoundColumnMap;
    }

    public Map<String, List<String>> getWarningFileMap() {
        return _warinngFileMap;
    }
}
