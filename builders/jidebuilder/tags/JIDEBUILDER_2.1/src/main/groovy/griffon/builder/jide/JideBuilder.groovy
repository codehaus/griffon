/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.jide

import java.beans.PropertyChangeListener
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JComboBox
import javax.swing.JTextField
import javax.swing.text.JTextComponent
import javax.swing.border.EtchedBorder
import java.text.NumberFormat

import groovy.swing.SwingBuilder
import groovy.swing.factory.*
import griffon.builder.jide.factory.*
import griffon.builder.jide.impl.*
import java.util.logging.Level
import java.util.logging.Logger

import com.jidesoft.dialog.*
import com.jidesoft.popup.*
import com.jidesoft.spinner.*
import com.jidesoft.swing.*
import com.jidesoft.converter.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class JideBuilder extends SwingBuilder {
   private Logger log = Logger.getLogger(getClass().getName())

   public JideBuilder( boolean init = true ) {
      super( init )
   }

   public void registerSVGAlias( String alias, String path ) {
      SVGIconFactory.registerSVGAlias( alias, path )
   }

   def registerJideSupportNodes() {
      addAttributeDelegate(JideBuilder.&selectAllAttributeDelegate)
      registerFactory("popupMenuCustomizer", new PopupMenuCustomizerFactory())
      registerBeanFactory("splitButtonGroup", SplitButtonGroup)
   }

   def registerJideWidgets() {
      registerBeanFactory("calculator", Calculator)
      registerBeanFactory("checkBoxList", CheckBoxList)
      registerBeanFactory("checkBoxTree", CheckBoxTree)
      registerBeanFactory("dateSpinner", DateSpinner)
      registerBeanFactory("folderChooser", FolderChooser)
      registerBeanFactory("gripper", Gripper)
      registerBeanFactory("infiniteProgressPanel", InfiniteProgressPanel)
      registerBeanFactory("meterProgressBar", MeterProgressBar)
      registerBeanFactory("pointSpinner", PointSpinner)
      registerBeanFactory("rangeSlider", RangeSlider)
      registerFactory("jideSvgIcon", new SVGIconFactory())
      registerFactory("titledSeparator", new TitledSeparatorFactory())

      // delegates
      addAttributeDelegate(JideBuilder.&calculatorAttributeDelegate)
   }

   def registerJideMenuWidgets() {
      registerFactory("jideMenu", new JideMenuFactory())
      registerBeanFactory("jidePopup", JidePopup)
      registerBeanFactory("jidePopupMenu", JidePopupMenu)
   }

   def registerJideAutocompletionWidgets() {
      registerFactory("animator", new AnimatorFactory())
      registerFactory("autoCompletion", new AutoCompletionFactory())
      registerFactory("autoCompletionComboBox", new AutoCompletionComboBoxFactory())
   }

   def registerJideActionButtonWidgets() {
      registerBeanFactory("headerBox", HeaderBox)
      registerFactory("jideButton", new RichActionWidgetFactory(JideButton))
      registerFactory("jideSplitButton", new JideSplitButtonFactory())
      registerFactory("jideToggleButton", new RichActionWidgetFactory(JideToggleButton))
      registerFactory("jideToggleSplitButton", new JideToggleSplitButtonFactory())
      registerFactory("multilineToggleButton", new MultilineToggleButtonFactory())
      registerFactory("nullButton", new RichActionWidgetFactory(NullButton))
      registerFactory("nullCheckBox", new RichActionWidgetFactory(NullCheckBox))
      registerFactory("nullJideButton", new RichActionWidgetFactory(NullJideButton))
      registerFactory("nullRadioButton", new RichActionWidgetFactory(NullRadioButton))
      registerFactory("nullTristateCheckBox", new NullTristateCheckBoxFactory())
      registerFactory("tristateCheckBox", new TristateCheckBoxFactory())

      // TODO CornerScroller
   }

   def registerJideTextWidgets() {
      registerFactory("autoResizingTextArea", new TextArgWidgetFactory(AutoResizingTextArea))
      registerFactory("clickThroughLabel", new TextArgWidgetFactory(ClickThroughLabel))
      registerFactory("clickThroughStyledLabel", new TextArgWidgetFactory(ClickThroughStyledLabel))
      registerFactory("labeledTextField", new LabeledTextFieldFactory())
      registerFactory("multilineLabel", new TextArgWidgetFactory(MultilineLabel))
      registerFactory("nullLabel", new TextArgWidgetFactory(NullLabel))
      registerFactory("tableSearchable", new TableSearchableFactory())
      registerFactory("textComponentSearchable", new TextComponentSearchableFactory())
      registerFactory("styledLabel", new TextArgWidgetFactory(StyledLabel))
   }

   def registerJideOverlayableWidgets() {
      registerFactory("overlayCheckBox", new RichActionWidgetFactory(OverlayCheckBox))
      registerFactory("overlayComboBox", new OverlayComboBoxFactory())
      registerFactory("overlayRadioButton", new RichActionWidgetFactory(OverlayRadioButton))
      registerFactory("overlayTextArea", new TextArgWidgetFactory(OverlayTextArea))
      registerFactory("overlayTextField", new TextArgWidgetFactory(OverlayTextField))
   }

   def registerJideSearchableWidgets() {
      registerBeanFactory("checkBoxListWithSelectable", CheckBoxListWithSelectable)
      registerFactory("comboBoxSearchable", new ComboBoxSearchableFactory())
      registerFactory("listSearchable", new ListSearchableFactory())
      registerFactory("treeSearchable", new TreeSearchableFactory())
      registerFactory("searchableBar", new SearchableBarFactory())
   }

   def registerJideContainers() {
      registerBeanFactory("bannerPanel", BannerPanel)
      registerBeanFactory("buttonPanel", com.jidesoft.dialog.ButtonPanel)
      registerBeanFactory("contentContainer", ContentContainer)
      registerBeanFactory("jideOptionPane", JideOptionPane)
      registerBeanFactory("jideScrollPane", JideScrollPane)
      registerBeanFactory("jideSplitPane", JideSplitPane)
      registerFactory("jideTabbedPane", new TabbedPaneFactory(JideTabbedPane))
      registerBeanFactory("nullPanel", NullPanel)
      registerBeanFactory("paintPanel", PaintPanel)
      registerBeanFactory("resizablePanel", ResizablePanel)
      registerBeanFactory("simpleScrollPane", SimpleScrollPane)
      registerBeanFactory("scrollableButtonPanel", ScrollableButtonPanel)
   }

   def registerJideWindows() {
      registerFactory("resizableDialog", new ResizableDialogFactory())
      registerFactory("resizableFrame", new ResizableFrameFactory())
      registerFactory("resizableWindow", new ResizableWindowFactory())

      registerFactory("multiplePageDialog", new MultiplePageDialogFactory())
      registerBeanFactory("multiplePageDialogPane", MultiplePageDialogPane)
      registerBeanFactory("dialogPage", DefaultDialogPage)
      registerFactory("standardDialog", new StandardDialogFactory())
      registerFactory("dialogBannerPanel", new StandardDialogPaneFactory(DialogBannerPanel))
      registerFactory("dialogContentPanel", new StandardDialogPaneFactory(DialogContentPanel))
      registerFactory("dialogButtonPanel", new StandardDialogPaneFactory(DialogButtonPanel))
   }

   def registerJideIntelliHints() {
      registerFactory("fileIntelliHints", new FileIntelliHintsFactory())
      registerFactory("listDataIntelliHints", new ListDataIntelliHintsFactory())
   }

   def registerJideLayouts() {
      registerFactory("jideBorderLayout", new LayoutFactory(JideBorderLayout))
      registerFactory("jideBoxLayout", new JideBoxLayoutFactory())

      // JideSwingUtilities
      registerFactory("left", new JideSwingUtilitiesPanelFactory("left"))
      registerFactory("right", new JideSwingUtilitiesPanelFactory("right"))
      registerFactory("top", new JideSwingUtilitiesPanelFactory("top"))
      registerFactory("bottom", new JideSwingUtilitiesPanelFactory("bottom"))
      registerFactory("center", new JideSwingUtilitiesPanelFactory("center"))
   }

   def registerJideBorders() {
      registerFactory("partialLineBorder", new PartialLineBorderFactory())
      registerFactory("partialEtchedBorder", new PartialEtchedBorderFactory(EtchedBorder.LOWERED))
      registerFactory("partialLoweredEtchedBorder", new PartialEtchedBorderFactory(EtchedBorder.LOWERED))
      registerFactory("partialRaisedEtchedBorder", new PartialEtchedBorderFactory(EtchedBorder.RAISED))
      registerFactory("partialGradientLineBorder", new PartialGradientLineBorderFactory())
   }

   /*
   def registerJideConverters() {
      registerExplicitMethod("registerConverter", { Class clazz, ObjectConverter converter, ConverterContext context = ConverterContext.DEFAULT_CONTEXT ->
      ObjectConverterManager.registerConverter(clazz, converter, context) })
      registerExplicitMethod("unregisterConverter", { Class clazz, ConverterContext context = ConverterContext.DEFAULT_CONTEXT ->
      ObjectConverterManager.unregisterConverter(clazz, context) })

      registerExplicitMethod("arrayConverter", { Map args = [:] ->
         String separator = args.remove("separator")
         Class clazz = args.remove("class")
         if( separator == null || !clazz ) {
            throw new RuntimeException("In arrayConverter you must specify values for separator: and class:")
         }
         new DefaultArrayConverter(separator,clazz)
      })
      registerExplicitMethod("bigDecimalConverter", { new BigDecimalConverter() })
      registerExplicitMethod("booleanConverter", { new BooleanConverter() })
      registerExplicitMethod("byteConverter", { NumberFormat format = null ->
         return format ? new ByteConverter(format): new ByteConverter()
      })
      registerExplicitMethod("calendarConverter", { new CalendarConverter() })
      registerExplicitMethod("currencyConverter", { NumberFormat format = null ->
         return format ? new CurrencyConverter(format): new CurrencyConverter()
      })
      registerExplicitMethod("dateConverter", { new DateConverter() })
      registerExplicitMethod("dimensionConverter", { new DimensionConverter() })
      registerExplicitMethod("doubleConverter", { NumberFormat format = null ->
         return format ? new DoubleConverter(format): new DoubleConverter()
      })
      registerExplicitMethod("enumConverter", { String name, Class type, Object[] values, String[] strings, Object defaultValue = null ->
         new EnumConverter(name,type,values,strings,defaultValue)
      })
      registerExplicitMethod("fileConverter", { new FileConverter() })
      registerExplicitMethod("floatConverter", { NumberFormat format = null ->
         return format ? new FloatConverter(format): new FloatConverter()
      })
      registerExplicitMethod("fontConverter", { new FontConverter() })
      registerExplicitMethod("fontNameConverter", { new FontNameConverter() })
      registerExplicitMethod("hexColorConverter", { new HexColorConverter() })
      registerExplicitMethod("insetsConverter", { new InsetsConverter() })
      registerExplicitMethod("integerConverter", { NumberFormat format = null ->
         return format ? new IntegerConverter(format): new IntegerConverter()
      })
      registerExplicitMethod("longConverter", { NumberFormat format = null ->
         return format ? new LongConverter(format): new LongConverter()
      })
      registerExplicitMethod("monthConverter", { new MonthConverter() })
      registerExplicitMethod("monthNameConverter", { new MonthNameConverter() })
      registerExplicitMethod("multilineStringConverter", { new MultilineStringConverter() })
      registerExplicitMethod("multipleEnumConverter", { Map args = [:] ->
         String separator = args.remove("separator")
         EnumConverter converter = args.remove("converter")
         if( separator == null || !converter ) {
            throw new RuntimeException("In multipleEnumConverter you must specify values for separator: and converter:")
         }
         new MultipleEnumConverter(separator,converter)
      })
      registerExplicitMethod("naturalNumberConverter", { NumberFormat format = null ->
         return format ? new NaturalNumberConverter(format): new NaturalNumberConverter()
      })
      registerExplicitMethod("objectConverter", { new DefaultObjectConverter() })
      registerExplicitMethod("passwordConverter", { new PasswordConverter() })
      registerExplicitMethod("percentConverter", { NumberFormat format = null ->
         return format ? new PercentConverter(format): new PercentConverter()
      })
      registerExplicitMethod("pointConverter", { new PointConverter() })
      registerExplicitMethod("quarterNameConverter", { String pattern = null ->
         def converter = new QuarterNameConverter()
         if( pattern != null ) converter.setQuarterNamePattern(pattern)
         return converter
      })
      registerExplicitMethod("rectangleConverter", { new RectangleConverter() })
      registerExplicitMethod("rgbColorConverter", { new RgbColorConverter() })
      registerExplicitMethod("shortConverter", { NumberFormat format = null ->
         return format ? new ShortConverter(format): new ShortConverter()
      })
      registerExplicitMethod("stringarrayConverter", { String separator = null ->
         return separator ? new StringArrayConverter(separator): new StringArrayConverter()
      })
      registerExplicitMethod("stringConverter", { new com.jidesoft.converter.StringConverter() })
      registerExplicitMethod("yearNameConverter", { new YearNameConverter() })
   }
   */

   public static selectAllAttributeDelegate( def builder, def node, def attributes ) {
      if( node instanceof JTextComponent && attributes.remove("selectAll") ){
         SelectAllUtils.install( node )
      }
   }

   public static calculatorAttributeDelegate( def builder, def node, def attributes ) {
      if( node instanceof Calculator ){
         JTextComponent textComponent = attributes.remove("textComponent")
         if( textComponent ){
            SelectAllUtils.install( textComponent )
            textComponent.setHorizontalAlignment(JTextField.TRAILING)
            node.registerKeyboardActions(textComponent, JComponent.WHEN_FOCUSED)
            node.addPropertyChangeListener(Calculator.PROPERTY_DISPLAY_TEXT,[
               propertyChange: { event ->
                  textComponent.text = "" + event.getNewValue()
               }] as PropertyChangeListener )
         }
      }
   }
}