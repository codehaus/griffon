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
import org.apache.pivot.wtk.Container
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.ContainerListener
import org.apache.pivot.wtk.FocusTraversalPolicy

/**
 * @author Andres Almiray
 */
class ContainerListenerAdapter extends BuilderDelegate implements ContainerListener {
    private Closure onComponentInserted
    private Closure onComponentsRemoved
    private Closure onFocusTraversalPolicyChanged
    private Closure onContextKeyChanged
    private Closure onComponentMoved
 
    ContainerListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onComponentInserted(Closure callback) {
        onComponentInserted = callback
        onComponentInserted.delegate = this
    }

    void onComponentsRemoved(Closure callback) {
        onComponentsRemoved = callback
        onComponentsRemoved.delegate = this
    }

    void onFocusTraversalPolicyChanged(Closure callback) {
        onFocusTraversalPolicyChanged = callback
        onFocusTraversalPolicyChanged.delegate = this
    }

    void onContextKeyChanged(Closure callback) {
        onContextKeyChanged = callback
        onContextKeyChanged.delegate = this
    }

    void onComponentMoved(Closure callback) {
        onComponentMoved = callback
        onComponentMoved.delegate = this
    }

    void componentInserted(Container arg0, int arg1) {
        if(onComponentInserted) onComponentInserted(arg0, arg1)
    }

    void componentsRemoved(Container arg0, int arg1, Sequence arg2) {
        if(onComponentsRemoved) onComponentsRemoved(arg0, arg1, arg2)
    }

    void focusTraversalPolicyChanged(Container arg0, FocusTraversalPolicy arg1) {
        if(onFocusTraversalPolicyChanged) onFocusTraversalPolicyChanged(arg0, arg1)
    }

    void contextKeyChanged(Container arg0, String arg1) {
        if(onContextKeyChanged) onContextKeyChanged(arg0, arg1)
    }

    void componentMoved(Container arg0, int arg1, int arg2) {
        if(onComponentMoved) onComponentMoved(arg0, arg1, arg2)
    }
}