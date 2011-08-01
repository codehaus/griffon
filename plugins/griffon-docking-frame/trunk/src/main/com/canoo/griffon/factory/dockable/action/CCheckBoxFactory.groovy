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
 */ package com.canoo.griffon.factory.dockable.action

import bibliothek.gui.dock.common.action.CButton
import java.awt.event.ActionListener
import bibliothek.gui.dock.common.action.CCheckBox
import javax.swing.Icon

/**
 * @author Alexander Klein
 */
class CCheckBoxFactory extends CDecorateableActionFactory {
    CCheckBoxFactory() {
        super(CCheckBox)
    }

    @Override
    createInstance(Map attributes) {
        def action = new ClosureCCheckBox()
        if (attributes.get("closure") instanceof Closure) {
            Closure closure = (Closure) attributes.remove("closure")
            action.setChanged(closure)
        }
        return action
    }
}

class ClosureCCheckBox extends CCheckBox {
    Closure changed

    @Override
    protected void changed() {
        this.changed.call(this)
    }
}
