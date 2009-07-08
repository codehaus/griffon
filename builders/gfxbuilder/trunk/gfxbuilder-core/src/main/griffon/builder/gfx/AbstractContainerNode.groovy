/*
 * Copyright 2007-2009 the original author or authors.
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
 */

package griffon.builder.gfx

import java.beans.PropertyChangeEvent
import groovy.util.ObservableList.ElementEvent

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractContainerNode extends GfxNode implements ContainerNode {
   private final ObservableList/*<GfxNode>*/ _nodes = new ObservableList()

   AbstractContainerNode(String name) {
      super( name )
      _nodes.addPropertyChangeListener(this)
   }

   final List/*<GfxNode>*/ getNodes() {
      _nodes
   }

   void addNode(GfxNode node) {
      if(!node || _nodes.contains(node)) return
      node.addPropertyChangeListener(this)
      _nodes << node
   }

   AbstractContainerNode leftShift(GfxNode node) {
      addNode(node)
      this
   }

   void propertyChange(PropertyChangeEvent event) {
      if(event.source == _nodes){
         handleElementEvent(event)
      } else {
         super.propertyChange(event)
      }
   }

   final void apply(GfxContext context) {
       beforeApply(context)
       applyNode(context)
       if( !_nodes.empty ){
          _nodes.each { n -> applyNestedNode(n, context) }
       }
       afterApply(context)
   }

   protected void beforeApply(GfxContext context) {}

   protected abstract void applyNode(GfxContext context)

   protected void applyNestedNode(GfxNode node, GfxContext context) {
      node.apply(context)
   }

   protected void afterApply(GfxContext context) {}

   protected void handleElementEvent(ElementEvent event) {
      switch( event.type ) {
         case ElementEvent.ADDED:
             event.newValue.addPropertyChangeListener(this)
             break
         case ElementEvent.REMOVED:
             event.newValue.removePropertyChangeListener(this)
         case ElementEvent.MULTI_ADD:
             event.values.each { it.addPropertyChangeListener(this) }
             break
         case ElementEvent.CLEARED:
         case ElementEvent.MULTI_REMOVE:
             event.values.each { it.removePropertyChangeListener(this) }
         case ElementEvent.UPDATED:
             break
      }
      _dirty = true
      onDirty(event)
      _dirty = false
   }

   protected def findLast(Closure cls) {
      _nodes.reverse().find( cls )
   }
}