/*
 * Copyright 2009 the original author or authors.
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

package griffon.gsql

import groovy.sql.Sql
import org.javanicus.gsql.*

import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.pool.ObjectPool
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.ConnectionFactory
import org.apache.commons.dbcp.PoolingDataSource
import org.apache.commons.dbcp.PoolableConnectionFactory
import org.apache.commons.dbcp.DriverManagerConnectionFactory

/**
 * @author Andres.Almiray
 */
class GsqlAddon {
   private def bootstrap
   private DataSource dataSource

   private static final String ENVIRONMENT = "griffon.env"
   private static final String ENVIRONMENT_DEV = "dev"
   private static final String ENVIRONMENT_PROD = "prod"
   private static final String ENVIRONMENT_DEV_LONG = "development"
   private static final String ENVIRONMENT_PROD_LONG = "production"

   def addonInit = { app ->
      app.addApplicationEventListener(this)
   }

   def onBootstrapEnd = { app ->
      def config = parseConfig()
      createDataSource(config)
      createSchema(config)
      bootstrapInit()
   }

   def onShutdownStart = { app ->
      Connection connection = null
      try {
         connection = dataSource.getConnection()
         bootstrap.destroy(new Sql(connection))
         def dbName = connection.metaData.databaseProductName
         if(dbName == 'HSQL Database Engine') {
            connection.createStatement().executeUpdate('SHUTDOWN')
         }
      } finally {
         connection?.close()
      }
   }

   def onNewInstance = { klass, type, instance ->
      if(type != "controller") return
      instance.metaClass.withSql = this.withSql
   }

   // ======================================================

   private parseConfig() {
      String env = System.getProperty(ENVIRONMENT, ENVIRONMENT_PROD)
      if(env == ENVIRONMENT_DEV) env = ENVIRONMENT_DEV_LONG 
      if(env == ENVIRONMENT_PROD) env = ENVIRONMENT_PROD_LONG 
      def dataSourceClass = this.class.classLoader.loadClass("DataSource")
      return new ConfigSlurper(env).parse(dataSourceClass)
   }

   private void createDataSource(config) {
      if(dataSource) return
      
      Class.forName(config.dataSource.driverClassName.toString())
      ObjectPool connectionPool = new GenericObjectPool(null)
      if(config.dataSource.pooled) {
         if(config?.pool?.maxWait != null)  connectionPool.maxWait = config.pool.maxWait
         if(config?.pool?.maxIdle != null)  connectionPool.maxIdle = config.pool.maxIdle
         if(config?.pool?.maxActive != null)  connectionPool.maxActive = config.pool.maxActive
      }

      String url = config.dataSource.url.toString()
      String username = config.dataSource.username.toString()
      String password = config.dataSource.password.toString()
      ConnectionFactory connectionFactory = null
      if( username ) {
         connectionFactory = new DriverManagerConnectionFactory(url, username, password)
      } else {
         connectionFactory = new DriverManagerConnectionFactory(url, null)
      }
      PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true)
      dataSource = new PoolingDataSource(connectionPool)
   }

   private void createSchema(config) {
      def dbCreate = config.dataSource?.dbCreate.toString()
      if(dbCreate == "skip") return

      TypeMap typeMap = new TypeMap()
      Binding binding = new Binding()
      binding.setVariable("builder", new RelationalBuilder(typeMap))
      def schemaClass = this.class.classLoader.loadClass("Schema")
      def schemaScript = schemaClass.newInstance()
      schemaScript.binding = binding
      def schema = schemaScript.run()

      SqlGenerator sqlGenerator = new SqlGenerator(typeMap,System.getProperty("line.separator","\n"))
      StringWriter writer = new StringWriter()
      sqlGenerator.writer = writer
      boolean drop = dbCreate == "create-drop"
      sqlGenerator.createDatabase(schema,drop)
      writer.flush()
      withSql { sql -> sql.execute(writer.toString()) }
   }

   private void bootstrapInit() {
      bootstrap = this.class.classLoader.loadClass("BootStrapGsql").newInstance()
      withSql { sql -> bootstrap.init(sql) }
   }

   private withSql = { Closure closure ->
      Connection connection = dataSource.getConnection()
      Sql sql = new Sql(connection)
      try {
         closure(sql)
      } finally {
         connection.close()
      }
   }
}
