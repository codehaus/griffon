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
class JdependGriffonPlugin {
    def version = '0.3'
    def dependsOn = [:]
    def griffonVersion = '0.9 > *'
    def license = 'Apache Software License 2.0'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Runs JDepend metrics on your Griffon application'
    def description = '''
JDepend traverses Java class file directories and generates design quality metrics for each Java package.
JDepend allows you to automatically measure the quality of a design in terms of its extensibility, reusability, 
and maintainability to manage package dependencies effectively.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Jdepend+Plugin'
}
