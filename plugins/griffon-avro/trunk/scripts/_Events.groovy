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

includeTargets << griffonScript('Init')
includePluginScript('avro', 'Avro')

eventCompileStart = { 
    if(compilingAvroPlugin()) return
    avro()
}

eventCleanEnd = {
    ant.delete(dir: "${projectWorkDir}/avro", quiet: false)
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('avro').file}/lib", includes: '*.jar').each {
        if(it.toString() =~ "avro-tools") return
        griffonCopyDist(it.toString(), jardir)
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{it.path == 'src.avro'} ) {
        pathToInfo << [name: 'Avro Sources', path: 'src.avro', filetype: ['.avpr', '.avsc']]
    }
}

private boolean compilingAvroPlugin() {
    getPluginDirForName('avro')?.file?.canonicalPath == basedir
}
