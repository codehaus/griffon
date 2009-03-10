/*
 * Copyright 2008-2009 the original author or authors.
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
 * Gant script that creates a new WizardPage
 */

import org.codehaus.griffon.commons.GriffonClassUtils as GCU

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target (createWizardPage: "Creates a new WizardPage") {
   depends(checkVersion, parseArguments)
   promptForName(type: "Wizard page")
   def (pkg, name) = extractArtifactName(argsMap["params"][0])
   def fqn = "${pkg?pkg:''}${pkg?'.':''}${GCU.getClassNameRepresentation(name)}"

   createArtifact(
      name: fqn,
      suffix: "WizardPage",
      type: "WizardPage",
      path: "griffon-app/wizards")
}

setDefaultTarget(createWizardPage)