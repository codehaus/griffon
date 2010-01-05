/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.gtk

import org.gnome.gtk.*
import org.gnome.notify.*
import org.gnome.sourceview.*

// import griffon.app.AbstractSyntheticMetaMethods
import griffon.gtk.adapters.*

/**
 * @author Andres Almiray
 */
final class GtkUtils {
    private GtkUtils() {}

    static void setBeanProperty(String propertyName, value, bean) {
        bean[propertyName] = value
    }

    static File createTempResource(String classpathResource, String suffix = '.gtk', String prefix = 'griffon') {
        return createTempResource(GtkUtils.class.classLoader.getResource(classpathResource), suffix, prefix)
    }

    static File createTempResource(URL resource, String suffix = '.gtk', String prefix = 'grffon') {
        File tmpFile = File.createTempFile(prefix, suffix)
        tmpFile.deleteOnExit()
        resource.withInputStream { is -> new FileOutputStream(tmpFile) << is }
        return tmpFile
    }

    static Stock parseStock(value, defaultValue = null ) {
        def fieldName = value.toUpperCase().replace(' ','_')
        try {
            return Stock."$fieldName"
        } catch(Exception e) {
            return defaultValue
        }
    }

    static boolean applyAsSignalHandler(FactoryBuilderSupport builder, String propertyName, value, bean) {
        Map signalInfo = SIGNAL_HANDLERS[bean.class.simpleName]
        if(!signalInfo || !signalInfo[propertyName]) return
        String connect = signalInfo[propertyName].connect ?: 'connect'
        bean."$connect"(makeAdapter(builder, signalInfo[propertyName].signal, propertyName, value))
        return true
    }

    private static final Class[] PARAMS = [FactoryBuilderSupport] as Class[]

    private static makeAdapter(FactoryBuilderSupport builder, Class type, String propertyName, Closure callback) {
        String adapterClassName = 'griffon.gtk.adapters.' + (type.memberClass? type.enclosingClass.simpleName + type.simpleName : type.simpleName) + 'Adapter'
        Class adapterClass = GtkUtils.classLoader.loadClass(adapterClassName)
        def adapter = adapterClass.getDeclaredConstructor(PARAMS).newInstance([builder] as java.lang.Object[])
        adapter."$propertyName"(callback)
        return adapter
    }
    private static final Map SIGNAL_HANDLERS = [
        'AboutDialog': [
            onEmailClicked: [signal: AboutDialog.EmailHook, connect: 'setEmailCallback'],
            onUrlClicked: [signal: AboutDialog.UrlHook, connect: 'setUrlCallback'],
        ],
        'Action': [
            onActivate: [signal: Action.Activate],
        ],
        'Adjustment': [
            onChanged: [signal: Adjustment.Changed],
            onValueChanged: [signal: Adjustment.ValueChanged],
        ],
        'Assistant': [
            onApply: [signal: Assistant.Apply],
            onCancel: [signal: Assistant.Cancel],
            onClose: [signal: Assistant.Close],
            onPrepare: [signal: Assistant.Prepare],
        ],
        'Button': [
            onClicked: [signal: Button.Clicked],
        ],
        'Calendar': [
            onDaySelected: [signal: Calendar.DaySelected],
            onDaySelectedDoubleClick: [signal: Calendar.DaySelectedDoubleClick],
        ],
        'CellRendererText': [
            onEdited: [signal: CellRendererText.Edited],
        ],
        'CellRendererToggle': [
            onToggled: [signal: CellRendererToggle.Toggled],
        ],
        'CheckMenuItem': [
            onToggled: [signal: CheckMenuItem.Toggled],
        ],
        'Clipboard': [
            onOwnerChange: [signal: Clipboard.OwnerChange],
        ],
        'ColorButton': [
            onColorSet: [signal: ColorButton.ColorSet],
        ],
        'ComboBox': [
            onChanged: [signal: ComboBox.Changed],
        ],
        'Dialog': [
            onResponse: [signal: Dialog.Response],
        ],
        'Editable': [
            onChanged: [signal: Editable.Changed],
        ],
        'Entry': [
            onActivate: [signal: Entry.Activate],
            onChanged: [signal: Editable.Changed],
            onIconPress: [signal: Entry.IconPress],
            onIconRelease: [signal: Entry.IconRelease],
        ],
        'EntryCompletion': [
            onActionActivated: [signal: EntryCompletion.ActionActivated],
            onCursorOnMatch: [signal: EntryCompletion.CursorOnMatch],
            onInsertPrefix: [signal: EntryCompletion.InsertPrefix],
            onMatch: [signal: EntryCompletion.Match, connect: 'setMatchCallback'],
            onMatchSelected: [signal: EntryCompletion.MatchSelected],
        ],
        'FileChooserButton': [
            onFileSet: [signal: FileChooserButton.FileSet],
        ],
        'FontButton': [
            onFontSet: [signal: FontButton.FontSet],
        ],
        'IconView': [
            onItemActivated: [signal: IconView.ItemActivated],
            onSelectionChanged: [signal: IconView.SelectionChanged],
        ],
        'InputMethod': [
            onCommit: [signal: InputMethod.Commit],
        ],
        'MenuItem': [
            onActivate: [signal: MenuItem.Activate],
        ],
        'Notebook': [
            onChangeCurrentPage: [signal: Notebook.ChangeCurrentPage],
            onSwitchPage: [signal: Notebook.SwitchPage],
        ],
        'Notification': [
            onClosed: [signal: Notification.Closed],
        ],
        'RadioButtonGroup': [
            onGroupToggled: [signal: RadioButtonGroup.GroupToggled],
        ],
        'Range': [
            onValueChanged: [signal: Range.ValueChanged],
        ],
        'SourceView': [
            onRedo: [signal: SourceView.Redo],
            onUndo: [signal: SourceView.Undo],
        ],
        'SpinButton': [
            onValueChanged: [signal: SpinButton.ValueChanged],
        ],
        'StatusIcon': [
            onActivate: [signal: StatusIcon.Activate],
            onPopupMenu: [signal: StatusIcon.PopupMenu],
            onSizeChanged: [signal: StatusIcon.SizeChanged],
        ],
        'TextBuffer': [
            onApplyTag: [signal: TextBuffer.ApplyTag],
            onBeginUserAction: [signal: TextBuffer.BeginUserAction],
            onChanged: [signal: TextBuffer.Changed],
            onDeleteRange: [signal: TextBuffer.DeleteRange],
            onEndUserAction: [signal: TextBuffer.EndUserAction],
            onInsertText: [signal: TextBuffer.InsertText, connect: 'connectAfter'],
            onMarkSet: [signal: TextBuffer.MarkSet],
            onNotifyCursorPosition: [signal: TextBuffer.NotifyCursorPosition],
            onRemoveTag: [signal: TextBuffer.RemoveTag],
        ],
        'TextView': [
            onPopulatePopup: [signal: TextView.PopulatePopup],
        ],
        'ToggleButton': [
            onToggled: [signal: ToggleButton.Toggled],
        ],
        'ToggleToolButton': [
            onToggled: [signal: ToggleToolButton.Toggled],
        ],
        'ToolButton': [
            onClicked: [signal: ToolButton.Clicked],
        ],
        'TreeModel': [
            onRowChanged: [signal: TreeModel.RowChanged],
        ],
        'TreeModelFilter': [
            onVisible: [signal: TreeModelFilter.Visible, connect: 'setVisibleCallback'],
        ],
        'TreeSelection': [
            onChanged: [signal: TreeSelection.Changed],
        ],
        'TreeView': [
            onRowActivated: [signal: TreeView.RowActivated],
            onRowExpanded: [signal: TreeView.RowExpanded],
            onSelectAll: [signal: TreeView.SelectAll],
        ],
        'Widget': [
            onButtonPressEvent: [signal: Widget.ButtonPressEvent],
            onButtonReleaseEvent: [signal: Widget.ButtonReleaseEvent],
            onEnterNotifyEvent: [signal: Widget.EnterNotifyEvent],
            onExposeEvent: [signal: Widget.ExposeEvent],
            onFocusInEvent: [signal: Widget.FocusInEvent],
            onFocusOutEvent: [signal: Widget.FocusOutEvent],
            onHide: [signal: Widget.Hide],
            onKeyPressEvent: [signal: Widget.KeyPressEvent],
            onKeyReleaseEvent: [signal: Widget.KeyReleaseEvent],
            onLeaveNotifyEvent: [signal: Widget.LeaveNotifyEvent],
            onMapEvent: [signal: Widget.MapEvent],
            onPopupMenu: [signal: Widget.PopupMenu],
            onScrollEvent: [signal: Widget.ScrollEvent],
            onUnmapEvent: [signal: Widget.UnmapEvent],
            onVisibilityNotifyEvent: [signal: Widget.VisibilityNotifyEvent],
        ],
        'Window': [
            onConfigureEvent: [signal: Window.ConfigureEvent],
            onDeleteEvent: [signal: Window.DeleteEvent],
        ],
    ]
}
