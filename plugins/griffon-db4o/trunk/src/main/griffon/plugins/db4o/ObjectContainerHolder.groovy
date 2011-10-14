/* --------------------------------------------------------------------
   griffon-db4o plugin
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
package griffon.plugins.db4o

import com.db4o.ObjectContainer

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
class ObjectContainerHolder {
    private static final Log LOG = LogFactory.getLog(ObjectContainerHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, ObjectContainer> dataSources = [:]
  
    String[] getObjectContainerNames() {
        List<String> dataSourceNames = new ArrayList().addAll(dataSources.keySet())
        dataSourceNames.toArray(new String[dataSourceNames.size()])
    }

    ObjectContainer getObjectContainer(String dataSourceName = 'default') {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        retrieveObjectContainer(dataSourceName)
    }

    void setObjectContainer(String dataSourceName = 'default', ObjectContainer oc) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        storeObjectContainer(dataSourceName, oc)       
    }

    Object withDb4o(String dataSourceName = 'default', Closure closure) {
        ObjectContainer oc = fetchObjectContainer(dataSourceName)
        if(LOG.debugEnabled) LOG.debug("Executing SQL statement on datasource '$dataSourceName'")
        return closure(dataSourceName, oc)
    }

    Object withDb4o(String dataSourceName = 'default', CallableWithArgs callable) {
        ObjectContainer oc = fetchObjectContainer(dataSourceName)
        if(LOG.debugEnabled) LOG.debug("Executing SQL statement on datasource '$dataSourceName'")
        return callable.call([dataSourceName, oc] as Object[])
    }
    
    boolean isObjectContainerConnected(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        retrieveObjectContainer(dataSourceName) != null
    }
    
    void disconnectObjectContainer(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        storeObjectContainer(dataSourceName, null)        
    }

    private ObjectContainer fetchObjectContainer(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        ObjectContainer oc = retrieveObjectContainer(dataSourceName)
        if(oc == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = Db4oConnector.instance.createConfig(app)
            oc = Db4oConnector.instance.connect(app, config, dataSourceName)
        }
        
        if(oc == null) {
            throw new IllegalArgumentException("No such ObjectContainer configuration for name $dataSourceName")
        }
        oc
    }

    private ObjectContainer retrieveObjectContainer(String dataSourceName) {
        synchronized(LOCK) {
            dataSources[dataSourceName]
        }
    }

    private void storeObjectContainer(String dataSourceName, ObjectContainer oc) {
        synchronized(LOCK) {
            dataSources[dataSourceName] = oc
        }
    }
}
