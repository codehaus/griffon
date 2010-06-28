/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

includeTargets << griffonScript('_GriffonCompile')

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        String libdir = "${getPluginDirForName('memcached').file}/lib/"
        ant.fileset(dir: libdir, includes: '*addon*').each {
            griffonCopyDist(it.toString(), jardir)
        }

        String connectorType = config.griffon?.memcached?.connector ?: 'java_memcached'
        switch(connectorType.toUpperCase()) {
            case 'SPYMEMCACHED':
                libdir = "${libdir}spymemcached/"
                break
            case 'XMEMCACHED':
                libdir = "${libdir}xmemcached/"
                break
        }

        ant.fileset(dir: libdir, includes: '*.jar').each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}

eventCompileEnd = {
    if(compilingMemcachedPlugin()) {
        ['spymemcached', 'xmemcached'].each { type ->
            ant.path(id: "${type}.classpath") {
                path(refid: 'griffon.compile.classpath')
                pathElement(location: classesDirPath)
                fileset(dir: "${basedir}/lib/${type}", includes: "*.jar")
            }
            compileSources(classesDirPath, "${type}.classpath") {
                src(path: "${basedir}/src/${type}")
                include(name:'**/*.groovy')
            }
        }
    }
}

private boolean compilingMemcachedPlugin() {
     def memcachedDir = getPluginDirForName('memcached')?.file?.canonicalPath
     memcachedDir == basedir
}
