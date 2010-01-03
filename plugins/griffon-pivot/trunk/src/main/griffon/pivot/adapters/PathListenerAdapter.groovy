/*
 * Copyright 2009-2010 the original author or authors.
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
import org.apache.pivot.wtk.media.drawing.Path
import org.apache.pivot.wtk.media.drawing.Path.Operation
import org.apache.pivot.wtk.media.drawing.Path.WindingRule
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.media.drawing.PathListener

/**
 * @author Andres Almiray
 */
class PathListenerAdapter extends BuilderDelegate implements PathListener {
    private Closure onWindingRuleChanged
    private Closure onOperationInserted
    private Closure onOperationsRemoved
    private Closure onOperationUpdated
 
    PathListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onWindingRuleChanged(Closure callback) {
        onWindingRuleChanged = callback
        onWindingRuleChanged.delegate = this
    }

    void onOperationInserted(Closure callback) {
        onOperationInserted = callback
        onOperationInserted.delegate = this
    }

    void onOperationsRemoved(Closure callback) {
        onOperationsRemoved = callback
        onOperationsRemoved.delegate = this
    }

    void onOperationUpdated(Closure callback) {
        onOperationUpdated = callback
        onOperationUpdated.delegate = this
    }

    void windingRuleChanged(Path arg0, WindingRule arg1) {
        if(onWindingRuleChanged) onWindingRuleChanged(arg0, arg1)
    }

    void operationInserted(Path arg0, int arg1) {
        if(onOperationInserted) onOperationInserted(arg0, arg1)
    }

    void operationsRemoved(Path arg0, int arg1, Sequence arg2) {
        if(onOperationsRemoved) onOperationsRemoved(arg0, arg1, arg2)
    }

    void operationUpdated(Operation arg0) {
        if(onOperationUpdated) onOperationUpdated(arg0)
    }
}