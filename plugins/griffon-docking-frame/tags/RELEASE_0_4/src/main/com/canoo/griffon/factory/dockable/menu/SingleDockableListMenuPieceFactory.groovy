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

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece
import bibliothek.gui.dock.support.menu.MenuPiece
import com.canoo.griffon.factory.dockable.CStationFactory
import groovy.swing.factory.BeanFactory
import javax.swing.JMenuBar

/**
 * @author Alexander Klein
 */
class SingleDockableListMenuPieceFactory extends BeanFactory {
    SingleDockableListMenuPieceFactory() {
        super(SingleCDockableListMenuPiece, true)
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof CControl) {
            value = new SingleCDockableListMenuPiece(value)
        } else {
            def control = attributes.remove('control') ?: CStationFactory.findParentControl(builder.context)
            if (control instanceof CControl)
                value = new SingleCDockableListMenuPiece(control)
        }
        if (value)
            return super.newInstance(builder, name, value, attributes)
        else
            throw new IllegalArgumentException('CControl needed')
    }
}
