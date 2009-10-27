package griffon.test.support

import griffon.util.IGriffonApplication
import junit.framework.Test
import junit.framework.TestResult
import junit.framework.TestSuite
import org.codehaus.groovy.runtime.InvokerHelper
 
class GriffonFestTestSuite extends TestSuite {
    private IGriffonApplication app

    public GriffonFestTestSuite(IGriffonApplication app, String testSuffix) {
        super()
        init(app, testSuffix)
    }

    public GriffonFestTestSuite(IGriffonApplication app, Class clazz, String testSuffix) {
		super(clazz)
        init(app, testSuffix)
    }

	public void runTest(final Test test, final TestResult result) {
		test.app = app
		test.run(result)
	}

    private void init(IGriffonApplication app, String testSuffix) {
 		this.app = app
    }
}
