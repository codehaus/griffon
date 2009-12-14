package griffon.lwjgl.opengl

import java.awt.Graphics
import java.awt.GraphicsDevice
import org.lwjgl.opengl.AWTGLCanvas
import org.lwjgl.opengl.PixelFormat
import org.lwjgl.opengl.Drawable
import org.lwjgl.opengl.ContextAttribs

/**
 * @author Andres Almiray
 */
class LWJGLCanvas extends AWTGLCanvas {
    Closure onInitGL
    Closure onPaintGL
    Closure onUpdate

    LWJGLCanvas() {
        super()
    }

    LWJGLCanvas(GraphicsDevice device, PixelFormat pixel_format) {
        super(device, pixel_format)
    }

    LWJGLCanvas(GraphicsDevice device, PixelFormat pixel_format, Drawable drawable) {
        super(device, pixel_format, drawable)
    }

    LWJGLCanvas(GraphicsDevice device, PixelFormat pixel_format, Drawable drawable, ContextAttribs attribs) {
        super(device, pixel_format, drawable, attribs)
    }

    LWJGLCanvas(PixelFormat pixel_format) {
        super(pixel_format)
    }

    protected void initGL() {
        super.initGL()
        if(onInitGL) {
            onInitGL.delegate = this
            onInitGL()
        }
    }

    protected void paintGL() {
        super.paintGL()
        if(onPaintGL) {
            onPaintGL.delegate = this
            onPaintGL()
        }
    }

    protected void update(Graphics g) {
        super.update(g)
        if(onUpdate) {
            onUpdate.delegate = this
            onUpdate(g)
        }
    }
}
