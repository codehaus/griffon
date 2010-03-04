import com.mongodb.*
import com.mongodb.griffon.*
import com.google.inject.*
import com.google.inject.name.*
import org.codehaus.griffon.util.BuildSettings

public class MongoModule extends AbstractModule {
    static String name = "mongo.properties"
    
    @Override
    protected void configure() {        
        def props = loadProperties()
        Names.bindProperties(binder(), props)
        
    
        bind(Mongo.class).toProvider(MongoProvider.class).asEagerSingleton()
        bind(DB.class).toProvider(MongoDBProvider.class).asEagerSingleton()
        bind(DBCollection.class).toProvider(MongoDBCollectionProvider.class)
        bind(DBCollectionFactory.class).to(MongoDBCollectionFactory.class).asEagerSingleton()
    }
    
    private Properties loadProperties() {
        try {
            def config = getClass().classLoader.loadClass("MongoConfig").newInstance()
            def mongoConfig = new ConfigSlurper().parse(config)
            return mongoConfig.toProperties()
        } catch (ClassNotFoundException cnfe) {
            println "WARNING: No MongoConfig.groovy found for the application."
        }
    }
}
