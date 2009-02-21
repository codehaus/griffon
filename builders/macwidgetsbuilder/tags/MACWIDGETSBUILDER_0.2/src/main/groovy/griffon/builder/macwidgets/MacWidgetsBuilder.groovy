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

package griffon.builder.macwidgets

import java.awt.Window
import javax.swing.JFrame
import groovy.swing.SwingBuilder
import griffon.builder.macwidgets.factory.*

import com.explodingpixels.macwidgets.*
import com.explodingpixels.widgets.WindowUtils

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MacWidgetsBuilder extends SwingBuilder {
   public MacWidgetsBuilder( boolean init = true ) {
      super( init )
   }

   def registerMacWidgets() {
      registerFactory("macButtonAddItem16", new MacButtonsFactory(
         MacWidgetsIcons.ADD_ITEM_16, "segmentedTextured", false
      ))
      registerFactory("macButtonAddItem18", new MacButtonsFactory(
         MacWidgetsIcons.ADD_ITEM_18, "segmentedTextured", false
      ))
      registerFactory("macButtonRemoveItem16", new MacButtonsFactory(
         MacWidgetsIcons.REMOVE_ITEM_16, "segmentedTextured", false
      ))
      registerFactory("macButtonRemoveItem18", new MacButtonsFactory(
         MacWidgetsIcons.REMOVE_ITEM_18, "segmentedTextured", false
      ))
      registerFactory("macButtonLock", new MacButtonsFactory(
         MacWidgetsIcons.LOCK, "textured"
      ))
      registerFactory("macButtonSourceViewNormal", new MacButtonsFactory(
         MacWidgetsIcons.SOURCE_VIEW_NORMAL, "segmentedTextured", true, MacWidgetsIcons.SOURCE_VIEW_NORMAL_SELECTED
      ))

      registerFactory("bottomBar", new BottomBarFactory())
      registerFactory("componentStatusBar", new ComponentStatusBarFactory())
      registerFactory("componentTopBar", new TriAreaComponentFactory(ComponentTopBar))
      registerFactory("emphasizedLabel", new EmphasizedLabelFactory())
      registerFactory("hudWindow", new HudWindowFactory())
      registerFactory("hudLabel", new HudWidgetsFactory("Label"))
      registerFactory("hudButton", new HudWidgetsFactory("Button"))
      registerFactory("hudCheckBox", new HudWidgetsFactory("CheckBox"))
      registerFactory("hudTextField", new HudWidgetsFactory("TextField"))
      registerFactory("hudComboBox", new HudComboBoxFactory())
      registerFactory("iappScrollPane", new IAppScrollPaneFactory())
      registerFactory("imageButton", new ImageButtonFactory())
      registerFactory("itunesTable", new ITunesTableFactory())
      registerFactory("labeledComponentGroup", new LabeledComponentGroupFactory())
//       registerFactory("macPreferencesTabBar", new MacPreferencesTabBarFactory())
      registerFactory("macGradientButton", new MacGradientButtonFactory())
      registerFactory("macGradientPopdownButton", new MacGradientPopdownButtonFactory())
      registerFactory("preferencesTab", new PreferencesTabFactory())
      registerFactory("preferencesTabBar", new PreferencesTabBarFactory())
      registerFactory("sourceList", new SourceListFactory())
      registerFactory("sourceListCategory", new SourceListCategoryFactory())
      registerFactory("sourceListItem", new SourceListItemFactory())
      registerFactory("sourceListControlBar", new SourceListControlBarFactory())
      registerFactory("sourceListSplitPane", new SourceListSplitPaneFactory())
      registerFactory("sourceListContextMenuProvider", new SourceListContextMenuProviderFactory())
      registerFactory("controlBarButton", new SourceListControlBarButtonFactory())
      registerFactory("controlBarPopdownButton", new SourceListControlBarPopdownButtonFactory())
      registerFactory("spacer", new SpacerFactory())
      registerFactory("unifiedToolBar", new UnifiedToolBarFactory())
      registerFactory("preferencesBarButton", new MacBarButonFactory(MacBarButtonType.UNIFIED_TOOLBAR))
      registerFactory("unifiedToolBarButton", new MacBarButonFactory(MacBarButtonType.UNIFIED_TOOLBAR))

      addAttributeDelegate(MacWidgetsBuilder.&repaintWindowAttributeDelegate)
      addAttributeDelegate(MacWidgetsBuilder.&leopardizeWindowAttributeDelegate)
   }

   public static repaintWindowAttributeDelegate( builder, node, attributes ) {
      if( node instanceof Window && attributes.containsKey("installRepaintListener") ) {
         boolean installRepaintListener = attributes.remove("installRepaintListener")
         if( installRepaintListener ) {
            WindowUtils.createAndInstallRepaintWindowFocusListener(node)
         }
      }
   }

   public static leopardizeWindowAttributeDelegate( builder, node, attributes ) {
      if( node instanceof JFrame && attributes.containsKey("makeLeopardStyle") ) {
         boolean makeLeopardStyle = attributes.remove("makeLeopardStyle")
         if( makeLeopardStyle ) {
            MacUtils.makeWindowLeopardStyle(node.rootPane)
         }
      }
   }
}