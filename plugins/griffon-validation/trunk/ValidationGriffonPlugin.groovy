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

/**
 * @author Nick Zhu
 */
class ValidationGriffonPlugin {
     // the plugin version
    def version = "0.9"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.3 > *'
    // the other plugins this plugin depends on
    def dependsOn = [i18n:'0.4']
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

    def author = "Nick Zhu"
    def authorEmail = "nzhu@jointsource.com"
    def title = "Griffon Validation Plugin"
    def description = 'A validation plugin that provides Grails like validation capability using constraints'

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Validation+Plugin'
}
