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
import org.apache.pivot.wtk.text.Node
import org.apache.pivot.wtk.text.NodeListener

/**
 * @author Andres Almiray
 */
class NodeListenerAdapter extends BuilderDelegate implements NodeListener {
    private Closure onRangeInserted
    private Closure onRangeRemoved
    private Closure onParentChanged
    private Closure onOffsetChanged
 
    NodeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRangeInserted(Closure callback) {
        onRangeInserted = callback
        onRangeInserted.delegate = this
    }

    void onRangeRemoved(Closure callback) {
        onRangeRemoved = callback
        onRangeRemoved.delegate = this
    }

    void onParentChanged(Closure callback) {
        onParentChanged = callback
        onParentChanged.delegate = this
    }

    void onOffsetChanged(Closure callback) {
        onOffsetChanged = callback
        onOffsetChanged.delegate = this
    }

    void rangeInserted(Node arg0, int arg1, int arg2) {
        if(onRangeInserted) onRangeInserted(arg0, arg1, arg2)
    }

    void rangeRemoved(Node arg0, int arg1, int arg2) {
        if(onRangeRemoved) onRangeRemoved(arg0, arg1, arg2)
    }

    void parentChanged(Node arg0, Element arg1) {
        if(onParentChanged) onParentChanged(arg0, arg1)
    }

    void offsetChanged(Node arg0, int arg1) {
        if(onOffsetChanged) onOffsetChanged(arg0, arg1)
    }
}