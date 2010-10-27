@artifact.package@import griffon.fest.*
import org.fest.swing.fixture.*

class @artifact.name@ extends FestSpec {
    // instance variables:
    // app    - current application
    // window - value returned from initWindow()
    //          defaults to app.windowManager.windows[0]

    def 'my first FEST spec'() {
        expect:
            window
    }

    // protected void onSetup() {}
    // protected void onCleanup() {}

    // protected FrameFixture initWindow() {
    //    return new FrameFixture(app.windowManager.windows[0])
    // }
}
