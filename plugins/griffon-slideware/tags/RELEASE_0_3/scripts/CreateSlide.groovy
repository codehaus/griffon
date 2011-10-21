/*
 * Copyright 2009-2011 the original author or authors.
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
 
import griffon.util.GriffonNameUtils

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target (default : "Creates a Slide view") {
    depends(checkVersion, parseArguments)

    def targetDir = new File("${basedir}/griffon-app/slides")
    targetDir.mkdirs()
    
    int count = 1
    targetDir.eachFile { file ->
        def m = file.name =~ /Page(\d+)Slide\.groovy/
        if(m) {
           count = Math.max(count, (m[0][1] as int) + 1)
        }
    }

    def slideName = "Page${count}Slide"
    argsMap.skipPackagePrompt = true

    createArtifact(
        name: slideName,
        suffix: "",
        type: "Slide",
        path: "griffon-app/slides")

    def artifactFile = "${basedir}/griffon-app/slides/${slideName}.groovy"

    ant.replace(file: artifactFile) {
        replacefilter(token: "@count@", value: count )
    }
}
