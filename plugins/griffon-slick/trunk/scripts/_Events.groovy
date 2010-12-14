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

/**
 * @author Andres Almiray
 */

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('slick')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-slick-plugin', dirs: "${slickPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('slick', [
        conf: 'compile',
        name: 'griffon-slick-addon',
        group: 'org.codehaus.griffon.plugins',
        version: slickPluginVersion
    ])
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'state' }) {
        artifactsInfo << [type: 'state', path: 'slick-states', suffix: 'GameState']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'slick-states'} ) {
        pathToInfo << [name: 'Slick Game States', path: 'slick-states', filetype: ['.groovy', '.java']]
    }
}

eventCreateConfigEnd = {
    if(compilingPlugin('slick')) return
    if(buildConfig.griffon.application.mainClass) return

    boolean hasStates = false
    new File("${basedir}/griffon-app/slick-states").eachFileRecurse { file ->
        if(hasStates) return
        if(file.name.endsWith('GameState.groovy') || file.name.endsWith('GameState.java')) {
            hasStates = true
        }
    }

    buildConfig.griffon.application.mainClass = hasStates ? 'griffon.plugins.slick.StateBasedSlickGriffonApplication' : 'griffon.plugins.slick.BasicSlickGriffonApplication'
}
