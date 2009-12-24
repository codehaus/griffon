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
import griffon.pivot.adapters.*
import org.apache.pivot.wtk.*
import org.apache.pivot.wtk.content.*
import org.apache.pivot.wtk.effects.*
import org.apache.pivot.wtk.effects.easing.*
import org.apache.pivot.wtk.media.*
import org.apache.pivot.wtk.media.drawing.*
import org.apache.pivot.wtk.text.*

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
        registerFactory('insets', new InsetsFactory())
        registerFactory('bounds', new BoundsFactory())
    }

    def registerPivotPassThruNodes() {
        registerFactory("widget", new WidgetFactory(Component, true))
        registerFactory("container", new WidgetFactory(Component, false))
        registerFactory("bean", new WidgetFactory(Object, true))
    }
 
    void registerPivotWidgets() {
        registerPivotComponentFactory('activityIndicator', ActivityIndicator)
        registerPivotComponentFactory('fileBrowser', FileBrowser)
        registerFactory('label', new TextComponentFactory(Label))
        registerFactory('meter', new TextComponentFactory(Meter))
        registerPivotComponentFactory('separator', Separator)
        registerFactory('textArea', new TextComponentFactory(TextArea))
        registerFactory('textInput', new TextComponentFactory(TextInput))

        registerFactory('slider', new SliderFactory())
        registerPivotComponentFactory('spinner', Spinner)
        registerFactory('numericSpinnerData', new NumericSpinnerDataFactory())
        registerFactory('calendarDateSpinnerData', new CalendarDateSpinnerDataFactory())
        registerPivotComponentFactory('scrollBar', ScrollBar)
        registerFactory('scrollBarScope', new ScrollBarScopeFactory())
    }

    void registerPivotViews() {
        registerPivotComponentFactory('listView', ListView)
        registerFactory('imageView', new ImageViewFactory())
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

        registerFactory('buttonDataRenderer', new ButtonDataRendererFactory(ButtonDataRenderer))
        registerFactory('calendarButtonDataRenderer', new ButtonDataRendererFactory(CalendarButtonDataRenderer))
        registerFactory('linkButtonDataRenderer', new ButtonDataRendererFactory(LinkButtonDataRenderer))
        registerFactory('listButtonColorItemRenderer', new ButtonDataRendererFactory(ListButtonColorItemRenderer))
        registerFactory('listButtonDataRenderer', new ButtonDataRendererFactory(ListButtonDataRenderer))
        registerFactory('menuButtonDataRenderer', new ButtonDataRendererFactory(MenuButtonDataRenderer))
        registerFactory('buttonDataRenderer', new ButtonDataRendererFactory(ButtonDataRenderer))
        registerFactory('buttonDataRenderer', new ButtonDataRendererFactory(ButtonDataRenderer))
    }

    void registerPivotMenus() {
        registerFactory('menu', new MenuFactory())
        registerFactory('menuItem', new MenuItemFactory())
        registerFactory('menuBar', new MenuBarFactory())
        registerFactory('menuBarItem', new MenuBarItemFactory())
        registerPivotComponentFactory('menuPopup', MenuPopup)
        registerFactory('menuBarItemDataRenderer', new ButtonDataRendererFactory(MenuBarItemDataRenderer))
        registerFactory('menuItemDataRenderer', new ButtonDataRendererFactory(MenuItemDataRenderer))
    }

    void registerPivotPanes() {
        def hbox = new BoxPaneFactory(Orientation.HORIZONTAL)
        registerFactory('boxPane', hbox)
        registerFactory('hbox', hbox)
        registerFactory('vbox', new BoxPaneFactory(Orientation.VERTICAL))
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
        registerFactory('tooltip', new TextComponentFactory(Tooltip))
        registerPivotContainerFactory('window', Window, true)
    }

    void registerPivotEffects() {
        registerPivotBeanFactory('baselineDecorator', BaselineDecorator)
        registerPivotBeanFactory('blurDecorator', BlurDecorator)
        registerPivotBeanFactory('clipDecorator', ClipDecorator)
        registerPivotBeanFactory('dropShadowDecorator', DropShadowDecorator)
        registerPivotBeanFactory('fadeDecorator', fadeDecorator)
        registerPivotBeanFactory('grayscaleDecorator', GrayscaleDecorator)
        registerPivotBeanFactory('reflectionDecorator', ReflectionDecorator)
        registerPivotBeanFactory('rotationDecorator', RotationDecorator)
        registerPivotBeanFactory('saturationDecorator', SaturationDecorator)
        registerPivotBeanFactory('scaleDecorator', ScaleDecorator)
        registerPivotBeanFactory('shadeDecorator', ShadeDecorator)
        registerPivotBeanFactory('translationDecorator', TranslationDecorator)
        registerFactory('overlayDecorator', new SingleElementContainerFactory(OverlayDecorator, 'component', Component))
        registerFactory('tagDecorator', new SingleElementContainerFactory(TagDecorator, 'tag', Visual))
        registerFactory('watermarkDecorator', new SingleElementContainerFactory(WatermarkDecorator, 'image', Image))

        registerBeanFactory('easingCircular', Circular)
        registerBeanFactory('easingCubic', Cubic)
        registerBeanFactory('easingExponential', Exponential)
        registerBeanFactory('easingLinear', Linear)
        registerBeanFactory('easingQuadratic', Quadratic)
        registerBeanFactory('easingQuartic', Quartic)
        registerBeanFactory('easingQuintic', Quintic)
        registerBeanFactory('easingSine', Sine)
    }

    void registerPivotMedia() {
        registerPivotComponentFactory('drawing', Drawing)
        registerFactory('picture', new PictureFactory())

        registerFactory('canvas', new GroupFactory(Canvas))
        registerFactory('group', new GroupFactory(Group))
        registerPivotComponentFactory('arc', Arc)
        registerPivotComponentFactory('cubicCurve', CubicCurve)
        registerPivotComponentFactory('ellipse', Elllipse)
        registerPivotComponentFactory('line', Line)
        registerPivotComponentFactory('quadCurve', QuadCurve)
        registerPivotComponentFactory('rectangle', Rectangle)
        registerPivotComponentFactory('rect', Rectangle)
        registerPivotComponentFactory('text', Text)
        registerFactory('path', new PathFactory())
        registerPivotComponentFactory('moveTo', Path.MoveTo)
        registerPivotComponentFactory('lineTo', Path.LineTo)
        registerPivotComponentFactory('curveTo', Path.CurveTo)
        registerPivotComponentFactory('quadTo', Path.QuadTo)
        registerFactory('transforms', new TransformsFactory())
        registerPivotComponentFactory('rotate', Shape.Rotate, true)
        registerPivotComponentFactory('scale', Shape.Scale, true)
        registerPivotComponentFactory('translate', Shape.Translate, true)

        registerFactory('drawingListener', new EventListenerFactory(DrawingListenerAdapter, [Drawing], 'getDrawingListeners'))
        registerFactory('imageListener', new EventListenerFactory(ImageListenerAdapter, [Image], 'getImageListeners'))
        registerFactory('movieListener', new EventListenerFactory(MovieListenerAdapter, [Movie], 'getMovieListeners'))

        registerFactory('arcListener', new EventListenerFactory(ArcListenerAdapter, [Arc], 'getArcListeners'))
        registerFactory('canvasListener', new EventListenerFactory(CanvasListenerAdapter, [Canvas], 'getCanvasListeners'))
        registerFactory('cubicCurveListener', new EventListenerFactory(CubicCurveListenerAdapter, [CubicCurve], 'getCubicCurveListeners'))
        registerFactory('ellipseListener', new EventListenerFactory(EllipseListenerAdapter, [Ellipse], 'getEllipseListeners'))
        registerFactory('groupListener', new EventListenerFactory(GroupListenerAdapter, [Group], 'getGroupListeners'))
        registerFactory('lineListener', new EventListenerFactory(LineListenerAdapter, [Line], 'getLineListeners'))
        registerFactory('pathListener', new EventListenerFactory(PathListenerAdapter, [Path], 'getPathListeners'))
        registerFactory('quadCurveListener', new EventListenerFactory(QuadCurveListenerAdapter, [QuadCurve], 'getQuadCurveListeners'))
        registerFactory('rectangleListener', new EventListenerFactory(RectangleListenerAdapter, [Rectangle], 'getRectangleListeners'))
        registerFactory('shapeListener', new EventListenerFactory(ShapeListenerAdapter, [Shape], 'getShapeListeners'))
        registerFactory('shapeTransformListener', new EventListenerFactory(ShapeTransformListenerAdapter, [Shape], 'getShapeTransformListeners'))
        registerFactory('textListener', new EventListenerFactory(TextListenerAdapter, [Text], 'getTextListeners'))
    }

    void registerPivotText() {
        registerFactory('elementListener', new EventListenerFactory(ElementListenerAdapter, [Element], 'getElementListeners'))
        registerFactory('imageNodeListener', new EventListenerFactory(ImageNodeListenerAdapter, [ImageNode], 'getImageNodeListeners'))
        registerFactory('nodeListener', new EventListenerFactory(NodeListenerAdapter, [Node], 'getNodeListeners'))
        registerFactory('textNodeListener', new EventListenerFactory(TextNodeListenerAdapter, [TextNode], 'getTextNodeListeners'))
    }

    void registerPivotListeners() {
        registerFactory('accordionAttributeListener', new EventListenerFactory(AccordionAttributeListenerAdapter, [Accordion], 'getAccordionAttributeListeners'))
        registerFactory('accordionListener', new EventListenerFactory(AccordionListenerAdapter, [Accordion], 'getAccordionListeners'))
        registerFactory('accordionSelectionListener', new EventListenerFactory(AccordionSelectionListenerAdapter, [Accordion], 'getAccordionSelectionListeners'))
        registerFactory('actionClassListener', new EventListenerFactory(ActionClassListenerAdapter, [Action], 'getActionClassListeners'))
        registerFactory('actionListener', new EventListenerFactory(ActionListenerAdapter, [Action], 'getActionListeners'))
        registerFactory('activityIndicatorListener', new EventListenerFactory(ActivityIndicatorListenerAdapter, [ActivityIndicator], 'getActivityIndicatorListeners'))
        registerFactory('alertListener', new EventListenerFactory(AlertListenerAdapter, [Alert], 'getAlertListeners'))
        registerFactory('borderListener', new EventListenerFactory(BorderListenerAdapter, [Border], 'getBorderListeners'))
        registerFactory('boxPaneListener', new EventListenerFactory(BoxPaneListenerAdapter, [BoxPane], 'getBoxPaneListeners'))
        registerFactory('buttonGroupListener', new EventListenerFactory(ButtonGroupListenerAdapter, [ButtonGroup], 'getButtonGroupListeners'))
        registerFactory('buttonListener', new EventListenerFactory(ButtonListenerAdapter, [Button], 'getButtonListeners'))
        registerFactory('buttonPressListener', new EventListenerFactory(ButtonPressListenerAdapter, [Button], 'getButtonPressListeners'))
        registerFactory('buttonStateListener', new EventListenerFactory(ButtonStateListenerAdapter, [Button], 'getButtonStateListeners'))
        registerFactory('calendarButtonListener', new EventListenerFactory(CalendarButtonListenerAdapter, [CalendarButton], 'getCalendarButtonListeners'))
        registerFactory('calendarButtonSelectionListener', new EventListenerFactory(CalendarButtonSelectionListenerAdapter, [CalendarButton], 'getCalendarButtonSelectionListeners'))
        registerFactory('calendarListener', new EventListenerFactory(CalendarListenerAdapter, [Calendar], 'getCalendarListeners'))
        registerFactory('calendarSelectionListener', new EventListenerFactory(CalendarSelectionListenerAdapter, [Calendar], 'getCalendarSelectionListeners'))
        registerFactory('cardPaneListener', new EventListenerFactory(CardPaneListenerAdapter, [CardPane], 'getCardPaneListeners'))
        registerFactory('colorChooserButtonListener', new EventListenerFactory(ColorChooserButtonListenerAdapter, [ColorChooserButton], 'getColorChooserButtonListeners'))
        registerFactory('colorChooserButtonSelectionListener', new EventListenerFactory(ColorChooserButtonSelectionListenerAdapter, [ColorChooserButton], 'getColorChooserButtonSelectionListeners'))
        registerFactory('colorChooserListener', new EventListenerFactory(ColorChooserListenerAdapter, [ColorChooser], 'getColorChooserListeners'))
        registerFactory('colorChooserSelectionListener', new EventListenerFactory(ColorChooserSelectionListenerAdapter, [ColorChooser], 'getColorChooserSelectionListeners'))
        registerFactory('componentClassListener', new EventListenerFactory(ComponentClassListenerAdapter, [Component], 'getComponentClassListeners'))
        registerFactory('componentDataListener', new EventListenerFactory(ComponentDataListenerAdapter, [Component], 'getComponentDataListeners'))
        registerFactory('componentDecoratorListener', new EventListenerFactory(ComponentDecoratorListenerAdapter, [Component], 'getComponentDecoratorListeners'))
        registerFactory('componentKeyListener', new EventListenerFactory(ComponentKeyListenerAdapter, [Component], 'getComponentKeyListeners'))
        registerFactory('componentListener', new EventListenerFactory(ComponentListenerAdapter, [Component], 'getComponentListeners'))
        registerFactory('componentMouseButtonListener', new EventListenerFactory(ComponentMouseButtonListenerAdapter, [Component], 'getComponentMouseButtonListeners'))
        registerFactory('componentMouseListener', new EventListenerFactory(ComponentMouseListenerAdapter, [Component], 'getComponentMouseListeners'))
        registerFactory('componentMouseWheelListener', new EventListenerFactory(ComponentMouseWheelListenerAdapter, [Component], 'getComponentMouseWheelListeners'))
        registerFactory('componentStateListener', new EventListenerFactory(ComponentStateListenerAdapter, [Component], 'getComponentStateListeners'))
        registerFactory('containerListener', new EventListenerFactory(ContainerListenerAdapter, [Container], 'getContainerListeners'))
        registerFactory('containerMouseListener', new EventListenerFactory(ContainerMouseListenerAdapter, [Container], 'getContainerMouseListeners'))
        registerFactory('dialogStateListener', new EventListenerFactory(DialogStateListenerAdapter, [Dialog], 'getDialogStateListeners'))
        registerFactory('expanderListener', new EventListenerFactory(ExpanderListenerAdapter, [Expander], 'getExpanderListeners'))
        registerFactory('fileBrowserListener', new EventListenerFactory(FileBrowserListenerAdapter, [FileBrowser], 'getFileBrowserListeners'))
        registerFactory('fileBrowserSheetListener', new EventListenerFactory(FileBrowserSheetListenerAdapter, [FileBrowserSheet], 'getFileBrowserSheetListeners'))
        registerFactory('formAttributeListener', new EventListenerFactory(FormAttributeListenerAdapter, [Form], 'getFormAttributeListeners'))
        registerFactory('formListener', new EventListenerFactory(FormListenerAdapter, [Form], 'getFormListeners'))
        registerFactory('frameListener', new EventListenerFactory(FrameListenerAdapter, [Frame], 'getFrameListeners'))
        registerFactory('gridPaneListener', new EventListenerFactory(GridPaneListenerAdapter, [GridPane], 'getGridPaneListeners'))
        registerFactory('imageViewListener', new EventListenerFactory(ImageViewListenerAdapter, [ImageView], 'getImageViewListeners'))
        registerFactory('labelListener', new EventListenerFactory(LabelListenerAdapter, [Label], 'getLabelListeners'))
        registerFactory('listButtonListener', new EventListenerFactory(ListButtonListenerAdapter, [ListButton], 'getListButtonListeners'))
        registerFactory('listButtonSelectionListener', new EventListenerFactory(ListButtonSelectionListenerAdapter, [ListButton], 'getListButtonSelectionListeners'))
        registerFactory('listViewItemEditorListener', new EventListenerFactory(ListViewItemEditorListenerAdapter, [ListView.ItemEditor], 'getItemEditorListeners'))
        registerFactory('listViewItemListener', new EventListenerFactory(ListViewItemListenerAdapter, [ListView], 'getListViewItemListeners'))
        registerFactory('listViewItemStateListener', new EventListenerFactory(ListViewItemStateListenerAdapter, [ListView], 'getListViewItemStateListeners'))
        registerFactory('listViewListener', new EventListenerFactory(ListViewListenerAdapter, [ListView], 'getListViewListeners'))
        registerFactory('listViewSelectionListener', new EventListenerFactory(ListViewSelectionListenerAdapter, [ListView], 'getListViewSelectionListeners'))
        registerFactory('menuItemListener', new EventListenerFactory(MenuItemListenerAdapter, [MenuBar.Item, Menu.Item], 'getItemListeners'))
        registerFactory('menuSectionListener', new EventListenerFactory(MenuSectionListenerAdapter, [Menu.Section], 'getSectionListeners'))
        registerFactory('menuBarListener', new EventListenerFactory(MenuBarListenerAdapter, [MenuBar], 'getMenuBarListeners'))
        registerFactory('menuButtonListener', new EventListenerFactory(MenuButtonListenerAdapter, [MenuButton], 'getMenuButtonListeners'))
        registerFactory('menuItemSelectionListener', new EventListenerFactory(MenuItemSelectionListenerAdapter, [Menu], 'getMenuItemSelectionListeners'))
        registerFactory('menuListener', new EventListenerFactory(MenuListenerAdapter, [Menu], 'getMenuListeners'))
        registerFactory('menuPopupListener', new EventListenerFactory(MenuPopupListenerAdapter, [MenuPopup], 'getMenuPopupListeners'))
        registerFactory('menuPopupStateListener', new EventListenerFactory(MenuPopupStateListenerAdapter, [MenuPopup], 'getMenuPopupStateListeners'))
        registerFactory('meterListener', new EventListenerFactory(MeterListenerAdapter, [Meter], 'getMeterListeners'))
        registerFactory('movieViewListener', new EventListenerFactory(MovieViewListenerAdapter, [MovieView], 'getMovieViewListeners'))
        registerFactory('promptListener', new EventListenerFactory(PromptListenerAdapter, [Prompt], 'getPromptListeners'))
        registerFactory('rollupListener', new EventListenerFactory(RollupListenerAdapter, [Rollup], 'getRollupListeners'))
        registerFactory('rollupStateListener', new EventListenerFactory(RollupStateListenerAdapter, [Rollup], 'getRollupStateListeners'))
        registerFactory('scrollBarListener', new EventListenerFactory(ScrollBarListenerAdapter, [ScrollBar], 'getScrollBarListeners'))
        registerFactory('scrollBarValueListener', new EventListenerFactory(ScrollBarValueListenerAdapter, [ScrollBar], 'getScrollBarValueListeners'))
        registerFactory('scrollPaneListener', new EventListenerFactory(ScrollPaneListenerAdapter, [ScrollPane], 'getScrollPaneListeners'))
        registerFactory('separatorListener', new EventListenerFactory(SeparatorListenerAdapter, [Separator], 'getSeparatorListeners'))
        registerFactory('sheetStateListener', new EventListenerFactory(SheetStateListenerAdapter, [Sheet], 'getSheetStateListeners'))
        registerFactory('sliderListener', new EventListenerFactory(SliderListenerAdapter, [Slider], 'getSliderListeners'))
        registerFactory('sliderValueListener', new EventListenerFactory(SliderValueListenerAdapter, [Slider], 'getSliderValueListeners'))
        registerFactory('spinnerItemListener', new EventListenerFactory(SpinnerItemListenerAdapter, [Spinner], 'getSpinnerItemListeners'))
        registerFactory('spinnerListener', new EventListenerFactory(SpinnerListenerAdapter, [Spinner], 'getSpinnerListeners'))
        registerFactory('spinnerSelectionListener', new EventListenerFactory(SpinnerSelectionListenerAdapter, [Spinner], 'getSpinnerSelectionListeners'))
        registerFactory('splitPaneListener', new EventListenerFactory(SplitPaneListenerAdapter, [SplitPane], 'getSplitPaneListeners'))
        registerFactory('tabPaneAttributeListener', new EventListenerFactory(TabPaneAttributeListenerAdapter, [TabPane], 'getTabPaneAttributeListeners'))
        registerFactory('tabPaneListener', new EventListenerFactory(TabPaneListenerAdapter, [TabPane], 'getTabPaneListeners'))
        registerFactory('tabPaneSelectionListener', new EventListenerFactory(TabPaneSelectionListenerAdapter, [TabPane], 'getTabPaneSelectionListeners'))
        registerFactory('tablePaneAttributeListener', new EventListenerFactory(TablePaneAttributeListenerAdapter, [TablePane], 'getTablePaneAttributeListeners'))
        registerFactory('tablePaneListener', new EventListenerFactory(TablePaneListenerAdapter, [TablePane], 'getTablePaneListeners'))
        registerFactory('tableViewRowEditorListener', new EventListenerFactory(TableViewRowEditorListenerAdapter, [TableView.RowEditor], 'getRowEditorListeners'))
        registerFactory('tableViewColumnListener', new EventListenerFactory(TableViewColumnListenerAdapter, [TableView], 'getTableViewColumnListeners'))
        registerFactory('tableViewHeaderListener', new EventListenerFactory(TableViewHeaderListenerAdapter, [TableViewHeader], 'getTableViewHeaderListeners'))
        registerFactory('tableViewHeaderPressListener', new EventListenerFactory(TableViewHeaderPressListenerAdapter, [TableViewHeader], 'getTableViewHeaderPressListeners'))
        registerFactory('tableViewListener', new EventListenerFactory(TableViewListenerAdapter, [TableView], 'getTableViewListeners'))
        registerFactory('tableViewRowListener', new EventListenerFactory(TableViewRowListenerAdapter, [TableView], 'getTableViewRowListeners'))
        registerFactory('tableViewSelectionListener', new EventListenerFactory(TableViewSelectionListenerAdapter, [TableView], 'getTableViewSelectionListeners'))
        registerFactory('tableViewSortListener', new EventListenerFactory(TableViewSortListenerAdapter, [TableView], 'getTableViewSortListeners'))
        registerFactory('textAreaCharacterListener', new EventListenerFactory(TextAreaCharacterListenerAdapter, [TextArea], 'getTextAreaCharacterListeners'))
        registerFactory('textAreaListener', new EventListenerFactory(TextAreaListenerAdapter, [TextArea], 'getTextAreaListeners'))
        registerFactory('textAreaSelectionListener', new EventListenerFactory(TextAreaSelectionListenerAdapter, [TextArea], 'getTextAreaSelectionListeners'))
        registerFactory('textInputCharacterListener', new EventListenerFactory(TextInputCharacterListenerAdapter, [TextInput], 'getTextInputCharacterListeners'))
        registerFactory('textInputListener', new EventListenerFactory(TextInputListenerAdapter, [TextInput], 'getTextInputListeners'))
        registerFactory('textInputSelectionListener', new EventListenerFactory(TextInputSelectionListenerAdapter, [TextInput], 'getTextInputSelectionListeners'))
        registerFactory('textInputTextListener', new EventListenerFactory(TextInputTextListenerAdapter, [TextInput], 'getTextInputTextListeners'))
        registerFactory('tooltipListener', new EventListenerFactory(TooltipListenerAdapter, [Tooltip], 'getTooltipListeners'))
        registerFactory('treeViewNodeEditorListener', new EventListenerFactory(TreeViewNodeEditorListenerAdapter, [TreeView.NodeEditor], 'getNodeEditorListeners'))
        registerFactory('treeViewBranchListener', new EventListenerFactory(TreeViewBranchListenerAdapter, [TreeView], 'getTreeViewBranchListeners'))
        registerFactory('treeViewListener', new EventListenerFactory(TreeViewListenerAdapter, [TreeView], 'getTreeViewListeners'))
        registerFactory('treeViewNodeListener', new EventListenerFactory(TreeViewNodeListenerAdapter, [TreeView], 'getTreeViewNodeListeners'))
        registerFactory('treeViewNodeStateListener', new EventListenerFactory(TreeViewNodeStateListenerAdapter, [TreeView], 'getTreeViewNodeStateListeners'))
        registerFactory('treeViewSelectionListener', new EventListenerFactory(TreeViewSelectionListenerAdapter, [TreeView], 'getTreeViewSelectionListeners'))
        registerFactory('viewportListener', new EventListenerFactory(ViewportListenerAdapter, [Viewport], 'getViewportListeners'))
        registerFactory('windowActionMappingListener', new EventListenerFactory(WindowActionMappingListenerAdapter, [Window], 'getWindowActionMappingListeners'))
        registerFactory('windowClassListener', new EventListenerFactory(WindowClassListenerAdapter, [Window], 'getWindowClassListeners'))
        registerFactory('windowListener', new EventListenerFactory(WindowListenerAdapter, [Window], 'getWindowListeners'))
        registerFactory('windowStateListener', new EventListenerFactory(WindowStateListenerAdapter, [Window], 'getWindowStateListeners'))
    }

    private void registerBeanFactory(String name, Class pivotBeanClass, boolean leaf = true) {
        registerFactory(name, new BeanFactory(pivotBeanClass, leaf))
    }

    private void registerPivotBeanFactory(String name, Class pivotBeanClass, boolean leaf = true) {
        registerFactory(name, new PivotBeanFactory(pivotBeanClass, leaf))
    }

    private void registerPivotComponentFactory(String name, Class pivotBeanClass, boolean leaf = false) {
        registerFactory(name, new ComponentFactory(pivotBeanClass, leaf))
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
