import groovy.swing.factory.ComponentFactory
import javax.media.opengl.GLCanvas
import javax.media.opengl.GLJPanel

class JoglGriffonAddon {
    def factories = [
        glcanvas: new ComponentFactory(GLCanvas),
        glpanel: new ComponentFactory(GLJPanel)
    ]
}
