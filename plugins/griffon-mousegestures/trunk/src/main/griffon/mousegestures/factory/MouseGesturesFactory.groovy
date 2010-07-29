/*
 * griffon-mouseguestures: MouseGestures Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package griffon.mousegestures.factory

import java.awt.event.MouseEvent
import com.smardec.mousegestures.MouseGestures
import com.smardec.mousegestures.MouseGesturesListener

/**
 * @author Andres Almiray
 */
class MouseGesturesFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        MouseGestures mg = new MouseGestures()
        mg.mouseButton = MouseEvent.BUTTON1_MASK
        mg
    }

    boolean isLeaf() {
        false
    }

    public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        builder.context.start = attributes.remove('start')
        return true
    }

    boolean isHandlesNodeChildren() {
        true
    }

    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        childContent.resolveStrategy = Closure.DELEGATE_FIRST
        childContent.delegate = new MouseGesturesListenerImpl(builder, node)

        String parentName = builder.currentName
        Map parentContext = builder.context
        builder.newContext()
        try {
            builder.context.put(FactoryBuilderSupport.OWNER, childContent.owner)
            builder.context.put(FactoryBuilderSupport.CURRENT_NODE, node)
            builder.context.put(FactoryBuilderSupport.PARENT_FACTORY, builder.currentFactory)
            builder.context.put(FactoryBuilderSupport.PARENT_NODE, builder.current)
            builder.context.put(FactoryBuilderSupport.PARENT_CONTEXT, parentContext)
            builder.context.put(FactoryBuilderSupport.PARENT_NAME, parentName)
            builder.context.put(FactoryBuilderSupport.PARENT_BUILDER, parentContext.get(FactoryBuilderSupport.CURRENT_BUILDER))
            builder.context.put(FactoryBuilderSupport.CURRENT_BUILDER, parentContext.get(FactoryBuilderSupport.CHILD_BUILDER))
            childContent()
        } finally {
            builder.popContext()
        }

        false
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(builder.context.start) node.start()
    }

    private static class MouseGesturesListenerImpl extends GroovyObject implements MouseGesturesListener {
        private final _node
        private final FactoryBuilderSupport _builder
        private Closure processGestureClosure
        private Closure gestureRecognizedClosure

        MouseGesturesListenerImpl(FactoryBuilderSupport builder, node) {
            _builder = builder
            _node = node
            _node.addMouseGesturesListener(this)
        }

        void gestureMovementRecognized(String gesture) {
            this.gestureRecognizedClosure?.call(gesture)
        }

        void processGesture(String gesture) {
            this.processGestureClosure?.call(gesture)
        }

        void onGestureMovementRecognized(Closure cls) {
            this.gestureRecognizedClosure = cls
        }

        void onProcessGesture(Closure cls) {
            this.processGestureClosure = cls
        }

        def methodMissing(String name, args) {
            try { return _node."$name"(*args) }
            catch(MissingMethodException mme) {
                return _builder."$name"(*args)
            }
        }

        void propertyMissinh(String name, value) {
            try {_ node."$name" = value }
            catch(MissingPropertyException mpe) {
                _builder."$name" = value
            }
        }

        def propertyMissing(String name) {
            try { return _node."$name" }
            catch(MissingPropertyException mpe) {
                return _builder."$name"
            }
        }
    }
}