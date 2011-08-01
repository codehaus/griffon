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

import bibliothek.gui.dock.facile.menu.NodeMenuPiece
import bibliothek.gui.dock.support.menu.MenuPiece
import groovy.swing.factory.BeanFactory
import javax.swing.JMenuBar

/**
 * @author Alexander Klein
 */
class NodeMenuPieceFactory extends BeanFactory {
    NodeMenuPieceFactory() {
        super(NodeMenuPiece, false)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof MenuPiece)
            parent.add(child)
    }
}
