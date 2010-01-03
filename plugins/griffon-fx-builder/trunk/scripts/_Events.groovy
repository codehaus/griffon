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
javafxHome = ant.antProject.properties."env.JAVAFX_HOME"

includeTargets << griffonScript("Package")
includePluginScript("lang-bridge", "CompileCommons")
javaFxUrl = "http://dl.javafx.com/1.2/javafx-rt.jnlp"

eventCopyLibsEnd = { jardir ->
    if( compilingJavaFXPlugin() ) return
    verifyJavaFXHome()
    ant.echo(message: "[fx] Copying FX jar files from ${getPluginDirForName('fx').file}/lib")

    ant.fileset(dir:"${getPluginDirForName('fx').file}/lib/", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }

    if(!config.griffon.extensions.jnlpUrls.contains(javaFxUrl)) {
        config.griffon.extensions.jnlpUrls << javaFxUrl
    }
}

eventCompileStart = { type ->
    verifyJavaFXHome()

    def javafxlibs = ant.fileset(dir: "${javafxHome}/lib/shared", includes: "*.jar")
    ant.project.references["griffon.compile.classpath"].addFileset(javafxlibs)
    javafxlibs = ant.fileset(dir: "${javafxHome}/lib/desktop", excludes: "*rt15.jar, *.so")
    ant.project.references["griffon.compile.classpath"].addFileset(javafxlibs)

    if( compilingJavaFXPlugin() ) return

    ant.taskdef(resource: "javafxc-ant-task.properties",
                classpathref: "griffon.compile.classpath")

    ant.property(name: "javafx.compiler.classpath", refid: "griffon.compile.classpath")
    def javafxCompilerClasspath = ant.antProject.properties.'javafx.compiler.classpath'


    if( type != "source" ) return
    def javafxSrc = "${basedir}/src/javafx"
    if(!new File(javafxSrc).exists()) return
    compileCommons()

    if(sourcesUpToDate(javafxSrc, classesDirPath, ".fx")) return
    def javafxSrcEncoding = buildConfig.javafx?.src?.encoding ?: 'UTF-8'

    ant.echo(message: "[fx] Compiling JavaFX sources with JAVAFX_HOME=${javafxHome} to $classesDirPath")
    try {
        ant.javafxc(destdir: classesDirPath,
                    classpathref: "griffon.compile.classpath",
                    compilerclasspath: javafxCompilerClasspath,
                    encoding: javafxSrcEncoding,
                    srcdir: javafxSrc)
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile JavaFX sources: " + e.class.simpleName + ": " + e.message)
    }
}

eventRunAppStart = {
    ant.echo(message: "[fx] Copying additional JavaFX jars to $jardir")
    ant.fileset(dir: "${javafxHome}/lib/shared", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
    ant.fileset(dir: "${javafxHome}/lib/desktop", excludes: "*rt15.jar, *.so").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventRunAppEnd = {
    ant.echo(message: "[fx] Deleting $jardir")
    ant.delete(dir: jardir)
}

/**
 * Detects whether we're compiling the fx plugin itself
 */
private boolean compilingJavaFXPlugin() { getPluginDirForName("fx") == null }

private void verifyJavaFXHome() {
    if( compilingJavaFXPlugin() ) return
    if( !javafxHome ) {
       ant.fail(message: "  [griffon-fx] environment variable $JAVAFX_HOME is undefined")
    }
}
