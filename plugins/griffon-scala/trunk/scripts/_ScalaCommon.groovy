/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

ant.property(environment: "env")
scalaHome = ant.antProject.properties."env.SCALA_HOME"

target(compileScalaSrc: "") {
    adjustScalaHome()
    def scalaSrc = "${basedir}/src/scala"
    def scalaSrcDir = new File(scalaSrc)
    if(!scalaSrcDir.exists() || !scalaSrcDir.list().size()) {
        ant.echo(message: "[scala] No Scala sources were found.")
        return
    }

    includePluginScript("lang-bridge", "CompileCommons")
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
    def scalaTestSrc = "${basedir}/test/scalatest"
    def scalaTestDir = new File(scalaTestSrc)
    if(!scalaTestDir.exists() || !scalaTestDir.list().size()) {
        ant.echo(message: "[scala] No Scala tests sources were found.")
        return
    }

    def destDir = new File(griffonSettings.testClassesDir, "scalatest")
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
            src(path: scalaTestSrc)
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Scala tests: " + e.class.simpleName + ": " + e.message)
    }
}

target(compileScalaCheck: "") {
    adjustScalaHome()
    def scalaCheckSrc = "${basedir}/test/scalacheck"
    def scalaCheckDir = new File(scalaCheckSrc)
    if(!scalaCheckDir.exists() || !scalaCheckDir.list().size()) {
        ant.echo(message: "[scala] No ScalaCheck tests sources were found.")
        return
    }

    def destDir = new File(griffonSettings.testClassesDir, "scalacheck")
    ant.mkdir(dir: destDir)

    def scalaDir = resolveResources("file:${scalaHome}/lib/*")
    if (!scalaDir) {
       ant.echo(message: "[scala] No Scala jar files found at ${scalaHome}")
       return
    }
    defineScalaCheckPathAndTask()

    if(sourcesUpToDate(scalaCheckSrc, destDir.absolutePath, ".scala")) return
    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    ant.echo(message: "[scala] Compiling ScalaCheck tests with SCALA_HOME=${scalaHome} to $destDir")
    try {
        ant.scalac(destdir: destDir,
                   classpathref: "scala.check.classpath",
                   encoding: scalaSrcEncoding) {
            src(path: scalaCheckSrc)
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile ScalaCheck tests: " + e.class.simpleName + ": " + e.message)
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

target(defineScalaCheckPathAndTask: "") {
    defineScalaCompilePathAndTask()
    ant.path(id: "scala.check.classpath") {
        path(refid:"scala.compile.classpath")
        fileset(dir: "${scalaPluginDir}/lib/check") {
            include(name: "*.jar")
        }
    }

    ant.taskdef(name: "scalacheck",
                classpathref: "scala.check.classpath",
                classname: "griffon.scalacheck.ScalacheckTask")
}

target(adjustScalaHome: "") {
    if( compilingScalaPlugin() ) return
    if( !scalaHome || (buildConfig.scala?.useBundledLibs) ) scalaHome = getPluginDirForName("scala").file
}

private boolean compilingScalaPlugin() { getPluginDirForName("scala") == null }
