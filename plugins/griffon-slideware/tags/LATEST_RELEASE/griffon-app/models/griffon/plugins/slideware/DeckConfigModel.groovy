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

package griffon.plugins.slideware

import static griffon.util.GriffonNameUtils.isBlank

/**
 * @author Andres Almiray
 */
class DeckConfigModel extends AbstractDialogModel {
    @Bindable boolean fullScreen = false
    @Bindable String screenWidth = ''
    @Bindable String screenHeight = ''
    @Bindable String fileName = ''

    protected String getDialogKey()   { 'DeckConfig' }
    protected String getDialogTitle() { 'Configuration' }

    void mvcGroupInit(Map<String, Object> args) {
        super.mvcGroupInit(args)
        resizable = false
        readConfig()
    }
    
    void readConfig() {
        fullScreen = app.config.presentation.fullScreen
        screenWidth = app.config.presentation.screenWidth
        screenHeight = app.config.presentation.screenHeight
        fileName = app.config.presentation.fileName
    }

    boolean validate() {
        fullScreen ?: isNumber(screenWidth) && isNumber(screenHeight)
    }

    void writeConfig() {
        app.config.presentation.fullScreen = fullScreen
        if(!fullScreen) {
            app.config.presentation.screenWidth = screenWidth
            app.config.presentation.screenHeight = screenHeight
        }
        if(fileName) app.config.presentation.fileName = fileName      
    }

    private static boolean isNumber(val) {
        try {
            Integer.parseInt(val)
            true
        } catch(NumberFormatException nfe) {
            false
        }
    }
}
