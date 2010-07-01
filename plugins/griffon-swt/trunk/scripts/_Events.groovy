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

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.swt.SWTApplication'
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def swtLibDir = "${getPluginDirForName('swt').file}/lib".toString()
        ant.fileset(dir: swtLibDir, includes: "*.jar").each {
            if(it.name =~ /griffon.swt.addon.*/) {
                griffonCopyDist(it.toString(), jardir)
            }
        }

        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: swtLibDir, includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            copyPlatformJars(swtLibDir, jardir)
            copyNativeLibs(swtLibDir, jardir)
        }
    }
}

