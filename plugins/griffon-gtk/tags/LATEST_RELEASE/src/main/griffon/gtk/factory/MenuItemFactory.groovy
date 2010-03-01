/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.gtk.factory

import org.gnome.gtk.Menu
import org.gnome.gtk.MenuShell
import org.gnome.gtk.MenuItem
import org.gnome.gtk.Widget

/**
 * @author Andres Almiray
 */
class MenuItemFactory extends ContainerFactory {
    private static final Class[] PARAMS = [String] as Class[]

    MenuItemFactory(Class menuItemClass) {
        super(menuItemClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(value instanceof String) return beanClass.getDeclaredConstructor(PARAMS).newInstance([value] as Object[])
        if(value && beanClass.isAssignableFrom(value.getClass())) {
            return value
        }
        
        String label = value ?: ''
        if(!label && attributes.containsKey('mnemonicLabel')) label = attributes.remove('mnenomicLabel').toString()
        return beanClass.getDeclaredConstructor(PARAMS).newInstance([label] as Object[])
    }

/*
    void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
        if(parent instanceof MenuShell) parent.append(node)
        else super.setParent(builder, parent, node)
    }
*/

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof Menu) parent.setSubmenu(child)
        else super.setChild(builder, parent, child)
    }
}
