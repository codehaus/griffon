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

includeTargets << griffonScript("_GriffonCompile")

eventCompileEnd = {
    def spring = "${basedir}/src/spring"
    def springdir = new File(spring)
    if(!springdir.exists()) return

    ant.mkdir(dir: classesDirPath)

    if(sourcesUpToDate("${basedir}/src/spring", classesDirPath, ".groovy")) return

    ant.echo(message: "Compiling Spring resources to $classesDirPath")
    String classpathId = "griffon.compile.classpath"
    compileSources(classesDirPath, classpathId) {
        src(path: spring)
        javac(classpathref: classpathId)
    }
}

sourcesUpToDate = { src, dest, srcsfx = ".java", destsfx = ".class" ->
    def srcdir = new File(src)
    def destdir = new File(dest)
    def skipIt = new RuntimeException()
    try {
        srcdir.eachFileRecurse { sf ->
            if(sf.isDirectory()) return
            if(!sf.toString().endsWith(srcsfx)) return
            def target = new File(destdir.toString() + sf.toString().substring(srcdir.toString().length()) - srcsfx + destsfx)
            if(!target.exists()) throw skipIt
            if(sf.lastModified() > target.lastModified()) throw skipIt
        }
    } catch(x) {
       if(x == skipIt) return false
       throw x
    }
    return true
}

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('spring')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-spring-plugin', dirs: "${springPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('spring', [
        conf: 'compile',
        name: 'griffon-spring-addon',
        group: 'org.codehaus.griffon.plugins',
        version: springPluginVersion
    ])
}