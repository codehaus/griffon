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
import griffon.domain.metaclass.AbstractFindPersistentMethod

/**
 * @author Andres Almiray
 */
class FindPersistentMethod extends AbstractFindPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindPersistentMethod)

    FindPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object findWithQueryArgs(String query, Object[] queryArgs) {
        LOG.trace("> ${domainClass.simpleName}.find(\"${query}\", ${queryArgs})")
        LOG.trace(query)
        def instance = null
        GsqlHelper.instance.withSql { sql ->
            def row = sql.firstRow(query, queryArgs.collect([]){it})
            if(row) instance = GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
        }
        LOG.trace("< ${domainClass.simpleName}.find() => ${instance}")
        instance
    }

    protected Object findWithNamedArgs(String query, Map namedArgs) {
        LOG.trace("< Named arguments not supported: ${domainClass.simpleName}.find(${namedArgs})")
        return null
    }

    protected Object findByProperties(Map properties) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('find_byProperties', domainClass.klass)
        if(query instanceof Closure) query = query(domainClass, properties)
        LOG.trace("> ${domainClass.simpleName}.find(${properties})")
        LOG.trace(query)

        def instance = null
        List args = new ArrayList(properties.values())
        GsqlHelper.instance.withSql { sql ->
            def row = sql.firstRow(query, args)
            if(row) instance = GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
        }
        LOG.trace("< ${domainClass.simpleName}.find() => ${instance}")
        instance
    }

    protected Object findByExample(Object example) {
        def props = domainClass.declaredProperties
        Map values = [:]
        props.each { p -> if(example[p] != null) values[p] = example[p] }

        def query = GsqlDomainClassHelper.instance.fetchQuery('find_byExample', domainClass.klass)
        if(query instanceof Closure) query = query(domainClass, example, values) 
        LOG.trace("> ${domainClass.simpleName}.find(${example})")
        LOG.trace(query)

        def instance = null
        List args = new ArrayList(values.values())
        GsqlHelper.instance.withSql { sql ->
            def row = sql.firstRow(query, args)
            if(row) instance = GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
        }
        LOG.trace("< ${domainClass.simpleName}.find() => ${instance}")
        instance
    }
}
