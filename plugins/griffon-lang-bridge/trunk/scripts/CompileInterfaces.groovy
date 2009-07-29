/*
 * Copyright 2009 the original author or authors.
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
 * Compiles sources under ${basedir}/src/interfaces
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("Compile")

target(compileInterfaces: "Compile interfaces") {
    depends(checkVersion, clean, parseArguments, classpath)
    def interfaces = "${basedir}/src/interfaces"
    def interfacesdir = new File(interfaces)
    if(!interfacesdir.exists()) return

    ant.mkdir(dir: classesDirPath)
    ant.echo(message: "Compiling interface sources to $classesDirPath")
    try {
        String classpathId = "griffon.compile.classpath"
        ant.groovyc(destdir: classesDirPath,
                    classpathref: classpathId) {
            src(path: interfaces)
            javac(classpathref: classpathId)
        }
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        ant.fail(message: "Could not compile interface sources: " + e.class.simpleName + ": " + e.message)
    }
}

setDefaultTarget(compileInterfaces)