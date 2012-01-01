/*
 * Copyright 2004-2010 the original author or authors.
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

import griffon.util.*
import org.codehaus.griffon.plugins.*
import org.apache.ivy.util.ChecksumHelper

includeTargets << griffonScript("Package")    
    
// Open source licences.
globalLicenses = [
        APACHE: [ name: "Apache License 2.0", url: "http://www.apache.org/licenses/LICENSE-2.0.txt" ],
        GPL2: [ name: "GNU General Public License 2", url: "http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt"],
        GPL3: [ name: "GNU General Public License 3", url: "http://www.gnu.org/licenses/gpl.txt"] ]

artifact = groovy.xml.NamespaceBuilder.newInstance(ant, 'antlib:org.apache.maven.artifact.ant')

target(init: "Initialisation for maven deploy/install") {
    depends(packageApp)

    ant.mkdir(dir: griffonSettings.projectTargetDir)
    // plugin = pluginManager?.allPlugins?.find { it.basePlugin }

    if(!isPluginProject) {
        package_zip()
    } else {
        includeTargets << griffonScript("_GriffonPluginDev")    
        packagePlugin()
        // plugin = pluginManager?.allPlugins?.find { it.basePlugin }
    }

    generatePom()
}

target(generatePom: "Generates a pom.xml file for the current project unless './pom.xml' exists.") {
    depends(init)

    pomFileLocation = "${griffonSettings.projectTargetDir}/pom.xml"
    basePom = new File("${basedir}/pom.xml")

    if (basePom.exists()) {
        pomFileLocation = basePom.absolutePath
        println "Skipping POM generation because 'pom.xml' exists in the root of the project."
        return 1
    }

    // Get hold of the plugin instance for this plugin if it's a plugin
    // project. If it isn't, then these variables will be null.

    new File(pomFileLocation).withWriter { w ->
        def xml = new groovy.xml.MarkupBuilder(w)

        xml.project(xmlns: "http://maven.apache.org/POM/4.0.0", 
                'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance", 
                'xsi:schemaLocation': "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd") {
            modelVersion "4.0.0"
            if(plugin) {
                def group = "org.griffon.plugins"
                if (getOptionalProperty(plugin, 'group')) {
                    group = plugin.group
                }
                else if(getOptionalProperty(plugin, 'groupId')) {
                    group = plugin.groupId
                }

                groupId group
                artifactId GriffonUtil.getLogicalName(plugin.class.name, 'GriffonPlugin') 
                packaging "zip"
                version plugin.version

                // I think description() and url() resolve against the AntBuilder
                // by default, so we have to call them explicitly on the MarkupBuilder.
                if (getOptionalProperty(plugin, "title")) name plugin.title
                if (getOptionalProperty(plugin, "description")) delegate.description plugin.description
                if (getOptionalProperty(plugin, "documentation")) delegate.url plugin.documentation
                if (getOptionalProperty(plugin, "license")) {
                    def l = globalLicenses[plugin.license]
                    if (l) {
                        licenses {
                            license {
                                name l.name
                                delegate.url l.url
                            }
                        }
                    } else if(plugin.license){
                        licenses {
                            license {
                                name plugin.license
                            }
                        }
                    } else {
                        event("StatusUpdate", [ "Unknown license: ${plugin.license}" ])
                    }
                }
                if (getOptionalProperty(plugin, "organization")) {
                    organization {
                        name plugin.organization.name
                        delegate.url plugin.organization.url
                    }
                }

                // Handle the developers
                def devs = []
                if (getOptionalProperty(plugin, "author")) {
                    def author = [ name: plugin.author ]
                    if (getOptionalProperty(plugin, "authorEmail")) {
                        author["email"] = plugin.authorEmail
                    }

                    devs << author
                }
                if (getOptionalProperty(plugin, "developers")) {
                    devs += plugin.developers
                }

                if (devs) {
                    developers {
                        for (d in devs) {
                            developer {
                                name d.name
                                if (d.email) email d.email
                            }
                        }
                    }
                }

                // Handle the issue tracker
                if (getOptionalProperty(plugin, "issueManagement")) {
                    def trackerInfo = plugin.issueManagement
                    issueManagement {
                        if (trackerInfo.system) system trackerInfo.system
                        if (trackerInfo.url) delegate.url trackerInfo.url
                    }
                }

                // Source control
                if (getOptionalProperty(plugin, "scm")) {
                    def scmInfo = plugin.scm
                    scm {
                        if (scmInfo.connection) connection scmInfo.connection
                        if (scmInfo.developerConnection) developerConnection scmInfo.developerConnection
                        if (scmInfo.tag) tag scmInfo.tag
                        if (scmInfo.url) delegate.url scmInfo.url
                    }
                }
            }
            else {
                groupId buildConfig.griffon.project.groupId ?: (config?.griffon?.project?.groupId ?: griffonAppName)
                artifactId griffonAppName
                packaging "zip"
                version griffonAppVersion
                name griffonAppName                
            }
                
            if(plugin && plugin.dependsOn) {
                dependencies {                    
                    for(dep in plugin.dependsOn) {
                        String depName = dep.key
                        def depGroup = "org.griffon.plugins"
                        if(depName.contains(":")) {
                            def i = depName.split(":")
                            depGroup = i[0]
                            depName = i[1]
                        }
                        String depVersion = dep.value
                        def upper = GriffonPluginUtils.getUpperVersion(depVersion)
                        def lower = GriffonPluginUtils.getLowerVersion(depVersion)
                        if(upper == lower) depVersion = upper
                        else {
                            upper = upper == '*' ? '' : upper
                            lower = lower == '*' ? '' : lower
                            
                            depVersion = "[$lower,$upper]"
                        }
                        
                        dependency {
                            groupId depGroup
                            artifactId GriffonUtil.getScriptName(depName)
                            version depVersion
                        }                            
                    }
                }
            }
        }
    }
}

target(mavenInstall:"Installs a plugin or application into your local Maven cache") {
    depends(init)
    def deployFile = new File(plugin ? pluginZip : "${distDir}/zip/${griffonAppName}-${griffonAppVersion}.zip")
    installOrDeploy(deployFile, plugin ? true : false, false)
}

private generateChecksum(File file) {
    def checksum = new File("${file.parentFile.absolutePath}/${file.name}.sha1")
    checksum.write ChecksumHelper.computeAsString(file, "sha1")
    return checksum
}
private installOrDeploy(File file, isPlugin, boolean deploy, repos = [:]) {
    if (!deploy) {
        ant.checksum file:pomFileLocation, algorithm:"sha1", todir:projectTargetDir
        ant.checksum file:file, algorithm:"sha1", todir:projectTargetDir
    }

    def pomCheck = generateChecksum(new File(pomFileLocation))
    def fileCheck = generateChecksum(file)

    artifact."${ deploy ? 'deploy' : 'install' }"(file: file) {
        if(isPlugin) {
            attach file:"${basedir}/plugin.xml",type:"xml", classifier:"plugin"
        }            

        if (!deploy) {
            attach file:"${projectTargetDir}/pom.xml.sha1",type:"pom.sha1"
            attach file:"${projectTargetDir}/${file.name}.sha1",type:"zip.sha1"
        }

        pom(file: pomFileLocation)
        if(repos.remote) {
            def repo = repos.remote
            if(repo.configurer) {
                remoteRepository(repo.args, repo.configurer)
            }
            else {
                remoteRepository(repo.args)
            }
        }
        if(repos.local) {
            localRepository(path:repos.local)
        }
    }    
}


private getOptionalProperty(obj, prop) {
    return obj.hasProperty(prop) ? obj."$prop" : null
}

target(mavenDeploy:"Deploys the plugin to a Maven repository") {
    depends(init)
    def protocols = [     http: "wagon-http",
                        scp:    "wagon-ssh",
                        scpexe:    "wagon-ssh-external",
                        ftp: "wagon-ftp",
                        webdav: "wagon-webdav" ]
    
    def distInfo = new DistributionManagementInfo()
    if(griffonSettings.config.griffon.project.dependency.distribution instanceof Closure) {
        def callable = griffonSettings.config.griffon.project.dependency.distribution
        callable.delegate = distInfo
        callable.resolveStrategy = Closure.DELEGATE_FIRST
        try {
            callable.call()
        }
        catch(e) {
            println "Error reading dependency distribution settings: ${e.message}"
            exit 1
        }
    }
    def protocol = protocols.http
    def repo = argsMap.repository ? distInfo.remoteRepos[argsMap.repository] : null
    if(argsMap.protocol) {
        protocol = protocols[argsMap.protocol]
    }
    else if(repo) {
        def url = repo?.args?.url
        if(url) {
            def i = url.indexOf('://')
            def urlProt = url[0..i-1]
            protocol = protocols[urlProt] ?: protocol
        }
    }         
    
    artifact.'install-provider'(artifactId:protocol, version:"1.0-beta-2")
    
    def deployFile = new File(plugin ? pluginZip : "${distDir}/zip/${griffonAppName}-${griffonAppVersion}.zip")
    try {
        installOrDeploy(deployFile, plugin ? true : false, true, [remote:repo, local:distInfo.local])
    }
    catch(e) {
        println "Error deploying artifact: ${e.message}"
        println "Have you specified a configured repository to deploy to (--repository argument) or specified distributionManagement in your POM?"
    }
}

class DistributionManagementInfo {
    Map remoteRepos = [:]
    String local
    void localRepository(String s) { local = s }
    void remoteRepository(Map args, Closure c = null) {
        if(!args?.id) throw new Exception("Remote repository misconfigured: Please specify a repository 'id'. Eg. remoteRepository(id:'myRepo')")
        if(!args?.url) throw new Exception("Remote repository misconfigured: Please specify a repository 'url'. Eg. remoteRepository(url:'http://..')")        
        def e = new Expando()
        e.args = args
        e.configurer = c
        remoteRepos[args.id] = e
    }
}
