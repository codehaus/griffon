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

import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.Transaction
import org.neo4j.index.IndexService

/**
 * @author Andres Almiray
 */
@Singleton
class DatabaseHolder {
    EmbeddedGraphDatabase db
    IndexService index

    def withNeo4j = { Closure closure ->
        Transaction tx = db.beginTx()
        try {
            closure(db, index)
            tx.success()
        } catch(Exception e) {
            tx.failure()
        } finally {
            tx.finish()
        }
    }
}
