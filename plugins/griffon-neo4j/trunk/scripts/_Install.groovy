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

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a Neo4jGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'Neo4jGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding Neo4jGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'Neo4jGriffonAddon'.addon=true
''')
}

if(!(config.flatten().'griffon.neo4j.injectInto')) {
    configFile.append('''
griffon.neo4j.injectInto = ['controller']
''')
}

argsMap = argsMap ?: [:]
argsMap.skipPackagePrompt = true

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

