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

//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a Jide Builder
ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.jide.JideBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding JideBuilder to Builder.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.builder.jide.JideBuilder'.view = '*'
""")
}
