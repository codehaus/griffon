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

import java.awt.event.ActionListener
import java.awt.Color
import java.awt.Image
import java.awt.Component
import javax.swing.JComponent
import org.jvnet.flamingo.common.*
import org.jvnet.flamingo.ribbon.*
import org.jvnet.flamingo.common.icon.*
import griffon.builder.flamingo.impl.*

/**
 * @author Andres Almiray
 */
public class RibbonFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if(FactoryBuilderSupport.checkValueIsType(value, name, JRibbon)) {
         return value
      }
      return new JRibbon()
   }

   public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
      switch(child) {
         case RibbonTask:
         case JRibbonBand:
         case RibbonContextualTaskGroup:
         case RibbonApplicationMenu:
            // handled by other factories
            break
         case RichTooltip:
            parent.setApplicationMenuRichTooltip(child)
            break
         case Component:
            parent.addTaskbarComponent(child)
            break
      }
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonTaskFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof RibbonTask ) {
         return value
      }

      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("title") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or title: must be defined.")
      }
      builder.context.selected = attributes.remove("selected")

      return new MutableRibbonTask(value)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof JRibbon || parent instanceof JRibbonFrame ) {
         def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
         ribbon.addTask(child)
         if(builder.context.selected) ribbon.setSelectedTask(child)
      } else if( parent instanceof MutableRibbonContextualTaskGroup ) {
         parent.addTask(child)
      }
   }

//    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
//       switch(child) {
//          case JRibbonBand:
//             break
//          case RibbonContextualTaskGroup:
//             parent.setRibbonContextualTaskGroup(child)
//             break
//       }
//    }
}

/**
 * @author Andres Almiray
 */
public class RibbonContextualTaskGroupFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof RibbonContextualTaskGroup ) {
         return value
      }

      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("title") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or title: must be defined.")
      }
      Color color = attributes.remove("hueColor")
      if( !color ) {
         throw new RuntimeException("In $name a value for hueColor: must be defined.")
      }

      return new MutableRibbonContextualTaskGroup(value, color)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof JRibbon || parent instanceof JRibbonFrame ) {
         def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
         parent.addRibbonContextualTaskGroup(child)
      }
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonBandFactory extends AbstractFactory {
   public static final String DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY = "_delegateProperty:ribbonElementPriority";
   public static final String DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY = "ribbonElementPriority";

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      def newChild = newRibbonBand(builder, name, value, attributes)
      builder.context.ribbonBandFactoryClosure = { FactoryBuilderSupport cBuilder, Object cNode, Map cAttributes ->
         if (builder.current == newChild) inspectChild(cBuilder, cNode, cAttributes)
      }
      builder.addAttributeDelegate(builder.context.ribbonBandFactoryClosure)
      builder.context[DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY] = attributes.remove("ribbonElementPriorityProperty") ?: DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY
      return newChild
   }

   static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
       def priority = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY) ?: DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY)
       builder.context.put(node, [priority])
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof AbstractCommandButton ) {
         def props = builder.context[child] ?: [RibbonElementPriority.LOW]
         switch(props[0]) {
            case RibbonElementPriority.TOP:
            case ~/(?i:top)/:
               props[0] = RibbonElementPriority.TOP
               break
            case RibbonElementPriority.MEDIUM:
            case ~/(?i:medium)/:
               props[0] = RibbonElementPriority.MEDIUM
               break
            case RibbonElementPriority.LOW:
            case ~/(?i:low)/:
            default:
               props[0] = RibbonElementPriority.LOW
               break
         }
         parent.addCommandButton(child, props[0])
      } else if( child instanceof JRibbonComponent ) {
         parent.addRibbonComponent(child)
      }
      // TODO RibbonGallery
   }

   void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      super.onNodeCompleted (builder, parent, node)
      builder.removeAttributeDelegate(builder.context.ribbonBandFactoryClosure)
   }

   protected Object newRibbonBand(FactoryBuilderSupport builder, Object name, Object value, Map attributes ) {
      if(value instanceof JRibbonBand) {
         return value
      }
      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("title") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or title: must be defined.")
      }

      def icon = createIcon(builder, name, attributes)
      ActionListener expandActionListener = attributes.remove("expandActionListener")
      def actionPerformed = attributes.remove("actionPerformed")
      if( !expandActionListener && actionPerformed instanceof Closure ) {
         expandActionListener = actionPerformed as ActionListener
      }
      builder.context.task = attributes.remove("task")
      return new JRibbonBand(value, icon, expandActionListener)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof MutableRibbonTask ) {
         parent.addBand(child)
      } else if( (parent instanceof JRibbon || parent instanceof JRibbonFrame) &&
                 builder.context.task ) {
         def taskName = builder.context.task.toString()
         def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
         int taskCount = ribbon.taskCount
         boolean found
         for( int i = 0; i < taskCount; i++ ) {
            def task = ribbon.getTask(i)
            if( task.title == taskName && task instanceof MutableRibbonTask) {
               found = true
               task.addBand(child)
            }
         }
         if( !found ) {
            ribbon.addTask(new MutableRibbonTask(taskName,child))
         }
      }
   }

   protected createIcon( FactoryBuilderSupport builder, Object name, Map attributes ) {
      def icon = FlamingoFactoryUtils.createIcon(builder, name, "", attributes)
      if( !icon ) icon = new EmptyResizableIcon(32)
      return icon
   }
}

/**
 * @author Andres Almiray
 */
public class FlowRibbonBandFactory extends RibbonBandFactory {
   protected Object newRibbonBand(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      if( value instanceof JFlowRibbonBand ) {
         return value
      }
      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("title") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or title: must be defined.")
      }

      def icon = createIcon(builder, name, attributes)
      ActionListener expandActionListener = attributes.remove("expandActionListener")
      def actionPerformed = attributes.remove("actionPerformed")
      if( !expandActionListener && actionPerformed instanceof Closure ) {
         expandActionListener = actionPerformed as ActionListener
      }
      builder.context.task = attributes.remove("task")
      return new JFlowRibbonBand(value, icon, expandActionListener)
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof JComponent ) {
         parent.addFlowComponent(child)
      } else {
         super.setChild(builder, parent, child)
      }
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonComponentFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof JRibbonComponent ) {
         return value
      }

      if( value == null && attributes.containsKey("component") ) {
         value = attributes.remove("component")
      }
      if( !(value instanceof JComponent) ) {
         throw new RuntimeException("In $name either value or component: must be a JComponent")
      }
      def icon = FlamingoFactoryUtils.createIcon(builder, name, "", attributes)
      def caption = attributes.remove("caption")
      return icon && caption ? new JRibbonComponent(value, icon, caption) : new JRibbonComponent(value)
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonApplicationMenuFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof RibbonApplicationMenu ) {
         return value
      }

      return new RibbonApplicationMenu()
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof JRibbon || parent instanceof JRibbonFrame ) {
         def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
         parent.setApplicationMenu(child)
      }
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonApplicationMenuEntryPrimaryFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof RibbonApplicationMenuEntryPrimary ) {
         return value
      }

      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("text") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or text: must be defined.")
      }

      def icon = createIcon(builder, name, attributes)
      ActionListener mainActionListener = attributes.remove("mainActionListener")
      def actionPerformed = attributes.remove("actionPerformed")
      if( !mainActionListener && actionPerformed instanceof Closure ) {
         mainActionListener = actionPerformed as ActionListener
      }
      FlamingoFactoryUtils.translateFlamingoConstant("entryKind", JCommandButton.CommandButtonKind, attributes)
      JCommandButton.CommandButtonKind entryKind = attributes.remove("entryKind")
      if( !entryKind ) throw new RuntimeException("In $name a value for entryKind: must be defined.")

      return new RibbonApplicationMenuEntryPrimary(icon, text, mainActionListener, entryKind)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof RibbonApplicationMenu ) {
         parent.addMenuEntry(child)
      }
   }
}

/**
 * @author Andres Almiray
 */
public class RibbonApplicationMenuEntryFooterFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      if( value instanceof RibbonApplicationMenuEntryFooter ) {
         return value
      }

      if( value instanceof String || value instanceof GString ) {
         value = value.toString()
      } else if( attributes.containsKey("text") ) {
         value = value.toString()
      } else {
         throw new RuntimeException("In $name either value is be a String or text: must be defined.")
      }

      def icon = createIcon(builder, name, attributes)
      ActionListener mainActionListener = attributes.remove("mainActionListener")
      def actionPerformed = attributes.remove("actionPerformed")
      if( !mainActionListener && actionPerformed instanceof Closure ) {
         mainActionListener = actionPerformed as ActionListener
      }

      return new RibbonApplicationMenuEntryFooter(icon, text, mainActionListener)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof RibbonApplicationMenu ) {
         parent.addFooterEntry(child)
      }
   }
}