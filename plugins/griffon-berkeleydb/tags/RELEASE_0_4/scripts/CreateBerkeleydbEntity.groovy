/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by getApplication()licable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

/**
 * @author Andres Almiray
 */

includeTargets << griffonScript('_GriffonInit')
includeTargets << griffonScript('_GriffonCreateArtifacts')

target('createBerkeleydbEntity': 'Creates a new Berkeleydb entity class') {
    depends(checkVersion, parseArguments)

    def type = 'BerkeleydbEntity'
    promptForName(type: type)

    def name = argsMap['params'][0]
    
    createArtifact(name: name,
        suffix: '',
        type: type,
        path: 'src/main')
    def myClassName = className  
    def myPropertyName = propertyName  
        
    ant.replace(dir: "${basedir}/src/main") {
        replacefilter(token: "@type.name@", value: myClassName)
        replacefilter(token: "@type.property.name@", value: myPropertyName)
    }
}
setDefaultTarget('createBerkeleydbEntity')
