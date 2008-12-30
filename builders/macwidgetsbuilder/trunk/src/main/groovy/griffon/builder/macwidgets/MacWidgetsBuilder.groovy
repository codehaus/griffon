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

import groovy.swing.SwingBuilder
import griffon.builder.macwidgets.factory.*

import com.explodingpixels.macwidgets.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MacWidgetsBuilder extends SwingBuilder {
   public MacWidgetsBuilder( boolean init = true ) {
      super( init )
   }

   def registerMacWidgets() {
      registerFactory("macButtonAddItem16", new MacButtonFactory(
         MacWidgetsIcons.ADD_ITEM_16, "segmentedTextured", false
      ))
      registerFactory("macButtonAddItem18", new MacButtonFactory(
         MacWidgetsIcons.ADD_ITEM_18, "segmentedTextured", false
      ))
      registerFactory("macButtonRemoveItem16", new MacButtonFactory(
         MacWidgetsIcons.REMOVE_ITEM_16, "segmentedTextured", false
      ))
      registerFactory("macButtonRemoveItem18", new MacButtonFactory(
         MacWidgetsIcons.REMOVE_ITEM_18, "segmentedTextured", false
      ))
      registerFactory("macButtonLock", new MacButtonFactory(
         MacWidgetsIcons.LOCK, "textured"
      ))
      registerFactory("macButtonSourceViewNormal", new MacButtonFactory(
         MacWidgetsIcons.SOURCE_VIEW_NORMAL, "segmentedTextured", true, MacWidgetsIcons.SOURCE_VIEW_NORMAL_SELECTED
      ))

      registerFactory("bottomBar", new BottomBarFactory())
      registerFactory("componentStatusBar", new ComponentStatusBarFactory())
      registerFactory("componentTopBar", new TriAreaComponentFactory(ComponentTopBar))
      registerFactory("emphasizedLabel", new EmphasizedLabelFactory())
      registerFactory("hudWindow", new HudWindowFactory())
      registerFactory("iappScrollPane", new IAppScrollPaneFactory())
      registerFactory("imageButton", new ImageButtonFactory())
      registerFactory("itunesTable", new ITunesTableFactory())
      registerFactory("labeledComponentGroup", new LabeledComponentGroupFactory())
//       registerFactory("macPreferencesTabBar", new MacPreferencesTabBarFactory())
      registerFactory("preferencesTab", new PreferencesTabFactory())
      registerFactory("preferencesTabBar", new PreferencesTabBarFactory())
      registerFactory("sourceList", new SourceListFactory())
      registerFactory("sourceListCategory", new SourceListCategoryFactory())
      registerFactory("sourceListItem", new SourceListItemFactory())
      registerFactory("sourceListControlBar", new SourceListControlBarFactory())
      registerFactory("controlBarButton", new SourceListControlBarButtonFactory())
      registerFactory("controlBarPopdownButton", new SourceListControlBarPopdownButtonFactory())
      registerFactory("spacer", new SpacerFactory())
   }
}