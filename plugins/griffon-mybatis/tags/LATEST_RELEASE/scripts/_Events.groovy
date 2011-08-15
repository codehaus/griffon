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
 
def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('mybatis')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-mybatis-plugin', dirs: "${mybatisPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('mybatis', [
        conf: 'compile',
        name: 'griffon-mybatis-addon',
        group: 'org.codehaus.griffon.plugins',
        version: mybatisPluginVersion
    ])
}

eventCompileSourcesStart = {
    if(compilingPlugin('mybatis')) return
    additionalSources << "${basedir}/src/mybatis"
}

eventCompileSourcesEnd = {
    if(compilingPlugin('mybatis')) return
    ant.copy(todir: classesDirPath) {
        fileset(dir: "${basedir}/src/mybatis") {
            include(name: '**/*.xml')
        }
    }
    collectMappers()
}

collectMappers = {
    def mappers = []
    def searchPath = new File(basedir, 'src/mybatis')
    searchPath.eachFileRecurse { file ->
        def fixedPath = file.path - searchPath.canonicalPath
        if(file.isFile() && file.absolutePath.endsWith('.xml')) {
            def klass = fixedPath.substring(1).replace(File.separator, '.')
            klass = klass.substring(0, klass.lastIndexOf("."))
            mappers << klass
        }
    }

    if(mappers) {
        File mappersResourceDir = new File("${resourcesDirPath}/griffon-app/resources/META-INF/mybatis")
        mappersResourceDir.mkdirs()
        File mappersResourceFile = new File(mappersResourceDir, '/mappers.txt')
        mappersResourceFile.withPrintWriter { writer ->
            writer.println(mappers.join(','))
        }
    }
}