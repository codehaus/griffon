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
import org.apache.pivot.wtk.Meter
import org.apache.pivot.wtk.MeterListener

/**
 * @author Andres Almiray
 */
class MeterListenerAdapter extends BuilderDelegate implements MeterListener {
    private Closure onOrientationChanged
    private Closure onTextChanged
    private Closure onPercentageChanged
 
    MeterListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onOrientationChanged(Closure callback) {
        onOrientationChanged = callback
        onOrientationChanged.delegate = this
    }

    void onTextChanged(Closure callback) {
        onTextChanged = callback
        onTextChanged.delegate = this
    }

    void onPercentageChanged(Closure callback) {
        onPercentageChanged = callback
        onPercentageChanged.delegate = this
    }

    void orientationChanged(Meter arg0) {
        if(onOrientationChanged) onOrientationChanged(arg0)
    }

    void textChanged(Meter arg0, String arg1) {
        if(onTextChanged) onTextChanged(arg0, arg1)
    }

    void percentageChanged(Meter arg0, double arg1) {
        if(onPercentageChanged) onPercentageChanged(arg0, arg1)
    }
}