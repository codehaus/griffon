/* --------------------------------------------------------------------
   griffon-db4o plugin
   Copyright (C) 2010 Andres Almiray

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

import griffon.core.GriffonApplication
import griffon.db4o.Db4oConnector

/**
 * @author Andres Almiray
 */
class Db4oGriffonAddon {
    def addonInit = { app ->
        ConfigObject config = Db4oConnector.instance.createConfig(app)
        Db4oConnector.instance.connect(app, config)
    }

    def events = [
        ShutdownStart: { app ->
            ConfigObject config = Db4oConnector.instance.createConfig(app)
            Db4oConnector.instance.disconnect(app, config)
        },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.gsql?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withDb4o = Db4oHelper.instance.withDb4o
        }
    ]
}
