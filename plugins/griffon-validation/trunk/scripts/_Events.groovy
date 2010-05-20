/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @author nick.zhu
 */

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('validation').file}/lib/", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'constraint' }) {
        artifactsInfo << [type: 'constraint', path: 'constraints', suffix: 'constraint']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'constraint'} ) {
        pathToInfo << [name: 'Constraint', path: 'constraints', filetype: ['.groovy','.java']]
    }
}

