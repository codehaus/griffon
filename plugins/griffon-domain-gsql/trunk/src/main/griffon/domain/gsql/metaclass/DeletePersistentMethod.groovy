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
import griffon.domain.metaclass.AbstractDeletePersistentMethod

/**
 * @author Andres Almiray
 */
class DeletePersistentMethod extends AbstractDeletePersistentMethod {
    private static final Log LOG = LogFactory.getLog(DeletePersistentMethod)

    DeletePersistentMethod(GsqlDomainHandler domainHandler) {
        super(domainHandler)
    }

    protected Object delete(ArtifactInfo artifactInfo, Object target) {
        if(target.id == null) return target
        def query = GsqlDomainClassUtils.instance.fetchQuery('delete', artifactInfo.klass)
        if(query instanceof Closure) query = query(artifactInfo, key) 
        LOG.trace("> ${artifactInfo.simpleName}.delete(${target.id})")
        LOG.trace(query)

        GsqlConnector.instance.withSql { sql ->
            sql.execute(query, [target.id])
        }
        LOG.trace("< ${artifactInfo.simpleName}.delete(${target.id})")
        target
    }
}
