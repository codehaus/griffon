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
import org.apache.pivot.wtk.media.Drawing
import org.apache.pivot.wtk.media.drawing.Canvas
import java.awt.Paint
import org.apache.pivot.wtk.media.DrawingListener

/**
 * @author Andres Almiray
 */
class DrawingListenerAdapter extends BuilderDelegate implements DrawingListener {
    private Closure onCanvasChanged
    private Closure onBackgroundChanged
 
    DrawingListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onCanvasChanged(Closure callback) {
        onCanvasChanged = callback
        onCanvasChanged.delegate = this
    }

    void onBackgroundChanged(Closure callback) {
        onBackgroundChanged = callback
        onBackgroundChanged.delegate = this
    }

    void canvasChanged(Drawing arg0, Canvas arg1) {
        if(onCanvasChanged) onCanvasChanged(arg0, arg1)
    }

    void backgroundChanged(Drawing arg0, Paint arg1) {
        if(onBackgroundChanged) onBackgroundChanged(arg0, arg1)
    }
}