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
package griffon.plugins.cassandra

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
import griffon.util.CallableWithArgs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
class CassandraConnector {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraConnector)
    
    private bootstrap

    static void enhance(MetaClass mc) {
        mc.withCql = {Closure closure ->
            DataSourceHolder.instance.withCql('default', closure)
        }
        mc.withCql << {String datasourceName, Closure closure ->
            DataSourceHolder.instance.withCql(datasourceName, closure)
        }
        mc.withCql << {CallableWithArgs callable ->
            DataSourceHolder.instance.withCql('default', callable)
        }
        mc.withCql << {String datasourceName, CallableWithArgs callable ->
            DataSourceHolder.instance.withCql(datasourceName, callable)
        }
    }

    Object withCql(String dataSourceName = 'default', Closure closure) {
        DataSourceHolder.instance.withCql(datasourceName, closure)
    }

    Object withCql(String dataSourceName = 'default', CallableWithArgs callable) {
        DataSourceHolder.instance.withCql(datasourceName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def dataSourceClass = app.class.classLoader.loadClass('CassandraConfig')
        new ConfigSlurper(Environment.current.name).parse(dataSourceClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {
        return dataSourceName == 'default' ? config.dataSource : config.dataSources[dataSourceName]
    }

    DataSource connect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if (DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) {
            return DataSourceHolder.instance.getDataSource(dataSourceName)
        }

        config = narrowConfig(config, dataSourceName)
        app.event('CassandraConnectStart', [config, dataSourceName])
        DataSource ds = createDataSource(config, dataSourceName)
        DataSourceHolder.instance.setDataSource(dataSourceName, ds)
        def skipSchema = config.schema?.skip ?: false
        if (!skipSchema) createSchema(config, dataSourceName)
        bootstrap = app.class.classLoader.loadClass('BootstrapCassandra').newInstance()
        bootstrap.metaClass.app = app
        DataSourceHolder.instance.withCql(dataSourceName) { dsName, sql -> bootstrap.init(dsName, sql) }
        app.event('CassandraConnectEnd', [dataSourceName, ds])
        ds
    }

    void disconnect(GriffonApplication app, ConfigObject config, String dataSourceName = 'default') {
        if (DataSourceHolder.instance.isDataSourceConnected(dataSourceName)) {
            config = narrowConfig(config, dataSourceName)
            DataSource ds = DataSourceHolder.instance.getDataSource(dataSourceName)
            app.event('CassandraDisconnectStart', [config, dataSourceName, ds])
            DataSourceHolder.instance.withCql(dataSourceName) { dsName, sql -> bootstrap.destroy(dsName, sql) }
            app.event('CassandraDisconnectEnd', [config, dataSourceName])
            DataSourceHolder.instance.disconnectDataSource(dataSourceName)
        }
    }

    private DataSource createDataSource(ConfigObject config, String dataSourceName) {
        Class.forName(config.driverClassName.toString())
        ObjectPool connectionPool = new GenericObjectPool(null)
        if (config.pool) {
            if (config.pool.maxWait != null) connectionPool.maxWait = config.pool.maxWait
            if (config.pool.maxIdle != null) connectionPool.maxIdle = config.pool.maxIdle
            if (config.pool.maxActive != null) connectionPool.maxActive = config.pool.maxActive
        }

        String url = config.url.toString()
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, null)
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true)
        new PoolingDataSource(connectionPool)
    }

    private void createSchema(ConfigObject config, String dataSourceName) {
        String dbCreate = config.dbCreate.toString()
        if (dbCreate != 'create') return

        String env = getEnvironmentShortName()
        URL ddl = null
        for(String schemaName : [dataSourceName + '-schema-'+ env +'.ddl', dataSourceName + '-schema.ddl', 'schema-'+ env +'.ddl', 'schema.ddl']) {
            ddl = getClass().classLoader.getResource(schemaName)
            if (!ddl) {
                LOG.warn("DataSource[${dataSourceName}].dbCreate was set to 'create' but ${schemaName} was not found in classpath.")
            } else {
                break
            }
        }
        if(!ddl) {
            LOG.error("DataSource[${dataSourceName}].dbCreate was set to 'create' but no suitable schema was found in classpath.")
        }        

        DataSourceHolder.instance.withCql(dataSourceName) { dsName, sql ->
            ddl.text.split(';').each { stmnt ->
                if (stmnt?.trim()) sql.execute(stmnt.trim() + ';')
            }
        }
    }
    
    private String getEnvironmentShortName() {
        switch(Environment.current) {
            case Environment.DEVELOPMENT: return 'dev'
            case Environment.TEST: return 'test'
            case Environment.PRODUCTION: return 'prod'
            default: return Environment.current.name
        }
    }
}
