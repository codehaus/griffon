/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.flamingo.factory

import javax.swing.Icon
import org.jvnet.flamingo.slider.FlexiRangeModel
import org.jvnet.flamingo.slider.JFlexiSlider
import javax.swing.event.ChangeListener

import groovy.swing.SwingBuilder
import groovy.swing.factory.LayoutFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class FlexiSliderFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        builder.context.ranges = []
        builder.context.controlPoints = []
        builder.context.flexiId = attributes[builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID]
        builder.context.constraints = attributes[LayoutFactory.DELEGATE_PROPERTY_CONSTRAINT] ?: attributes[LayoutFactory.DEFAULT_DELEGATE_PROPERTY_CONSTRAINT]
        return [:]
    }

    public boolean isLeaf() {
        return false
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        if( builder.context.ranges && builder.context.controlPoints ) {
           def icons = []
           def texts = []
           builder.context.controlPoints.each {
              icons << it.icon
              texts << it.text
           }
           def flexiSlider = new JFlexiSlider( builder.context.ranges as FlexiRangeModel.Range[],
                                               icons as Icon[],
                                               texts as String[] )
           if( node.stateChanged ) {
              flexiSlider.model.addChangeListener(node.stateChanged as ChangeListener)
           }
           if( builder.context.flexiId ) {
              builder.setVariable(builder.context.flexiId, flexiSlider)
           }
           if( builder.context.startingRange ) {
              def range = builder.context.startingRange.range
              def fraction = builder.context.startingRange.fraction
              flexiSlider.value = new FlexiRangeModel.Value(range,fraction)
           }
           if( builder.parentFactory ) {
               builder.parentFactory.setChild(builder,parent,flexiSlider)
           }
        }
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        // empty
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        // empty
    }
}

class FlexiRangeFactory extends AbstractFactory {
    public boolean isLeaf() {
        return true
    }

    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def n = attributes.remove("name")
        def d = attributes.remove("discrete") ?: false
        def w = attributes.remove("weight") ?: 0.0
        builder.context.startingValue = attributes.remove("startingValue")

        if( !n ) {
            return new FlexiRangeModel.Range(d,w)
        } else {
            return new FlexiRangeModel.Range(n,d,w)
        }
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( builder?.parentContext?.ranges != null ) {
            builder.parentContext.ranges << child
            if( builder.context.startingValue != null ) {
               builder.parentContext.startingRange = [range:child,fraction:builder.context.startingValue]
            }
        }
    }
}

class FlexiControlPointFactory extends AbstractFactory {
    public boolean isLeaf() {
        return true
    }

    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def i = attributes.remove("icon")
        def t = attributes.remove("text")

        if( !i || !(i instanceof Icon)) {
            throw new IllegalArgumentException("In $name icon: attributes must be of type javax.swing.Icon")
        }
        if( !t || !(t instanceof String)) {
            throw new IllegalArgumentException("In $name text: attributes must be of type java.lang.String")
        }

        return [icon: i, text: t]
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        if( builder?.parentContext?.controlPoints != null ) {
            builder.parentContext.controlPoints << child
        }
    }
}