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

/**
 * @author Andres Almiray
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a GsqlGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'GsqlGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding GsqlGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'GsqlGriffonAddon'.addon=true
''')
}

if(!(config.flatten().'griffon.gsql.injectInto')) {
     configFile.append('''
griffon.gsql.injectInto = ['controller']
''')
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

if(!new File("${basedir}/griffon-app/conf/DataSource.groovy").exists()) {
   createArtifact(
      name: "DataSource",
      suffix: "",
      type: "DataSource",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapGsql.groovy").exists()) {
   createArtifact(
      name: "BootstrapGsql",
      suffix: "",
      type: "BootstrapGsql",
      path: "griffon-app/conf")
}

printFramed("""You may need to create an schema.ddl file depending on your settings.
If so, place it in griffon-app/resources.""")
