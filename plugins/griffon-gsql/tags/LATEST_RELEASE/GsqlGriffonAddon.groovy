/*
 * Copyright 2009-2010 the original author or authors.
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

import groovy.sql.Sql

import java.sql.Connection

import griffon.core.GriffonApplication
import griffon.gsql.DataSourceHolder
import griffon.gsql.GsqlHelper

/**
 * @author Andres.Almiray
 */
class GsqlGriffonAddon {
    private bootstrap
 
    def addonPostInit = { app ->
        def config = GsqlHelper.instance.parseConfig()
        GsqlHelper.instance.createDataSource(config)
        def skipSchema = app.config?.griffon?.gsql?.schema?.skip ?: false
        if(!skipSchema) GsqlHelper.instance.createSchema(config)
    }

    def events = [
        BootstrapEnd: { app ->
            bootstrapInit()
        },
        ShutdownStart: { app ->
            Connection connection = null
            try {
                connection = DataSourceHolder.instance.dataSource.getConnection()
                bootstrap.destroy(new Sql(connection))
                if(connection.metaData.databaseProductName == 'HSQL Database Engine') {
                    connection.createStatement().executeUpdate('SHUTDOWN')
                }
            } finally {
                connection?.close()
            }
        },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.gsql?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withSql = GsqlHelper.instance.withSql
        }
    ]

    def exportWithJmx = { exporter, domain, ctx ->
        exporter.beans."${domain}:service=datasource,type=configuration" = DataSourceHolder.instance.dataSource
    }

    // ======================================================

    private void bootstrapInit() {
        bootstrap = this.class.classLoader.loadClass('BootstrapGsql').newInstance()
        bootstrap.metaClass.app = app
        GsqlHelper.instance.withSql { sql -> bootstrap?.init(sql) }
    }
}
