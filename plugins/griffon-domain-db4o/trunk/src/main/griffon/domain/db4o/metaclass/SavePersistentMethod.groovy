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
