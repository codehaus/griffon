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

import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.Menu
import java.awt.PopupMenu
import java.awt.Image
import java.awt.Toolkit

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TrayIconFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        // the following code taken from groovy.swing.factory.ImageIcon

        if (value == null) {
            if (attributes.containsKey("image")) {
                value = attributes.remove("image")
                if (!(value instanceof Image)) {
                    throw new RuntimeException("In $name image: attributes must be of type java.awt.Image")
                }
            } else if (attributes.containsKey("url")) {
                value = attributes.remove("url")
                if (!(value instanceof URL)) {
                    throw new RuntimeException("In $name url: attributes must be of type java.net.URL")
                }
            } else if (attributes.containsKey("file")) {
                value = attributes.remove("file")
                if (value instanceof File) {
                    value = value.toURL()
                } else if (!(value instanceof String)) {
                    throw new RuntimeException("In $name file: attributes must be of type java.io.File or a string")
                }
            }
        }

        // not else if so we can adjust for the case of file string where the file does not exist
        def resource = null
        if ((value == null) && (attributes.containsKey("resource"))) {
            resource = attributes.remove('resource')
        } else if ((value instanceof String) && !(new File(value).exists())) {
            resource = value
        }
        if (resource != null) {
            def klass = builder.context.owner
            def origValue = value
            if (attributes.containsKey("class")) {
                klass = attributes.remove("class")
            }
            if (klass == null) {
                klass = ImageIconFactory
            } else if (!(klass instanceof Class)) {
                klass = klass.class
            }
            // for now try URL approach.
            // we may need to extract the byte[] for some packaging cases
            value = klass.getResource(resource)
            if (value == null) {
                throw new RuntimeException("In $name the value argument '$origValue' does not refer to a file or a class resource")
            }
        }

        if (value == null) {
            throw new RuntimeException("$name has neither a value argument or one of image:, url:, file:, or resource:")
        }

        def trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(value))
        trayIcon.imageAutoSize = true
        return trayIcon
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( parent instanceof SystemTray ) {
            parent.add(child)
        }
    }
}