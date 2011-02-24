griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'jtreemapPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'net.sf.jtreemap:jtreemap:1.1.0'
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
griffon.plugin.pack.additional.sources = ['src/gdsl']

//griffon.jars.jarName='JtreemapGriffonAddon.jar'
