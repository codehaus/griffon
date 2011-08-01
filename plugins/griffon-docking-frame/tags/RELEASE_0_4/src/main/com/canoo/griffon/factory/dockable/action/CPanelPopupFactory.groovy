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
import java.awt.Component
import bibliothek.gui.dock.common.action.CPanelPopup
import bibliothek.gui.dock.common.action.CPanelPopup.ButtonBehavior
import bibliothek.gui.dock.common.action.CPanelPopup.MenuBehavior

/**
 * @author Alexander Klein
 */
class CPanelPopupFactory extends CDecorateableActionFactory {
    CPanelPopupFactory() {
        super(CPanelPopup, false)
    }

    @Override
    createInstance(Map attributes) {
        def action = new CPanelPopup()
        def buttonBehavior = attributes.remove('buttonBehavior')
        if (buttonBehavior instanceof String)
            action.buttonBehavior = ButtonBehavior.valueOf(buttonBehavior.toUpperCase())
        else if (buttonBehavior instanceof ButtonBehavior)
            action.buttonBehavior = buttonBehavior

            def menuBehavior = attributes.remove('menuBehavior')
        if (menuBehavior instanceof String)
            action.menuBehavior = MenuBehavior.valueOf(menuBehavior.toUpperCase())
        else if (menuBehavior instanceof MenuBehavior)
            action.menuBehavior = menuBehavior

        return action
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Component)
            parent.content = child
    }
}

