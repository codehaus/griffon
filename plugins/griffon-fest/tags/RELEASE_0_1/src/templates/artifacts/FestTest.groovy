@artifact.package@

import org.fest.swing.fixture.*
import org.testng.annotations.*
import griffon.application.SingleFrameApplication
import griffon.util.GriffonApplicationHelper

class @artifact.name@ {
   private SingleFrameApplication app
   private FrameFixture window

   static {
      SingleFrameApplication.metaClass.shutdown = { ->
         GriffonApplicationHelper.runScriptInsideEDT("Shutdown", delegate)
         delegate.appFrames[0].visible = false
      }
   }

   @BeforeMethod void init() {
      app = new SingleFrameApplication()
      app.bootstrap()
      app.realize()
      window = new FrameFixture( app.appFrames[0] )
      window.show()
   }

   @AfterMethod void cleanup() {
      window.cleanUp()
   }

   // --== tests ==--

   @Test void testSomething() {

   }
}