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

    def slideName
    if(!argsMap["params"]) {
        int count = 1
        targetDir.eachFile { file ->
            def m = file.name =~ /Page(\d+)Slide\.groovy/
            if(m) {
               count = Math.max(count, (m[0][1] as int) + 1)
            }
        }
        slideName = "Page${count}"
    } else {
        slideName = argsMap["params"][0]
    }
    argsMap.skipPackagePrompt = true

    createArtifact(
        name: "${slideName}Slide",
        suffix: "",
        type: "Slide",
        path: "griffon-app/slides")

    def artifactFile = "${basedir}/griffon-app/slides/${slideName}Slide.groovy"

    ant.replace(file: artifactFile) {
        replacefilter(token: "@name@", value: slideName )
    }

    def configFile = new File("${basedir}/griffon-app/conf/Config.groovy")
    def configText = configFile.text
    def matcher = configText =~ /\s*presentation\s*\{/
    if (! matcher) {
        configText += """
presentation {
    screenWidth = 1024
    screenHeight = 768
    order = [
        "${slideName}"
    ]
}
"""
    } else {
        def m = configText =~ /(?ms)\s*presentation\s*\{.*order\s*=\s*(\[.*\]).*}/
        def order = evaluate(m[0][1])
        order << slideName
        def newOrder = order.inspect().replaceAll(/, /, ',\n\t').replaceAll(/\[/, '[\n\t').replaceAll(/\]/, '\n]')
        configText = m.replaceFirst(newOrder)
    }
    configFile.write configText
}
