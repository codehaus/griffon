/*
 * Copyright 2004-2005 the original author or authors.
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
 * Gant script that creates a new Griffon integration test
 * 
 * @author Graeme Rocher
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target (_createIntegrationTest: "Creates a new Griffon integration test which loads the whole Griffon environment when run") {
	depends(checkVersion, parseArguments)

    promptForName(type: "Integration test")

    def name = argsMap["params"][0]
    createIntegrationTest(name: name, suffix: "")
}

setDefaultTarget(_createIntegrationTest)