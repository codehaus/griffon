/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.collections.Sequence$Tree.Path
import org.apache.pivot.wtk.TreeView
import org.apache.pivot.wtk.TreeViewNodeListener

/**
 * @author Andres Almiray
 */
class TreeViewNodeListenerAdapter extends BuilderDelegate implements TreeViewNodeListener {
    private Closure onNodeInserted
    private Closure onNodesRemoved
    private Closure onNodeUpdated
    private Closure onNodesCleared
    private Closure onNodesSorted
 
    TreeViewNodeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onNodeInserted(Closure callback) {
        onNodeInserted = callback
        onNodeInserted.delegate = this
    }

    void onNodesRemoved(Closure callback) {
        onNodesRemoved = callback
        onNodesRemoved.delegate = this
    }

    void onNodeUpdated(Closure callback) {
        onNodeUpdated = callback
        onNodeUpdated.delegate = this
    }

    void onNodesCleared(Closure callback) {
        onNodesCleared = callback
        onNodesCleared.delegate = this
    }

    void onNodesSorted(Closure callback) {
        onNodesSorted = callback
        onNodesSorted.delegate = this
    }

    void nodeInserted(TreeView arg0, Path arg1, int arg2) {
        if(onNodeInserted) onNodeInserted(arg0, arg1, arg2)
    }

    void nodesRemoved(TreeView arg0, Path arg1, int arg2, int arg3) {
        if(onNodesRemoved) onNodesRemoved(arg0, arg1, arg2, arg3)
    }

    void nodeUpdated(TreeView arg0, Path arg1, int arg2) {
        if(onNodeUpdated) onNodeUpdated(arg0, arg1, arg2)
    }

    void nodesCleared(TreeView arg0, Path arg1) {
        if(onNodesCleared) onNodesCleared(arg0, arg1)
    }

    void nodesSorted(TreeView arg0, Path arg1) {
        if(onNodesSorted) onNodesSorted(arg0, arg1)
    }
}