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

package griffon.domain.db4o.metaclass

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.db4o.Db4oHelper
import griffon.domain.metaclass.AbstractFindPersistentMethod
import griffon.domain.db4o.query.PropertiesPredicate

/**
 * @author Andres Almiray
 */
class FindPersistentMethod extends AbstractFindPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindPersistentMethod)

    FindPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object findWithQueryArgs(String query, Object[] queryArgs) {
        LOG.trace("< String queries with arguments are not supported: ${domainClass.simpleName}.find('${query}', ${queryArgs})")
        return null
    }

    protected Object findWithNamedArgs(String query, Map namedArgs) {
        LOG.trace("< String queries with named arguments are not supported: ${domainClass.simpleName}.find('${query}', ${namedArgs})")
        return null
    }

    protected Object findByProperties(Map properties) {
        LOG.trace("> ${domainClass.simpleName}.find(${properties})")
        LOG.trace(query)

        def instance = null
        Db4oHelper.instance.withDb4o { db4o ->
            instance = db4o.query(new PropertiesPredicate(properties)).next()
        }
        LOG.trace("< ${domainClass.simpleName}.find() => ${instance}")
        instance
    }

    protected Object findByExample(Object example) {
        LOG.trace("> ${domainClass.simpleName}.find(${example})")
        Object instance = null
        Db4oHelper.instance.withDb4o { db4o ->
            instance = db4o.queryByExample(example).next()
        }
        LOG.trace("< ${domainClass.simpleName}.find() => ${instance}")
        instance
    }
}
