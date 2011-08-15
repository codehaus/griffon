/*
 * Copyright 2009-2011 the original author or authors.
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
package griffon.plugins.datasource

import groovy.sql.Sql

import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.pool.ObjectPool
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.ConnectionFactory
import org.apache.commons.dbcp.PoolingDataSource
import org.apache.commons.dbcp.PoolableConnectionFactory
import org.apache.commons.dbcp.DriverManagerConnectionFactory

import griffon.core.GriffonApplication
import griffon.util.Environment

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class DataSourceConnector {
    private static final Log LOG = LogFactory.getLog(DataSourceConnector)

    ConfigObject createConfig(GriffonApplication app) {
        def dataSourceClass = app.class.classLoader.loadClass('DataSource')
        new ConfigSlurper(Environment.current.name).parse(dataSourceClass) 
    }   
    
    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {   
        return dataSourceName == 'default' ? config.dataSource : config.dataSources[dataSourceName]
    }

    DataSource connect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if(DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) {
            return DataSourceHolder.instance.getDataSource(dataSourceName)
        }
        
        config = narrowConfig(config, dataSourceName)
        app.event('DataSourceConnectStart', [config, dataSourceName])
        DataSource ds = createDataSource(config, dataSourceName)
        DataSourceHolder.instance.setDataSource(dataSourceName, ds)
        def skipSchema = config.schema?.skip ?: false
        if(!skipSchema) createSchema(config, dataSourceName)
        app.event('DataSourceConnectEnd', [dataSourceName, ds])
        ds
    }

    void disconnect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if(DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) {
            config = narrowConfig(config, dataSourceName)
            app.event('DataSourceDisconnectStart', [config, dataSourceName, DataSourceHolder.instance.getDataSource(dataSourceName)])
            app.event('DataSourceDisconnectEnd', [config, dataSourceName])
            DataSourceHolder.instance.disconnectDataSource(dataSourceName)
        }
    }

    private DataSource createDataSource(ConfigObject config, String dataSourceName) {
        Class.forName(config.driverClassName.toString())
        ObjectPool connectionPool = new GenericObjectPool(null)
        if(config.pool) {
            if(config.pool.maxWait   != null)  connectionPool.maxWait   = config.pool.maxWait
            if(config.pool.maxIdle   != null)  connectionPool.maxIdle   = config.pool.maxIdle
            if(config.pool.maxActive != null)  connectionPool.maxActive = config.pool.maxActive
        }
 
        String url = config.url.toString()
        String username = config.username.toString()
        String password = config.password.toString()
        ConnectionFactory connectionFactory = null
        if(username) {
            connectionFactory = new DriverManagerConnectionFactory(url, username, password)
        } else {
            connectionFactory = new DriverManagerConnectionFactory(url, null)
        }
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true)
        new PoolingDataSource(connectionPool)
    }

    private void createSchema(ConfigObject config, String dataSourceName) {
        String dbCreate = config.dbCreate.toString()
        if(dbCreate != 'create') return
 
        URL ddl = getClass().classLoader.getResource(dataSourceName-'schema.ddl')
        if(!ddl) {
            LOG.warn("DataSource[${dataSourceName}].dbCreate was set to 'create' but ${dataSourceName}-schema.ddl was not found in classpath.") 
        }
        ddl = getClass().classLoader.getResource('schema.ddl')
        if(!ddl) {
            LOG.warn("DataSource[${dataSourceName}].dbCreate was set to 'create' but schema.ddl was not found in classpath.") 
            return
        }
 
        boolean tokenizeddl = config.tokenizeddl ?: false
        DataSourceHolder.instance.withSql(dataSourceName) { dsName, sql -> 
            if(!tokenizeddl) {
                sql.execute(ddl.text)
            } else {
                ddl.text.split(';').each { stmnt ->
                    if(stmnt?.trim()) sql.execute(stmnt + ';')
                }
            }
        }
    }
}