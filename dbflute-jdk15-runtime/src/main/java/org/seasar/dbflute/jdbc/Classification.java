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
package org.seasar.dbflute.jdbc;

import java.util.Map;

/**
 * The basic interface of classification. <br />
 * It's an internal interface for DBFlute runtime.
 * @author jflute
 */
public interface Classification {

    /**
     * @return The code of the classification. (NotNull)
     */
    String code();

    /**
     * @return The name of the classification. (NotNull)
     */
    String name();

    /**
     * @return The code of the classification. (NullAllowed: when an alias is not specified in its setting)
     */
    String alias();

    /**
     * @return The map of sub-items. (NotNull, EmptyAllowed, ReadOnly)
     */
    Map<String, Object> subItemMap();

    /**
     * @return The meta of the classification. (NotNull)
     */
    ClassificationMeta meta();
}
