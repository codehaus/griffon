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

import java.awt.Dimension

import org.jvnet.flamingo.common.icon.DecoratedResizableIcon
import org.jvnet.flamingo.common.icon.ResizableIcon

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class DecoratedResizableIconFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, DecoratedResizableIcon) ) {
         return value
      }

      def icon = FlamingoFactoryUtils.createIcon(builder, name, value, attributes)
      if( !(icon instanceof ResizableIcon) )
         throw new IllegalArgumentException("$name requires a ResizableIcon as delegate, current one is ${icon.class.name}")
      def decorators = attributes.remove("decorators") ?: []
      return new DecoratedResizableIcon(icon,decorators)
   }

   public boolean isLeaf() {
      return true
   }
}