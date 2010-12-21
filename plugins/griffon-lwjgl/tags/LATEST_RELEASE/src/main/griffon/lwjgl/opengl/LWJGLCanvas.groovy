/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    boolean initSharedContext = true
    boolean autoSwapBuffers = true

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
        if(initSharedContext) createSharedContext()
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
        if(autoSwapBuffers) swapBuffers()
    }

    protected void update(Graphics g) {
        super.update(g)
        if(onUpdate) {
            onUpdate.delegate = this
            onUpdate(g)
        }
    }
}
