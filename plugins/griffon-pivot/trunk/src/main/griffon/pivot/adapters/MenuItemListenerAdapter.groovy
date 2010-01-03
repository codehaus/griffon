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
import org.apache.pivot.wtk.Menu
import org.apache.pivot.wtk.Menu.Item
import org.apache.pivot.wtk.Menu.ItemListener

/**
 * @author Andres Almiray
 */
class MenuItemListenerAdapter extends BuilderDelegate implements Menu.ItemListener {
    private Closure onActiveChanged
    private Closure onMenuChanged
    private Closure onNameChanged
 
    MenuItemListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onActiveChanged(Closure callback) {
        onActiveChanged = callback
        onActiveChanged.delegate = this
    }

    void onMenuChanged(Closure callback) {
        onMenuChanged = callback
        onMenuChanged.delegate = this
    }

    void onNameChanged(Closure callback) {
        onNameChanged = callback
        onNameChanged.delegate = this
    }

    void activeChanged(Item arg0) {
        if(onActiveChanged) onActiveChanged(arg0)
    }

    void menuChanged(Item arg0, Menu arg1) {
        if(onMenuChanged) onMenuChanged(arg0, arg1)
    }

    void nameChanged(Item arg0, String arg1) {
        if(onNameChanged) onNameChanged(arg0, arg1)
    }
}