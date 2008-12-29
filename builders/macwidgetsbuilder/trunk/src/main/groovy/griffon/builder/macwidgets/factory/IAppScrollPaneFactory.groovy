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

package griffon.builder.macwidgets.factory

import groovy.swing.factory.ScrollPaneFactory
import com.explodingpixels.macwidgets.IAppWidgetFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class IAppScrollPaneFactory extends ScrollPaneFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def scrollpane = super.newInstance(builder, name, value, attributes)
        IAppWidgetFactory.makeIAppScrollPane(scrollpane)
        return scrollpane
    }
}