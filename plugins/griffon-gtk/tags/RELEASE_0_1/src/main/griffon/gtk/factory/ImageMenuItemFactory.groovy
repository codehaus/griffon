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

import griffon.gtk.GtkUtils
import org.gnome.gtk.MenuShell
import org.gnome.gtk.MenuItem
import org.gnome.gtk.ImageMenuItem
import org.gnome.gtk.Widget
import org.gnome.gtk.Image
import org.gnome.gtk.Stock
import org.gnome.gdk.Pixbuf

/**
 * @author Andres Almiray
 */
class ImageMenuItemFactory extends MenuItemFactory {
    ImageMenuItemFactory() {
        super(ImageMenuItem)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof ImageMenuItem) return value

        Image image = null
        Stock stock = null
        String label = ''

        if(value instanceof Image) image = value
        else if(value instanceof Pixbuf) image = new Image(value)
        else if(value instanceof GString) value = value as String
        if(value instanceof String) label = value
        stock = GtkUtils.parseStock(attributes.remove('stock'))
        if(!stock) image = new Image(PixbufFactory.newPixbuf(builder, name, value, attributes))

        if(!label && attributes.containsKey('mnemonicLabel')) label = attributes.remove('mnenomicLabel').toString()
        
        if(stock) {
            return new ImageMenuItem(stock)
        } else if(image) {
            return new ImageMenuItem(image, label)
        } else {
            throw new IllegalArgumentException("In $name you must specify a value for either image: or stock:")
        }
    }
}
