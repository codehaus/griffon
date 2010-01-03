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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.TabPane
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.TabPaneListener

/**
 * @author Andres Almiray
 */
class TabPaneListenerAdapter extends BuilderDelegate implements TabPaneListener {
    private Closure onTabInserted
    private Closure onTabsRemoved
    private Closure onCornerChanged
 
    TabPaneListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTabInserted(Closure callback) {
        onTabInserted = callback
        onTabInserted.delegate = this
    }

    void onTabsRemoved(Closure callback) {
        onTabsRemoved = callback
        onTabsRemoved.delegate = this
    }

    void onCornerChanged(Closure callback) {
        onCornerChanged = callback
        onCornerChanged.delegate = this
    }

    void tabInserted(TabPane arg0, int arg1) {
        if(onTabInserted) onTabInserted(arg0, arg1)
    }

    void tabsRemoved(TabPane arg0, int arg1, Sequence arg2) {
        if(onTabsRemoved) onTabsRemoved(arg0, arg1, arg2)
    }

    void cornerChanged(TabPane arg0, Component arg1) {
        if(onCornerChanged) onCornerChanged(arg0, arg1)
    }
}