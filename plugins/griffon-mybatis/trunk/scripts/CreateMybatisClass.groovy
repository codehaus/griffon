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

target('createMybatisClass': 'Creates a new Mybatis class') {
    depends(checkVersion, parseArguments)

    ant.mkdir(dir: "${basedir}/src/mybatis")

    def type = 'MybatisClass'
    promptForName(type: type)

    def name = argsMap['params'][0]
    
    createArtifact(name: name,
        suffix: '',
        type: type,
        path: 'src/mybatis')
    def myPackageName = packageName
    def myClassName = className  
    def myPropertyName = propertyName  
    def mapperName = packageName + '.mappers.' + className + 'Mapper'  
        
    createArtifact(name: mapperName,
        suffix: '',
        type: 'Mapper',
        template: 'MybatisMapper',
        path: 'src/mybatis')
    argsMap.fileType = 'xml'
    allowDuplicate = true
    createArtifact(name: mapperName,
        suffix: '',
        type: 'XML Mapper',
        template: 'MybatisMapper',
        path: 'src/mybatis')
    ant.replace(dir: "${basedir}/src/mybatis") {
        replacefilter(token: "@artifact.package.name@", value: myPackageName)
        replacefilter(token: "@type.package@", value: myPackageName)
        replacefilter(token: "@type.name@", value: myClassName)
        replacefilter(token: "@type.property.name@", value: myPropertyName)
        replacefilter(token: "@table.name@", value: myClassName.toLowerCase())
    }
}
setDefaultTarget('createMybatisClass')