/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.macwidgets.factory

import java.awt.event.ActionListener
import javax.swing.Icon
import javax.swing.ImageIcon
import com.explodingpixels.macwidgets.PreferencesTabBar
import com.explodingpixels.macwidgets.MacPreferencesTabBar
import griffon.builder.macwidgets.impl.PreferencesTab

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PreferencesTabFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( builder.context.parentFactory && ( !(builder.context.parentFactory instanceof PreferencesTabBarFactory) &&
          !(builder.context.parentFactory instanceof MacPreferencesTabBarFactory) ) ) {
         throw new RuntimeException("$name can only be nested inside preferencesTabBar or macPreferencesTabBar")
      }
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, PreferencesTab) ) {
         return value
      }

      def text = attributes.remove("text") ?: value

      Icon icon = null
      if( !attributes.containsKey("icon") ) {
         if (attributes.containsKey("url")) {
            value = attributes.remove("url")
            if (!(value instanceof URL)) {
               throw new RuntimeException("In $name url: attributes must be of type java.net.URL")
            }
         } else if (attributes.containsKey("file")) {
            value = attributes.remove("file")
            if (value instanceof File) {
               value = value.toURI().toURL()
            } else if (value instanceof String) {
               value = new File(value).toURI().toURL()
            } else {
               throw new RuntimeException("In $name file: attributes must be of type java.io.File or a string")
            }
         } else if (attributes.containsKey("inputStream")) {
            value = attributes.remove("inputStream")
            if (!(value instanceof InputStream)) {
               throw new RuntimeException("In $name inputStream: attributes must be of type java.io.InputStream")
            }
         }

         // not else if so we can adjust for the case of file string where the file does not exist
         def resource = attributes.remove("resource")
         if (resource != null) {
            def klass = builder.context.owner
            if (attributes.containsKey("class")) {
                klass = attributes.remove("class")
            }
            if (klass == null) {
                klass = PreferencesTabFactory
            } else if (!(klass instanceof Class)) {
                klass = klass.class
            }
            // for now try URL approach.
            // we may need to extract the byte[] for some packaging cases
            value = klass.getResource(resource)
            if (value == null) {
                throw new RuntimeException("In $name the value argument '$resource' does not refer to a file or a class resource")
            }
         }

         if(value) icon = new ImageIcon(value)
      } else {
         icon = attributes.remove("icon")
      }

      ActionListener actionListener = attributes.remove("actionListener")
      Closure actionPerformed = attributes.remove("actionPerformed")
      if( !actionListener && actionPerformed instanceof Closure ) {
         actionListener = actionPerformed as ActionListener
      }

      if( text == null && icon == null ) {
         throw new RuntimeException("in $name at least one of text: or icon: attributes must be specified")
      }

      return new PreferencesTab(text:text, icon: icon, actionListener: actionListener)
   }
}