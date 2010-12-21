/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('quartz')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-quartz-plugin', dirs: "${quartzPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('quartz', [
        conf: 'compile',
        name: 'griffon-quartz-addon',
        group: 'org.codehaus.griffon.plugins',
        version: quartzPluginVersion
    ])
    griffonSettings.dependencyManager.addPluginDependency('quartz', [
        conf: 'build',
        name: 'griffon-quartz-cli',
        group: 'org.codehaus.griffon.plugins',
        version: quartzPluginVersion
    ])
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'job' }) {
        artifactsInfo << [type: 'job', path: 'jobs', suffix: 'Job']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'jobs'} ) {
        pathToInfo << [name: 'Quartz Jobs', path: 'jobs', filetype: ['.groovy']]
    }
}
