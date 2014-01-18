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
package org.seasar.dbflute.resource;

import java.util.HashMap;
import java.util.Map;

import org.seasar.dbflute.jdbc.SqlLogInfo;

/**
 * The context of internal map.
 * @author jflute
 */
public class InternalMapContext {

    // ===================================================================================
    //                                                                        Thread Local
    //                                                                        ============
    /** The thread-local for this. */
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    protected static void initialize() {
        if (threadLocal.get() != null) {
            return;
        }
        threadLocal.set(new HashMap<String, Object>());
    }

    public static Map<String, Object> getInternalMap() {
        initialize();
        return threadLocal.get();
    }

    /**
     * Get the value of the object by the key.
     * @param key The key of the object. (NotNull)
     * @return The value of the object. (NullAllowed)
     */
    public static Object getObject(String key) {
        initialize();
        return threadLocal.get().get(key);
    }

    /**
     * Set the value of the object.
     * @param key The key of the object. (NotNull)
     * @param value The value of the object. (NullAllowed)
     */
    public static void setObject(String key, Object value) {
        initialize();
        threadLocal.get().put(key, value);
    }

    /**
     * Is existing internal-map-context on thread?
     * @return The determination, true or false.
     */
    public static boolean isExistInternalMapContextOnThread() {
        return (threadLocal.get() != null);
    }

    /**
     * Clear internal-map-context on thread.
     */
    public static void clearInternalMapContextOnThread() {
        threadLocal.set(null);
    }

    // ===================================================================================
    //                                                                        Regular Item
    //                                                                        ============
    protected static final String KEY_BEHAVIOR_INVOKE_NAME = "df:BehaviorInvokeName";
    protected static final String KEY_CLIENT_INVOKE_NAME = "df:ClientInvokeName";
    protected static final String KEY_BYPASS_INVOKE_NAME = "df:ByPassInvokeName";
    protected static final String KEY_HOOK_SQL_LOG_INFO = "df:HookSqlLogInfo";
    protected static final String KEY_RESULT_SQL_LOG_INFO = "df:ResultSqlLogInfo";
    protected static final String KEY_SQL_BEFORE_TIME_MILLIS = "df:SqlBeforeTimeMillis";
    protected static final String KEY_SQL_AFTER_TIME_MILLIS = "df:SqlAfterTimeMillis";

    public static String getBehaviorInvokeName() {
        return (String) getObject(KEY_BEHAVIOR_INVOKE_NAME);
    }

    public static void setBehaviorInvokeName(String behaviorInvokeName) {
        setObject(KEY_BEHAVIOR_INVOKE_NAME, behaviorInvokeName);
    }

    public static String getClientInvokeName() {
        return (String) getObject(KEY_CLIENT_INVOKE_NAME);
    }

    public static void setClientInvokeName(String clientInvokeName) {
        setObject(KEY_CLIENT_INVOKE_NAME, clientInvokeName);
    }

    public static String getByPassInvokeName() {
        return (String) getObject(KEY_BYPASS_INVOKE_NAME);
    }

    public static void setByPassInvokeName(String byPassInvokeName) {
        setObject(KEY_BYPASS_INVOKE_NAME, byPassInvokeName);
    }

    public static SqlLogInfo getHookSqlLogInfo() {
        return (SqlLogInfo) getObject(KEY_HOOK_SQL_LOG_INFO);
    }

    public static void setHookSqlLogInfo(SqlLogInfo sqlLogInfo) {
        setObject(KEY_HOOK_SQL_LOG_INFO, sqlLogInfo);
    }

    public static SqlLogInfo getResultSqlLogInfo() {
        return (SqlLogInfo) getObject(KEY_RESULT_SQL_LOG_INFO);
    }

    public static void setResultSqlLogInfo(SqlLogInfo sqlLogInfo) {
        setObject(KEY_RESULT_SQL_LOG_INFO, sqlLogInfo);
    }

    public static Long getSqlBeforeTimeMillis() {
        return (Long) getObject(KEY_SQL_BEFORE_TIME_MILLIS);
    }

    public static void setSqlBeforeTimeMillis(Long sqlBeforeTimeMillis) {
        setObject(KEY_SQL_BEFORE_TIME_MILLIS, sqlBeforeTimeMillis);
    }

    public static Long getSqlAfterTimeMillis() {
        return (Long) getObject(KEY_SQL_AFTER_TIME_MILLIS);
    }

    public static void setSqlAfterTimeMillis(Long sqlAfterTimeMillis) {
        setObject(KEY_SQL_AFTER_TIME_MILLIS, sqlAfterTimeMillis);
    }
}
