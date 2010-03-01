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
import org.gnome.gtk.EntryCompletion
import org.gnome.gtk.TreeIter
import org.gnome.gtk.TreeModel
import org.gnome.gtk.EntryCompletion.CursorOnMatch

/**
 * @author Andres Almiray
 */
class EntryCompletionCursorOnMatchAdapter extends BuilderDelegate implements CursorOnMatch {
    private Closure onCursorOnMatchClosure

    EntryCompletionCursorOnMatchAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onCursorOnMatch(Closure callback) {
        onCursorOnMatchClosure = callback
        onCursorOnMatchClosure.delegate = this
    }

    boolean onCursorOnMatch(EntryCompletion arg0, TreeModel arg1, TreeIter arg2) {
        if(onCursorOnMatchClosure) onCursorOnMatchClosure.call(arg0, arg1, arg2)
    }
}