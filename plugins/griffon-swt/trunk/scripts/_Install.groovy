/*
 * Copyright 2009-2011 the original author or authors.
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

appToolkits = metadata.'app.toolkits'
if(!appToolkits) appToolkits = ''
appToolkits = appToolkits.split(',').toList()
updateMetadata('app.toolkits': 'swt')

// check to see if we already have a SwtGriffonAddon
boolean addonIsSet1
boolean builderIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'SwtGriffonAddon' == builder
        builderIsSet1 = builderIsSet1 || 'groovy.swt.SwtBuilder' == builder
    }
}

if (!builderIsSet1) {
    println 'Adding groovy.swt.SwtBuilder to Builder.groovy'
    builderConfigFile.append('''
root.'groovy.swt.SwtBuilder'.view = '*'
''')
}
if (!addonIsSet1) {
    println 'Adding SwtGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'SwtGriffonAddon'.addon=true
''')
}
