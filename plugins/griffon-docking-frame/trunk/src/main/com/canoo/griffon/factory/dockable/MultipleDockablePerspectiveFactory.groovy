/*
 * Copyright 2009-2011 the original author or authors.
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
package com.canoo.griffon.factory.dockable

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.perspective.SingleCDockablePerspective
import bibliothek.gui.dock.common.perspective.MultipleCDockablePerspective
import bibliothek.gui.dock.common.MultipleCDockableLayout

/**
 * @author Alexander Klein
 */
class MultipleDockablePerspectiveFactory extends CStationFactory {
    MultipleDockablePerspectiveFactory() {
        super(MultipleCDockablePerspective, true)
    }

    @Override
    protected Object createInstance(CControl control, String id, Map attributes) {
        def layout = attributes.remove('layout')
        if (!(layout instanceof MultipleCDockableLayout))
            throw new IllegalArgumentException('layout required')

        def factory = attributes.remove('factory')
        if (!(factory instanceof String))
            throw new IllegalArgumentException('factory required')

        if (!id)
            id = UUID.randomUUID().toString()
        return new MultipleCDockablePerspective(factory, id, layout)
    }
}
