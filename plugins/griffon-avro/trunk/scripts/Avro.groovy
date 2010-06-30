/*
 * Copyright 2010 the original author or authors.
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

includeTargets << griffonScript('Init')
includeTargets << griffonScript('_GriffonCompile')

target(avro: 'Compile Avro sources') {
    depends(checkVersion, classpath)
    gensrcDir = "${projectWorkDir}/avro"
    gensrcDirPath = new File(gensrcDir)
    gensrcDirPath.mkdirs()
    gensrcDirPath = new File(gensrcDir, 'gen-java')
    gensrcDirPath.mkdirs()

    avrosrc = "${basedir}/src/avro"
    avrosrcDir = new File(avrosrc)
    if(!avrosrcDir.list().size()) {
        ant.echo(message: "[avro] No avro sources found at $avrosrc")
        System.exit(0)
    }

    boolean uptodate1 = true
    boolean uptodate2 = true
    def skipIt = new RuntimeException()
    try {
        ant.fileset(dir: avrosrcDir, includes: "**/*.avpr").each { avrofile ->
            File markerFile = new File(gensrcDir+File.separator+'gen-java', "." + (avrofile.toString() - avrosrc).substring(1))
            if(!markerFile.exists() || avrofile.file.lastModified() > markerFile.lastModified()) throw skipIt
        }
    } catch(x) {
       if(x == skipIt) uptodate1 = false
       else throw x
    }
    try {
        ant.fileset(dir: avrosrcDir, includes: "**/*.avsc").each { avrofile ->
            File markerFile = new File(gensrcDir+File.separator+'gen-java', "." + (avrofile.toString() - avrosrc).substring(1))
            if(!markerFile.exists() || avrofile.file.lastModified() > markerFile.lastModified()) throw skipIt
        }
    } catch(x) {
       if(x == skipIt) uptodate2 = false
       else throw x
    }

    if(uptodate1 && uptodate2) {
       ant.echo(message: "[avro] Sources are up to date")
       return
    }

    ant.echo(message: "[avro] Invoking Avro protocol+schema generators on $avrosrc")
    ant.echo(message: "[avro] Generated sources will be placed in ${gensrcDirPath.absolutePath}")

    ant.taskdef(name: "protocol",
                classname: "org.apache.avro.specific.ProtocolTask",
                classpathref: "griffon.compile.classpath")
    ant.taskdef(name: "schema",
                classname: "org.apache.avro.specific.SchemaTask",
                classpathref: "griffon.compile.classpath")
    ant.taskdef(name: "paranamer",
                classname: "com.thoughtworks.paranamer.ant.ParanamerGeneratorTask",
                classpathref: "griffon.compile.classpath")

    ant.protocol(destdir: gensrcDirPath) {
        fileset(dir: avrosrcDir) {
            include(name: "**/*.avpr")
        }
    }
    ant.fileset(dir: avrosrcDir, includes: "**/*.avpr").each { avrofile ->
        File markerFile = new File(gensrcDirPath.absolutePath + File.separator + '.' + (avrofile.toString() - avrosrc).substring(1))
        ant.touch(file: markerFile)
    }

    ant.schema(destdir: gensrcDirPath) {
        fileset(dir: avrosrcDir) {
            include(name: "**/*.avsc")
        }
    }
    ant.fileset(dir: avrosrcDir, includes: "**/*.avsc").each { avrofile ->
        File markerFile = new File(gensrcDirPath.absolutePath + File.separator + '.' + (avrofile.toString() - avrosrc).substring(1))
        ant.touch(file: markerFile)
    }
    
    ant.mkdir(dir: classesDirPath)
    ant.echo(message: "[avro] Compiling generated sources to $classesDirPath")
    try {
    String classpathId = "griffon.compile.classpath"
        compileSources(classesDirPath, classpathId) {
            src(path: gensrcDirPath)
            javac(classpathref: classpathId)
        }
        paranamer(sourceDirectory: gensrcDirPath,
                  outputDirectory: classesDirPath)
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        ant.fail(message: "[avro] Could not compile generated sources: " + e.class.simpleName + ": " + e.message)
    }
}
setDefaultTarget('avro')
