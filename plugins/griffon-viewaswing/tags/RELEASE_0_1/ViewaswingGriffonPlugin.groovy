class ViewaswingGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.3 > *' 
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    // TODO Fill in these fields
    def author = 'Mario Garcia'
    def authorEmail = ''
    def title = 'Viewa swing components'
    def description = '''
This plugin adds new builders to use viewa components inside your Griffon applications.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Viewaswing+Plugin'
}
