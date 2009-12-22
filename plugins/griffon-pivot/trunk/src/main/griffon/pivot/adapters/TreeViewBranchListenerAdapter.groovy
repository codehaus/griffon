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
import org.apache.pivot.wtk.TreeViewBranchListener

/**
 * @author Andres Almiray
 */
class TreeViewBranchListenerAdapter extends BuilderDelegate implements TreeViewBranchListener {
    private Closure onBranchExpanded
    private Closure onBranchCollapsed
 
    TreeViewBranchListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onBranchExpanded(Closure callback) {
        onBranchExpanded = callback
        onBranchExpanded.delegate = this
    }

    void onBranchCollapsed(Closure callback) {
        onBranchCollapsed = callback
        onBranchCollapsed.delegate = this
    }

    void branchExpanded(TreeView arg0, Path arg1) {
        if(onBranchExpanded) onBranchExpanded(arg0, arg1)
    }

    void branchCollapsed(TreeView arg0, Path arg1) {
        if(onBranchCollapsed) onBranchCollapsed(arg0, arg1)
    }
}