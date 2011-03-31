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
 * Gant script that creates a Clojure script
 * 
 * @author Jeff Brown
 *
 * @since 0.3
 */

includeTargets << griffonScript("Init")
includeTargets << griffonScript("CreateIntegrationTest")

target('createClojureScript': "Creates a new Clojure script") {
    depends(parseArguments)

    promptForName(type: 'Script')

    def name = argsMap["params"][0]
    def scriptName = "./griffon-app/resources/clj/${name}.clj"
    def scriptFile = new File(scriptName) 
    if(scriptFile.exists()) {
        if(!confirmInput("WARNING: ${scriptName} already exists. Are you sure you want to replace this script?")) {
            exit(0)
        }
    }
    println "Creating ${scriptName} ..."
    scriptFile.text = """\
(ns griffon)

(defn ${name}_fn []
    (println "${name}_fn function called."))
    
"""
}

setDefaultTarget('createClojureScript')
