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
package org.seasar.dbflute.properties.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.seasar.dbflute.infra.dfprop.DfPropFile;

/**
 * @author jflute
 * @since 0.5.4 (2007/07/18)
 */
public class DfStringFileReader {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final DfPropFile _dfpropFile = new DfPropFile();

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====
    public String readString(String path) {
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(new File(path));
            return _dfpropFile.readString(ins);
        } catch (FileNotFoundException e) {
            return "";
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}