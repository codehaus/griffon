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
import org.apache.pivot.collections.List
import org.apache.pivot.wtk.Spinner.ItemRenderer
import org.apache.pivot.wtk.SpinnerListener

/**
 * @author Andres Almiray
 */
class SpinnerListenerAdapter extends BuilderDelegate implements SpinnerListener {
    private Closure onItemRendererChanged
    private Closure onSelectedItemKeyChanged
    private Closure onSpinnerDataChanged
    private Closure onCircularChanged
 
    SpinnerListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onItemRendererChanged(Closure callback) {
        onItemRendererChanged = callback
        onItemRendererChanged.delegate = this
    }

    void onSelectedItemKeyChanged(Closure callback) {
        onSelectedItemKeyChanged = callback
        onSelectedItemKeyChanged.delegate = this
    }

    void onSpinnerDataChanged(Closure callback) {
        onSpinnerDataChanged = callback
        onSpinnerDataChanged.delegate = this
    }

    void onCircularChanged(Closure callback) {
        onCircularChanged = callback
        onCircularChanged.delegate = this
    }

    void itemRendererChanged(Spinner arg0, ItemRenderer arg1) {
        if(onItemRendererChanged) onItemRendererChanged(arg0, arg1)
    }

    void selectedItemKeyChanged(Spinner arg0, String arg1) {
        if(onSelectedItemKeyChanged) onSelectedItemKeyChanged(arg0, arg1)
    }

    void spinnerDataChanged(Spinner arg0, List arg1) {
        if(onSpinnerDataChanged) onSpinnerDataChanged(arg0, arg1)
    }

    void circularChanged(Spinner arg0) {
        if(onCircularChanged) onCircularChanged(arg0)
    }
}