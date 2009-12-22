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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.Rollup
import org.apache.pivot.wtk.RollupListener

/**
 * @author Andres Almiray
 */
class RollupListenerAdapter extends BuilderDelegate implements RollupListener {
    private Closure onContentChanged
    private Closure onHeadingChanged
    private Closure onCollapsibleChanged
 
    RollupListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onContentChanged(Closure callback) {
        onContentChanged = callback
        onContentChanged.delegate = this
    }

    void onHeadingChanged(Closure callback) {
        onHeadingChanged = callback
        onHeadingChanged.delegate = this
    }

    void onCollapsibleChanged(Closure callback) {
        onCollapsibleChanged = callback
        onCollapsibleChanged.delegate = this
    }

    void contentChanged(Rollup arg0, Component arg1) {
        if(onContentChanged) onContentChanged(arg0, arg1)
    }

    void headingChanged(Rollup arg0, Component arg1) {
        if(onHeadingChanged) onHeadingChanged(arg0, arg1)
    }

    void collapsibleChanged(Rollup arg0) {
        if(onCollapsibleChanged) onCollapsibleChanged(arg0)
    }
}