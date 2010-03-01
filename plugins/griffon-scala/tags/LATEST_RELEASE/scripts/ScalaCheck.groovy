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
