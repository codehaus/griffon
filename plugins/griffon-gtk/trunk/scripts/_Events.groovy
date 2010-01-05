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
    config.griffon.application.mainClass = 'griffon.gtk.GtkApplication'
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def gtkLibDir = "${getPluginDirForName('gtk').file}/lib".toString()
        ant.fileset(dir: gtkLibDir, includes: "*.jar").each {
            if(it.name =~ /griffon.gtk.addon.*/) {
                griffonCopyDist(it.toString(), jardir)
            }
        }

        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: gtkLibDir, includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            copyPlatformJars(gtkLibDir, jardir)
            copyNativeLibs(gtkLibDir, jardir)
            // java-gnome expects the native lib to be in the same dir
            // as the jar so we have to move it
            ant.move(todir: "${jardir}/linux") {
                fileset(dir: "${jardir}/linux/native", includes: "*.so")
            }
        }
    }
}
