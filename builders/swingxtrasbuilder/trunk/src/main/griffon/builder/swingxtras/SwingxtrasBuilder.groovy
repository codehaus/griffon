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

package griffon.builder.swingxtras

import groovy.swing.SwingBuilder
import groovy.swing.factory.RichActionWidgetFactory
import groovy.swing.factory.TextArgWidgetFactory
import groovy.swing.factory.TabbedPaneFactory
import groovy.swing.factory.TableFactory
import groovy.swing.factory.LayoutFactory
import griffon.builder.swingxtras.factory.*

import org.jdesktop.xswingx.*
import com.l2fprod.common.swing.*
import com.l2fprod.common.swing.tips.*
import com.l2fprod.common.propertysheet.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class SwingxtrasBuilder extends SwingBuilder {
   public SwingxtrasBuilder( boolean init = true ) {
      super( init )
   }

   public void registerBalloonTip() {
      registerFactory("balloonTip", new BalloonTipFactory())
      registerFactory("customBalloonTip", new CustomBalloonTipFactory())
      registerFactory("tablecellBalloonTip", new TablecellBalloonTipFactory())
      registerFactory("edgedBalloonStyle", new EdgedBalloonStyleFactory())
      registerFactory("minimalBalloonStyle", new MinimalBalloonStyleFactory())
      registerFactory("modernBalloonStyle", new ModernBalloonStyleFactory())
      registerFactory("roundedBalloonStyle", new RoundedBalloonStyleFactory())
      registerFactory("texturedBalloonStyle", new TexturedBalloonStyleFactory())
   }

   public void registerXSwingX() {
      registerFactory("searchField", new SearchFieldFactory())
      registerFactory("promptTextArea", new TextArgWidgetFactory(JXTextArea))
      registerFactory("promptTextField", new TextArgWidgetFactory(JXTextField))
      registerFactory("promptSupport", new PromptSupportFactory())
      registerFactory("buddySupport", new BuddySupportFactory())
   }

   public void registerL2fprod() {
      registerBeanFactory("jbannerPanel", BannerPanel)
      registerFactory("jbuttonBar", new JButtonBarFactory())
      registerFactory("linkButton", new RichActionWidgetFactory(JLinkButton))
      registerBeanFactory("jtipOfTheDay", JTipOfTheDay)
      registerBeanFactory("jdefaultTipModel", DefaultTipModel)
      registerBeanFactory("jdefaultTip", DefaultTip)
      registerBeanFactory("jtaskPaneGroup", JTaskPaneGroup)
      registerBeanFactory("jtaskPane", JTaskPane)
      registerFactory("outlookBar", new TabbedPaneFactory(JOutlookBar))
      registerBeanFactory("directoryChooser", JDirectoryChooser)
      registerBeanFactory("fontChooser", JFontChooser)
      registerFactory("percentLayout", new LayoutFactory(PercentLayout))
      registerFactory("propertySheetPanel", new PropertySheetPanelFactory())
      registerFactory("propertySheetTable", new TableFactory(PropertySheetTable))
      registerFactory("propertySheetTableModel", new PropertySheetTableModelFactory())
      registerFactory("property", new PropertyFactory())
      // statusBar
      // -- zone
      // baseDialog
      // -- banner
      // -- content
      // propertySheetDialog
   }
}