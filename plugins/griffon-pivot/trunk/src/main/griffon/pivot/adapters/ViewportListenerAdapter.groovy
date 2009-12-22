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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.Viewport
import org.apache.pivot.wtk.ViewportListener

/**
 * @author Andres Almiray
 */
class ViewportListenerAdapter extends BuilderDelegate implements ViewportListener {
    private Closure onScrollTopChanged
    private Closure onScrollLeftChanged
    private Closure onViewChanged
 
    ViewportListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onScrollTopChanged(Closure callback) {
        onScrollTopChanged = callback
        onScrollTopChanged.delegate = this
    }

    void onScrollLeftChanged(Closure callback) {
        onScrollLeftChanged = callback
        onScrollLeftChanged.delegate = this
    }

    void onViewChanged(Closure callback) {
        onViewChanged = callback
        onViewChanged.delegate = this
    }

    void scrollTopChanged(Viewport arg0, int arg1) {
        if(onScrollTopChanged) onScrollTopChanged(arg0, arg1)
    }

    void scrollLeftChanged(Viewport arg0, int arg1) {
        if(onScrollLeftChanged) onScrollLeftChanged(arg0, arg1)
    }

    void viewChanged(Viewport arg0, Component arg1) {
        if(onViewChanged) onViewChanged(arg0, arg1)
    }
}