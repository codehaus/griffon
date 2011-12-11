griffon.project.dependency.resolution = {
    inherits("global")
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'jzy3dPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'org.jzy3d:jzy3d:0.8',
                'org.convexhull:convexhull:1.0',
                'net.sf.opencsv:opencsv:2.1'
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
