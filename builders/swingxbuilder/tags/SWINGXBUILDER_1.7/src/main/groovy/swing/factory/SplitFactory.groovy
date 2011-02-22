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

import groovy.swing.SwingBuilder
import org.jdesktop.swingx.JXMultiSplitPane
import org.jdesktop.swingx.MultiSplitLayout.Leaf
import org.jdesktop.swingx.MultiSplitLayout.Split
import org.jdesktop.swingx.MultiSplitLayout.RowSplit

public class SplitFactory extends AbstractFactory {
	def children = []
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
        if (SwingBuilder.checkValueIsType(value, name, Split.class)) {
            return value
        }
		println properties
		
		def split
		def rowLayout = properties.remove("rowLayout")
		if (rowLayout !=null) {
			if (rowLayout == false) {
				split = new Split()
			} else {
				split = new RowSplit()
			}
		} else split = new Split()
        
        return split
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
		node.setChildren(children)
		println parent + " | " + node
        if (parent instanceof JXMultiSplitPane) {
            parent.getMultiSplitLayout().setModel(node)
        }
    }
	
	public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
		println "parent:"+ parent+" child:"+child
		def children = parent.getChildren()
		children.add(child)
		parent.setChildren(children)
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
		println "child:"+child
		children.add(child)
    }

}
