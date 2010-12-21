/*
 * Copyright 2006-2010 the original author or authors.
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
 * Gant script that installs Quartz config file into /griffon-app/conf/ directory.
 *
 * @author Sergey Nebolsin (Grails 0.3)
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target('default': "Installs Quartz config in the /griffon-app/conf/ directory") {
    installQuartzConfig()
}

target(installQuartzConfig: "The implementation task") {
    depends(checkVersion)
    def configFile = "${basedir}/griffon-app/conf/QuartzConfig.groovy"
    if(!(configFile as File).exists() || confirmInput("Quartz config file already exists in your project. Overwrite it?")) {
        ant.copy(
            file:"${quartzPluginDir}/src/templates/artifacts/DefaultQuartzConfig.groovy",
            tofile:configFile,
            overwrite: true
        )
        event("CreatedFile", [configFile])
        event("StatusFinal", ["Quartz configuration file was installed into /griffon-app/conf/QuartzConfig.groovy"])
    }
}

confirmInput = {String message ->
    ant.input(message: message, addproperty: "confirm.message", validargs: "y,n")
    ant.antProject.properties."confirm.message" == "y"
}
