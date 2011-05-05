/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.flamingo

import groovy.swing.SwingBuilder
import griffon.builder.flamingo.factory.*

import org.jvnet.flamingo.common.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class FlamingoBuilder extends SwingBuilder {
   public FlamingoBuilder( boolean init = true ) {
      super( init )
   }

   public void registerFlamingoIcons() {
      registerFactory("arrowIcon", new ArrowIconFactory())
      registerFactory("decoratedIcon", new DecoratedResizableIconFactory())
      registerFactory("emptyIcon", new EmptyResizableIconFactory())
      registerFactory("filteredIcon", new FilteredResizableIconFactory())
      registerFactory("iconDeck", new IconDeckIconFactory())
      registerFactory("svgIcon", new SvgIconFactory())
      registerFactory("wrapperIcon", new WrapperIconFactory())
   }

   public void registerFlamingoCommand() {
      registerFactory("commandButton", new CommandButtonFactory(JCommandButton))
      registerFactory("commandButtonPanel", new CommandButtonPanelFactory())
      registerFactory("commandButtonStrip", new CommandButtonStripFactory())
      registerFactory("commandMenuButton", new CommandButtonFactory(JCommandMenuButton))
      registerFactory("commandToggleButton", new CommandButtonFactory(JCommandToggleButton))
      registerFactory("commandToggleButtonGroup", new CommandToggleButtonGroupFactory())
      addAttributeDelegate(CommandToggleButtonGroupFactory.&buttonGroupAttributeDelegate)
   }

   public void registerFlamingoSlider() {
      registerFactory("flexiSlider", new FlexiSliderFactory())
      registerFactory("range", new FlexiRangeFactory())
      registerFactory("controlPoint", new FlexiControlPointFactory())
   }

   public void registerFlamingoBreadcrumb() {
      registerFactory("breadcrumbBar", new BreadcrumbBarFactory())
      registerFactory("fileBreadcrumbBar", new BreadcrumbFileSelectorFactory())
      registerFactory("treeBreadcrumbBar", new BreadcrumbTreeAdapterSelectorFactory())
//       registerFactory("breadcrumbItem", new BreadcrumbItemFactory())
   }

   public void registerFlamingoRibbon() {
      registerFactory("ribbonFrame", new RibbonFrameFactory())
      registerFactory("ribbon", new RibbonFactory())
      registerFactory("ribbonTask", new RibbonTaskFactory())
      registerFactory("ribbonContextualTaskGroup", new RibbonContextualTaskGroupFactory())
      registerFactory("ribbonBand", new RibbonBandFactory())
      registerFactory("ribbonFlowBand", new FlowRibbonBandFactory())
      registerFactory("ribbonComponent", new RibbonComponentFactory())
      registerFactory("ribbonApplicationMenu", new RibbonApplicationMenuFactory())
      registerFactory("ribbonApplicationMenuEntryPrimary", new RibbonApplicationMenuEntryPrimaryFactory())
      registerFactory("ribbonApplicationMenuEntryFooter", new RibbonApplicationMenuEntryFooterFactory())
   }
}