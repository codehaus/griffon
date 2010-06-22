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
 * @author Ixchel Ruiz
 */
class OxbowGriffonPlugin {
    def version = 0.2
    def dependsOn = [miglayout: 0.1]
    def toolkits = ['swing']
    def griffonVersion = '0.9 > *'
    def license = 'Apache Software License 2.0'

    def author = "Ixchel Ruiz"
    def authorEmail = "ixchelruiz@yahoo.com"
    def title = "Based on Oxbow project"
    def description = '''
A collection of projects for Swing UI enhancements based on Oxbow project
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Oxbow+Plugin"
}
