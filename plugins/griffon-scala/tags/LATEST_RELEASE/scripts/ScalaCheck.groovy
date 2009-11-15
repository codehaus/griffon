includeTargets << griffonScript("Init")
includePluginScript("scala", "_ScalaCommon")

testReportsDir = griffonSettings.testReportsDir

target(default: "Run ScalaCheck tests") {
    depends(parseArguments)
    def scalaCheckSrc = new File("${basedir}/test/scalacheck")
    if(!scalaCheckSrc.exists() || !scalaCheckSrc.list().size()) {
        ant.echo(message: "[scala] No ScalaCheck test sources were found.")
        return
    }
    compileScalaCheck()
    ant.mkdir(dir: testReportsDir)

    def scalaCheckClassesDir = new File(griffonSettings.testClassesDir, "scalacheck")
    ant.path(id: "scala.check.run.classpath") {
        path(refid: "scala.check.classpath")
        pathelement(location: "${classesDir.absolutePath}")
        pathelement(location: "${scalaCheckClassesDir.absolutePath}")
    }

    def specifications = []
    if(!argsMap.spec) {
        // TODO externalize pattern ?
        def pattern = ~/.*Specification\.scala/
        def findSpecifications
        findSpecifications = {
            it.eachFileMatch(pattern) { f ->
                def spec = f.absolutePath - scalaCheckSrc.absolutePath - File.separator - ".scala"
                specifications << spec.replaceAll(File.separator,".")
            }
            it.eachDir(findSpecifications)
        }
        findSpecifications(scalaCheckSrc)
    }

    if(!specifications.size()) return
    ant.scalacheck(classpathref: "scala.check.run.classpath",
                   destdir: testReportsDir,
                   fork: true) {
        specifications.each{ s -> specification(name: s) }
    }
/*
    ant.java(classname: "griffon.scalacheck.ScalacheckRunner",
             classpathref: "scala.check.run.classpath",
             fork:true,
             dir: basedir) {
        arg(value: "--o=$testReportsDir")
        arg(value: specifications.join(" "))
    }
*/
}
