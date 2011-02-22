/*
 * Copyright 2007 the original author or authors.
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
package groovy.swing

import groovy.swing.SwingXBuilder
import javax.swing.WindowConstants
import org.jdesktop.swingx.*
import org.jdesktop.swingx.JXMonthView
import org.jdesktop.swingx.color.EyeDropperColorChooserPanel
import org.jdesktop.swingx.color.GradientPreviewPanel
import org.jdesktop.swingx.combobox.ListComboBoxModel
import org.jdesktop.swingx.combobox.MapComboBoxModel
// import org.jdesktop.swingx.editors.ImagePicker
// import org.jdesktop.swingx.editors.PaintPicker
// import org.jdesktop.swingx.editors.ShapeChooser
import org.jdesktop.swingx.icon.ColumnControlIcon
import org.jdesktop.swingx.icon.EmptyIcon
// import org.jdesktop.swingx.icon.SortArrowIcon
import org.jdesktop.swingx.tips.DefaultTip
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel


/**
 * @author James Williams
 */
public class ComponentsTest extends GroovyTestCase {
    def swing
    def frame
    void setup() {
        swing = new SwingXBuilder()
    }
    void testWidgetCreation() {
        def widgets = [
            busyLabel: JXBusyLabel,
            button: JXButton,
            label: JXLabel,
            panel: JXPanel,
            titledPanel: JXTitledPanel,
            titledSeparator: JXTitledSeparator,
            colorSelectionButton: JXColorSelectionButton,
            collapsiblePane: JXCollapsiblePane,
            columnControlIcon: ColumnControlIcon,
            datePicker: JXDatePicker,
            editorPane: JXEditorPane,
            emptyIcon: EmptyIcon,
            errorPane: JXErrorPane,
            eyeDropperColorChooser: EyeDropperColorChooserPanel,
            findBar: JXFindBar,
            findPanel: JXFindPanel,
            glassBox: JXGlassBox,
            gradientChooser: JXGradientChooser,
            gradientPreviewPanel: GradientPreviewPanel,
            header: JXHeader,
            hyperlink: JXHyperlink,
            imagePanel: JXImagePanel,
            // imagePicker: ImagePicker,
            imageView: JXImageView,
            list: JXList,
            listComboBoxModel: ListComboBoxModel,
            // loginDialog: JXLoginPane.JXLoginDialog,
            loginPanel: JXLoginPane,
            mapComboBoxModel: MapComboBoxModel,
            monthView: JXMonthView,
            multiSplitPane: JXMultiSplitPane,
            // paintPicker: PaintPicker,
            radioGroup: JXRadioGroup,
            rootPane: JXRootPane,
            searchPanel: JXSearchPanel,
            // shapeChooser: ShapeChooser,
            // sortArrowIcon: SortArrowIcon,
            statusBar: JXStatusBar,
            table: JXTable,
            tableHeader: JXTableHeader,
            tree: JXTree,
            treeTable: JXTreeTable
        ]
        setup()
        def a
        widgets.each {name, expectedClass ->
            def frame = swing.frame() {
                if (name != "progressBar")
                    a = "$name"(id: "${name}Id".toString())
                else {a = "$name"()}
            }
            assert a.class == expectedClass
        }

    }

    void testWidget() {
        setup()
        def panel = swing.panel() {
            label(text: "This is a label.")
        }
        frame = swing.frame() {
            widget(panel)
        }
        assertTrue frame.getContentPane().getComponents().size() > 0
    }

    void testTipOfTheDay() {
        setup()
        def list = [new DefaultTip(), new DefaultTip()]
        def tip
        def frame = swing.frame() {
            tip = tipOfTheDay(model: new DefaultTipOfTheDayModel(list))
        }
        assertTrue tip != null && tip.model != null
    }

    void testDialog() {
        setup()
        def testdialog
        def frame = swing.frame(id: "frame", title: "Swing", size: [640, 480],
                resizable: false, locationRelativeTo: null,
                defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
            testdialog = dialog(id: "dialog", size: [320, 240], resizable: false, locationRelativeTo: frame, owner: frame)

        }
        assertTrue testdialog != null
    }

    void testClassicComponents() {
        setup()
        def button = swing.button(text: 'Click me', classicSwing: true)
        assertTrue button.class == javax.swing.JButton
    }

/* appears to be broken broken broken.  Need tests when it's fixed
    void testMultiSplitPane() {
        setup()

        swing.frame(id:'frame') {
            multiSplitPane(id: 'msp') {
                
                    split(rowLayout: false) {
                        leaf(name: "top")
                        divider()
                        leaf(name: "bottom")
                    }
                
                button(text: "Left Button", constraints: "left", id: 'left')
                button(text: "Right Button", constraints: "right", id: 'right')
                button(text: "Top Button", constraints: "top", id: 'top')
                button(text: "Bottom Button", constraints: "bottom", id: 'bottom')
            }
        }
        swing.frame.pack()
        swing.frame.dispose()
        // assert all components there
        assert swing.msp.components == [swing.left, swing.right, swing.top, swing.bottom]
        // assert left, top, right got sized
        def zero = [0,0] as java.awt.Dimension
        assert swing.left.size != zero
        assert swing.top.size != zero
        assert swing.bottom.size != zero
        // assert right didn't get sized, not laid out
        assert swing.right.size == zero
    }*/

    void testFrame() {
        setup()

        // test cancel button
        swing.frame(id:'frame') {
            button('button', cancelButton:true, id:'button')
        }
        assert swing.frame.rootPaneExt.cancelButton == swing.button
        swing.frame(id:'frame') {
            button('button', id:'button')
        }
        assert swing.frame.rootPaneExt.cancelButton == null

        // toobar and status bar
        swing.frame(id:'frame') {
            toolBar(id:'toolbar') {
                button('test')
            }
            statusBar(id:'statusbar') {
                label('test')
            }
            label('content', id:'content')
        }
        assert swing.toolbar.parent == swing.frame.contentPane
        //assert swing.statusbar.parent == swing.frame.contentPane
        // swingx 0.9.2 statutsbar is added to frame.rootPane
        assert swing.statusbar.parent == swing.frame.rootPane
        assert swing.toolbar == swing.frame.rootPaneExt.toolBar
        assert swing.statusbar == swing.frame.rootPaneExt.statusBar
    }
}
