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

import org.apache.pivot.wtk.*
import org.apache.pivot.collections.List as PivotList
import org.apache.pivot.collections.Set as PivotSet
import org.apache.pivot.collections.Map as PivotMap
import org.apache.pivot.collections.Dictionary 
import org.apache.pivot.collections.Sequence
import org.apache.pivot.util.ListenerList
import griffon.app.AbstractSyntheticMetaMethods
import griffon.pivot.impl.FixedIterator

/**
 * @author Andres Almiray
 */
final class PivotUtils {
    private PivotUtils() {}

    static enhanceClasses() {
        AbstractSyntheticMetaMethods.enhance(Sequence, [
            iterator: { -> new FixedIterator(delegate, true) },
            size: { -> delegate.getLength() },
            getAt: { i -> delegate.get(i) },
            putAt: { i, e -> delegate.update(i,e) }
        ])
        AbstractSyntheticMetaMethods.enhance(ListenerList, [
            leftShift: { e -> delegate.add(e) }
        ])
        AbstractSyntheticMetaMethods.enhance(PivotList, [
            leftShift: { e -> delegate.add(e) }
        ])
        AbstractSyntheticMetaMethods.enhance(PivotSet, [
            leftShift: { e -> delegate.add(e) },
            size: { -> delegate.getCount() }
        ])
        AbstractSyntheticMetaMethods.enhance(Dictionary, [
            putAt: { k, v -> delegate.put(k, v) },
            getAt: { k -> delegate.get(k) }
        ])
        AbstractSyntheticMetaMethods.enhance(PivotMap, [
            size: { -> delegate.getCount() }
        ])
        AbstractSyntheticMetaMethods.enhance(Span, [
            asRange: { -> new IntRange(delegate.start, delegate.end) },
            size: { -> delegate.getLength() }
        ])
    }

    static void setBeanProperty(String propertyName, value, bean) {
        try {
            // use setter method instead of property access
            // workaround to multiple property setters & single property getter
            String setter = 'set' + propertyName[0].toUpperCase() + propertyName[1..-1]
            bean."$setter"(value)
        } catch(MissingPropertyException mpe) {
            bean.styles.put(propertyName, value)
        }
    }

    static boolean applyAsEventListener(String propertyName, value, bean) {
        List<Class> listenerTypes = EVENT_TO_LISTENER_MAP[propertyName]
        if(!listenerTypes || !(value instanceof Closure)) return false
        for(type in listenerTypes) {
            String getListenersMethod = "get" + type.simpleName + "s"
            if(!bean.metaClass.respondsTo(bean, getListenersMethod)) continue
            bean."$getListenersMethod"().add([
               (propertyName): value
            ].asType(type))
            return true
        }
        return false
    }

    private static final Map EVENT_TO_LISTENER_MAP = [
        actionAdded: [ActionClassListener],
        actionChanged: [ButtonListener, WindowActionMappingListener],
        actionMappingAdded: [WindowActionMappingListener],
        actionMappingsRemoved: [WindowActionMappingListener],
        actionRemoved: [ActionClassListener],
        actionUpdated: [ActionClassListener],
        activeChanged: [WindowListener, ActivityIndicatorListener], // Menu$ItemListener MenuBar$ItemListener
        activeItemChanged: [MenuListener, MenuBarListener],
        activeWindowChanged: [WindowClassListener],
        asynchronousChanged: [ImageViewListener],
        blockIncrementChanged: [ScrollBarListener],
        bottomRightChanged: [SplitPaneListener],
        branchCollapsed: [TreeViewBranchListener],
        branchExpanded: [TreeViewBranchListener],
        buttonAdded: [ButtonGroupListener],
        buttonDataChanged: [ButtonListener],
        buttonGroupChanged: [ButtonListener],
        buttonPressed: [ButtonPressListener],
        buttonRemoved: [ButtonGroupListener],
        cellInserted: [TablePaneListener, GridPaneListener],
        cellUpdated: [TablePaneListener, GridPaneListener],
        cellsRemoved: [TablePaneListener, GridPaneListener],
        // changesSaved: [TreeView$NodeEditorListener, TableView$RowEditorListener, ListView$ItemEditorListener],
        charactersInserted: [TextAreaCharacterListener, TextInputCharacterListener],
        charactersRemoved: [TextAreaCharacterListener, TextInputCharacterListener],
        checkmarksEnabledChanged: [TreeViewListener, ListViewListener],
        circularChanged: [SpinnerListener],
        closeableChanged: [TabPaneAttributeListener],
        collapsibleChanged: [RollupListener, ExpanderListener],
        columnCellRendererChanged: [TableViewColumnListener],
        columnCountChanged: [GridPaneListener],
        columnFilterChanged: [TableViewColumnListener],
        columnHeaderChanged: [ScrollPaneListener],
        columnHeaderDataChanged: [TableViewColumnListener],
        columnHighlightedChanged: [TablePaneListener],
        columnInserted: [TablePaneListener, TableViewColumnListener],
        columnNameChanged: [TableViewColumnListener],
        columnSourceChanged: [TableViewListener],
        columnSpanChanged: [TablePaneAttributeListener],
        columnWidthChanged: [TablePaneListener, TableViewColumnListener],
        columnWidthLimitsChanged: [TableViewColumnListener],
        columnsRemoved: [TablePaneListener, TableViewColumnListener],
        componentInserted: [ContainerListener],
        componentMoved: [ContainerListener],
        componentsRemoved: [ContainerListener],
        contentChanged: [RollupListener, WindowListener, ClipboardContentListener, BorderListener, ExpanderListener],
        contextKeyChanged: [ContainerListener],
        cornerChanged: [ScrollPaneListener, TabPaneListener],
        cursorChanged: [ComponentListener],
        dataRendererChanged: [TableViewHeaderListener, ButtonListener],
        decoratorInserted: [ComponentDecoratorListener],
        decoratorUpdated: [ComponentDecoratorListener],
        decoratorsRemoved: [ComponentDecoratorListener],
        dialogCloseVetoed: [DialogStateListener],
        dialogClosed: [DialogCloseListener],
        disabledCheckmarkFilterChanged: [TreeViewListener, ListViewListener],
        disabledDateFilterChanged: [CalendarButtonListener, CalendarListener],
        disabledFileFilterChanged: [FileBrowserSheetListener, FileBrowserListener],
        disabledItemFilterChanged: [ListButtonListener, ListViewListener],
        disabledNodeFilterChanged: [TreeViewListener],
        disabledRowFilterChanged: [TableViewListener],
        documentChanged: [TextAreaListener],
        dragSourceChanged: [ComponentListener],
        dropTargetChanged: [ComponentListener],
        // editCancelled: [TreeView$NodeEditorListener, TableView$RowEditorListener, ListView$ItemEditorListener],
        // editItemVetoed: [ListView$ItemEditorListener],
        // editNodeVetoed: [TreeView$NodeEditorListener],
        // editRowVetoed: [TableView$RowEditorListener],
        editableChanged: [TextAreaListener],
        enabledChanged: [ComponentStateListener, ActionListener],
        expandedChangeVetoed: [RollupStateListener, ExpanderListener],
        expandedChanged: [RollupStateListener, ExpanderListener],
        fieldInserted: [FormListener],
        fieldsRemoved: [FormListener],
        flagChanged: [FormAttributeListener],
        focusTraversalPolicyChanged: [ContainerListener],
        focusedChanged: [ComponentStateListener],
        focusedComponentChanged: [ComponentClassListener],
        headerPressed: [TableViewHeaderPressListener],
        headingChanged: [RollupListener, SeparatorListener],
        horizontalScrollBarPolicyChanged: [ScrollPaneListener],
        iconChanged: [WindowListener, TabPaneAttributeListener, AccordionAttributeListener],
        imageChanged: [ImageViewListener],
        imageKeyChanged: [ImageViewListener],
        itemCheckedChanged: [ListViewItemStateListener],
        // itemEditing: [ListView$ItemEditorListener],
        itemEditorChanged: [ListViewListener],
        itemInserted: [ListViewItemListener, SpinnerItemListener, MenuBarListener], // Menu$SectionListener
        itemRendererChanged: [ListButtonListener, SpinnerListener, ListViewListener],
        itemSelected: [MenuItemSelectionListener],
        itemUpdated: [ListViewItemListener, SpinnerItemListener],
        itemsCleared: [ListViewItemListener, SpinnerItemListener],
        itemsRemoved: [ListViewItemListener, SpinnerItemListener, MenuBarListener], // Menu$SectionListener
        itemsSorted: [ListViewItemListener, SpinnerItemListener],
        keyPressed: [ComponentKeyListener],
        keyReleased: [ComponentKeyListener],
        keyStrokeChanged: [WindowActionMappingListener],
        keyTyped: [ComponentKeyListener],
        labelChanged: [TabPaneAttributeListener, AccordionAttributeListener, FormAttributeListener],
        listDataChanged: [ListButtonListener, ListViewListener],
        localeChanged: [CalendarButtonListener, CalendarListener],
        locationChanged: [ComponentListener],
        lockedChanged: [SplitPaneListener],
        maximizedChanged: [WindowListener],
        maximumLengthChanged: [TextInputListener],
        menuBarChanged: [FrameListener],
        menuChanged: [MenuButtonListener, /*MenuBar$ItemListener, Menu$ItemListener,*/ MenuPopupListener],
        menuHandlerChanged: [ComponentListener],
        menuPopupCloseVetoed: [MenuPopupStateListener],
        menuPopupClosed: [MenuPopupStateListener],
        monthChanged: [CalendarListener],
        mouseClick: [ComponentMouseButtonListener],
        mouseDown: [ContainerMouseListener, ComponentMouseButtonListener],
        mouseMove: [ContainerMouseListener, ComponentMouseListener],
        mouseOut: [ComponentMouseListener],
        mouseOver: [ComponentMouseListener],
        mouseUp: [ContainerMouseListener, ComponentMouseButtonListener],
        mouseWheel: [ContainerMouseListener, ComponentMouseWheelListener],
        movieChanged: [MovieViewListener],
        multiSelectChanged: [FileBrowserListener],
        // nameChanged: [Menu$ItemListener, Menu$SectionListener],
        nodeCheckStateChanged: [TreeViewNodeStateListener],
        // nodeEditing: [TreeView$NodeEditorListener],
        nodeEditorChanged: [TreeViewListener],
        nodeInserted: [TreeViewNodeListener],
        nodeRendererChanged: [TreeViewListener],
        nodeUpdated: [TreeViewNodeListener],
        nodesCleared: [TreeViewNodeListener],
        nodesRemoved: [TreeViewNodeListener],
        nodesSorted: [TreeViewNodeListener],
        orientationChanged: [ScrollBarListener, MeterListener, SplitPaneListener, BoxPaneListener, SliderListener],
        panelInserted: [AccordionListener],
        panelsRemoved: [AccordionListener],
        parentChanged: [ComponentListener],
        passwordChanged: [TextInputListener],
        percentageChanged: [MeterListener],
        preferredHeightLimitsChanged: [ComponentListener],
        preferredSizeChanged: [ComponentListener],
        preferredWidthLimitsChanged: [ComponentListener],
        previewDialogClose: [DialogStateListener],
        // previewEditItem: [ListView$ItemEditorListener],
        // previewEditNode: [TreeView$NodeEditorListener],
        // previewEditRow: [TableView$RowEditorListener],
        previewExpandedChange: [RollupStateListener, ExpanderListener],
        previewMenuPopupClose: [MenuPopupStateListener],
        // previewSaveChanges: [TreeView$NodeEditorListener, TableView$RowEditorListener, ListView$ItemEditorListener],
        previewSelectedIndexChange: [CardPaneListener, AccordionSelectionListener, TabPaneSelectionListener],
        previewSheetClose: [SheetStateListener],
        previewWindowClose: [WindowStateListener],
        previewWindowOpen: [WindowStateListener],
        primaryRegionChanged: [SplitPaneListener],
        promptChanged: [TextInputListener],
        rangeChanged: [SliderListener],
        repeatableChanged: [MenuButtonListener],
        resizeModeChanged: [SplitPaneListener],
        rootDirectoryChanged: [FileBrowserSheetListener, FileBrowserListener],
        // rowEditing: [TableView$RowEditorListener],
        rowEditorChanged: [TableViewListener],
        rowHeaderChanged: [ScrollPaneListener],
        rowHeightChanged: [TablePaneListener],
        rowHighlightedChanged: [TablePaneListener],
        rowInserted: [TablePaneListener, TableViewRowListener, GridPaneListener],
        rowSpanChanged: [TablePaneAttributeListener],
        rowUpdated: [TableViewRowListener],
        rowsCleared: [TableViewRowListener],
        rowsRemoved: [TablePaneListener, TableViewRowListener, GridPaneListener],
        rowsSorted: [TableViewRowListener],
        // saveChangesVetoed: [TreeView$NodeEditorListener, TableView$RowEditorListener, ListView$ItemEditorListener],
        scopeChanged: [ScrollBarListener],
        scrollLeftChanged: [ViewportListener],
        scrollTopChanged: [ViewportListener],
        sectionHeadingChanged: [FormListener],
        sectionInserted: [MenuListener, FormListener],
        sectionsRemoved: [MenuListener, FormListener],
        selectModeChanged: [TreeViewListener, ListViewListener, TableViewListener],
        selectedColorChanged: [ColorChooserButtonSelectionListener, ColorChooserSelectionListener],
        selectedColorKeyChanged: [ColorChooserButtonListener, ColorChooserListener],
        selectedDateChanged: [CalendarSelectionListener, CalendarButtonSelectionListener],
        selectedDateKeyChanged: [CalendarButtonListener, CalendarListener],
        selectedFileAdded: [FileBrowserListener],
        selectedFileRemoved: [FileBrowserListener],
        selectedFilesChanged: [FileBrowserSheetListener, FileBrowserListener],
        selectedIndexChangeVetoed: [CardPaneListener, AccordionSelectionListener, TabPaneSelectionListener],
        selectedIndexChanged: [CardPaneListener, AccordionSelectionListener, ListButtonSelectionListener, TabPaneSelectionListener, SpinnerSelectionListener],
        selectedItemKeyChanged: [ListButtonListener, SpinnerListener, ListViewListener],
        selectedItemsKeyChanged: [ListViewListener],
        selectedKeyChanged: [ButtonListener],
        selectedOptionChanged: [AlertListener, PromptListener],
        selectedPathAdded: [TreeViewSelectionListener],
        selectedPathRemoved: [TreeViewSelectionListener],
        selectedPathsChanged: [TreeViewSelectionListener],
        selectedRangeAdded: [TableViewSelectionListener, ListViewSelectionListener],
        selectedRangeRemoved: [TableViewSelectionListener, ListViewSelectionListener],
        selectedRangesChanged: [TableViewSelectionListener, ListViewSelectionListener],
        selectionChanged: [TextInputSelectionListener, TextAreaSelectionListener, ButtonGroupListener],
        sheetCloseVetoed: [SheetStateListener],
        sheetClosed: [SheetCloseListener],
        showMixedCheckmarkStateChanged: [TreeViewListener],
        sizeChanged: [ComponentListener],
        sortAdded: [TableViewSortListener],
        sortChanged: [TableViewSortListener],
        sortModeChanged: [TableViewHeaderListener],
        sortRemoved: [TableViewSortListener],
        sortUpdated: [TableViewSortListener],
        spinnerDataChanged: [SpinnerListener],
        splitRatioChanged: [SplitPaneListener],
        stateChanged: [ButtonStateListener],
        stateKeyChanged: [ButtonListener],
        styleUpdated: [ComponentListener],
        tabInserted: [TabPaneListener],
        tableDataChanged: [TableViewListener],
        tableViewChanged: [TableViewHeaderListener],
        tabsRemoved: [TabPaneListener],
        textChanged: [LabelListener, TooltipListener, MeterListener, TextInputTextListener],
        textKeyChanged: [LabelListener, TextInputListener, TextAreaListener],
        textNodeChanged: [TextInputListener],
        textSizeChanged: [TextInputListener],
        textValidChanged: [TextInputListener],
        textValidatorChanged: [TextInputListener],
        titleChanged: [WindowListener, BorderListener, ExpanderListener],
        toggleButtonChanged: [ButtonListener],
        tooltipTextChanged: [ComponentListener],
        topLeftChanged: [SplitPaneListener],
        treeDataChanged: [TreeViewListener],
        triStateChanged: [ButtonListener],
        unitIncrementChanged: [ScrollBarListener],
        valueAdded: [ComponentDataListener],
        valueChanged: [ScrollBarValueListener, SliderValueListener],
        valueRemoved: [ComponentDataListener],
        valueUpdated: [ComponentDataListener],
        verticalScrollBarPolicyChanged: [ScrollPaneListener],
        viewChanged: [ViewportListener],
        visibleChanged: [ComponentListener],
        windowCloseVetoed: [WindowStateListener],
        windowClosed: [WindowStateListener],
        windowOpenVetoed: [WindowStateListener],
        windowOpened: [WindowStateListener],
        yearChanged: [CalendarListener]
    ]
}
