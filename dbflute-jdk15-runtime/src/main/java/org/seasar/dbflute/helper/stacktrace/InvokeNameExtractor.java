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
package org.seasar.dbflute.helper.stacktrace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jflute
 */
public class InvokeNameExtractor {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final StackTraceElement[] _stackTrace;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public InvokeNameExtractor(StackTraceElement[] stackTrace) {
        if (stackTrace == null) {
            String msg = "The argument 'stackTrace' should not be null.";
            throw new IllegalStateException(msg);
        }
        _stackTrace = stackTrace;
    }

    // ===================================================================================
    //                                                                             Extract
    //                                                                             =======
    /**
     * @param resource the call-back resource for invoke-name-extracting. (NotNull)
     * @return Invoke name. (NotNull: If not found, returns empty string.)
     */
    public List<InvokeNameResult> extractInvokeName(InvokeNameExtractingResource resource) {
        final List<InvokeNameResult> resultList = new ArrayList<InvokeNameResult>();
        String simpleClassName = null;
        String methodName = null;
        int lineNumber = 0;
        int foundIndex = -1; // The minus one means 'not found'.
        int foundFirstIndex = -1; // The minus one means 'not found'.
        boolean onTarget = false;
        boolean existsDuplicate = false;
        for (int i = resource.getStartIndex(); i < _stackTrace.length; i++) {
            final StackTraceElement element = _stackTrace[i];
            if (i > resource.getStartIndex() + resource.getLoopSize()) {
                break;
            }
            final String currentClassName = element.getClassName();
            if (currentClassName.startsWith("sun.") || currentClassName.startsWith("java.")) {
                if (onTarget) {
                    break;
                }
                continue;
            }
            final String currentMethodName = element.getMethodName();
            if (resource.isTargetElement(currentClassName, currentMethodName)) {
                if (currentMethodName.equals("invoke")) {
                    continue;
                }
                simpleClassName = currentClassName.substring(currentClassName.lastIndexOf(".") + 1);
                simpleClassName = resource.filterSimpleClassName(simpleClassName);
                methodName = currentMethodName;
                if (resource.isUseAdditionalInfo()) {
                    lineNumber = element.getLineNumber();
                }
                foundIndex = i;
                if (foundFirstIndex == -1) {
                    foundFirstIndex = i;
                }
                onTarget = true;
                if (resultList.isEmpty()) { // first element
                    resultList.add(createResult(simpleClassName, methodName, lineNumber, foundIndex, foundFirstIndex));
                } else {
                    existsDuplicate = true;
                }
                continue;
            }
            if (onTarget) {
                break;
            }
        }
        if (simpleClassName == null) {
            return new ArrayList<InvokeNameResult>();
        }
        if (existsDuplicate) {
            resultList.add(createResult(simpleClassName, methodName, lineNumber, foundIndex, foundFirstIndex));
        }
        return resultList;
    }

    private InvokeNameResult createResult(String simpleClassName, String methodName, int lineNumber, int foundIndex,
            int foundFirstIndex) {
        final InvokeNameResult result = new InvokeNameResult();
        result.setSimpleClassName(simpleClassName);
        result.setMethodName(methodName);
        result.setLineNumber(lineNumber);
        result.setFoundIndex(foundIndex);
        result.setFoundFirstIndex(foundFirstIndex);
        return result;
    }
}