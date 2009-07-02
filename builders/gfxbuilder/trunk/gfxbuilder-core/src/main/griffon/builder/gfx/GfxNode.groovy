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

import java.beans.PropertyChangeListener

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class GfxNode /*extends GroovyObjectSupport*/ implements PropertyChangeListener {
   private final String _name
   private Map _props = new ObservableMap()

   @GfxAttribute(alias="n") String name
   protected boolean _dirty

   GfxNode( String name ) {
      _name = name
      this.addPropertyChangeListener(this)
      this._props.addPropertyChangeListener(this)
   }

   String getNodeName() {
      _name
   }

   String toString() {
      name ? "${_name}[${name}]" : _name
   }

   final Map getProps() {
      _props
   }

   abstract void apply(GfxContext context)

   public void propertyChange( PropertyChangeEvent event ) {
      if( event.source == this ||
         (event.source == _props && event instanceof ObservableMap.PropertyUpdatedEvent) ) {
          _dirty = true
          onDirty(event)
          _dirty = false
      }
   }

   void onDirty(PropertyChangeEvent event) {
      // empty
   }

// TODO - write property value converter

}