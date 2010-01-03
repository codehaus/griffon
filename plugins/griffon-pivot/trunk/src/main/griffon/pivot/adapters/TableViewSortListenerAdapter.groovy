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
import org.apache.pivot.wtk.SortDirection
import org.apache.pivot.wtk.TableView
import org.apache.pivot.wtk.TableViewSortListener

/**
 * @author Andres Almiray
 */
class TableViewSortListenerAdapter extends BuilderDelegate implements TableViewSortListener {
    private Closure onSortChanged
    private Closure onSortAdded
    private Closure onSortUpdated
    private Closure onSortRemoved
 
    TableViewSortListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSortChanged(Closure callback) {
        onSortChanged = callback
        onSortChanged.delegate = this
    }

    void onSortAdded(Closure callback) {
        onSortAdded = callback
        onSortAdded.delegate = this
    }

    void onSortUpdated(Closure callback) {
        onSortUpdated = callback
        onSortUpdated.delegate = this
    }

    void onSortRemoved(Closure callback) {
        onSortRemoved = callback
        onSortRemoved.delegate = this
    }

    void sortChanged(TableView arg0) {
        if(onSortChanged) onSortChanged(arg0)
    }

    void sortAdded(TableView arg0, String arg1) {
        if(onSortAdded) onSortAdded(arg0, arg1)
    }

    void sortUpdated(TableView arg0, String arg1, SortDirection arg2) {
        if(onSortUpdated) onSortUpdated(arg0, arg1, arg2)
    }

    void sortRemoved(TableView arg0, String arg1, SortDirection arg2) {
        if(onSortRemoved) onSortRemoved(arg0, arg1, arg2)
    }
}