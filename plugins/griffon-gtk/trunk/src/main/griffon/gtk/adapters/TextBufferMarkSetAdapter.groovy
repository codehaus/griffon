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
import org.gnome.gtk.TextBuffer
import org.gnome.gtk.TextIter
import org.gnome.gtk.TextMark
import org.gnome.gtk.TextBuffer.MarkSet

/**
 * @author Andres Almiray
 */
class TextBufferMarkSetAdapter extends BuilderDelegate implements MarkSet {
    private Closure onMarkSetClosure

    TextBufferMarkSetAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onMarkSet(Closure callback) {
        onMarkSetClosure = callback
        onMarkSetClosure.delegate = this
    }

    void onMarkSet(TextBuffer arg0, TextIter arg1, TextMark arg2) {
        if(onMarkSetClosure) onMarkSetClosure.call(arg0, arg1, arg2)
    }
}