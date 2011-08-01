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

import bibliothek.gui.dock.common.action.CAction
import bibliothek.gui.dock.common.action.CMenu

/**
 * @author Alexander Klein
 */
class CMenuFactory extends CDecorateableActionFactory {
    CMenuFactory() {
        super(CMenu, false)
    }

    @Override
    createInstance(Map attributes) {
        return new CMenu()
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof CAction)
            parent.add(child)
    }
}

