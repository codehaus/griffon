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

package com.canoo.griffon.factory.dockable;


import bibliothek.gui.Dockable
import bibliothek.gui.dock.common.CContentArea
import bibliothek.gui.dock.common.CMinimizeArea
import groovy.swing.factory.BeanFactory
import java.awt.Component
import java.awt.Window
import java.lang.ref.WeakReference
import javax.swing.SwingUtilities
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.util.ComponentWindowProvider
import bibliothek.gui.dock.util.DirectWindowProvider
import bibliothek.gui.dock.common.CStation
import bibliothek.gui.dock.common.MultipleCDockable
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.CGridArea

/**
 * @author Alexander Klein
 */
abstract class CStationFactory extends BeanFactory {
    Class type
    public static final String CURRENT_CONTROL = '_CURRENT_CONTROL_'

    public CStationFactory(Class type, boolean leaf = false) {
        super(type, leaf)
        this.type = type
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof GString)
            value = value.toString()
        if (!(type.isAssignableFrom(value.getClass()))) {
            def id
            if (value instanceof String)
                id = value
            else
                id = attributes.id
            def control = attributes.remove('control') ?: findParentControl(builder.context)
            if (!control)
                throw new IllegalArgumentException('CControl missing')
            builder.context[CURRENT_CONTROL] = control
            value = createInstance(control, id, attributes)
        }
        return super.newInstance(builder, name, value, attributes)
    }

    public static CControl findParentControl(Map context) {
        def value = context[CControlFactory.CCONTROL]
        if(value instanceof CControl)
            return value
        else {
            def parentContext = context[FactoryBuilderSupport.PARENT_CONTEXT]
            if(!parentContext)
                return null
            return findParentControl(parentContext)
        }
    }

    protected abstract Object createInstance(CControl control, String id, Map attributes)
}
