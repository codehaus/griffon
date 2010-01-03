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
import org.apache.pivot.wtk.Spinner
import org.apache.pivot.wtk.SpinnerItemListener

/**
 * @author Andres Almiray
 */
class SpinnerItemListenerAdapter extends BuilderDelegate implements SpinnerItemListener {
    private Closure onItemInserted
    private Closure onItemUpdated
    private Closure onItemsRemoved
    private Closure onItemsCleared
    private Closure onItemsSorted
 
    SpinnerItemListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onItemInserted(Closure callback) {
        onItemInserted = callback
        onItemInserted.delegate = this
    }

    void onItemUpdated(Closure callback) {
        onItemUpdated = callback
        onItemUpdated.delegate = this
    }

    void onItemsRemoved(Closure callback) {
        onItemsRemoved = callback
        onItemsRemoved.delegate = this
    }

    void onItemsCleared(Closure callback) {
        onItemsCleared = callback
        onItemsCleared.delegate = this
    }

    void onItemsSorted(Closure callback) {
        onItemsSorted = callback
        onItemsSorted.delegate = this
    }

    void itemInserted(Spinner arg0, int arg1) {
        if(onItemInserted) onItemInserted(arg0, arg1)
    }

    void itemUpdated(Spinner arg0, int arg1) {
        if(onItemUpdated) onItemUpdated(arg0, arg1)
    }

    void itemsRemoved(Spinner arg0, int arg1, int arg2) {
        if(onItemsRemoved) onItemsRemoved(arg0, arg1, arg2)
    }

    void itemsCleared(Spinner arg0) {
        if(onItemsCleared) onItemsCleared(arg0)
    }

    void itemsSorted(Spinner arg0) {
        if(onItemsSorted) onItemsSorted(arg0)
    }
}