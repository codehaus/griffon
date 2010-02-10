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
firstTime = !metadata.'plugins.pivot'

updateMetadata('app.toolkits': 'pivot')

if(firstTime) {
    def builderConfigFile = new File("${basedir}/griffon-app/conf/Builder.groovy")
    def addonPattern = ~/^.+\.addon=true$/
    def addonsCopied = false
    def builderConf = new StringBuffer("/* PIVOT_PLUGIN_COMMENT_START\n")
    builderConf.append(builderConfigFile.text)
    builderConf.append("PIVOT_PLUGIN_COMMENT_END */\n")
    builderConf.append("// ADDED_BY_PIVOT_PLUGIN_START\n")
    builderConf.append("root.'griffon.pivot.PivotBuilder'.view = '*'\n")
    builderConf.append("root.'PivotGriffonAddon'.addon=true\n")
    builderConfigFile.text.eachLine { line ->
        if(line =~ addonPattern) {
            addonsCopied = true
            builderConf.append(line).append('\n')
        }
    }
    builderConf.append("\n// ADDED_BY_PIVOT_PLUGIN_END\n")
    builderConfigFile.text = builderConf.toString()
    
    if(addonsCopied) {
        printFramed("""Please review griffon-app/conf/Builder.groovy as some addons
were relocated. Make sure any ommisions are inserted in the
same block as PivotGriffonAddon.""")
    }
}

// Replace views
new File("${basedir}/griffon-app/views").eachFileMatch(~/.*View\.groovy/) { view ->
    if(view.text =~ /application\(/) {
        askAndDoNoNag("Would you like to replace ${view.name} with a Pivot view?") {
            println "Replacing ${view.name} with Pivot code..."
            view.text = """application(title: '$griffonAppName', maximized: true) {
    label('Hello Griffon!', styles: "{font:'Arial bold 24', color:'#ff0000', horizontalAlignment:'center', verticalAlignment:'center'}")
}
"""
        }
    }
}
