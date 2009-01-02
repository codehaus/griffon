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

import groovy.swing.SwingBuilder
import groovy.swing.factory.LayoutFactory
import javax.swing.table.TableModel
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class ITunesTableFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)
      builder.context.constraints = attributes[LayoutFactory.DELEGATE_PROPERTY_CONSTRAINT] ?: attributes[LayoutFactory.DEFAULT_DELEGATE_PROPERTY_CONSTRAINT]

      [:]
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof TableModel ) {
         parent.model = child
      } else {
         throw new RuntimeException("itunesTable accepts tableModel() as child content only.")
      }
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      if( !node.model ) {
         throw new RuntimeException("No model has been defined for itunesTable.")
      }

      def itunesTable = MacWidgetFactory.createITunesTable(node.remove("model"))
      node.each { property, value ->
         InvokerHelper.setProperty(itunesTable, property, value)
      }
      if( builder.context.widgetId ) {
         builder.setVariable(builder.context.widgetId, itunesTable)
      }

      if( builder.parentFactory ) {
         builder.parentFactory.setChild(builder,parent,itunesTable)
      }
   }
}