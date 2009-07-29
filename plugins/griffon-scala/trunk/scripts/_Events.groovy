import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

ant.property(environment: "env")
scalaHome = ant.antProject.properties."env.SCALA_HOME"

includeTargets << pluginScript("lang-bridge","CompileInterfaces")
//includePluginScript("lang-bridge","CompileInterfaces")

eventSetClasspath = { classLoader ->
    if( compilingScalaPlugin() ) return

    adjustScalaHome()
    ant.echo(message: "[scala] Using SCALA_HOME => ${scalaHome}")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL( jar.file.toURI().toURL() )
    }
}

eventCopyLibsEnd = { jardir ->
    if( compilingScalaPlugin() ) return
    adjustScalaHome()
    ant.echo(message: "[scala] Copying Scala jar files from ${scalaHome}/lib")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if( compilingScalaPlugin() ) return
    if( type != "source" ) return
    adjustScalaHome()
    def scalaSrc = "${basedir}/src/scala"
    if(!new File(scalaSrc).exists()) return

    compileInterfaces()
    def scalaDir = resolveResources("file:${scalaHome}/lib/*")
    if (!scalaDir) {
       ant.echo(message: "[scala] No Scala jar files found at ${scalaHome}")
       return
    }
    ant.path(id : "scalaJarSet") {
       fileset(dir: "${scalaHome}/lib" , includes : "*.jar")
    }
    ant.taskdef(resource: "scala/tools/ant/antlib.xml",
                classpathref: "scalaJarSet")
    ant.path( id: "scala.compile.classpath" ) {
       path(refid:"griffon.compile.classpath")
       path(refid:"scalaJarSet")
    }

    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    ant.echo(message: "[scala] Compiling Scala sources with SCALA_HOME=${scalaHome} to $classesDirPath")
    try {
        ant.scalac(destdir: classesDirPath,
                   classpathref: "scala.compile.classpath",
                   encoding: scalaSrcEncoding) {
            // joint compile java sources
            src(path: "${basedir}/src/main")
            src(path: scalaSrc)
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Scala sources: " + e.class.simpleName + ": " + e.message)
    }
}

/**
 * Detects whether we're compiling the scala plugin itself
 */
private boolean compilingScalaPlugin() { getPluginDirForName("scala") == null }

private void adjustScalaHome() {
    if( compilingScalaPlugin() ) return
    if( !scalaHome || (buildConfig.scala?.useBundledLibs) ) scalaHome = getPluginDirForName("scala").file
}

getPluginDirForName = { String pluginName ->
    // pluginsHome = griffonSettings.projectPluginsDir.path
    GriffonPluginUtils.getPluginDirForName(pluginsHome, pluginName)
}