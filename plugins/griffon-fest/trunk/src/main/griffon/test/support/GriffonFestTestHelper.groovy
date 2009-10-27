package griffon.test.support

import junit.framework.TestSuite
import org.codehaus.griffon.util.BuildSettings
import org.codehaus.griffon.test.DefaultGriffonTestHelper
import griffon.util.IGriffonApplication

class GriffonFestTestHelper extends DefaultGriffonTestHelper {
    private IGriffonApplication app
    private config
	private cl

    GriffonFestTestHelper(
		BuildSettings settings,
		ClassLoader parentLoader,
		Closure resourceResolver,
	    Object config) {
        super(settings, parentLoader, resourceResolver)
		this.config = config
    }

    TestSuite createTestSuite() {
		createApp()
        new GriffonFestTestSuite(app, testSuffix)
    }

    TestSuite createTestSuite(Class clazz) {
		createApp()
        new GriffonFestTestSuite(app, clazz, testSuffix)
    }

	private void createApp() {
		if(!app) {
			String appClassName = config?.griffon?.application?.mainClass ?: "griffon.application.SwingApplication"
			app = currentClassLoader.loadClass(appClassName).newInstance()
			app.bootstrap()
			app.realize()
		}
	}
}
