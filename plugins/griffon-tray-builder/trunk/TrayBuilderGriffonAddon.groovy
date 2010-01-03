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

/**
 * @author Andres Almiray
 */

import griffon.builder.tray.factory.*
import griffon.builder.tray.impl.*

/**
 * @author Andres Almiray
 */
class TrayBuilderGriffonAddon {
    def factories = [:]
    def props = [:]

    TrayBuilderGriffonAddon() {
        try {
            def systemTrayClass = getClass().classLoader.loadClass("java.awt.SystemTray")
            if(systemTrayClass.isSupported()) {
                props.systemTray = [
                   get: { systemTrayClass.getSystemTray() },
                   set: { }
                ]
                factories.systemTray = new SystemTrayFactory()
                factories.trayIcon = new TrayIconFactory()
            } else {
                registerDummyObjects()
            }
        } catch(ClassNotFoundException ex) {
            registerDummyObjects()
        } 
    }

    private void registerDummyObjects() {
        def os = System.getProperty("os.name")
        os += " "
        os += System.getProperty("os.arch") ?: " "
        os += System.getProperty("os.version") ?: ""
        // TODO use logging instead
        println("SystemTray is not supported on ${os.trim()} - JVM ${System.getProperty('java.version')}")
        props.systemTray = [
            get: { new DummyTrayObject() },
            set: { }
        ]
        factories.systemTray = new BeanFactory(DummyTrayObject)
        factories.trayIcon = new BeanFactory(DummyTrayObject)
    }
}
