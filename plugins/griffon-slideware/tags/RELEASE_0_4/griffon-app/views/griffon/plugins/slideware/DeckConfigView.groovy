/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.plugins.slideware

actions {
    action(id: 'cancelAction',
       name: app.getMessage('application.action.Cancel.name', 'Cancel'),
       closure: controller.hide,
       mnemonic: app.getMessage('application.action.Cancel.mnemonic', 'C'),
       shortDescription: app.getMessage('application.action.Cancel.description', 'Cancel')
    )
    action(id: 'okAction',
       name: app.getMessage('application.action.Ok.name', 'Ok'),
       closure: controller.accept,
       mnemonic: app.getMessage('application.action.Ok.mnemonic', 'K'),
       shortDescription: app.getMessage('application.action.Ok.description', 'Ok')
    )
}

panel(id: 'content') {
    migLayout layoutConstraints: 'fill'
    label(app.getMessage('application.dialog.DeckConfig.fullScreen', 'Full screen'))
    checkBox(constraints: 'left, span 3, wrap',
        selected: bind('fullScreen', source: model, mutual: true))
    label(app.getMessage('application.dialog.DeckConfig.screenSize', 'Screen size'))
    textField(columns: 4, constraints: 'left',
        editable: bind{!model.fullScreen}, enabled: bind{!model.fullScreen},
        text: bind('screenWidth', source: model, mutual: true))
    label('x', constraints: 'center')
    textField(columns: 4, constraints: 'left, wrap',
        editable: bind{!model.fullScreen}, enabled: bind{!model.fullScreen},
        text: bind('screenHeight', source: model, mutual: true))
    label(app.getMessage('application.dialog.DeckConfig.fileName', 'File Name'))
    textField(columns: 20, constraints: 'left, span 3, wrap',
        text: bind('fileName', source: model, mutual: true))
    panel(constraints: 'span 4, grow') {
        migLayout layoutConstraints: 'fill'
        label(' ', constraints: 'grow')
        button(cancelAction, constraints: 'right')
        button(okAction, constraints: 'right')
    }
    
    keyStrokeAction(component: current,
        keyStroke: "ESCAPE",
        condition: "in focused window",
        action: cancelAction)
}
