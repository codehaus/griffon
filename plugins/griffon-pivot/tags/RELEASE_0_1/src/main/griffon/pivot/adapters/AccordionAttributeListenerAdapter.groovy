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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.media.Image
import org.apache.pivot.wtk.AccordionAttributeListener

/**
 * @author Andres Almiray
 */
class AccordionAttributeListenerAdapter extends BuilderDelegate implements AccordionAttributeListener {
    private Closure onIconChanged
    private Closure onLabelChanged
 
    AccordionAttributeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onIconChanged(Closure callback) {
        onIconChanged = callback
        onIconChanged.delegate = this
    }

    void onLabelChanged(Closure callback) {
        onLabelChanged = callback
        onLabelChanged.delegate = this
    }

    void iconChanged(Accordion arg0, Component arg1, Image arg2) {
        if(onIconChanged) onIconChanged(arg0, arg1, arg2)
    }

    void labelChanged(Accordion arg0, Component arg1, String arg2) {
        if(onLabelChanged) onLabelChanged(arg0, arg1, arg2)
    }
}