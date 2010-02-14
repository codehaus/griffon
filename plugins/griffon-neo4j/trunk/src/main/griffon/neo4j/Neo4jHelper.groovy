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
package griffon.neo4j

import griffon.core.GriffonApplication
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
final class Neo4jHelper {
    private static final Log LOG = LogFactory.getLog(Neo4jHelper)

    def parseConfig(GriffonApplication app) {
        def neo4jConfigClass = app.class.classLoader.loadClass("Neo4jConfig")
        return new ConfigSlurper(Environment.current.name).parse(neo4jConfigClass)
    }

    void startNeo4j(dbConfig) {
        String storeDirName = dbConfig?.neo4j?.storeDir ?: 'neo4j/db'
        File storeDir = new File(storeDirName)
        if(!storeDir.absolute) storeDir = new File(System.getProperty('griffon.start.dir'), storeDirName)
        storeDir.mkdirs()
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        storeDir?.eachFileRecurse { f -> 
                            try { if(f?.exists()) f.delete() }
                            catch(IOException ioe) { /* ignore */ }
                        }
                    }
                })
        }

        GraphDatabaseService db = new EmbeddedGraphDatabase(storeDir.absolutePath)
        DatabaseHolder.instance.db = db
        String indexType = dbConfig?.neo4j?.index?.type ?: 'normal'
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

    void stopNeo4j(dbConfig) {
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
