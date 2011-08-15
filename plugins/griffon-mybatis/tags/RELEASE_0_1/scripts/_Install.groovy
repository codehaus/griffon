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
 
// check to see if we already have a MybatisGriffonAddon
configText = '''root.'MybatisGriffonAddon'.addon=true'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding MybatisGriffonAddon to Builder.groovy'
    builderConfigFile.text += '\n' + configText + '\n'
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

if(!new File("${basedir}/griffon-app/conf/MybatisConfig.groovy").exists()) {
   createArtifact(
      name: "MybatisConfig",
      suffix: "",
      type: "MybatisConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapMybatis.groovy").exists()) {
   createArtifact(
      name: "BootstrapMybatis",
      suffix: "",
      type: "BootstrapMybatis",
      path: "griffon-app/conf")
}

ant.mkdir(dir: "${basedir}/src/mybatis")