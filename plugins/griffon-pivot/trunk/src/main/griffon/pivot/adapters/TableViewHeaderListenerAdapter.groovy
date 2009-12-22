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
import org.apache.pivot.wtk.TableView
import org.apache.pivot.wtk.TableViewHeader
import org.apache.pivot.wtk.TableViewHeader.SortMode
import org.apache.pivot.wtk.TableViewHeader.DataRenderer
import org.apache.pivot.wtk.TableViewHeaderListener

/**
 * @author Andres Almiray
 */
class TableViewHeaderListenerAdapter extends BuilderDelegate implements TableViewHeaderListener {
    private Closure onDataRendererChanged
    private Closure onTableViewChanged
    private Closure onSortModeChanged
 
    TableViewHeaderListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onDataRendererChanged(Closure callback) {
        onDataRendererChanged = callback
        onDataRendererChanged.delegate = this
    }

    void onTableViewChanged(Closure callback) {
        onTableViewChanged = callback
        onTableViewChanged.delegate = this
    }

    void onSortModeChanged(Closure callback) {
        onSortModeChanged = callback
        onSortModeChanged.delegate = this
    }

    void dataRendererChanged(TableViewHeader arg0, DataRenderer arg1) {
        if(onDataRendererChanged) onDataRendererChanged(arg0, arg1)
    }

    void tableViewChanged(TableViewHeader arg0, TableView arg1) {
        if(onTableViewChanged) onTableViewChanged(arg0, arg1)
    }

    void sortModeChanged(TableViewHeader arg0, SortMode arg1) {
        if(onSortModeChanged) onSortModeChanged(arg0, arg1)
    }
}