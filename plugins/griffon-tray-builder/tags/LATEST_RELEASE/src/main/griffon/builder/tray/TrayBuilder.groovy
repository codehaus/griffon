/* --------------------------------------------------------------------
   TrayBuilder
   Copyright (C) 2008-2010 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
*/

package griffon.builder.tray

import griffon.builder.tray.factory.*

import java.awt.SystemTray

/**
 * @author Andres Almiray
 */
class TrayBuilder extends FactoryBuilderSupport {
   public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
   public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

   public TrayBuilder( boolean init = true ) {
      super( init )
      if( !SystemTray.isSupported() ) {
         def os = System.getProperty("os.name")
         os += " "
         os += System.getProperty("os.arch") ?: " "
         os += System.getProperty("os.version") ?: ""
         throw new IllegalStateException("SystemTray is not supported on ${os.trim()}")
      }
      this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
   }

   public void registerTray() {
      addAttributeDelegate(TrayBuilder.&objectIDAttributeDelegate)
      setVariable("systemTray", SystemTray.systemTray)
      registerFactory("systemTray", new SystemTrayFactory())
      registerFactory("trayIcon", new TrayIconFactory())
   }

   // taken from groovy.swing.SwingBuilder
   public static objectIDAttributeDelegate(def builder, def node, def attributes) {
      def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
      def theID = attributes.remove(idAttr)
      if (theID) {
          builder.setVariable(theID, node)
      }
   }
}
