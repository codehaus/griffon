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
package griffon.plugins.carbonado

import groovy.sql.Sql
import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.pool.ObjectPool
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.ConnectionFactory
import org.apache.commons.dbcp.PoolingDataSource
import org.apache.commons.dbcp.PoolableConnectionFactory
import org.apache.commons.dbcp.DriverManagerConnectionFactory

import com.amazon.carbonado.Repository
import com.amazon.carbonado.repo.jdbc.JDBCRepositoryBuilder
import com.amazon.carbonado.repo.jdbc.JDBCConnectionCapability
import com.amazon.carbonado.repo.map.MapRepositoryBuilder
import com.amazon.carbonado.repo.sleepycat.BDBRepositoryBuilder

import griffon.core.GriffonApplication
import griffon.util.Environment

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres.Almiray
 */
@Singleton
final class CarbonadoConnector {
    private static final Log LOG = LogFactory.getLog(CarbonadoConnector)
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap
    private GriffonApplication app
    private DataSource dataSource

    ConfigObject createConfig(GriffonApplication app) {
        def carbonadoClass = app.class.classLoader.loadClass('CarbonadoConfig')
        return new ConfigSlurper(Environment.current.name).parse(carbonadoClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        
        app.event('CarbonadoConnectStart', [config])
        switch(app.config.carbonado.repository) {
            case 'jdbc':
                createJDBCRepository(app, config)
                break
            case 'bdb':
                createBDBRepository(app, config)
                break
            case 'map':
            default:
                createMapRepository(app, config)
        }
        
        bootstrap = app.class.classLoader.loadClass('BootstrapCarbonado').newInstance()
        bootstrap.metaClass.app = app
        RepositoryHolder.instance.withCarbonado { repository -> bootstrap.init(repository) }

        app.event('CarbonadoConnectEnd', [RepositoryHolder.instance.repository])
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        app.event('CarbonadoDisconnectStart', [config, RepositoryHolder.instance.repository])
        bootstrap.destroy(RepositoryHolder.instance.repository)       
        switch(app.config.carbonado.repository) {
            case 'jdbc':
                disconnectJDBCRepository(app, config)
                break
            /*    
            case 'bdb':
                disconnectBDBRepository(app, config)
                break
            case 'map':
            default:
                disconnectMapRepository(app, config)
            */
        }
        app.event('CarbonadoDisconnectEnd')
    }
    
    private void disconnectJDBCRepository(GriffonApplication app, ConfigObject config) {
        Connection connection = null
        try {
            connection = dataSource.getConnection()
            if(connection.metaData.databaseProductName == 'HSQL Database Engine') {
                connection.createStatement().executeUpdate('SHUTDOWN')
            }
        } finally {
            connection?.close()
        }
    }

    /*
    private void disconnectBDBRepository(GriffonApplication app, ConfigObject config) { }

    private void disconnectMapRepository(GriffonApplication app, ConfigObject config) { }
    */
    
    private void createJDBCRepository(GriffonApplication app, ConfigObject config) {   
        dataSource = createDataSource(config)
        def skipSchema = app.config.carbonado?.schema?.skip ?: false
        if(!skipSchema) createSchema(config)
        
        JDBCRepositoryBuilder builder = new JDBCRepositoryBuilder()
        builder.name = config.dataSource.name
        builder.dataSource = dataSource
        RepositoryHolder.instance.repository = builder.build()
    }

    private void createBDBRepository(GriffonApplication app, ConfigObject config) {   
        BDBRepositoryBuilder builder = new BDBRepositoryBuilder()
        config.berkeleydb.each { propName, propValue ->
            builder[propName] = propValue
        }
        RepositoryHolder.instance.repository = builder.build()
    }

    private void createMapRepository(GriffonApplication app, ConfigObject config) {   
        MapRepositoryBuilder builder = new MapRepositoryBuilder()
        config.map.each { propName, propValue ->
            builder[propName] = propValue
        }
        RepositoryHolder.instance.repository = builder.build()
    }

    private DataSource createDataSource(ConfigObject config) {
        if(dataSource) return
        
        Class.forName(config.dataSource.driverClassName.toString())
        ObjectPool connectionPool = new GenericObjectPool(null)
        if(config.dataSource.pooled) {
            if(config?.pool?.maxWait   != null) connectionPool.maxWait = config.pool.maxWait
            if(config?.pool?.maxIdle   != null) connectionPool.maxIdle = config.pool.maxIdle
            if(config?.pool?.maxActive != null) connectionPool.maxActive = config.pool.maxActive
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
        return new PoolingDataSource(connectionPool)
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

    private withSql = { Closure closure ->
        Connection connection = dataSource.getConnection()
        try {
            closure(new Sql(connection))
        } finally {
            connection.close()
        }
    } 
}
