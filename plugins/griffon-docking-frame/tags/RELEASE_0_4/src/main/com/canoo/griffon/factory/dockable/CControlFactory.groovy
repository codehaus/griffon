/*
 * Copyright 2009-2011 the original author or authors.
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
 * limitations under the License.
 */
package com.canoo.griffon.factory.dockable

import bibliothek.gui.dock.common.CControl
import groovy.swing.factory.BeanFactory
import bibliothek.gui.dock.common.CPreferenceModel
import bibliothek.gui.dock.common.group.CGroupBehavior

/**
 * @author Alexander Klein
 */
class CControlFactory extends BeanFactory {
    public static final String CCONTROL = '_CCONTROL_'
    def addon

    CControlFactory(def addon) {
        super(CControl, false)
        this.addon = addon
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (!(value instanceof CControl)) {
            value = new CControl(addon.provider)
        }
        if(attributes.groupBehaviour instanceof String)
            attributes.groupBehaviour = CGroupBehavior.getField(attributes.groupBehaviour.toUpperCase()).get(null)
        def enablePreferences = attributes.remove('enablePreferences')
        CControl control = super.newInstance(builder, name, value, attributes)
        builder.context[CCONTROL] = control
        if (enablePreferences)
            control.preferenceModel = new CPreferenceModel(control)
        return control
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (builder.childBuilder.currentFactory instanceof MultipleDockableFactoryFactory)
            parent.addMultipleDockableFactory(builder.context.id, child)

        if (builder.childBuilder.currentFactory instanceof GriffonSingleDockableFactoryFactory)
            parent.addSingleDockableFactory(builder.context.filter, child)

        if (builder.childBuilder.currentFactory instanceof SingleDockableFactoryFactory)
            parent.addSingleDockableFactory(builder.context.filter, child)

        def parentFactory = builder.parentContext.get(FactoryBuilderSupport.PARENT_FACTORY)
        if (parentFactory)
            parentFactory.setChild(builder, builder.getParentNode(), child)
    }
}
