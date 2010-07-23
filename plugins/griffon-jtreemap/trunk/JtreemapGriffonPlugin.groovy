class JtreemapGriffonPlugin {
    def version = 0.2
    def griffonVersion = "0.9 > *" 
    def dependsOn = [:]
    def license = 'Apache Software License 2.0'
    // optional. Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = ['swing']
    // optional. Valid values are linux, windows, macosx, solaris
    // def platforms = []

    // TODO Fill in these fields
    def author = "Hackergarten"
    def authorEmail = "hackergarten@canoo.com"
    def title = "JTreeMap Integration"
    def description = '''Integrates the JTreeMap library.'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Jtreemap+Plugin"
}
