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
import org.apache.pivot.wtk.media.drawing.Arc
import org.apache.pivot.wtk.media.drawing.Arc.Type
import org.apache.pivot.wtk.media.drawing.ArcListener

/**
 * @author Andres Almiray
 */
class ArcListenerAdapter extends BuilderDelegate implements ArcListener {
    private Closure onStartChanged
    private Closure onExtentChanged
    private Closure onTypeChanged
    private Closure onSizeChanged
 
    ArcListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onStartChanged(Closure callback) {
        onStartChanged = callback
        onStartChanged.delegate = this
    }

    void onExtentChanged(Closure callback) {
        onExtentChanged = callback
        onExtentChanged.delegate = this
    }

    void onTypeChanged(Closure callback) {
        onTypeChanged = callback
        onTypeChanged.delegate = this
    }

    void onSizeChanged(Closure callback) {
        onSizeChanged = callback
        onSizeChanged.delegate = this
    }

    void startChanged(Arc arg0, float arg1) {
        if(onStartChanged) onStartChanged(arg0, arg1)
    }

    void extentChanged(Arc arg0, float arg1) {
        if(onExtentChanged) onExtentChanged(arg0, arg1)
    }

    void typeChanged(Arc arg0, Type arg1) {
        if(onTypeChanged) onTypeChanged(arg0, arg1)
    }

    void sizeChanged(Arc arg0, int arg1, int arg2) {
        if(onSizeChanged) onSizeChanged(arg0, arg1, arg2)
    }
}