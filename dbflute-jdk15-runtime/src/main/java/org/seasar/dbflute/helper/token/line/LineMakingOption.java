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
package org.seasar.dbflute.helper.token.line;

/**
 * @author jflute
 */
public class LineMakingOption {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String _delimiter;

    protected boolean _quoteAll;

    protected boolean _quoteMinimally;

    protected boolean _trimSpace;

    // ===================================================================================
    //                                                                         Easy-to-Use
    //                                                                         ===========
    public LineMakingOption delimitateByComma() {
        _delimiter = ",";
        return this;
    }

    public LineMakingOption delimitateByTab() {
        _delimiter = "\t";
        return this;
    }

    public LineMakingOption quoteAll() {
        _quoteAll = true;
        _quoteMinimally = false;
        return this;
    }

    public LineMakingOption quoteMinimally() {
        _quoteMinimally = true;
        _quoteAll = false;
        return this;
    }

    public LineMakingOption trimSpace() {
        _trimSpace = true;
        return this;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDelimiter() {
        return _delimiter;
    }

    public void setDelimiter(String delimiter) {
        _delimiter = delimiter;
    }

    public boolean isQuoteAll() {
        return _quoteAll;
    }

    public boolean isQuoteMinimally() {
        return _quoteMinimally;
    }

    public boolean isTrimSpace() {
        return _trimSpace;
    }
}