import groovy.xml.MarkupBuilder

includeTargets << griffonScript("Init")
includeTargets << griffonScript("_PluginDependencies")

target(default: "Update the application's Eclipse classpath file") {
    updateEclipseClasspathFile()
}

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
                classpathentry(kind: 'src', path: "${base}/${dir.name}")
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
            classpathentry(kind: 'var', path: "lib/${file.name}")
        }
        
        def nativeLibDir = new File("${basedir}/lib/${platform}")
        if(nativeLibDir.exists()) {
        mkp.yieldUnescaped("\n${indent}<!-- application native libs -->")
            nativeLibDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'var', path: "lib/${platform}/${file.name}")
            }
        }

        def pluginClasspathEntries = { libDir ->
            if(!libDir.exists()) return
            libDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'var', path: file.absolutePath)
            }
            def platformDir = new File(libDir.absolutePath, platform)
            if(!platformDir.exists()) return
            platformDir.eachFileMatch(~/.*\.jar/) { file ->
                classpathentry(kind: 'var', path: file.absolutePath)
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
