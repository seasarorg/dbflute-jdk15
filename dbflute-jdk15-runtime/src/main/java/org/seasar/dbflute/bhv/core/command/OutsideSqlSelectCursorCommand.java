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
package org.seasar.dbflute.bhv.core.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.dbflute.jdbc.CursorHandler;
import org.seasar.dbflute.outsidesql.OutsideSqlContext;
import org.seasar.dbflute.s2dao.jdbc.TnResultSetHandler;

/**
 * The behavior command for OutsideSql.selectList().
 * @author jflute
 */
public class OutsideSqlSelectCursorCommand extends AbstractOutsideSqlSelectCommand<Object> {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** The handler of cursor. (Required) */
    protected CursorHandler _cursorHandler;

    // ===================================================================================
    //                                                                   Basic Information
    //                                                                   =================
    public String getCommandName() {
        return "selectCursor";
    }

    public Class<?> getCommandReturnType() {
        return Object.class;
    }

    // ===================================================================================
    //                                                                  Detail Information
    //                                                                  ==================
    @Override
    public boolean isSelectCursor() {
        return true;
    }

    // ===================================================================================
    //                                                                    Process Callback
    //                                                                    ================
    @Override
    protected void setupOutsideSqlContextProperty(OutsideSqlContext outsideSqlContext) {
        super.setupOutsideSqlContextProperty(outsideSqlContext);
        outsideSqlContext.setCursorHandler(_cursorHandler);
    }

    // ===================================================================================
    //                                                                     Extension Point
    //                                                                     ===============
    @Override
    protected TnResultSetHandler createOutsideSqlSelectResultSetHandler() {
        return new TnResultSetHandler() {
            public Object handle(ResultSet rs) throws SQLException {
                if (!OutsideSqlContext.isExistOutsideSqlContextOnThread()) {
                    String msg = "The context of outside SQL should be required here!";
                    throw new IllegalStateException(msg);
                }
                OutsideSqlContext context = OutsideSqlContext.getOutsideSqlContextOnThread();
                CursorHandler cursorHandler = context.getCursorHandler();
                return cursorHandler.handle(rs);
            }
        };
    }

    @Override
    protected Class<?> getResultType() {
        return _cursorHandler.getClass();
    }

    // ===================================================================================
    //                                                                       Assert Helper
    //                                                                       =============
    @Override
    protected void assertStatus(String methodName) {
        assertBasicProperty(methodName);
        assertComponentProperty(methodName);
        assertOutsideSqlBasic(methodName);
        if (_cursorHandler == null) {
            throw new IllegalStateException(buildAssertMessage("_cursorHandler", methodName));
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setCursorHandler(CursorHandler cursorHandler) {
        _cursorHandler = cursorHandler;
    }
}
