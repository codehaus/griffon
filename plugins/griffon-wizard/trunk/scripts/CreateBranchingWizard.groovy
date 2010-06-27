/*
 * Copyright 2009-2010 the original author or authors.
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
 * Gant script that creates a new Branching Wizard
 *
 * @author Andres Almiray
 */

import org.codehaus.griffon.commons.GriffonClassUtils as GCU

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target(createBranchingWizard: "Creates a new BranchingWizard") {
   depends(checkVersion, parseArguments)
   promptForName(type: "Branching Wizarde")
   def (pkg, name) = extractArtifactName(argsMap["params"][0])
   def fqn = "${pkg?pkg:''}${pkg?'.':''}${GCU.getClassNameRepresentation(name)}"

   createArtifact(
      name: fqn,
      suffix: "BranchingWizard",
      type: "BranchingWizard",
      path: "griffon-app/wizards")
}
setDefaultTarget(createBranchingWizard)
