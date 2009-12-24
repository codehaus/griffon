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
import org.apache.pivot.wtk.media.drawing.CubicCurve
import org.apache.pivot.wtk.media.drawing.CubicCurveListener

/**
 * @author Andres Almiray
 */
class CubicCurveListenerAdapter extends BuilderDelegate implements CubicCurveListener {
    private Closure onEndpointsChanged
    private Closure onControlPointsChanged
 
    CubicCurveListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onEndpointsChanged(Closure callback) {
        onEndpointsChanged = callback
        onEndpointsChanged.delegate = this
    }

    void onControlPointsChanged(Closure callback) {
        onControlPointsChanged = callback
        onControlPointsChanged.delegate = this
    }

    void endpointsChanged(CubicCurve arg0, int arg1, int arg2, int arg3, int arg4) {
        if(onEndpointsChanged) onEndpointsChanged(arg0, arg1, arg2, arg3, arg4)
    }

    void controlPointsChanged(CubicCurve arg0, int arg1, int arg2, int arg3, int arg4) {
        if(onControlPointsChanged) onControlPointsChanged(arg0, arg1, arg2, arg3, arg4)
    }
}