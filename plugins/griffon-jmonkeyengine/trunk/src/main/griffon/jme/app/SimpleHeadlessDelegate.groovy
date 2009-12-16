package griffon.jme.app

class SimpleHeadlessDelegate {
    private final SimpleHeadlessGriffonApplication app

    SimpleHeadlessDelegate(SimpleHeadlessGriffonApplication app) {
        this.app = app
    } 

    void simpleInitGame() {}
    void simpleUpdate() {}
    void simpleRender() {}

    def methodMissing(String methodName, args) {
        app.invokeMethod(methodName, args)
    }

    def propertyMissing(String propertyName) {
        app.getProperty(propertyName)
    }

    void propertyMissing(String propertyName, value) {
        app.setProperty(propertyName, value)
    }
}
