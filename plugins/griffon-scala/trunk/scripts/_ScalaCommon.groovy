import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

ant.property(environment: "env")
scalaHome = ant.antProject.properties."env.SCALA_HOME"

includePluginScript("lang-bridge", "CompileCommons")

/*
eventSetClasspath = { classLoader ->
    if( compilingScalaPlugin() ) return

    adjustScalaHome()
    ant.echo(message: "[scala] Using SCALA_HOME => ${scalaHome}")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL( jar.file.toURI().toURL() )
    }
}
*/

/*
eventCopyLibsEnd = { jardir ->
    if( compilingScalaPlugin() ) return
    adjustScalaHome()
    ant.echo(message: "[scala] Copying Scala jar files from ${scalaHome}/lib")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
*/

target(compileScalaSrc: "") {
    adjustScalaHome()
    def scalaSrc = "${basedir}/src/scala"
    if(!new File(scalaSrc).exists()) return

    compileCommons()
    if(sourcesUpToDate(scalaSrc, classesDirPath, ".scala")) return

    def scalaDir = resolveResources("file:${scalaHome}/lib/*")
    if (!scalaDir) {
       ant.echo(message: "[scala] No Scala jar files found at ${scalaHome}")
       return
    }
    defineScalaCompilePathAndTask()

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
 
target(compileScalaTest: "") {
    adjustScalaHome()
    def scalaTestSrc = "${basedir}/test/scala"
    if(!new File(scalaTestSrc).exists()) return

    def destDir = new File(griffonSettings.testClassesDir, "scala")
    ant.mkdir(dir: destDir)

    def scalaDir = resolveResources("file:${scalaHome}/lib/*")
    if (!scalaDir) {
       ant.echo(message: "[scala] No Scala jar files found at ${scalaHome}")
       return
    }
    defineScalaTestPathAndTask()

    if(sourcesUpToDate(scalaTestSrc, destDir.absolutePath, ".scala")) return
    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    ant.echo(message: "[scala] Compiling Scala tests with SCALA_HOME=${scalaHome} to $destDir")
    try {
        ant.scalac(destdir: destDir,
                   classpathref: "scala.test.classpath",
                   encoding: scalaSrcEncoding) {
            // joint compile java sources
            // src(path: "${basedir}/src/main")
            src(path: scalaTestSrc)
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Scala tests: " + e.class.simpleName + ": " + e.message)
    }
}
 
target(defineScalaCompilePathAndTask: "") {
    ant.path(id : "scalaJarSet") {
       fileset(dir: "${scalaHome}/lib", includes: "*.jar")
    }
    ant.taskdef(resource: "scala/tools/ant/antlib.xml",
                classpathref: "scalaJarSet")
    ant.path(id: "scala.compile.classpath" ) {
       path(refid:"griffon.compile.classpath")
       path(refid:"scalaJarSet")
    }
}

target(defineScalaTestPathAndTask: "") {
    defineScalaCompilePathAndTask()
    ant.path(id: "scala.test.classpath") {
        path(refid:"scala.compile.classpath")
        fileset(dir: "${scalaPluginDir}/lib/test") {
            include(name: "*.jar")
        }
    }

    ant.taskdef(name: "scalatest",
                classpathref: "scala.test.classpath",
                classname: "org.scalatest.tools.ScalaTestAntTask")
}

private boolean compilingScalaPlugin() { getPluginDirForName("scala") == null }

private void adjustScalaHome() {
    if( compilingScalaPlugin() ) return
    if( !scalaHome || (buildConfig.scala?.useBundledLibs) ) scalaHome = getPluginDirForName("scala").file
}
