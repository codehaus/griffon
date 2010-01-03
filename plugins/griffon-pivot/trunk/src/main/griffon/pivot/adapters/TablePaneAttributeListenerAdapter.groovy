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
import org.apache.pivot.wtk.TablePane
import org.apache.pivot.wtk.TablePaneAttributeListener

/**
 * @author Andres Almiray
 */
class TablePaneAttributeListenerAdapter extends BuilderDelegate implements TablePaneAttributeListener {
    private Closure onRowSpanChanged
    private Closure onColumnSpanChanged
 
    TablePaneAttributeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRowSpanChanged(Closure callback) {
        onRowSpanChanged = callback
        onRowSpanChanged.delegate = this
    }

    void onColumnSpanChanged(Closure callback) {
        onColumnSpanChanged = callback
        onColumnSpanChanged.delegate = this
    }

    void rowSpanChanged(TablePane arg0, Component arg1, int arg2) {
        if(onRowSpanChanged) onRowSpanChanged(arg0, arg1, arg2)
    }

    void columnSpanChanged(TablePane arg0, Component arg1, int arg2) {
        if(onColumnSpanChanged) onColumnSpanChanged(arg0, arg1, arg2)
    }
}