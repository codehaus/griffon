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
