/*
 * Copyright 2010 the original author or authors.
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

import org.codehaus.griffon.util.BuildSettings

includeTargets << griffonScript("_GriffonSettings")

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

eventCreateConfigEnd = {
    config.griffon.application.mainClass = 'griffon.charva.CharvaApplication'
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        if(packagingType in ['applet', 'webstart']) {
           ant.fail("Charva does not support $type mode.") 
        }

        def charvaLibDir = "${getPluginDirForName('charva').file}/lib".toString()
        ant.fileset(dir: charvaLibDir, includes: "*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
        copyPlatformJars(charvaLibDir, jardir)
        copyNativeLibs(charvaLibDir, jardir)
    }
}
