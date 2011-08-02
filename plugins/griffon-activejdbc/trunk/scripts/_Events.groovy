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
    if(compilingPlugin('activejdbc')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-activejdbc-plugin', dirs: "${activejdbcPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('activejdbc', [
        conf: 'compile',
        name: 'griffon-activejdbc-addon',
        group: 'org.codehaus.griffon.plugins',
        version: activejdbcPluginVersion
    ])
}

eventCopyLibsEnd = { jardir ->
    new File(jardir).eachFileMatch(~/.*activejdbc-instrumentation.*|.*javassist.*/) { jar ->
        ant.delete(file: jar, failonerror: false, quiet: true)
    }
}
