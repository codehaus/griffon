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
import org.gnome.gtk.Dialog
import org.gnome.gtk.ResponseType
import org.gnome.gtk.Dialog.Response

/**
 * @author Andres Almiray
 */
class DialogResponseAdapter extends BuilderDelegate implements Response {
    private Closure onResponseClosure

    DialogResponseAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onResponse(Closure callback) {
        onResponseClosure = callback
        onResponseClosure.delegate = this
    }

    void onResponse(Dialog arg0, ResponseType arg1) {
        if(onResponseClosure) onResponseClosure.call(arg0, arg1)
    }
}