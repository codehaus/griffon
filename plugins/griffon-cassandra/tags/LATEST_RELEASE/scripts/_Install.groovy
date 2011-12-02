/*
 * Copyright 2011 the original author or authors.
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

// check to see if we already have a CassandraGriffonAddon
configText = '''root.'CassandraGriffonAddon'.addon=true'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding CassandraGriffonAddon to Builder.groovy'
    builderConfigFile.text += '\n' + configText + '\n'
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

if(!new File("${basedir}/griffon-app/conf/CassandraConfig.groovy").exists()) {
   createArtifact(
      name: "CassandraConfig",
      suffix: "",
      type: "CassandraConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapCassandra.groovy").exists()) {
   createArtifact(
      name: "BootstrapCassandra",
      suffix: "",
      type: "BootstrapCassandra",
      path: "griffon-app/conf")
}

