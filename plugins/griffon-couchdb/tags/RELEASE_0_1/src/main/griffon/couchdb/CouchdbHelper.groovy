/*
 * Copyright 2010 the original author or authors.
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
package griffon.couchdb

import griffon.core.GriffonApplication
import griffon.util.Environment

import org.apache.commons.lang.StringUtils
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.codehaus.griffon.couchdb.json.JsonConverterUtils
import org.codehaus.griffon.couchdb.json.JsonDateConverter
import org.jcouchdb.db.Database
import org.jcouchdb.db.Options
import org.jcouchdb.document.Attachment
import org.jcouchdb.document.DesignDocument
import org.jcouchdb.document.ValueRow
import org.jcouchdb.document.ViewResult
import org.jcouchdb.exception.NotFoundException
import org.jcouchdb.util.CouchDBUpdater
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
final class CouchdbHelper {
    def parseConfig(GriffonApplication app) {
        def couchdbConfigClass = app.class.classLoader.loadClass("CouchdbConfig")
        return new ConfigSlurper(Environment.current.name).parse(couchdbConfigClass)
    }

    void startCouchdb(dbConfig) {
        def ds = dbConfig.couchdb

        String host = ds?.host ?: "localhost"
        Integer port = ds?.port ?: 5984
        String database = ds?.database ?: app.metadata["app.name"]
        String username = ds?.username ?: ""
        String password = ds?.password ?: ""

        String realm = ds?.realm ?: null
        String scheme = ds?.scheme ?: null

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
        generator.setIgnoredProperties(Arrays.asList("metaClass"))
        generator.setTypeConverterRepository(typeConverterRepository)
        generator.registerTypeConversion(java.util.Date, dateConverter)
        generator.registerTypeConversion(java.sql.Date, dateConverter)
        generator.registerTypeConversion(java.sql.Timestamp, dateConverter)

        JSONParser parser = new JSONParser()
        parser.setTypeConverterRepository(typeConverterRepository)
        parser.registerTypeConversion(java.util.Date, dateConverter)
        parser.registerTypeConversion(java.sql.Date, dateConverter)
        parser.registerTypeConversion(java.sql.Timestamp, dateConverter)

        db.jsonConfig = new JSONConfig(generator, parser)
        DatabaseHolder.instance.db = db

        // setup views
        ClasspathCouchDBUpdater updater = new ClasspathCouchDBUpdater()
        updater.setDatabase(db)
        updater.updateDesignDocuments()
    }

    def withCouchdb = { Closure closure ->
        closure(DatabaseHolder.instance.db)
    }
}
