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
import org.javanicus.gsql.*

import java.sql.Connection
import javax.sql.DataSource

import org.apache.commons.pool.ObjectPool
import org.apache.commons.pool.impl.GenericObjectPool
import org.apache.commons.dbcp.ConnectionFactory
import org.apache.commons.dbcp.PoolingDataSource
import org.apache.commons.dbcp.PoolableConnectionFactory
import org.apache.commons.dbcp.DriverManagerConnectionFactory

import org.codehaus.griffon.util.BuildSettings

includeTargets << griffonScript("Init")
includeTargets << griffonScript("Package")

/**
 * @author Andres.Almiray
 */
target(initSchema: "Initializes a database schema") {
   depends(checkVersion, parseArguments, compile, createConfig)
   def dsconfig = parseConfig()
   def dataSource = createDataSource(dsconfig)
   createSchema(dsconfig, dataSource, argsMap["params"])
   File destdir = new File(ant.antProject.replaceProperties(config.griffon.jars.destDir.toString()))
   ant.mkdir(dir: destdir)
   relocateSchema(dsconfig, destdir)
   println "  [gsql] Schema initialized successfully"
}

private parseConfig() {
   String env = System.getProperty(BuildSettings.ENVIRONMENT, BuildSettings.ENV_PRODUCTION)
   def dataSourceClass = this.class.classLoader.loadClass("DataSource")
   return new ConfigSlurper(env).parse(dataSourceClass)
}

private DataSource createDataSource(dsconfig) {
   Class.forName(dsconfig.dataSource.driverClassName.toString())
   ObjectPool connectionPool = new GenericObjectPool(null)

   String url = dsconfig.dataSource.url.toString()
   String username = dsconfig.dataSource.username.toString()
   String password = dsconfig.dataSource.password.toString()
   ConnectionFactory connectionFactory = null
   if( username ) {
      connectionFactory = new DriverManagerConnectionFactory(url, username, password)
   } else {
      connectionFactory = new DriverManagerConnectionFactory(url, null)
   }
   PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true)
   return new PoolingDataSource(connectionPool)
}

private void createSchema(dsconfig, dataSource, args) {
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
   sqlGenerator.createDatabase(schema,args[0] == "drop")
   writer.flush()
   Connection connection = null
   try{ 
      connection = dataSource.getConnection()
      Sql sql = new Sql(connection)
      sql.execute(writer.toString())
      def dbName = connection.metaData.databaseProductName
      if(dbName == 'HSQL Database Engine') {
         connection.createStatement().executeUpdate('SHUTDOWN')
      }
   }
   finally {
      connection?.close()
   }
}

private void relocateSchema(dsconfig, destdir) {
   // rellocate files only if HSQLDB is used
   String url = dsconfig.dataSource.url.toString()
   def prefix = "jdbc:hsqldb:file:"
   if( !url.startsWith(prefix) ) return
   def dbname = url.substring(prefix.size(),url.indexOf(";")) 
   //new File(basedir).list({dir, name -> name.startsWith(dbname)} as FilenameFilter).each
   new File(basedir).eachFileMatch({it.startsWith(dbname)}){ file ->
      def copy = new File(destdir, file.name)
      copy.text = file.text
      file.delete()
   }
}

setDefaultTarget(initSchema)
