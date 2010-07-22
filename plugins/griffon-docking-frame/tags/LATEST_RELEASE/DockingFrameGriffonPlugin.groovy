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
 * @author Per Junel
 * @author Christoph Lipp
 */
class DockingFrameGriffonPlugin {
    def version = 0.2
    def griffonVersion = "0.9 > *" 
    def toolkits = ['swing']
    def license = 'Apache Software License 2.0'
    def dependsOn = [:]

    def author = "Hackergarten"
    def authorEmail = "hackergarten@googlegroups.com"
    def title = "Griffon docking frame add-on"
    def description = '''\\
The docking frame add-on for Griffon, alows the user to have movable panels, or other components, in a frame, the docking frame.
The panels can be rearranged inside the frame, stacked on top of each other (i.e. creating a tabbed pane), or even moved to
another docking frame in the application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/DockingFrame+Plugin"
}
