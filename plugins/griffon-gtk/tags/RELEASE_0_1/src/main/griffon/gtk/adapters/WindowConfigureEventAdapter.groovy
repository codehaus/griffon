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
import org.gnome.gdk.EventConfigure
import org.gnome.gtk.Widget
import org.gnome.gtk.Window.ConfigureEvent

/**
 * @author Andres Almiray
 */
class WindowConfigureEventAdapter extends BuilderDelegate implements ConfigureEvent {
    private Closure onConfigureEventClosure

    WindowConfigureEventAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onConfigureEvent(Closure callback) {
        onConfigureEventClosure = callback
        onConfigureEventClosure.delegate = this
    }

    boolean onConfigureEvent(Widget arg0, EventConfigure arg1) {
        if(onConfigureEventClosure) onConfigureEventClosure.call(arg0, arg1)
    }
}