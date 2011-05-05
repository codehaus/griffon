class AirbagGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.2 > *'
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Nick Zhu'
    def authorEmail = 'nzhu@jointsource.com'
    def title = 'Griffon Airbag Plugin'
    def description = '''
This plugin provides a simple user-friendly dialog to display any uncaught exception
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Airbag+Plugin'
}
