package dockingframe

import bibliothek.gui.dock.util.NullWindowProvider
import griffon.test.GriffonUnitTestCase
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import bibliothek.gui.dock.common.*
import bibliothek.gui.dock.util.DirectWindowProvider
import bibliothek.util.xml.XElement
import javax.swing.JTextArea
import javax.swing.JScrollPane
import bibliothek.gui.dock.common.action.CAction
import java.awt.Color

class BuilderTests extends GriffonUnitTestCase {
    private def createBuilder() {
        def builder = new SwingBuilder(true)
        builder.frame(id: 'frame', title: 'TestFrame') {
            borderLayout()
        }
        def addon = new GroovyClassLoader().parseClass(new File("DockingFrameGriffonAddon.groovy")).newInstance()
        addon.provider = new DirectWindowProvider(builder.frame)
        addon.factories.each { name, factory ->
            builder.registerFactory(name, factory)
        }
        for (def delegate: addon.attributeDelegates) {
            builder.addAttributeDelegate(delegate)
        }
        builder
    }

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreation() {
        def builder = createBuilder()
        builder.with {
            dockingControl() {
                def d = dockingArea()
                assert d instanceof CContentArea
                assert d.uniqueId == 'ccontrol'

                def c = contentArea()
                assert c instanceof CContentArea
                assert c.uniqueId != 'ccontrol'

                def x = contentArea('mainContent1')
                assert x instanceof CContentArea
                assert x.uniqueId == 'mainContent1'

                contentArea(id: 'area', 'mainContent2')
                assert area instanceof CContentArea
                assert area.uniqueId == 'mainContent2'

                contentArea(id: 'mainContent3')
                assert mainContent3 instanceof CContentArea
                assert mainContent3.uniqueId == 'mainContent3'

                dockable(id: 'dockable')
                assert dockable instanceof SingleCDockable
                assert dockable.uniqueId == 'dockable'

                assert minimizeArea('minimizeArea') instanceof CMinimizeArea
                assert gridArea('gridArea') instanceof CGridArea
                assert workingArea('workingArea') instanceof CWorkingArea
            }
        }
    }

    void testNesting() {
        def builder = createBuilder()
        builder.with {
            dockingControl(id: 'ctrl') {
                panel(id: 'root') {
                    dockingArea() {
                        panel(id: 'dockable')
                        panel(id: 'dockable2', name: 'dockable2')
                        panel(id: 'dockable3', constraints: 'dockable3')
                        dockable(id: 'singleDockable') {
                            borderLayout()
                            panel(id: 'singleInner')
                        }
                    }
                }
            }
            panel(id: 'side') {
                contentArea(id: 'area2', control: ctrl)
                gridArea(id: 'gridArea', control: ctrl)
                minimizeArea(id: 'minimizeArea', control: ctrl) {
                    panel(id: 'minimizePanel')
                    panel(id: 'minimizePanel2', constraints: 'North')
                }
                workingArea(id: 'workingArea', control: ctrl)
            }

            def area = root.components[0]
            assert area instanceof CContentArea
            assert area.control.register.singleDockables[0].contentPane.components[0] == dockable
            assert area.control.getSingleDockable('dockable2').contentPane.components[0] == dockable2
            assert area.control.getSingleDockable('dockable3').contentPane.components[0] == dockable3
            assert area.control.contentAreas.contains(area2)
            assert area.control.stations.contains(gridArea)
            assert area.control.stations.contains(minimizeArea)
            assert area.control.stations.find {it.uniqueId == 'minimizeArea'}.layout.south == minimizePanel
            assert area.control.stations.find {it.uniqueId == 'minimizeArea'}.layout.north == minimizePanel2
            assert area.control.stations.contains(workingArea)
            assert area.control.getSingleDockable('singleDockable') == singleDockable
            assert area.control.getSingleDockable('singleDockable').contentPane.layout instanceof BorderLayout
            assert area.control.getSingleDockable('singleDockable').contentPane.components[0] == singleInner

            dockingControl() {
                panel(id: 'next') {
                    gridArea(id: 'nextGrid')
                    workingArea(id: 'nextWorking')
                }
            }

            assert next.components[0] == nextGrid.station
            assert next.components[1] == nextWorking.station
        }
    }

    void testLayout() {
        def builder = createBuilder()
        builder.with {
            dockingControl(theme: 'bubble') {
                frame(frame) {
                    dockingArea(id: 'area') {
                        dockable(id: 'dock1', titleText: 'Dock 1', dock: [0, 0, 20, 50], dockExternalized: [100, 100, 300, 300]) { label('dock1') }
                        dockable(id: 'dock2', titleText: 'Dock 2', dock: [0, 50, 20, 50]) { label('dock2') }
                        dockable(id: 'dock3', titleText: 'Dock 3', dock: [20, 0, 80, 20]) { label('dock3') }
                        dockable(id: 'dock4', titleText: 'Dock 4', dock: [80, 20, 20, 60]) { label('dock4') }
                        dockable(id: 'dock5', titleText: 'Dock 5', dock: [20, 80, 80, 20], dockMinimized: 'south', dockState: 'normalized') { label('dock5') }
                        dockable(id: 'dock6', titleText: 'Dock 6', dock: [20, 20, 60, 60]) { label('dock6') }
                        dockable(id: 'dock7', titleText: 'Dock 7', dock: [20, 20, 60, 60]) { label('dock7') }
                        dockable(id: 'dock8', titleText: 'Dock 8', dock: [0, 0, 20, 50], dockMinimized: 'west', dockState: 'minimized') { label('dock8') }
                    }
                }
                frame.setBounds(0, 0, 10000, 10000)
                frame.visible = true
                def d1 = dock1.intern().component.bounds
                def d2 = dock2.intern().component.bounds
                def d3 = dock3.intern().component.bounds
                def d4 = dock4.intern().component.bounds
                def d5 = dock5.intern().component.bounds
                def d6 = dock6.intern().component.bounds
                def probe = [
                        d1.width / 20, d1.height / 50,
                        d2.width / 20, d2.height / 50,
                        d3.width / 80, d3.height / 20,
                        d4.width / 20, d4.height / 60,
                        d5.width / 80, d5.height / 20,
                        d6.width / 60, d6.height / 60
                ]
                //Thread.sleep 10000
                Thread.sleep(500)
                for (def p: probe) {
                    assert p > 96 && p < 102
                }
                frame?.dispose()
            }
        }
    }

    void testMenus() {
        def builder = createBuilder()
        builder.with {
            dockingControl(theme: 'bubble') {
                frame(frame) {
                    menuBar {
                        rootMenuPiece(text: 'Main') {
                            subMenuPiece(text: 'Sub-Menu', disableWhenEmpty: true) {
                                separatingMenuPiece(topSeparator: true) {
                                    dockableListMenuPiece()
                                }
                                separatingMenuPiece(topSeparator: true, bottomSeparator: true) {
                                    themeMenuPiece()
                                }
                                separatingMenuPiece(bottomSeparator: true) {
                                    preferenceMenuPiece()
                                }
                                separatingMenuPiece(topSeparator: true, bottomSeparator: true) {
                                    lookAndFeelMenuPiece()
                                }
                            }
                        }
                        rootMenuPiece(text: 'Layout') {
                            layoutMenuPiece()
                        }
                    }
                    dockingArea(id: 'area') {
                        dockable(id: 'dock1', titleText: 'Dock 1', dock: [0, 0, 1, 1]) { label('dock1') }
                        dockable(id: 'dock2', titleText: 'Dock 2', closeable: true, dock: [0, 1, 1, 1]) { label('dock2') }
                        dockable(id: 'dock3', titleText: 'Dock 3', dock: [1, 0, 1, 1]) { label('dock3') }
                        dockable(id: 'dock4', titleText: 'Dock 4', closeable: true, dock: [1, 1, 1, 1]) { label('dock4') }
                    }
                }
                frame.setBounds(0, 0, 1000, 500)
                frame.visible = true
                //Thread.sleep 10000
                frame?.dispose()
            }
        }
    }

    void testFactory() {
        def builder = createBuilder()
        builder.with {
            dockingControl(theme: 'bubble') {
                def data = [
                        dock1: dockable(titleText: 'Dock 1') { label('dock1') },
                        dock2: dockable(titleText: 'Dock 2') { label('dock2') }
                ]
                def factory = { id ->
                    if (id == 'dock3')
                        return dockable(titleText: 'Dock 3') { label(id) }
                    return null
                } as SingleCDockableFactory

                MultipleCDockableFactory factory2 = new RandomFactory()

                dockableFactory(filter: ['dock1', 'dock2'], data: data)
                dockableFactory(filter: 'dock3', factory)
                multiFactory(id: 'multiFactory', factory2)

                frame(frame) {
                    dockingArea(id: 'area') {
                        placeholder(id: 'dock1', dock: [0, 0, 1, 1])
                        placeholder(id: 'dock2', dock: [1, 0, 1, 1], dockMinimized: 'south', dockState: 'minimized')
                        placeholder(id: 'dock3', dock: [0, 1, 1, 1])
                        for (int i: 4..6)
                            multiPlaceholder(id: "dock$i", factory: 'multiFactory', layout: new RandomLayout(title: "Random $i"), dock: [1, 1, 1, 1])
                    }
                }
                frame.setBounds(0, 0, 1000, 500)
                frame.visible = true
                //Thread.sleep 10000
                frame?.dispose()
            }
        }
    }

    void testActions() {
        def builder = createBuilder()
        builder.with {
            dockingControl(theme: 'flat') {
                frame(frame) {
                    dockingArea(id: 'area') {
                        dockable(id: 'dock1', titleText: 'Dock 1', dock: [0, 0, 1, 1]) {
                            buttonAction(
                                    icon: imageIcon('cd.png'),
                                    closure: { evt -> println('Button pressed') }
                            )
                            checkBoxAction(
                                    selected: true,
                                    icon: imageIcon('1uparrow.png'),
                                    selectedIcon: imageIcon('1downarrow.png'),
                                    closure: { println("CheckBox changed: ${it.selected}") }
                            )
                            radioButtonAction(
                                    group: 'firstGroup',
                                    icon: imageIcon('ark_new.png'),
                                    selectedIcon: imageIcon('blend.png'),
                                    closure: { println("RadioButton changed: ${it.selected}") }
                            )
                            radioButtonAction(
                                    group: 'firstGroup',
                                    selected: true,
                                    icon: imageIcon('ark_new.png'),
                                    selectedIcon: imageIcon('blend.png'),
                                    closure: { println("RadioButton changed: ${it.selected}") }
                            )
                            separatorAction()
                            radioButtonAction(
                                    group: 'secondGroup',
                                    selected: true,
                                    icon: imageIcon('attach.png'),
                                    selectedIcon: imageIcon('bookmark.png'),
                                    closure: { println("RadioButton changed: ${it.selected}") }
                            )
                            radioButtonAction(
                                    group: 'secondGroup',
                                    icon: imageIcon('attach.png'),
                                    selectedIcon: imageIcon('bookmark.png'),
                                    closure: { println("RadioButton changed: ${it.selected}") }
                            )
                            dropDownButtonAction(icon: imageIcon('colorize.png')) {
                                buttonAction(
                                        icon: imageIcon('button_accept.png'),
                                        closure: { evt -> println('OK pressed') }
                                )
                                buttonAction(
                                        icon: imageIcon('button_cancel.png'),
                                        selection: true,
                                        closure: { evt -> println('CANCEL pressed') }
                                )
                            }
                            menuAction(icon: imageIcon('configure.png')) {
                                buttonAction(
                                        icon: imageIcon('button_accept.png'),
                                        closure: { evt -> println('OK pressed') }
                                )
                                buttonAction(
                                        icon: imageIcon('button_cancel.png'),
                                        closure: { evt -> println('CANCEL pressed') }
                                )
                            }
                            popupAction(icon: imageIcon('edit.png')) {
                                label('Label 1')
                            }
                            label('dock1')
                        }
                        dockable(id: 'dock2', titleText: 'Dock 2', dock: [0, 1, 1, 1]) {
                            label('dock2')
                        }
                    }
                }
                frame.setBounds(0, 0, 1000, 500)
                frame.visible = true
                //Thread.sleep 10000
                frame?.dispose()
            }
        }
    }

    void testExample() {
        def builder = createBuilder()
        builder.with {
            def frame = frame(title: 'TestFrame') {
                borderLayout()
                dockingControl(theme: 'eclipse') {
                    menuBar {
                        rootMenuPiece(text: 'Docking') {
                            subMenuPiece(text: 'Docks', disableWhenEmpty: true) {
                                dockableListMenuPiece()
                            }
                            subMenuPiece(text: 'Options') {
                                subMenuPiece(text: 'Look&Feel / Theme') {
                                    lookAndFeelMenuPiece()
                                    separatingMenuPiece(topSeparator: true) {
                                        themeMenuPiece()
                                    }
                                }
                                subMenuPiece(text: 'Layout') {
                                    layoutMenuPiece()
                                }
                                preferenceMenuPiece()
                            }
                        }
                    }
                    dockingArea(id: 'area') {
                        dockable(id: 'dock1',
                                titleText: 'Dock 1',
                                dock: [0, 0, 20, 50], // relative bounds in normal mode
                                dockExternalized: [100, 100, 300, 300] // absolute bounds in externalized mode
                        ) { panel(background: Color.GREEN) { label('dock1')} }
                        dockable(id: 'dock2',
                                titleText: 'Dock 2',
                                dock: [0, 50, 20, 50] // relative bounds in normal mode
                        ) { panel(background: Color.RED) { label('dock2')} }
                        dockable(id: 'dock3',
                                titleText: 'Dock 3',
                                dock: [20, 0, 80, 20] // relative bounds in normal mode
                        ) { panel(background: Color.YELLOW) { label('dock3')} }
                        dockable(id: 'dock4',
                                titleText: 'Dock 4',
                                dock: [80, 20, 20, 60] // relative bounds in normal mode
                        ) { panel(background: Color.BLUE) { label('dock4')} }
                        dockable(id: 'dock5',
                                titleText: 'Dock 5',
                                dock: [20, 80, 80, 20], // relative bounds in normal mode
                                dockMinimized: 'south', // position when minimized
                                dockState: 'normalized' // initial mode
                        ) { panel(background: Color.ORANGE) { label('dock5')} }
                        dockable(id: 'dock6',
                                titleText: 'Dock 6',
                                closeable: true,       // dockable can be hidden
                                dock: [20, 20, 60, 60] // relative bounds in normal mode
                        ) { panel(background: Color.MAGENTA) { label('dock6')} }
                        dockable(id: 'dock7',
                                titleText: 'Dock 7',
                                closeable: true,       // dockable can be hidden
                                dock: [20, 20, 60, 60] // relative bounds in normal mode
                        ) { panel(background: Color.CYAN) { label('dock7')} }
                        dockable(id: 'dock8',
                                titleText: 'Dock 8',
                                dock: [0, 0, 20, 50],  // relative bounds in normal mode
                                dockMinimized: 'west', // position when minimized
                                dockState: 'minimized' // initial mode
                        ) { panel(background: Color.WHITE) { label('dock8')} }
                    }
                }
            }
            frame.setBounds(0, 0, 1000, 500)
            frame.visible = true
            Thread.sleep 30000
            frame?.dispose()
        }
    }
}

class RandomDockable extends DefaultMultipleCDockable {
    JTextArea text

    RandomDockable(RandomFactory factory, RandomLayout layout) {
        super(factory, null)
        setTitleText(layout.title)
        text = new JTextArea()
        text.text = layout.content
        add(new JScrollPane(text))
    }

    RandomLayout getLayout() {
        return new RandomLayout(title: getTitleText(), content: text.text)
    }
}

class RandomFactory implements MultipleCDockableFactory<RandomDockable, RandomLayout> {
    RandomLayout write(RandomDockable dockable) {
        return dockable.getLayout()
    }

    RandomDockable read(RandomLayout layout) {
        return new RandomDockable(this, layout)
    }

    boolean match(RandomDockable dockable, RandomLayout layout) {
        return dockable.layout == layout
    }

    RandomLayout create() {
        return new RandomLayout()
    }
}

class RandomLayout implements MultipleCDockableLayout {
    String title
    String content = createContent()

    void writeStream(DataOutputStream out) {
        out.writeUTF(title)
        out.writeUTF(content)
    }

    void readStream(DataInputStream input) {
        title = input.readUTF()
        content = input.readUTF()
    }

    void writeXML(XElement element) {
        element.addElement('title').setString(title)
        element.addElement('content').setString(content)
    }

    void readXML(XElement element) {
        title = element.getElement('title').getString()
        content = element.getElement('content').getString()
    }

    String createContent() {
        StringBuilder sb = new StringBuilder(200)
        def size = Math.random() * 100 + 100
        def chars = 32..126
        for (int p: 100..size) {
            int i = Math.random() * (126 - 32 + 1)
            sb.append((char) chars[i])
        }
        sb.toString()
    }
}



