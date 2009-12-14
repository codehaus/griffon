import groovy.swing.factory.ComponentFactory
import griffon.lwjgl.opengl.LWJGLCanvas

class LwjglGriffonAddon {
    def factories = [
        lwjglCanvas: new ComponentFactory(LWJGLCanvas)
    ]
}
