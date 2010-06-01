/*
 * Copyright 2010 the original author or authors.
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

/**
 * @author Andres Almiray
 */

import java.awt.GridBagConstraints as GBC
import griffon.lookandfeel.LookAndFeelManager

actions {
    action(id: 'sampleNewFileAction',
        name: 'New File',
        mnemonic: 'N',
        closure: {},
        accelerator: shortcut('N'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/page.png"),
        shortDescription: 'New File')
    action(id: 'sampleOpenAction',
        name: 'Open',
        mnemonic: 'O',
        closure: {},
        accelerator: shortcut('O'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/folder_page.png"),
        shortDescription: 'Open File')
    action(id: 'sampleSaveAction',
        name: 'Save',
        mnemonic: 'S',
        closure: {},
        accelerator: shortcut('S'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/disk.png"),
        shortDescription: 'Save File',
        enabled: false)
    action(id: 'sampleCutAction',
        name: 'Cut',
        mnemonic: 'T',
        closure: {},
        accelerator: shortcut('X'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/cut.png"),
        shortDescription: 'Cut')
    action(id: 'sampleCopyAction',
        name: 'Copy',
        mnemonic: 'C',
        closure: {},
        accelerator: shortcut('C'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/page_copy.png"),
        shortDescription: 'Copy')
    action(id: 'samplePasteAction',
        name: 'Paste',
        mnemonic: 'P',
        closure: {},
        accelerator: shortcut('V'),
        smallIcon: imageIcon(resource: "/groovy/ui/icons/page_paste.png"),
        shortDescription: 'Paste')
}

panel(id: 'box') {     
    borderLayout(hgap: 15, vgap: 15)
    panel(constraints: NORTH) {
        gridBagLayout()
        label('Look & Feel', constraints: gbc(fill: GBC.HORIZONTAL, weightx: 0.5))
        comboBox(model: model.providerModel, id: 'providers',
            constraints: gbc(fill: GBC.BOTH, gridwidth: GBC.REMAINDER, weightx: 1.0))
        bean(model, providerSelection: bind{providers.selectedItem})

        label('Theme', constraints: gbc(fill: GBC.BOTH, weightx: 0.5))
        comboBox(model: model.lafInfoModel, id: 'lafs',
            constraints: gbc(fill: GBC.BOTH, gridwidth: GBC.REMAINDER, weightx: 1.0))
        bean(model, lafSelection: bind{lafs.selectedItem})

        label('', constraints: gbc(fill: GBC.BOTH, weightx: 0.5))
        button('Reset [System]', actionPerformed: controller.reset,
            constraints: gbc(fill: GBC.BOTH, gridwidth: GBC.RELATIVE, weightx: 1.0))
        button('Preview', actionPerformed: controller.preview,
            constraints: gbc(fill: GBC.BOTH, gridwidth: GBC.REMAINDER, weightx: 1.0))
    }
    desktopPane(id: 'desktop', constraints: CENTER, preferredSize: [430, 330]) {
        internalFrame(id: 'sampleFrame', title: 'Sample',
                      iconifiable: true, maximizable: true, resizable: true,
                      size: [400, 300], location: [20, 20], visible: true) {
            borderLayout()
            panel(constraints: NORTH) {
                gridLayout(cols: 1, rows: 2)
                menuBar {
                    menu('File') {
                        menuItem(sampleNewFileAction)
                        menuItem(sampleOpenAction)
                        menuItem(sampleSaveAction)
                    }
                    menu('Edit') {
                        menuItem(sampleCutAction)
                        menuItem(sampleCopyAction)
                        menuItem(samplePasteAction)
                    }
                }
                toolBar {
                    button(sampleNewFileAction, text: '')
                    button(sampleOpenAction, text: '')
                    button(sampleSaveAction, text: '')
                    separator()
                    button(sampleCutAction, text: '')
                    button(sampleCopyAction, text: '')
                    button(samplePasteAction, text: '')
                }
            }
            tabbedPane(constraints: CENTER) {
                panel(title: 'Sample') {
                    borderLayout()
                    scrollPane(constraints: CENTER) {
                        panel {
                            gridLayout(cols: 2, rows: 6)
                            checkBox('Enabled selected', selected: true) 
                            radioButton('Enabled selected', selected: true) 
                            checkBox('Disabled selected', enabled: false, selected: true) 
                            radioButton('Disabled selected', enabled: false, selected: true) 
                            checkBox('Enabled unselected')
                            radioButton('Enabled unselected') 
                            comboBox(items: ['Item 1', 'Item 2', 'Item 3'])
                            textField('TextField')
                            comboBox(items: ['Item 1', 'Item 2', 'Item 3'], enabled: false)
                            textField('TextField', enabled: false)
                            label('Label')
                            textField('TextField', editable: false)
                        }
                    } 
                    panel(constraints: SOUTH) {
                        flowLayout()
                        button('prev')
                        button('cancel', enabled: false)
                        button('ok', selected: true)
                    }
                }
                panel(title: 'Renderers') {
                    borderLayout()
                    splitPane(constraints: CENTER, resizeWeight: 0.4d) {
                        scrollPane { tree() }
                        scrollPane { list(items: (1..20).collect{i -> "Item $i"}) }
                    }
                }
            } 
        }
    }
}
