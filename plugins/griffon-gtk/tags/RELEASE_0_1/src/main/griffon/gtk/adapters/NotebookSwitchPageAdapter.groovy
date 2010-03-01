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
import org.gnome.gtk.Notebook
import org.gnome.gtk.Notebook.SwitchPage

/**
 * @author Andres Almiray
 */
class NotebookSwitchPageAdapter extends BuilderDelegate implements SwitchPage {
    private Closure onSwitchPageClosure

    NotebookSwitchPageAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSwitchPage(Closure callback) {
        onSwitchPageClosure = callback
        onSwitchPageClosure.delegate = this
    }

    void onSwitchPage(Notebook arg0, int arg1) {
        if(onSwitchPageClosure) onSwitchPageClosure.call(arg0, arg1)
    }
}