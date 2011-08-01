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
package com.canoo.griffon.factory.dockable.menu

import bibliothek.gui.dock.facile.menu.RootMenuPiece
import bibliothek.gui.dock.facile.menu.SubmenuPiece
import bibliothek.gui.dock.support.menu.MenuPiece
import groovy.swing.factory.BeanFactory
import javax.swing.JMenu
import javax.swing.JMenuBar

/**
 * @author Alexander Klein
 */
class RootMenuPieceFactory extends BeanFactory {
    boolean subMenu = false

    RootMenuPieceFactory(boolean subMenu = false) {
        super(subMenu ? SubmenuPiece : RootMenuPiece, false)
        this.subMenu = subMenu
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof JMenu) {
            value = subMenu ? new SubmenuPiece(value) : new RootMenuPiece(value)
        } else if (value instanceof String) {
            value = subMenu ? new SubmenuPiece(new JMenu(value)) : new RootMenuPiece(new JMenu(value))
        } else if (!value) {
            def disableWhenEmpty = attributes.remove('disableWhenEmpty')
            JMenu menu = new BeanFactory(JMenu).newInstance(builder, 'menu', null, attributes)
            builder.setNodeAttributes(menu, attributes)
            value = subMenu ? new SubmenuPiece(menu) : new RootMenuPiece(menu)
            attributes.clear()
            if (disableWhenEmpty)
                attributes.disableWhenEmpty = disableWhenEmpty
        }
        return super.newInstance(builder, name, value, attributes)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof MenuPiece)
            if (subMenu)
                parent.root.add(child)
            else
                parent.add(child)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof JMenuBar)
            parent.add(child.menu)
    }
}
