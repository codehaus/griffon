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

import org.disco.easyb.*
import org.disco.easyb.ant.*
import org.disco.easyb.util.*
import org.disco.easyb.report.*
import org.disco.easyb.listener.*

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Run Easyb stories") {
    depends(checkVersion, configureProxy, packageApp, classpath)
    runEasybImpl()
}

includeTargets << griffonScript("Bootstrap")

easybSourceDir = "${basedir}/test/easyb"
easybTargetDir = "${projectWorkDir}/easyb-classes"
easybReportDir = "${basedir}/test/easyb-reports"
easybPluginBase = getPluginDirForName('easyb').file as String
_easyb_skip = false

Ant.path( id : "easybJarSet" ) {
    fileset( dir: "${easybPluginBase}/lib/test" , includes : "*.jar" )
}

// Ant.taskdef( name: "easyb",
//              classname: "org.disco.easyb.ant.BehaviorRunnerTask",
//              classpathref: "easybJarSet" )
// 
// Ant.path( id: "easyb.runtime.classpath" ) {
//     path(refid:"griffon.classpath")
//     path(refid:"easybJarSet")
//     pathelement(location: "${classesDirPath}")
// }

target(runEasybImpl:"Run FEST tests") {
    checkEasybStoriesSources()
    loadApp()
    configureApp()
    runEasybStories()
}

target(checkEasybStoriesSources:"") {
    def src = new File( easybSourceDir )
    if( !src || !src.list() ) {
        println "No Easyb sources were found. SKIPPING"
        _easyb_skip = true
    }
}

target(runEasybStories: "") {
    if( _easyb_skip ) return

//     easybReportDir = config.griffon.testing.reports.destDir ?: easybReportDir
//     Ant.mkdir( dir: easybReportDir )
//     Ant.testng( classpathref: "easyb.runtime.classpath",
//                 outputDir: "${easybReportDir}",
//                 suitename: "$baseName suite",
//                 haltOnfailure: false ) {
//         classfileset( dir: "${easybTargetDir}", includes: "**/*Test.class" )
//     }

    easybTestSource = Ant.path {
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

    Ant.mkdir(dir: easybReportDir)
    Ant.mkdir(dir: "${easybReportDir}/xml")
    Ant.mkdir(dir: "${easybReportDir}/plain")

    reports = [[location:"${easybReportDir}/xml/easyb.xml",format:"xml",type:"easyb"],
               [location:"${easybReportDir}/plain/stories.txt",format:"txt",type:"story"],
               [location:"${easybReportDir}/plain/specifications.txt",format:"txt",type:"specification"]]

    def convertedReports = reports.inject([]){ rs, r -> rs << (r as Report) }
    BehaviorRunner br = new BehaviorRunner(reports,griffonApp)
    br.runBehavior(behaviors)


//     def jarFiles = []
//     new File("${easybPluginBase}/lib/test").eachFileMatch( ~/.*\.jar/ ) { f ->
//         jarFiles << f.toURI().toURL()
//     }

//     def binding = new Binding()
//     binding.setVariable( "reports", reports )
//     binding.setVariable( "behaviors", behaviors )
//     binding.setVariable( "app", griffonApp )
//     def classloader = new URLClassLoader(jarFiles as URL[], rootLoader)

//     def script = """
// import BehaviorRunner
// import org.disco.easyb.*
// import org.disco.easyb.ant.*
// import org.disco.easyb.util.*
// import org.disco.easyb.report.*
// import org.disco.easyb.listener.*
// 
// def convertedReports = reports.inject([]){ rs, r -> rs << (r as Report) }
// BehaviorRunner br = new BehaviorRunner(reports,app)
// br.runBehavior(behaviors)
// """
// 
//     shell = new GroovyShell( /*classloader,*/ binding )
//     shell.evaluate( script )

}

// TODO mve BehaviorRunner into its own file
class BehaviorRunner {
   private reports = []
   private app

   public BehaviorRunner( reports, app ) {
      this.reports = addDefaultReports(reports)
      this.app = app
   }

   /**
    * @param specs collection of files that contain the specifications
    * @throws Exception if unable to write report file
    */
   public void runBehavior(Collection specs) throws Exception {
      BehaviorListener listener = new DefaultListener()

      executeSpecifications(specs, listener)

      println("\n" +
            // prints "1 behavior run" or "x behaviors run"
            (listener.getBehaviorCount() > 1 ? listener.getBehaviorCount() + " total behaviors run" : "1 behavior run")
            // outer ternary prints either 1..X failure(s) or no failures
            // inner ternary determines if more than one failure and makes it plural if so
            + (listener.getFailedBehaviorCount() > 0 ? " with "
                  + (listener.getFailedBehaviorCount() == 1 ? "1 failure" : listener.getFailedBehaviorCount() + " failures") : " with no failures"))

      produceReports(listener)

      if (listener.getFailedBehaviorCount() > 0) {
         System.exit(-6)
      }
   }
   /**
    * @param listener
    */
   private void produceReports(BehaviorListener listener) {
      reports.each { report ->
         if (report.format.concat(report.type) == Report.XML_EASYB) {
            new XmlReportWriter(report, listener).writeReport()
         } else if (report.format.concat(report.type) == Report.TXT_STORY) {
            new TxtStoryReportWriter(report, listener).writeReport()
         } else if (report.format.concat(report.type) == Report.TXT_SPECIFICATION) {
            new TxtSpecificationReportWriter(report, listener).writeReport()
         }
      }
   }

   private void executeSpecifications(final Collection behaviorFiles, final BehaviorListener listener) throws IOException {
      behaviorFiles.each { behaviorFile ->
         def behavior = null
         try {
            behavior = BehaviorFactory.createBehavior(behaviorFile);
         } catch(IllegalArgumentException iae) {
            println(iae.getMessage())
            System.exit(-1)
         }

         long startTime = System.currentTimeMillis()
         println "Running ${behavior.getPhrase()} ${((behavior instanceof Story) ? ' story' : ' specification')} (${behaviorFile.getName()})"

         BehaviorStep currentStep
         GroovyShell g = null
         if (behavior instanceof Story) {
            currentStep = listener.startStep(BehaviorStepType.STORY, behavior.phrase)
            g = new GroovyShell(app.class.classLoader,StoryBinding.getBinding(listener))
         } else {
            currentStep = listener.startStep(BehaviorStepType.SPECIFICATION, behavior.phrase)
            g = new GroovyShell(app.class.classLoader,SpecificationBinding.getBinding(listener))
         }
         g.getContext().setVariable("app",app)
         g.evaluate(behaviorFile)
         listener.stopStep()

         long endTime = System.currentTimeMillis()

         printMetrics(behavior, startTime, currentStep, endTime)
      }
   }

   private void printMetrics(Behavior behavior, long startTime, BehaviorStep currentStep, long endTime) {
      if(behavior instanceof Story) {
         println((currentStep.getFailedScenarioCountRecursively() == 0 ? "" : "FAILURE ") +
               "Scenarios run: " + currentStep.getScenarioCountRecursively() +
               ", Failures: " + currentStep.getFailedScenarioCountRecursively() +
               ", Pending: " + currentStep.getPendingScenarioCountRecursively() +
               ", Time Elapsed: " + (endTime - startTime) / 1000f + " sec")
      } else {
         println((currentStep.getFailedSpecificationCountRecursively() == 0 ? "" : "FAILURE ") +
               "Specifications run: " + currentStep.getSpecificationCountRecursively() +
               ", Failures: " + currentStep.getFailedSpecificationCountRecursively() +
               ", Pending: " + currentStep.getPendingSpecificationCountRecursively() +
               ", Time Elapsed: " + (endTime - startTime) / 1000f + " sec")
      }
   }

   private List addDefaultReports(List userConfiguredReports) {
      def configuredReports = []

      if (userConfiguredReports ) {
         configuredReports.addAll(userConfiguredReports)
      }

      return configuredReports
   }
}