griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.jboss.org/nexus/content/groups/public'
    }
    dependencies {
/*
        compile('javax.enterprise:cdi-api:1.0-SP4') {
            excludes 'testng', 'jboss-ejb-api_3.1_spec'
        }
        compile('org.jboss.weld:weld-core:1.1.0.Final') {
            excludes('servlet-api', 'jboss-ejb-api_3.1_spec', 'jsp-api', 'el-api', 'jsf-api', 'slf4j-ext',
                     'cal10n-api', 'slf4j-api', 'annotations', 'junit', 'slf4j-simple')
        }
        compile('org.jboss.weld:weld-api:1.1.Final') {
            excludes 'servlet-api'
        }
        compile('org.jboss.weld:weld-spi:1.1.Final') {
            excludes 'testng', 'servlet-api', 'jsf-api', 'jboss-ejb-api_3.1_spec'
        }
*/
        compile('org.jboss.weld.se:weld-se-core:1.1.0.Final') {
            excludes 'junit', 'slf4j-simple', 'slf4j-ext'
        }
        compile 'org.slf4j:slf4j-ext:1.6.1'
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
