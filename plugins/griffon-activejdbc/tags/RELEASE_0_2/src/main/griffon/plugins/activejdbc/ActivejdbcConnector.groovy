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
package griffon.plugins.activejdbc

import javax.sql.DataSource

import griffon.core.GriffonApplication
import griffon.plugins.datasource.DataSourceHolder
import griffon.plugins.datasource.DataSourceConnector

/**
 * @author Andres Almiray
 */
@Singleton
final class ActivejdbcConnector {
    private bootstrap

    void connect(GriffonApplication app, String dataSourceName = 'default') {
        DataSource dataSource = null
        if(!DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) {       
            ConfigObject config = DataSourceConnector.instance.createConfig(app)
            dataSource = DataSourceConnector.instance.connect(app, config, dataSourceName)
        } else {
            dataSource = DataSourceHolder.instance.getDataSource(dataSourceName)
        }

        app.event('ActivejdbcConnectStart', [dataSourceName, dataSource])
        bootstrap = app.class.classLoader.loadClass('BootstrapActivejdbc').newInstance()
        bootstrap.metaClass.app = app
        ActivejdbcHolder.instance.withActivejdbc(dataSourceName) { dsName -> bootstrap.init(dsName) }
        app.event('ActivejdbcConnectEnd', [dataSourceName, dataSource])
    }

    void disconnect(GriffonApplication app, String dataSourceName = 'default') {
        if(!DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) return
        
        DataSource dataSource = DataSourceHolder.instance.getDataSource(dataSourceName)
        app.event('ActivejdbcDisconnectStart', [dataSourceName, dataSource])
        ActivejdbcHolder.instance.withActivejdbc(dataSourceName) { dsName -> bootstrap.destroy(dsName) }
        app.event('ActivejdbcDisconnectEnd', [dataSourceName, dataSource])
        ConfigObject config = DataSourceConnector.instance.createConfig(app)
        DataSourceConnector.instance.disconnect(app, config, dataSourceName)
    }
}
