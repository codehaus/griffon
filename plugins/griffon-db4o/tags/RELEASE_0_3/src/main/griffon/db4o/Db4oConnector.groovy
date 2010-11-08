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
package griffon.db4o

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.util.Environment
import com.db4o.*
import com.db4o.config.EmbeddedConfiguration

/**
 * @author Andres Almiray
 */
@Singleton
final class Db4oConnector {
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap
    private GriffonApplication app

    ConfigObject parseConfig(GriffonApplication app) {
        def db4oConfigClass = app.class.classLoader.loadClass('Db4oConfig')
        return new ConfigSlurper(Environment.current.name).parse(db4oConfigClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        app.event('Db4oConnectStart', [config])
        startObjectContainer(config)
        bootstrap = app.class.classLoader.loadClass('BootstrapDb4o').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(ObjectContainerHolder.instance.objectContainer)
        app.event('Db4oConnectEnd', [ObjectContainerHolder.instance.objectContainer])
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        app.event('Db4oDisconnectStart', [config, ObjectContainerHolder.instance.objectContainer])
        bootstrap.destroy(ObjectContainerHolder.instance.objectContainer)
        stopObjectContainer(config)
        app.event('Db4oDisconnectEnd')
    }

    private void startObjectContainer(config) {
        String dbfileName = config?.dataSource?.name ?: 'db.yarv'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(Metadata.current.getGriffonWorkingDir(), dbfileName)
        dbfile.parentFile.mkdirs()
        Script configScript = app.class.classLoader.loadClass('Db4oConfig').newInstance()
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration()
        configScript.configure(configuration)
        ObjectContainerHolder.instance.objectContainer = Db4oEmbedded.openFile(configuration, dbfile.absolutePath)
    }

    private void stopObjectContainer(config) {
        ObjectContainerHolder.instance.objectContainer.close()

        String dbfileName = config?.dataSource?.name ?: 'db.yarv'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(Metadata.current.getGriffonWorkingDir(), dbfileName)
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                dbfile.delete()
        }
    }

    def withDb4o = { Closure closure ->
        closure(ObjectContainerHolder.instance.objectContainer)
    }
}
