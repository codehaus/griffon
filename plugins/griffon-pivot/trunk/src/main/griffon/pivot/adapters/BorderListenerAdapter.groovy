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
import org.apache.pivot.wtk.Border
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.BorderListener

/**
 * @author Andres Almiray
 */
class BorderListenerAdapter extends BuilderDelegate implements BorderListener {
    private Closure onTitleChanged
    private Closure onContentChanged
 
    BorderListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTitleChanged(Closure callback) {
        onTitleChanged = callback
        onTitleChanged.delegate = this
    }

    void onContentChanged(Closure callback) {
        onContentChanged = callback
        onContentChanged.delegate = this
    }

    void titleChanged(Border arg0, String arg1) {
        if(onTitleChanged) onTitleChanged(arg0, arg1)
    }

    void contentChanged(Border arg0, Component arg1) {
        if(onContentChanged) onContentChanged(arg0, arg1)
    }
}