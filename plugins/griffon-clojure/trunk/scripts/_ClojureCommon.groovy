import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includePluginScript("lang-bridge", "CompileCommons")

target(compileClojureSrc: "") {
    compileCommons()
    def clojuresrc = "${basedir}/src/clojure"
    def clojuresrcdir = new File(clojuresrc)
    if(!clojuresrcdir.exists() || !clojuresrcdir.list().size()) {
        ant.echo(message: "[clojure] No Clojure sources were found.")
        return
    }

    if(sourcesUpToDate("${basedir}/src/clojure", classesDirPath, ".clj")) return

    ant.echo(message: "[clojure] Compiling Clojure sources to $classesDirPath")
    try {
        convertNamespacePath(clojuresrc, "clojure.compile.namespaces")
        defineClojureCompilePath(clojuresrc, classesDirPath)
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
    def clojuretest = "${basedir}/test/clojure"
    def clojuretestdir = new File(clojuretest)
    if(!clojuretestdir.exists() || !clojuretestdir.list().size()) {
        ant.echo(message: "[clojure] No Clojure tests sources were found.")
        return
    }

    def destdir = new File(griffonSettings.testClassesDir, "clojure")
    ant.mkdir(dir: destdir)

    if(sourcesUpToDate(clojuretest, destdir.absolutePath, ".clj")) return

    ant.echo(message: "[clojure] Compiling Clojure test sources to $destdir")
    try {
        convertNamespacePath(clojuresrc, "clojure.test.namespaces")
        defineClojureTestPath(clojuretest, destdir)
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

private boolean compilingClojurePlugin() { getPluginDirForName("clojure") == null }