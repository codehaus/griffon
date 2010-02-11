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
import griffon.domain.metaclass.AbstractSavePersistentMethod

/**
 * @author Andres Almiray
 */
class SavePersistentMethod extends AbstractSavePersistentMethod {
    private static final Log LOG = LogFactory.getLog(SavePersistentMethod)

    SavePersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected boolean shouldInsert(Object target, Object[] arguments) {
        target.id == null
    }

    protected Object insert(Object target, Object[] arguments) {
        def props = domainClass.declaredProperties
        Map values = [:]
        props.each { p -> if(target[p] != null) values[p] = target[p] }

        def query = GsqlDomainClassHelper.instance.fetchQuery('insert', domainClass.klass)
        if(query instanceof Closure) query = query(domainClass, target, props) 
        LOG.trace("> ${domainClass.simpleName}.insert(${target})")
        LOG.trace(query)

        List args = new ArrayList(properties.values())
        GsqlHelper.instance.withSql { sql ->
            def rs = sql.executeInsert(query, args)
        }
        LOG.trace("< ${domainClass.simpleName}.insert().id => ${target.id}")
        target
    }

    protected Object save(Object target, Object[] arguments) {
        def props = domainClass.declaredProperties
        Map values = [:]
        props.each { p -> if(target[p] != null) values[p] = target[p] }

        def query = GsqlDomainClassHelper.instance.fetchQuery('save', domainClass.klass)
        if(query instanceof Closure) query = query(domainClass, target, values) 
        LOG.trace("> ${domainClass.simpleName}.save(${target})")
        LOG.trace(query)

        List args = []
        values.each{ k, v -> if(k != 'id') args << v }
        args << target.id
        GsqlHelper.instance.withSql { sql ->
            sql.execute(query, args)
        }
        LOG.trace("< ${domainClass.simpleName}.save().id => ${target.id}")
        target
    }
}
