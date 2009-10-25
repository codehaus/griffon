includeTargets << griffonScript("Init")
includePluginScript("clojure", "_ClojureCommon")

eventSetClasspath = { classLoader ->
    if(compilingClojurePlugin()) return

    ant.fileset(dir: "${getPluginDirForName('clojure').file}/lib", includes: "*.jar").each {
        classLoader.addURL(jar.file.toURI().toURL())
    }
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('clojure').file}/lib/", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
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

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == "src.commons"} ) {
        pathToInfo << [name: "Common Sources", path: "src.commons", filetype: [".groovy",".java"]]
    }
    // TODO -- match multiline comments -> (comment ...)
    if(!pathToInfo.find{ it.path == "src.clojure"} ) {
        def EMPTY = /^\s*$/
        pathToInfo << [name: "Clojure Sources", path: "src.clojure", filetype: [".clj"], locmatcher: {file ->
            def loc = 0
            file.eachLine { line ->
                if(line ==~ EMPTY || line ==~ /^\s*\;.*/) return
                loc++
            }
            loc
        }]
    }
}
