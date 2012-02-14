dataFile = buildConfig.cobertura.file ?: 'cobertura.ser'

codeCoverageExclusionList = [
        "Application*",
        "Builder*",
        "BuildConfig*",
        "Config*",
        "**/*Tests*",
        "**/griffon/test/**",
        "**/org/codehaus/griffon/**",]

eventTestPhasesStart = {
    if (isCoverageEnabled()) {
        event("StatusUpdate", ["Instrumenting classes for coverage"])
        ant.delete(file: "${dataFile}")

        if (buildConfig.coverage.exclusionListOverride) {
            codeCoverageExclusionList = buildConfig.coverage.exclusionListOverride
        }

        if (buildConfig.coverage.exclusions) {
            codeCoverageExclusionList += buildConfig.coverage.exclusions
        }

        defineCoberturaPathAndTasks()
        instrumentClasses()
    }
}

eventTestPhasesEnd = {
    if (isCoverageEnabled()) {
        defineCoberturaPathAndTasks()
        flushReportData()
        createCoverageReports()
        replaceClosureNamesInReports()

        //clear out the instrumented classes
        cleanCompiledSources()

        event("StatusFinal", ["Cobertura Code Coverage Complete (view reports in: ${coverageReportDir})"])
    }
}

def createCoverageReports() {
    coverageReportDir = "${buildConfig.griffon.testing.reports.destDir ?: testReportsDir}/cobertura"

    ant.mkdir(dir: "${coverageReportDir}")

    coverageReportFormats = ['html']
    if (argsMap.xml || buildConfig.coverage.xml) {
        coverageReportFormats << 'xml'
    }

    coverageReportFormats.each {reportFormat ->
        ant.'cobertura-report'(destDir: "${coverageReportDir}", datafile: "${dataFile}", format: reportFormat) {
            //load all these dirs independently so the dir structure is flattened,
            //otherwise the source isn't found for the reports
            def exclusions = ["i18n", "resources"]
            new File("${basedir}/griffon-app").list().each { f ->
                if(f in exclusions) return
                fileset(dir: "${basedir}/griffon-app/${f}")
            }
            fileset(dir: "${basedir}/src/main")
            buildConfig.coverage.sourceInclusions.each {
                fileset(dir: "${basedir}/${it}")
            }
        }
    }
}

def defineCoberturaPathAndTasks() {
    ant.path(id: "cobertura.classpath") {
        fileset(dir: "${codeCoveragePluginDir}/lib") {
            include(name: "*.jar")
        }
    }

    ant.taskdef(classpathRef: 'cobertura.classpath', resource: "tasks.properties")
}

def findArtefacts(String type) {
    def artefacts = []
    if(griffonApp) {
	    griffonApp."$type".each { a ->
		    artefacts << artefact
	    }
	    return artefacts
    }
    
    File searchDir = new File("${basedir}/griffon-app/$type")
    def pattern = ~/.*\.groovy/
    def artefactFinder
    artefactFinder = {
            it.eachFileMatch(pattern) { f ->
	        def artefact = f.absolutePath - searchDir.absolutePath - File.separator - ".groovy"
	        artefacts << artefact.replaceAll(File.separator, ".")
	    }
	    it.eachDir(artefactFinder)
    }
    artefactFinder(searchDir)
    artefacts.collect([]){ a -> classLoader.loadClass(a).newInstance() }
}


def replaceClosureNamesInReports() {
    if (!argsMap.nopost || !buildConfig.coverage.noPost) {
        def startTime = new Date().time
        Map<String, List<Class>> artifacts = collectArtifacts()
        replaceClosureNames(artifacts.controller)
        replaceClosureNames(artifacts.model)
        replaceClosureNamesInXmlReports(artifacts.controller)
        replaceClosureNamesInXmlReports(artifacts.model)
        def endTime = new Date().time
        println "Done with post processing reports in ${endTime - startTime}ms"
    }
}

collectArtifacts = {
    Map<String, List<Class>> artifacts = [:]
    collectArtifactMetadata()
    File resourcesDir = new File("${resourcesDirPath}/griffon-app/resources")
    File artifactMetadataDir = new File("${resourcesDirPath}/griffon-app/resources/META-INF")
    File artifactMetadataFile = new File(artifactMetadataDir, '/griffon-artifacts.properties')
    Properties p = new Properties()
    try {
        p.load(new FileReader(artifactMetadataFile))
    } catch (IOException e) {
        return artifacts
    }

    ConfigObject artifactConfig = new ConfigSlurper().parse(p)

    for (Object key : artifactConfig.keySet()) {
        String type = key.toString()
        String classes = (String) artifactConfig.get(type)
        if (classes.startsWith("'") && classes.endsWith("'")) {
            classes = classes.substring(1, classes.length() - 1)
        }
        String[] classNames = classes.split(",")

        List<Class> list = artifacts.get(type)
        if (list == null) {
            list = new ArrayList<Class>()
            artifacts.put(type, list)
        }

        for (String className : classNames) {
            try {
                Class clazz = classLoader.loadClass(className)
                if (!list.contains(clazz)) list.add(clazz)
            } catch (ClassNotFoundException e) {
                continue
            }
        }
    }
    return artifacts
}

def replaceClosureNames(List<Class> artifacts) {
    artifacts.each { Class klass ->
        def packageName = ""
        def shortName = klass.name.toString()
        def lastdot = shortName.lastIndexOf(".")
        if (lastdot != -1) {
            packageName = shortName[0..lastdot]
            shortName = shortName[(lastdot+1)..-1]
        }
        def beanInfo = java.beans.Introspector.getBeanInfo(klass)
        def reference = klass.newInstance()
        beanInfo.propertyDescriptors.each { propertyDescriptor ->
            if(propertyDescriptor.name == 'griffonClass') return
            def property = reference."${propertyDescriptor.name}"
            if(!(property instanceof Closure)) return
            def closureClassName = property.class.name 
            // the name in the reports is sans package; subtract the package name
            def nameToReplace = closureClassName - packageName

            ant.replace(dir: "${coverageReportDir}",
				token: ">${nameToReplace}<",
				value: ">${shortName}.${propertyDescriptor.name}<") {
                include(name: "**/*${klass.name}.html")
                include(name: "frame-summary*.html")
            }
            ant.replace(dir: "${coverageReportDir}",
				token: ">${nameToReplace}_closure2<",
				value: ">${shortName}.${propertyDescriptor.name}<") {
                include(name: "**/*${klass.name}.html")
                include(name: "frame-summary*.html")
            }
        }
    }
}

def replaceClosureNamesInXmlReports(List<Class> artifacts) {
    def xml = new File("${coverageReportDir}/coverage.xml")
    if(xml.exists()) {
        def parser = new XmlParser().parse(xml)

        artifacts.each {Class klass ->
            def beanInfo = java.beans.Introspector.getBeanInfo(klass)
            def reference = klass.newInstance()
            beanInfo.propertyDescriptors.each { propertyDescriptor ->
                if(propertyDescriptor.name == 'griffonClass') return
                def property = reference."${propertyDescriptor.name}"
                if(!(property instanceof Closure)) return
                def closureClassName = property.class.name
                def node = parser['packages']['package']['classes']['class'].find {it.@name == closureClassName}
                if(node) {
                    node.@name = "${klass.name}.${propertyDescriptor.name}"
                }
                node = parser['packages']['package']['classes']['class'].find {it.@name == closureClassName + '_closure2'}
                if(node) {
                    node.@name = "${klass.name}.${propertyDescriptor.name}"
                }
            }
        }

        xml.withPrintWriter {writer ->
            new XmlNodePrinter(writer).print(parser)
        }
    }
}

def instrumentClasses() {
    try {
        ant.'cobertura-instrument'(datafile: "${dataFile}") {
            fileset(dir: classesDirPath) {
                include(name: "**/*.class")
                codeCoverageExclusionList.each {pattern ->
                    exclude(name: pattern)
                }
            }
        }
    }
    catch (Exception e) {
        event("StatusFinal", ["Error instrumenting classes: ${e.message}"])
        exit(1)
    }
}

boolean isCoverageEnabled(){
    if (argsMap.containsKey ('nocoverage')){
        return false
    } else if (argsMap.containsKey ('coverage')) {
        return true
    } else {
        return buildConfig.coverage.enabledByDefault
    }
}

def flushReportData() {
    try {
        net.sourceforge.cobertura.coveragedata.ProjectData.saveGlobalProjectData()
    } catch (Exception e) {
        event("StatusError", ["""
--------------------------------------------
***********WARNING*************
Unable to flush code coverage data.  
This usually happens when tests don't actually test anything;
e.g. none of the instrumented classes were exercised by tests!
--------------------------------------------
"""])
    }
}
