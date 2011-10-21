/*
 * Copyright 2007-2011 the original author or authors.
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

package griffon.plugins.actions;

import griffon.core.GriffonController;
import griffon.core.GriffonControllerClass;
import griffon.swing.SwingAction;
import griffon.plugins.i18n.MessageSourceHolder;
import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;

import javax.swing.*;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;

import static griffon.util.GriffonNameUtils.*;

/**
 * @author Andres Almiray
 */
public final class ActionManager {
    private static final Logger LOG = LoggerFactory.getLogger(ActionManager.class);
    public static final String ACTION = "Action";
    private static ActionManager INSTANCE;
    private Map<GriffonController, Map<String, Action>> actionCache = new LinkedHashMap<GriffonController, Map<String, Action>>();

    static {
        INSTANCE = new ActionManager();
    }

    public static ActionManager getInstance() {
        return INSTANCE;
    }

    private ActionManager() { }
  
    public Map<String, Action> actionsFor(GriffonController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller parameter is null!");
        } 
        Map<String, Action> actions = actionCache.get(controller);
        if(actions == null) {
            actions = Collections.emptyMap();                
            if (LOG.isTraceEnabled()) {
                LOG.trace("No actions defined for controller "+controller);
            }
        }
        return actions;      
    }

    public Action actionFor(GriffonController controller, String actionName) {
        if (controller == null) {
            throw new IllegalArgumentException("controller parameter is null!");
        }
        if (isBlank(actionName)) {
            throw new IllegalArgumentException("actionName parameter is null!");
        }

        return actionCache.get(controller).get(normalizeName(actionName));
    }

    public void createActions(GriffonController controller) {
        GriffonControllerClass griffonClass = (GriffonControllerClass) controller.getGriffonClass();
        for (String actionName : griffonClass.getActionNames()) {
            Action action = createAction(controller, actionName);
            Map<String, Action> actions = actionCache.get(controller);
            if (actions == null) {
                actions = new LinkedHashMap<String, Action>();
                actionCache.put(controller, actions);
            }
            String actionKey = normalizeName(actionName);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Action for " + controller.getClass().getName() + "." + actionName + " stored as " + actionKey);
            }
            actions.put(actionKey, action);
        }
    }

    public String normalizeName(String actionName) {
        if (actionName.endsWith(ACTION)) {
            actionName = actionName.substring(0, actionName.length() - ACTION.length());
        }
        return uncapitalize(actionName);
    }

    private Action createAction(GriffonController controller, String actionName) {
        SwingAction.ActionBuilder builder = SwingAction.action();

        String normalizeNamed = capitalize(normalizeName(actionName));
        String prefix = controller.getClass().getName() + ".action.";

        String rsActionName = msg(prefix, normalizeNamed, "name", getNaturalName(normalizeNamed));
        if (!isBlank(rsActionName)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".name = " + rsActionName);
            }
            builder.withName(rsActionName);
        }

        String rsAccelerator = msg(prefix, normalizeNamed, "accelerator", "");
        if (!isBlank(rsAccelerator)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".accelerator = " + rsAccelerator);
            }
            builder.withAccelerator(rsAccelerator);
        }

        String rsCommand = msg(prefix, normalizeNamed, "command", "");
        if (!isBlank(rsCommand)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".command = " + rsCommand);
            }
            builder.withCommand(rsCommand);
        }

        String rsShortDescription = msg(prefix, normalizeNamed, "short_description", "");
        if (!isBlank(rsShortDescription)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".short_description = " + rsShortDescription);
            }
            builder.withShortDescription(rsShortDescription);
        }

        String rsLongDescription = msg(prefix, normalizeNamed, "long_description", "");
        if (!isBlank(rsLongDescription)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".long_description = " + rsLongDescription);
            }
            builder.withLongDescription(rsLongDescription);
        }

        String rsMnemonic = msg(prefix, normalizeNamed, "mnemonic", "");
        if (!isBlank(rsMnemonic)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".mnemonic = " + rsMnemonic);
            }
            builder.withMnemonic(rsMnemonic);
        }

        String rsSmallIcon = msg(prefix, normalizeNamed, "small_icon", "");
        if (!isBlank(rsSmallIcon)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".small_icon = " + rsSmallIcon);
            }
            builder.withSmallIcon(new ImageIcon(rsSmallIcon));
        }

        String rsLargeIcon = msg(prefix, normalizeNamed, "large_icon", "");
        if (!isBlank(rsLargeIcon)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".large_icon = " + rsLargeIcon);
            }
            builder.withLargeIcon(new ImageIcon(rsLargeIcon));
        }

        String rsEnabled = msg(prefix, normalizeNamed, "enabled", "true");
        if (!isBlank(rsEnabled)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(prefix + normalizeNamed + ".enabled = " + rsEnabled);
            }
            builder.withEnabled(DefaultTypeTransformation.castToBoolean(rsEnabled));
        }

        builder.withClosure(new MethodClosure(controller, actionName));

        return builder.build();
    }

    private static String msg(String key, String actionName, String subkey, String defaultValue) {
        try {
            return MessageSourceHolder.getMessageSource().getMessage(key + actionName + "." + subkey);
        } catch (NoSuchMessageException nsme) {
            return MessageSourceHolder.getMessageSource().getMessage("application.action." + actionName + "." + subkey, defaultValue);
        }
    }
}
