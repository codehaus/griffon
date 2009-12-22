/*
 * Copyright 2009 the original author or authors.
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
import org.apache.pivot.wtk.Orientation
import org.apache.pivot.wtk.ScrollBar
import org.apache.pivot.wtk.ScrollBarListener

/**
 * @author Andres Almiray
 */
class ScrollBarListenerAdapter extends BuilderDelegate implements ScrollBarListener {
    private Closure onOrientationChanged
    private Closure onScopeChanged
    private Closure onUnitIncrementChanged
    private Closure onBlockIncrementChanged
 
    ScrollBarListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onOrientationChanged(Closure callback) {
        onOrientationChanged = callback
        onOrientationChanged.delegate = this
    }

    void onScopeChanged(Closure callback) {
        onScopeChanged = callback
        onScopeChanged.delegate = this
    }

    void onUnitIncrementChanged(Closure callback) {
        onUnitIncrementChanged = callback
        onUnitIncrementChanged.delegate = this
    }

    void onBlockIncrementChanged(Closure callback) {
        onBlockIncrementChanged = callback
        onBlockIncrementChanged.delegate = this
    }

    void orientationChanged(ScrollBar arg0, Orientation arg1) {
        if(onOrientationChanged) onOrientationChanged(arg0, arg1)
    }

    void scopeChanged(ScrollBar arg0, int arg1, int arg2, int arg3) {
        if(onScopeChanged) onScopeChanged(arg0, arg1, arg2, arg3)
    }

    void unitIncrementChanged(ScrollBar arg0, int arg1) {
        if(onUnitIncrementChanged) onUnitIncrementChanged(arg0, arg1)
    }

    void blockIncrementChanged(ScrollBar arg0, int arg1) {
        if(onBlockIncrementChanged) onBlockIncrementChanged(arg0, arg1)
    }
}