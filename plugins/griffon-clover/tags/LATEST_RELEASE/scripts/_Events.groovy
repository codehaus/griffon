import org.apache.tools.ant.BuildLogger
import org.apache.tools.ant.Project

import org.codehaus.griffon.test.GriffonTestTargetPattern


// some clover defaults
defCloverSrcDirs = ["src/main", "test/unit", "test/integration", "griffon-app"];
defCloverIncludes = ["**/*.groovy", "**/*.java"];
defCloverExcludes = ["**/conf/**", "**/plugins/**"];
defCloverReportDir = "${projectTargetDir}/clover/report" // flim-flamming between projectWorkDir and build. build is consistent
defCloverHistoryDir = "${basedir}/.cloverhistory"
defCloverReportTitle = metadata["app.name"]
defCloverHistorical = true; // by default, we will generate a historical report.
defCloverSnapshotFile = new File("${projectWorkDir}", "clover.snapshot") // this location can be overridden via the -clover.snapshotLocation argument

defStoredTestTargetPatterns = [];

// HACK to work-around: http://jira.codehaus.org/browse/GRAILS-5755
loadDependencyClass = {name ->
  def doLoad = { -> classLoader.loadClass(name) }
  try {
    doLoad()
  } catch (ClassNotFoundException e) {
    includeTargets << griffonScript("_GriffonCompile")
    compile()
    doLoad()
  }
}


eventCompileStart = {
  ConfigObject config = mergeConfig()
  // Ants Project is available via: kind.ant.project
  println "Clover: Compile start."
  System.setProperty "grover.ast.dump", "" + config.dumpAST
}

eventSetClasspath = {URLClassLoader rootLoader ->

//  griffonSettings.compileDependencies.each { println it }

  ConfigObject config = mergeConfig()
  println "Clover: Using config: ${config}"

  if (config.debug) {
    println "Clover: Dumping binding variables:"
    binding.variables.each { println it.key + " = " + it.value } // dumps all available vars and their values
  }

  toggleAntLogging(config)

  if (config.on || config.optimize) // automatically enable clover when optimizing
  {

    toggleCloverOn(config)

    if ((!config.containsKey('forceClean') || config.forceClean) && !config.optimize) // do not clean when optimizing
    {
      // force a clean
      def cleanDirs = [classesDirPath, testDirPath, "${projectWorkDir}/clover"]

      println "Clover: Forcing a clean to ensure Clover instrumentation occurs. Disable by setting: clover.forceClean=false "
      cleanDirs.each {ant.delete(dir: it, failonerror: false)}

    }
  }
}

eventTestPhasesStart = {phase ->

//  binding.variables.each { println it.key + " = " + it.value } // dumps all available vars and their values
  defStoredTestTargetPatterns = testTargetPatterns;

}
//TODO uncomment this when http://jira.codehaus.org/browse/GRAILS-5755 is released
static class FileOptimizable /**implements Optimizable**/ {

  final File file;
  final File baseDir;
  public FileOptimizable(file, baseDir) {
    this.file = file;
    this.baseDir = baseDir;
  }

  public String getName() {
    sourceFileToClassName(baseDir, file)
  }

  public String getClassName() {
    sourceFileToClassName(baseDir, file)
  }

  /**
   * Gets the corresponding class name for a source file of this test type.
   *
   * Copied from GriffonTestTypeSupport.groovy
   */
  String sourceFileToClassName(File sourceDir, File sourceFile) {
    String relativePath = getRelativePathName(sourceDir, sourceFile)
    def suffixPos = relativePath.lastIndexOf(".")
    relativePath[0..(suffixPos - 1)].replace(File.separatorChar, '.' as char)
  }

  String getRelativePathName(File sourceDir, File sourceFile) {
    def filePath = sourceFile.canonicalPath
    def basePath = sourceDir.canonicalPath

    if (!filePath.startsWith(basePath)) {
      throw new IllegalArgumentException("File path (${filePath}) is not descendent of base path (${basePath}).")
    }

    def relativePath = filePath.substring(basePath.size() + 1)
    return relativePath
  }

}

eventTestCompileEnd = { type ->

  def phasesToRun = [type.name]
  ConfigObject config = mergeConfig()
  if (config.optimize)
  {

    //TODO import this class when http://jira.codehaus.org/browse/GRAILS-5755 is released
    def antInstrConfClass = loadDependencyClass('com.cenqua.clover.tasks.AntInstrumentationConfig')
    def antInstrConfig = antInstrConfClass.getFrom(ant.project)

    //TODO import this class when http://jira.codehaus.org/browse/GRAILS-5755 is released
    def optionsBuilderClass = loadDependencyClass('com.atlassian.clover.api.optimization.OptimizationOptions$Builder')
    def builder = optionsBuilderClass.newInstance()
    def options = builder.enabled(true).
                                    debug(true).
                                    initString(antInstrConfig.initString).
                                    snapshot(defCloverSnapshotFile).build()

    //TODO import this class when http://jira.codehaus.org/browse/GRAILS-5755 is released
    def optimizerClass = loadDependencyClass('com.atlassian.clover.api.optimization.TestOptimizer')

    def optimizer = optimizerClass.newInstance(options)
    // convert the testTargetPatterns into a list of optimizables...

    List optimizables = new ArrayList()

    // for each phase, gather source files and turn into optimizables
    def optimizableClass = loadDependencyClass('com.atlassian.clover.api.optimization.Optimizable')

    phasesToRun.each {phaseName ->

      List<File> files = new LinkedList<File>()
      defStoredTestTargetPatterns.each { files.addAll(scanForSourceFiles(it, binding, phaseName)) }

      files.each {  optimizables << new FileOptimizable(it, new File("test/${phaseName}")) }

    }

    
    List optimizedTests = optimizer.optimize(optimizables)

    final List<GriffonTestTargetPattern> optimizedTestTargetPatterns = new LinkedList<GriffonTestTargetPattern>()
    optimizedTests.each { optimizedTestTargetPatterns << new GriffonTestTargetPattern(createTestPattern(it.className))  }

    testTargetPatterns = optimizedTestTargetPatterns as GriffonTestTargetPattern[];    
  }
}

private String createTestPattern(String name) {
  return name.endsWith("Tests") ? name.substring(0,name.lastIndexOf("Tests")) : name;
}

private List<File> scanForSourceFiles(GriffonTestTargetPattern targetPattern, Binding binding, String phaseName) {
  def sourceFiles = []
  def resolveResources = binding['resolveResources']
  def testSuffixes = ['']
  def testExtensions = ["java", "groovy"]
  def sourceDir = new File("test/${phaseName}")
    
  testSuffixes.each { suffix ->
    testExtensions.each { extension ->
      def resources = resolveResources("file:${sourceDir.absolutePath}/${targetPattern.filePattern}${suffix}.${extension}".toString())
      sourceFiles.addAll(resources*.file.findAll { it.exists() }.toList())
    }
  }

  sourceFiles
}

eventTestPhasesEnd = {
  ConfigObject config = mergeConfig()
  println "Clover: Tests ended"

  if (!config.on && !config.optimize)
  {
    return;
  }

  def historyDir = config.historydir ?: defCloverHistoryDir
  def reportLocation = config.reportdir ?: defCloverReportDir

  def historical = defCloverHistorical
  if (config.historical != null) {
    historical = config.historical
  }

  if (historical)
  {
    if (!config.historypointtask)
    {
      ant.'clover-historypoint'(historyDir: historyDir)
    }
    else
    {
      config.historypointtask(ant, binding)
    }
  }

  if (!config.reporttask)
  {

    ant.'clover-report' {
      ant.current(outfile: reportLocation, title: config.title ?: defCloverReportTitle) {
        format(type: "html")
        ant.columns {
          lineCount()
          filteredElements()
          uncoveredElements()
          totalPercentageCovered()
        }
      }
      if (historical) {
        ant.historical(outfile: reportLocation, historyDir: historyDir)
      }
      ant.current(outfile: "${reportLocation}/clover.xml") {
        format(type: "xml")
      }
      if (config.json) {
        ant.current(outfile: reportLocation) {
          format(type: "json")
        }
      }
    }


    if (config.view) {
      launchReport(reportLocation)
    }
  }
  else
  {
    // reporttask is a user defined closure that takes a single parameter that is a reference to the org.codehaus.gant.GantBuilder instance.
    // this closure can be used to generate a custom html report.
    // see : http://groovy.codehaus.org/Using+Ant+from+Groovy
    config.reporttask(ant, binding, this)
  }

  // TODO: if -clover.optimize, save a snapshot file to -clover.snapshotLocation

  
  if (config.optimize)
  {
    ant.'clover-snapshot'(file: defCloverSnapshotFile)
  }

}

/**
 * Tries to launch a HTML report in your browser.
 *
 * If only a single test was run, then just that test's page will be shown.
 * Otherwise, the dashboard page is displayed. This is useful if using IDEA/Eclipse to run griffon tests.
 * 
 * @param reportLocation the directory containing the report to launch
 * @return
 */
public def launchReport(def reportLocation )
{
  File openFile = new File(reportLocation, "index.html")
  if (openFile.exists())
  {
    if (testNames.size() > 0) // if there is a wildcard in the testname, we can't do anything...
    {
      String testName = testNames[0].replace((char)'.', File.separatorChar)
      String suffix = testName.toString().endsWith("Tests") ? "" : "Tests"
      File testFile = new File(reportLocation, testName + suffix + ".html")
      openFile = testFile.exists() ? testFile : openFile
    }

    String openLoc = openFile.toURI().toString()
    println "About to launch: ${openLoc}"
    com.cenqua.clover.reporters.util.BrowserLaunch.openURL openLoc;
  }
}

def toggleCloverOn(ConfigObject clover)
{

  configureLicense(clover)

  ant.taskdef(resource: 'cloverlib.xml')
  ant.'clover-env'()

  // create an AntInstrumentationConfig object, and set this on the ant project
  def antInstrConfClass = loadDependencyClass('com.cenqua.clover.tasks.AntInstrumentationConfig')

  def antConfig = antInstrConfClass.newInstance(ant.project)
  configureAntInstr(clover, antConfig)
  antConfig.setIn ant.project

  if (clover.setuptask)
  {
    println "Using custom clover-setup configuration."

    clover.setuptask(ant, binding, this)
  }
  else
  {
    println "Using default clover-setup configuration."

    final String initString = clover.get("initstring") != null ? clover.initstring : "${projectWorkDir}/clover/db/clover.db"
    antConfig.initstring = initString

    def cloverSrcDirs = clover.srcDirs ? clover.srcDirs : this.defCloverSrcDirs
    def cloverIncludes = clover.includes ? clover.includes : this.defCloverIncludes
    def cloverExcludes = clover.excludes ? clover.excludes : this.defCloverExcludes

    println """Clover:
               directories: ${cloverSrcDirs}
               includes:    ${cloverIncludes}
               excludes     ${cloverExcludes}"""

    ant.'clover-setup'(initString: initString, tmpDir: "${projectWorkDir}/clover/tmp") {

      cloverSrcDirs.each {dir ->
        if (new File(dir.toString()).exists()) {
          ant.fileset(dir: dir) {
            cloverExcludes.each { exclude(name: it) }
            cloverIncludes.each { include(name: it) }
          }
        }
      }
    }
  }

  if (clover.snapshotLocation) {
    defCloverSnapshotFile = new File(clover.snapshotLocation);
  }
  
}

/**
 * Populates an AntInstrumentationConfig instance with any matching properties in the ConfigObject.
 *
 * Currently only primitive boolean, int and long are supported.
 * As are String.
 *
 */
private def configureAntInstr(ConfigObject clover, def antConfig)
{

  return clover.each {

    if (antConfig.getProperties().containsKey(it.key))
    {

      String setter = MetaProperty.getSetterName(it.key)
      MetaProperty property = antConfig.metaClass.getMetaProperty(it.key.toString())

      final def val;
      switch (property.type)
      {
        case Integer.class.getPrimitiveClass("int"):
          val = it.value.toInteger()
          break;
        case Long.class.getPrimitiveClass("long"):
          val = it.value.toLong()
          break;
        case Boolean.class.getPrimitiveClass("boolean"):
          val = (it.value == null || Boolean.parseBoolean(it.value.toString()))
          break;
        case File.class:
          val = new File(it.value.toString())
          break;
        default:
          val = it.value
      }

      antConfig.invokeMethod(setter, val)
    }
  }
}

private def configureLicense(ConfigObject clover)
{
// the directories to search for a clover.license file
  final String[] licenseSearchPaths = ["${userHome}", "${basedir}", "${basedir}/etc", "${griffonWorkDir}"]

  // the name of the system property that holds the clover license file
  final LICENSE_PROP = 'clover.license.path'

  final license;
  if (clover.license.path)
  {
    license = clover.license.path
  }
  else
  {

    licenseSearchPaths.each {
      final String licensePath = "${it}/clover.license"
      if (new File(licensePath).exists())
      {
        license = licensePath;
        return;
      }
    }
  }

  // check for a bundled eval clover license
  def cloverPluginDir = binding.variables["cloverPluginDir"]
  final File evalLicense = new File(cloverPluginDir, "griffon-app/conf/clover/clover-evaluation.license")
  if (!license && evalLicense.exists()) {
    license = evalLicense.getAbsolutePath()
  }

  if (!license)
  {
    println """
               No clover.license configured. Please define license.path=/path/to/clover.license in the
               clover configuration in conf/BuildConfig.groovy"""
  }
  else
  {
    System.setProperty LICENSE_PROP, license
    println "Using clover license path: ${System.getProperty LICENSE_PROP}"
  }
}

private void toggleAntLogging(ConfigObject clover)
{
// get any BuildListeners and turn logging on
  if (clover.debug)
  {
    ant.project.buildListeners.each {listener ->
      if (listener instanceof BuildLogger)
      {
        listener.messageOutputLevel = Project.MSG_DEBUG
      }
    }
  }
}

/**
 * Takes any CLI arguments and merges them with any configuration defined in BuildConfig.groovy in the clover block.
 */
private def ConfigObject mergeConfig()
{

  final Map argsMap = parseArguments()
  final ConfigObject config = buildConfig.clover == null ? new ConfigObject() : buildConfig.clover

  final ConfigSlurper slurper = new ConfigSlurper()
  final Properties props = new Properties()
  props.putAll(argsMap)

  final ConfigObject argsMapConfig = slurper.parse(props)
  config.merge(argsMapConfig.clover)

  return config

}

// Copied from _GriffonArgParsing.groovy since _GriffonCompile.groovy does not depend on parseArguments target
// and the argsMap is not populated in time for the testStart event.
// see: http://jira.codehaus.org/browse/GRAILS-2663

private Map parseArguments()
{
  // Only ever parse the arguments once. We also don't bother parsing
  // the arguments if the "args" string is empty.
//    if (argsMap.size() > 1 || argsMap["params"] || !args) return
  argsMap = [params: []]

  args?.tokenize().each {token ->
    def nameValueSwitch = token =~ "--?(.*)=(.*)"
    if (nameValueSwitch.matches())
    { // this token is a name/value pair (ex: --foo=bar or -z=qux)
      final def value = nameValueSwitch[0][2]
      argsMap[nameValueSwitch[0][1]] = "false".equalsIgnoreCase(value) ? false : value;
    }
    else
    {
      def nameOnlySwitch = token =~ "--?(.*)"
      if (nameOnlySwitch.matches())
      {  // this token is just a switch (ex: -force or --help)
        argsMap[nameOnlySwitch[0][1]] = true
      }
      else
      { // single item tokens, append in order to an array of params
        argsMap["params"] << token
      }
    }
  }

  if (argsMap.containsKey('non-interactive'))
  {
    println "Setting non-interactive mode"
    isInteractive = !(argsMap.'non-interactive')
  }
  return argsMap
}
