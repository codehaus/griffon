/*
 * griffon-abeilleformbuilder: AbeilleForms Griffon plugin
 * Copyright 2010 and beyond, Jim Shingler
 *
 * AbeilleForms is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

/**
 * @author Jim Shingler
 */
class AbeilleformBuilderGriffonPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.2 > *'
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'GNU Lesser Public License 2.1'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = "Jim Shingler"
    def authorEmail = "ShinglerJim@gmail.com"
    def title = "A Builder for Abeille Forms"
    def description = '''\\
This plugin installs the AbeilleFormsBuilder.  The builder helps integrate forms (".jfrm") into griffon
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/AbeilleformBuilder+Plugin"
}
