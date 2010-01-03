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
 * Gant script that runs FEST tests
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("_GriffonPackage")
includeTargets << griffonScript("_GriffonClean")
includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonBootstrap")

festSourceDir = "${basedir}/test/fest"
festTargetDir = "${projectWorkDir}/fest-classes"
festReportDir = config.griffon.testing.reports.destDir ?: "${basedir}/test/fest-reports"
festPluginBase = getPluginDirForName("fest").file as String
_fest_skip = false
_cobertura_enabled = false

ant.path( id : 'festJarSet' ) {
    fileset( dir: "${festPluginBase}/lib" , includes : "*.jar" )
}

ant.taskdef( resource: "testngtasks", classpathref: "festJarSet" )

ant.path( id: "fest.compile.classpath" ) {
    path(refid:"griffon.compile.classpath")
    path(refid:"griffon.runtime.classpath")
    path(refid:"festJarSet")
    pathelement(location: "${classesDirPath}")
}

ant.path( id: "fest.runtime.classpath" ) {
    path(refid:"fest.compile.classpath")
    pathelement(location: "${festTargetDir}")
}

target(runFest:"Run FEST tests") {
    depends(checkVersion, configureProxy, clean, packageApp, parseArguments, classpath)
//    initCobertura()
    checkFestTestsSources()
    compileFestTests()
    runFestTests()
//    finishCobertura()
}

target(initCobertura:"") {
   def coberturaPlugin = getPluginDirForName("code-coverage")
   if( coberturaPlugin && argsMap.cobertura ) {
      _cobertura_enabled = true
       includeTargets << pluginScript("code-coverage","TestAppCobertura")
       coberturaSetup()
       coberturaInstrumentClasses()
       coberturaInstrumentTests()
   }
}

target(finishCobertura:"") {
   if( _cobertura_enabled ) {
      coberturaReport()
   }
}

target(checkFestTestsSources:"") {
    def src = new File( festSourceDir )
    if( !src || !src.list() ) {
        println "No FEST sources were found. SKIPPING"
        _fest_skip = true
    }
}

target(compileFestTests: "") {
    if( _fest_skip ) return
    event("CompileStart", ['fest'])

    ant.mkdir( dir: festTargetDir )
    try {
        ant.groovyc( destdir: festTargetDir,
                     classpathref: "fest.compile.classpath",
                     encoding: "UTF-8" ) {
           src( path: "${festSourceDir}" )
           include( name: "**/*.groovy" )
           include( name: "**/*.java" )
           javac( classpathref: "fest.compile.classpath", debug:"yes" )
       }
    }
    catch(Exception e) {
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }

    event("CompileEnd", ['fest'])
}

target(runFestTests: "") {
    if( _fest_skip ) return
    ant.mkdir( dir: festReportDir )
    ant.testng( classpathref: "fest.runtime.classpath",
                outputDir: "${festReportDir}",
                suitename: "$baseName suite",
                haltOnfailure: false ) {
        classfileset( dir: "${festTargetDir}", includes: "**/*Test.class" )
    }
}

setDefaultTarget(runFest)
