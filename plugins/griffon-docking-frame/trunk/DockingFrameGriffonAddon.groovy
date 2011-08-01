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

import bibliothek.gui.DockController
import bibliothek.gui.dock.support.menu.MenuPiece
import bibliothek.gui.dock.util.WindowProvider
import com.canoo.griffon.factory.dockable.action.CActionFactory
import com.canoo.griffon.factory.dockable.action.CBlankFactory
import com.canoo.griffon.factory.dockable.action.CButtonFactory
import griffon.core.GriffonApplication
import com.canoo.griffon.factory.dockable.*
import com.canoo.griffon.factory.dockable.menu.*
import com.canoo.griffon.factory.dockable.action.CCheckBoxFactory
import com.canoo.griffon.factory.dockable.action.CDropDownButtonFactory
import com.canoo.griffon.factory.dockable.action.CMenuFactory
import bibliothek.gui.dock.common.action.CPanelPopup
import com.canoo.griffon.factory.dockable.action.CPanelPopupFactory
import com.canoo.griffon.factory.dockable.action.CRadioButtonFactory
import com.canoo.griffon.factory.dockable.action.CSeparatorFactory
import com.canoo.griffon.factory.dockable.action.CSystemActionFactory

/**
 * @author Per Junel
 * @author Christoph Lipp
 * @author Alexander Klein
 */
class DockingFrameGriffonAddon {
    WindowProvider provider
    GriffonApplication app

    def addonInit(app) {
        this.provider = new GriffonWindowProvider(app)
        this.app = app
        app.metaClass.dockController = new DockController()
    }

    def factories = [
            dockingFrame: new DockingFrameFactory(),
            dockingControl: new CControlFactory(this),
            dockingArea: new CContentAreaFactory(true),
            contentArea: new CContentAreaFactory(),
            minimizeArea: new CMinimizeAreaFactory(),
            gridArea: new CGridAreaFactory(),
            workingArea: new CWorkingAreaFactory(),
            dockable: new CDockableFactory(),

            dockableFactory: new SingleDockableFactoryFactory(),
            mvcGroupFactory: new GriffonSingleDockableFactoryFactory(app),
            multiFactory: new MultipleDockableFactoryFactory(),

            placeholder: new SingleDockablePerspectiveFactory(),
            multiPlaceholder: new MultipleDockablePerspectiveFactory(),

            rootMenuPiece: new RootMenuPieceFactory(),
            subMenuPiece: new RootMenuPieceFactory(true),
            nodeMenuPiece: new NodeMenuPieceFactory(),
            separatingMenuPiece: new SeparatingMenuPieceFactory(),
            menuPiece: new MenuPieceFactory(MenuPiece),
            dockableListMenuPiece: new SingleDockableListMenuPieceFactory(),
            themeMenuPiece: new CThemeMenuPieceFactory(),
            preferenceMenuPiece: new CPreferenceMenuPieceFactory(),
            lookAndFeelMenuPiece: new CLookAndFeelMenuPieceFactory(),
            layoutMenuPiece: new CLayoutChoiceMenuPieceFactory(),

            dockingAction: new CActionFactory(),
            blankAction: new CBlankFactory(),
            buttonAction: new CButtonFactory(),
            checkBoxAction: new CCheckBoxFactory(),
            dropDownButtonAction: new CDropDownButtonFactory(),
            menuAction: new CMenuFactory(),
            popupAction: new CPanelPopupFactory(),
            radioButtonAction: new CRadioButtonFactory(),
            separatorAction: new CSeparatorFactory(),
            systemAction: new CSystemActionFactory(),
    ]

    def attributeDelegates = [
            { FactoryBuilderSupport builder, node, attributes ->
                builder.context.dock = attributes.remove('dock')
                builder.context.dockMinimized = attributes.remove('dockMinimized')
                builder.context.dockExternalized = attributes.remove('dockExternalized')
                builder.context.dockState = attributes.remove('dockState')
                if (builder.parentFactory instanceof CDropDownButtonFactory)
                    builder.context.selection = attributes.remove('selection')
            }
    ]
}
