/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.fx

import groovy.swing.SwingBuilder
import griffon.builder.fx.factory.*
import griffon.builder.fx.impl.*

import com.sun.javafx.runtime.FXObject
import com.sun.javafx.runtime.location.ObjectLocation
import javafx.stage.*
import javafx.scene.*
import javafx.scene.shape.*
import javafx.scene.text.*
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.paint.*
import javafx.scene.image.*
import javafx.scene.transform.*
import javafx.scene.effect.*
import javafx.scene.effect.light.*
import javafx.ext.swing.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class FxBuilder extends SwingBuilder {
   static {
      Fx.enhance()
   }

   public FxBuilder( boolean init = true ) {
      super( init )
   }

   public void registerFxSwing() {
      registerBeanFactory("swingButton", SwingButton)
      registerBeanFactory("swingCheckBox", SwingCheckBox)
      registerBeanFactory("swingComboBox", SwingComboBox)
      registerBeanFactory("swingIcon", SwingIcon)
      registerFactory("swingList", new FxBeanFactory(SwingList,false))
      registerBeanFactory("swingListItem", SwingListItem)
      registerBeanFactory("swingLabel", SwingLabel)
      registerBeanFactory("swingRadioButton", SwingRadioButton)
      registerFactory("swingScrollPane", new FxBeanFactory(SwingScrollPane,false))
      registerBeanFactory("swingSlider", SwingSlider)
      registerBeanFactory("swingTextField", SwingTextField)
      registerBeanFactory("swingToggleButton", SwingToggleButton)
//       registerBeanFactory("swingButton", SwingToggleGroup)

      setVariable("SwingHorizontalAlignment_LEADING", SwingHorizontalAlignment.LEADING)
      setVariable("SwingHorizontalAlignment_TRAILING",SwingHorizontalAlignment.TRAILING)
      setVariable("SwingHorizontalAlignment_LEFT",    SwingHorizontalAlignment.LEFT)
      setVariable("SwingHorizontalAlignment_CENTER",  SwingHorizontalAlignment.CENTER)
      setVariable("SwingHorizontalAlignment_RIGHT",   SwingHorizontalAlignment.RIGHT)
      setVariable("SwingVerticalAlignment_TOP",   SwingVerticalAlignment.TOP)
      setVariable("SwingVerticalAlignment_CENTER",SwingVerticalAlignment.CENTER)
      setVariable("SwingVerticalAlignment_BOTTOM",SwingVerticalAlignment.BOTTOM)
   }

   public void registerFxSupport() {
      registerBeanFactory("cursor", Cursor)
      registerFactory("group", new FxBeanFactory(Group,false))
      registerFactory("scene", new FxBeanFactory(Scene,false))
      registerFactory("content", new ContentFactory())
      registerFactory("stage", new FxBeanFactory(Stage,false))

      setVariable("CURSOR_DEFAULT",  Cursor."\$DEFAULT")
      setVariable("CURSOR_CROSSHAIR",Cursor."\$CROSSHAIR")
      setVariable("CURSOR_TEXT",     Cursor."\$TEXT")
      setVariable("CURSOR_WAIT",     Cursor."\$WAIT")
      setVariable("CURSOR_SW_RESIZE",Cursor."\$SW_RESIZE")
      setVariable("CURSOR_SE_RESIZE",Cursor."\$SE_RESIZE")
      setVariable("CURSOR_NW_RESIZE",Cursor."\$NW_RESIZE")
      setVariable("CURSOR_NE_RESIZE",Cursor."\$NE_RESIZE")
      setVariable("CURSOR_N_RESIZE", Cursor."\$N_RESIZE")
      setVariable("CURSOR_S_RESIZE", Cursor."\$S_RESIZE")
      setVariable("CURSOR_W_RESIZE", Cursor."\$W_RESIZE")
      setVariable("CURSOR_E_RESIZE", Cursor."\$E_RESIZE")
      setVariable("CURSOR_HAND",     Cursor."\$HAND")
      setVariable("CURSOR_MOVE",     Cursor."\$MOVE")
      setVariable("CURSOR_H_RESIZE", Cursor."\$H_RESIZE")
      setVariable("CURSOR_V_RESIZE", Cursor."\$V_RESIZE")
      setVariable("CURSOR_NONE",     Cursor."\$NONE")

      addAttributeDelegate(FxBuilder.&fxAttributeDelegate)
   }

   public void registerFxLayout() {
      // TODO resolve clash with SwingBuilder[hbox,vbox]
      registerFactory("hbox", new FxLayoutFactory(HBox))
      registerFactory("vbox", new FxLayoutFactory(VBox))
   }

   public void registerFxImage() {
      registerBeanFactory("imageView", ImageView)
   }

   public void registerFxText() {
      registerBeanFactory("font", Font)
      registerBeanFactory("text", Text)

      setVariable("FontPosition_REGULAR",    FontPosition.REGULAR)
      setVariable("FontPosition_SUPERSCRIPT",FontPosition.SUPERSCRIPT)
      setVariable("FontPosition_SUBSCRIPT",  FontPosition.SUBSCRIPT)
      setVariable("FontPosture_REGULAR",FontPosture.REGULAR)
      setVariable("FontPosture_ITALIC", FontPosture.ITALIC)
      setVariable("FontWeight_EXTRA_LIGHT",FontWeight.EXTRA_LIGHT)
      setVariable("FontWeight_LIGHT",      FontWeight.LIGHT)
      setVariable("FontWeight_DEMI_LIGHT", FontWeight.DEMI_LIGHT)
      setVariable("FontWeight_REGULAR",    FontWeight.REGULAR)
      setVariable("FontWeight_SEMI_BOLD",  FontWeight.SEMI_BOLD)
      setVariable("FontWeight_MEDIUM",     FontWeight.MEDIUM)
      setVariable("FontWeight_DEMI_BOLD",  FontWeight.DEMI_BOLD)
      setVariable("FontWeight_BOLD",       FontWeight.BOLD)
      setVariable("FontWeight_HEAVY",      FontWeight.HEAVY)
      setVariable("FontWeight_EXTRA_BOLD", FontWeight.EXTRA_BOLD)
      setVariable("FontWeight_ULTRA_BOLD", FontWeight.ULTRA_BOLD)
      setVariable("TextAlignment_LEFT",    TextAlignment.LEFT)
      setVariable("TextAlignment_CENTER",  TextAlignment.CENTER)
      setVariable("TextAlignment_RIGHT",   TextAlignment.RIGHT)
      setVariable("TextAlignment_JUSTIFY", TextAlignment.JUSTIFY)
      setVariable("TextOrigin_BASELINE", TextOrigin.BASELINE)
      setVariable("TextOrigin_TOP",      TextOrigin.TOP)
      setVariable("TextOrigin_BOTTOM",   TextOrigin.BOTTOM)
   }

   public void registerFxTransform() {
      registerBeanFactory("affine", Affine)
      registerBeanFactory("rotate", Rotate)
      registerBeanFactory("scale", Scale)
      registerBeanFactory("shear", Shear)
      registerBeanFactory("translate", Translate)
   }

   public void registerFxPaint() {
      ["transparent", "aliceBlue", "antiqueWhite", "aqua", "aquamarine", "azure", "beige", "bisque",
      "black", "blancheDalmond", "blue", "blueViolet", "brown", "burlyWood", "cadetBlue",
      "chartreuse", "chocolate", "coral", "cornflowerBlue", "cornSilk", "crimson", "cyan",
      "darkBlue", "darkCyan", "darkGoldenRod", "darkGray", "darkGreen", "darkGrey", "darkKhaki",
      "darkMagenta", "darkOliveGreen", "darkOrange", "darkOrchid", "darkRed", "darkSalmon",
      "darkSeaGreen", "darkSlateBlue", "darkSlategray", "darkSlateGrey", "darkTurquoise",
      "darkViolet", "deepPink", "deepSkyBlue", "dimGray", "dimGrey", "dodgerBlue", "firebrick",
      "floralWhite", "forestGreen", "fuchsia", "gainsboro", "ghostWhite", "gold", "goldenRod",
      "gray", "green", "greenYellow", "grey", "honeyDew", "hotPink", "indianRed", "indigo",
      "ivory", "khaki", "lavender", "lavenderBlush", "lawnGreen", "lemonChiffon", "lightBlue",
      "lightCoral", "lightCyan", "lightGoldenRodYellow", "lightGray", "lightGreen", "lightGrey",
      "lightPink", "lightSalmon", "lightSeaGreen", "lightSkyBlue", "lightSlateGray", "lightSlateGrey",
      "lightSteelBlue", "lightYellow", "lime", "limeGreen", "linen", "magenta", "maroon",
      "mediumAquamarine", "mediumBlue", "mediumOrchid", "mediumPurple", "mediumSeaGreen",
      "mediumSlateBlue", "mediumSpringGreen", "mediumTurquoise", "mediumVioletRed", "midnightBlue",
      "mintCream", "mistyRose", "moccasin", "navajoWhite", "navy", "oldLace", "olive", "oliveDrab",
      "orange", "orangeRed", "orchid", "paleGoldenRod", "paleGreen", "paleTurquoise", "paleVioletRed",
      "papayaWhip", "peachPuff", "peru", "pink", "plum", "powderBlue", "purple", "red", "rosyBrown",
      "royalBlue", "saddleBrown", "salmon", "sandyBrown", "seaGreen", "seaShell", "sienna", "silver",
      "skyBlue", "slateBlue", "slateGray", "slateGrey", "snow", "springGreen", "steelBlue", "tan",
      "teal", "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whiteSmoke", "yellow", "yellowGreen"].each {
         setVariable(it, Color."\$${it.toUpperCase()}")
         setVariable("color_$it", Color."\$${it.toUpperCase()}")
      }

      registerExplicitMethod("color", Color.&color)
      registerExplicitMethod("rgb", Color.&rgb)
      registerExplicitMethod("hsb", Color.&hsb)
      registerExplicitMethod("web", Color.&web)
      registerExplicitMethod("fromAWTColor", Color.&fromAWTColor)

      setVariable("cycleMethod_NO_CYCLE", CycleMethod.NO_CYCLE)
      setVariable("cycleMethod_REFLECT", CycleMethod.REFLECT)
      setVariable("cycleMethod_REPEAT", CycleMethod.REPEAT)

      registerFactory("linearGradient", new FxPaintFactory(LinearGradient))
      registerFactory("radialGradient", new FxPaintFactory(RadialGradient))
//       registerBeanFactory("stop", Stop)
   }

   public void registerFxShape() {
      registerFactory("arc", new FxShapeFactory(Arc))
      registerFactory("circle", new FxShapeFactory(Circle))
      registerFactory("cubicCurve", new FxShapeFactory(CubicCurve))
      registerFactory("eclipse", new FxShapeFactory(Ellipse))
      registerFactory("line", new FxShapeFactory(Line))
      registerFactory("polygon", new FxShapeFactory(Polygon))
      registerFactory("polyline", new FxShapeFactory(Polyline))
      registerFactory("quadCurve", new FxShapeFactory(QuadCurve))
      registerFactory("rectangle", new FxShapeFactory(Rectangle))
      registerFactory("shape", new FxShapeFactory(Shape))
      registerFactory("intersect", new FxShapeFactory(ShapeIntersect))
      registerFactory("subtract", new FxShapeFactory(ShapeSubtract))
      registerFactory("svgPath", new FxShapeFactory(SVGPath))

      registerFactory("path", new FxShapeFactory(Path))
      registerFactory("arcTo", new FxPathElementFactory(ArcTo))
      registerFactory("cubicCurveTo", new FxPathElementFactory(CubicCurveTo))
      registerFactory("hlineTo", new FxPathElementFactory(HLineTo))
      registerFactory("lineTo", new FxPathElementFactory(LineTo))
      registerFactory("moveTo", new FxPathElementFactory(MoveTo))
      registerFactory("quadCurveTo", new FxPathElementFactory(QuadCurveTo))
      registerFactory("vlineTo", new FxPathElementFactory(VLineTo))

      setVariable("ArcType_OPEN",  ArcType.OPEN)
      setVariable("ArcType_CHORD", ArcType.CHORD)
      setVariable("ArcType_ROUND", ArcType.ROUND)
      setVariable("StrokeLineCap_SQUARE", StrokeLineCap.SQUARE)
      setVariable("StrokeLineCap_BUTT", StrokeLineCap.BUTT)
      setVariable("StrokeLineCap_ROUND", StrokeLineCap.ROUND)
      setVariable("StrokeLineJoin_MITER", StrokeLineJoin.MITER)
      setVariable("StrokeLineJoin_BEVEL", StrokeLineJoin.BEVEL)
      setVariable("StrokeLineJoin_ROUND", StrokeLineJoin.ROUND)
   }

   public void registerFxEffect() {
      registerFactory("blend", new FxEffectFactory(Blend))
      registerFactory("bloom", new FxEffectFactory(Bloom))
      registerFactory("colorAdjust", new FxEffectFactory(ColorAdjust))
      registerFactory("displacementMap", new FxEffectFactory(DisplacementMap))
      registerFactory("dropShadow",new FxEffectFactory( DropShadow))
      registerFactory("floatMap", new FxEffectFactory(FloatMap))
      registerFactory("flood", new FxEffectFactory(Flood))
      registerFactory("gaussianBlur", new FxEffectFactory(GaussianBlur))
      registerFactory("glow", new FxEffectFactory(Glow))
      registerFactory("identity", new FxEffectFactory(Identity))
      registerFactory("innerShadow", new FxEffectFactory(InnerShadow))
      registerFactory("invertMask", new FxEffectFactory(InvertMask))

      registerFactory("lighting", new FxEffectFactory(Lighting))
      registerBeanFactory("distantLight", DistantLight)
      registerBeanFactory("light", Light)
      registerBeanFactory("pointLight", PointLight)
      registerBeanFactory("spotLight", SpotLight)

      registerFactory("motionBlur", new FxEffectFactory(MotionBlur))
      registerFactory("perspectiveTransform", new FxEffectFactory(PerspectiveTransform))
      registerFactory("reflection", new FxEffectFactory(Reflection))
      registerFactory("sepiaTone", new FxEffectFactory(SepiaTone))
      registerFactory("shadow", new FxEffectFactory(Shadow))
   }

   public void registerBeanFactory( String theName, Class beanClass ) {
      if( FXObject.isAssignableFrom(beanClass) ) {
         registerFactory(theName, new FxBeanFactory(beanClass))
      } else {
         super.registerBeanFactory(theName,beanClass)
         registerFactory(theName, factories[theName])
      }
   }

   public void registerBeanFactory( String nodeName, String groupName, Class klass ) {
      if( !FXObject.isAssignableFrom(klass) ) {
         registerFactory(nodeName, groupName, new FxBeanFactory(klass))
      } else {
         super.registerBeanFactory(nodeName, groupName, klass)
         registerFactory(nodeName, groupName, factories[nodeName])
      }
   }

   public void registerFactory( String name, Factory factory ) {
      if( factory instanceof FxFactory ) super.registerFactory(name, factory)
      else super.registerFactory(name, new FxWrapperFactory(factory) )
   }

   public void registerFactory( String name, String groupName, Factory factory ) {
      if( factory instanceof FxFactory ) super.registerFactory(name, groupName, factory)
      else super.registerFactory(name, groupName, new FxWrapperFactory(factory) )
   }

   public static fxAttributeDelegate( builder, node, attributes ) {
      attributes.each { k, v ->
          if( !(node instanceof FXObject) ) return
          // let the wrapper handle property conversions
          if( builder.context.wrapped ) return
          translateValue( node, attributes, k, v )
      }
   }

   public static translateValue( node, attributes, name, value ) {
      try {
         if( ObjectLocation.isAssignableFrom(node.attributeType(name)) ) {
            attributes[name] = value instanceof Closure ? new ClosureFunction(value) : value
            attributes[name] = value instanceof List ? new ListSequence(value) : attributes[name]
         }
      } catch( MissingPropertyException mpe ) {
         // ignore
      }
   }
}