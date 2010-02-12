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

package griffon.domain.db4o

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.DomainClassEnhancerDelegate

import griffon.domain.db4o.metaclass.*

/**
 * @author Andres Almiray
 */
class Db4oDomainClassEnhancerDelegate extends DomainClassEnhancerDelegate {
    Db4oDomainClassEnhancerDelegate(GriffonApplication app) {
    }

    void enhance(ArtifactInfo domainClass, GriffonApplication app) {
        MetaClass mc = domainClass.klass.metaClass
        def queryMethod = new QueryPersistentMethod(app, domainClass)
        mc.static.query = {Closure closure ->
            queryMethod.invoke(delegate, 'query', [closure] as Object[])
        }
    }

    def make = { GriffonApplication app, ArtifactInfo domainClass ->
        new MakePersistentMethod(app, domainClass)
    }

    def list = { GriffonApplication app, ArtifactInfo domainClass ->
        new ListPersistentMethod(app, domainClass)
    }
    
    def find = { GriffonApplication app, ArtifactInfo domainClass ->
        new FindPersistentMethod(app, domainClass)
    }
    
    def findAll = { GriffonApplication app, ArtifactInfo domainClass ->
        new FindAllPersistentMethod(app, domainClass)
    }
    
    def count = { GriffonApplication app, ArtifactInfo domainClass ->
        new CountPersistentMethod(app, domainClass)
    }
    
    def delete = { GriffonApplication app, ArtifactInfo domainClass ->
        new DeletePersistentMethod(app, domainClass)
    }
    
    def save = { GriffonApplication app, ArtifactInfo domainClass ->
        new SavePersistentMethod(app, domainClass)
    }
}
