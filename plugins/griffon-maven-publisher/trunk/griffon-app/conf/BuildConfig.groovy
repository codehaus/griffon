griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenLocal()
        mavenCentral()
    }
    dependencies {
        build "org.apache.maven:maven-ant-tasks:2.1.0",
              "org.codehaus.groovy.modules.http-builder:http-builder:0.5.0", {
            excludes "commons-logging", "xml-apis", "groovy"
        }
        compile "org.tmatesoft.svnkit:svnkit:1.3.3", {
            excludes "jna", "trilead-ssh2", "sqljet"
        }
        test  "org.gmock:gmock:0.8.0", {
            export = false
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
