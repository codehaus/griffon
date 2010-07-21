/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.jgraph.shape

import com.mxgraph.shape.mxBasicShape
import java.awt.Shape
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import com.mxgraph.canvas.mxGraphics2DCanvas
import com.mxgraph.view.mxCellState

/**
 * @author Andres.Almiray
 */
class ShapeAdapter extends mxBasicShape {
    final Shape shapeTemplate

    ShapeAdapter(Shape shapeTemplate) {
        this.shapeTemplate = shapeTemplate
    }

    public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state) {
        Rectangle bounds = state.rectangle
        Rectangle sbounds = shapeTemplate.bounds
        double s = Math.min(bounds.width/sbounds.width, bounds.height/sbounds.height)
        double tx = sbounds.minX + sbounds.width/2
        double ty = sbounds.minY + sbounds.height/2

        AffineTransform at = AffineTransform.getScaleInstance(s, s)
        at.translate(-tx,-ty)
        Shape shape = at.createTransformedShape(shapeTemplate)
        sbounds = shape.bounds

        double dx = (sbounds.x + sbounds.width/2) + (bounds.x + bounds.width/2)
        double dy = (sbounds.y + sbounds.height/2) + (bounds.y + bounds.height/2)
        at = AffineTransform.getTranslateInstance(dx, dy)
        return at.createTransformedShape(shape)
    }
}
