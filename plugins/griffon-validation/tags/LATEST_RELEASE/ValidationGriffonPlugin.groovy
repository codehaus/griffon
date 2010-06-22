/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

class ValidationGriffonPlugin {
    def version = 0.4
    def griffonVersion = "0.3 > *"
    def dependsOn = [:]
    // optional. Valid values are: swing, javafx, swt, pivot, gtk
    // def toolkits = ['swing']
    // optional. Valid values are linux, windows, macosx, solaris
    // def platforms = []

    def author = "Nick Zhu"
    def authorEmail = "nzhu@jointsource.com"
    def title = "Griffon Validation Plugin"
    def description = '''
A validation plugin that provides Grails like validation capability using constraint
declaration without depending on Spring
'''

    // URL to the plugin's documentation
    def documentation = "http://gvalidation.sourceforge.net/"
}
