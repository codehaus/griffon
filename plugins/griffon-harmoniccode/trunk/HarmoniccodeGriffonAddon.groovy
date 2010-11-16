/*
 * Copyright (c) 2010 griffon-harmoniccode - Andres Almiray. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of griffon-harmoniccode - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 'AS IS'
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import groovy.swing.factory.RichActionWidgetFactory
import eu.hansolo.custom.mbutton.*
import eu.hansolo.custom.*
import eu.hansolo.signaltower.*
import eu.hansolo.tools.ColorDef

import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
class HarmoniccodeGriffonAddon {
    void addonInit(GriffonApplication app) {
        for(color in ColorDef) {
            props['COLORDEF_'+color.name()] = [get: {c-> c}.curry(color)]
        }
    }

    def factories = [
        mbutton: new RichActionWidgetFactory(MButton),
        steelCheckBox: new RichActionWidgetFactory(SteelCheckBox),
        rollingCounter: Counter,
        signalTower: Design42
    ]

    def props = [
        COUNTER_THEME_BRIGHT: [get: {Theme.BRIGHT}],
        COUNTER_THEME_DARK: [get: {Theme.DARK}]
    ]
}
