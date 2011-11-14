griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'swtPluginLib', dirs: 'lib'
    }
    dependencies {
        compile('org.codehaus.groovy:groovy-swt:0.5.2',
                'com.miglayout:miglayout:3.7.3.1',
                'org.eclipse:org.apache.commons.logging:1.0.4.v200904062259',
                'org.eclipse:org.eclipse.core.commands:3.5.0.I20090525_2000',
                'org.eclipse:org.eclipse.core.databinding.beans:1.2.0.I20090525_2000',
                'org.eclipse:org.eclipse.core.databinding.observable:1.2.0.M20090902_0800',
                'org.eclipse:org.eclipse.core.databinding.property:1.2.0.M20090819_0800',
                'org.eclipse:org.eclipse.core.databinding:1.2.0.M20090819_0800',
                'org.eclipse:org.eclipse.core.runtime:3.5.0.v20090525',
                'org.eclipse:org.eclipse.jface.databinding:1.3.1.M20090826_0800',
                'org.eclipse:org.eclipse.jface:3.5.2.M20100120_0800',
                'org.eclipse:org.eclipse.equinox.common:3.5.1.R35x_v20090807_1100',
                'org.eclipse:org.eclipse.swt:3.5.2.v3557f',
                'org.eclipse:org.eclipse.ui.forms:3.4.1.v20090714_35x') {
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

