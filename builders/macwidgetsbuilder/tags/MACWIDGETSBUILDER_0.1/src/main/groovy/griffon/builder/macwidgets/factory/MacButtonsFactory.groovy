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

package griffon.builder.macwidgets.factory

import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JToggleButton
import javax.swing.ImageIcon
import javax.swing.AbstractButton
import java.awt.event.ActionListener
import com.explodingpixels.macwidgets.MacButtonFactory
import com.explodingpixels.widgets.PopupMenuCustomizer

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MacButtonsFactory extends AbstractFactory {
   final Icon icon
   final String buttonType
   protected Icon selectedIcon
   final boolean focusable

   MacButtonsFactory( Icon icon, String buttonType, boolean focusable = true, Icon selectedIcon = null ) {
      this.icon = icon
      this.buttonType = buttonType
      this.focusable = focusable
      this.selectedIcon = selectedIcon
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      def button = selectedIcon ? new JToggleButton(icon) : new JButton(icon)
      if(selectedIcon) button.setSelectedIcon(selectedIcon)
      button.putClientProperty("JButton.buttonType", buttonType )
      if( attributes.containsKey("segmentedPosition") ) {
         String segmentedPosition = attributes.remove("segmentedPosition").toLowerCase()
         button.putClientProperty("JButton.segmentedPosition", segmentedPosition )
      }
      button.focusable = focusable
      return button
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MacGradientButtonFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if (value == null) {
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
         } else if (attributes.containsKey("icon")) {
            value = attributes.remove("icon")
            if (!(value instanceof Icon)) {
               throw new RuntimeException("In $name icon: attributes must be of type javax.swing.Icon")
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
               klass = AbstractMacButtonFactory
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
         throw new RuntimeException("$name has neither a value argument or one of url:, file:, resource: or icon:")
      }

      return newButton(builder, name, attributes, value instanceof Icon ? value : new ImageIcon(value))
   }

   protected Object newButton( FactoryBuilderSupport builder, Object name, Map attributes, Icon icon )
            throws InstantiationException, IllegalAccessException {
      ActionListener actionListener = attributes.remove("actionListener")
      Closure actionPerformed = attributes.remove("actionPerformed")
      if( !actionListener && actionPerformed instanceof Closure ) {
         actionListener = actionPerformed as ActionListener
      }

      return MacButtonFactory.createGradientButton(icon,actionListener)
   }

   public boolean isLeaf() {
      return true
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MacGradientPopdownButtonFactory extends MacGradientButtonFactory {
   protected Object newButton( FactoryBuilderSupport builder, Object name, Map attributes, Icon icon )
            throws InstantiationException, IllegalAccessException {
      PopupMenuCustomizer popupMenuCustomizer = attributes.remove("popupMenuCustomizer")
      Closure customizePopup = attributes.remove("customizePopup")
      if( !popupMenuCustomizer && customizePopup instanceof Closure ) {
         popupMenuCustomizer = customizePopup as PopupMenuCustomizer
      }
      if( !popupMenuCustomizer ) {
         throw new RuntimeException("$name does not have a value for popupMenuCustomizer: nor customizePopup:")
      }

      return MacButtonFactory.createGradientPopdownButton(icon,popupMenuCustomizer)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
enum MacBarButtonType {
  UNIFIED_TOOLBAR,
  PREFERENCES_TAB_BAR
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MacBarButonFactory extends MacGradientButtonFactory {
   final MacBarButtonType type

   MacBarButonFactory( MacBarButtonType type = MacBarButtonType.UNIFIED_TOOLBAR ) {
      this.type = type
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value == null && attributes.containsKey("button") ) {
         value = attributes.remove("button")
         if( value instanceof AbstractButton ) {
            return makeButton(value)
         } else {
            throw new RuntimeException("In $name attribute button: must be of type javax.swing.AbstractButton")
         }
      } else if( value instanceof AbstractButton ) {
         return makeButton(value)
      }

      return super.newInstance(builder,name,value,attributes)
   }

   protected Object newButton( FactoryBuilderSupport builder, Object name, Map attributes, Icon icon )
            throws InstantiationException, IllegalAccessException {
      ActionListener actionListener = attributes.remove("actionListener")
      Closure actionPerformed = attributes.remove("actionPerformed")
      if( !actionListener && actionPerformed instanceof Closure ) {
         actionListener = actionPerformed as ActionListener
      }

      def button  = new JButton(icon)
      if(actionListener) button.addActionListener(actionListener)
      return makeButton(button)
   }

   private makeButton( AbstractButton button ) {
      switch(type) {
         case MacBarButtonType.PREFERENCES_TAB_BAR: return MacButtonFactory.makePreferencesTabBarButton(button)
         case MacBarButtonType.UNIFIED_TOOLBAR:
         default: return MacButtonFactory.makeUnifiedToolBarButton(button)
      }
   }
}