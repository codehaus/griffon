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
import org.gnome.gtk.Image
import org.gnome.gtk.Stock
import org.gnome.gtk.IconSize

/**
 * @author Andres Almiray
 */
class ImageFactory extends GtkBeanFactory {
    ImageFactory(beanClass) {
        super(Image, true)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof Image) return value
        if(value instanceof Pixbuf) return new Image(value)
        if(value instanceof GString) value = value as String
        if(value instanceof String) return new Image(PixbufFactory.newPixbuf(builder, name, value, attributes))
        if(value) throw new IllegalArgumentException("$name does not accept a value of type ${value.getClass().name}")
        // TODO add Stock
    }
}
