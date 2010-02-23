//
// This script is executed by Griffon when the plugin is uninstalled from project.
// Use this script if you intend to do any additional clean-up on uninstall, but
// beware of messing up SVN directories!
//

// check to see if we already have a RiakGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'RiakGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing RiakGriffonAddon from Builder.groovy'
    def builderConfigFile1 = new File("${basedir}/griffon-app/conf/Builder.groovy")
    builderConfigFile1.text = builderConfigFile1.text - "root.'RiakGriffonAddon'.addon=true\n"
}
