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

package griffon.domain.gsql

import griffon.core.ArtifactInfo

/**
 * @author Andres Almiray
 */
class DefaultGsqlQueries {
    def findAll = {ArtifactInfo artifactInfo, String table ->
        return "SELECT * FROM $table ORDER BY id ASC"
    }

    def findAll_byExample = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Object example, Map values ->
            "SELECT * FROM $table WHERE ${values.keySet().join(' = ?, ')} = ? ORDER BY id ASC"
        }
    }

    def findAll_byProperties = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Map props ->
            "SELECT * FROM $table WHERE ${props.keySet().join(' = ?, ')} = ? ORDER BY id ASC"
        }
    }

    def find_byExample = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Object example, Map values ->
            "SELECT * FROM $table WHERE ${values.keySet().join(' = ?, ')} = ? ORDER BY id ASC"
        }
    }

    def find_byProperties = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Map props ->
            "SELECT * FROM $table WHERE ${props.keySet().join(' = ?, ')} = ? ORDER BY id ASC"
        }
    }

    def fetch = {ArtifactInfo artifactInfo, String table ->
        return "SELECT * FROM $table WHERE id = ?"
    }

    def list = {ArtifactInfo artifactInfo, String table ->
        return "SELECT * FROM $table ORDER BY id ASC"
    }

    def list_byProperties = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Map props ->
            "SELECT * FROM $table WHERE ${props.keySet().join(' = ?, ')} = ? ORDER BY id ASC"
        }
    }

    def count = {ArtifactInfo artifactInfo, String table ->
        return "SELECT COUNT(id) AS __domain_count__ FROM $table"
    }

    def countBy = {ArtifactInfo artifactInfo, String table ->
        return "SELECT COUNT(id) AS __domain_count__ FROM $table WHERE \$column = ?"
    }

    def findBy = {ArtifactInfo artifactInfo, String table ->
        return "SELECT * FROM $table WHERE \$column = ?"
    }

    def findWhere = {ArtifactInfo artifactInfo, String table ->
        return "SELECT COUNT(id) AS __domain_count__ FROM $table"
    }

    def findAllBy = {ArtifactInfo artifactInfo, String table ->
        return "SELECT * FROM $table WHERE \$column = ? ORDER BY id ASC"
    }

    def findAllWhere = {ArtifactInfo artifactInfo, String table ->
        return "SELECT COUNT(id) AS __domain_count__ FROM $table"
    }

    def insert = {ArtifactInfo artifactInfo, String table ->
        return "SELECT COUNT(id) AS __domain_count__ FROM $table"
    }

    def save = {ArtifactInfo artifactInfo, String table ->
        return {ArtifactInfo dc, Object target, Map props ->
            List columns = []
            columns.addAll(props.keySet())
            columns.remove('id')
            return "UPDATE $table SET ${columns.join(' = ?, ')} = ? WHERE id = ?"
        }
    }

    def delete = {ArtifactInfo artifactInfo, String table ->
        return "DELETE FROM $table WHERE id = ?"
    }
}
