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

import javax.swing.Action
import org.jvnet.flamingo.common.icon.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class CommandButtonFactory extends AbstractFactory {
    private Class beanClass

    CommandButtonFactory( Class beanClass ) {
       this.beanClass = beanClass
    }

    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value && beanClass.isAssignableFrom(value.getClass()) ) {
         return value
      }

      def icon = null
      def text = null
      /*if( value instanceof Action ) {
         icon = value.getValue(FlamingoFactoryUtils.ICON_PROPERTY) ?: new EmptyResizableIcon(32)
         text = value.getValue(FlamingoFactoryUtils.NAME_PROPERTY)
         if( !(icon instanceof ResizableIcon) )
            throw new IllegalArgumentException("In $name, provided icon: by action is not a ResizableIcon.")
      } else {*/
         icon = FlamingoFactoryUtils.createIcon(builder, name, null, attributes)
         if( !icon ) icon = new EmptyResizableIcon(32)
         if( !(icon instanceof ResizableIcon) )
            throw new IllegalArgumentException("In $name a value for icon: must be defined.")
         text = value instanceof String || value instanceof GString ? value : attributes.remove("text")
      //}
      text = text != null ? text.toString() : "" // force GString eval

      def cmd = beanClass.getDeclaredConstructor([String,ResizableIcon] as Class[]).newInstance(text, icon)
      //if( value instanceof Action ) FlamingoFactoryUtils.configureFromAction(cmd,value)
      return cmd
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      FlamingoFactoryUtils.translateCommandButtonConstants(attributes)
      return true
   }

   public boolean isLeaf() {
      return true
   }
}