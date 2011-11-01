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

import org.fife.ui.rsyntaxtextarea.SyntaxConstants

modifyFont = { target, sizeFilter, sizeMod ->
    def currentFont = target.font
    if(sizeFilter(currentFont.size)) return
    target.font = currentFont.deriveFont((currentFont.size + sizeMod) as float)
}

action(id: 'hideOutputAction',
    closure: {it.source.visible = false})
action(id: 'runAction',
    keyStroke: shortcut('ENTER'),
    enabled: bind{ model.editable },
    closure: controller.runAction)
action(id: 'increaseFontAction',
    closure: {modifyFont(it.source, {it > 40}, +2)})
action(id: 'decreaseFontAction',
    closure: {modifyFont(it.source, {it < 5}, -2)})

rtextScrollPane(id: 'groovyEditorContainer') {
    rsyntaxTextArea(id: 'groovyEditor', editable: bind{ model.editable },
        syntaxEditingStyle: SyntaxConstants.SYNTAX_STYLE_GROOVY,
        tabSize: 4,
        text: bind('code', source: model, mutual: true),
        cssClass: 'codeEditor') {
        action(runAction)
    }
    
    keyStrokeAction(component: groovyEditor,
        keyStroke: shortcut('shift L'),
        condition: 'in focused window',
        action: increaseFontAction)
    keyStrokeAction(component: groovyEditor,
        keyStroke: shortcut('shift S'),
        condition: 'in focused window',
        action: decreaseFontAction)
}
        
noparent {
    window(visible: false, id: 'outputWindow', pack: true, locationRelativeTo: groovyEditor) {
        scrollPane(border: emptyBorder(2)) {
            outputArea = textPane(
                editable: false,
                name: "outputArea",
                contentType: "text/html",
                background: new Color(255,255,218),
                font: new Font("Monospaced", Font.PLAIN, 18),
                border: emptyBorder(4),
                mousePressed: { outputWindow.visible = false }
            )
            keyStrokeAction(component: outputArea,
                keyStroke: shortcut('shift W'),
                condition: 'in focused window',
                action: hideOutputAction)
            noparent {
                StyledDocument doc = outputArea.styledDocument
                model.document = doc
                Style defStyle = StyleContext.defaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE)
                def applyStyle = {Style style, values -> values.each{k, v -> style.addAttribute(k, v)}}

                def styles = EditorStyles.getPlatformStyles()
                model.styles.regular = doc.addStyle("regular", defStyle)
                applyStyle(model.styles.regular, styles.regular)

                model.styles.promptStyle = doc.addStyle("prompt", model.styles.regular)
                applyStyle(model.styles.promptStyle, styles.prompt)

                model.styles.commandStyle = doc.addStyle("command", model.styles.regular)
                applyStyle(model.styles.commandStyle, styles.command)

                model.styles.outputStyle = doc.addStyle("output", model.styles.regular)
                applyStyle(model.styles.outputStyle, styles.output)

                model.styles.resultStyle = doc.addStyle("result", model.styles.regular)
                applyStyle(model.styles.resultStyle, styles.result)

                model.styles.stacktraceStyle = doc.addStyle("stacktrace", model.styles.regular)
                applyStyle(model.styles.stacktraceStyle, styles.stacktrace)
            }
        }
    }
}
