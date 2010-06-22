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
import griffon.util.GriffonClassUtils

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.gsql.GsqlHelper
import griffon.domain.gsql.GsqlDomainClassHelper
import griffon.domain.GriffonPersistenceUtil
import griffon.domain.metaclass.AbstractFindPersistentMethod
import griffon.domain.orm.*

/**
 * @author Andres Almiray
 */
class FindPersistentMethod extends AbstractFindPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindPersistentMethod)

    FindPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments && arguments.length >= 1) {
            final Object arg = arguments[0] instanceof CharSequence ? arguments[0].toString() : arguments[0]
            if(arg instanceof String) {
                String query = arg.trim()
                Object[] queryArgs = null
    
                if(arguments.length > 1) {
                    if (arguments[1] instanceof Collection) {
                        queryArgs = GriffonClassUtils.collectionToObjectArray((Collection) arguments[1])
                    } else if (arguments[1].getClass().isArray()) {
                        queryArgs = (Object[]) arguments[1]
                    }
                }
    
                return findWithQueryArgs(query, queryArgs != null ? queryArgs : GriffonPersistenceUtil.EMPTY_ARRAY); 
            }
        }
        return super.doInvokeInternal(clazz, methodName, arguments)
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

    protected Object findByProperties(Map properties) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('find_byProperties', domainClass.klass)
        if(query instanceof Closure) (query, properties) = query(domainClass, properties)
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
        if(query instanceof Closure) (query, values) = query(domainClass, example, values) 
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

    protected Object findByCriterion(Criterion criterion, Map options) {
        Map values = [:]
        def query = GsqlDomainClassHelper.instance.fetchQuery('find_byCriterion', domainClass.klass)
        if(query instanceof Closure) (query, values) = query(domainClass, criterion) 
        LOG.trace("> ${domainClass.simpleName}.find()")
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
