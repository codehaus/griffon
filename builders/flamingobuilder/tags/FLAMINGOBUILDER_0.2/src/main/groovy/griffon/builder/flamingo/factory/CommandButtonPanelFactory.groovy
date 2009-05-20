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

import groovy.swing.factory.BeanFactory
import org.jvnet.flamingo.common.JCommandButton
import org.jvnet.flamingo.common.JCommandButtonPanel
import org.jvnet.flamingo.common.CommandButtonDisplayState

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class CommandButtonPanelFactory extends AbstractFactory {
    public static final String DELEGATE_PROPERTY_GROUP_NAME = "_delegateProperty:groupName";
    public static final String DEFAULT_DELEGATE_PROPERTY_GROUP_NAME = "groupName";
    public static final String DELEGATE_PROPERTY_GROUP_INDEX = "_delegateProperty:groupIndex";
    public static final String DEFAULT_DELEGATE_PROPERTY_GROUP_INDEX = "groupIndex";

    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def newChild = newPanel(builder, name, value, attributes)
        builder.context.commandButtonPanelFactoryClosure =
            { FactoryBuilderSupport cBuilder, Object cNode, Map cAttributes ->
                if (builder.current == newChild) inspectChild(cBuilder, cNode, cAttributes)
            }
        builder.addAttributeDelegate(builder.context.commandButtonPanelFactoryClosure)

        builder.context[DELEGATE_PROPERTY_GROUP_NAME] = attributes.remove("groupNameProperty") ?: DEFAULT_DELEGATE_PROPERTY_GROUP_NAME
        builder.context[DELEGATE_PROPERTY_GROUP_INDEX] = attributes.remove("groupIndexProperty") ?: DEFAULT_DELEGATE_PROPERTY_GROUP_INDEX

        return newChild
    }

   private JCommandButtonPanel newPanel( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, JCommandButtonPanel) ) {
         return value
      }

      def startingDimension = attributes.remove("startingDimension")
      if(startingDimension) return new JCommandButtonPanel(startingDimension)
      FlamingoFactoryUtils.translateCommandButtonConstants(attributes)
      def commandButtonDisplayState = attributes.remove("commandButtonDisplayState")
      if(commandButtonDisplayState) return new JCommandButtonPanel(commandButtonDisplayState)
      def panel = new JCommandButtonPanel()
      panel.iconDimension = -1
      return panel
   }

    public static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
        def name = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_GROUP_NAME) ?: DEFAULT_DELEGATE_PROPERTY_GROUP_NAME)
        def index = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_GROUP_INDEX) ?: DEFAULT_DELEGATE_PROPERTY_GROUP_INDEX)

        builder.context.put(node, [name, index])
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (!(child instanceof JCommandButton) ) {
            return
        }
        try {
            def ops = builder.context[child] ?: [null, null]
            if (ops[0] == null) {
                ops[0] = child.name
            }

            int count = parent.groupCount
            boolean found = false
            for( i in (0..<count) ) {
               if( parent.getGroupTitleAt(i) == ops[0] ) found = true
            }
            if( !found ) parent.addButtonGroup(ops[0])
            if( ops[1] == null ) {
               parent.addButtonToGroup(ops[0],child)
            } else {
               parent.addButtonToGroup(ops[0],ops[1],child)
            }
        } catch (MissingPropertyException mpe) {
            parent.add(child)
        }
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        super.onNodeCompleted (builder, parent, node)
        builder.removeAttributeDelegate(builder.context.commandButtonPanelFactoryClosure)
    }

   public boolean isLeaf() {
      return false
   }
}