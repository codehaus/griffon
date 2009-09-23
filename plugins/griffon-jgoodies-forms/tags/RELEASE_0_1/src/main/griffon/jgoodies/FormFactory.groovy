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
                    layout = resolveLayout(builder, name, attributes.remove("layout"))
                    break
                case String:
                    layout = new FormLayout(value,"")
                    break
                case List:
                    layout = resolveLayout(builder, name, value)
                    break
                case FormLayout:
                    layout = value
                    break
                default:
                    throw new IllegalArgumentException("$name does not know how to handle $value as value.")
            }
            if(layout && attributes.containsKey("layout")) {
                 throw new IllegalArgumentException("$name does not know how to handle a FormLayout as value and layout:, specify only one.")
            }
        } else {
            target = attributes.remove("target")
            layout = resolveLayout(builder, name, attributes.remove("layout"))
        }
        ResourceBundle bundle = attributes.remove("resourceBundle")
        builder.context.formBuilder = newFormBuilder(layout, bundle, target)
        return builder.context.formBuilder.panel
    }

    private static resolveLayout(FactoryBuilderSupport builder, Object name, layoutValue ) {
        if(!layoutValue) throw new IllegalArgumentException("Must specify a value for ${name}.layout:")
        def layout
        switch(layoutValue) {
            case String:
                layout = new FormLayout(layoutValue,"")
                break
            case List:
                switch(layoutValue.size()) {
                    case 1: layout = new FormLayout(layoutValue[0]?.toString()); break
                    case 2: layout = new FormLayout(layoutValue[0]?.toString(), layoutValue[1]?.toString()); break
                    default: throw new IllegalArgumentException("Too many arguments in ${name}.layout:, specify 1 or 2 elements.")
                }
                break
            case FormLayout:
                layout = layoutValue
                break
            default:
                throw new IllegalArgumentException("$name does not know how to handle $layoutValue as layout: value")
        }
        return layout
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
