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
import griffon.util.Environment
import griffon.db4o.Db4oHelper
import griffon.db4o.ObjectContainerHolder

/**
 * @author Andres Almiray
 */
class Db4oGriffonAddon {
    private bootstrap

    def events = [
        BootstrapEnd: { app -> start(app) },
        ShutdownStart: { app -> stop(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.db4o?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withDb4o = Db4oHelper.instance.withDb4o
        }
    ]

    private void start(GriffonApplication app) {
        def dbConfig = Db4oHelper.instance.parseConfig(app)
        Db4oHelper.instance.startObjectContainer(dbConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapDb4o').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(ObjectContainerHolder.instance.objectContainer)
    }

    private void stop(GriffonApplication app) {
        bootstrap.destroy(ObjectContainerHolder.instance.objectContainer)
        def dbConfig = Db4oHelper.instance.parseConfig(app)
        Db4oHelper.instance.stopObjectContainer(dbConfig)
    }
}
