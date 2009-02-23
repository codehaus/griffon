/*
 * $Id:  $
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
 *
 */

package groovy.swing.factory

import groovy.swing.GroovySplit
import groovy.swing.SwingBuilder
import org.jdesktop.swingx.JXMultiSplitPane
import org.jdesktop.swingx.MultiSplitLayout.Leaf


public class SplitFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
        if (SwingBuilder.checkValueIsType(value, name, GroovySplit.class)) {
            return value
        }
        def split = new GroovySplit(new Leaf("center"))
        def children = properties.remove("children")

        if (children != null && children.size() > 0) {
            if (children instanceof List)
                split = new GroovySplit(children)
        }
        return split
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        node.setChildren()
        if (parent instanceof JXMultiSplitPane) {
            parent.getMultiSplitLayout().setModel(node)
        }
    }


    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.nodes.add(child)
    }

}
