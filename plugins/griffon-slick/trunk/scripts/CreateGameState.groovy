/*
 * Copyright (c) 2010 Griffon Slick - Andres Almiray. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 *  o Neither the name of Griffon Slick - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.codehaus.griffon.commons.GriffonClassUtils as GCU

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target(createGameState : "Creates a new Game State") {
    depends(checkVersion, parseArguments)

    if(isPluginProject && !isAddonPlugin) {
        println """You must create an Addon descriptor first.
Type in griffon create-addon then execute this command again."""
        System.exit(1)        
    }

    promptForName(type: "Game State")
    def (pkg, name) = extractArtifactName(argsMap['params'][0])
    def fqn = "${pkg?pkg:''}${pkg?'.':''}${GCU.getClassNameRepresentation(name)}"

    createArtifact(
        name: fqn,
        suffix: "Model",
        type: "Model",
        path: "griffon-app/models")

    createArtifact(
        name: fqn,
        suffix: "GameState",
        type: "GameState",
        path: "griffon-app/slick-states")

    createArtifact(
        name: fqn,
        suffix: "Controller",
        type: "Controller",
        path: "griffon-app/controllers")

    if (isAddonPlugin) {
        // create mvcGroup in a plugin
        def addonFile = isAddonPlugin
        def addonText = addonFile.text

        if (!(addonText =~ /\s*def\s*mvcGroups\s*=\s*\[/)) {
            addonText = addonText.replaceAll(/\}\s*\z/, """
    def mvcGroups = [
    ]
}
""")
        }
        addonFile.withWriter { it.write addonText.replaceAll(/\s*def\s*mvcGroups\s*=\s*\[/, """
    def mvcGroups = [
        // Game State for "$args"
        '$name' : [
            model : '${fqn}Model',
            state : '${fqn}GameState',
            controller : '${fqn}Controller'
        ]
    """) }


    } else {
        // create mvcGroup in an application
        def applicationConfigFile = new File("${basedir}/griffon-app/conf/Application.groovy")
        def configText = applicationConfigFile.text
        if (!(configText =~ /\s*mvcGroups\s*\{/)) {
            configText += """
mvcGroups {
}
"""
        }
        applicationConfigFile.withWriter { it.write configText.replaceAll(/\s*mvcGroups\s*\{/, """
mvcGroups {
    // Game State for "$name"
    '$name' {
        model = '${fqn}Model'
        state = '${fqn}GameState'
        controller = '${fqn}Controller'
    }
""") }
    }
}

setDefaultTarget(createGameState)