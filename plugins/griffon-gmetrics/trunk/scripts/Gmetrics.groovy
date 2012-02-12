/*
 * Copyright 2010-2012 the original author or authors.
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

includeTargets << griffonScript('_GriffonCompile')

target(gmetrics: 'Run GMetrics') {
    depends(compile)
    runGmetrics()
}
setDefaultTarget('gmetrics')

private void runGmetrics() {
    ant.taskdef(name: 'gmetrics', classname: 'org.gmetrics.ant.GMetricsTask')

    def gmetricsConfig = buildConfig.gmetrics

    String reportName     = gmetricsConfig.reportName ?: 'gmetrics.html'
    String reportLocation = gmetricsConfig.reportLocation ?: "${projectTargetDir}/test-reports/gmetrics"
    String reportType     = gmetricsConfig.reportType ?: 'org.gmetrics.report.BasicHtmlReportWriter'
    String reportTitle    = gmetricsConfig.reportTitle ?: "GMetrics - $griffonAppName"
    String metricSetFile  = gmetricsConfig.metricSetFile ?: null

    List includes = gmetricsConfigureIncludes(gmetricsConfig)

    ant.echo(message: "[gmetrics] Running Gmetrics ...")
    ant.mkdir(dir: projectTargetDir)
    ant.mkdir(dir: reportLocation)

    String reportFile = "${reportLocation}/${reportName}".toString()
    Map params = [:]
    if(metricSetFile) params.metricSetFile = metricSetFile
    ant.gmetrics(params) {
        report(type: reportType) {
            option(name: 'outputFile', value: reportFile)
            option(name: 'title', value: reportTitle)
        }
        fileset(dir: '.', includes: includes.join(','))
    }

    ant.echo(message: "[gmetrics] GMetrics finished; report generated: $reportFile")
}

private int getConfigInt(gmetricsConfig, String name, int defaultIfMissing) {
    def value = gmetricsConfig[name]
    return value instanceof Integer ? value : defaultIfMissing
}

private boolean getConfigBoolean(gmetricsConfig, String name) {
    def value = gmetricsConfig[name]
    return value instanceof Boolean ? value : true
}

private List gmetricsConfigureIncludes(gmetricsConfig) {
    List includes = []

    if (getConfigBoolean(gmetricsConfig, 'processSrcGroovy')) {
        includes << 'src/main/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processModels')) {
        includes << 'griffon-app/models/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processControllers')) {
        includes << 'griffon-app/controllers/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processViews')) {
        includes << 'griffon-app/views/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processServices')) {
        includes << 'griffon-app/services/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processTestUnit')) {
        includes << 'test/unit/**/*.groovy'
    }

    if (getConfigBoolean(gmetricsConfig, 'processTestIntegration')) {
        includes << 'test/integration/**/*.groovy'
    }

    for (includeDir in gmetricsConfig.extraIncludeDirs) {
        includes << "$includeDir/**/*.groovy"
    }

    return includes
}
