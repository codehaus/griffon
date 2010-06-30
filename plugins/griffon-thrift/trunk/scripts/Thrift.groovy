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

includeTargets << griffonScript('Init')
includeTargets << griffonScript('_GriffonCompile')

target(thrift: 'Compile Thrift sources with thrift') {
    depends(checkVersion, classpath)
    gensrcDir = "${projectWorkDir}/thrift"
    gensrcDirPath = new File(gensrcDir)
    gensrcDirPath.mkdirs()
    gensrcDirPath = new File(gensrcDir, 'gen-java')
    gensrcDirPath.mkdirs()

    def thriftExecutable = buildConfig?.apache?.thrift?.executable
    if(!thriftExecutable) {
        println('''Could not find thrift executable. Did you forget to define a value for it?
Make sure you have a similar setting on your griffon-app/conf/BuildSettings.groovy script
    apache.thrift.executable = "/path/to/thrift"''')
        System.exit(1)
    }

    thriftsrc = "${basedir}/src/thrift"
    thriftsrcDir = new File(thriftsrc)
    if(!thriftsrcDir.list().size()) {
        ant.echo(message: "[thrift] No thrift sources found at $thriftsrc")
        System.exit(0)
    }

    boolean uptodate = true
    def skipIt = new RuntimeException()
    try {
        ant.fileset(dir: thriftsrcDir, includes: "**/*.thrift").each { thriftfile ->
            File markerFile = new File(gensrcDir+File.separator+'gen-java', "." + (thriftfile.toString() - thriftsrc).substring(1))
            if(!markerFile.exists() || thriftfile.file.lastModified() > markerFile.lastModified()) throw skipIt
        }
    } catch(x) {
       if(x == skipIt) uptodate = false
       else throw x
    }
    if(uptodate) {
       ant.echo(message: "[thrift] Sources are up to date")
       return
    }

    ant.echo(message: "[thrift] Invoking $thriftExecutable on $thriftsrc")
    ant.echo(message: "[thrift] Generated sources will be placed in ${gensrcDirPath.absolutePath}")
    ant.fileset(dir: thriftsrcDir, includes: "**/*.thrift").each { thriftfile ->
        ant.echo(message: "[thrift] Compiling $thriftfile")
        Process thrift = "$thriftExecutable -I $thriftsrc -o $gensrcDir --gen java:hashcode,beans $thriftfile".execute([] as String[], new File(basedir))
        thrift.consumeProcessOutput(System.out, System.err)
        thrift.waitFor()
        File markerFile = new File(gensrcDirPath.absolutePath + File.separator + '.' + (thriftfile.toString() - thriftsrc).substring(1))
        ant.touch(file: markerFile)
    }

    ant.mkdir(dir: classesDirPath)
    ant.echo(message: "[thrift] Compiling generated sources to $classesDirPath")
    String classpathId = "griffon.compile.classpath"
    compileSources(classesDirPath, classpathId) {
        src(path: gensrcDirPath)
        javac(classpathref: classpathId)
    }
}
setDefaultTarget(thrift)
