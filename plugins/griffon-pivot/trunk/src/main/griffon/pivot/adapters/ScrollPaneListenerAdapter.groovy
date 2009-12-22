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
import org.apache.pivot.wtk.ScrollPane
import org.apache.pivot.wtk.ScrollPane.ScrollBarPolicy
import org.apache.pivot.wtk.ScrollPaneListener

/**
 * @author Andres Almiray
 */
class ScrollPaneListenerAdapter extends BuilderDelegate implements ScrollPaneListener {
    private Closure onCornerChanged
    private Closure onHorizontalScrollBarPolicyChanged
    private Closure onVerticalScrollBarPolicyChanged
    private Closure onRowHeaderChanged
    private Closure onColumnHeaderChanged
 
    ScrollPaneListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onCornerChanged(Closure callback) {
        onCornerChanged = callback
        onCornerChanged.delegate = this
    }

    void onHorizontalScrollBarPolicyChanged(Closure callback) {
        onHorizontalScrollBarPolicyChanged = callback
        onHorizontalScrollBarPolicyChanged.delegate = this
    }

    void onVerticalScrollBarPolicyChanged(Closure callback) {
        onVerticalScrollBarPolicyChanged = callback
        onVerticalScrollBarPolicyChanged.delegate = this
    }

    void onRowHeaderChanged(Closure callback) {
        onRowHeaderChanged = callback
        onRowHeaderChanged.delegate = this
    }

    void onColumnHeaderChanged(Closure callback) {
        onColumnHeaderChanged = callback
        onColumnHeaderChanged.delegate = this
    }

    void cornerChanged(ScrollPane arg0, Component arg1) {
        if(onCornerChanged) onCornerChanged(arg0, arg1)
    }

    void horizontalScrollBarPolicyChanged(ScrollPane arg0, ScrollBarPolicy arg1) {
        if(onHorizontalScrollBarPolicyChanged) onHorizontalScrollBarPolicyChanged(arg0, arg1)
    }

    void verticalScrollBarPolicyChanged(ScrollPane arg0, ScrollBarPolicy arg1) {
        if(onVerticalScrollBarPolicyChanged) onVerticalScrollBarPolicyChanged(arg0, arg1)
    }

    void rowHeaderChanged(ScrollPane arg0, Component arg1) {
        if(onRowHeaderChanged) onRowHeaderChanged(arg0, arg1)
    }

    void columnHeaderChanged(ScrollPane arg0, Component arg1) {
        if(onColumnHeaderChanged) onColumnHeaderChanged(arg0, arg1)
    }
}