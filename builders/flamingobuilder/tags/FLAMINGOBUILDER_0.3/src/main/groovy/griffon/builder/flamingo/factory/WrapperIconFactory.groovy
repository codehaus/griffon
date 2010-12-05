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

package griffon.builder.flamingo.factory

import java.awt.Dimension
import org.jvnet.flamingo.common.icon.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class WrapperIconFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, ImageWrapperResizableIcon) ) {
         return value
      }
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, IcoWrapperResizableIcon) ) {
         return value
      }

      value = FlamingoFactoryUtils.processIconAttributes(builder, name, value, attributes)

      if (value == null) {
         throw new RuntimeException("$name has neither a value argument or one of url:, file:, or resource:")
      }

      def type = attributes.remove("type")
      def id = FlamingoFactoryUtils.processIconInitialDimAttribute(name, attributes)

      if( value instanceof URL ) {
         if( value.toString().endsWith("ico") ) {
            return new IcoWrapperResizableIcon(value,initialDim)
         } else {
            return new ImageWrapperResizableIcon(value,initialDim)
         }
      } else if( value instanceof InputStream ) {
        switch( type ) {
            case "image": return new ImageWrapperResizableIcon(url,initialDim)
            case "icon": return new IcoWrapperResizableIcon(url,initialDim)
            default:
               throw new RuntimeException("In $name type: must be defined when an java.io.InputStream is also supplied")
         }
      }
   }

   public boolean isLeaf() {
      return true
   }
}