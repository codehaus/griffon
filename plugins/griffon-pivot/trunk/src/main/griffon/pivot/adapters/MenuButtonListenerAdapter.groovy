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
import org.apache.pivot.wtk.MenuButton
import org.apache.pivot.wtk.MenuButtonListener

/**
 * @author Andres Almiray
 */
class MenuButtonListenerAdapter extends BuilderDelegate implements MenuButtonListener {
    private Closure onMenuChanged
    private Closure onRepeatableChanged
 
    MenuButtonListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onMenuChanged(Closure callback) {
        onMenuChanged = callback
        onMenuChanged.delegate = this
    }

    void onRepeatableChanged(Closure callback) {
        onRepeatableChanged = callback
        onRepeatableChanged.delegate = this
    }

    void menuChanged(MenuButton arg0, Menu arg1) {
        if(onMenuChanged) onMenuChanged(arg0, arg1)
    }

    void repeatableChanged(MenuButton arg0) {
        if(onRepeatableChanged) onRepeatableChanged(arg0)
    }
}