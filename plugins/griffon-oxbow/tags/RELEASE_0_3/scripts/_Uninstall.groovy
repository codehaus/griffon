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
 * @author Ixchel Ruiz
 */

// check to see if we already have a OxbowGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'OxbowGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing OxbowGriffonAddon from Builder.groovy'
    builderConfigFile.text -= "root.'OxbowGriffonAddon'.addon=true\n"
    builderConfigFile.text -= "root.'OxbowGriffonAddon'.controller=['ask','choice','error','inform','showException','radioChoice','warn']\n"
}