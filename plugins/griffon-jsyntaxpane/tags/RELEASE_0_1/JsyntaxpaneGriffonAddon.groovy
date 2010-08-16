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

import griffon.core.GriffonApplication
import jsyntaxpane.DefaultSyntaxKit
import jsyntaxpane.util.Configuration
import javax.swing.JEditorPane

/**
 * @author Andres Almiray
 */
class JsyntaxpaneGriffonAddon {
    def addonInit(GriffonApplication app) {
        // 1. merge config
        Configuration syntaxConfig = DefaultSyntaxKit.getConfig(DefaultSyntaxKit)
        app.config?.syntaxpane?.props.each { k, v ->
            syntaxConfig.put(k as String, v as String)
        }

        // 2. init the kit
        app.execSync {
            DefaultSyntaxKit.initKit()
        }
    }

    private static final Map<JEditorPane, Map<String,String>> PROPS = [:]

    def attributeDelegates = [
        {builder, node, attributes ->
            if(node instanceof JEditorPane && attributes.contentType) {
                PROPS[node] =  [:]
                PROPS[node].contentType = attributes.remove('contentType')
                PROPS[node].text = attributes.text
            }
        }
    ]

    def postNodeCompletionDelegates = [
        {builder, parent, node ->
            if(node instanceof JEditorPane && PROPS[node]) {
                Map props = PROPS.remove(node)
                node.contentType = props.contentType
                node.text = props.text
            }
        }
    ]
}
