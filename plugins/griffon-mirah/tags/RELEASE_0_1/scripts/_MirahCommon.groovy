/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0(the "License");
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

import org.codehaus.groovy.runtime.StackTraceUtils
// import org.mirah.MirahCommand

includeTargets << griffonScript('_GriffonArgParsing')

target(name: 'compileMirahSrc', description: "", prehook: null, posthook: null) {
    depends(parseArguments)

    includePluginScript('lang-bridge', 'CompileCommons')
    compileCommons()

    File mirahSrc = new File("${basedir}/src/mirah")
    ant.mkdir(dir: mirahSrc)
    def mirahSources = resolveResources("file:/${mirahSrc}/**/*.mirah")
    def mirahArtifactSources = resolveResources("file:/${basedir}/griffon-app/**/*.mirah")

    if(!mirahSources && !mirahArtifactSources) {
        ant.echo(message: "[mirah] No Mirah sources were found.")
        return
    }

    defineMirahCompilePathAndTask()

    classpathId = 'mirah.compile.classpath'
    if(argsMap.compileTrace) {
        println('-'*80)
        println "[GRIFFON] compiling to ${classesDirPath}"
        println "[GRIFFON] '${classpathId}' entries"
        ant.project.getReference(classpathId).list().each{println("  $it")}
        println('-'*80)
    }

    try {
        def compileMirah = { srcdir, sources ->
            if(sourcesUpToDate(srcdir, classesDirPath, '.mirah')) return
            
            sources = sources.collect { file -> 
                file -= srcdir
                file.startsWith(File.separator) ? file[1..-1] : file
            }
            srcdir -= basedir
            srcdir = srcdir.startsWith(File.separator) ? srcdir[1..-1] : srcdir 
            ant.java(classpathref: classpathId, classname: 'org.mirah.MirahCommand') {
                arg(value: 'compile')
                arg(value: '-d')
                arg(value: classesDirPath)
                arg(value: '-c')
                arg(value: ant.project.getReference(classpathId).list().join(File.pathSeparator))
                arg(value: '--cd')
                arg(value: srcdir)
                sources.each { arg(value: it) }
            }
            /*
            List cargs = ['-d', classesDirPath, '-c']
            cargs << ant.project.getReference(classpathId).list().join(File.pathSeparator)
            cargs << '--cd'
            cargs << srcdir
            sources.each { cargs << it }
            println cargs.join(' ')
            MirahCommand.compile(cargs)
            */
        }
        
        if(mirahSources) compileMirah(mirahSrc.absolutePath, mirahSources.file.absolutePath)
        def excludedPaths = ['resources', 'i18n', 'conf']
        for(dir in new File("${basedir}/griffon-app").listFiles()) {
            def sources = resolveResources("file:/${dir}/**/*.mirah")
            if(!excludedPaths.contains(dir.name) && dir.isDirectory() && sources)
                compileMirah(dir.absolutePath, sources.file.absolutePath)
        }
    } catch(Exception e) {
        if(argsMap.verboseCompile) {
            StackTraceUtils.deepSanitize(e)
            e.printStackTrace(System.err)
        }
        event('StatusFinal', ["Compilation error: ${e.message}"])
        exit(1)
    }
}

target(name: 'defineMirahCompilePathAndTask', description: "", prehook: null, posthook: null) {
    ant.path(id: 'mirah.compile.classpath') {
        path(refid: 'griffon.compile.classpath')
        pathElement(location: classesDirPath)
        griffonSettings.buildDependencies.each { jar ->
            pathElement(location: jar.absolutePath)
        }
    }
}
