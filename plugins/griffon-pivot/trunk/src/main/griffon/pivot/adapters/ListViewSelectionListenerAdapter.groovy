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
import org.apache.pivot.wtk.ListView
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.ListViewSelectionListener

/**
 * @author Andres Almiray
 */
class ListViewSelectionListenerAdapter extends BuilderDelegate implements ListViewSelectionListener {
    private Closure onSelectedRangesChanged
    private Closure onSelectedRangeAdded
    private Closure onSelectedRangeRemoved
 
    ListViewSelectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectedRangesChanged(Closure callback) {
        onSelectedRangesChanged = callback
        onSelectedRangesChanged.delegate = this
    }

    void onSelectedRangeAdded(Closure callback) {
        onSelectedRangeAdded = callback
        onSelectedRangeAdded.delegate = this
    }

    void onSelectedRangeRemoved(Closure callback) {
        onSelectedRangeRemoved = callback
        onSelectedRangeRemoved.delegate = this
    }

    void selectedRangesChanged(ListView arg0, Sequence arg1) {
        if(onSelectedRangesChanged) onSelectedRangesChanged(arg0, arg1)
    }

    void selectedRangeAdded(ListView arg0, int arg1, int arg2) {
        if(onSelectedRangeAdded) onSelectedRangeAdded(arg0, arg1, arg2)
    }

    void selectedRangeRemoved(ListView arg0, int arg1, int arg2) {
        if(onSelectedRangeRemoved) onSelectedRangeRemoved(arg0, arg1, arg2)
    }
}