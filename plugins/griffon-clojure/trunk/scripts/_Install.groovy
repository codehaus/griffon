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

// append hints for config options if not present
appConfig = configSlurper1.parse(new File("$basedir/griffon-app/conf/Application.groovy").toURL())
if(!(appConfig.flatten().'griffon.clojure.dynamicPropertyName')) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.dynamicPropertyName = "clj"
""")
}
if(!(appConfig.flatten().'griffon.clojure.injectInto')) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.injectInto = ["controller"]
""")
}

ant.mkdir(dir: "${basedir}/src/clojure")
ant.mkdir(dir: "${basedir}/griffon-app/resources/clj")
ant.mkdir(dir: "${basedir}/test/tap")

// check to see if we already have a ClojureGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'ClojureGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding ClojureGriffonAddon to Builder.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'ClojureGriffonAddon'.addon=true
''')
}
