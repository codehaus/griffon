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
package com.canoo.griffon.factory.dockable.action

import bibliothek.gui.dock.common.action.CAction
import java.awt.Component
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.intern.AbstractCDockable

/**
 * @author Alexander Klein
 */
class CSystemActionFactory extends AbstractFactory {
    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def key = attributes.remove('name') ?: value
        if (key instanceof String) {
            if (!key.startsWith('cdockable.'))
                key = CDockable.getField("ACTION_KEY_${key.toUpperCase()}").get(null)
        } else
            throw new IllegalArgumentException('Name required')

        def action = attributes.remove('replacement')
        return [name: key, action: action]
    }

    @Override
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (!(child instanceof CAction))
            return
        parent.action = child
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof AbstractCDockable && child.action instanceof CAction)
            parent.putAction(child.name, child.action)
    }
}
