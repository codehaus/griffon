/*
 * Copyright 2008-2009 the original author or authors.
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

package griffon.builder.swingxtras.impl

import groovy.beans.*
import java.beans.*
import com.l2fprod.common.propertysheet.Property

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MutableProperty implements Property {
   private final PropertyDescriptor propertyDescriptor
   @Bindable Object value
   String category
   Property parentProperty
   List<Property> subproperties = new ArrayList<Property>()

   // overridable properties
   String displayName
   String name
   String shortDescription
   Class type

   MutableProperty( PropertyDescriptor propertyDescriptor ) {
      this.propertyDescriptor = propertyDescriptor
   }

   public Object clone() {
      MutableProperty other = new MutableProperty(propertyDescriptor)
      other.category = category
      other.parentProperty = parentProperty
      other.subproperties.addAll(suproperties)
      other.displayName = displayName
      other.name = name
      other.shortDescription = shortDescription
      other.type = type
   }

   public String getDisplayName() {
      displayName ?: propertyDescriptor.displayName
   }

   public String getName() {
      name ?: propertyDescriptor.name
   }

   public String getShortDescription() {
      shortDescription ?: propertyDescriptor.shortDescription
   }

   public Property[] getSubProperties() {
      subproperties as Property[]
   }

   public Class getType() {
      type ?: propertyDescriptor.propertyType
   }

   public boolean isEditable() {
      try {
         def writeMethod = propertyDescriptor.getWriteMethod()
         return writeMethod != null
      } catch( exception ) {
         return false
      }
   }

   public void readFromObject( object ) {
      try {
         value = propertyDescriptor.readMethod.invoke(object,[] as Object[])
      } catch( exception ) {
         // ignore
      }
   }

   public void writeToObject( object ) {
      try {
         propertyDescriptor.writeMethod.invoke(object,[value] as Object[])
      } catch( exception ) {
         // ignore
      }
   }
}