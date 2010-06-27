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
import griffon.domain.metaclass.AbstractSavePersistentMethod

/**
 * @author Andres Almiray
 */
class SavePersistentMethod extends AbstractSavePersistentMethod {
    private static final Log LOG = LogFactory.getLog(SavePersistentMethod)

    SavePersistentMethod(GsqlDomainHandler domainHandler) {
        super(domainHandler)
    }

    protected boolean shouldInsert(ArtifactInfo artifactInfo, Object target, Object[] arguments) {
        target.id == null
    }

    protected Object insert(ArtifactInfo artifactInfo, Object target, Object[] arguments) {
        def props = artifactInfo.declaredProperties
        Map values = [:]
        props.each { p -> if(target[p] != null) values[p] = target[p] }

        def query = GsqlDomainClassUtils.instance.fetchQuery('insert', artifactInfo.klass)
        if(query instanceof Closure) query = query(artifactInfo, target, props) 
        LOG.trace("> ${artifactInfo.simpleName}.insert(${target})")
        LOG.trace(query)

        List args = new ArrayList(properties.values())
        GsqlConnector.instance.withSql { sql ->
            def rs = sql.executeInsert(query, args)
        }
        LOG.trace("< ${artifactInfo.simpleName}.insert().id => ${target.id}")
        target
    }

    protected Object save(ArtifactInfo artifactInfo, Object target, Object[] arguments) {
        def props = artifactInfo.declaredProperties
        Map values = [:]
        props.each { p -> if(target[p] != null) values[p] = target[p] }

        def query = GsqlDomainClassUtils.instance.fetchQuery('save', artifactInfo.klass)
        if(query instanceof Closure) query = query(artifactInfo, target, values) 
        LOG.trace("> ${artifactInfo.simpleName}.save(${target})")
        LOG.trace(query)

        List args = []
        values.each{ k, v -> if(k != 'id') args << v }
        args << target.id
        GsqlConnector.instance.withSql { sql ->
            sql.execute(query, args)
        }
        LOG.trace("< ${artifactInfo.simpleName}.save().id => ${target.id}")
        target
    }
}
