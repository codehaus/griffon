/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

includeTargets << griffonScript('_GriffonCreateProject')

configText = '''root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }

    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}'''
builderConfigFile.text -= configText    

configText = '''
root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
}'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding groovy.swing.SwingBuilder to Builder.groovy'
    builderConfigFile.text += configText
}

configText = '''
root.'SwingGriffonAddon'.addon=true
'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding SwingGriffonAddon to Builder.groovy'
    builderConfigFile.text += configText
}

tempWorkDir = new File("${basedir}/swing-install-tmp")
griffonUnpack(dest: tempWorkDir, src: "griffon-$projectType-files.jar")

resolveFileType()

initializeScriptOrClassInApp = new File("${basedir}/griffon-app/lifecycle/Initialize${fileType}")
initializeScriptOrClassInFiles = new File("${tempWorkDir}/griffon-app/lifecycle/Initialize${fileType}")
initializeScriptOrClassInPlugin = new File("${swingPluginDir}/src/templates/griffon-app/lifecycle/Initialize${fileType}")

if(initializeScriptOrClassInApp.text == initializeScriptOrClassInFiles.text) {
    initializeScriptOrClassInApp.text = initializeScriptOrClassInPlugin.text
}

ant.delete(quiet: true, failonerror: false, dir: tempWorkDir) 
