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
 * @author Josh A. Reed
 */
class MailGriffonPlugin {
    def version = 0.3
    def canBeGlobal = false
    def griffonVersion = '0.9.2 > *'
    def license = 'Apache Software License 2.0'

    def author = "Josh Reed"
    def authorEmail = "jareed@andrill.org"
    def title = "Send email from your Griffon app"
    def description = '''\\
Send email from your Griffon app
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Mail+Plugin"
}
