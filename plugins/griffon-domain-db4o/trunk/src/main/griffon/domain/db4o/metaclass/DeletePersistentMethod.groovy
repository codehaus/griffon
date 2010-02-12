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
import griffon.domain.metaclass.AbstractDeletePersistentMethod

/**
 * @author Andres Almiray
 */
class DeletePersistentMethod extends AbstractDeletePersistentMethod {
    private static final Log LOG = LogFactory.getLog(DeletePersistentMethod)

    DeletePersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Object delete(Object target) {
        LOG.trace("> ${domainClass.simpleName}.delete(${target})")
        Object stored = target
        Db4oHelper.instance.withDb4o { db4o ->
            stored = db4o.queryByExample(target).next()
            if(stored) db4o.delete(stored)
            else stored = target
        }
        LOG.trace("< ${domainClass.simpleName}.delete(${target})")
        stored
    }
}
