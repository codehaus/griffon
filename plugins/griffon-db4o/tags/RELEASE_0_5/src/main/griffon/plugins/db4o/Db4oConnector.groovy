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
package griffon.plugins.db4o

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.util.Environment
import griffon.util.CallableWithArgs

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import com.db4o.*
import com.db4o.config.EmbeddedConfiguration

/**
 * @author Andres Almiray
 */
@Singleton
final class Db4oConnector {
    private bootstrap

    private static final Log LOG = LogFactory.getLog(Db4oConnector)
    
    static void enhance(MetaClass mc) {
        mc.withDb4o = {Closure closure ->
            ObjectContainerHolder.instance.withDb4o('default', closure)   
        }
        mc.withDb4o << {String datasourceName, Closure closure ->
            ObjectContainerHolder.instance.withDb4o(datasourceName, closure)   
        }
        mc.withDb4o << {CallableWithArgs callable ->
            ObjectContainerHolder.instance.withDb4o('default', callable)   
        }
        mc.withDb4o << {String datasourceName, CallableWithArgs callable ->
            ObjectContainerHolder.instance.withDb4o(datasourceName, callable)   
        }       
    }

    Object withDb4o(String dataSourceName = 'default', Closure closure) {
        ObjectContainerHolder.instance.withDb4o(datasourceName, closure) 
    }

    Object withDb4o(String dataSourceName = 'default', CallableWithArgs callable) {
        ObjectContainerHolder.instance.withDb4o(datasourceName, callable)
    }

    // ======================================================
  
    ConfigObject createConfig(GriffonApplication app) {
        def db4oConfigClass = app.class.classLoader.loadClass('Db4oConfig')
        return new ConfigSlurper(Environment.current.name).parse(db4oConfigClass)
    }   
    
    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {   
        return dataSourceName == 'default' ? config.dataSource : config.dataSources[dataSourceName]
    }

    ObjectContainer connect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if(ObjectContainerHolder.instance.isObjectContainerConnected(dataSourceName)) {
            return ObjectContainerHolder.instance.getObjectContainer(dataSourceName)
        }
        
        config = narrowConfig(config, dataSourceName)
        app.event('Db4oConnectStart', [config, dataSourceName])
        ObjectContainer oc = startObjectContainer(app, config, dataSourceName)
        ObjectContainerHolder.instance.setObjectContainer(dataSourceName, oc)
        bootstrap = app.class.classLoader.loadClass('BootstrapDb4o').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(dataSourceName, oc)
        app.event('Db4oConnectEnd', [dataSourceName, oc])
        oc
    }

    void disconnect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if(ObjectContainerHolder.instance.isObjectContainerConnected(dataSourceName)) {
            config = narrowConfig(config, dataSourceName)
            ObjectContainer oc = ObjectContainerHolder.instance.getObjectContainer(dataSourceName)
            app.event('Db4oDisconnectStart', [config, dataSourceName, oc])
            bootstrap.destroy(dataSourceName, oc)
            stopObjectContainer(config, dataSourceName)
            app.event('Db4oDisconnectEnd', [config, dataSourceName])
        }
    }

    private ObjectContainer startObjectContainer(GriffonApplication app, ConfigObject config, String dataSourceName) {
        String dbfileName = config.name ?: 'db.yarv'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(Metadata.current.getGriffonWorkingDir(), dbfileName)
        dbfile.parentFile.mkdirs()
        Script configScript = app.class.classLoader.loadClass('Db4oConfig').newInstance()
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration()
        configScript.configure(configuration)
        return Db4oEmbedded.openFile(configuration, dbfile.absolutePath)
    }

    private void stopObjectContainer(ConfigObject config, String dataSourceName) {
        ObjectContainer oc = ObjectContainerHolder.instance.getObjectContainer(dataSourceName)
        oc.close()
        ObjectContainerHolder.instance.disconnectObjectContainer(dataSourceName)

        if(config.delete) {
            String dbfileName = config.name ?: 'db.yarv'
            File dbfile = new File(dbfileName)
            if(!dbfile.absolute) dbfile = new File(Metadata.current.getGriffonWorkingDir(), dbfileName)
            dbfile.delete()
        }
    }
}
