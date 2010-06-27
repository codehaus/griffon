/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

import groovy.xml.MarkupBuilder

includeTargets << griffonScript("Init")
includeTargets << griffonScript("_PluginDependencies")

target(updateEclipseClasspath: "Update the application's Eclipse classpath file") {
    updateEclipseClasspathFile()
}
setDefaultTarget('updateEclipseClasspath')

updateEclipseClasspathFile = { newPlugin = null ->
    println "Updating Eclipse classpath file..."

    String indent = '    '
    def writer = new PrintWriter(new FileWriter('.classpath'))
    def xml = new MarkupBuilder(new IndentPrinter(writer, indent))
    xml.setDoubleQuotes(true)
    xml.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
    xml.mkp.comment("Auto generated on ${new Date()}")
    xml.mkp.yieldUnescaped '\n'
    xml.classpath {
        mkp.yieldUnescaped("\n${indent}<!-- source paths -->")
        ['griffon-app', 'src', 'test'].each { base ->
            new File(base).eachDir { dir ->
                if (! (dir.name =~ /^\..+/) ) {
                    classpathentry(kind: 'src', path: "${base}/${dir.name}")
                }
            }
        }
        mkp.yieldUnescaped("\n${indent}<!-- output paths -->")
        classpathentry(kind: 'con', path: 'org.eclipse.jdt.launching.JRE_CONTAINER')
        classpathentry(kind: 'output', path: 'staging/classes')
        mkp.yieldUnescaped("\n${indent}<!-- griffon dist libs -->")
        (new File("${griffonHome}/dist")).eachFileMatch(~/^griffon-.*\.jar/) { file ->
            classpathentry(kind: 'var', path: "GRIFFON_HOME/dist/${file.name}")
        }
        mkp.yieldUnescaped("\n${indent}<!-- griffon libs -->")
        (new File("${griffonHome}/lib")).eachFileMatch(~/.*\.jar/) { file ->
            classpathentry(kind: 'var', path: "GRIFFON_HOME/lib/${file.name}")
        }
        mkp.yieldUnescaped("\n${indent}<!-- application libs -->")
        (new File("${basedir}/lib")).eachFileMatch(~/.*\.jar/) { file ->
            classpathentry(kind: 'lib', path: "lib/${file.name}")
        }
        
        def nativeLibDir = new File("${basedir}/lib/${platform}")
        if(nativeLibDir.exists()) {
        mkp.yieldUnescaped("\n${indent}<!-- application native libs -->")
            nativeLibDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'lib', path: "lib/${platform}/${file.name}")
            }
        }

        def pluginClasspathEntries = { libDir ->
            if(!libDir.exists()) return
            libDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'lib', path: file.absolutePath)
            }
            def platformDir = new File(libDir.absolutePath, platform)
            if(!platformDir.exists()) return
            platformDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'lib', path: file.absolutePath)
            }
        }

        mkp.yieldUnescaped("\n${indent}<!-- plugin libs -->")
        getPluginDirectories().each { pluginDir ->
            if(pluginDir.file.name == newPlugin) return
            def libDir = new File(pluginDir.file.absolutePath, 'lib')
            pluginClasspathEntries(libDir)
        }
        if(newPlugin) {
            def libDir = new File([pluginsHome, newPlugin, 'lib'].join(File.separator))
            pluginClasspathEntries(libDir)
        }
    }
}
