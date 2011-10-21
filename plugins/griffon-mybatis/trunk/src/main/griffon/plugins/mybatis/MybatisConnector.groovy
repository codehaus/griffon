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
package griffon.plugins.mybatis

import javax.sql.DataSource
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory

import griffon.core.GriffonApplication
import griffon.util.CallableWithArgs
import griffon.plugins.datasource.DataSourceHolder
import griffon.plugins.datasource.DataSourceConnector

/**
 * @author Andres Almiray
 */
@Singleton
final class MybatisConnector {
    private final Set<Class> mappers = [] as LinkedHashSet
    private bootstrap

    static void enhance(MetaClass mc) {
        mc.withSqlSession = {Closure closure ->
            SqlSessionFactoryHolder.instance.withSqlSession('default', closure)   
        }
        mc.withSqlSession << {String sessionFactoryName, Closure closure ->
            SqlSessionFactoryHolder.instance.withSqlSession(sessionFactoryName, closure)   
        }
        mc.withSqlSession << {CallableWithArgs callable ->
            SqlSessionFactoryHolder.instance.withSqlSession('default', callable)   
        }
        mc.withSqlSession << {String sessionFactoryName, CallableWithArgs callable ->
            SqlSessionFactoryHolder.instance.withSqlSession(sessionFactoryName, callable)   
        }       
    }

    Object withSqlSession(String sessionFactoryName = 'default', Closure closure) {
        SqlSessionFactoryHolder.instance.withSqlSession(sessionFactoryName, closure) 
    }

    Object withSqlSession(String sessionFactoryName = 'default', CallableWithArgs callable) {
        SqlSessionFactoryHolder.instance.withSqlSession(sessionFactoryName, callable) 
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('MybatisConfig')
        new ConfigSlurper(griffon.util.Environment.current.name).parse(configClass) 
    }   
    
    private ConfigObject narrowConfig(ConfigObject config, String dataSourceName) {   
        return dataSourceName == 'default' ? config.sessionFactory : config.sessionFactories[dataSourceName]
    }

    SqlSessionFactory connect(GriffonApplication app, String dataSourceName = 'default') {
        if(SqlSessionFactoryHolder.instance.isSqlSessionFactoryAvailable(dataSourceName)) {
            return SqlSessionFactoryHolder.instance.getSqlSessionFactory(dataSourceName)
        }
        
        ConfigObject config = DataSourceConnector.instance.createConfig(app)
        DataSource dataSource = DataSourceConnector.instance.connect(app, config, dataSourceName)

        config = narrowConfig(createConfig(app), dataSourceName)
        app.event('MybatisConnectStart', [config, dataSourceName])
        SqlSessionFactory sessionFactory = createSqlSessionFactory(config, dataSourceName)
        SqlSessionFactoryHolder.instance.setSqlSessionFactory(dataSourceName, sessionFactory)
        bootstrap = app.class.classLoader.loadClass('BootstrapMybatis').newInstance()
        bootstrap.metaClass.app = app
        SqlSessionFactoryHolder.instance.withSqlSession(dataSourceName) { dsName, sqlSession -> bootstrap.init(dsName, sqlSession) }
        app.event('MybatisConnectEnd', [dataSourceName, sessionFactory])
        sessionFactory
    }

    void disconnect(GriffonApplication app, String dataSourceName = 'default') {
        if(!SqlSessionFactoryHolder.instance.isSqlSessionFactoryAvailable(dataSourceName)) return
        
        SqlSessionFactory sessionFactory = SqlSessionFactoryHolder.instance.getSqlSessionFactory(dataSourceName)
        app.event('MybatisDisconnectStart', [dataSourceName, sessionFactory])
        SqlSessionFactoryHolder.instance.withSqlSession(dataSourceName) { dsName, sqlSession -> bootstrap.destroy(dsName, sqlSession) }
        SqlSessionFactoryHolder.instance.disconnectSqlSessionFactory(dataSourceName)
        app.event('MybatisDisconnectEnd', [dataSourceName])
        ConfigObject config = DataSourceConnector.instance.createConfig(app)
        DataSourceConnector.instance.disconnect(app, config, dataSourceName)
    }

    private SqlSessionFactory createSqlSessionFactory(ConfigObject config, String dataSourceName) {
        DataSource dataSource = DataSourceHolder.instance.getDataSource(dataSourceName)
        Environment environment = new Environment(dataSourceName, new JdbcTransactionFactory(), dataSource)
        Configuration configuration = new Configuration(environment)
        config.each { propName, propValue ->
            configuration[propName] = propValue
        }
        if(mappers.isEmpty()) {
            readMappers()
        }
        mappers.each { Class mapper -> configuration.addMapper(mapper) }
        
        new SqlSessionFactoryBuilder().build(configuration)
    }

    private void readMappers() {
        ClassLoader cl = Thread.currentThread().contextClassLoader
        Enumeration urls = cl.getResources('META-INF/mybatis/mappers.txt')
        urls.each { url ->
            url.eachLine { text ->
                text.trim().split(/,/).each { className -> 
                    mappers << cl.loadClass(className)
                }
            }
        }
    }
}
