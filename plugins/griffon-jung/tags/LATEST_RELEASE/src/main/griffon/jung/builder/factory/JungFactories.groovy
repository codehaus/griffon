/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.jung.builder.factory

import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.Forest
import edu.uci.ics.jung.graph.Tree

import edu.uci.ics.jung.visualization.*
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
import edu.uci.ics.jung.algorithms.layout.*

import java.util.logging.Level
import java.util.logging.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
class VisualizationChildFactory extends AbstractFactory {
    final Class beanClass
    final String property
    final protected boolean leaf

    VisualizationChildFactory(Class beanClass, String property) {
        this(beanClass, property, false)
    }

    VisualizationChildFactory(Class beanClass, String property, boolean leaf) {
        this.beanClass = beanClass
        this.property = property
        this.leaf = leaf
    }

    boolean isLeaf() {
        return leaf
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        beanClass.newInstance()
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
        if(parent instanceof VisualizationServer) {
            parent."$property" = node
        }
    }
}

/**
 * @author Andres.Almiray
 */
class LayoutFactory extends VisualizationChildFactory {
    final Class[] ctrArgs
    final Class graphType

    LayoutFactory(Class beanClass, Class graphType = Graph) {
        super(beanClass, "graphLayout", true)
        this.graphType = graphType
        ctrArgs = [graphType] as Class[]
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(value && beanClass.isAssignableFrom(value.getClass())) {
            return value
        }
        if(!value) value = builder.parentContext?.graph
        if(!value) value = attributes.remove("graph")
        if(!FactoryBuilderSupport.checkValueIsType(value, name, graphType)) {
            throw new IllegalArgumentException("In $name you must define a value or a graph: property of type [Graph, Forest, Tree].")
        }

        try {
            return beanClass.getConstructor(ctrArgs).newInstance(value)
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        }
    }
}

/**
 * @author Andres.Almiray
 */
class VisualizationViewerFactory extends AbstractFactory {
    final Class beanClass
    final Constructor ctor
    final protected boolean leaf
    private static final Class[] CTOR_ARGS = [Layout]

    VisualizationViewerFactory(Class beanClass) {
        this(beanClass, false)
    }

    VisualizationViewerFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf

        try {
            ctor = beanClass.getConstructor(CTOR_ARGS);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger("global").log(Level.INFO, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }

    boolean isLeaf() {
        return leaf
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(value && beanClass.isAssignableFrom(value.getClass())) {
            return value
        }
        if(!value) value = attributes.remove("graph")
        if(!value) value = attributes.remove("graphLayout")
        switch(value) {
            case Forest:
                return makeViewer(builder, name, new TreeLayout(value))
            case Graph:
                return makeViewer(builder, name, new CircleLayout(value))
            case Layout:
                return makeViewer(builder, name, value)
            default:
                throw new IllegalArgumentException("In $name you must define a one of graph:, graphLayout: or a node value.")
        }
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        def deletions = []
        attributes.each { k, v ->
           try {
              node.renderContext[(k)] = v
              deletions << k
              return
           } catch(MissingPropertyException mpe) { /*ignore*/ }
           try {
              node.renderer[(k)] = v
              deletions << k
              return
           } catch(MissingPropertyException mpe) { /*ignore*/ }
        }
        deletions.each { attributes.remove(it) }
        return true
    }

    private VisualizationServer makeViewer(FactoryBuilderSupport builder, Object name, Layout layout) {
        builder.context.graph = layout.graph
        try {
            return ctor.newInstance(layout)
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        }
    }
}
