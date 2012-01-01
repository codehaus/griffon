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

configText = '''root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
}'''
if(builderConfigFile.text.contains(configText)) {
    println 'Removing groovy.swing.SwingBuilder from Builder.groovy'
    builderConfigFile.text -= configText
}

/*
configText = '''root.'SwingGriffonAddon'.addon=true'''
if(builderConfigFile.text.contains(configText)) {
    println 'Removing SwingGriffonAddon from Builder.groovy'
    builderConfigFile.text -= configText
}
*/

initializeFile = new File(basedir, '/griffon-app/lifecycle/Initialize.groovy')
initializeFile.text -= 'import groovy.swing.SwingBuilder\n'
initializeFile.text -= 'import static griffon.util.GriffonApplicationUtils.isMacOSX\n'
initializeFile.text -= "SwingBuilder.lookAndFeel((isMacOSX ? 'system' : 'nimbus'), 'gtk', ['metal', [boldFonts: false]])\n"

/*
griffonSettings.dependencyManager.removePluginDependency('swing', [
    conf: 'compile',
    name: 'griffon-swing-addon',
    group: 'org.codehaus.griffon.plugins',
    version: swingPluginVersion
])
*/