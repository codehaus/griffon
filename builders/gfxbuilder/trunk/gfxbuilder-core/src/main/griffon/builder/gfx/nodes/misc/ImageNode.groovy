/*
 * Copyright 2007-2008 the original author or authors.
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
 */

package griffon.builder.gfx.nodes.misc

import java.awt.Image
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.*
import griffon.builder.gfx.runtime.*


/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ImageNode extends AbstractDrawableNode {
   private Image _image
   private Shape _shape
   private Shape _localShape

   @GfxAttribute(alias="i") Image image
   @GfxAttribute(alias="f") def file
   @GfxAttribute(alias="u") def url
   @GfxAttribute(alias="cl") def classpath
   @GfxAttribute double x = 0d
   @GfxAttribute double y = 0d
   @GfxAttribute(alias="w") double width = Double.NaN
   @GfxAttribute(alias="h") double height = Double.NaN
   @GfxAttribute(alias="tx") double translateX = Double.NaN
   @GfxAttribute(alias="ty") double translateY = Double.NaN
   @GfxAttribute(alias="ra") double rotateAngle = Double.NaN
   @GfxAttribute(alias="sx") double scaleX = Double.NaN
   @GfxAttribute(alias="sy") double scaleY = Double.NaN

   ImageNode() {
      super("image")
   }

   ImageNode(Image image) {
      super("image")
      this.image = image
   }

   ImageNode(File file) {
      super("image")
      this.file = file
   }

   ImageNode(URL url) {
      super("image")
      this.url = url
   }

   ImageNode(String classpath) {
      super("image")
      this.classpath = classpath
   }

   void onDirty(PropertyChangeEvent event) {
      _image = null
      super.onDirty(event)
   }

   Image getImage() {
      if(!_image) {
         _image = getRuntime().getImage()
      }
      _image
   }

   Shape getShape() {
      if(!_shape) {
         _shape = getRuntime().getShape()
      }
      _shape
   }

   Shape getLocalShape() {
      if( !_localShape ) {
         _localShape = getShape()
         if(_localShape) {
            AffineTransform affineTransform = getLocalTransforms(_localShape)
            _localShape = affineTransform.createTransformedShape(_localShape)
         }
      }
      _localShape
   }

   private AffineTransform getLocalTransforms(Shape shape) {
      double _x = shape.bounds.x
      double _y = shape.bounds.x
      double _cx = _x + (shape.bounds.width/2)
      double _cy = _y + (shape.bounds.height/2)
      AffineTransform affineTransform = new AffineTransform()
      if(!Double.isNaN(sx) && !Double.isNaN(sy)) {
         affineTransform.concatenate AffineTransform.getTranslateInstance(_x-_cx, _y-_cy)
         affineTransform.concatenate AffineTransform.getScaleInstance(sx, sy)
      }
      if(!Double.isNaN(tx) && !Double.isNaN(ty)) {
         affineTransform.concatenate AffineTransform.getTranslateInstance(tx, ty)
      }
      if(!Double.isNaN(ra)) {
         affineTransform.concatenate AffineTransform.getRotateInstance(Math.toRadians(ra),_cx, _cy)
      }
      affineTransform
   }

   protected void beforeApply(GfxContext context) {
      getImage()
      super.beforeApply(context)
      AffineTransform transform = new AffineTransform()
      transform.concatenate context.g.transform
      transform.concatenate getLocalTransforms(getShape())
      context.g.transform = transform
   }

   protected void applyNode(GfxContext context) {
      getImage()
      if(shouldSkip(context)) return
      if(Double.isNaN(width) && Double.isNaN(height)) {
         context.g.drawImage(_image, x, y, context.component)
      } else {
         context.g.drawImage(_image, x, y,, width, height, context.component)
      }
   }

   GfxRuntime createRuntime(GfxContext context) {
      _runtime = new ImageGfxRuntime(this, context)
      _runtime
   }
}