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

package griffon.builder.tray

import griffon.builder.tray.factory.*

import java.awt.SystemTray

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
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
      registerFactory("trayMenu", new PopupMenuFactory())
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