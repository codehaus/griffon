@artifact.package@

import org.fest.swing.fixture.*
import griffon.test.FestSwingTestCase

class @artifact.name@ extends FestSwingTestCase {
   // instance variables:
   // app    - current application
   // window - value returned from initWindow()
   //          defaults to app.appFrames[0]

   void testSomething() {

   }

   protected void onSetUp() throws Exception { }
   protected void onTearDown() throws Exception { }

   /*
   protected FrameFixture initWindow() {
      return new FrameFixture(app.appFrames[0])
   }
   */
}
