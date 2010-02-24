/*
    griffon-neo4j plugin
    Copyright (C) 2010 Andres Almiray

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

