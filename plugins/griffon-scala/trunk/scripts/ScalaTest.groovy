import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("Init")
includePluginScript("scala", "_ScalaCommon")

testReportsDir = griffonSettings.testReportsDir

target(default: "Run Scala tests") {
    def scalaTestSrc = new File("${basedir}/test/scala")
    if(!scalaTestSrc.exists()) return
    compileScalaTest()
    ant.mkdir(dir: testReportsDir)

    def pattern = ~/.*Tests\.scala/
    def suites = []
    def findSuites
    findSuites = {
        it.eachFileMatch(pattern) { f ->
            def suite = f.absolutePath - scalaTestSrc.absolutePath - File.separator - ".scala"
            suites << suite.replaceAll(File.separator,".")
        }
        it.eachDir(findSuites)
    } 
    findSuites(scalaTestSrc)

    def scalaTestClassesDir = new File(griffonSettings.testClassesDir, "scala")
    ant.path(id: "scala.run.classpath") {
        path(refid: "scala.test.classpath")
        pathelement(location: "${classesDir.absolutePath}")
        pathelement(location: "${scalaTestClassesDir.absolutePath}")
    }

    ant.scalatest {
        runpath {
            ant.project.references["scala.run.classpath"].each { p ->
                pathelement(location: p)
            }
        }
        suites.each{ classname -> suite(classname: classname) }
        reporter(type: "xml", directory: testReportsDir)
        reporter(type: "stdout", config: "FAB")
    }
}
