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

import griffon.core.GriffonApplication
import groovy.swing.factory.BeanFactory
import bibliothek.gui.dock.common.CMinimizeArea
import bibliothek.gui.dock.common.CControl
import java.awt.Component
import java.awt.BorderLayout
import bibliothek.gui.dock.common.SingleCDockable
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.FlapDockStation

/**
 * @author Alexander Klein
 */
class CMinimizeAreaFactory extends CStationFactory {

    public CMinimizeAreaFactory() {
        super(CMinimizeArea)
    }

    @Override
    protected Object createInstance(CControl control, String id, Map attributes) {
        def auto = false
        if (attributes.direction instanceof String) {
            if (attributes.direction == 'auto')
                auto = true
            else
                attributes.direction = FlapDockStation.Direction.getField(attributes.direction.toUpperCase()).get(null)
        }
        if (!id)
            throw new IllegalArgumentException("CGridArea needs a unique id")
        def result = control.createMinimizeArea(id)
        if (auto)
            result.setDirection(null)
        return result
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        def constraints = builder.context.constraints ?: BorderLayout.SOUTH
        if (child instanceof Component)
            parent.add(child, constraints)
        else if (child instanceof CDockable) {
            parent.control.addDockable(child)
        } else {
            throw new IllegalArgumentException('Only Components and CDockables can be added to a minimizeArea ')
        }
    }
}
