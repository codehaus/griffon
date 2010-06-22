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

package griffon.domain.gsql

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.DomainClassEnhancerDelegate

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import griffon.domain.gsql.metaclass.*

/**
 * @author Andres Almiray
 */
class GsqlDomainClassEnhancerDelegate extends DomainClassEnhancerDelegate {
    private static final Log LOG = LogFactory.getLog(GsqlDomainClassEnhancerDelegate.class)

    GsqlDomainClassEnhancerDelegate(GriffonApplication app) {
        GsqlDomainClassHelper.instance.init(app)
    }

    void enhance(GriffonApplication app, ArtifactInfo domainClass) {
        def mc = domainClass.metaClass

        def findAllMethod = findAll(app, domainClass)
        mc.static.findAll = {String query ->
            findAllMethod.invoke(dc.klass, 'findAll', [query] as Object[])
        }
        mc.static.findAll = {String query, Collection positionalParams ->
            findAllMethod.invoke(dc.klass, 'findAll', [query, positionalParams] as Object[])
        }
        mc.static.findAll = {String query, Collection positionalParams, Map paginateParams ->
            findAllMethod.invoke(dc.klass, 'findAll', [query, positionalParams, paginateParams] as Object[])
        }

        def findMethod = find(app, domainClass)
        mc.static.find = {String query ->
            findMethod.invoke(dc.klass, 'find', [query] as Object[])
        }
        mc.static.find = {String query, Collection args ->
            findMethod.invoke(dc.klass, 'find', [query, args] as Object[])
        }
    }
    
    def make = {GriffonApplication app, ArtifactInfo domainClass ->
        new MakePersistentMethod(app, domainClass)
    }

    def fetch = {GriffonApplication app, ArtifactInfo domainClass ->
        new FetchPersistentMethod(app, domainClass)
    }
    
    def list = {GriffonApplication app, ArtifactInfo domainClass ->
        new ListPersistentMethod(app, domainClass)
    }
    
    def find = {GriffonApplication app, ArtifactInfo domainClass ->
        new FindPersistentMethod(app, domainClass)
    }
    
    def findAll = {GriffonApplication app, ArtifactInfo domainClass ->
        new FindAllPersistentMethod(app, domainClass)
    }
    
    def count = {GriffonApplication app, ArtifactInfo domainClass ->
        new CountPersistentMethod(app, domainClass)
    }
    
    def delete = {GriffonApplication app, ArtifactInfo domainClass ->
        new DeletePersistentMethod(app, domainClass)
    }
    
    def save = {GriffonApplication app, ArtifactInfo domainClass ->
        new SavePersistentMethod(app, domainClass)
    }
}
