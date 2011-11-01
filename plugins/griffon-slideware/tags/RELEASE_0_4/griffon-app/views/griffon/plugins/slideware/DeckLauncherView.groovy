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
    action(id: 'playAction',
        name: app.getMessage('application.action.Play.name', 'Play'),
        closure: controller.playAction,
        mnemonic: app.getMessage('application.action.Play.mnemonic', 'Y'),
        accelerator: shortcut(app.getMessage('application.action.Play.shortcut', 'Y')),
        shortDescription: app.getMessage('application.action.Play.description', 'Play')
    )
    action(id: 'configAction',
        name: app.getMessage('application.action.Config.name', 'Config'),
        closure: controller.configAction,
        mnemonic: app.getMessage('application.action.Config.mnemonic', 'C'),
        accelerator: shortcut(app.getMessage('application.action.Config.shortcut', 'C')),
        shortDescription: app.getMessage('application.action.Config.description', 'Config')
    )
    action(id: 'helpAction',
        name: app.getMessage('application.action.Help.name', 'Help'),
        closure: controller.helpAction,
        mnemonic: app.getMessage('application.action.Help.mnemonic', 'H'),
        accelerator: shortcut(app.getMessage('application.action.Help.shortcut', 'H')),
        shortDescription: app.getMessage('application.action.Help.description', 'Help')
    )
    action(id: 'printAction',
        name: app.getMessage('application.action.Print.name', 'Print'),
        closure: controller.printAction,
        mnemonic: app.getMessage('application.action.Print.mnemonic', 'P'),
        accelerator: shortcut(app.getMessage('application.action.Print.shortcut', 'P')),
        shortDescription: app.getMessage('application.action.Print.description', 'Print')
    )
    action(id: 'quitAction',
        name: app.getMessage('application.action.Quit.name', 'Quit'),
        closure: controller.quitAction,
        mnemonic: app.getMessage('application.action.Quit.mnemonic', 'Q'),
        accelerator: shortcut(app.getMessage('application.action.Quit.shortcut', 'Q')),
        shortDescription: app.getMessage('application.action.Quit.description', 'Quit')
    )
}

application(name: 'deckLauncherWindow',
    title: GriffonNameUtils.capitalize(app.getMessage('application.title', app.config.application.title)),
    pack: true,
    locationByPlatform: true,
    iconImage: imageIcon('/griffon-icon-48x48.png').image,
    iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                 imageIcon('/griffon-icon-32x32.png').image,
                 imageIcon('/griffon-icon-16x16.png').image]) {
    busyComponent(busy: bind{model.busy}) {
        panel {
            gridLayout(cols: 1, rows: 5)
            button(playAction)
            button(configAction)
            button(printAction)
            button(helpAction)
            button(quitAction)
        }
    }

    keyStrokeAction(component: current.rootPane,
        keyStroke: "ESCAPE",
        condition: "in focused window",
        action: quitAction)
}
