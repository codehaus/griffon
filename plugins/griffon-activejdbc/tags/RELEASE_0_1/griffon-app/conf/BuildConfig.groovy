griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits "global"
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://ipsolutionsdev.com/snapshots/'
    }
    
    def activejdbcVersion = '1.1-SNAPSHOT'
    
    dependencies {
        compile("activejdbc:activejdbc:$activejdbcVersion") {
            excludes 'junit', 'servlet-api', 'slf4j-api', 'jcl-over-slf4j', 'slf4j-simple'
        }
        compile("javalite:javalite-common:1.0") {
            excludes 'jaxen'
        }
        compile("activejdbc:activejdbc-instrumentation:$activejdbcVersion") {
            transitive = false
        }
        compile('commons-dbcp:commons-dbcp:1.2.2',
                'commons-pool:commons-pool:1.5.3',
                'com.h2database:h2:1.3.158') {
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
