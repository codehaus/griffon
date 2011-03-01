//
// This script is executed by Griffon when the plugin is uninstalled from project.
// Use this script if you intend to do any additional clean-up on uninstall, but
// beware of messing up SVN directories!
//

// check to see if we already have a AirbagGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'AirbagGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing AirbagGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'AirbagGriffonAddon'.addon=true\n"
}
