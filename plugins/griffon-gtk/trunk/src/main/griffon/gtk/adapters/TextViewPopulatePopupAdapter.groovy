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
import org.gnome.gtk.Menu
import org.gnome.gtk.TextView
import org.gnome.gtk.TextView.PopulatePopup

/**
 * @author Andres Almiray
 */
class TextViewPopulatePopupAdapter extends BuilderDelegate implements PopulatePopup {
    private Closure onPopulatePopupClosure

    TextViewPopulatePopupAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPopulatePopup(Closure callback) {
        onPopulatePopupClosure = callback
        onPopulatePopupClosure.delegate = this
    }

    void onPopulatePopup(TextView arg0, Menu arg1) {
        if(onPopulatePopupClosure) onPopulatePopupClosure.call(arg0, arg1)
    }
}