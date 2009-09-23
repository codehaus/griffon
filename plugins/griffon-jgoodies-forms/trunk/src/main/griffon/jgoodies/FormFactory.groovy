/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.jgoodies

import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder

import javax.swing.RootPaneContainer
import javax.swing.JPanel

/**
 * @author Andres.Almiray
 */
class FormFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        JPanel target = null
        FormLayout layout = null
        if(value != null) {
            switch(value) {
                case JPanel:
                    target = value
                    break
                case String:
                    layout = new FormLayout(value,"")
                    break
                case List:
                    layout = new FormLayout(value[0].toString(), value[1].toString())
                    break
                case FormLayout:
                    layout = value
                    break
                default:
                    throw new IllegalArgumentException("$name does not know how to handle $value as value.")
            }
        } else {
            target = attributes.remove("target")
            def l = attributes.remove("layout")
            switch(l) {
                case String:
                    layout = new FormLayout(l,"")
                    break
                case List:
                    layout = new FormLayout(l[0].toString(), l[1].toString())
                    break
                case FormLayout:
                    layout = l
                    break
                default:
                    throw new IllegalArgumentException("$name does not know how to handle $value as layout: value")
            }
        }
        ResourceBundle bundle = attributes.remove("resourceBundle")
        builder.context.formBuilder = newFormBuilder(layout, bundle, target)
        return builder.context.formBuilder.panel
    }

    private static newFormBuilder(FormLayout layout, ResourceBundle bundle, JPanel panel) {
        if(panel) {
            return bundle? new DefaultFormBuilder(layout, bundle, panel) : new DefaultFormBuilder(layout, panel)
        } else {
            return bundle? new DefaultFormBuilder(layout, bundle) : new DefaultFormBuilder(layout)
        }
    }

    boolean isHandlesNodeChildren() {
        return true
    }

    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        def currentNode = builder.current
        try {
            builder.context[builder.CURRENT_NODE] = currentNode instanceof RootPaneContainer ? [] : currentNode
            childContent.resolveStrategy = Closure.DELEGATE_FIRST
            childContent.delegate = new ChainedDelegate([builder.context.formBuilder, builder])
            childContent()
        } finally {
            builder.context[builder.CURRENT_NODE] = currentNode
            return false
        }
    }
}
