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

package griffon.test.support

import junit.framework.TestSuite
import org.codehaus.griffon.util.BuildSettings
import org.codehaus.griffon.test.DefaultGriffonTestHelper
import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
class GriffonFestTestHelper extends DefaultGriffonTestHelper {
    private GriffonApplication app
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
