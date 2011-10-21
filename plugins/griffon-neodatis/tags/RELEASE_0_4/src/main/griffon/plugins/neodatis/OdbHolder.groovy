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
package griffon.neodatis

import org.neodatis.odb.ODB

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
class OdbHolder {
    private static final Logger LOG = LoggerFactory.getLogger(OdbHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, ODB> databases = [:]
  
    String[] getDatabaseNames() {
        List<String> databaseNames = new ArrayList().addAll(databases.keySet())
        databaseNames.toArray(new String[databaseNames.size()])
    }

    ODB getDatabase(String databaseName = 'default') {
        if(isBlank(databaseName)) databaseName = 'default'
        retrieveDatabase(databaseName)
    }

    void setDatabase(String databaseName = 'default', ODB db) {
        if(isBlank(databaseName)) databaseName = 'default'
        storeDatabase(databaseName, db)       
    }

    Object withOdb(String databaseName = 'default', Closure closure) {
        ODB db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$databaseName'")
        Object result = null
        try { 
            result = closure(databaseName, db)
            db.commit()
        } catch(x) {
            db.rollback()
            throw x
        }
        return result
    }

    public <T> T withOdb(String databaseName = 'default', CallableWithArgs<T> callable) {
        ODB db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$databaseName'")
        T result = null
        try { 
            callable.args = [databaseName, db] as Object[]
            result = callable.run()
            db.commit()
        } catch(x) {
            db.rollback()
            throw x
        }
        return result
    }
    
    boolean isDatabaseConnected(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        retrieveDatabase(databaseName) != null
    }
    
    void disconnectDatabase(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        storeDatabase(databaseName, null)        
    }

    private ODB fetchDatabase(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        ODB db = retrieveDatabase(databaseName)
        if(db == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = NeodatisConnector.instance.createConfig(app)
            db = NeodatisConnector.instance.connect(app, config, databaseName)
        }
        
        if(db == null) {
            throw new IllegalArgumentException("No such Database configuration for name $databaseName")
        }
        db
    }

    private ODB retrieveDatabase(String databaseName) {
        synchronized(LOCK) {
            databases[databaseName]
        }
    }

    private void storeDatabase(String databaseName, ODB db) {
        synchronized(LOCK) {
            databases[databaseName] = db
        }
    }
}