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
import org.apache.pivot.wtk.Accordion
import org.apache.pivot.wtk.AccordionSelectionListener

/**
 * @author Andres Almiray
 */
class AccordionSelectionListenerAdapter extends BuilderDelegate implements AccordionSelectionListener {
    private Closure onPreviewSelectedIndexChange
    private Closure onSelectedIndexChanged
    private Closure onSelectedIndexChangeVetoed
 
    AccordionSelectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewSelectedIndexChange(Closure callback) {
        onPreviewSelectedIndexChange = callback
        onPreviewSelectedIndexChange.delegate = this
    }

    void onSelectedIndexChanged(Closure callback) {
        onSelectedIndexChanged = callback
        onSelectedIndexChanged.delegate = this
    }

    void onSelectedIndexChangeVetoed(Closure callback) {
        onSelectedIndexChangeVetoed = callback
        onSelectedIndexChangeVetoed.delegate = this
    }

    Vote previewSelectedIndexChange(Accordion arg0, int arg1) {
        if(onPreviewSelectedIndexChange) onPreviewSelectedIndexChange(arg0, arg1); else Vote.APPROVE
    }

    void selectedIndexChanged(Accordion arg0, int arg1) {
        if(onSelectedIndexChanged) onSelectedIndexChanged(arg0, arg1)
    }

    void selectedIndexChangeVetoed(Accordion arg0, Vote arg1) {
        if(onSelectedIndexChangeVetoed) onSelectedIndexChangeVetoed(arg0, arg1)
    }
}