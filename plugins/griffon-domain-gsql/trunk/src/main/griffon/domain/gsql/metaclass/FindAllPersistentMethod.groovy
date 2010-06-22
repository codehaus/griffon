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
import griffon.domain.metaclass.AbstractFindAllPersistentMethod
import griffon.domain.orm.*

/**
 * @author Andres Almiray
 */
class FindAllPersistentMethod extends AbstractFindAllPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindAllPersistentMethod)

    FindAllPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments && arguments.length >= 1) {
            final Object arg = arguments[0] instanceof CharSequence ? arguments[0].toString() : arguments[0]
            if(arg instanceof String) {
                String query = arg.trim()
                Object[] queryArgs = null;
                int max = GriffonPersistenceUtil.retrieveMaxValue(arguments);
                int offset = GriffonPersistenceUtil.retrieveOffsetValue(arguments);
    
                if(arguments.length > 1) {
                    if (arguments[1] instanceof Collection) {
                        queryArgs = GriffonClassUtils.collectionToObjectArray((Collection) arguments[1]);
                    } else if (arguments[1].getClass().isArray()) {
                        queryArgs = (Object[]) arguments[1];
                    }
                }
    
                return findWithQueryArgs(query, queryArgs != null ? queryArgs : GriffonPersistenceUtil.EMPTY_ARRAY, max, offset); 
            }
        }
        return super.doInvokeInternal(clazz, methodName, arguments)
    }

    protected Collection findAll(Class clazz) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('findAll', clazz)
        if(query instanceof Closure) query = query(domainClass) 
        LOG.trace("> ${domainClass.simpleName}.findAll()")
        LOG.trace(query)
        List list = []
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query.toString()) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(clazz, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findWithQueryArgs(String query, Object[] queryArgs, int max, int offset) {
        LOG.trace("> ${domainClass.simpleName}.findAll(\"${query}\", ${queryArgs})")
        LOG.trace(query)
        List list = []
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query, queryArgs.collect([]){it}) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByProperties(Map properties) {
        def query = GsqlDomainClassHelper.instance.fetchQuery('findAll_byProperties', domainClass.klass)
        if(query instanceof Closure) (query, properties) = query(domainClass, properties) 
        LOG.trace("> ${domainClass.simpleName}.findAll(${properties})")
        LOG.trace(query)
        List list = []
        List args = new ArrayList(properties.values())
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByExample(Object example) {
        def props = domainClass.declaredProperties
        Map values = [:]
        props.each { p -> if(example[p] != null) values[p] = example[p] }

        def query = GsqlDomainClassHelper.instance.fetchQuery('findAll_byExample', domainClass.klass)
        if(query instanceof Closure) (query, values) = query(domainClass, example, values) 
        LOG.trace("> ${domainClass.simpleName}.findAll(${example})")
        LOG.trace(query)

        List list = []
        List args = new ArrayList(values.values())
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Object findByCriterion(Criterion criterion, Map options) {
        Map values = [:]
        def query = GsqlDomainClassHelper.instance.fetchQuery('findAll_byCriterion', domainClass.klass)
        if(query instanceof Closure) (query, values) = query(domainClass, criterion)
        LOG.trace("> ${domainClass.simpleName}.findAll()")
        LOG.trace(query)

        List list = []
        List args = new ArrayList(values.values())
        GsqlHelper.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassHelper.instance.makeAndPopulateInstance(domainClass.klass, row)
            }
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }
}
