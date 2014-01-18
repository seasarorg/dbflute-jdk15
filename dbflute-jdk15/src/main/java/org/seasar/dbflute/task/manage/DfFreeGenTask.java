/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.dbflute.task.manage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.seasar.dbflute.properties.DfFreeGenProperties;
import org.seasar.dbflute.properties.assistant.freegen.DfFreeGenRequest;
import org.seasar.dbflute.task.DfDBFluteTaskStatus;
import org.seasar.dbflute.task.DfDBFluteTaskStatus.TaskType;
import org.seasar.dbflute.task.bs.DfAbstractTexenTask;
import org.seasar.dbflute.util.Srl;

/**
 * @author jflute
 * @since 0.9.9.4C (2012/10/06 Thursday)
 */
public class DfFreeGenTask extends DfAbstractTexenTask {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Log _log = LogFactory.getLog(DfFreeGenTask.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final List<DfFreeGenRequest> _freeGenRequestList = new ArrayList<DfFreeGenRequest>();
    protected String _genTarget;

    // ===================================================================================
    //                                                                           Beginning
    //                                                                           =========
    @Override
    protected void begin() {
        _log.info("+------------------------------------------+");
        _log.info("|                                          |");
        _log.info("|                 Free Gen                 |");
        _log.info("|                                          |");
        _log.info("+------------------------------------------+");
        DfDBFluteTaskStatus.getInstance().setTaskType(TaskType.FreeGen);
    }

    // ===================================================================================
    //                                                                          DataSource
    //                                                                          ==========
    @Override
    protected boolean isUseDataSource() {
        return true;
    }

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Override
    protected void doExecute() {
        setupControlTemplate();
        final DfFreeGenProperties prop = getFreeGenProperties();
        final List<DfFreeGenRequest> requestList = prop.getFreeGenRequestList();
        for (DfFreeGenRequest request : requestList) {
            final String requestName = request.getRequestName();
            if (_genTarget != null && !_genTarget.equalsIgnoreCase(requestName)) {
                continue;
            }
            _freeGenRequestList.add(request);
        }
        fireVelocityProcess();
        refreshResources();
    }

    protected void setupControlTemplate() {
        final String freegenSpace = "./freegen";
        try {
            templatePath = new File(freegenSpace).getCanonicalPath();
        } catch (IOException e) {
            String msg = "Failed to set template path: " + freegenSpace;
            throw new IllegalStateException(msg, e);
        }
        _log.info("");
        _log.info("* * * * * * * * * *");
        _log.info("* Process FreeGen *");
        _log.info("* * * * * * * * * *");
        final String control = "ControlFreeGen.vm";
        _log.info("...Using control: " + control);
        setControlTemplate(control);
    }

    // ===================================================================================
    //                                                                  Prepare Generation
    //                                                                  ==================
    @Override
    public Context initControlContext() throws Exception {
        _log.info("");
        _log.info("...Preparing generation of free generate");
        for (DfFreeGenRequest request : _freeGenRequestList) {
            _log.info("  " + request.toString());
        }
        final VelocityContext context = createVelocityContext();
        return context;
    }

    protected VelocityContext createVelocityContext() {
        final VelocityContext context = new VelocityContext();
        context.put("manager", getFreeGenProperties().getFreeGenManager());
        context.put("requestList", _freeGenRequestList);
        return context;
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected DfFreeGenProperties getFreeGenProperties() {
        return getProperties().getFreeGenProperties();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setGenTarget(String genTarget) {
        if (Srl.is_Null_or_TrimmedEmpty(genTarget)) {
            return;
        }
        if (genTarget.equals("${gentgt}")) {
            return;
        }
        _genTarget = genTarget;
    }
}
