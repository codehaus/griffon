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
import org.apache.pivot.wtk.media.drawing.Shape
import java.awt.Paint
import org.apache.pivot.wtk.media.drawing.ShapeListener

/**
 * @author Andres Almiray
 */
class ShapeListenerAdapter extends BuilderDelegate implements ShapeListener {
    private Closure onOriginChanged
    private Closure onFillChanged
    private Closure onStrokeChanged
    private Closure onStrokeThicknessChanged
    private Closure onVisibleChanged
 
    ShapeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onOriginChanged(Closure callback) {
        onOriginChanged = callback
        onOriginChanged.delegate = this
    }

    void onFillChanged(Closure callback) {
        onFillChanged = callback
        onFillChanged.delegate = this
    }

    void onStrokeChanged(Closure callback) {
        onStrokeChanged = callback
        onStrokeChanged.delegate = this
    }

    void onStrokeThicknessChanged(Closure callback) {
        onStrokeThicknessChanged = callback
        onStrokeThicknessChanged.delegate = this
    }

    void onVisibleChanged(Closure callback) {
        onVisibleChanged = callback
        onVisibleChanged.delegate = this
    }

    void originChanged(Shape arg0, int arg1, int arg2) {
        if(onOriginChanged) onOriginChanged(arg0, arg1, arg2)
    }

    void fillChanged(Shape arg0, Paint arg1) {
        if(onFillChanged) onFillChanged(arg0, arg1)
    }

    void strokeChanged(Shape arg0, Paint arg1) {
        if(onStrokeChanged) onStrokeChanged(arg0, arg1)
    }

    void strokeThicknessChanged(Shape arg0, int arg1) {
        if(onStrokeThicknessChanged) onStrokeThicknessChanged(arg0, arg1)
    }

    void visibleChanged(Shape arg0) {
        if(onVisibleChanged) onVisibleChanged(arg0)
    }
}