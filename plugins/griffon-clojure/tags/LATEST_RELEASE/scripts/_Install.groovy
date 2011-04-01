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
if(!(config.flatten().'griffon.clojure.dynamicPropertyName')) {
    configFile.append('''
griffon.clojure.dynamicPropertyName = 'clj'
''')
}
if(!(config.flatten().'griffon.clojure.injectInto')) {
    configFile.append('''
griffon.clojure.injectInto = ['controller']
''')
}

ant.mkdir(dir: "${basedir}/src/clojure")
ant.mkdir(dir: "${basedir}/griffon-app/resources/clj")
// ant.mkdir(dir: "${basedir}/test/tap")

// check to see if we already have a ClojureGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'ClojureGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding ClojureGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'ClojureGriffonAddon'.addon=true
''')
}
