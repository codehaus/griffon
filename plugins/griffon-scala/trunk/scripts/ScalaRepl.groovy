import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils
import scala.tools.nsc.MainGenericRunner

includeTargets << griffonScript("_GriffonBootstrap")
includePluginScript("scala", "_ScalaCommon")

target(default: "Run Scala REPL") {
    depends(checkVersion, configureProxy, packageApp, classpath, adjustScalaHome)
    ant.echo(message: "[scala] Using SCALA_HOME => ${scalaHome}")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL(jar.file.toURI().toURL())
    }

    File stagingdir = new File(jardir)
    runtimeJars = []
    stagingdir.eachFileMatch(~/.*\.jar/) {f -> runtimeJars += f }
    def scalaClasspath = ([stagingdir.absolutePath] + runtimeJars).join(File.pathSeparator)

    MainGenericRunner.main(["-cp", scalaClasspath] as String[])
}
