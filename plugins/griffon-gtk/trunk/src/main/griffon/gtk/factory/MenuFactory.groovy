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
import org.gnome.gtk.MenuItem
import org.gnome.gtk.MenuToolButton
import org.gnome.gtk.Widget

/**
 * @author Andres Almiray
 */
class MenuFactory extends ContainerFactory {
    MenuFactory() {
        super(Menu)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
        if(parent instanceof MenuItem) parent.setSubmenu(node)
        else if(parent instanceof MenuToolButton) parent.setMenu(node)
        else super.setParent(builder, parent, node)
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof Widget) parent.append(child)
        else super.setChild(builder, parent, child)
    }
}
