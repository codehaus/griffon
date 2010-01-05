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

//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

includeTargets << griffonScript('_GriffonArgParsing')

appToolkits = metadata.'app.toolkits'
if(!appToolkits) appToolkits = ''
appToolkits = appToolkits.split(',').toList()
firstTime = !appToolkits.contains('gtk')

updateMetadata('app.toolkits': 'gtk')

if(firstTime) {
    def builderConfigFile = new File("${basedir}/griffon-app/conf/Builder.groovy")
    def addonPattern = ~/^.+\.addon=true$/
    def addonsCopied = false
    def builderConf = new StringBuffer("/* GTK_PLUGIN_COMMENT_START\n")
    builderConf.append(builderConfigFile.text)
    builderConf.append("GTK_PLUGIN_COMMENT_END */\n")
    builderConf.append("// ADDED_BY_GTK_PLUGIN_START\n")
    builderConf.append("root.'griffon.gtk.GtkBuilder'.view = '*'\n")
    builderConf.append("root.'GtkGriffonAddon'.addon=true\n")
    builderConfigFile.text.eachLine { line ->
        if(line =~ addonPattern) {
            addonsCopied = true
            builderConf.append(line).append('\n')
        }
    }
    builderConf.append("\n// ADDED_BY_GTK_PLUGIN_END\n")
    builderConfigFile.text = builderConf.toString()
    
    if(addonsCopied) {
        printFramed("""Please review griffon-app/conf/Builder.groovy as some addons
were relocated. Make sure any ommisions are inserted in the
same block as GtkGriffonAddon.""")
    }
}

// Replace views
new File("${basedir}/griffon-app/views").eachFileMatch(~/.*View\.groovy/) { view ->
    if(view.text =~ /application\(/) {
        askAndDoNoNag("Would you like to replace ${view.name} with a Gtk view?") {
            println "Replacing ${view.name} with Gtk code..."
            view.text = """application(title: '$griffonAppName', width: 320, height: 240,
            icon: pixbuf('griffon-icon-24x24.png')) {
    vbox(spacing: 3) {
        label(label: "Hello Griffon!")
        button(label: "Press me!", onClicked: {println "I was clicked: \${it.label}"})
    }
}
"""
        }
    }
}
