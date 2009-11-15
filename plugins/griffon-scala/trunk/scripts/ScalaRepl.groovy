import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("Compile")
includePluginScript("scala", "_ScalaCommon")

target(default: "Run Scala REPL") {
    depends(checkVersion, configureProxy, compile, classpath, adjustScalaHome)
    ant.echo(message: "[scala] Using SCALA_HOME => ${scalaHome}")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL(jar.file.toURI().toURL())
    }
    classLoader.parent.addURL(classesDir.toURI().toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/resources/".toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/i18n/".toURL())

    def scalaClasspath = classLoader.getURLs().collect([]){ it.toString() }
    classLoader.parent.getURLs().collect(scalaClasspath){ it.toString() }

    classLoader.loadClass("scala.tools.nsc.MainGenericRunner").main(["-cp", scalaClasspath.join(File.pathSeparator)] as String[])
}
