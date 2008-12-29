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

import java.awt.LayoutManager
import java.awt.Component
import java.awt.Window
import javax.swing.JFrame
import com.explodingpixels.macwidgets.HudWindow

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class HudWindowFactory extends AbstractFactory {
   static {
      HudWindow.metaClass.getProperty = { String name ->
         def metaProperty = HudWindow.metaClass.getMetaProperty(name)
         if(metaProperty) return metaProperty.getProperty(delegate)
         metaProperty = JFrame.metaClass.getMetaProperty(name)
         if(metaProperty) return metaProperty.getProperty(delegate.getJFrame())
         throw new MissingPropertyException(name,HudWindow)
      }

      HudWindow.metaClass.setProperty = { String name, value ->
         def metaProperty = HudWindow.metaClass.getMetaProperty(name)
         if(metaProperty){ metaProperty.setProperty(delegate,value); return }
         metaProperty = JFrame.metaClass.getMetaProperty(name)
         if(metaProperty){ metaProperty.setProperty(delegate.getJFrame(),value); return }
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