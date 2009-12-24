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
import org.apache.pivot.wtk.media.drawing.Canvas
import org.apache.pivot.wtk.media.drawing.CanvasListener

/**
 * @author Andres Almiray
 */
class CanvasListenerAdapter extends BuilderDelegate implements CanvasListener {
    private Closure onRegionUpdated
    private Closure onCanvasInvalidated
 
    CanvasListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRegionUpdated(Closure callback) {
        onRegionUpdated = callback
        onRegionUpdated.delegate = this
    }

    void onCanvasInvalidated(Closure callback) {
        onCanvasInvalidated = callback
        onCanvasInvalidated.delegate = this
    }

    void regionUpdated(Canvas arg0, int arg1, int arg2, int arg3, int arg4) {
        if(onRegionUpdated) onRegionUpdated(arg0, arg1, arg2, arg3, arg4)
    }

    void canvasInvalidated(Canvas arg0) {
        if(onCanvasInvalidated) onCanvasInvalidated(arg0)
    }
}