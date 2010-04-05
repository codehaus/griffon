class ValidationGriffonPlugin {
    def version = 0.2
    def griffonVersion = "0.3 > *"
    def dependsOn = [:]
    // optional. Valid values are: swing, javafx, swt, pivot, gtk
    // def toolkits = ['swing']
    // optional. Valid values are linux, windows, macosx, solaris
    // def platforms = []

    def author = "Nick Zhu"
    def authorEmail = "nzhu@jointsource.com"
    def title = "Griffon Validation Plugin"
    def description = '''\\
A validation plugin that provides Grails like validation capability using constraint
declaration without depending on Spring
'''

    // URL to the plugin's documentation
    def documentation = "http://gvalidation.sourceforge.net/"
}
