/*
 * Copyright 2009-2010 the original author or authors.
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
package griffon.gsql

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
 * @author Andres.Almiray
 */
@Singleton
final class GsqlConnector {
    private static final Log LOG = LogFactory.getLog(GsqlConnector)
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap

    ConfigObject createConfig(GriffonApplication app) {
        def dataSourceClass = app.class.classLoader.loadClass('DataSource')
        return new ConfigSlurper(Environment.current.name).parse(dataSourceClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }
        createDataSource(config)
        def skipSchema = app.config?.griffon?.gsql?.schema?.skip ?: false
        if(!skipSchema) createSchema(config)

        bootstrap = app.class.classLoader.loadClass('BootstrapGsql').newInstance()
        bootstrap.metaClass.app = app
        withSql { sql -> bootstrap.init(sql) }
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        Connection connection = null
        try {
            connection = DataSourceHolder.instance.dataSource.getConnection()
            bootstrap.destroy(new Sql(connection))
            if(connection.metaData.databaseProductName == 'HSQL Database Engine') {
                connection.createStatement().executeUpdate('SHUTDOWN')
            }
        } finally {
            connection?.close()
        }
    }

    private void createDataSource(ConfigObject config) {
        if(DataSourceHolder.instance.dataSource) return
        
        Class.forName(config.dataSource.driverClassName.toString())
        ObjectPool connectionPool = new GenericObjectPool(null)
        if(config.dataSource.pooled) {
            if(config?.pool?.maxWait != null)  connectionPool.maxWait = config.pool.maxWait
            if(config?.pool?.maxIdle != null)  connectionPool.maxIdle = config.pool.maxIdle
            if(config?.pool?.maxActive != null)  connectionPool.maxActive = config.pool.maxActive
        }
 
        String url = config.dataSource.url.toString()
        String username = config.dataSource.username.toString()
        String password = config.dataSource.password.toString()
        ConnectionFactory connectionFactory = null
        if(username) {
            connectionFactory = new DriverManagerConnectionFactory(url, username, password)
        } else {
            connectionFactory = new DriverManagerConnectionFactory(url, null)
        }
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true)
        DataSourceHolder.instance.dataSource = new PoolingDataSource(connectionPool)
    }

    private void createSchema(ConfigObject config) {
        def dbCreate = config.dataSource?.dbCreate.toString()
        if(dbCreate != 'create') return
 
        URL ddl = getClass().classLoader.getResource('schema.ddl')
        if(!ddl) {
            LOG.warn("DataSource.dbCreate was set to 'create' but schema.ddl was not found in classpath.") 
            return
        }
 
        boolean tokenizeddl = config.dataSource?.tokenizeddl ?: false
        withSql { sql -> 
            if(!tokenizeddl) {
                sql.execute(ddl.text)
            } else {
                ddl.text.split(';').each { stmnt ->
                    if(stmnt?.trim()) sql.execute(stmnt + ';')
                }
            }
        }
    }

    def withSql = DataSourceHolder.instance.withSql
}
