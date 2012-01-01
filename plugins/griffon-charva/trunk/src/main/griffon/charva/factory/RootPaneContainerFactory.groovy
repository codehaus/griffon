/*
 * Copyright 2003-2010 the original author or authors.
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

package griffon.charva.factory

import charva.awt.Component
import charva.awt.Window
import charvax.swing.JButton

abstract class RootPaneContainerFactory extends AbstractCharvaFactory {
    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        println ">>> adding $child to $parent"
        if (!(child instanceof Component) || (child instanceof Window)) {
            return;
        }
        try {
            def constraints = builder.context.constraints
            if (constraints != null) {
                parent.contentPane.add(child, constraints)
                builder.context.remove('constraints')
            } else {
                parent.contentPane.add(child)
            }
        } catch (MissingPropertyException mpe) {
            parent.contentPane.add(child)
        }
    }

    public void handleRootPaneTasks(FactoryBuilderSupport builder, Window container, Map attributes) {
        builder.context.pack = attributes.remove('pack')
        builder.context.show = attributes.remove('show')
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (builder.context.pack) {
            node.pack()
        }
        if (builder.context.show) {
            node.visible = true
        }
    }
}
