/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.io.File;
import java.util.List;

import org.seasar.dbflute.helper.dataset.DfDataSet;

/**
 * The handler of xls data. (reading, writing)
 * @author jflute
 */
public interface DfXlsDataHandler {

    List<DfDataSet> readSeveralData(DfXlsDataResource resource);

    /**
     * @param resource The resource of xls data handling. (NotNull)
     * @param loadedDataInfo The info of loaded data for history. (NotNull)
     */
    void writeSeveralData(DfXlsDataResource resource, DfLoadedDataInfo loadedDataInfo);

    List<File> getXlsList(DfXlsDataResource resource);
}
