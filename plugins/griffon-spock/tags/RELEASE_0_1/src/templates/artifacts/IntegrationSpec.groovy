@artifact.package@import griffon.spock.*
import griffon.core.GriffonApplication

class @artifact.name@ extends IntegrationSpec {
    GriffonApplication app

    def 'my first integration spec'() {
        expect:
            app
    }
}
