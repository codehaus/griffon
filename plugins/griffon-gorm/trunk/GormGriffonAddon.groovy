import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.pool.ObjectPool
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.ConnectionFactory
import org.apache.commons.dbcp.PoolingDataSource
import org.apache.commons.dbcp.PoolableConnectionFactory
import org.apache.commons.dbcp.DriverManagerConnectionFactory

import griffon.util.IGriffonApplication
import griffon.spring.factory.support.ObjectFactoryBean
import griffon.spring.artifact.SpringDomainArtifactHandler
import org.springframework.context.support.StaticMessageSource

class GormGriffonAddon {
    private static final String ENVIRONMENT = "griffon.env"
    private static final String ENVIRONMENT_DEV = "dev"
    private static final String ENVIRONMENT_PROD = "prod"
    private static final String ENVIRONMENT_DEV_LONG = "development"
    private static final String ENVIRONMENT_PROD_LONG = "production"
   
    private IGriffonApplication app
    private bootstrap

    def addonInit(app) {
        this.app = app
        app.event("SpringAddon", [this])
        app.artifactManager.registerArtifactHandler(new SpringDomainArtifactHandler())
    }

    def doWithSpring = {
        xmlns gorm:'http://grails.org/schema/gorm'

        dataSource(ObjectFactoryBean) { bean ->
            bean.scope = "singleton"
            bean.autowire = "byName"
            object = createDataSource(parseConfig())
            objectClass = DataSource
        }
        messageSource(StaticMessageSource)
        gorm.sessionFactory('data-source-ref': 'dataSource',
                            'base-package': 'domain',
                            'message-source-ref': 'messageSource') {
            // TODO read HBM properties from config file
            hibernateProperties = [
                'hibernate.show_sql': 'true',
                'hibernate.hbm2ddl.auto': 'update'
            ]
        }
    }

    def whenSpringReady = { app ->
        // called after all Spring related addons have contributed their beans via doWithSpring()
        // this is a great place to tweak classes via meta-programming
        // this closure is roughly equivalent to doWithDynamicMethods() in Grails
        bootstrap = this.class.classLoader.loadClass("BootstrapGorm").newInstance()
        bootstrap.init(app)
    }

    def events = [
        "ShutdownStart": { app ->
            bootstrap.destroy(app)
        }
    ]

    // ==============================================

    private parseConfig() {
        String env = System.getProperty(ENVIRONMENT, ENVIRONMENT_PROD)
        if(env == ENVIRONMENT_DEV) env = ENVIRONMENT_DEV_LONG
        if(env == ENVIRONMENT_PROD) env = ENVIRONMENT_PROD_LONG
        def dataSourceClass = this.class.classLoader.loadClass("DataSource")
        return new ConfigSlurper(env).parse(dataSourceClass)
    }
 
    private DataSource createDataSource(config) {
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
}
