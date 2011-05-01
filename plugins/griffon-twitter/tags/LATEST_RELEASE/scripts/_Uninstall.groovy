//
// This script is executed by Griffon when the plugin is uninstalled from project.
// Use this script if you intend to do any additional clean-up on uninstall, but
// beware of messing up SVN directories!
//

// check to see if we already have a TwitterGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'TwitterGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing TwitterGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'TwitterGriffonAddon'.addon=true\n"
}
