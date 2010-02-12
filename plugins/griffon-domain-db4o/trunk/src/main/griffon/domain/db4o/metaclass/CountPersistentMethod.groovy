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
import griffon.domain.metaclass.AbstractCountPersistentMethod

/**
 * @author Andres Almiray
 */
class CountPersistentMethod extends AbstractCountPersistentMethod {
    private static final Log LOG = LogFactory.getLog(CountPersistentMethod)

    CountPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected int count(Class clazz) {
        LOG.trace("> ${domainClass.simpleName}.count()")
        int count = 0
        Db4oHelper.instance.withDb4o { db4o ->
            count = db4o.query(clazz)?.size() ?: 0
        }
        LOG.trace("< ${domainClass.simpleName}.count() => ${count}")
        count
    }
}
