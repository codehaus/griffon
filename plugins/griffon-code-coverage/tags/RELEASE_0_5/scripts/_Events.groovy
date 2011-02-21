dataFile = "cobertura.ser"

codeCoverageExclusionList = [
        "Application*",
        "Builder*",
        "BuildConfig*",
        "Config*",
        "**/*Tests*",
        "**/griffon/test/**",
        "**/org/codehaus/griffon/**",
        "*GriffonPlugin*"]

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
            def exclusions = ["conf", "i18n", "resources"]
            new File("${basedir}/griffon-app").list().each { f ->
                if(f in exclusions) return
                fileset(dir: "${basedir}/griffon-app/${f}")
            }
            fileset(dir: "${basedir}/src/main")
            if (buildConfig.coverage?.sourceInclusions) {
                buildConfig.coverage.sourceInclusions.each {
                    fileset(dir: "${basedir}/${it}")
                }
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
        replaceClosureNames(findArtefacts("controllers"))
        replaceClosureNames(findArtefacts("models"))
        replaceClosureNamesInXmlReports(findArtefacts("controllers"))
        replaceClosureNamesInXmlReports(findArtefacts("models"))
        def endTime = new Date().time
        println "Done with post processing reports in ${endTime - startTime}ms"
    }
}

def replaceClosureNames(artefacts) {
    artefacts?.each { artefact ->
		def klass = artefact.class
        def packageName = ""
        def shortName = klass.name.toString()
        def lastdot = klass.name.toString().lastIndexOf(".")
        if (lastdot != -1) {
            packageName = klass.toString()[0..lastdot]
            shortName = klass.name.toString[(lastdot+1)..-1]
        }
        def beanInfo = java.beans.Introspector.getBeanInfo(klass)
        beanInfo.propertyDescriptors.each {propertyDescriptor ->
            def property = artefact."${propertyDescriptor.name}"
            if(!(property instanceof Closure)) return
            def closureClassName = property.class.name 
            // the name in the reports is sans package; subtract the package name
            def nameToReplace = closureClassName - packageName

            ant.replace(dir: "${coverageReportDir}",
				token: ">${nameToReplace}<",
				value: ">${shortName}.${propertyDescriptor.name}<") {
                include(name: "**/*${klass.toString()}.html")
                include(name: "frame-summary*.html")
            }
        }
    }
}

def replaceClosureNamesInXmlReports(artefacts) {
    def xml = new File("${coverageReportDir}/coverage.xml")
    if(xml.exists()) {
        def parser = new XmlParser().parse(xml)

        artefacts?.each {artefact ->
            def closures = [:]
            artefact.reference.propertyDescriptors.each {propertyDescriptor ->
                def closureClassName = artefact.getPropertyOrStaticPropertyOrFieldValue(propertyDescriptor.name, Closure)?.class?.name
                if (closureClassName) {
                    def node = parser['packages']['package']['classes']['class'].find {it.@name == closureClassName}
                    if(node) {
                        node.@name = "${artefact.fullName}.${propertyDescriptor.name}"
                    }
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
