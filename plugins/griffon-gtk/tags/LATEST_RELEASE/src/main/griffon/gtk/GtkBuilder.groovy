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

import griffon.gtk.factory.*
import griffon.gtk.impl.*
import griffon.gtk.adapters.*
import org.gnome.gdk.*
import org.gnome.gtk.*
import org.gnome.notify.*
import org.gnome.sourceview.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GtkBuilder extends FactoryBuilderSupport {
    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";
 
    public GtkBuilder(boolean init = true) {
        super( init )
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }
 
    void registerGtkSupportNodes() {
        registerFactory('glade', new GladeFactory())
        registerFactory('noparent', new JavaCollectionFactory())
        addAttributeDelegate(GtkBuilder.&objectIDAttributeDelegate)
        registerFactory('actionGroup', new ActionGroupFactory())
        registerFactory('action', new ActionFactory())
    }

    void registerGtkPassThruNodes() {
        registerFactory("widget", new WidgetFactory(Widget, false))
        registerFactory("container", new WidgetFactory(Container, false))
        registerFactory("bean", new WidgetFactory(java.lang.Object, true))
    }
 
    void registerGtkWidgets() {
        registerGtkComponentFactory('label', Label)
        registerGtkComponentFactory('button', Button)
    }

    void registerGtkContainers() {
        registerFactory('hbox', new BoxFactory(HBox))
        registerFactory('vbox', new BoxFactory(VBox))
    }

    void registerGtkWindows() {
        registerGtkContainerFactory('assistant', Assistant)
        registerGtkContainerFactory('aboutDialog', AboutDialog)
        registerGtkContainerFactory('colorSelectionDialog', ColorSelectionDialog)
        registerGtkContainerFactory('fileChooserDialog', FileChooserDialog)
        registerGtkContainerFactory('fontSelectionDialog', FontSelectionDialog)
        registerGtkContainerFactory('recentChooserDialog', RecentChooserDialog)
        registerFactory('messageDialog', new MessageDialogFactory())
        registerFactory('infoMessageDialog', new MessageDialogFactory(InfoMessageDialog))
        registerFactory('errorMessageDialog', new MessageDialogFactory(ErrorMessageDialog))
        registerFactory('questionMessageDialog', new MessageDialogFactory(QuestionMessageDialog))
        registerFactory('warningMessageDialog', new MessageDialogFactory(WarningMessageDialog))
        registerFactory('dialog', new DialogFactory())
        registerFactory('window', new WindowFactory())
    }

    void registerGtkMenus() {
        registerFactory('menu', new MenuFactory())
        registerFactory('menuBar', new MenuBarFactory())
        registerFactory('menuItem', new MenuItemFactory(MenuItem))
        registerFactory('checkMenuItem', new MenuItemFactory(CheckMenuItem))
        registerFactory('radioMenuItem', new MenuItemFactory(RadioMenuItem))
        registerFactory('imageMenuItem', new ImageMenuItemFactory())
        // registerGtkContainerFactory('tearoffMenuItem', TearoffMenuItem)
    }

    void registerGtk() {
        registerGtkComponentFactory('accelLabel', AccelLabel)
        registerGtkComponentFactory('arrow', Arrow)
        registerGtkComponentFactory('calendar', Calendar)
        registerGtkComponentFactory('cellView', CellView)
        registerGtkComponentFactory('drawingArea', DrawingArea)
        registerGtkComponentFactory('entry', Entry)
        registerGtkComponentFactory('hruler', HRuler)
        registerGtkComponentFactory('hscale', HScale)
        registerGtkComponentFactory('hscrollbar', HScrollbar)
        registerFactory('pixbuf', new PixbufFactory())
        registerFactory('image', new ImageFactory())
        registerGtkComponentFactory('progressBar', ProgressBar)
        registerGtkComponentFactory('ruler', Ruler)
        registerGtkComponentFactory('spinButton', SpinButton)
        registerGtkComponentFactory('vruler', VRuler)
        registerGtkComponentFactory('vscale', VScale)
        registerGtkComponentFactory('vscrollbar', VScrollbar)
        registerFactory('separator', new SeparatorFactory())

        registerGtkContainerFactory('alignment', Alignment)
        registerGtkContainerFactory('aspectFrame', AspectFrame)
        registerGtkContainerFactory('checkButton', CheckButton)
        registerGtkContainerFactory('colorButton', ColorButton)
        registerGtkContainerFactory('colorSelection', ColorSelection)
        registerGtkContainerFactory('comboBox', ComboBox)
        registerGtkContainerFactory('comboBoxEntry', ComboBoxEntry)
        registerGtkContainerFactory('eventBox', EventBox)
        registerGtkContainerFactory('expander', Expander)
        registerGtkContainerFactory('fileChooserButton', FileChooserButton)
        registerGtkContainerFactory('fileChooserWidget', FileChooserWidget)
        registerGtkContainerFactory('fixed', Fixed)
        registerGtkContainerFactory('fontButton', FontButton)
        registerGtkContainerFactory('fontSelection', FontSelection)
        registerGtkContainerFactory('frame', Frame)
        registerGtkContainerFactory('hbuttonBox', HButtonBox)
        registerGtkContainerFactory('hpaned', HPaned)
        registerGtkContainerFactory('handleBox', HandleBox)
        registerGtkContainerFactory('iconView', IconView)
        registerGtkContainerFactory('layout', Layout)
        registerGtkContainerFactory('linkButton', LinkButton)
        registerGtkContainerFactory('menuToolButton', MenuToolButton)
        registerGtkContainerFactory('notebook', Notebook)
        registerGtkContainerFactory('plug', Plug)
        registerGtkContainerFactory('radioButton', RadioButton)
        registerGtkContainerFactory('radioToolButton', RadioToolButton)
        registerGtkContainerFactory('recentChooserMenu', RecentChooserMenu)
        registerGtkContainerFactory('recentChooserWidget', RecentChooserWidget)
        registerGtkContainerFactory('scaleButton', ScaleButton)
        registerGtkContainerFactory('scrolledWindow', ScrolledWindow)
        registerGtkContainerFactory('socket', Socket)
        registerGtkContainerFactory('sourceView', SourceView)
        registerGtkContainerFactory('statusbar', Statusbar)
        registerGtkContainerFactory('table', Table)
        registerGtkContainerFactory('textComboBox', TextComboBox)
        registerGtkContainerFactory('textComboBoxEntry', TextComboBoxEntry)
        registerGtkContainerFactory('textView', TextView)
        registerGtkContainerFactory('toggleButton', ToggleButton)
        registerGtkContainerFactory('toggleToolButton', ToggleToolButton)
        registerGtkContainerFactory('toolButton', ToolButton)
        registerGtkContainerFactory('toolItem', ToolItem)
        registerGtkContainerFactory('toolbar', Toolbar)
        registerGtkContainerFactory('treeView', TreeView)
        registerGtkContainerFactory('vbuttonBox', VButtonBox)
        registerGtkContainerFactory('vpaned', VPaned)
        registerGtkContainerFactory('viewport', Viewport)
    }

    private void registerBeanFactory(String name, Class gtkBeanClass, boolean leaf = true) {
        registerFactory(name, new BeanFactory(gtkBeanClass, leaf))
    }

    private void registerGtkBeanFactory(String name, Class gtkBeanClass, boolean leaf = true) {
        registerFactory(name, new GtkBeanFactory(gtkBeanClass, leaf))
    }

    private void registerGtkComponentFactory(String name, Class gtkBeanClass, boolean leaf = false) {
        registerFactory(name, new ComponentFactory(gtkBeanClass, leaf))
    }
 
    private void registerGtkContainerFactory(String name, Class gtkBeanClass) {
        registerFactory(name, new ContainerFactory(gtkBeanClass))
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
