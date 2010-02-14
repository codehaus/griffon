/*
 * Copyright 2010 the original author or authors.
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

//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a Neo4jGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'Neo4jGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding Neo4jGriffonAddon to Builder.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'Neo4jGriffonAddon'.addon=true
''')
}

appConfig = configSlurper1.parse(new File("$basedir/griffon-app/conf/Application.groovy").toURL())
if(!(appConfig.flatten().'griffon.neo4j.injectInto')) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.neo4j.injectInto = ['controller']
""")
}

if(!new File("${basedir}/griffon-app/conf/Neo4jConfig.groovy").exists()) {
   createArtifact(
      name: "Neo4jConfig",
      suffix: "",
      type: "Neo4jConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapNeo4j.groovy").exists()) {
   createArtifact(
      name: "BootstrapNeo4j",
      suffix: "",
      type: "BootstrapNeo4j",
      path: "griffon-app/conf")
}

