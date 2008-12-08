/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.fx.factory

import griffon.builder.fx.FxBuilder
import javax.swing.JComponent
import javafx.ext.swing.SwingComponent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class FxWrapperFactory extends AbstractFxFactory {
    private Factory delegate

    FxWrapperFactory( Factory delegate ) {
        this.delegate = delegate
    }

    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def node = delegate.newInstance( builder, name, value, attributes )
        if( node instanceof JComponent ) {
            builder.context.wrapped = true
            return SwingComponent.wrap(node)
        }
        return node
    }

    public boolean isLeaf() {
        return delegate.leaf
    }

    public boolean isHandlesNodeChildren() {
        return delegate.handlesNodeChildren
    }

    public void onFactoryRegistration(FactoryBuilderSupport builder, String registeredName, String group) {
        delegate.onFactoryRegistration( builder, registeredName, group )
    }

    public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
        // find out if the wrapped object (if any) responds to some of the attributes
        if( builder.context.wrapped ) {
            def wrapped = node.getJComponent()
            def keys = attributes.keySet() as List
            keys.each { key ->
                // using MetaClass.hasProperty() can be tricky with
                // reflect properties coming from listeners
                // such as actionPerformed
                try {
                    wrapped[key] = attributes[key]
                    attributes.remove(key)
                } catch( MissingPropertyException mpe ) {
                    // ignore
                }
            }
            // transform any remaining attributes
            attributes.each { key, value ->
                FxBuilder.translateValue( node, attributes, key, value )
            }
        }
        return delegate.onHandleNodeAttributes( builder, node, attributes )
    }

    public boolean onNodeChildren( FactoryBuilderSupport builder, Object node, Closure childContent) {
        return delegate.onNodeChildren( builder, node, childContent )
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        delegate.onNodeCompleted( builder, parent, node )
        super.onNodeCompleted( builder, parent, node )
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        delegate.setParent( builder, parent, child )
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        delegate.setChild( builder, parent, child )
    }
}