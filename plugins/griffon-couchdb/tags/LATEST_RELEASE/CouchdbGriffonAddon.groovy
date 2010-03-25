/*
 * Copyright 2010 the original author or authors.
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

import griffon.core.GriffonApplication
import griffon.util.Environment
import griffon.couchdb.CouchdbHelper
import griffon.couchdb.DatabaseHolder

/**
 * @author Andres Almiray
 */
class CouchdbGriffonAddon {
    private bootstrap

    def events = [
        BootstrapEnd: { app -> startCouchdb(app) },
        ShutdownStart: { app -> stopCouchdb(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.couchdb?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withCouchdb = CouchdbHelper.instance.withCouchdb
        }
    ]

    private void startCouchdb(GriffonApplication app) {
        def dbConfig = CouchdbHelper.instance.parseConfig(app)
        CouchdbHelper.instance.startCouchdb(dbConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapCouchdb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(DatabaseHolder.instance.db)
    }

    private void stopCouchdb(GriffonApplication app) {
        bootstrap.destroy(DatabaseHolder.instance.db)
    }
}
