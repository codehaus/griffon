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

import com.explodingpixels.macwidgets.MacWidgetFactory
import com.explodingpixels.macwidgets.MacPreferencesTabBar
import griffon.builder.macwidgets.impl.PreferencesTab

import groovy.swing.SwingBuilder

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MacPreferencesTabBarFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)

      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, MacPreferencesTabBar) ) {
         builder.context.tabBar = value
         builder.setVariable(builder.context.widgetId,value)
         return value
      }

      def tabBar = new MacPreferencesTabBar()
      builder.context.tabBar = tabBar
      builder.setVariable(builder.context.widgetId,tabBar)
      return tabBar.component
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof PreferencesTab ) {
         builder.parentContext.tabBar.addTabButton(child.text, child.icon, child.actionListener)
      } else {
         throw new RuntimeException("preferencesTabBar accepts preferencesTab() as child content only.")
      }
   }
}