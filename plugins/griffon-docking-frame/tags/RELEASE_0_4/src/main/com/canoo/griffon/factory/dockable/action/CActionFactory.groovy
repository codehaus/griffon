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

import bibliothek.gui.dock.support.menu.MenuPiece
import java.awt.Component
import javax.swing.JMenuBar
import bibliothek.gui.dock.common.action.CAction
import bibliothek.gui.dock.common.intern.DefaultCDockable

/**
 * @author Alexander Klein
 */
class   CActionFactory extends AbstractFactory {
    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value == null)
            value = attributes.remove(name);
        if ((value != null) && FactoryBuilderSupport.checkValueIsType(value, name, CAction)) {
            return value;
        } else {
            throw new RuntimeException("$name must have either a value argument or an attribute named $name that must be of type $CAction.name");
        }
    }

    @Override
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (!(child instanceof CAction) && !(child instanceof Component))
            return
        try {
            parent.add(child)
        } catch (MissingPropertyException mpe) {
            try {
                parent.content = child
            } catch (MissingPropertyException e) {}
        }
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof DefaultCDockable)
            parent.addAction(child)
    }
}
