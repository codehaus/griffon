import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils
import clojure.lang.Repl

includeTargets << griffonScript("_GriffonBootstrap")
includePluginScript("clojure", "_ClojureCommon")

target(default: "Run Clojure REPL") {
    depends(checkVersion, configureProxy, packageApp, classpath)

    File stagingdir = new File(jardir)
    runtimeJars = []
    stagingdir.eachFileMatch(~/.*\.jar/) {f -> runtimeJars += f }
    def clojureClasspath = ([stagingdir.absolutePath] + runtimeJars).join(File.pathSeparator)

    classLoader.loadClass("clojure.lang.Repl").main([] as String[])
}
