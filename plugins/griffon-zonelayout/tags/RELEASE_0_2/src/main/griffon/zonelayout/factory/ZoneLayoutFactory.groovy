/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.zonelayout.factory

import groovy.swing.factory.LayoutFactory
import com.atticlabs.zonelayout.swing.ZoneLayout
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory as ZLF

/**
 * @author Andres Almiray
 */
class ZoneLayoutFactory extends LayoutFactory {
    ZoneLayoutFactory() {
        super(ZoneLayout)
    }

    boolean isLeaf() {
        false
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context[DELEGATE_PROPERTY_CONSTRAINT] = attributes.remove("constraintsProperty") ?: DEFAULT_DELEGATE_PROPERTY_CONSTRAINT
        Object o = ZLF.newZoneLayout()
        addLayoutProperties(builder.getContext())
        return o
    }
}
