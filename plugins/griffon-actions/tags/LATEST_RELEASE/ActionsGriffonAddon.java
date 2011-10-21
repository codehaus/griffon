/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

import java.util.Map;
import java.util.LinkedHashMap;
import javax.swing.Action;
import griffon.core.GriffonApplication;
import griffon.core.GriffonController;
import griffon.core.GriffonControllerClass;
import griffon.core.MVCGroup;
import griffon.core.MVCGroupConfiguration;
import griffon.util.ApplicationHolder;
import griffon.util.RunnableWithArgs;
import griffon.util.RunnableWithArgsClosure;
import griffon.plugins.actions.ActionManager;
import groovy.lang.Closure;
import groovy.util.FactoryBuilderSupport;
import org.codehaus.griffon.runtime.core.AbstractGriffonAddon;

/**
 * @author Andres Almiray
 */
public class ActionsGriffonAddon extends AbstractGriffonAddon {
    public ActionsGriffonAddon() {
        super(ApplicationHolder.getApplication());
    }

    public Map<String, Closure> getEvents() {
        Map<String, Closure> events = new LinkedHashMap<String, Closure>();
        
        events.put(GriffonApplication.Event.NEW_INSTANCE.getName(), new RunnableWithArgsClosure(new RunnableWithArgs() {
            public void run(Object[] args) {
                String type = (String) args[1];
                if (GriffonControllerClass.TYPE.equals(type)) {
                    GriffonController controller = (GriffonController) args[2];
                    ActionManager.getInstance().createActions(controller);
                }
            }
        }));

        events.put(GriffonApplication.Event.INITIALIZE_MVC_GROUP.getName(), new RunnableWithArgsClosure(new RunnableWithArgs() {
            public void run(Object[] args) {
                MVCGroupConfiguration groupConfig = (MVCGroupConfiguration) args[0];
                MVCGroup group = (MVCGroup) args[1];
                GriffonController controller = group.getController();
                if (controller == null) return;
                FactoryBuilderSupport builder = group.getBuilder();
                Map<String, Action> actions = ActionManager.getInstance().actionsFor(controller);
                for (Map.Entry<String, Action> action : actions.entrySet()) {
                    String actionKey = ActionManager.getInstance().normalizeName(action.getKey()) + ActionManager.ACTION;
                    if (getLog().isTraceEnabled()) {
                        getLog().trace("Adding action " + actionKey + " to " + groupConfig.getMvcType() + ":" + group.getMvcId() + ":builder");
                    }
                    builder.setVariable(actionKey, action.getValue());
                }
            }
        }));
        
        return events;
    }
}
