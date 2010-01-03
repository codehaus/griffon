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

import java.sql.Connection
import javax.sql.DataSource
import java.sql.Driver
import java.sql.DriverManager
import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jndi.JndiObjectFactoryBean
import org.codehaus.groovy.grails.orm.support.TransactionManagerPostProcessor

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
// import org.apache.commons.pool.ObjectPool
// import org.apache.commons.pool.impl.GenericObjectPool
// import org.apache.commons.dbcp.ConnectionFactory
// import org.apache.commons.dbcp.PoolingDataSource
// import org.apache.commons.dbcp.PoolableConnectionFactory
// import org.apache.commons.dbcp.DriverManagerConnectionFactory
import org.springframework.context.support.StaticMessageSource
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor
import org.codehaus.groovy.grails.orm.hibernate.support.HibernateDialectDetectorFactoryBean
import org.codehaus.groovy.grails.orm.hibernate.support.SpringLobHandlerDetectorFactoryBean

import griffon.util.IGriffonApplication
import griffon.spring.factory.support.ObjectFactoryBean
import griffon.spring.artifact.SpringDomainArtifactHandler

/**
 * @author Andres Almiray
 */
class GormGriffonAddon {
    private static final String ENVIRONMENT = "griffon.env"
    private static final String ENVIRONMENT_DEV = "dev"
    private static final String ENVIRONMENT_PROD = "prod"
    private static final String ENVIRONMENT_DEV_LONG = "development"
    private static final String ENVIRONMENT_PROD_LONG = "production"

    static final Log log = LogFactory.getLog(GormGriffonAddon)

    static config = [:]
    static hibProps = [:]

    private IGriffonApplication app
    private bootstrap

    def addonInit(app) {
        this.app = app
        app.event("SpringAddon", [this])
        app.artifactManager.registerArtifactHandler(new SpringDomainArtifactHandler())
        config = parseConfig()
    }

    def doWithSpring = {
        if(!app.artifactManager.getArtifactsOfType(SpringDomainArtifactHandler.TYPE)) return
        xmlns gorm:'http://grails.org/schema/gorm'

        def ds = config.dataSource
        def hib = config.hibernate

        transactionManagerPostProcessor(TransactionManagerPostProcessor)

        if(ds.jndiName) {
            dataSource(JndiObjectFactoryBean) {
                jndiName = ds.jndiName
                expectedType = DataSource
            }
        } else {
            def dsproperties = {
                def driver = ds?.driverClassName ? ds.driverClassName : "org.hsqldb.jdbcDriver"
                driverClassName = driver
                url = ds?.url ? ds.url : "jdbc:hsqldb:mem:griffonDB"
                boolean defaultDriver = (driver == "org.hsqldb.jdbcDriver")
                String theUsername = ds?.username ?: (defaultDriver ? "sa" : null)
                if(theUsername!=null) username = theUsername
                if(ds?.password)  {
                    def thePassword = ds.password
//                     if(ds?.passwordEncryptionCodec) {
//                         def encryptionCodec = ds?.passwordEncryptionCodec
//                         if(encryptionCodec instanceof Class) {
//                             try {
//                                 password = encryptionCodec.decode(thePassword)
//                             } catch (Exception e) {
//                                 throw new IllegalArgumentException("Error decoding dataSource password with codec [$encryptionCodec]: ${e.message}", e)
//                             }
//                         } else {
//                             encryptionCodec = encryptionCodec.toString()
//                             def codecClass = application.codecClasses.find { it.name?.equalsIgnoreCase(encryptionCodec) || it.fullName == encryptionCodec}?.clazz
//                             try {
//                                 if(!codecClass) {
//                                     codecClass = Class.forName(encryptionCodec, true, application.classLoader)
//                                 }
//                                 if(codecClass) {
//                                     password = codecClass.decode(thePassword)
//                                 } else {
//                                     throw new IllegalArgumentException("Error decoding dataSource password. Codec class not found for name [$encryptionCodec]")
//                                 }
//                             } catch (ClassNotFoundException e) {
//                                 throw new IllegalArgumentException("Error decoding dataSource password. Codec class not found for name [$encryptionCodec]: ${e.message}", e)
//                             } catch(Exception e) {
//                                 throw new IllegalArgumentException("Error decoding dataSource password with codec [$encryptionCodec]: ${e.message}", e)
//                             }
//                         }
//                     } else {
                        password = ds.password
//                     }
                } else {
                    String thePassword = defaultDriver ? "" : null
                    if(thePassword!=null) password = thePassword
                }
            }

            if(ds && !app.applicationContext?.containsBean("dataSource")) {
                log.info("[RuntimeConfiguration] Configuring data source for environment: ${System.getProperty(ENVIRONMENT, ENVIRONMENT_PROD)}");
                def bean
                if(ds.pooled) {
                    bean = dataSource(BasicDataSource, dsproperties)
                    bean.destroyMethod = "close"
                } else {
                    bean = dataSource(DriverManagerDataSource, dsproperties)
                }
                // support for setting custom properties (for example maxActive) on the dataSource bean
                def dataSourceProperties = ds.properties
                if(dataSourceProperties != null) {
                    if(dataSourceProperties instanceof Map) {
                        dataSourceProperties.each { entry ->
                            if(log.debugEnabled) {
                                log.debug("Setting property on dataSource bean ${entry.key} -> ${entry.value}")
                            }
                            bean.setPropertyValue(entry.key.toString(), entry.value)
                        }
                    } else {
                        log.warn("dataSource.properties is not an instanceof java.util.Map, ignoring")
                    }
                }
            } else if(!app.applicationContext?.containsBean("dataSource")) {
                def bean = dataSource(BasicDataSource, dsproperties)
                bean.destroyMethod = "close"
            }
        }

/*
        def dataSourceBean = createDataSource(ds)
        dataSource(ObjectFactoryBean) { bean ->
            bean.scope = "singleton"
            bean.autowire = "byName"
            object = dataSourceBean
            objectClass = DataSource
        }
*/

        messageSource(StaticMessageSource)

        def vendorToDialect = new Properties()
        def hibernateDialects = app.class.classLoader.getResource("hibernate-dialects.properties")
        if(hibernateDialects) {
            def p = new Properties()
            p.load(hibernateDialects.openStream())
            p.each { entry ->
                vendorToDialect[entry.value] = "org.hibernate.dialect.${entry.key}".toString()
            }
        }

        if(ds && ds.loggingSql || ds && ds.logSql) {
            hibProps."hibernate.show_sql" = "true"
            hibProps."hibernate.format_sql" = "true"
        }
        if(ds && ds.dialect) {
            if(ds.dialect instanceof Class)
                hibProps."hibernate.dialect" = ds.dialect.name
            else
                hibProps."hibernate.dialect" = ds.dialect.toString()
        } else {
            dialectDetector(HibernateDialectDetectorFactoryBean) {
                dataSource = ref("dataSource")
                vendorNameDialectMappings = vendorToDialect
            }
            hibProps."hibernate.dialect" = dialectDetector
        }
        hibProps."hibernate.hbm2ddl.auto" = ds.dbCreate ?: 'create-drop'
        log.info "Set db generation strategy to '${hibProps.'hibernate.hbm2ddl.auto'}'"

        if(hib) {
            def cacheProvider = hib.cache.provider_class ?: 'net.sf.ehcache.hibernate.EhCacheProvider'
            if(cacheProvider.contains('OSCacheProvider')) {
                try {
                    def cacheClass = app.class.classLoader.loadClass(cacheProvider)
                } catch (Throwable t) {
                    hib.remove('cache')
                    log.error """WARNING: Your cache provider is set to '${cacheProvider}' in DataSource.groovy, however the classes for this provider cannot be found.
Try using Griffon's default cache provider: 'net.sf.ehcache.hibernate.EhCacheProvider'"""
                }
            }
            hibProps.putAll(hib.flatten().toProperties('hibernate'))
        }

/*
        hibernateProperties(PropertiesFactoryBean) { bean ->
            bean.scope = "prototype"
            properties = hibProps
        }
*/
        nativeJdbcExtractor(CommonsDbcpNativeJdbcExtractor)
        lobHandlerDetector(SpringLobHandlerDetectorFactoryBean) {
            dataSource = dataSource
            pooledConnection =  ds && ds.pooled ?: false
            nativeJdbcExtractor = ref("nativeJdbcExtractor")
        }

println hibProps
        gorm.sessionFactory('data-source-ref': 'dataSource',
                            'base-package': app.config?.domain?.package ?: 'domain',
                            'message-source-ref': 'messageSource') {
            hibernateProperties = hibProps
        }
    }

    def whenSpringReady = { app ->
        // called after all Spring related addons have contributed their beans via doWithSpring()
        // this is a great place to tweak classes via meta-programming
        // this closure is roughly equivalent to doWithDynamicMethods() in Grails
        bootstrap = app.class.classLoader.loadClass("BootstrapGorm").newInstance()
        bootstrap.init(app)
    }

    def events = [
        "ShutdownStart": { app ->
            bootstrap.destroy(app)
            if(app.applicationContext?.containsBean("dataSource")) {
                DataSource dataSource = appCtx.dataSource
                Connection connection
                try {
                    connection = dataSource.getConnection()
                    def dbName =connection.metaData.databaseProductName
                    if(dbName == 'HSQL Database Engine') {
                        connection.createStatement().executeUpdate('SHUTDOWN')
                    }
                } finally {
                    connection?.close()
                }
            }
        }
    ]

    // ==============================================

    private parseConfig() {
        String env = System.getProperty(ENVIRONMENT, ENVIRONMENT_PROD)
        if(env == ENVIRONMENT_DEV) env = ENVIRONMENT_DEV_LONG
        if(env == ENVIRONMENT_PROD) env = ENVIRONMENT_PROD_LONG
        def dataSourceClass = app.class.classLoader.loadClass("DataSource")
        return new ConfigSlurper(env).parse(dataSourceClass)
    }

/*
    private DataSource createDataSource() {
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
        def poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true)
        return new PoolingDataSource(connectionPool)
    }
*/
}
