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
    if(buildConfig.griffon.extensions) {
        buildConfig.griffon.extensions.jnlpUrls << "http://worldwind.arc.nasa.gov/java/0.3.0/webstart/worldwind.jnlp"
        if(!buildConfig.griffon.extensions.props) buildConfig.griffon.extensions.props = new ConfigObject()
        buildConfig.griffon.extensions.props.'sun.java2d.noddraw' = true
    }
}

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('worldwind')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-worldwind-plugin', dirs: "${worldwindPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('worldwind', [
        conf: 'compile',
        name: 'griffon-worldwind-addon',
        group: 'org.codehaus.griffon.plugins',
        version: worldwindPluginVersion
    ])
}

