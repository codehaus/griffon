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
