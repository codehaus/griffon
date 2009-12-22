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
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.Expander
import org.apache.pivot.wtk.ExpanderListener

/**
 * @author Andres Almiray
 */
class ExpanderListenerAdapter extends BuilderDelegate implements ExpanderListener {
    private Closure onTitleChanged
    private Closure onContentChanged
    private Closure onPreviewExpandedChange
    private Closure onExpandedChanged
    private Closure onExpandedChangeVetoed
    private Closure onCollapsibleChanged
 
    ExpanderListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTitleChanged(Closure callback) {
        onTitleChanged = callback
        onTitleChanged.delegate = this
    }

    void onContentChanged(Closure callback) {
        onContentChanged = callback
        onContentChanged.delegate = this
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

    void onCollapsibleChanged(Closure callback) {
        onCollapsibleChanged = callback
        onCollapsibleChanged.delegate = this
    }

    void titleChanged(Expander arg0, String arg1) {
        if(onTitleChanged) onTitleChanged(arg0, arg1)
    }

    void contentChanged(Expander arg0, Component arg1) {
        if(onContentChanged) onContentChanged(arg0, arg1)
    }

    Vote previewExpandedChange(Expander arg0) {
        if(onPreviewExpandedChange) onPreviewExpandedChange(arg0); else Vote.APPROVE
    }

    void expandedChanged(Expander arg0) {
        if(onExpandedChanged) onExpandedChanged(arg0)
    }

    void expandedChangeVetoed(Expander arg0, Vote arg1) {
        if(onExpandedChangeVetoed) onExpandedChangeVetoed(arg0, arg1)
    }

    void collapsibleChanged(Expander arg0) {
        if(onCollapsibleChanged) onCollapsibleChanged(arg0)
    }
}