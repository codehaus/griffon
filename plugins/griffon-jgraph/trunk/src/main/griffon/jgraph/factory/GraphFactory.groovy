/*
 * Copyright 2010 the original author or authors.
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

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph

/**
 * @author Andres.Almiray
 */
class GraphFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof GString) value = value as String
        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, mxGraph)) {
            return value
        }
        new mxGraph()
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof mxGraphComponent) {
            parent.graph = child
        }
    }

    boolean isHandlesNodeChildren() {
        true
    }

    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        childContent.delegate = node
        try {
            node.model.beginUpdate()
            childContent()
        } finally {
            node.model.endUpdate()
        }

        false
    }
}
