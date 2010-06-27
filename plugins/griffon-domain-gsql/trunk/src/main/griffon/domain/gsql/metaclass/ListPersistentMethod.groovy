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

package griffon.domain.gsql.metaclass

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.gsql.GsqlConnector
import griffon.domain.gsql.GsqlDomainHandler
import griffon.domain.gsql.GsqlDomainClassUtils
import griffon.domain.metaclass.AbstractListPersistentMethod

/**
 * @author Andres Almiray
 */
class ListPersistentMethod extends AbstractListPersistentMethod {
    private static final Log LOG = LogFactory.getLog(ListPersistentMethod)

    ListPersistentMethod(GsqlDomainHandler domainHandler) {
        super(domainHandler)
    }

    protected Collection list(ArtifactInfo artifactInfo, Class clazz) {
        def query = GsqlDomainClassUtils.instance.fetchQuery('list', clazz)
        if(query instanceof Closure) query = query(artifactInfo) 
        LOG.trace("> ${artifactInfo.simpleName}.list()")
        LOG.trace(query)
        List list = []
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query.toString()) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(clazz, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.list().size() => ${list.size()}")
        list
    }

    protected Collection list(ArtifactInfo artifactInfo, Map properties) {
        def query = GsqlDomainClassUtils.instance.fetchQuery('list_byProperties', artifactInfo.klass)
        if(query instanceof Closure) (query, properties) = query(artifactInfo, properties)
        LOG.trace("> ${artifactInfo.simpleName}.list(${properties})")
        LOG.trace(query)
        List list = []
        List args = new ArrayList(properties.values())
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(artifactInfo.klass, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.list().size() => ${list.size()}")
        list
    }
}
