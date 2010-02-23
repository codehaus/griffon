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

import com.db4o.ObjectSet
import griffon.db4o.Db4oHelper
import griffon.domain.metaclass.AbstractFindAllPersistentMethod
import griffon.domain.db4o.query.PropertiesPredicate

/**
 * @author Andres Almiray
 */
class FindAllPersistentMethod extends AbstractFindAllPersistentMethod {
    private static final Log LOG = LogFactory.getLog(FindAllPersistentMethod)

    FindAllPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass)
    }

    protected Collection findAll(Class clazz) {
        LOG.trace("> ${domainClass.simpleName}.findAll()")
        ObjectSet list = null
        Db4oHelper.instance.withDb4o { db4o ->
            list = db4o.query(clazz)
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findWithQueryArgs(String query, Object[] queryArgs, int max, int offset) {
        LOG.trace("< String queries with arguments are not supported: ${domainClass.simpleName}.find('${query}', ${queryArgs})")
        Collections.emptyList()
    }

    protected Collection findWithNamedArgs(String query, Map namedArgs, int max, int offset) {
        LOG.trace("< String queries with named arguments are not supported: ${domainClass.simpleName}.find('${query}', ${namedArgs})")
        return Collections.emptyList()
    }

    protected Collection findByProperties(Map properties) {
        LOG.trace("> ${domainClass.simpleName}.findAll(${properties})")
        ObjectSet list = null 
        Db4oHelper.instance.withDb4o { db4o ->
            list = db4o.query(new PropertiesPredicate(properties)).next()
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }

    protected Collection findByExample(Object example) {
        LOG.trace("> ${domainClass.simpleName}.findAll(${example})")
        ObjectSet list = null
        Db4oHelper.instance.withDb4o { db4o ->
            list = db4o.queryByExample(example)
        }
        LOG.trace("< ${domainClass.simpleName}.findAll().size() => ${list.size()}")
        list
    }
}
