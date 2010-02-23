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
