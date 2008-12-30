/* --------------------------------------------------------------------
   TrayBuilder
   Copyright (C) 2008 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
*/

package griffon.builder.tray.factory

import java.awt.SystemTray
import java.awt.Image
import java.awt.Toolkit

import javax.swing.JPopupMenu
import net.java.fishfarm.ui.JPopupTrayIcon

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
                klass = TrayIconFactory
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

        def trayIcon = new JPopupTrayIcon(Toolkit.getDefaultToolkit().getImage(value))
        trayIcon.imageAutoSize = true
        return trayIcon
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( parent instanceof SystemTray ) {
            parent.add(child)
        }
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( child instanceof JPopupMenu ) {
            parent.setJPopupMenu(child)
        } else {
            throw new RuntimeException("taryIcon only accepts JPopupMenu instances as children")
        }
    }
}