import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("_GriffonBootstrap")
includePluginScript("clojure", "_ClojureCommon")

target(default: "Run Clojure REPL") {
    depends(checkVersion, configureProxy, packageApp, classpath)
    
    def cl = new GroovyClassLoader(classLoader)
    // File stagingdir = new File(jardir)
    // stagingdir.eachFileMatch(~/.*\.jar/) { f -> println "${f.class} $f"; cl.addURL(f.toURI().toURL()) }

    cl.loadClass("clojure.lang.Repl").main([] as String[])
/*
    // setup the vm
    if (!binding.variables.javaVM) {
        javaVM = [System.properties['java.home'], 'bin', 'java'].join(File.separator)
    }

    File stagingdir = new File(jardir)
    runtimeJars = []
    ant.fileset(dir:"${getPluginDirForName('clojure').file}/lib/repl", includes:"*.jar").each {
        runtimeJars << it.toString()
    }
    stagingdir.eachFileMatch(~/.*\.jar/) {f -> runtimeJars += f }
    def clojureClasspath = ([stagingdir.absolutePath] + runtimeJars).join(File.pathSeparator)
    
    // Process p = "$javaVM -cp $clojureClasspath $proxySettings jline.ConsoleRunner clojure.lang.Repl".execute(null as String[], stagingdir)
    Process p = "$javaVM -cp $clojureClasspath $proxySettings clojure.lang.Repl".execute(null as String[], stagingdir)

    // pipe the output
    p.consumeProcessOutput(System.out, System.err)

    // wait for it.... wait for it...
    p.waitFor()
*/
}
