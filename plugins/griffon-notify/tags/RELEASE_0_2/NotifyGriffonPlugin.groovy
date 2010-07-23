class NotifyGriffonPlugin {
    def version = 0.2
    def griffonVersion = "0.9 > *" 
    def license = 'Apache Software License 2.0'
    def dependsOn = [:]
    // optional. Valid values are: swing, javafx, swt, pivot, gtk
    // def toolkits = ['swing']
    // optional. Valid values are linux, windows, macosx, solaris
    // def platforms = []

    // TODO Fill in these fields
    def author = "Hackergarten"
    def authorEmail = "hackergarten@gmail.com"
    def title = "Griffon Desktop Notification Plugin"
    def description = '''This plugin allows easy access to several desktop notification agents. If the user is running notify-send, Growl, or Snarl, then these services are used to display a notification message. If none of these are installed, then a dialog message is shown for several seconds to the user. '''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Notify+Plugin"
}
