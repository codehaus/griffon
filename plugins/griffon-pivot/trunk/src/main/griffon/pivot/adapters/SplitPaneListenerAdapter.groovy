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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.SplitPane
import org.apache.pivot.wtk.SplitPane.ResizeMode
import org.apache.pivot.wtk.SplitPaneListener

/**
 * @author Andres Almiray
 */
class SplitPaneListenerAdapter extends BuilderDelegate implements SplitPaneListener {
    private Closure onTopLeftChanged
    private Closure onBottomRightChanged
    private Closure onOrientationChanged
    private Closure onPrimaryRegionChanged
    private Closure onSplitRatioChanged
    private Closure onLockedChanged
    private Closure onResizeModeChanged
 
    SplitPaneListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTopLeftChanged(Closure callback) {
        onTopLeftChanged = callback
        onTopLeftChanged.delegate = this
    }

    void onBottomRightChanged(Closure callback) {
        onBottomRightChanged = callback
        onBottomRightChanged.delegate = this
    }

    void onOrientationChanged(Closure callback) {
        onOrientationChanged = callback
        onOrientationChanged.delegate = this
    }

    void onPrimaryRegionChanged(Closure callback) {
        onPrimaryRegionChanged = callback
        onPrimaryRegionChanged.delegate = this
    }

    void onSplitRatioChanged(Closure callback) {
        onSplitRatioChanged = callback
        onSplitRatioChanged.delegate = this
    }

    void onLockedChanged(Closure callback) {
        onLockedChanged = callback
        onLockedChanged.delegate = this
    }

    void onResizeModeChanged(Closure callback) {
        onResizeModeChanged = callback
        onResizeModeChanged.delegate = this
    }

    void topLeftChanged(SplitPane arg0, Component arg1) {
        if(onTopLeftChanged) onTopLeftChanged(arg0, arg1)
    }

    void bottomRightChanged(SplitPane arg0, Component arg1) {
        if(onBottomRightChanged) onBottomRightChanged(arg0, arg1)
    }

    void orientationChanged(SplitPane arg0) {
        if(onOrientationChanged) onOrientationChanged(arg0)
    }

    void primaryRegionChanged(SplitPane arg0) {
        if(onPrimaryRegionChanged) onPrimaryRegionChanged(arg0)
    }

    void splitRatioChanged(SplitPane arg0, float arg1) {
        if(onSplitRatioChanged) onSplitRatioChanged(arg0, arg1)
    }

    void lockedChanged(SplitPane arg0) {
        if(onLockedChanged) onLockedChanged(arg0)
    }

    void resizeModeChanged(SplitPane arg0, ResizeMode arg1) {
        if(onResizeModeChanged) onResizeModeChanged(arg0, arg1)
    }
}