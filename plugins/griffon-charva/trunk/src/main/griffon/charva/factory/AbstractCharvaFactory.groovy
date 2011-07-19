/*
 * Copyright 2010 the original author or authors.
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

package griffon.charva.factory

import charvax.swing.JFrame
import charvax.swing.JDialog

/**
 * @author Andres Almiray
 */
abstract class AbstractCharvaFactory extends AbstractFactory {
    static boolean isRootPaneContainer(bean) {
        bean.metaClass.respondsTo(bean, 'getContentPane') ? true: false
    }

    static int toInteger(value) {
        if(value instanceof Number) return value.intValue()
        return Double.valueOf(value.toString()).intValue()
    }

    static boolean toBoolean(value) {
        if(value instanceof Boolean) return value.booleanValue()
        return Boolean.valueOf(value.toString()).booleanValue()
    }
}
