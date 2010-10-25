/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License')
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.designgridlayout.factory

import java.awt.Container
import groovy.swing.factory.LayoutFactory
import net.java.dev.designgridlayout.DesignGridLayout

/**
 * @author Andres Almiray
 */
class DesignGridLayoutFactory extends LayoutFactory {
    DesignGridLayoutFactory() {
        super(DesignGridLayout)
    }

    boolean isLeaf() {
        false
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        Container container = null
        if(value instanceof Container) {
            container = value
        } else if(builder.current instanceof Container) {
            container = builder.current
        } else {
            throw new IllegalArgumentException("iIn $name you must nest it inside a Container or specify a Container as value.")
        }

        new DesignGridLayout(getLayoutTarget(container))
    }

    boolean isHandlesNodeChildren() {
        true
    }

    boolean onNodeChildren(final FactoryBuilderSupport builder, final Object node, Closure childContent) {
        childContent.resolveStrategy = Closure.DELEGATE_FIRST
        childContent.delegate = new GroovyObject() {
            def invokeMethod(String name, args) {
                try { return node."$name"(*args) }
                catch(MissingMethodException mme) {
                    return builder."$name"(*args)
                }
            }

            void setProperty(String name, value) {
                try { node."$name" = value }
                catch(MissingPropertyException mpe) {
                    builder."$name" = value
                }
            }

            def getProperty(String name) {
                try { return node."$name" }
                catch(MissingPropertyException mpe) {
                    return builder."$name"
                }
            }
        }

        String parentName = builder.currentName
        Map parentContext = builder.context
        builder.newContext()
        try {
            builder.context.put(FactoryBuilderSupport.OWNER, childContent.owner)
            builder.context.put(FactoryBuilderSupport.CURRENT_NODE, node)
            builder.context.put(FactoryBuilderSupport.PARENT_FACTORY, builder.currentFactory)
            builder.context.put(FactoryBuilderSupport.PARENT_NODE, builder.current)
            builder.context.put(FactoryBuilderSupport.PARENT_CONTEXT, parentContext)
            builder.context.put(FactoryBuilderSupport.PARENT_NAME, parentName)
            builder.context.put(FactoryBuilderSupport.PARENT_BUILDER, parentContext.get(FactoryBuilderSupport.CURRENT_BUILDER))
            builder.context.put(FactoryBuilderSupport.CURRENT_BUILDER, parentContext.get(FactoryBuilderSupport.CHILD_BUILDER))
            childContent()
        } finally {
            builder.popContext()
        }

        false
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        // empty
    }
}
