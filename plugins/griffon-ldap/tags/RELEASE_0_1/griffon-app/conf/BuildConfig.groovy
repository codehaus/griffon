griffon.project.dependency.resolution = {
    inherits("global")
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
        flatDir name: 'ldapPluginLib', dirs: 'lib'
    }
    dependencies {
        compile('org.springframework.ldap:org.springframework.ldap:1.3.0.RELEASE',
                'commons-lang:commons-lang:2.5',
                'org.codehaus:gldapo:0.8.2',
                'com.ldaley:injecto:0.7',
                'com.sun:ldapbp:1.0') {
            transitive = false
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

griffon.jars.destDir='target/addon'
