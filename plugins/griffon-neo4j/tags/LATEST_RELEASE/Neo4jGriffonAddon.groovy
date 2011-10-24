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

import griffon.core.GriffonApplication
import griffon.plugins.neo4j.Neo4jConnector

/*
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.RelationshipType
*/

/**
 * @author Andres Almiray
 */
class Neo4jGriffonAddon {
    void addonInit(GriffonApplication app) {
        /*
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
        */
        ConfigObject config = Neo4jConnector.instance.createConfig(app)
        Neo4jConnector.instance.connect(app, config)
    }

    def events = [
        ShutdownStart: { app ->
            ConfigObject config = Neo4jConnector.instance.createConfig(app)
            Neo4jConnector.instance.disconnect(app, config)
        },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.neo4j?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            def mc = app.artifactManager.findGriffonClass(klass).metaClass
            Neo4jConnector.enhance(mc)
        }
    ]
}
