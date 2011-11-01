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

import groovy.swing.factory.ComponentFactory
import griffon.plugins.slideware.Slide
import griffon.plugins.slideware.factory.SlideHeaderFactory
import griffon.plugins.slideware.factory.SlideFooterFactory

/**
 * @author Andres Almiray
 */
class SlidewareGriffonAddon {
    def factories = [
        slide      : new ComponentFactory(Slide, false),
        slideHeader: new SlideHeaderFactory(),
        slideFooter: new SlideFooterFactory()
    ]

    def mvcGroups = [
        GroovyCodeEditor: [
            model     : 'griffon.plugins.slideware.GroovyCodeEditorModel',
            view      : 'griffon.plugins.slideware.GroovyCodeEditorView',
            controller: 'griffon.plugins.slideware.GroovyCodeEditorController'
        ],
        DeckConfig: [
            model     : 'griffon.plugins.slideware.DeckConfigModel',
            view      : 'griffon.plugins.slideware.DeckConfigView',
            controller: 'griffon.plugins.slideware.DeckConfigController'
        ],
        DeckHelp: [
            model     : 'griffon.plugins.slideware.DeckHelpModel',
            view      : 'griffon.plugins.slideware.DeckHelpView',
            controller: 'griffon.plugins.slideware.DialogController'
        ],
        DeckLauncher: [
            model     : 'griffon.plugins.slideware.DeckLauncherModel',
            view      : 'griffon.plugins.slideware.DeckLauncherView',
            controller: 'griffon.plugins.slideware.DeckLauncherController'
        ],
        DeckPlayer: [
            model     : 'griffon.plugins.slideware.DeckPlayerModel',
            view      : 'griffon.plugins.slideware.DeckPlayerView',
            controller: 'griffon.plugins.slideware.DeckPlayerController'
        ]
    ]
}
