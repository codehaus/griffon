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
 * Gant script that runs CodeNarc metrics.<p>
 *
 * @author Burt Beckwith (Grails version)
 * @author Andres Almiray (Griffon version)
 */

import griffon.util.Environment

includeTargets << griffonScript('_GriffonCompile')

target('codenarc': 'Run CodeNarc') {
   depends(compile)
   runCodenarc()
}

setDefaultTarget('codenarc')

private void runCodenarc() {
    ant.taskdef(name: "codenarc", classname: "org.codenarc.ant.CodeNarcTask")

    def codenarcConfig = buildCondig.codenarc

    String reportName = codenarcConfig.reportName ?: 'CodeNarcReport.html'
    String reportLocation = codenarcConfig.reportLocation ?: projectTargetDir
    String reportType = codenarcConfig.reportType ?: 'html'
    String reportTitle = codenarcConfig.reportTitle ?: ''
    int maxPriority1Violations = getConfigInt(codenarcConfig, 'maxPriority1Violations', Integer.MAX_VALUE)
    int maxPriority2Violations = getConfigInt(codenarcConfig, 'maxPriority2Violations', Integer.MAX_VALUE)
    int maxPriority3Violations = getConfigInt(codenarcConfig, 'maxPriority3Violations', Integer.MAX_VALUE)
    String ruleSetFiles = codenarcConfig.ruleSetFiles ?: 'rulesets/basic.xml,rulesets/exceptions.xml,rulesets/imports.xml,rulesets/unused.xml'
    List includes = codenarcConfigureIncludes(codenarcConfig)

    ant.echo(message: "[codenarc] Running CodeNarc ...")
    ant.mkdir(dir: projectTargetDir)   

    String reportFile = "${reportLocation}/${reportName}".toString()
    ant.codenarc(ruleSetFiles: ruleSetFiles,
            maxPriority1Violations: maxPriority1Violations,
            maxPriority2Violations: maxPriority2Violations,
            maxPriority3Violations: maxPriority3Violations) {
        report(type: reportType) {
            option(name: 'outputFile', value: reportFile)
            option(name: 'title', value: reportTitle)
        }
        fileset(dir: '.', includes: includes.join(','))
    }

    ant.echo(message: "[codenarc] CodeNarc finished; report generated: $reportFile")
}

private int getConfigInt(codenarcConfig, String name, int defaultIfMissing) {
    def value = codenarcConfig[name]
    return value instanceof Integer ? value : defaultIfMissing
}

private boolean getConfigBoolean(codenarcConfig, String name) {
    def value = codenarcConfig[name]
    return value instanceof Boolean ? value : true
}

private List codenarcConfigureIncludes(codenarcConfig) {
    List includes = []

    if (getConfigBoolean(codenarcConfig, 'processSrcGroovy')) {
        includes << 'src/main/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processControllers')) {
        includes << 'griffon-app/controllers/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processModels')) {
        includes << 'griffon-app/models/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processViews')) {
        includes << 'griffon-app/views/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processServices')) {
        includes << 'griffon-app/services/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processTestUnit')) {
        includes << 'test/unit/**/*.groovy'
    }

    if (getConfigBoolean(codenarcConfig, 'processTestIntegration')) {
        includes << 'test/integration/**/*.groovy'
    }

    for (includeDir in codenarcConfig.extraIncludeDirs) {
        includes << "$includeDir/**/*.groovy"
    }

    return includes
}
