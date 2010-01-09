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
import javax.swing.SwingConstants
import org.jvnet.flamingo.utils.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class ArrowIconFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, ArrowResizableIcon) ) {
         return value
      }
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, DoubleArrowResizableIcon) ) {
         return value
      }

      def d = attributes.remove("direction")
      if( !d ){
         d = SwingConstants.EAST
      } else {
         switch(d) {
            case SwingConstants.EAST:
            case SwingConstants.WEST:
            case SwingConstants.NORTH:
            case SwingConstants.SOUTH:
               break
            case {it instanceof String}:
               d = SwingConstants."${d.toUpperCase()}"
               break
            default:
               throw new IllegalArgumentException("Invalid direction '$d' for arrowIcon");
         }
      }

      def id = FlamingoFactoryUtils.processIconInitialDimAttribute(name, attributes)

      if( attributes.remove("doubleHeaded") ) {
         return new DoubleArrowResizableIcon(id,SwingConstants.SOUTH)
      } else {
         return new ArrowResizableIcon(id,d)
      }
   }

   public boolean isLeaf() {
      return true
   }
}