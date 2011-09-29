/*
 * Copyright 2010-2011 the original author or authors.
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

package griffon.plugins.couchdb

import org.jcouchdb.db.Database
import griffon.util.RunnableWithArgs

import griffon.util.ApplicationHolder
import static griffon.util.GriffonNameUtils.isBlank

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
class DatabaseHolder {
    private static final Log LOG = LogFactory.getLog(DatabaseHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, Database> databases = [:]

    Object withCouchdb(Closure closure) {
        Database db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing Couchdb staments on datasource '$databaseName'")
        return closure(db)
    }
    
    void withCouchdb(RunnableWithArgs runnable) {
        Database db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing Couchdb staments on datasource '$databaseName'")
        runnable.setArgs(db)
        runnable.run()
    }

    String[] getDatabaseNames() {
        List<String> databaseNames = new ArrayList().addAll(databases.keySet())
        databaseNames.toArray(new String[databaseNames.size()])
    }

    Database getDatabase(String databaseName = 'default') {
        if(isBlank(databaseName)) databaseName = 'default'
        retrieveDatabase(databaseName)
    }

    void setDatabase(String databaseName = 'default', Database ds) {
        if(isBlank(databaseName)) databaseName = 'default'
        storeDatabase(databaseName, ds)       
    }
    
    boolean isDatabaseConnected(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        retrieveDatabase(databaseName) != null
    }
    
    void disconnectDatabase(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        storeDatabase(databaseName, null)        
    }

    private Database fetchDatabase(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        Database db = retrieveDatabase(databaseName)
        if(db == null) {
            db = CouchdbConnector.instance.connect(ApplicationHolder.application, databaseName)
        }
        
        if(db == null) {
            throw new IllegalArgumentException("No such couchdb database configuration for name $databaseName")
        }
        db
    }

    private Database retrieveDatabase(String databaseName) {
        synchronized(LOCK) {
            databases[databaseName]
        }
    }

    private void storeDatabase(String databaseName, Database db) {
        synchronized(LOCK) {
            databases[databaseName] = db
        }
    }
}
