class InfinispanGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.2 > *' 
    // the other plugins this plugin depends on
    def dependsOn = []
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'GNU LGPL Version 3'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Thomas P. Fuller'
    def authorEmail = 'thomas.fuller@coherentlogic.com'
    def title = 'Griffon plugin for the JBoss Infinispan distributed cache.'
    def description = '''
This is a plugin for the JBoss Infinispan distributed cache (
http://www.jboss.org/infinispan).

Refer to the Griffon Infinispan plugin web page for more information (
http://griffon.org/plugin/infinispan).
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Infinispan+Plugin'
}
