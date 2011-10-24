/*
    griffon-neo4j plugin
    Copyright (C) 2010 Andres Almiray

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package griffon.plugins.neo4j

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction

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
class DatabaseHolder {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, GraphDatabaseService> databases = [:]
  
    String[] getDatabaseNames() {
        List<String> databaseNames = new ArrayList().addAll(databases.keySet())
        databaseNames.toArray(new String[databaseNames.size()])
    }

    GraphDatabaseService getDatabase(String databaseName = 'default') {
        if(isBlank(databaseName)) databaseName = 'default'
        retrieveDatabase(databaseName)
    }

    void setDatabase(String databaseName = 'default', GraphDatabaseService db) {
        if(isBlank(databaseName)) databaseName = 'default'
        storeDatabase(databaseName, db)       
    }

    Object withNeo4j(String databaseName = 'default', Closure closure) {
        GraphDatabaseService db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$databaseName'")
        Transaction tx = db.beginTx()
        Object result = null
        try {
            result = closure(databaseName, db, tx)
            tx.success()
        } catch(Exception e) {
            tx.failure()
        } finally {
            tx.finish()
        }
        
        return result
    }

    public <T> T withNeo4j(String databaseName = 'default', CallableWithArgs<T> callable) {
        GraphDatabaseService db = fetchDatabase(databaseName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on datasource '$databaseName'")
        Transaction tx = db.beginTx()
        T result = null
        try {
            callable.args = [databaseName, db, tx] as Object[]
            result = callable.call()
            tx.success()
        } catch(Exception e) {
            tx.failure()
        } finally {
            tx.finish()
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

    private GraphDatabaseService fetchDatabase(String databaseName) {
        if(isBlank(databaseName)) databaseName = 'default'
        GraphDatabaseService db = retrieveDatabase(databaseName)
        if(db == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = Neo4jConnector.instance.createConfig(app)
            db = Neo4jConnector.instance.connect(app, config, databaseName)
        }
        
        if(db == null) {
            throw new IllegalArgumentException("No such Database configuration for name $databaseName")
        }
        db
    }

    private GraphDatabaseService retrieveDatabase(String databaseName) {
        synchronized(LOCK) {
            databases[databaseName]
        }
    }

    private void storeDatabase(String databaseName, GraphDatabaseService db) {
        synchronized(LOCK) {
            databases[databaseName] = db
        }
    }
}
