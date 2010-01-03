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

// Clobber griffon-app/conf/Builder.groovy
println 'Setting SwtBuilder on Builders.groovy'
println 'Setting SWTGriffonAddon on Builders.groovy'
new File("${basedir}/griffon-app/conf/Builder.groovy").text = """
root.'groovy.swt.SwtBuilder'.view = '*'
root.'SWTGriffonAddon'.addon = true
"""

def mainClass = "griffon.swt.SWTApplication"
def configSlurper1 = new ConfigSlurper()
def buildconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Config.groovy").toURL())
if(!(mainClass in buildconf.flatten().'griffon.application.mainClass')) {
    println "Setting '$mainClass' as main class"
    new File("$basedir/griffon-app/conf/Config.groovy").append("""
griffon.application.mainClass = "$mainClass"
""")
}

confirmInput = {String message ->
    ant.input(message: message, addproperty: "confirm.message", validargs: "y,n")
    ant.antProject.properties."confirm.message"
}

// Replace Swing views
new File("${basedir}/griffon-app/views").eachFileMatch(~/.*View\.groovy/) { view ->
    if(view.text =~ /application\(/) {
        if(confirmInput("Would you like to replace ${view.name} with an SWT view?")) {
            view.text = """import org.eclipse.swt.layout.GridData

application(text: "SWT shell", location:[100,100], size:[280, 70]) {
    gridLayout(numColumns:1)
    cLabel(background: "#fff777", "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog",
           layoutData: gridData(horizontalAlignment: GridData.FILL, grabExcessHorizontalSpace:true))
}
"""
        }
    }
}
