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

parseArguments()

confirmInput = {String message ->
    def propName = "confirm.message" + System.currentTimeMillis()
    ant.input(message: message, addproperty: propName, validargs: "y,n")
    ant.antProject.properties[propName].toLowerCase() == 'y'
}

nonagYes = 'y'.equalsIgnoreCase(argsMap.nonag) ?: false
nonagNo = 'n'.equalsIgnoreCase(argsMap.nonag) ?: false

doOrAsk = { message, callback ->
    if(nonagNo) return
    boolean proceed = nonagYes
    if(!proceed) {
        proceed = confirmInput(message)
    }
    if(proceed) callback()
}

// Clobber griffon-app/conf/Builder.groovy
println 'Setting PivotBuilder on Builders.groovy'
println 'Setting PivotGriffonAddon on Builders.groovy'
new File("${basedir}/griffon-app/conf/Builder.groovy").text = """
root.'griffon.pivot.PivotBuilder'.view = '*'
root.'PivotGriffonAddon'.addon = true
"""

def mainClass = 'griffon.pivot.DesktopPivotApplication'
def appletClass = 'org.apache.pivot.wtk.BrowserApplicationContext$HostApplet'
def appletClass1 = 'org.apache.pivot.wtk.BrowserApplicationContext[:]'
def configSlurper1 = new ConfigSlurper()
def buildconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Config.groovy").toURL())
if(!(mainClass in buildconf.flatten().'griffon.application.mainClass')) {
    println "Setting '$mainClass' as main class"
    new File("$basedir/griffon-app/conf/Config.groovy").append("""
griffon.application.mainClass = "$mainClass"
""")
}
if(appletClass1.toString() != buildconf.flatten().'griffon.applet.mainClass') {
    println "Setting '$appletClass' as applet class"
    new File("$basedir/griffon-app/conf/Config.groovy").append("""
griffon.applet.mainClass = "$appletClass"
""")
}

// Replace Swing views
new File("${basedir}/griffon-app/views").eachFileMatch(~/.*View\.groovy/) { view ->
    if(view.text =~ /application\(/) {
        doOrAsk("Would you like to replace ${view.name} with a Pivot view?") {
            println "Replacing ${view.name} with Pivot code..."
            view.text = """import org.apache.pivot.wtk.HorizontalAlignment
import org.apache.pivot.wtk.VerticalAlignment
import java.awt.Font
import java.awt.Color

application(title: "Pivot Window", maximized: true) {
    label(text: "Hello Griffon!", font: new Font('Arial', Font.BOLD, 24), color: Color.RED,
          horizontalAlignment: HorizontalAlignment.CENTER, verticalAlignment: VerticalAlignment.CENTER)
}
"""
        }
    }
}
