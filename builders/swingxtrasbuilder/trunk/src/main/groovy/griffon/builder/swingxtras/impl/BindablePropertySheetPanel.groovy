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

package griffon.builder.swingxtras.impl

import java.beans.*
import com.l2fprod.common.propertysheet.Property
import com.l2fprod.common.propertysheet.PropertySheetPanel

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BindablePropertySheetPanel extends PropertySheetPanel {
   Object bean
   private PropertyChangeListener listener

   public void setup( Object bean, List<String> excludes = [], List<String> includes = [] ) {
      if( !this.bean.is(bean) ) {
         unbind()
      }

      this.bean = bean
      def properties = []
      Introspector.getBeanInfo(bean.class).propertyDescriptors.each {
         if( it.name in excludes ) return
         if( includes && !(it.name in includes) ) return
         properties << new MutableProperty(it)
      }
      this.setProperties(properties as Property[])
      this.readFromObject(bean)
   }

   public void bind( Object bean ) {
      if( !this.bean.is(bean) ) {
         unbind()
      }
      this.bean = bean
      listener = new PropertySheetChangeListener(bean)
      addPropertySheetChangeListener(listener)
   }

   public void unbind() {
      if( listener ) removePropertySheetChangeListener(listener)
      this.bean = null
      this.listener = null
   }

   public boolean isBound() {
      return bean != null
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PropertySheetChangeListener implements PropertyChangeListener {
   private final Object bean

   PropertySheetChangeListener( Object bean ) {
      this.bean = bean
   }

   public void propertyChange( PropertyChangeEvent evt ) {
      Property prop = evt.source
      try {
         prop.writeToObject(bean)
      } catch( RuntimeException e ) {
         // handle PropertyVetoException and restore previous value
         if( e.cause instanceof PropertyVetoException) {
            prop.value = evt.oldValue
         }
      }
   }
}