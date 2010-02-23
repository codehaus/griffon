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
import java.util.regex.Pattern

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import com.db4o.ObjectSet
import griffon.db4o.Db4oHelper
import griffon.domain.metaclass.AbstractStaticPersistentMethod
import griffon.domain.db4o.query.ClosurePredicate

/**
 * @author Andres Almiray
 */
public class QueryPersistentMethod extends AbstractStaticPersistentMethod {
    private static final Log LOG = LogFactory.getLog(QueryPersistentMethod)

    public QueryPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass, Pattern.compile('^query$'))
    }

    protected final Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments && arguments.length == 1 && arguments[0] instanceof Closure) {
            return query((Closure) arguments[0])
        }
        throw new MissingMethodException(methodName, clazz, arguments)
    }

    protected Object query(Closure closure) {
        LOG.trace("> ${domainClass.simpleName}.query()")
        ObjectSet set = null
        Db4oHelper.instance.withDb4o { db4o ->
            set = db4o.query(new ClosurePredicate(closure))
        }
        LOG.trace("< ${domainClass.simpleName}.query()")
        set
    }
}
