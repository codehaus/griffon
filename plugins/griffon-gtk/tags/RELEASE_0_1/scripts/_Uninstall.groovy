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
// This script is executed by Griffon when the plugin is uninstalled from project.
// Use this script if you intend to do any additional clean-up on uninstall, but
// beware of messing up SVN directories!
//

appToolkits = metadata.'app.toolkits'.split(',').toList() - 'gtk'
if(appToolkits) {
    updateMetadata('app.toolkits': appToolkits.join(','))
} else {
    metadata.remove('app.toolkits')
    metadata.persist()
}

def builderConfigFile = new File("${basedir}/griffon-app/conf/Builder.groovy")
def builderConfigText = new StringBuffer()
boolean insideOldDefs = false
boolean insideGtkPluginDefs = false
builderConfigFile.eachLine { line ->
    switch(line) {
        case ~/.*GTK_PLUGIN_COMMENT_START.*/: insideOldDefs = true;  break
        case ~/.*GTK_PLUGIN_COMMENT_END.*/: insideOldDefs = false; break
        case ~/.*ADDED_BY_GTK_PLUGIN_START.*/: insideGtkPluginDefs = true; break
        case ~/.*ADDED_BY_GTK_PLUGIN_END.*/: insideGtkPluginDefs = false; break
        default:
            if(insideOldDefs || !insideGtkPluginDefs) builderConfigText += line + '\n'
    }
}
builderConfigFile.text = builderConfigText

printFramed("""Please review your View scripts as they may contain
Gtk specific nodes which won't work after uninstalling
""")
