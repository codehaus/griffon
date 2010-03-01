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

import org.gnome.gtk.Window
import org.gnome.gtk.WindowType

/**
 * @author Andres Almiray
 */
class WindowFactory extends ContainerFactory {
    WindowFactory() {
        super(Window)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }

        def width = attributes.remove('width')
        def height = attributes.remove('height')

        def window = new Window(parseWindowType(attributes.remove('windowType')))
        if(width != null && height != null) window.setDefaultSize(toInteger(width), toInteger(height))
        return window
    }

    private WindowType parsewindowType(type) {
        if(type instanceof WindowType) return type
        if('popup'.equalsIgnoreCase(type)) return WindowType.POPUP
        return WindowType.TOPLEVEL
    }
}
