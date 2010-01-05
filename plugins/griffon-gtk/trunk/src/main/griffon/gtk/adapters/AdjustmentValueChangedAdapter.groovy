/*
* Copyright 2009-2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
 
package griffon.gtk.adapters

import griffon.gtk.impl.BuilderDelegate
import org.gnome.gtk.Adjustment
import org.gnome.gtk.Adjustment.ValueChanged

/**
 * @author Andres Almiray
 */
class AdjustmentValueChangedAdapter extends BuilderDelegate implements ValueChanged {
    private Closure onValueChangedClosure

    AdjustmentValueChangedAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onValueChanged(Closure callback) {
        onValueChangedClosure = callback
        onValueChangedClosure.delegate = this
    }

    void onValueChanged(Adjustment arg0) {
        if(onValueChangedClosure) onValueChangedClosure.call(arg0)
    }
}