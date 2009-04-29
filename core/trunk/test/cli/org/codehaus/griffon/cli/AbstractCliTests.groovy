package org.codehaus.griffon.cli

import gant.Gant
import org.codehaus.griffon.util.BuildSettings
import org.codehaus.griffon.util.BuildSettingsHolder
import org.codehaus.gant.GantBinding
import org.codehaus.griffon.cli.support.GriffonRootLoader
import org.codehaus.griffon.commons.ConfigurationHolder
import org.codehaus.griffon.plugins.GriffonPluginUtils
import org.codehaus.griffon.plugins.PluginManagerHolder

abstract class AbstractCliTests extends GroovyTestCase {
    String scriptName

    protected appBase = "test/cliTestApp"
	protected ant = new AntBuilder()

    private GantBinding binding
    private ClassLoader savedContextLoader

    /**
     * Creates a new test case for the script whose name matches the
     * name of the test (minus any package and "Tests" suffix).
     */
    AbstractCliTests() {
        def name = getClass().name
        def pos = name.lastIndexOf(".")
        if (pos != -1) {
            name = name[pos+1..-1]
        }

        if (name.endsWith("Tests")) {
            name = name[0..-6]
        }

        this.scriptName = name
    }

    AbstractCliTests(String scriptName) {
        this.scriptName = scriptName
    }

    void setUp() {
        ExpandoMetaClass.enableGlobally()
        ant.delete(dir:appBase, failonerror:false)
        System.setProperty("base.dir", appBase)
        System.setProperty("griffon.cli.args", "testapp")

        savedContextLoader = Thread.currentThread().contextClassLoader
    }

	void tearDown() {
        Thread.currentThread().contextClassLoader = savedContextLoader

        ant.delete(dir:appBase, failonerror:false)

        ExpandoMetaClass.disableGlobally()

        BuildSettingsHolder.settings = null
        ConfigurationHolder.config = null
        PluginManagerHolder.pluginManager = null
        ant = null
	}

	protected String createTestApp(appName = "testapp") {
        // Pass the name of the test project to the create-app script.
        System.setProperty("griffon.cli.args", appName)

        // Create the application.
	    gantRun("CreateApp_")

	    // Update the base directory to the application dir.
        def appDir = appBase + File.separator + appName
        System.setProperty("base.dir", appDir)

		// Finally, clear the CLI arguments.
        System.setProperty("griffon.cli.args", "")

        // Return the path to the new app.
        return appDir
    }

	protected void gantRun() {
        gantRun(this.scriptName)
    }

    protected void gantRun(String scriptName) {
        def workDir = "${appBase}/work"
        def projectDir = "${System.getProperty("base.dir")}/work"
        GriffonPluginUtils.clearCaches()
        System.setProperty("griffon.script.profile","true")

        // Configure the build settings directly rather than using
        // system properties and the BuildSettings.groovy file. Note that
        // the order here is important for things to work!
        def settings = new BuildSettings(".")
        settings.rootLoader = new GriffonRootLoader([] as URL[], getClass().classLoader)
        settings.loadConfig()
        settings.griffonWorkDir = new File(workDir)
        settings.projectWorkDir = new File(projectDir)
        settings.classesDir = new File("$projectDir/classes")
        settings.resourcesDir = new File("$projectDir/resources")
        settings.testClassesDir = new File("$projectDir/test-classes")
        settings.projectPluginsDir = new File("$projectDir/plugins")
        settings.globalPluginsDir = new File("$workDir/global-plugins")

        // Set up a binding for Gant and put some essential variables
        // in there.
        binding = new GantBinding()
        binding.with {
            // Core properties.
            griffonSettings = settings
            basedir = settings.baseDir.path
            baseFile = settings.baseDir
            baseName = settings.baseDir.name
            griffonHome = settings.griffonHome?.path
            griffonVersion = settings.griffonVersion
            userHome = settings.userHome
            griffonEnv = settings.griffonEnv
            defaultEnv = settings.defaultEnv
            buildConfig = settings.config
            rootLoader = settings.rootLoader

            // Add the project paths too!
            griffonWorkDir = settings.griffonWorkDir.path
            projectWorkDir = settings.projectWorkDir.path
            classesDirPath = settings.classesDir.path
            testDirPath = settings.testClassesDir.path
            resourcesDirPath = settings.resourcesDir.path
            pluginsDirPath = settings.projectPluginsDir.path
            globalPluginsDirPath = settings.globalPluginsDir.path

            // Closure for specifying script dependencies.
            griffonScript = { return new File("./scripts/${it}.groovy") }
        }

        BuildSettingsHolder.settings = settings

        def classLoader = new URLClassLoader([ settings.classesDir.toURI().toURL() ] as URL[], settings.rootLoader)
        Thread.currentThread().contextClassLoader = classLoader

        def gant = new Gant(binding, classLoader)
        gant.loadScript(new File("./scripts/${scriptName}.groovy"))
        gant.processTargets()
    }

    protected GantBinding getBinding() {
        return this.binding
    }
}
