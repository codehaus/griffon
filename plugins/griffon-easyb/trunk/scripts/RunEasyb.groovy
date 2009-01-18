/*
 * Copyright 2004-2008 the original author or authors.
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
 * Gant script that runs Easyb stories.<p>
 *
 * Based on the Grails Easyb plugin by Rodrigo Urubatan
 * http://github.com/urubatan/easybtest/tree/master
 *
 * @author Andres Almiray
 * @since 0.1
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("_GriffonPackage")
includeTargets << griffonScript("_GriffonClean")
includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonBootstrap")

easybSourceDir = "${basedir}/test/easyb"
easybTargetDir = "${projectWorkDir}/easyb-classes"
easybReportDir = "${basedir}/test/easyb-reports"
easybPluginBase = getPluginDirForName("easyb").file as String
_easyb_skip = false
_cobertura_enabled = false

ant.path( id : "easybJarSet" ) {
    fileset( dir: "${easybPluginBase}/lib/test" , includes : "*.jar" )
}

target(runEasyb:"Run Easyb stories") {
    depends(checkVersion, configureProxy, clean, packageApp, parseArguments, classpath)
    checkEasybStoriesSources()
    if( !_easyb_skip ) {
        initCobertura()
        loadApp()
        configureApp()
        runEasybStories()
        finishCobertura()
    }
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

target(checkEasybStoriesSources:"") {
    def src = new File( easybSourceDir )
    if( !src || !src.list() ) {
        println "No Easyb sources were found. SKIPPING"
        _easyb_skip = true
    }
}

target(runEasybStories: "") {
    easybTestSource = ant.path {
        fileset( dir : easybSourceDir ) {
            include(name:"**/*Story.groovy")
            include(name:"**/*.story")
            include(name:"**/*Specification.groovy")
            include(name:"**/*.specification")
        }
    }
    behaviors = []
    easybTestSource.list().each{
        behaviors.add(new File(it))
    }

    easybReportDir = config.griffon.testing.reports.destDir ?: easybReportDir

    ant.mkdir(dir: easybReportDir)
    ant.mkdir(dir: "${easybReportDir}/xml")
    ant.mkdir(dir: "${easybReportDir}/plain")

    reports = [[location:"${easybReportDir}/xml/easyb.xml",format:"xml",type:"easyb"],
               [location:"${easybReportDir}/plain/stories.txt",format:"txt",type:"story"],
               [location:"${easybReportDir}/plain/specifications.txt",format:"txt",type:"specification"]]

    def urls = [new File("${easybPluginBase}/src/main").toURI().toURL()]
    new File("${easybPluginBase}/lib/test").eachFileMatch( ~/.*\.jar/ ) { f ->
        urls << f.toURI().toURL()
    }

    def binding = new Binding()
    binding.setVariable( "reports", reports )
    binding.setVariable( "behaviors", behaviors )
    binding.setVariable( "app", griffonApp )
    def classloader = new URLClassLoader(urls as URL[], rootLoader)

    def script = """
import griffon.plugins.easyb.BehaviorRunner
import org.disco.easyb.ant.*
import org.disco.easyb.report.*

def convertedReports = reports.inject([]){ rs, r -> rs << (r as Report) }
new BehaviorRunner(convertedReports,app).runBehavior(behaviors)
"""

    shell = new GroovyShell( classloader, binding )
    shell.evaluate( script )
}

setDefaultTarget(runEasyb)