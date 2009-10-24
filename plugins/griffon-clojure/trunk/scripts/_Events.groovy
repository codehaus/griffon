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
