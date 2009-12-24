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
import org.apache.pivot.wtk.text.Element
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.text.ElementListener

/**
 * @author Andres Almiray
 */
class ElementListenerAdapter extends BuilderDelegate implements ElementListener {
    private Closure onNodeInserted
    private Closure onNodesRemoved
 
    ElementListenerAdapter(FactoryBuilderSupport builder) {
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

    void nodeInserted(Element arg0, int arg1) {
        if(onNodeInserted) onNodeInserted(arg0, arg1)
    }

    void nodesRemoved(Element arg0, int arg1, Sequence arg2) {
        if(onNodesRemoved) onNodesRemoved(arg0, arg1, arg2)
    }
}