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

import java.awt.LayoutManager
import java.awt.Component
import java.awt.Window
import javax.swing.JDialog
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel
import com.explodingpixels.macwidgets.HudWindow
import com.explodingpixels.macwidgets.HudWidgetFactory
import groovy.swing.binding.JComboBoxMetaMethods

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class HudWindowFactory extends AbstractFactory {
   static {
      HudWindow.metaClass.getProperty = { String name ->
         def metaProperty = HudWindow.metaClass.getMetaProperty(name)
         if(metaProperty) return metaProperty.getProperty(delegate)
         metaProperty = JDialog.metaClass.getMetaProperty(name)
         if(metaProperty) return metaProperty.getProperty(delegate.getJDialog())
         throw new MissingPropertyException(name,HudWindow)
      }

      HudWindow.metaClass.setProperty = { String name, value ->
         def metaProperty = HudWindow.metaClass.getMetaProperty(name)
         if(metaProperty){ metaProperty.setProperty(delegate,value); return }
         metaProperty = JDialog.metaClass.getMetaProperty(name)
         if(metaProperty){ metaProperty.setProperty(delegate.getJDialog(),value); return }
         throw new MissingPropertyException(name,HudWindow)
      }
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, HudWindow) ) {
         return value
      }

      def title = attributes.remove("title") ?: ""
      return new HudWindow(title)
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof LayoutManager ) {
         parent.getContentPane().layout = child
      } else if( !(child instanceof Component) || (child instanceof Window) ) {
         return
      }

      try {
         def constraints = builder.context.constraints
         if( constraints != null ) {
            parent.getContentPane().add(child, constraints)
            builder.context.remove('constraints')
         } else {
            parent.getContentPane().add(child)
         }
      } catch( MissingPropertyException mpe ) {
         parent.getContentPane().add(child)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class HudWidgetsFactory extends AbstractFactory {
   private final widgetType

   HudWidgetsFactory( String widgetType ) {
      this.widgetType = widgetType
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      def text = value ?: attributes.remove("text")
      text = text ?: attributes.remove("label")
      text = text != null ? text.toString() : ""
      return HudWidgetFactory."createHud$widgetType"(text)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class HudComboBoxFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      def model = null
      if( value instanceof List ) {
         model = new DefaultComboBoxModel(value.toArray())
      } else if( value instanceof Object[] ) {
         model = new DefaultComboBoxModel(value)
      } else if( value instanceof ComboBoxModel ) {
         model = value
      }

      if( !model ) {
         if( attributes.containsKey("items") ) {
            value = attributes.remove("items")
            if( value instanceof List ) {
               model = new DefaultComboBoxModel(value.toArray())
            } else if( value instanceof Object[] ) {
               model = new DefaultComboBoxModel(value)
            } else {
               throw new RuntimeException("In $name value of items: must be a List or Object[]")
            }
         } else if( attributes.containsKey("model") ) {
            value = attributes.remove("model")
            if( value instanceof ComboBoxModel ) {
               model = value
            } else {
               throw new RuntimeException("In $name value of model: must be a ComboBoxModel")
            }
         } else {
            throw new RuntimeException("in $name you must define a value of type List, Object[] or ComboBoxModel; or supply a value for either items: or model:")
         }
      }

      def comboBox = HudWidgetFactory.createHudComboBox(model)
      JComboBoxMetaMethods.enhanceMetaClass(comboBox)
      return comboBox
   }
}