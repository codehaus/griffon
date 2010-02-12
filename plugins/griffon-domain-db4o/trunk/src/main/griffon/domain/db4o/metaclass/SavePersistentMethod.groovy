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
import griffon.domain.metaclass.AbstractSavePersistentMethod

import org.apache.commons.beanutils.PropertyUtils

/**
 * @author Andres Almiray
 */
class SavePersistentMethod extends AbstractSavePersistentMethod {
    private static final Log LOG = LogFactory.getLog(SavePersistentMethod)

    SavePersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected boolean shouldInsert(Object target, Object[] arguments) {
        return Db4oHelper.instance.withDb4o { db4o ->
            ObjectSet set = db4o.queryByExample(target)
            return !set.hasNext()
        }
    }

    protected Object insert(Object target, Object[] arguments) {
        LOG.trace("> ${domainClass.simpleName}.insert(${target})")
        Db4oHelper.instance.withDb4o { db4o ->
            db4o.store(target)
            target = db4o.queryByExample(target).next()
        }
        LOG.trace("< ${domainClass.simpleName}.insert() => ${target}")
        target
    }

    protected Object save(Object target, Object[] arguments) {
        LOG.trace("> ${domainClass.simpleName}.save(${target})")
        Object stored
        Db4oHelper.instance.withDb4o { db4o ->
            stored = db4o.queryByExample(target).next()
            // TODO use DomainArtifactClass to find out which properties
            // can be copied from target to stored
            PropertyUtils.copyProperties(stored, target)
            db4o.store(stored)
        }
        LOG.trace("< ${domainClass.simpleName}.save() => ${stored}")
        stored
    }
}
