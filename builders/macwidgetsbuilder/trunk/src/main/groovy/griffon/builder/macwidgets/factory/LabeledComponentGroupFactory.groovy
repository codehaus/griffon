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

package griffon.builder.macwidgets.factory

import javax.swing.JComponent
import com.explodingpixels.macwidgets.LabeledComponentGroup

import groovy.swing.SwingBuilder
import groovy.swing.factory.LayoutFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class LabeledComponentGroupFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)
      builder.context.constraints = attributes[LayoutFactory.DELEGATE_PROPERTY_CONSTRAINT] ?: attributes[LayoutFactory.DEFAULT_DELEGATE_PROPERTY_CONSTRAINT]

      if( value == null ) {
         if( attributes.containsKey("label") ) {
            value = attributes.remove("label")
         } else if( attributes.containsKey("text") ) {
            value = attributes.remove("text")
         }
      }
      if( value == null || !(value instanceof String) ) {
         throw new RuntimeException("$name has neither a value argument or one of label: or text:")
      }

      return [label: value, components: []]
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      if( attributes ) {
         throw new RuntimeException("${builder.currentName} does not know how to handle additional properties $attributes")
      }
      return false
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof JComponent ) {
         parent.components << child
      }
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
       super.onNodeCompleted(builder, parent, node)
       if( !node.components ) {
          throw new RuntimeException("No children components have been defined for ${builder.currentName}.")
       }

       def labeledComponentGroup = new LabeledComponentGroup(node.label, *node.components)
       if( builder.parentFactory ) {
          builder.parentFactory.setChild(builder,parent,labeledComponentGroup.component)
       }
       if( builder.context.widgetId ) {
          builder.setVariable(builder.context.widgetId,labeledComponentGroup)
       }
   }
}