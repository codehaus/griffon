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

package griffon.builder.swingxtras.factory

import java.beans.*
import com.l2fprod.common.propertysheet.*
import griffon.builder.swingxtras.impl.MutableProperty
import groovy.swing.factory.ComponentFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PropertySheetPanelFactory extends ComponentFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      if( value instanceof PropertySheetPanel) {
         return value
      }
      return new PropertySheetPanel()
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof PropertySheetTable ) {
         parent.tabel = child
      } else if( child instanceof Property ) {
         def table = parent.table
         if( !table ) {
            table = new PropertySheetTable()
            parent.table = table
         }
         def model = table.model
         if( !model ) {
            model = new PropertySheetTableModel()
            table.model = model
         }
         model.addProperty(child)
      } else {
         super.setChild(builder, parent, child)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PropertySheetTableModelFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      if( value instanceof PropertySheetTableModel) {
         return value
      }
      def excludes = attributes.remove("excludes") ?: []
      def model = new PropertySheetTableModel()
      if( value ) {
         Introspector.getBeanInfo(value.class).propertyDescriptors.each {
            if( it.name in excludes ) return
            def property = new MutableProperty(it)
            property.readFromObject(value)
            model.addProperty(property)
         }
      }
      return model
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof Property ) {
         parent.addProperty(child)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PropertyFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {      
      if( value instanceof Property ) {
         return value
      }
      if( value == null ) {
         throw new RuntimeException("In $name you must specify a bean as value")
      }
      def propertyName = attributes.remove("name")
      if( !propertyName ) {
         throw new RuntimeException("In $name you must specify a value for name:")
      }
      PropertyDescriptor pd = Introspector.getBeanInfo(value.class).propertyDescriptors.find{ it.name == propertyName }
      if( !pd ) {
         throw new RuntimeException("There is no matching property '$propertyName' on ${value.class}")
      }
      def property = new MutableProperty(pd)
      property.readFromObject(value)
      return property
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof MutableProperty ) {
         parent.subproperties << child
         child.parentProperty = parent
      }
   }
}