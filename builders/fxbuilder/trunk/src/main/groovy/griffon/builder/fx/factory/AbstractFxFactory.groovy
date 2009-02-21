/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.fx.factory

import com.sun.javafx.runtime.location.*
import com.sun.javafx.runtime.sequence.*
import com.sun.javafx.runtime.TypeInfo

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
abstract class AbstractFxFactory extends AbstractFactory implements FxFactory {
    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        try {
           node."initialize\$"()
        } catch( MissingMethodException mme ) {
           // ignore
        }
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        attributes.each { key, value ->
            def attr = node.attribute(key)
            switch(attr) {
                case BooleanVariable: attr.setAsBooleanFromLiteral(value); break
                case DoubleVariable: attr.setAsDoubleFromLiteral(value); break
                case IntVariable: attr.setAsIntFromLiteral(value); break
                case SequenceVariable:
                    switch(value) {
                        case Sequence: attr.setAsSequenceFromLiteral(value); break
                        default:
                            def sb = new SequenceBuilder(TypeInfo.Object)
                            value.each{ sb.add(it) }
                            attr.setAsSequenceFromLiteral(sb.toSequence())
                    }
                    break
                default: attr.setFromLiteral(value)
            }
        }
        return false
    }
}