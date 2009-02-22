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

package griffon.builder.flamingo.factory

import org.jvnet.flamingo.bcb.BreadcrumbItem

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BreadcrumbItemFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, BreadcrumbItem) ) {
         return value
      }

      def key = attributes.remove("key")
      if( !key ) throw new IllegalArgumentException("$name requires a key attribute")
      def data = attributes.remove("date")
      def title = value instanceof String ? value : attributes.remove("text")
      title = title ?: ""
      return data != null ? new BreadcrumbItem(key, data) : new BreadcrumbItem(key)
   }

   public boolean isLeaf() {
      return true
   }
}