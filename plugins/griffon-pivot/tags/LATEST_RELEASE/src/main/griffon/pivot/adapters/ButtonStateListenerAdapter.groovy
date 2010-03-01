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
import org.apache.pivot.wtk.Button
import org.apache.pivot.wtk.Button.State
import org.apache.pivot.wtk.ButtonStateListener

/**
 * @author Andres Almiray
 */
class ButtonStateListenerAdapter extends BuilderDelegate implements ButtonStateListener {
    private Closure onStateChanged
 
    ButtonStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onStateChanged(Closure callback) {
        onStateChanged = callback
        onStateChanged.delegate = this
    }

    void stateChanged(Button arg0, State arg1) {
        if(onStateChanged) onStateChanged(arg0, arg1)
    }
}
