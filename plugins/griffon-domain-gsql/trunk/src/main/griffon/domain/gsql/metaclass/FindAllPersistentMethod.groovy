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

import griffon.gsql.GsqlConnector
import griffon.domain.gsql.GsqlDomainHandler
import griffon.domain.gsql.GsqlDomainClassUtils
import griffon.domain.metaclass.AbstractFindAllPersistentMethod
import griffon.domain.orm.*
import griffon.persistence.GriffonPersistenceUtil

/**
 * @author Andres Almiray
 */
class FindAllPersistentMethod extends AbstractFindAllPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindAllPersistentMethod)

    FindAllPersistentMethod(GsqlDomainHandler domainHandler) {
        super(domainHandler)
    }

    protected Object invokeInternal(ArtifactInfo artifactInfo, Class clazz, String methodName, Object[] arguments) {
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
    
                return findWithQueryArgs(artifactInfo, query, queryArgs != null ? queryArgs : GriffonPersistenceUtil.EMPTY_ARRAY, max, offset); 
            }
        }
        return super.invokeInternal(artifactInfo, clazz, methodName, arguments)
    }

    protected Collection findAll(ArtifactInfo artifactInfo, Class clazz) {
        def query = GsqlDomainClassUtils.instance.fetchQuery('findAll', clazz)
        if(query instanceof Closure) query = query(artifactInfo) 
        LOG.trace("> ${artifactInfo.simpleName}.findAll()")
        LOG.trace(query)
        List list = []
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query.toString()) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(clazz, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findWithQueryArgs(ArtifactInfo artifactInfo, String query, Object[] queryArgs, int max, int offset) {
        LOG.trace("> ${artifactInfo.simpleName}.findAll(\"${query}\", ${queryArgs})")
        LOG.trace(query)
        List list = []
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query, queryArgs.collect([]){it}) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(artifactInfo.klass, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByProperties(ArtifactInfo artifactInfo, Map properties) {
        def query = GsqlDomainClassUtils.instance.fetchQuery('findAll_byProperties', artifactInfo.klass)
        if(query instanceof Closure) (query, properties) = query(artifactInfo, properties) 
        LOG.trace("> ${artifactInfo.simpleName}.findAll(${properties})")
        LOG.trace(query)
        List list = []
        List args = new ArrayList(properties.values())
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(artifactInfo.klass, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByExample(ArtifactInfo artifactInfo, Object example) {
        def props = artifactInfo.declaredProperties
        Map values = [:]
        props.each { p -> if(example[p] != null) values[p] = example[p] }

        def query = GsqlDomainClassUtils.instance.fetchQuery('findAll_byExample', artifactInfo.klass)
        if(query instanceof Closure) (query, values) = query(artifactInfo, example, values) 
        LOG.trace("> ${artifactInfo.simpleName}.findAll(${example})")
        LOG.trace(query)

        List list = []
        List args = new ArrayList(values.values())
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(artifactInfo.klass, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByCriterion(ArtifactInfo artifactInfo, Criterion criterion, Map options) {
        Map values = [:]
        def query = GsqlDomainClassUtils.instance.fetchQuery('findAll_byCriterion', artifactInfo.klass)
        if(query instanceof Closure) (query, values) = query(artifactInfo, criterion)
        LOG.trace("> ${artifactInfo.simpleName}.findAll()")
        LOG.trace(query)

        List list = []
        List args = new ArrayList(values.values())
        GsqlConnector.instance.withSql { sql ->
            sql.eachRow(query, args) { row ->
                list << GsqlDomainClassUtils.instance.makeAndPopulateInstance(artifactInfo.klass, row)
            }
        }
        LOG.trace("< ${artifactInfo.simpleName}.findAll().size() => ${list.size()}")
        list
    }
}
