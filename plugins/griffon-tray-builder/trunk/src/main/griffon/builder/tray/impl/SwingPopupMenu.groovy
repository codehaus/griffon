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

package griffon.builder.tray.impl

import java.awt.Component
import java.awt.PopupMenu
import javax.swing.Action
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JSeparator

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SwingPopupMenu extends PopupMenu {
   private JPopupMenu delegate = new JPopupMenu()

   public void add( Action action ) {
      delegate.add(action)
   }

   public void add( JMenuItem item ) {
      delegate.add(item)
   }

   public void add( String label ) {
      delegate.add(action)
   }

   public void add( JSeparator separator ) {
      delegate.addSeparator()
   }

   public void addSeparator() {
      delegate.addSeparator()
   }

   public void show( Component invoker, int x, int y ) {
      delegate.show(invoker, x, y)
   }

   def methodMissing( String name, args ) {
      try {
         return delegate."$name"(*args)
      } catch( MissingMethodException mpe ) {
         throw new MissingMethodException(name,PopupMenu,mpe.arguments,mpe.static)
      }
   }

   def getProperty( String name ) {
      def metaProperty = JPopupMenu.metaClass.getMetaProperty(name)
      if(metaProperty) return metaProperty.getProperty(delegate)
      metaProperty = PopupMenu.metaClass.getMetaProperty(name)
      if(metaProperty) return metaProperty.getProperty(this)
      throw new MissingPropertyException(name,Object)
   }

   void setProperty( String name, value ) {
      def metaProperty = JPopupMenu.metaClass.getMetaProperty(name)
      if(metaProperty) metaProperty.setProperty(delegate, value)
      else {
         metaProperty = PopupMenu.metaClass.getMetaProperty(name)
         if(metaProperty) metaProperty.setProperty(this, value)
         else throw new MissingPropertyException(name,value?.class ?: Object)
      }
   }
}