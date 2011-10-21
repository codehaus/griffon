griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenRepo 'http://repository.jboss.org/nexus/content/groups/public-jboss'
    }
    dependencies {
        def metawidgetVersion = '1.30'
        compile("org.metawidget.modules:metawidget-core:$metawidgetVersion",
                "org.metawidget.modules:metawidget-annotation:$metawidgetVersion",
                "org.metawidget.modules:metawidget-beanvalidation:$metawidgetVersion",
                "org.metawidget.modules:metawidget-java5:$metawidgetVersion",
                "org.metawidget.modules:metawidget-groovy:$metawidgetVersion",
                "org.metawidget.modules.swing:metawidget-swing:$metawidgetVersion") {
            excludes 'jboss-javaee-6.0', 'hibernate-validator', 'servlet-api', 'commons-logging'            
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

//griffon.jars.jarName='MetawidgetGriffonAddon.jar'
