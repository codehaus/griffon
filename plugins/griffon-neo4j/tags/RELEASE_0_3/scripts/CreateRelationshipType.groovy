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

includeTargets << griffonScript('_GriffonInit')
includeTargets << griffonScript('_GriffonCreateArtifacts')

target(createRelationshipType: 'Creates a Neo4j RelationshipType enum') {
    depends(checkVersion, parseArguments)

    def type = 'RelationshipType'
    promptForName(type: type)
    def (pkg, name) = extractArtifactName(argsMap['params'][0])

    createArtifact(
        name: name,
        suffix: 'RelationshipType',
        type: type,
        fileType: '.java',
        lineTerminator: ';',
        path: 'src/main')
}
setDefaultTarget(createRelationshipType)
