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
 * Gant script that runs JDepend metrics.<p>
 *
 * @author Andres Almiray
 * @since 0.1
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("Package")

jdependReportDir = "${projectTargetDir}"
jdependPluginBase = getPluginDirForName('jdepend').file as String

ant.taskdef(name: "jdepend",
            classname: "org.apache.tools.ant.taskdefs.optional.jdepend.JDependTask",
            classpathref: "griffon.compile.classpath")

target(jdepend: "Run JDepend metrics") {
    depends(checkVersion, packageApp, classpath)

    jdependReportDir = buildConfig.griffon.testing.reports.destDir ?: jdependReportDir
    jdependWorkDir = "${projectWorkDir}/jdepend-classes"

    ant.mkdir(dir: jdependReportDir)
    ant.delete(dir: jdependWorkDir, failonerror: false )
    ant.mkdir(dir: jdependWorkDir )

    packageApp()

    ant.copy( todir: jdependWorkDir ) {
        fileset( dir: "${projectWorkDir}/classes" ) {
            exclude( name: "**/*_closure*" )
        }
    }

    def jdependConfig = buildConfig.jdepend

    def options = {
        exclude( name: "java.lang" )
        exclude( name: "java.util" )
        exclude( name: "java.net" )
        exclude( name: "java.io" )
        exclude( name: "java.math" )
        exclude( name: "groovy.lang" )
        exclude( name: "groovy.util" )
        exclude( name: "org.codehaus.groovy.*" )
        if( jdependConfig?.excludes && jdependConfig?.excludes instanceof List ) {
            jdependConfig.excludes.each { x -> exclude( name: x ) }
        }
        classespath {
           pathelement( location: jdependWorkDir )
        }
        classpath {
           path( refid: "griffon.compile.classpath" )
           pathelement( location: jdependWorkDir )
        }
    }

    ant.jdepend(outputfile: "${jdependReportDir}/jdepend-report.txt",
                options )
    ant.jdepend(outputfile: "${jdependReportDir}/jdepend-report.xml",
                format: "xml",
                options)
    ant.xslt(basedir: "${jdependReportDir}",
             destdir: "${jdependReportDir}",
             includes: "jdepend-report.xml",
             style: "${jdependPluginBase}/src/etc/jdepend.xsl")
}

setDefaultTarget(jdepend)
