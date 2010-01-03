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
import org.apache.pivot.wtk.ButtonPressListener

/**
 * @author Andres Almiray
 */
class ButtonPressListenerAdapter extends BuilderDelegate implements ButtonPressListener {
    private Closure onButtonPressed
 
    ButtonPressListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onButtonPressed(Closure callback) {
        onButtonPressed = callback
        onButtonPressed.delegate = this
    }

    void buttonPressed(Button arg0) {
        if(onButtonPressed) onButtonPressed(arg0)
    }
}