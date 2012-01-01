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

appToolkits = metadata.'app.toolkits'.split(',').toList() - 'charva'
if(appToolkits) {
    updateMetadata('app.toolkits': appToolkits.join(','))
} else {
    metadata.remove('app.toolkits')
    metadata.persist()
}

// check to see if we already have a CharvaGriffonAddon
boolean addonIsSet1
boolean builderIsSet1
builderConfig = configSlurper.parse(builderConfigFile.text)
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'CharvaGriffonAddon' == builder
        builderIsSet1 = builderIsSet1 || 'griffon.charva.CharvaBuilder' == builder
    }
}

if (builderIsSet1) {
    println 'Removing griffon.charva.CharvaBuilder from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'griffon.charva.CharvaBuilder'.view = '*'\n"
}
if (addonIsSet1) {
    println 'Removing CharvaGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'CharvaGriffonAddon'.addon=true\n"
}
