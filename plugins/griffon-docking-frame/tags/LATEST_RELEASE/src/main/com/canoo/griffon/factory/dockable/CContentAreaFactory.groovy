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

import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.mode.ExtendedMode
import java.awt.Component
import bibliothek.gui.dock.common.*
import bibliothek.gui.dock.common.perspective.*

/**
 * @author Alexander Klein
 */
class CContentAreaFactory extends CStationFactory {
    boolean defaultArea
    CControlPerspective perspectives
    CPerspective perspective
    boolean childrenSet = false

    public CContentAreaFactory(boolean defaultArea = false) {
        super(CContentArea)
        this.defaultArea = defaultArea
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        CContentArea area = super.newInstance(builder, name, value, attributes)
        builder.context.perspectives = area.control.getPerspectives()
        builder.context.perspectiveName = attributes.perspective ?: 'default'
        builder.context.perspective = builder.context.perspectives.createEmptyPerspective()
        return area
    }

    @Override
    protected Object createInstance(CControl control, String id, Map attributes) {
        if (defaultArea)
            return control.getContentArea()
        return control.createContentArea(id ?: UUID.randomUUID().toString())
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        CControl control = parent.control
        CContentPerspective contentPerspective = builder.context.contentPerspective ?: builder.parentContext.perspective.getContentArea(parent.uniqueId)
        builder.context.contentPerspective = contentPerspective

        def constraints = builder.context.constraints
        def dock = builder.context.dock ?: []
        if (dock instanceof GString) dock = dock.toString()
        def dockMinimized = builder.context.dockMinimized
        def dockExternalized = builder.context.dockExternalized
        def dockState = builder.context.dockState ?: ExtendedMode.NORMALIZED

        def dockable = child
        String id = (constraints instanceof String) ? constraints : constraints?.id
        if (!id && (child instanceof CStation || child instanceof CContentArea || child instanceof CMinimizeArea))
            id = child.uniqueId
        if (child instanceof CGridArea)
            child = child.station
        if (child instanceof Component) {
            if (!id) id = child.name ?: UUID.randomUUID().toString()
            dockable = control.getSingleDockable(id)
            if (!dockable) {
                dockable = new DefaultSingleCDockable(id, child)
                control.addDockable(dockable)
            }
        }
        if (dockable instanceof CDockable || dockable instanceof CDockablePerspective) {
            CDockablePerspective perspective
            if (dockable instanceof CDockable)
                perspective = createPerspective(dockable)
            else
                perspective = dockable
            if (dockState instanceof String)
                dockState = ExtendedMode.getField(dockState.toUpperCase()).get(null)
            if (dockState != ExtendedMode.MINIMIZED)
                setMinimized(dockMinimized, perspective, builder.context)
            if (dockState != ExtendedMode.EXTERNALIZED)
                setExternalized(dockExternalized, perspective, builder.context)
            if (dockState != ExtendedMode.NORMALIZED)
                setNormalized(dock, perspective, builder.context)
            switch (dockState) {
                case ExtendedMode.NORMALIZED:
                    setNormalized(dock, perspective, builder.context)
                    break
                case ExtendedMode.MINIMIZED:
                    setMinimized(dockMinimized, perspective, builder.context)
                    break
                case ExtendedMode.EXTERNALIZED:
                    setExternalized(dockExternalized, perspective, builder.context)
                    break
            }
        }
        builder.context.remove('constraints')
    }

    CDockablePerspective createPerspective(CDockable dockable) {
        if (dockable instanceof SingleCDockable) {
            return new SingleCDockablePerspective(dockable.uniqueId)
        } else if (dockable instanceof MultipleCDockable) {
            def layout = dockable.factory.create()
            String id = UUID.randomUUID().toString()
            return new MultipleCDockablePerspective(dockable.control.getFactoryId(dockable.factory), id, layout)
        }
    }

    private final static List defaultLocation = [0.0, 0.0, 1.0, 1.0]

    private void setNormalized(def dock, CDockablePerspective perspective, Map context) {
        CContentPerspective contentPerspective = context.contentPerspective
        if (dock instanceof CGridArea) {
            dock.createPerspective().add(perspective)
        } else if (dock instanceof CContentArea) {
            dock.centerArea.createPerspective().add(perspective)
        } else if (dock instanceof Collection) {
            def size = dock.size()
            for (int i = 0; i < 4 - size; i++)
                dock[i + size] = defaultLocation[i + size]
            def data = (dock as double[]) as List
            contentPerspective.center.gridAdd(* (data + perspective))
        } else
            return
        contentPerspective.perspective.storeLocations()
    }

    private void setMinimized(def dockMinimized, CDockablePerspective perspective, Map context) {
        CContentPerspective contentPerspective = context.contentPerspective
        if (dockMinimized instanceof CMinimizeArea) {
            dockMinimized.createPerspective().add(perspective)
        } else if (dockMinimized instanceof String) {
            try {
                def m = dockMinimized.toLowerCase() =~ /\s*(north|south|east|west)\s*(\d*)\s*/
                if (m.matches()) {
                    contentPerspective[m[0][1].toLowerCase()].add(perspective)
                } else
                    throw new IllegalArgumentException("Cannot dock to ${dockMinimized}Area - north, east, south and west are valid")
            } catch (e) {
                throw new IllegalArgumentException("Cannot dock to ${dockMinimized}Area - north, east, south and west are valid", e)
            }
        } else
            return
        contentPerspective.perspective.storeLocations()
    }

    private void setExternalized(def dockExternalized, CDockablePerspective perspective, Map context) {
        CContentPerspective contentPerspective = context.contentPerspective
        if (dockExternalized instanceof Collection) {
            if (dockExternalized.size() < 4)
                throw new IllegalArgumentException('dockExternal has to be in the form [x, y, width, height, <fullscreen>]')
            def data = dockExternalized
            if (data.size() < 5)
                data = data + false
            CExternalizePerspective root = contentPerspective.perspective.getRoot('external')
            data = [perspective] + data
            root.add(* data)
        } else
            return
        contentPerspective.perspective.storeLocations()
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        builder.context.perspective.shrink()
        builder.context.perspectives.setPerspective(builder.context.perspectiveName, builder.context.perspective)
        node.control.load(builder.context.perspectiveName)
    }
}
