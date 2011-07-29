/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by getApplication()licable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

/**
 * @author Andres Almiray
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")
 
// check to see if we already have a ActivejdbcGriffonAddon
configText = '''root.'ActivejdbcGriffonAddon'.addon=true'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding ActivejdbcGriffonAddon to Builder.groovy'
    builderConfigFile.text += '\n' + configText + '\n'
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

if(!new File("${basedir}/griffon-app/conf/ActivejdbcConfig.groovy").exists()) {
   createArtifact(
      name: "ActivejdbcConfig",
      suffix: "",
      type: "ActivejdbcConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapActivejdbc.groovy").exists()) {
   createArtifact(
      name: "BootstrapActivejdbc",
      suffix: "",
      type: "BootstrapActivejdbc",
      path: "griffon-app/conf")
}

printFramed("""You may need to create an schema.ddl file depending on your settings.
If so, place it in griffon-app/resources.""")
