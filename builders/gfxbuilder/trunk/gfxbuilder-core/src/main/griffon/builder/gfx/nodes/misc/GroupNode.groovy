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

import griffon.builder.gfx.*

import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.AffineTransform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GroupNode extends VisualGfxNode  {
    private Map previousGroupSettings = [:]

    GroupNode() {
        super("group")
    }

    protected void applyBeforeAll(GfxContext context) {
       super.applyBeforeAll(context)
       previousGroupSettings = [:]
       previousGroupSettings.putAll(context.groupSettings)
       if( borderColor != null ) context.groupSettings.borderColor = borderColor
       if( borderWidth != null ) context.groupSettings.borderWidth = borderWidth
       if( opacity != null ) context.groupSettings.opacity = opacity
       if( fill != null ) context.groupSettings.fill = fill
    }

    protected void applyAfterAll(GfxContext context) {
       context.groupSettings = previousGroupSettings
       super.applyAfterAll(context)
    }

    protected boolean shouldSkip(GfxContext context){
       return !visible
    }

    protected void applyNode(GfxContext context) {

    }

    Shape calculateShape() { null }

    protected void applyNestedNode(GfxNode node, GfxContext context) {
       node.apply( context )
    }
}