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

package griffon.flyingsaucer.factory

import org.xhtmlrenderer.extend.UserAgentCallback

/**
 * @author Andres Almiray
 */
class XhtmlPanelFactory extends AbstractFactory {
    private static final Class[] PARAMS = [UserAgentCallback] as Class[]
    final Class beanClass
    final protected boolean leaf

    XhtmlPanelFactory(Class beanClass) {
        this(beanClass, false)
    }

    XhtmlPanelFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }

    boolean isLeaf() {
        return leaf
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        UserAgentCallback userAgentCallback = attributes.remove('userAgentCallback')
        if(!userAgentCallback) return beanClass.newInstance()
        return beanClass.getDeclaredConstructor(PARAMS).newInstance([userAgentCallback] as Object[])
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def document = attributes.remove('document')
        if(document) node.setDocument(document)
        return true
    }
}
