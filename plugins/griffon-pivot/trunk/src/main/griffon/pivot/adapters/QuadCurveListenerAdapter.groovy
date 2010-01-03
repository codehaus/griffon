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
import org.apache.pivot.wtk.media.drawing.QuadCurve
import org.apache.pivot.wtk.media.drawing.QuadCurveListener

/**
 * @author Andres Almiray
 */
class QuadCurveListenerAdapter extends BuilderDelegate implements QuadCurveListener {
    private Closure onEndpointsChanged
    private Closure onControlPointChanged
 
    QuadCurveListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onEndpointsChanged(Closure callback) {
        onEndpointsChanged = callback
        onEndpointsChanged.delegate = this
    }

    void onControlPointChanged(Closure callback) {
        onControlPointChanged = callback
        onControlPointChanged.delegate = this
    }

    void endpointsChanged(QuadCurve arg0, int arg1, int arg2, int arg3, int arg4) {
        if(onEndpointsChanged) onEndpointsChanged(arg0, arg1, arg2, arg3, arg4)
    }

    void controlPointChanged(QuadCurve arg0, int arg1, int arg2) {
        if(onControlPointChanged) onControlPointChanged(arg0, arg1, arg2)
    }
}