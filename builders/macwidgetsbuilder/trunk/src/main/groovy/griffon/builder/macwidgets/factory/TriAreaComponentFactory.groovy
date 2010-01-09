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

package griffon.builder.macwidgets.factory

import javax.swing.SwingConstants
import javax.swing.JComponent
import com.explodingpixels.macwidgets.BottomBarSize
import com.explodingpixels.macwidgets.ComponentBottomBar
import com.explodingpixels.macwidgets.TriAreaComponent
import com.explodingpixels.macwidgets.MacWidgetFactory

import groovy.swing.SwingBuilder

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TriAreaComponentFactory extends AbstractFactory {
   public static final String DELEGATE_PROPERTY_LOCATION_ON_BAR = "_delegateProperty:locationOnBar";
   public static final String DEFAULT_DELEGATE_PROPERTY_LOCATION_ON_BAR= "locationOnBar";
   public static final String DELEGATE_PROPERTY_SPACER = "_delegateProperty:spacer";
   public static final String DEFAULT_DELEGATE_PROPERTY_SPACER= "spacer";

   final Class beanClass

   TriAreaComponentFactory( Class beanClass ) {
      this.beanClass = beanClass
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
        def newChild = newTriAreaComponent(builder, name, value, attributes)
        builder.context.triAreaComponentFactoryClosure =
            { FactoryBuilderSupport cBuilder, Object cNode, Map cAttributes ->
                if (builder.current == newChild) inspectChild(cBuilder, cNode, cAttributes)
            }
        builder.addAttributeDelegate(builder.context.triAreaComponentFactoryClosure)

        builder.context[DELEGATE_PROPERTY_LOCATION_ON_BAR] = attributes.remove("locationOnBarProperty") ?: DEFAULT_DELEGATE_PROPERTY_LOCATION_ON_BAR
        builder.context[DELEGATE_PROPERTY_SPACER] = attributes.remove("spacerProperty") ?: DEFAULT_DELEGATE_PROPERTY_SPACER

        return newChild
   }

   public Object newTriAreaComponent( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)

      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass) ) {
         builder.context.triArea = value
         builder.setVariable(builder.context.widgetId,value)
         return value
      }

      def triArea = triAreaInstance(builder, name, value, attributes)
      builder.context.triArea = triArea
      builder.setVariable(builder.context.widgetId,triArea)
      return triArea.component
   }

   public static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
       def locationOnBar = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_LOCATION_ON_BAR) ?: DEFAULT_DELEGATE_PROPERTY_LOCATION_ON_BAR)
       def spacer = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_SPACER) ?: DEFAULT_DELEGATE_PROPERTY_SPACER)

       builder.context.put(node, [locationOnBar:locationOnBar,spacer:spacer])
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if (!(child instanceof JComponent) ) {
         return
      }
      try {
         def ops = builder.context[child] ?: [:]
         if( !ops ) {
            throw new RuntimeException("In ${builder.parentName} you must specify a value for locationOnBar:")
         }

         switch(ops.locationOnBar) {
            case ~/(?i:left)/:
            case SwingConstants.LEFT:
               addChildToLeft(builder,parent,child,ops.get("spacer",0))
               break
            case ~/(?i:center)/:
            case SwingConstants.CENTER:
               addChildToCenter(builder,parent,child,ops.get("spacer",0))
               break
            case ~/(?i:right)/:
            case SwingConstants.RIGHT:
               addChildToRight(builder,parent,child,ops.get("spacer",0))
               break
            default:
               throw new RuntimeException("In ${builder.parentName} value of locationOnBar: must be one of ['left','center','right] or the corresponding SwingConstants field.")
         }
      } catch( MissingPropertyException mpe ) {
         addChildToLeft(builder,parent,child,ops.get("spacer",0))
      }
   }

   protected void addChildToLeft(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToLeft(child,spacer)
   }

   protected void addChildToCenter(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToCenter(child,spacer)
   }

   protected void addChildToRight(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToRight(child,spacer)
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
       super.onNodeCompleted (builder, parent, node)
       builder.removeAttributeDelegate(builder.context.triAreaComponentFactoryClosure)
   }

   protected triAreaInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      return beanClass.newInstance()
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BottomBarFactory extends TriAreaComponentFactory {
   BottomBarFactory() {
      super(TriAreaComponent)
   }

   protected triAreaInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      def size = attributes.remove("size") ?: BottomBarSize.SMALL
      if( size instanceof String ) {
         switch(size) {
            case ~/(?i:extra[_| ]small)/:
               size = BottomBarSize.EXTRA_SMALL
               break
            case ~/(?i:large)/:
               size = BottomBarSize.LARGE
               break
            default:
               size = BottomBar.SMALL
         }
      }
      return MacWidgetFactory.createBottomBar(size)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class ComponentStatusBarFactory extends TriAreaComponentFactory {
   ComponentStatusBarFactory() {
      super(ComponentBottomBar)
   }

   protected void addChildToLeft(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToLeftWithBorder(child)
   }

   protected void addChildToCenter(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToCenterWithBorder(child)
   }

   protected void addChildToRight(FactoryBuilderSupport builder, Object parent, Object child, int spacer) {
      builder.parentContext.triArea.addComponentToRightWithBorder(child)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class UnifiedToolBarFactory extends TriAreaComponentFactory {
   UnifiedToolBarFactory() {
      super(TriAreaComponent)
   }

   protected triAreaInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      return MacWidgetFactory.createUnifiedToolBar()
   }
}