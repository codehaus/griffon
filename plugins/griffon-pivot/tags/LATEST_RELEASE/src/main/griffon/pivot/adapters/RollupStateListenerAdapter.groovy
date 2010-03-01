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
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.Rollup
import org.apache.pivot.wtk.RollupStateListener

/**
 * @author Andres Almiray
 */
class RollupStateListenerAdapter extends BuilderDelegate implements RollupStateListener {
    private Closure onPreviewExpandedChange
    private Closure onExpandedChanged
    private Closure onExpandedChangeVetoed
 
    RollupStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewExpandedChange(Closure callback) {
        onPreviewExpandedChange = callback
        onPreviewExpandedChange.delegate = this
    }

    void onExpandedChanged(Closure callback) {
        onExpandedChanged = callback
        onExpandedChanged.delegate = this
    }

    void onExpandedChangeVetoed(Closure callback) {
        onExpandedChangeVetoed = callback
        onExpandedChangeVetoed.delegate = this
    }

    Vote previewExpandedChange(Rollup arg0) {
        if(onPreviewExpandedChange) onPreviewExpandedChange(arg0); else Vote.APPROVE
    }

    void expandedChanged(Rollup arg0) {
        if(onExpandedChanged) onExpandedChanged(arg0)
    }

    void expandedChangeVetoed(Rollup arg0, Vote arg1) {
        if(onExpandedChangeVetoed) onExpandedChangeVetoed(arg0, arg1)
    }
}