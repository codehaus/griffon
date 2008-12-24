/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.builder.tray.factory

import java.awt.TrayIcon
import java.awt.PopupMenu
import java.awt.Menu
import javax.swing.JMenuItem
import javax.swing.JSeparator
import javax.swing.Action

import griffon.builder.tray.impl.SwingPopupMenu

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PopupMenuFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return new SwingPopupMenu()
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( parent instanceof TrayIcon ) {
            parent.popupMenu = child
        }
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( child instanceof String || child instanceof Action || 
            child instanceof JMenuItem || child instanceof JSeparator ) {
            parent.add(child)
        }
    }
}
