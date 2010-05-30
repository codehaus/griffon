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
 * @author Scott Ryan
 * @author Andres Almiray
 */

import griffon.util.Environment

includeTargets << griffonScript('Compile')

target('default': 'Run GMetrics') {
    depends(compile)
    runGmetrics()
}

private void runGmetrics() {
    ant.taskdef(name: 'gmetrics', classname: 'org.gmetrics.ant.GMetricsTask')

    def config = loadConfig()

    String reportName = config.reportName ?: 'GMetricsReport.html'
    String reportLocation = config.reportLocation ?: projectTargetDir
    String reportType = config.reportType ?: 'org.gmetrics.report.BasicHtmlReportWriter'
    String reportTitle = config.reportTitle ?: ''

    List includes = configureIncludes(config)

    ant.echo(message: "[gmetrics] Running Gmetrics ...")
    ant.mkdir(dir: projectTargetDir)

    String reportFile = "${reportLocation}/${reportName}".toString()
    ant.gmetrics() {
        report(type: reportType) {
            option(name: 'outputFile', value: reportFile)
            option(name: 'title', value: reportTitle)
        }
        fileset(dir: '.', includes: includes.join(','))
    }

    ant.echo(message: "[gmetrics] GMetrics finished; report generated: $reportFile")
}

private ConfigObject loadConfig() {
    def classLoader = Thread.currentThread().contextClassLoader
    classLoader.addURL(new File(classesDirPath).toURL())
    return new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('Config')).gmetrics
}

private int getConfigInt(config, String name, int defaultIfMissing) {
    def value = config[name]
    return value instanceof Integer ? value : defaultIfMissing
}

private boolean getConfigBoolean(config, String name) {
    def value = config[name]
    return value instanceof Boolean ? value : true
}

private List configureIncludes(config) {
    List includes = []

    if (getConfigBoolean(config, 'processSrc')) {
        includes << 'src/main/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processModels')) {
        includes << 'griffon-app/models/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processControllers')) {
        includes << 'griffon-app/controllers/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processViews')) {
        includes << 'griffon-app/views/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processServices')) {
        includes << 'griffon-app/services/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processTestUnit')) {
        includes << 'test/unit/**/*.groovy'
    }

    if (getConfigBoolean(config, 'processTestIntegration')) {
        includes << 'test/integration/**/*.groovy'
    }

    for (includeDir in config.extraIncludeDirs) {
        includes << "$includeDir/**/*.groovy"
    }

    return includes
}
