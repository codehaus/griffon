/*
 * Copyright 2009-2011 the original author or authors.
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

eventCreateConfigEnd = {
    buildConfig.griffon.application.mainClass = 'griffon.swt.SWTApplication'
    if(!buildConfig.griffon.app.javaOpts) buildConfig.griffon.app.javaOpts = []
    buildConfig.griffon.app.javaOpts << '-XstartOnFirstThread'
    
    if(!isPluginProject) {
        def runtimeConfigFile = new File("${basedir}/griffon-app/conf/Config.groovy")
        def runtimeConfig = new ConfigSlurper().parse(runtimeConfigFile.text)
        if(!runtimeConfig.platform.handler.macosx) {
            runtimeConfigFile.append("platform.handler.macosx = 'griffon.swt.DefaultMacOSXPlatformHandler'")
        }
    }
}

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('swt')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-swt-plugin', dirs: "${swtPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('swt', [
        conf: 'compile',
        name: 'griffon-swt-addon',
        group: 'org.codehaus.griffon.plugins',
        version: swtPluginVersion
    ])
}
