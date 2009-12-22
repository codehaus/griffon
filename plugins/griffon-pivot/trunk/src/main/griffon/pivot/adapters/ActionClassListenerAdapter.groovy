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
import org.apache.pivot.wtk.Action
import org.apache.pivot.wtk.ActionClassListener

/**
 * @author Andres Almiray
 */
class ActionClassListenerAdapter extends BuilderDelegate implements ActionClassListener {
    private Closure onActionAdded
    private Closure onActionUpdated
    private Closure onActionRemoved
 
    ActionClassListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onActionAdded(Closure callback) {
        onActionAdded = callback
        onActionAdded.delegate = this
    }

    void onActionUpdated(Closure callback) {
        onActionUpdated = callback
        onActionUpdated.delegate = this
    }

    void onActionRemoved(Closure callback) {
        onActionRemoved = callback
        onActionRemoved.delegate = this
    }

    void actionAdded(String arg0) {
        if(onActionAdded) onActionAdded(arg0)
    }

    void actionUpdated(String arg0, Action arg1) {
        if(onActionUpdated) onActionUpdated(arg0, arg1)
    }

    void actionRemoved(String arg0, Action arg1) {
        if(onActionRemoved) onActionRemoved(arg0, arg1)
    }
}