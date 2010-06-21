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

includePluginScript("lang-bridge", "CompileCommons")

target(compileClojureSrc: "") {
    compileCommons()
    def clojureSrc = "${basedir}/src/clojure"
    def clojureSrcDir = new File(clojureSrc)
    if(!clojureSrcDir.exists()) return
    if(!hasSourcesOfType(clojureSrc, ".clj")) {
        ant.echo(message: "[clojure] No Clojure sources were found.")
        return
    }

    if(sourcesUpToDate("${basedir}/src/clojure", classesDirPath, ".clj")) return

    ant.echo(message: "[clojure] Compiling Clojure sources to $classesDirPath")
    ant.mkdir(dir: classesDirPath)
    try {
        convertNamespacePath(clojureSrc, "clojure.compile.namespaces")
        defineClojureCompilePath(clojureSrc, classesDirPath)
        ant.java(classname: "clojure.lang.Compile",
                 classpathref: "clojure.compile.classpath") {
            sysproperty(key: "clojure.compile.path", value: classesDirPath)
            arg(line: ant.antProject.properties."clojure.compile.namespaces")
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Clojure sources: " + e.class.simpleName + ": " + e.message)
    }
}

target(compileClojureTest: "") {
    def clojureTest = "${basedir}/test/clojure"
    def clojureTestDir = new File(clojureTest)
    if(!clojureTestDir.exists()) return
    if(!hasSourcesOfType(clojureTest, ".clj")) {
        ant.echo(message: "[clojure] No Clojure tests sources were found.")
        return
    }

    def destdir = new File(griffonSettings.testClassesDir, "clojure")
    ant.mkdir(dir: destdir)

    if(sourcesUpToDate(clojureTest, destdir.absolutePath, ".clj")) return

    ant.echo(message: "[clojure] Compiling Clojure test sources to $destdir")
    try {
        convertNamespacePath(clojureSrc, "clojure.test.namespaces")
        defineClojureTestPath(clojureTest, destdir)
        ant.java(classname: "clojure.lang.Compile",
                 classpathref: "clojure.test.classpath") {
            sysproperty(key: "clojure.compile.path", value: destdir)
            arg(line: ant.antProject.properties."clojure.test.namespaces")
        }
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Clojure test sources: " + e.class.simpleName + ": " + e.message)
    }
}

defineClojureCompilePath = { srcdir, destdir ->
    ant.path(id: "clojure.compile.classpath") {
        path(refid: "griffon.compile.classpath")
        fileset(dir: "${getPluginDirForName('clojure').file}/lib", includes: "*.jar")
        path(location: destdir)
        path(location: srcdir)
    }
}

defineClojureTestPath = { srcdir, destdir ->
    ant.path(id: "clojure.test.classpath") {
        path(refid: "clojure.compile.classpath")
        path(location: destdir)
        path(location: srcdir)
    }
}

convertNamespacePath = { srcdir, pathProperty ->
    ant.pathconvert(pathsep: "", property: pathProperty) {
        fileset(dir: srcdir, includes: "**/*.clj")
        chainedmapper {
            packagemapper(from: "${srcdir}/*.clj", to: "*")
            filtermapper {
                replacestring(from: "_", to: "-")
            }
        }
    }
}

private boolean compilingClojurePlugin() { getPluginDirForName("clojure") == basedir }
