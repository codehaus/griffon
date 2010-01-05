/*
 * Copyright 2008-2010 the original author or authors.
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
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.gtk

import org.gnome.gtk.Window
import griffon.gtk.factory.WindowFactory

/**
 * @author Andres Almiray
 */
class ApplicationFactory extends WindowFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        def window = builder.app.createApplicationContainer()

        def width = attributes.remove('width')
        def height = attributes.remove('height')

        if(width != null && height != null) window.setDefaultSize(toInteger(width), toInteger(height))
        return window
    }
}
