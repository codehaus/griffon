/*
 * Copyright 2011 the original author or authors.
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
 * limitations under the License.
 */
package griffon.plugins.ebean

import javax.sql.DataSource

import com.avaje.ebean.EbeanServer
import com.avaje.ebean.EbeanServerFactory
import com.avaje.ebean.config.ServerConfig

import griffon.core.GriffonApplication
import griffon.util.RunnableWithArgs
import griffon.plugins.datasource.DataSourceHolder
import griffon.plugins.datasource.DataSourceConnector

/**
 * @author Andres Almiray
 */
@Singleton
final class EbeanConnector {
    private bootstrap

    static void enhance(MetaClass mc) {
        mc.withEbean = {Closure closure ->
            EbeanServerHolder.instance.withEbean('default', closure)
        }
        mc.withEbean << {String ebeanServerName, Closure closure ->
            EbeanServerHolder.instance.withEbean(ebeanServerName, closure)
        }
        mc.withEbean << {RunnableWithArgs runnable ->
            EbeanServerHolder.instance.withEbean('default', runnable)
        }
        mc.withEbean << {String ebeanServerName, RunnableWithArgs runnable ->
            EbeanServerHolder.instance.withEbean(ebeanServerName, runnable)
        }
    }

    void withEbean(String ebeanServerName = 'default', Closure closure) {
        EbeanServerHolder.instance.withEbean(ebeanServerName, closure)
    }

    void withEbean(String ebeanServerName = 'default', RunnableWithArgs runnable) {
        EbeanServerHolder.instance.withEbean(ebeanServerName, runnable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('EbeanConfig')
        new ConfigSlurper(griffon.util.Environment.current.name).parse(configClass) 
    }   
    
    private ConfigObject narrowConfig(ConfigObject config, String ebeanServerName) {   
        return ebeanServerName == 'default' ? config.ebeanServer : config.ebeanServers[ebeanServerName]
    }

    EbeanServer connect(GriffonApplication app, String ebeanServerName = 'default') {
        if(EbeanServerHolder.instance.isEbeanServerAvailable(ebeanServerName)) {
            return EbeanServerHolder.instance.getEbeanServer(ebeanServerName)
        }
        
        ConfigObject dsConfig = DataSourceConnector.instance.createConfig(app)
        if(ebeanServerName == 'default') {
            dsConfig.dataSource.schema.skip = true
        } else {
            dsConfig.dataSources."$ebeanServerName".schema.skip = true
        }
        DataSource dataSource = DataSourceConnector.instance.connect(app, dsConfig, ebeanServerName)

        ConfigObject config = narrowConfig(createConfig(app), ebeanServerName)
        app.event('EbeanConnectStart', [config, ebeanServerName])
        EbeanServer ebeanServer = createEbeanServer(config, dsConfig, ebeanServerName)
        EbeanServerHolder.instance.setEbeanServer(ebeanServerName, ebeanServer)
        bootstrap = app.class.classLoader.loadClass('BootstrapEbean').newInstance()
        bootstrap.metaClass.app = app
        EbeanServerHolder.instance.withEbean(ebeanServerName) { ebsName, ebs -> bootstrap.init(ebsName, ebs) }
        app.event('EbeanConnectEnd', [ebeanServerName, ebeanServer])
        ebeanServer
    }

    void disconnect(GriffonApplication app, String ebeanServerName = 'default') {
        if(!EbeanServerHolder.instance.isEbeanServerAvailable(ebeanServerName)) return
        
        EbeanServer ebeanServer = EbeanServerHolder.instance.getEbeanServer(ebeanServerName)
        app.event('EbeanDisconnectStart', [ebeanServerName, ebeanServer])
        EbeanServerHolder.instance.withEbean(ebeanServerName) { ebsName, ebs -> bootstrap.destroy(ebsName, ebs) }
        EbeanServerHolder.instance.disconnectEbeanServer(ebeanServerName)
        app.event('EbeanDisconnectEnd', [ebeanServerName])
        ConfigObject config = DataSourceConnector.instance.createConfig(app)
        DataSourceConnector.instance.disconnect(app, config, ebeanServerName)
    }

    private EbeanServer createEbeanServer(ConfigObject config, ConfigObject dsConfig, String ebeanServerName) {
        DataSource dataSource = DataSourceHolder.instance.getDataSource(ebeanServerName)
        boolean dbCreate = false
        if(ebeanServerName == 'default') {
            dbCreate = dsConfig.dataSource.dbCreate.toString() == 'create'
        } else {
            dbCreate = dsConfig.dataSources."$ebeanServerName".dbCreate.toString() == 'create'
        }

        ServerConfig serverConfig = new ServerConfig(
            name: ebeanServerName,
            register: true,
            defaultServer: ebeanServerName == 'default',
            ddlGenerate: dbCreate,
            ddlRun: dbCreate,
            dataSource: dataSource
        )
        config.each { propName, propValue ->
            serverConfig[propName] = propValue
        }

        EbeanServerFactory.create(serverConfig)
    }
}
