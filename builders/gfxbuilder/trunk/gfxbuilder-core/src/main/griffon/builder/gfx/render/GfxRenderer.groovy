/*
 * Copyright 2007-2009 the original author or authors.
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

package griffon.builder.gfx.render

import java.awt.*
import griffon.builder.gfx.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
final class GfxRenderer {
/*
   void render( VisualGfxNode node, GfxContext context ) {
      if(!node || !context) return
      def gcopy = context.g
      context.g = g.copy.create()
      if( !shouldSkip(node, context) ) {
         doBeforeChildren(node,context)
         doRender(node,context)
         doRenderChildren(node,context)
         doAfterChildren(node,context)
         cleanup(node,context)
      }
      context.g = gcopy
   }

   private void doBeforeChildren( GfxNode node, GfxContext context ) {
      if(node.beforeRender) {
         node.beforeRender(context,node)
      }
   }

   private void doRender( GfxNode node, GfxContext context ) {
      fill(node, context)
      draw(node, context)
   }

   private void doRenderChildren( GfxNode node, GfxContext context ) {
      node.nodes.each { n ->
         switch(n) {
            case VisualGfxNode:
               render(n, context)
               break
            case PropertyGfxNode:
               n.apply(context)
               break
         }
      }
   }

   private void doAfterChildren( GfxNode node, GfxContext context ) {
      if(node.afterRender) {
         node.afterRender(context,node)
      }
   }

   private void cleanup( GfxNode node, GfxContext context ) {
      context.g.dispose()
   }

   private void fill( GfxNode node, GfxContext context ){
      def f = node.runtime.fill
      def g = context.g

      switch(f){
         case Color:
            Color color = g.color
            g.color = f
            applyFill(node, context)
            g.color = color
            break
         case Paint:
            Paint paint = g.paint
            g.paint = f
            applyFill(node, context)
            g.paint = paint
            break
         case MultiPaintProvider:
             f.apply(context, shape)
             break
         default:
             // no fill
             break
      }
   }

   private void applyFill( GfxNode node, GfxContext context  ) {
      context.g.fill(getShape(node))
   }

   private void draw( GfxNode node, GfxContext context ) {
      def g = context.g
      def bc = node.runtime.borderColor
      def st = mode.runtime.stroke
      def bp = node.findLast{ it instanceof BorderPaintProvider }

      if( bp && bp.paint ){
          def ss = st.createStrokedShape(shape)
          if(bp.paint instanceof MultiPaintProvider){
             bp.apply(context, ss)
          }else{
             def p = g.paint
             g.paint = bp.getPaint(context, ss.bounds2D)
             g.fill(ss)
             g.paint = p
          }
      } else if( bc ){
          Color pc = g.color
          Stroke ps = g.stroke
          g.color = bc
          if( st ) g.stroke = st
          g.draw(getShape(node))
          g.color = pc
          if( st ) g.stroke = ps
      } else {
          // don't draw the shape
      }
   }

   private boolean shouldSkip( GfxNode node, GfxContext context ){
      Shape shape = getShape(node)
      // honor the clip
      return node.asShape || !withinClipBounds( context, shape )
   }

   private boolean withinClipBounds( GfxContext context, Shape shape ) {
      return context.g.clipBounds ? shape.intersects( context.g.clipBounds ) : false
   }

   private Shape getShape( GfxNode node ) {
      node.shape
   }
*/
}