/*
 * Copyright 2010 the original author or authors.
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
class P6spyGriffonPlugin {
    def version = 0.1
    def griffonVersion = "0.3 > *" 
    def dependsOn = [:]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "P6spy support"
    def description = '''\\
P6Spy plugin adds the p6spy library to your Grails application.
P6Spy lets you monitor the JDBC queries by proxying your database driver.
In addition to logging the prepared statements, it also logs the sql with
parameters in place so you can copy and paste the exact sql into your favourite
database client to test the results.

Visit the p6spy website at http://p6spy.com/

Based on Grails' P6spy plugin http://grails.org/plugin/p6spy
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/P6spy+Plugin"
}
