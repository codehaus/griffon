/*
 * Copyright 2009-2011 the original author or authors.
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

ant.mkdir(dir: "${basedir}/griffon-app/wizards")

configText = '''root.'WizardGriffonAddon'.addon=true'''
if(!(builderConfigFile.text.contains(configText))) {
    println 'Adding WizardGriffonAddon to Builder.groovy'
    builderConfigFile.text += '\n' + configText + '\n'
}

configText = '''root.'WizardGriffonAddon'.controller=['wizard', 'branchingWizard', 'showWizard']'''
if(!(builderConfigFile.text.contains(configText))) {
    builderConfigFile.text += '\n' + configText + '\n'
}
