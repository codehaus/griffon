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
import org.apache.pivot.wtk.media.drawing.Rectangle
import org.apache.pivot.wtk.media.drawing.RectangleListener

/**
 * @author Andres Almiray
 */
class RectangleListenerAdapter extends BuilderDelegate implements RectangleListener {
    private Closure onCornerRadiusChanged
    private Closure onSizeChanged
 
    RectangleListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onCornerRadiusChanged(Closure callback) {
        onCornerRadiusChanged = callback
        onCornerRadiusChanged.delegate = this
    }

    void onSizeChanged(Closure callback) {
        onSizeChanged = callback
        onSizeChanged.delegate = this
    }

    void cornerRadiusChanged(Rectangle arg0, int arg1) {
        if(onCornerRadiusChanged) onCornerRadiusChanged(arg0, arg1)
    }

    void sizeChanged(Rectangle arg0, int arg1, int arg2) {
        if(onSizeChanged) onSizeChanged(arg0, arg1, arg2)
    }
}