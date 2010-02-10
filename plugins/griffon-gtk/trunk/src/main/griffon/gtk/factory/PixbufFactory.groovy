/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.gtk.factory

import griffon.gtk.GtkUtils
import org.gnome.gdk.Pixbuf

/**
 * @author Andres Almiray
 */
class PixbufFactory extends GtkBeanFactory {
    PixbufFactory(beanClass) {
        super(Pixbuf, true)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        return newPixbuf(builder, name, value, attributes)
    }

    static Pixbuf newPixbuf(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(!value) value = attributes.remove('resource')
        if(value instanceof String || value instanceof URL) {
            String str = value.toString()
            String suffix = str.substring(str.lastIndexOf('.')) ?: '.png'
            File temp = GtkUtils.createTempResource(value, suffix)
            if(!attributes) {
                return new Pixbuf(temp.absolutePath)
            } else {
                int width = toInteger(attributes.remove('width') ?: -1)
                int height = toInteger(attributes.remove('height') ?: -1)
                boolean preserveAspectRatio = toBoolean(attributes.remove('preserveAspectRatio') ?: true)
                return new Pixbuf(temp.absolutePath, width, height, preserveAspectRatio)
            }
        } else {
            throw new IllegalArgumentException("In $name you must define a value or an attribute resource: of type String or URL")
        }
    }
}
