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
 * @author Andres Almiray
 */

import griffon.util.Environment

includeTargets << griffonScript("Package")

findbugsPluginBase = getPluginDirForName("findbugs").file as String
findBugsHome = "${findbugsPluginBase}/lib/findbugs"

target(default: "Run FindBugs on Java sources") {
    depends(prepackage)

    ant.path(id: "findBugsJarSet") {
        fileset(dir: "${findBugsHome}/lib/" , includes : "*.jar")
    }

    ant.taskdef(name: "findbugs",
                classname: "edu.umd.cs.findbugs.anttask.FindBugsTask",
                classpathref: "findBugsJarSet")
    
    jardir = ant.antProject.replaceProperties(config.griffon.jars.destDir)
    def findbugsConfig = loadFindbugsConfig()

    Map findBugsOptions = [home: findBugsHome]
    findBugsOptions.output = findbugsConfig.output ?: 'html'
    findBugsOptions.outputFile = findbugsConfig.outputFile ?: "${projectTargetDir}/findbugs.html"
    if(findbugsConfig.quietErrors) findBugsOptions.quietErrors = findbugsConfig.quietErrors
    findBugsOptions.reportLevel = findbugsConfig.reportLevel ?: 'medium'
    findBugsOptions.sort = !findbugsConfig.styleSheet ? false : true
    if(findbugsConfig.debug) findBugsOptions.debug = findbugsConfig.debug
    findBugsOptions.effort = findbugsConfig.effort ?: 'default'
    if(findbugsConfig.styleSheet) findBugsOptions.styleSheet = findbugsConfig.styleSheet
    if(findbugsConfig.visitors) findBugsOptions.visitors = findbugsConfig.visitors
    if(findbugsConfig.omitVisitors) findBugsOptions.omitVisitors = findbugsConfig.omitVisitors
    if(findbugsConfig.excludeFilter) findBugsOptions.excludeFilter = findbugsConfig.excludeFilter
    if(findbugsConfig.includeFilter) findBugsOptions.includeFilter = findbugsConfig.includeFilter
    if(findbugsConfig.jvmargs) findBugsOptions.jvmargs = findbugsConfig.jvmargs
    if(findbugsConfig.timeout) findBugsOptions.timeout = findbugsConfig.timeout
    if(findbugsConfig.failOnError) findBugsOptions.failOnError = findbugsConfig.failOnError
    if(findbugsConfig.errorProperty) findBugsOptions.errorProperty = findbugsConfig.errorProperty
    if(findbugsConfig.warningsProperty) findBugsOptions.warningsProperty = findbugsConfig.warningsProperty

    ant.delete(file: findBugsOptions.outputFile, quiet: true, failOnError: false)
    ant.findbugs(findBugsOptions) {
        auxClasspath {
            fileset(dir: jardir , includes : "*.jar")
        }
        sourcePath(path: "${basedir}/src/main")
        'class'(location: classesDir)
        findbugsConfig.systemProperty.each { k, v ->
            systemProperty(name: k, value: v)
        }
    }
}

private ConfigObject loadFindbugsConfig() {
    def classLoader = Thread.currentThread().contextClassLoader
    classLoader.addURL(new File(classesDirPath).toURL())
    return new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('Config')).findbugs
}

private int getConfigInt(config, String name, int defaultIfMissing) {
    def value = config[name]
    return value instanceof Integer ? value : defaultIfMissing
}

private boolean getConfigBoolean(config, String name) {
    def value = config[name]
    return value instanceof Boolean ? value : true
}
