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
import griffon.neo4j.Neo4jHelper
import griffon.neo4j.DatabaseHolder

import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.RelationshipType

/**
 * @author Andres Almiray
 */
class Neo4jGriffonAddon {
    private bootstrap

    def addonInit(app) {
        [Node.metaClass, Relationship.metaClass].each { mc ->
            mc.getAt = {String propertyName ->
                delegate.getProperty(propertyName)
            }
            mc.putAt = {String propertyName, Object value ->
                delegate.setProperty(propertyName, value) 
            }
        }
        Node.metaClass.relate = {RelationshipType relType, Node other ->
            return delegate.createRelationshipTo(other, relType)
        }
    }

    def events = [
        BootstrapEnd: { app -> startNeo4j(app) },
        ShutdownStart: { app -> stopNeo4j(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.neo4j?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withNeo4j = Neo4jHelper.instance.withNeo4j
        }
    ]

    private void startNeo4j(GriffonApplication app) {
        def dbConfig = Neo4jHelper.instance.parseConfig(app)
        Neo4jHelper.instance.startNeo4j(dbConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapNeo4j').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(DatabaseHolder.instance.db)
    }

    private void stopNeo4j(GriffonApplication app) {
        bootstrap.destroy(DatabaseHolder.instance.db)
        def dbConfig = Neo4jHelper.instance.parseConfig(app)
        Neo4jHelper.instance.stopNeo4j(dbConfig)
    }
}
