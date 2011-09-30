/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package griffon.plugins.couchdb

import griffon.core.GriffonApplication
import griffon.util.Environment
import griffon.util.RunnableWithArgs

import org.apache.commons.lang.StringUtils
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.codehaus.griffon.couchdb.json.JsonConverterUtils
import org.codehaus.griffon.couchdb.json.JsonDateConverter
import org.jcouchdb.db.Database
import org.svenson.JSON
import org.svenson.JSONConfig
import org.svenson.JSONParser
import org.svenson.converter.DefaultTypeConverterRepository

/**
 * Contains code from grails-couchdb 
 *
 * @author Andres Almiray
 */
@Singleton
final class CouchdbConnector {
    private bootstrap

    static void enhance(MetaClass mc) {
        mc.withCouchdb = {String databaseName, Closure closure ->
            DatabaseHolder.instance.withCouchdb(databaseName, closure)
        }
        mc.withCouchdb << {String databaseName, RunnableWithArgs runnable ->
            DatabaseHolder.instance.withCouchdb(databaseName, runnable)
        }
    }

    ConfigObject createConfig(GriffonApplication app) {
        def couchconfigClass = app.class.classLoader.loadClass('CouchdbConfig')
        return new ConfigSlurper(Environment.current.name).parse(couchconfigClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String databaseName) {   
        return databaseName == 'default' ? config.database : config.databases[databaseName]
    }
    
    Database connect(GriffonApplication app, String databaseName = 'default') {
        if(DatabaseHolder.instance.isDatabaseConnected(databaseName)) {
            return DatabaseHolder.instance.getDatabase(databaseName)
        }
        
        ConfigObject config = createConfig(app)
        config = narrowConfig(config, databaseName)
        app.event('CouchdbConnectStart', [config, databaseName])
        Database db = createDatabase(app, config, databaseName)
        DatabaseHolder.instance.setDatabase(databaseName, db)
        bootstrap = app.class.classLoader.loadClass('BootstrapCouchdb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(db, databaseName)
        app.event('CouchdbConnectEnd', [databaseName, db])
        db
    }
    
    void disconnect(GriffonApplication app, String databaseName = 'default') {
        if(DatabaseHolder.instance.isDatabaseConnected(databaseName)) {
            ConfigObject config = narrowConfig(createConfig(app), databaseName)
            Database db = DatabaseHolder.instance.getDatabase(databaseName)
            app.event('CouchdbDisconnectStart', [config, databaseName, db])
            bootstrap.destroy(db, databaseName)
            app.event('CouchdbDisconnectStart', [config, databaseName])
            DatabaseHolder.instance.disconnectDatabase(databaseName)
        }
    }

    private Database createDatabase(GriffonApplication app, ConfigObject config, String databaseName) {
        String host = config?.host ?: 'localhost'
        Integer port = config?.port ?: 5984
        String database = config?.datastore ?: app.metadata['app.name']
        String username = config?.username ?: ''
        String password = config?.password ?: ''

        String realm = config?.realm ?: null
        String scheme = config?.scheme ?: null

        Database db = new Database(host, port, database)

        // check to see if there are any user credentials and set them
        if (StringUtils.isNotEmpty(username)) {
            def credentials = new UsernamePasswordCredentials(username, password)
            def authScope = new AuthScope(host, port)

            // set the realm and scheme if they are set
            if (StringUtils.isNotEmpty(realm) || StringUtils.isNotEmpty(scheme)) {
                authScope = new AuthScope(host, port, realm, scheme)
            }

            db.server.setCredentials(authScope, credentials)
        }

        DefaultTypeConverterRepository typeConverterRepository = new DefaultTypeConverterRepository()
        JsonDateConverter dateConverter = new JsonDateConverter()
        typeConverterRepository.addTypeConverter(dateConverter)

        JSON generator = new JSON()
        generator.setIgnoredProperties(Arrays.asList('metaClass'))
        generator.setTypeConverterRepository(typeConverterRepository)
        generator.registerTypeConversion(java.util.Date, dateConverter)
        generator.registerTypeConversion(java.sql.Date, dateConverter)
        generator.registerTypeConversion(java.sql.Timestamp, dateConverter)

        JSONParser parser = new JSONParser()
        parser.setTypeConverterRepository(typeConverterRepository)
        parser.registerTypeConversion(java.util.Date, dateConverter)
        parser.registerTypeConversion(java.sql.Date, dateConverter)
        parser.registerTypeConversion(java.sql.Timestamp, dateConverter)
        app.event('ConfigureCouchdbJSONParser', [config, databaseName, parser])

        db.jsonConfig = new JSONConfig(generator, parser)

        // setup views
        ClasspathCouchDBUpdater updater = new ClasspathCouchDBUpdater()
        updater.setDatabase(db)
        updater.updateDesignDocuments()

        db
    }
}
