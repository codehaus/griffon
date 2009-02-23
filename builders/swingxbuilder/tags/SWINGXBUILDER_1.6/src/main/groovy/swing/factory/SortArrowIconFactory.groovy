/*
 * $Id:  $
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
 *
 */

package groovy.swing.factory

import groovy.swing.SwingBuilder
import org.jdesktop.swingx.icon.SortArrowIcon


public class SortArrowIconFactory extends AbstractFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
        if (SwingBuilder.checkValueIsType(value, name, SortArrowIcon.class)) {
            return value
        }
        def sortIcon
        def ascending = properties.remove("ascending")
        if (ascending == null)
            ascending = true

        sortIcon = new SortArrowIcon(ascending)
        return sortIcon
    }

}
