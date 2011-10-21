/*
 * Copyright 2010-2011 the original author or authors.
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

// check to see if we already have a BerkeleydbGriffonAddon
configText = '''root.'BerkeleydbGriffonAddon'.addon=true'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding BerkeleydbGriffonAddon to Builder.groovy'
    builderConfigFile.text += '\n' + configText + '\n'
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

if(!new File("${basedir}/griffon-app/conf/BerkeleydbConfig.groovy").exists()) {
   createArtifact(
      name: "BerkeleydbConfig",
      suffix: "",
      type: "BerkeleydbConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapBerkeleydb.groovy").exists()) {
   createArtifact(
      name: "BootstrapBerkeleydb",
      suffix: "",
      type: "BootstrapBerkeleydb",
      path: "griffon-app/conf")
}

