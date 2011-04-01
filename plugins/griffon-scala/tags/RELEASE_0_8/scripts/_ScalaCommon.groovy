/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0(the "License");
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

import org.codehaus.groovy.runtime.StackTraceUtils

includeTargets << griffonScript('_GriffonArgParsing')

target(name: 'compileScalaCommons', description: "", prehook: null, posthook: null) {
    depends(parseArguments)

    includePluginScript('lang-bridge', 'CompileCommons')
    compileCommons()

    ant.path(id: 'scala.compile.classpath') {
        path(refid: 'griffon.compile.classpath')
        pathElement(location: classesDirPath)
    }

    def commonsSrc = "${basedir}/src/commons-scala"
    ant.mkdir(dir: commonsSrc)
    def commonsSrcDir = new File(commonsSrc)
    compileSources(classesDir, 'scala.compile.classpath') {
        src(path: commonsSrcDir)
        javac(classpathref: 'scala.compile.classpath', debug: 'yes')
    }
}

target(name: 'compileScalaSrc', description: "", prehook: null, posthook: null) {
    depends(compileScalaCommons)

    def scalaSrc = "${basedir}/src/scala"
    ant.mkdir(dir: scalaSrc)
    def scalaSrcDir = new File(scalaSrc)
    def scalaArtifactSrc = resolveResources("file:/${basedir}/griffon-app/**/*.scala")

    if(!scalaArtifactSrc && !scalaSrcDir.list().size()) {
        ant.echo(message: "[scala] No Scala sources were found.")
        return
    }

    // def srcUptoDate1 = scalaSrcDir.exists() ? sourcesUpToDate(scalaSrc, classesDirPath, ".scala") : true

    defineScalaCompilePathAndTask()

    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    try {
        ant.scalac(destdir: classesDirPath,
                   logging: isDebugEnabled() ? 'verbose': 'none',
                   fork: true,
                   compilerpathref: 'scala.compile.classpath',
                   classpathref: 'scala.compile.classpath',
                   encoding: scalaSrcEncoding) {
            // joint compile java sources
            src(path: "${basedir}/src/main")
            src(path: scalaSrc)
            def excludedPaths = ['resources', 'i18n', 'conf'] // conf gets special handling
            for(dir in new File("${basedir}/griffon-app").listFiles()) {
                if(!excludedPaths.contains(dir.name) && dir.isDirectory() &&
                    resolveResources("file:/${dir}/**/*.scala"))
                    src(path: "$dir")
            }
        }
    } catch(Exception e) {
        if(argsMap.verboseCompile) {
            StackTraceUtils.deepSanitize(e)
            e.printStackTrace(System.err)
        }
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }
}
 
target(name: 'compileScalaTest', description: "", prehook: null, posthook: null) {
    def scalaTestSrc = "${basedir}/test/scalatest"
    def scalaTestDir = new File(scalaTestSrc)
    if(!scalaTestDir.exists() || !scalaTestDir.list().size()) {
        ant.echo(message: "[scala] No Scala tests sources were found.")
        return
    }

    def destDir = new File(griffonSettings.testClassesDir, "scalatest")
    ant.mkdir(dir: destDir)

    defineScalaTestPathAndTask()

    if(sourcesUpToDate(scalaTestSrc, destDir.absolutePath, ".scala")) return
    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    try {
        ant.scalac(destdir: destDir,
                   classpathref: "scala.test.classpath",
                   encoding: scalaSrcEncoding) {
            src(path: scalaTestSrc)
        }
    } catch(Exception e) {
        ant.fail(message: "Could not compile Scala tests: " + e.class.simpleName + ": " + e.message)
    }
}

target(name: 'compileScalaCheck', description: "", prehook: null, posthook: null) {
    def scalaCheckSrc = "${basedir}/test/scalacheck"
    def scalaCheckDir = new File(scalaCheckSrc)
    if(!scalaCheckDir.exists() || !scalaCheckDir.list().size()) {
        ant.echo(message: "[scala] No ScalaCheck tests sources were found.")
        return
    }

    def destDir = new File(griffonSettings.testClassesDir, "scalacheck")
    ant.mkdir(dir: destDir)

    defineScalaCheckPathAndTask()

    if(sourcesUpToDate(scalaCheckSrc, destDir.absolutePath, ".scala")) return
    def scalaSrcEncoding = buildConfig.scala?.src?.encoding ?: 'UTF-8'

    try {
        ant.scalac(destdir: destDir,
                   classpathref: "scala.check.classpath",
                   encoding: scalaSrcEncoding) {
            src(path: scalaCheckSrc)
        }
    } catch(Exception e) {
        ant.fail(message: "Could not compile ScalaCheck tests: " + e.class.simpleName + ": " + e.message)
    }
}

target(name: 'defineScalaCompilePathAndTask', description: "", prehook: null, posthook: null) {
    ant.taskdef(resource: 'scala/tools/ant/antlib.xml')
}

target(name: 'defineScalaTestPathAndTask', description: "", prehook: null, posthook: null) {
    defineScalaCompilePathAndTask()
    ant.path(id: "scala.test.classpath") {
        path(refid:"griffon.compile.classpath")
        fileset(dir: "${scalaPluginDir}/lib/test") {
            include(name: "*.jar")
        }
    }

    ant.taskdef(name: "scalatest",
                classpathref: "scala.test.classpath",
                classname: "org.scalatest.tools.ScalaTestAntTask")
}

target(name: 'defineScalaCheckPathAndTask', description: "", prehook: null, posthook: null) {
    defineScalaCompilePathAndTask()
    ant.path(id: "scala.check.classpath") {
        path(refid:"griffon.compile.classpath")
        fileset(dir: "${scalaPluginDir}/lib/check") {
            include(name: "*.jar")
        }
    }

    ant.taskdef(name: "scalacheck",
                classpathref: "scala.check.classpath",
                classname: "griffon.scalacheck.ScalacheckTask")
}
