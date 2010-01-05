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
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

ant.property(environment: "env")
ant.mkdir(dir: "${basedir}/src/javafx")

def checkOptionIsSet = { where, option ->
   boolean optionIsSet = false
   where.each { prefix, v ->
       v.each { key, views ->
           optionIsSet = optionIsSet || option == key
       }
   }
   optionIsSet
}

ConfigSlurper configSlurper = new ConfigSlurper()

builderConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
if(!checkOptionIsSet(builderConfig, "griffon.builder.fx.FxBuilder")) {
    println 'Adding FxBuilder to Builder.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
fx.'griffon.builder.fx.FxBuilder'.view = '*'
""")
}

/*
if(!checkOptionIsSet(builderConfig, "griffon.javafx.ApplicationBuilder")) {
    println 'Adding FxBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
fx.'griffon.javafx.ApplicationBuilder'.view = '*'
""")
}
*/
