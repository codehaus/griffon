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

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.util.Environment
import griffon.util.CallableWithArgs

import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.Node

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class Neo4jConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jConnector)

    static void enhance(MetaClass mc) {
        mc.withNeo4j = {Closure closure ->
            DatabaseHolder.instance.withNeo4j('default', closure)
        }
        mc.withNeo4j << {String databaseName, Closure closure ->
            DatabaseHolder.instance.withNeo4j(databaseName, closure)
        }
        mc.withNeo4j << {CallableWithArgs callable ->
            DatabaseHolder.instance.withNeo4j('default', callable)
        }
        mc.withNeo4j << {String databaseName, CallableWithArgs callable ->
            DatabaseHolder.instance.withNeo4j(databaseName, callable)
        }
    }

    Object withNeo4j(String databaseName = 'default', Closure closure) {
        return DatabaseHolder.instance.withNeo4j(databaseName, closure)
    }

    public <T> T withNeo4j(String databaseName = 'default', CallableWithArgs<T> callable) {
        return DatabaseHolder.instance.withNeo4j(databaseName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def databaseClass = app.class.classLoader.loadClass('Neo4jConfig')
        new ConfigSlurper(Environment.current.name).parse(databaseClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String databaseName) {
        return databaseName == 'default' ? config.database : config.databases[databaseName]
    }

    GraphDatabaseService connect(GriffonApplication app, ConfigObject config, String databaseName = 'default') {
        if (DatabaseHolder.instance.isDatabaseConnected(databaseName)) {
            return DatabaseHolder.instance.getDatabase(databaseName)
        }

        config = narrowConfig(config, databaseName)
        app.event('Neo4jConnectStart', [config, databaseName])
        GraphDatabaseService database = startNeo4j(config)
        DatabaseHolder.instance.setDatabase(databaseName, database)
        bootstrap = app.class.classLoader.loadClass('BootstrapNeo4j').newInstance()
        bootstrap.metaClass.app = app
        withNeo4j(databaseName) { dsName, db, tx -> bootstrap.init(dsName, db, tx) }
        app.event('Neo4jConnectEnd', [databaseName, database])
        database
    }

    void disconnect(GriffonApplication app, ConfigObject config, String databaseName = 'default') {
        if (DatabaseHolder.instance.isDatabaseConnected(databaseName)) {
            config = narrowConfig(config, databaseName)
            GraphDatabaseService database = DatabaseHolder.instance.getDatabase(databaseName)
            app.event('DatabaseDisconnectStart', [config, databaseName, database])
            withNeo4j(databaseName) { dsName, db, tx -> bootstrap.destroy(dsName, db, tx) }
            stopNeo4j(config, database)
            app.event('DatabaseDisconnectEnd', [config, databaseName])
            DatabaseHolder.instance.disconnectDatabase(databaseName)
        }
    }

    private GraphDatabaseService startNeo4j(ConfigObject config) {
        String storeDirName = config.storeDir ?: 'neo4j/db'
        File storeDir = new File(storeDirName)
        if(!storeDir.absolute) storeDir = new File(Metadata.current.getGriffonWorkingDir(), storeDirName)
        storeDir.mkdirs()
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                Runtime.getRuntime().addShutdownHook {
                    storeDir?.eachFileRecurse { f -> 
                        try { if(f?.exists()) f.delete() }
                        catch(IOException ioe) { /* ignore */ }
                    }
                }
        }

        new EmbeddedGraphDatabase(storeDir.absolutePath, config.params ?: [:])    
    }

    private void stopNeo4j(ConfigObject config, GraphDatabaseService db) {
        db.shutdown()
    }
}