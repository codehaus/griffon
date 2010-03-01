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
import org.gnome.gtk.RadioButton
import org.gnome.gtk.RadioButtonGroup.GroupToggled

/**
 * @author Andres Almiray
 */
class RadioButtonGroupGroupToggledAdapter extends BuilderDelegate implements GroupToggled {
    private Closure onGroupToggledClosure

    RadioButtonGroupGroupToggledAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onGroupToggled(Closure callback) {
        onGroupToggledClosure = callback
        onGroupToggledClosure.delegate = this
    }

    void onGroupToggled(RadioButton arg0) {
        if(onGroupToggledClosure) onGroupToggledClosure.call(arg0)
    }
}