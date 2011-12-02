/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.plugins.cassandra

import groovy.sql.Sql

import java.sql.Connection
import javax.sql.DataSource

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
class DataSourceHolder {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, DataSource> dataSources = [:]
  
    String[] getDataSourceNames() {
        List<String> dataSourceNames = new ArrayList().addAll(dataSources.keySet())
        dataSourceNames.toArray(new String[dataSourceNames.size()])
    }

    DataSource getDataSource(String dataSourceName = 'default') {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        retrieveDataSource(dataSourceName)
    }

    void setDataSource(String dataSourceName = 'default', DataSource ds) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        storeDataSource(dataSourceName, ds)       
    }

    Object withCql(String dataSourceName = 'default', Closure closure) {
        DataSource ds = fetchDataSource(dataSourceName)
        if(LOG.debugEnabled) LOG.debug("Executing SQL statement on datasource '$dataSourceName'")
        Connection connection = ds.getConnection()
        try {
            return closure(dataSourceName, new Sql(connection))
        } finally {
            //connection.close()
        }
    }

    Object withCql(String dataSourceName = 'default', CallableWithArgs callable) {
        DataSource ds = fetchDataSource(dataSourceName)
        if(LOG.debugEnabled) LOG.debug("Executing SQL statement on datasource '$dataSourceName'")
        Connection connection = ds.getConnection()
        try {
            return callable.call([dataSourceName, new Sql(connection)] as Object[])
        } finally {
            connection.close()
        }
    }
    
    boolean isDataSourceConnected(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        retrieveDataSource(dataSourceName) != null
    }
    
    void disconnectDataSource(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        storeDataSource(dataSourceName, null)        
    }

    private DataSource fetchDataSource(String dataSourceName) {
        if(isBlank(dataSourceName)) dataSourceName = 'default'
        DataSource ds = retrieveDataSource(dataSourceName)
        if(ds == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = CassandraConnector.instance.createConfig(app)
            ds = CassandraConnector.instance.connect(app, config, dataSourceName)
        }
        
        if(ds == null) {
            throw new IllegalArgumentException("No such DataSource configuration for name $dataSourceName")
        }
        ds
    }

    private DataSource retrieveDataSource(String dataSourceName) {
        synchronized(LOCK) {
            dataSources[dataSourceName]
        }
    }

    private void storeDataSource(String dataSourceName, DataSource ds) {
        synchronized(LOCK) {
            dataSources[dataSourceName] = ds
        }
    }
}
