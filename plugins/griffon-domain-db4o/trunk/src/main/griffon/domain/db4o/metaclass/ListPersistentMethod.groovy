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

import com.db4o.ObjectSet
import griffon.db4o.Db4oHelper
import griffon.domain.metaclass.AbstractListPersistentMethod
import griffon.domain.db4o.query.PropertiesPredicate

/**
 * @author Andres Almiray
 */
class ListPersistentMethod extends AbstractListPersistentMethod {
    private static final Log LOG = LogFactory.getLog(ListPersistentMethod)

    ListPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Collection list(Class clazz) {
        LOG.trace("> ${domainClass.simpleName}.list()")
        ObjectSet list = null
        Db4oHelper.instance.withDb4o { db4o ->
            list = db4o.query(clazz)
        }
        LOG.trace("< ${domainClass.simpleName}.list().size() => ${list.size()}")
        list
    }
    protected Collection list(Map properties) {
        LOG.trace("> ${domainClass.simpleName}.list(${properties})")
        ObjectSet list = null
        Db4oHelper.instance.withDb4o { db4o ->
            list = db4o.query(new PropertiesPredicate(properties)).next()
        }
        LOG.trace("< ${domainClass.simpleName}.list().size() => ${list.size()}")
        list
    }
}
