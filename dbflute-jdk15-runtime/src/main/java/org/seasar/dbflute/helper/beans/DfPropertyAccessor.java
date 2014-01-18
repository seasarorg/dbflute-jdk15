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
package org.seasar.dbflute.helper.beans;

import org.seasar.dbflute.helper.beans.exception.DfBeanIllegalPropertyException;

/**
 * @author jflute
 */
public interface DfPropertyAccessor {

    String getPropertyName();

    Class<?> getPropertyType();

    /**
     * @param target The target instance. (NullAllowed)
     * @return The value of the property. (NullAllowed)
     * @throws DfBeanIllegalPropertyException When the property of bean is illegal. (basically has a cause)
     */
    Object getValue(Object target);

    /**
     * @param target The target instance. (NullAllowed)
     * @param value The value of the property. (NullAllowed)
     * @throws DfBeanIllegalPropertyException When the property of bean is illegal. (basically has a cause)
     */
    void setValue(Object target, Object value);

    boolean isReadable();

    boolean isWritable();
}
