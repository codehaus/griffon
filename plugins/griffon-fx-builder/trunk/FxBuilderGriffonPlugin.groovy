/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */
class FxBuilderGriffonPlugin {
    def version = '0.4'
    def dependsOn = ['lang-bridge': 0.4]
    def toolkits = ['javafx, swing']
    def griffonVersion = '0.9 > *'
    def license = 'Apache Software License 2.0'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@user.sourceforge.net'
    def title = 'Enables JavaFX on your Griffon application'
    def description = '''
Enables JavaFX on your Griffon application. Brings the FxBuilder and dependencies libraries, enables
the usage of the JavaFX Script compiler on your application.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/FxBuilder+Plugin'
}
