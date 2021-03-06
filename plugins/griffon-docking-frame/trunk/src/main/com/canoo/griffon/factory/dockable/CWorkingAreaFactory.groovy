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

import bibliothek.gui.dock.common.CContentArea
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CMinimizeArea
import bibliothek.gui.dock.common.CWorkingArea
import bibliothek.gui.dock.common.intern.CDockable
import java.awt.Container

/**
 * @author Alexander Klein
 */
class CWorkingAreaFactory extends CStationFactory {

    public CWorkingAreaFactory() {
        super(CWorkingArea)
    }

    @Override
    protected Object createInstance(CControl control, String id, Map attributes) {
        if (!id)
            throw new IllegalArgumentException("CWorkingArea needs a unique id")
        return control.createWorkingArea(id)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        def constraints = builder.context.constraints
        if (child instanceof CDockable) {
            parent.control.addDockable(child)
        } else {
            throw new IllegalArgumentException('Only CDockables can be added to a workingArea ')
        }
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof Container && !(parent instanceof CContentArea)  && !(parent instanceof CMinimizeArea))
            parent.add(child.station)
    }
}
