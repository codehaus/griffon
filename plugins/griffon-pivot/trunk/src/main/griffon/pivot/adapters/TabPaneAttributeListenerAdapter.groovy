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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.TabPane
import org.apache.pivot.wtk.media.Image
import org.apache.pivot.wtk.TabPaneAttributeListener

/**
 * @author Andres Almiray
 */
class TabPaneAttributeListenerAdapter extends BuilderDelegate implements TabPaneAttributeListener {
    private Closure onIconChanged
    private Closure onLabelChanged
    private Closure onCloseableChanged
 
    TabPaneAttributeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onIconChanged(Closure callback) {
        onIconChanged = callback
        onIconChanged.delegate = this
    }

    void onLabelChanged(Closure callback) {
        onLabelChanged = callback
        onLabelChanged.delegate = this
    }

    void onCloseableChanged(Closure callback) {
        onCloseableChanged = callback
        onCloseableChanged.delegate = this
    }

    void iconChanged(TabPane arg0, Component arg1, Image arg2) {
        if(onIconChanged) onIconChanged(arg0, arg1, arg2)
    }

    void labelChanged(TabPane arg0, Component arg1, String arg2) {
        if(onLabelChanged) onLabelChanged(arg0, arg1, arg2)
    }

    void closeableChanged(TabPane arg0, Component arg1) {
        if(onCloseableChanged) onCloseableChanged(arg0, arg1)
    }
}