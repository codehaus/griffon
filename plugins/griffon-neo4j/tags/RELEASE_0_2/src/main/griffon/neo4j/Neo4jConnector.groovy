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
package griffon.neo4j

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.util.Environment

import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.Node
import org.neo4j.index.IndexService
import org.neo4j.index.lucene.LuceneIndexService
import org.neo4j.index.lucene.LuceneFulltextIndexService
import org.neo4j.index.lucene.LuceneFulltextQueryIndexService

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import org.codehaus.groovy.runtime.StackTraceUtils

/**
 * @author Andres.Almiray
 */
@Singleton
final class Neo4jConnector {
    private static final Log LOG = LogFactory.getLog(Neo4jConnector)
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap

    ConfigObject createConfig(GriffonApplication app) {
        def neo4jConfigClass = app.class.classLoader.loadClass("Neo4jConfig")
        return new ConfigSlurper(Environment.current.name).parse(neo4jConfigClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        startNeo4j(config)
        bootstrap = app.class.classLoader.loadClass('BootstrapNeo4j').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(DatabaseHolder.instance.db)
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        bootstrap.destroy(DatabaseHolder.instance.db)
        stopNeo4j(config)
    }


    private void startNeo4j(config) {
        String storeDirName = config?.neo4j?.storeDir ?: 'neo4j/db'
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

        GraphDatabaseService db = new EmbeddedGraphDatabase(storeDir.absolutePath)
        DatabaseHolder.instance.db = db
        String indexType = config?.neo4j?.index?.type ?: 'normal'
        IndexService index = null
        switch(indexType) {
            case 'query': index = new LuceneFulltextQueryIndexService(db); break
            case 'fulltext': index = new LuceneFulltextIndexService(db); break
            default: index = new LuceneIndexService(db)
        }
        DatabaseHolder.instance.index = index

        Node.metaClass.indexBy = {String propertyName ->
            index.index(delegate, propertyName, delegate.getProperty(propertyName))
        }
        Node.metaClass.indexBy = {List properties ->
            def node = delegate
            properties.each {String propertyName ->
                index.index(node, propertyName, node.getProperty(propertyName))
            }
        }
    }

    private void stopNeo4j(config) {
        try{ 
            DatabaseHolder.instance.index.shutdown()
        } catch(IOException ioe) { 
            StackTraceUtils.deepSanitize(ioe)
            LOG.warn(ioe.toString(), ioe)
        }
        try{ 
            DatabaseHolder.instance.db.shutdown()
        } catch(IOException ioe) { 
            StackTraceUtils.deepSanitize(ioe)
            LOG.warn(ioe.toString(), ioe)
        }
    }

    def withNeo4j = { Closure closure ->
        def db = DatabaseHolder.instance.db
        def index = DatabaseHolder.instance.index
        Transaction tx = db.beginTx()
        try {
            closure(db, index)
            tx.success()
        } catch(Exception e) {
            StackTraceUtils.deepSanitize(e)
            LOG.warn(e.toString(), e)
            tx.failure()
        } finally {
            tx.finish()
        }
    }
}
