/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.builder.css

import java.awt.Color
import java.awt.Insets
import javax.swing.border.Border
import java.util.WeakHashMap

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class BorderUtils {
   private static final Map BORDER_CACHE = new WeakHashMap()

   enum BorderType {
      MARGIN, PADDING, NORMAL
   }

   static getBorderType( Border border ) {
      def map = BORDER_CACHE.get(border,[:])
      map.get("type", BorderType.NORMAL)
   }

   static setBorderType( Border border, BorderType type ) {
      def map = BORDER_CACHE.get(border,[:])
      map.put("type", type ?: BorderType.NORMAL)
   }

   static getBorderColor( Border border ) {
      def map = BORDER_CACHE.get(border,[:])
      map.get("color", Color.BLACK)
   }

   static setBorderColor( Border border, Color color ) {
      def map = BORDER_CACHE.get(border,[:])
      map.put("color", color ?: Color.BLACK)
   }

   static getBorderInsets( Border border ) {
      def map = BORDER_CACHE.get(border,[:])
      map.get("insets")
   }

   static setBorderInsets( Border border, Insets insets ) {
      def map = BORDER_CACHE.get(border,[:])
      map.put("insets", insets)
   }

   static setBorderType( Border border, String type ) {
      setBorderType(border, Enum.valueOf(BorderType, type))
   }

   static isMargin( Border border ) {
      getBorderType(border) == BorderType.MARGIN
   }

   static isPadding( Border border ) {
      getBorderType(border) == BorderType.PADDING
   }

   static setAsMargin( Border border ) {
      setBorderType(border, BorderType.MARGIN)
   }

   static setAsPadding( Border border ) {
      setBorderType(border, BorderType.PADDING)
   }

   static clear( Border border ) {
      BORDER_CACHE.remove(border)
   }
}