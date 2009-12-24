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
import org.apache.pivot.wtk.media.drawing.Shape.Transform
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.media.drawing.ShapeTransformListener

/**
 * @author Andres Almiray
 */
class ShapeTransformListenerAdapter extends BuilderDelegate implements ShapeTransformListener {
    private Closure onTransformUpdated
    private Closure onTransformInserted
    private Closure onTransformsRemoved
 
    ShapeTransformListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTransformUpdated(Closure callback) {
        onTransformUpdated = callback
        onTransformUpdated.delegate = this
    }

    void onTransformInserted(Closure callback) {
        onTransformInserted = callback
        onTransformInserted.delegate = this
    }

    void onTransformsRemoved(Closure callback) {
        onTransformsRemoved = callback
        onTransformsRemoved.delegate = this
    }

    void transformUpdated(Transform arg0) {
        if(onTransformUpdated) onTransformUpdated(arg0)
    }

    void transformInserted(Shape arg0, int arg1) {
        if(onTransformInserted) onTransformInserted(arg0, arg1)
    }

    void transformsRemoved(Shape arg0, int arg1, Sequence arg2) {
        if(onTransformsRemoved) onTransformsRemoved(arg0, arg1, arg2)
    }
}