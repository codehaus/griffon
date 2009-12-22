/*
 * Copyright 2009 the original author or authors.
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

package griffon.pivot

import griffon.pivot.factory.*
import griffon.pivot.impl.*
import org.apache.pivot.wtk.*
import org.apache.pivot.wtk.content.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class PivotBuilder extends FactoryBuilderSupport {
    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";
 
    public PivotBuilder(boolean init = true) {
        super( init )
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }
 
    void registerPivotSupportNodes() {
        registerPivotBeanFactory('action', DefaultAction)
        registerFactory('actions', new JavaCollectionFactory())
        registerFactory('noparent', new JavaCollectionFactory())
        registerFactory('wtkx', new WTKXFactory())
        addAttributeDelegate(PivotBuilder.&objectIDAttributeDelegate)
        registerFactory('buttonGroup', new ButtonGroupFactory())
        addAttributeDelegate(ButtonGroupFactory.&buttonGroupAttributeDelegate)

        registerFactory('dimensions', new PairFactory(Dimensions, 'width', 'height'))
        registerFactory('point', new PairFactory(Point, 'x', 'y'))
        registerFactory('span', new PairFactory(Span, 'start', 'end'))
    }

    def registerPivotPassThruNodes() {
        registerFactory("widget", new WidgetFactory(Component, true))
        registerFactory("container", new WidgetFactory(Component, false))
        registerFactory("bean", new WidgetFactory(Object, true))
    }
 
    void registerPivotWidgets() {
        registerPivotComponentFactory('activityIndicator', ActivityIndicator)
        registerPivotComponentFactory('fileBrowser', FileBrowser)
        registerPivotComponentFactory('label', Label)
        registerPivotComponentFactory('meter', Meter)
        registerPivotComponentFactory('separator', Separator)
        registerPivotComponentFactory('textArea', TextArea)
        registerPivotComponentFactory('textInput', TextInput)

        registerFactory('slider', new SliderFactory())
        registerPivotComponentFactory('spinner', Spinner, false)
        registerFactory('numericSpinnerData', new NumericSpinnerDataFactory())
        registerFactory('calendarDateSpinnerData', new CalendarDateSpinnerDataFactory())
        registerPivotComponentFactory('scrollBar', ScrollBar)
        registerFactory('scrollBarScope', new ScrollBarScopeFactory())
    }

    void registerPivotViews() {
        registerPivotComponentFactory('listView', ListView)
        registerPivotComponentFactory('imageView', ImageView)
        // registerPivotComponentFactory('movieView', MovieView)
        // registerPivotComponentFactory('tableView', TableView)
        // registerPivotComponentFactory('tableViewHeader', TableViewHeader)
        // registerPivotComponentFactory('treeView', TreeView)
    }

    void registerPivotButtons() {
        registerFactory('buttonData', new ButtonDataFactory())
        registerFactory('calendarButton', new ButtonFactory(CalendarButton))
        registerFactory('checkbox', new ButtonFactory(Checkbox))
        registerFactory('colorChooserButton', new ButtonFactory(ColorChooserButton))
        registerFactory('linkButton', new ButtonFactory(LinkButton))
        registerFactory('listButton', new ButtonFactory(ListButton))
        registerFactory('menuButton', new ButtonFactory(MenuButton))
        def button = new ButtonFactory(PushButton)
        registerFactory('button', button)
        registerFactory('pushButton', button)
        registerFactory('radioButton', new ButtonFactory(RadioButton))
    }

    void registerPivotMenus() {
        registerFactory('menu', new MenuFactory())
        registerFactory('menuItem', new MenuItemFactory())
        registerFactory('menuBar', new MenuBarFactory())
        registerFactory('menuBarItem', new MenuBarItemFactory())
        registerPivotComponentFactory('menuPopup', MenuPopup, false)
    }

    void registerPivotPanes() {
        def hbox = new BoxPaneFactory(Orientation.HORIZONTAL)
        registerContainerFactory('boxPane', hbox)
        registerContainerFactory('hbox', hbox)
        registerContainerFactory('vbox', new BoxPaneFactory(Orientation.VERTICAL))
        registerPivotContainerFactory('boxPane', BoxPane)
        registerPivotContainerFactory('cardPane', CardPane)
        registerPivotContainerFactory('flowPane', FlowPane)
        registerFactory('gridPane', new GridPaneFactory())
        registerFactory('gridRow', new GridPaneRowFactory())
        registerPivotContainerFactory('gridFiller', GridPane.Filler)
        registerFactory('scrollPane', new ScrollPaneFactory())
        registerFactory('splitPane', new SplitPaneFactory())
        registerPivotContainerFactory('stackPane', StackPane)
        registerFactory('tabPane', new TabPaneFactory())
        registerFactory('tablePane', new TablePaneFactory())
        registerFactory('tablePaneColumn', new TablePaneColumnFactory())
        registerFactory('tablePaneRow', new TablePaneRowFactory())
        registerPivotContainerFactory('tablePaneFiller', TablePane.Filler)
    }

    void registerPivotContainers() {
        registerFactory('accordion', new AccordionFactory())
        registerPivotContainerFactory('border', Border, true)
        registerPivotContainerFactory('calendar', Calendar)
        registerPivotContainerFactory('colorChooser', ColorChooser)
        registerPivotContainerFactory('expander', Expander, true)
        // registerPivotContainerFactory('form', Form)
        registerPivotContainerFactory('panel', Panel)
        registerFactory('panorama', new ViewportFactory(Panorama))
        registerFactory('rollup', new RollupFactory())
    }

    void registerPivotWindows() {
        // registerPivotContainerFactory('alert', Alert)
        // registerPivotContainerFactory('prompt', Prompt)
        registerPivotContainerFactory('dialog', Dialog, true)
        registerPivotContainerFactory('frame', Frame, true)
        registerFactory('fileBrowserSheet', new FileBrowserSheetFactory())
        registerPivotContainerFactory('palette', Palette, true)
        registerPivotContainerFactory('sheet', Sheet, true)
        registerPivotContainerFactory('tooltip', Tooltip, true)
        registerPivotContainerFactory('window', Window, true)
    }

    private void registerPivotBeanFactory(String name, Class pivotBeanClass, boolean leaf = true) {
        registerFactory(name, new BeanFactory(pivotBeanClass, leaf))
    }

    private void registerPivotComponentFactory(String name, Class pivotBeanClass, boolean leaf = true) {
        registerFactory(name, new PivotBeanFactory(pivotBeanClass, leaf))
    }
 
    private void registerPivotContainerFactory(String name, Class pivotBeanClass, boolean singleElement = false) {
        registerFactory(name, singleElement ? new SingleElementContainerFactory(pivotBeanClass) : new ContainerFactory(pivotBeanClass))
    }
 
    // taken from groovy.swing.SwingBuilder
    public static objectIDAttributeDelegate(def builder, def node, def attributes) {
        def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
        def theID = attributes.remove(idAttr)
        if(theID) {
            builder.setVariable(theID, node)
        }
    }
}
