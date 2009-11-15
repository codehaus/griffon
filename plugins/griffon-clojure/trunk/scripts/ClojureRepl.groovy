import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("_GriffonBootstrap")
includePluginScript("clojure", "_ClojureCommon")

target(default: "Run Clojure REPL") {
    depends(checkVersion, configureProxy, packageApp, classpath)

    ant.fileset(dir: "${clojurePluginDir}/lib/repl/", includes:"*.jar").each { jar ->
        classLoader.addURL(jar.file.toURI().toURL())
    }
    
    classLoader.parent.addURL(classesDir.toURI().toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/resources/".toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/i18n/".toURL())

    // classLoader.loadClass("clojure.lang.Repl").main([] as String[])
    classLoader.loadClass("jline.ConsoleRunner").main(["clojure.lang.Repl"] as String[])
}
