/*
 * Copyright 2004-2008 the original author or authors.
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
 * Gant script that creates a Griffon Gant script
 * 
 * @author Graeme Rocher
 *
 * @since 0.4
 */

Ant.property(environment:"env")                             
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"    

includeTargets << new File ( "${griffonHome}/scripts/Init.groovy" )  

target ('default': "Creates a Griffon Gant Script") {
    depends(checkVersion)

    typeName = "" 
    artifactName = "Script"     
    artifactPath = "scripts"
        
    createArtifact()
}
