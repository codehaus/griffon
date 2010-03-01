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
import org.apache.pivot.wtk.Accordion
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.AccordionListener

/**
 * @author Andres Almiray
 */
class AccordionListenerAdapter extends BuilderDelegate implements AccordionListener {
    private Closure onPanelInserted
    private Closure onPanelsRemoved
 
    AccordionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPanelInserted(Closure callback) {
        onPanelInserted = callback
        onPanelInserted.delegate = this
    }

    void onPanelsRemoved(Closure callback) {
        onPanelsRemoved = callback
        onPanelsRemoved.delegate = this
    }

    void panelInserted(Accordion arg0, int arg1) {
        if(onPanelInserted) onPanelInserted(arg0, arg1)
    }

    void panelsRemoved(Accordion arg0, int arg1, Sequence arg2) {
        if(onPanelsRemoved) onPanelsRemoved(arg0, arg1, arg2)
    }
}