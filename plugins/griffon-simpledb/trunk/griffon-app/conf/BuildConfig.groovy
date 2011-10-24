griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()    
        mavenRepo 'http://repository.sonatype.org/content/groups/public'    
        flatDir name: 'simpledbPluginLib', dirs: 'lib'
    }
    dependencies {
        compile('com.amazonaws:aws-java-sdk:1.2.10',
                'commons-codec:commons-codec:1.3',
                'org.apache.httpcomponents:httpclient:4.1.2') {
            excludes 'commons-logging'        
        }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
