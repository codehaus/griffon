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

import griffon.gsql.GsqlHelper
import griffon.domain.gsql.GsqlDomainClassHelper
import griffon.domain.metaclass.AbstractListPersistentMethod

/**
 * @author Andres Almiray
 */
class ListPersistentMethod extends AbstractListPersistentMethod {
    private static final Log LOG = LogFactory.getLog(ListPersistentMethod)

    ListPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Collection list(Class clazz) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('list', clazz)
        if(query instanceof Closure) query = query(domainClass) 
        LOG.trace("> ${domainClass.simpleName}.list()")
        LOG.trace(query)
        List list = []
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query.toString()) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(clazz, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.list().size() => ${list.size()}")
        list
    }

    protected Collection list(Map properties) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('list_byProperties', domainClass.klass)
        if(query instanceof Closure) (query, properties) = query(domainClass, properties)
        LOG.trace("> ${domainClass.simpleName}.list(${properties})")
        LOG.trace(query)
        List list = []
        List args = new ArrayList(properties.values())
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.list().size() => ${list.size()}")
        list
    }
}
