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
 * Gant script that manages the application version
 *
 * @author Marc Palmer
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 * @author Graeme Rocher
 *
 * @since 0.5
 */

includeTargets << griffonScript("_GriffonEvents")

target (setVersion: "Sets the current application version") {
    if(args != null) {
        ant.property(name:"app.version.new", value: args)
    } else {
        def oldVersion = metadata.'app.version'
        ant.input(addProperty:"app.version.new", message:"Enter the new version",defaultvalue:oldVersion)
    }

    def newVersion = ant.antProject.properties.'app.version.new'
    metadata.'app.version' = newVersion
    metadataFile.withOutputStream {
        metadata.store it, 'utf-8'
    }
    event("StatusFinal", [ "Application version updated to $newVersion"])
}

setDefaultTarget(setVersion)