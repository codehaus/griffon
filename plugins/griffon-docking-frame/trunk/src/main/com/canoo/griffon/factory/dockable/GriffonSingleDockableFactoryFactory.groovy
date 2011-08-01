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

import bibliothek.gui.dock.common.SingleCDockable
import bibliothek.gui.dock.common.SingleCDockableFactory
import bibliothek.util.Filter
import bibliothek.util.filter.PresetFilter
import bibliothek.gui.dock.common.CControl
import griffon.core.GriffonApplication
import java.awt.Component
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.lang.ref.SoftReference

/**
 * @author Alexander Klein
 */
class GriffonSingleDockableFactoryFactory extends AbstractFactory {
    GriffonApplication app

    GriffonSingleDockableFactoryFactory(GriffonApplication app) {
        super()
        this.app = app
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        Filter filter = attributes.remove('filter')
        if (filter instanceof Filter) {
        } else if (filter instanceof String) {
        } else if (filter instanceof String[])
            filter = new PresetFilter(filter)
        else if (filter instanceof Collection)
            filter = new PresetFilter(filter.collect { it.toString() })
        else
            filter = new PresetFilter(app.mvcGroups.keySet())
        return new MVCGroupSingleDockableFactory(app, filter)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof CControl) {
            parent.addSingleDockableFactory(child.filter, child)
        }
    }

    @Override
    boolean isLeaf() {
        return true
    }
}
class MVCGroupSingleDockableFactory implements SingleCDockableFactory {
    WeakHashMap<String, SoftReference<SingleCDockable>> cache = [:] as WeakHashMap
    GriffonApplication app
    def filter

    MVCGroupSingleDockableFactory(GriffonApplication app, def filter) {
        this.filter = filter
        this.app = app
    }

    SingleCDockable createBackup(String id) {
        def dockable = cache.get(id)?.get()
        if (dockable)
            return dockable
        def view = app.views[id]
        if (!view) {
            app.buildMVCGroup(id)
            view = app.views[id]
        }
        if (view instanceof Component) {
            dockable = new DefaultSingleCDockable("$id", view)
            cache[id] = new SoftReference<SingleCDockable>(dockable)
            return dockable
        } else {
            throw new IllegalArgumentException('View of MVCGroup $id does not return a Component')
        }
    }
}



