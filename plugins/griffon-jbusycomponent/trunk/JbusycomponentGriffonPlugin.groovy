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
class JbusycomponentGriffonPlugin {
    def version = '0.5.2'
    def dependsOn = ['swingx-builder': '0.4', jxlayer: 0.2]
    def toolkits = ['swing']
    def griffonVersion = '0.9.2 > *'
    def license = 'Apache Software License 2.0'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Enhance any swing components with a \'busy\' state'
    def description = '''
JBusyComponent: Enhance any swing components with a 'busy' state.
http://code.google.com/p/jbusycomponent
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/JBusyComponent+Plugin'
}
