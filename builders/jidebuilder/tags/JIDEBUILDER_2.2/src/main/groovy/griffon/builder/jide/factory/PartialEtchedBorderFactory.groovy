/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.jide.factory

import java.awt.Color
import com.jidesoft.swing.PartialSide
import com.jidesoft.swing.PartialEtchedBorder
import groovy.swing.factory.SwingBorderFactory

/**
 *
 * Based on groovy.swing.factory.EtchedBorderFactory
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class PartialEtchedBorderFactory extends SwingBorderFactory {
    final int type

    public PartialEtchedBorderFactory(int newType) {
        type = newType
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        builder.context.applyBorderToParent = attributes.remove('parent')

        def sides = attributes.remove("sides")
        sides = sides == null ? PartialSide.ALL : sides
        Color highlight = attributes.remove("highlight")
        Color shadow = attributes.remove("shadow")
        if( highlight && shadow ) {
           return new PartialEtchedBorder(type, highlight, shadow, sides)
        } else {
           return new PartialEtchedBorder(type,sides)
        }
    }
}