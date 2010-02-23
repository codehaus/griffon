/* --------------------------------------------------------------------
   griffon-domain-db4o plugin
   Copyright (C) 2010 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
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
