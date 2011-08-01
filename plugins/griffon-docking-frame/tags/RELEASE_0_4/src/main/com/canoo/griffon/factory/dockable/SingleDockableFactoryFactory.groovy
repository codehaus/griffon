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

/**
 * @author Alexander Klein
 */
class SingleDockableFactoryFactory extends AbstractFactory {
    public static final Filter noFilter = new NoFilter()

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        def filter = attributes.remove('filter')
        if (filter instanceof Filter) {
        } else if (filter instanceof String) {
        } else if (filter instanceof String[])
            filter = new PresetFilter(filter)
        else if (filter instanceof Collection)
            filter = new PresetFilter(* filter.collect { it.toString() })
        else
            filter = noFilter

        builder.context.filter = filter
        return value ?: new MapSingleDockableFactory(attributes.remove('data'))
    }

    @Override
    boolean isLeaf() {
        return true
    }
}

class MapSingleDockableFactory implements SingleCDockableFactory {
    Map data

    MapSingleDockableFactory(Map data = [:]) {
        this.data = data
    }

    SingleCDockable createBackup(String id) {
        def val = data[id]
        if (val instanceof Closure)
            return val(id)
        else if (val instanceof SingleCDockable)
            return val
    }
}

class DelegatingDockableFactory implements SingleCDockableFactory, Filter {
    SingleCDockableFactory factory
    Filter filter

    DelegatingDockableFactory(SingleCDockableFactory factory, Filter filter) {
        this.filter = filter
        this.factory = factory
    }

    SingleCDockable createBackup(String id) {
        return factory.createBackup(id)
    }

    boolean includes(Object item) {
        return filter.includes(item)
    }
}

class NoFilter implements Filter {
    boolean includes(Object item) {
        return true
    }
}


