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

/**
 * @author Andres Almiray
 */
class ScalaGriffonPlugin {
    def version = "0.5.2"
    def canBeGlobal = true
    def dependsOn = ["lang-bridge": "0.2.1"]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Brings the Scala language compiler and libraries"
    def description = '''\\
This plugin enables the usage of Scala classes in your Griffon application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Scala+Plugin"
}
