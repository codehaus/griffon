/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres.Almiray
 */
class CloverGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9 > *' 
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Clover Code Coverage for Griffon'
    def description = '''
A plugin that integrates Clover into GRIFFON projects.
Clover is a Code Coverage tool that produces beautiful reports which are highly configurable and extensible.
Clover not only reports which lines of code were executed during a test run, it also reports which tests hit which lines of code.
Clover's reports include:
* Tag Clouds of your entire project that compare Complexity with Coverage
* A TreeMap to visualise the most complex, uncovered areas of your project
* Fully Cross Referenced HTML view of your source code
* Much more: See http://atlassian.com/clover 
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Clover+Plugin'
}
