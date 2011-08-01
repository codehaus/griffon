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
import bibliothek.gui.dock.common.CGridArea
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.intern.CDockable
import java.awt.Component
import groovy.swing.factory.LayoutFactory
import bibliothek.gui.dock.common.intern.DefaultCDockable

/**
 * @author Alexander Klein
 */
class CDockableFactory extends CStationFactory {
    CDockableFactory() {
        super(CDockable)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        CDockable instance = super.newInstance(builder, name, value, attributes)
        def control = builder.context[CURRENT_CONTROL]
        control.addDockable(instance)
        return instance
    }

    @Override
    protected Object createInstance(CControl control, String id, Map attributes) {
        if (!id)
            id = UUID.randomUUID().toString()
        return new DefaultSingleCDockable(id)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof CGridArea)
            child = child.station
        if (child instanceof Component)
            parent.add(child, builder.context?.constraints)
        if (builder.currentFactory instanceof LayoutFactory && parent instanceof DefaultCDockable)
            parent.contentPane.layout = child
        builder.context.remove('constraints')
    }
}
