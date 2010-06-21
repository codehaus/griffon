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

if(!checkOptionIsSet(builderConfig, "griffon.builder.fx.FxBuilder")) {
    println 'Adding FxBuilder to Builder.groovy'
    builderConfigFile.append('''
fx.'griffon.builder.fx.FxBuilder'.view = '*'
''')
}
