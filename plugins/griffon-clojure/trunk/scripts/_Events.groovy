includeTargets << griffonScript("Init")
includePluginScript("clojure", "_ClojureCommon")

eventSetClasspath = { classLoader ->
    if(compilingClojurePlugin()) return

    ant.fileset(dir: "${getPluginDirForName('clojure').file}/lib", includes: "*.jar").each {
        classLoader.addURL( jar.file.toURI().toURL() )
    }
}

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/griffon-${pluginName}-addon-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name: '_*.class')
            exclude(name: '*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('clojure').file}/lib", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if(compilingClojurePlugin()) return
    if(type != "source") return
    compileClojureSrc()
}

/**
 * Detects whether we're compiling the Clojure plugin itself
 */
private boolean compilingClojurePlugin() { getPluginDirForName("clojure") == null }