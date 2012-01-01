/*
 * Copyright 2010-2011 the original author or authors.
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

package griffon.charva

import griffon.charva.factory.*
import charva.awt.*
import charvax.swing.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CharvaBuilder extends FactoryBuilderSupport {
    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";
 
    public CharvaBuilder(boolean init = true) {
        super( init )
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }
 
    void registerCharvaSupportNodes() {
        registerFactory('noparent', new JavaCollectionFactory())
        addAttributeDelegate(CharvaBuilder.&objectIDAttributeDelegate)

        registerFactory("action", new ActionFactory())
        registerFactory("actions", new CollectionFactory())
        registerFactory("map", new MapFactory())
        registerFactory("buttonGroup", new ButtonGroupFactory())
        addAttributeDelegate(ButtonGroupFactory.&buttonGroupAttributeDelegate)

        //object id delegage, for propertyNotFound
        addAttributeDelegate(CharvaBuilder.&objectIDAttributeDelegate)
    }

    void registerCharvaPassThruNodes() {
        registerFactory("widget", new WidgetFactory(Component, false))
        registerFactory("container", new WidgetFactory(Container, false))
        registerFactory("bean", new WidgetFactory(Object, true))
    }

    void registerCharvaWindows() {
        registerFactory("dialog", new DialogFactory())
        registerCharvaComponentFactory("fileChooser", JFileChooser)
        registerFactory("frame", new FrameFactory())
        registerCharvaComponentFactory("optionPane", JOptionPane)
    }

    void registerActionButtonWidgets() {
        registerFactory("button", new RichActionWidgetFactory(JButton))
        registerFactory("checkBox", new RichActionWidgetFactory(JCheckBox))
        registerFactory("menuItem", new RichActionWidgetFactory(JMenuItem))
        registerFactory("radioButton", new RichActionWidgetFactory(JRadioButton))
    }

    void registerTextWidgets() {
        registerFactory("label", new TextArgWidgetFactory(JLabel))
        registerFactory("passwordField", new TextArgWidgetFactory(JPasswordField))
        registerFactory("textArea", new TextArgWidgetFactory(JTextArea))
        registerFactory("textField", new TextArgWidgetFactory(JTextField))
    }

    void registerBasicWidgets() {
        registerFactory("comboBox", new ComboBoxFactory())
        registerFactory("list", new ListFactory())
        registerCharvaComponentFactory("progressBar", JProgressBar)
        // registerFactory("separator", new SeparatorFactory())
        registerCharvaComponentFactory("scrollBar", JScrollBar)
        registerCharvaComponentFactory("tree", JTree)
    }

    void registerMenuWidgets() {
        registerCharvaComponentFactory("menu", JMenu)
        registerCharvaComponentFactory("menuBar", JMenuBar)
        registerCharvaComponentFactory("popupMenu", JPopupMenu)
    }

    void registerContainers() {
        registerCharvaContainerFactory("panel", JPanel)
        registerFactory("scrollPane", new ScrollPaneFactory())
        registerFactory("tabbedPane", new TabbedPaneFactory(JTabbedPane))
        registerCharvaContainerFactory("viewport", JViewport) // sub class?
    }

    void registerTableComponents() {
        registerFactory("table", new TableFactory())
        // registerBeanFactory2("tableColumn", TableColumn)
        registerFactory("tableModel", new TableModelFactory())
        registerFactory("propertyColumn", new PropertyColumnFactory())
        registerFactory("closureColumn", new ClosureColumnFactory())
    }

    void registerBasicLayouts() {
        registerFactory("borderLayout", new LayoutFactory(BorderLayout))
        registerFactory("flowLayout", new LayoutFactory(FlowLayout))

        registerFactory("gridBagLayout", new GridBagFactory())
        registerCharvaBeanFactory("gridBagConstraints", GridBagConstraints)
        registerCharvaBeanFactory("gbc", GridBagConstraints) // shortcut name
        // constraints delegate
        addAttributeDelegate(GridBagFactory.&processGridBagConstraintsAttributes)

        addAttributeDelegate(LayoutFactory.&constraintsAttributeDelegate)
    }

    void registerBoxLayout() {
        registerFactory("boxLayout", new BoxLayoutFactory())
        registerFactory("box", new BoxFactory())
        registerFactory("hbox", new HBoxFactory())
        registerFactory("hglue", new HGlueFactory())
        registerFactory("hstrut", new HStrutFactory())
        registerFactory("vbox", new VBoxFactory())
        registerFactory("vglue", new VGlueFactory())
        registerFactory("vstrut", new VStrutFactory())
        registerFactory("glue", new GlueFactory())
        registerFactory("rigidArea", new RigidAreaFactory())
    }

    void registerBorders() {
        registerFactory("lineBorder", new LineBorderFactory())
        // registerFactory("titledBorder", new TitledBorderFactory())
        registerFactory("emptyBorder", new EmptyBorderFactory())
        registerFactory("compoundBorder", new CompoundBorderFactory())
    }

    private void registerBeanFactory2(String name, Class charvaBeanClass, boolean leaf = true) {
        registerFactory(name, new BeanFactory(charvaBeanClass, leaf))
    }

    private void registerCharvaBeanFactory(String name, Class charvaBeanClass, boolean leaf = true) {
        registerFactory(name, new CharvaBeanFactory(charvaBeanClass, leaf))
    }

    private void registerCharvaComponentFactory(String name, Class charvaBeanClass, boolean leaf = false) {
        registerFactory(name, new ComponentFactory(charvaBeanClass, leaf))
    }
 
    private void registerCharvaContainerFactory(String name, Class charvaBeanClass) {
        registerFactory(name, new ContainerFactory(charvaBeanClass))
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
