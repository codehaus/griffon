package org.codehaus.griffon.macmenus

import javax.swing.Icon

class MacAboutDialogModel {

    Icon icon

    String appName
    String version
    String copyright
    String copyrightYear
    String license

    def mvcGroupInit(Map args) {
        icon = icon ?: args.builder.imageIcon('/griffon-icon-64x64.png')

        appName = appName ?: args.app.applicationProperties['app.name']
        version = version ?: "Version ${args.app.applicationProperties['app.version']}"
        copyrightYear = copyrightYear ?: "2008-${new Date().year + 1900}"
        copyright = copyright ?: "Copyright \u00a9 $copyrightYear the original author or authors"
        license = license ?: "All Rights Reserved"
    }

}