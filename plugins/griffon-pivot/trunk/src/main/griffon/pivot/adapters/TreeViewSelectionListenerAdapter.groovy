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
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.TreeViewSelectionListener

/**
 * @author Andres Almiray
 */
class TreeViewSelectionListenerAdapter extends BuilderDelegate implements TreeViewSelectionListener {
    private Closure onSelectedPathsChanged
    private Closure onSelectedPathAdded
    private Closure onSelectedPathRemoved
 
    TreeViewSelectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectedPathsChanged(Closure callback) {
        onSelectedPathsChanged = callback
        onSelectedPathsChanged.delegate = this
    }

    void onSelectedPathAdded(Closure callback) {
        onSelectedPathAdded = callback
        onSelectedPathAdded.delegate = this
    }

    void onSelectedPathRemoved(Closure callback) {
        onSelectedPathRemoved = callback
        onSelectedPathRemoved.delegate = this
    }

    void selectedPathsChanged(TreeView arg0, Sequence arg1) {
        if(onSelectedPathsChanged) onSelectedPathsChanged(arg0, arg1)
    }

    void selectedPathAdded(TreeView arg0, Path arg1) {
        if(onSelectedPathAdded) onSelectedPathAdded(arg0, arg1)
    }

    void selectedPathRemoved(TreeView arg0, Path arg1) {
        if(onSelectedPathRemoved) onSelectedPathRemoved(arg0, arg1)
    }
}