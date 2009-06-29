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

package griffon.builder.gfx

// import java.awt.AlphaComposite
// import java.awt.image.AffineTransformOp
// 
// import groovy.swing.factory.BindFactory
// import groovy.swing.factory.BindProxyFactory
import groovy.swing.factory.CollectionFactory
import griffon.builder.gfx.factory.*
import griffon.builder.gfx.nodes.misc.*
import griffon.builder.gfx.nodes.outlines.*
import griffon.builder.gfx.nodes.paints.*
import griffon.builder.gfx.nodes.shapes.*
import griffon.builder.gfx.nodes.shapes.path.*
import griffon.builder.gfx.nodes.strokes.*
import griffon.builder.gfx.nodes.transforms.*
import org.kordamp.jsilhouette.geom.*

import java.lang.reflect.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GfxBuilder extends FactoryBuilderSupport {
    private Map shortcuts = [:]

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    static {
        GfxUtils.enhanceShapes()
        GfxUtils.enhanceColor()
        GfxUtils.enhanceBasicStroke()
    }

    public GfxBuilder( boolean init = true ) {
        super( false )
        if(init) {
           gfxbAutoRegiser()
        }

        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }

    public gfxbAutoRegiser() {
        // if java did atomic blocks, this would be one
        synchronized (this) {
            if (autoRegistrationRunning || autoRegistrationComplete) {
                // registration already done or in process, abort
                return;
            }
        }
        autoRegistrationRunning = true;
        try {
            gfxbCallAutoRegisterMethods(getClass());
        } finally {
            autoRegistrationComplete = true;
            autoRegistrationRunning = false;
        }
    }


    private void gfxbCallAutoRegisterMethods(Class declaredClass) {
        if (declaredClass == null) {
            return;
        }
        gfxbCallAutoRegisterMethods(declaredClass.getSuperclass());

        for (Method method in declaredClass.getDeclaredMethods()) {
            if (method.getName().startsWith("register") && method.getParameterTypes().length == 0) {
                registringGroupName = method.getName().substring("register".length());
                registrationGroup.put(registringGroupName, new TreeSet<String>());
                try {
                    if (Modifier.isPublic(method.getModifiers())) {
                        method.invoke(this);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cound not init " + getClass().getName() + " because of an access error in " + declaredClass.getName() + "." + method.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cound not init " + getClass().getName() + " because of an exception in " + declaredClass.getName() + "." + method.getName(), e);
                } finally {
                    registringGroupName = "";
                }
            }
        }
    }

    private registerGfxBeanFactory( String name, Class beanClass ){
        registerFactory( name, new GfxBeanFactory(beanClass,false) )
    }

    private registerGfxBeanFactory( String name, Class beanClass, boolean leaf ){
        registerFactory( name, new GfxBeanFactory(beanClass,leaf) )
    }

    void registerGfxSupportNodes() {
        addAttributeDelegate(GfxBuilder.&objectIDAttributeDelegate)
//         addAttributeDelegate(GfxBuilder.&interpolationAttributeDelegate)
//         addAttributeDelegate(GfxBuilder.&alphaCompositeAttributeDelegate)

//         registerFactory( "draw", new DrawFactory() )
//         registerFactory( "font", new FontFactory() )
        registerGfxBeanFactory("group", GroupNode)
        registerGfxBeanFactory("renderingHint", RenderingHintNode, true)
        registerFactory("noparent", new CollectionFactory())
//         registerFactory( "shape", new ShapeFactory() )

        // binding related classes
//         BindFactory bindFactory = new BindFactory()
//         registerFactory("bind", bindFactory)
//         addAttributeDelegate(bindFactory.&bindingAttributeDelegate)
//         registerFactory("bindProxy", new BindProxyFactory())

//         registerFactory( "image", new ImageFactory() )
        registerFactory("color", new ColorFactory())
        registerFactory("rgba", factories.color)
//         registerFactory( "clip", new ClipFactory() )
        registerFactory( "antialias", new AntialiasFactory() )
//         registerFactory( "alphaComposite", new AlphaCompositeFactory() )
//         registerFactory( "viewBox", new ViewBoxFactory() )
//         registerFactory( "props", new PropsFactory() )
        registerFactory( "background", new BackgroundFactory() )

        // variables
        variables['on'] = true
        variables['yes'] = true
        variables['off'] = false
        variables['no'] = false
    }

    void registerGfxShapes() {
        registerGfxBeanFactory("arc", ArcNode)
        registerGfxBeanFactory("circle", CircleNode)
        registerGfxBeanFactory("ellipse", EllipseNode)
        registerGfxBeanFactory("polygon", PolygonNode)
        registerGfxBeanFactory("rect", RectangleNode)
//         registerGfxBeanFactory("text", TextNode)
        registerGfxBeanFactory("almond", AlmondNode)
        registerGfxBeanFactory("arrow", ArrowNode)
        registerGfxBeanFactory("asterisk", AsteriskNode)
        registerGfxBeanFactory("astroid", AstroidNode)
        registerGfxBeanFactory("balloon", BalloonNode)
        registerGfxBeanFactory("cross", CrossNode)
        registerGfxBeanFactory("donut", DonutNode)
        registerGfxBeanFactory("fan", FanNode)
        registerGfxBeanFactory("lauburu", LauburuNode)
        registerGfxBeanFactory("multiRoundRect", MultiRoundRectangleNode)
        registerGfxBeanFactory("rays", RaysNode)
        registerGfxBeanFactory("regularPolygon", RegularPolygonNode)
        registerGfxBeanFactory("reuleauxTriangle", ReuleauxTriangle)
        registerGfxBeanFactory("pin", RoundPinNode)
        registerGfxBeanFactory("star", StarNode)
        registerGfxBeanFactory("triangle", TriangleNode)

        //
        // paths
        //
        registerGfxBeanFactory("path", PathNode)
        registerFactory("moveTo", new PathSegmentFactory(MoveToPathSegment))
        registerFactory("lineTo", new PathSegmentFactory(LineToPathSegment))
        registerFactory("quadTo", new PathSegmentFactory(QuadToPathSegment))
        registerFactory("curveTo", new PathSegmentFactory(CurveToPathSegment))
        registerFactory("hline", new PathSegmentFactory(HLinePathSegment))
        registerFactory("vline", new PathSegmentFactory(VLinePathSegment))
        registerFactory("shapeTo", new ShapePathSegmentFactory())
        registerFactory("close", new PathSegmentFactory(ClosePathSegment))

        //
        // outlines
        //
        registerGfxBeanFactory("line", LineNode)
        registerGfxBeanFactory("cubicCurve", CubicCurveNode)
        registerGfxBeanFactory("polyline", PolylineNode)
        registerGfxBeanFactory("quadCurve", QuadCurveNode)

        //
        // area operations
        //
        registerFactory("add", new AreaFactory("add","add"))
        registerFactory("subtract", new AreaFactory("subtract","subtract"))
        registerFactory("intersect", new AreaFactory("intersect","intersect"))
        registerFactory("xor", new AreaFactory("xor","exclusiveOr"))
    }

    void registerGfxTransforms() {
        registerFactory("transforms", new TransformsFactory())
        registerFactory("rotate", new TransformFactory(RotateTransform))
        registerFactory("scale", new TransformFactory(ScaleTransform))
        registerFactory("shear", new TransformFactory(ShearTransform))
        registerFactory("translate", new TransformFactory(TranslateTransform))
        registerFactory("matrix", new TransformFactory(MatrixTransform))
        registerFactory("transform", new TransformTransformFactory())
    }
        //
        // paints
        //
//         registerFactory( "borderPaint", new BorderPaintFactory() )
//         registerGfxBeanFactory( "gradientPaint", GradientPaintGfx, true )
//         registerGfxBeanFactory( "multiPaint", MultiPaintGfx )
//         registerFactory( "paint", new PaintFactory() )
//         registerGfxBeanFactory( "texturePaint", TexturePaintGfx, true )
//         registerFactory( "colorPaint", new ColorPaintFactory() )

        //
        // strokes
        //
    void registerGfxStrokes() {
        registerFactory("stroke", new StrokeFactory())
        registerFactory("basicStroke", new StrokesFactory(BasicStrokeNode))
        registerFactory("compositeStroke", new StrokesFactory(CompositeStrokeNode,false))
        registerFactory("compoundStroke", new StrokesFactory(CompoundStrokeNode,false))
        registerFactory("textStroke", new StrokesFactory(TextStrokeNode))
        registerFactory("shapeStroke", new ShapeStrokeFactory())
        registerFactory("wobbleStroke", new StrokesFactory(WobbleStrokeNode))
        registerFactory("zigzagStroke", new StrokesFactory(ZigzagStrokeNode,false))
        registerFactory("bristleStroke", new StrokesFactory(BristleStrokeNode))
        registerFactory("brushStroke", new StrokesFactory(BrushStrokeNode))
        registerFactory("calligraphyStroke", new StrokesFactory(CalligraphyStrokeNode))
        registerFactory("clarcoalStroke", new StrokesFactory(CharcoalStrokeNode,false))
    }
        //
        // filters
        //
//         registerFactory( "filters", new FilterGroupFactory() )


//         variables['angleNone'] = Triangle.NONE
//         variables['angleAtStart'] = Triangle.ANGLE_AT_START
//         variables['angleAtEnd'] = Triangle.ANGLE_AT_END

//         variables['alphaClear'] = AlphaComposite.CLEAR
//         variables['alphaDst'] = AlphaComposite.DST
//         variables['alphaDstAtop'] = AlphaComposite.DST_ATOP
//         variables['alphaDstIn'] = AlphaComposite.DST_IN
//         variables['alphaDstOut'] = AlphaComposite.DST_OUT
//         variables['alphaDstOver'] = AlphaComposite.DST_OVER
//         variables['alphaSrc'] = AlphaComposite.SRC
//         variables['alphaSrcAtop'] = AlphaComposite.SRC_ATOP
//         variables['alphaSrcIn'] = AlphaComposite.SRC_IN
//         variables['alphaSrcOut'] = AlphaComposite.SRC_OUT
//         variables['alphaSrcOver'] = AlphaComposite.SRC_OVER
//         variables['alphaXor'] = AlphaComposite.XOR
//     }

    public static objectIDAttributeDelegate(def builder, def node, def attributes) {
       def idAttr = builder.getAt(DELEGATE_PROPERTY_OBJECT_ID) ?: DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
       def theID = attributes.remove(idAttr)
       if (theID) {
           builder.setVariable(theID, node)
       }
    }

//     public static interpolationAttributeDelegate( def builder, def node, def attributes ) {
//        def interpolation = attributes.remove("interpolation")
//        switch( interpolation ){
//           case "bicubic":  interpolation = AffineTransformOp.TYPE_BICUBIC; break;
//           case "bilinear": interpolation = AffineTransformOp.TYPE_BILINEAR; break;
//           case "nearest":  interpolation = AffineTransformOp.TYPE_NEAREST_NEIGHBOR; break;
//        }
//        if( interpolation != null ) node.interpolation = interpolation
//     }

//     public static alphaCompositeAttributeDelegate( def builder, def node, def attributes ) {
//        def alphaComposite = attributes.remove("alphaComposite")
//        if( alphaComposite ){
//           if( alphaComposite instanceof AlphaComposite ){
//              node.alphaComposite = alphaComposite
//           }else if( alphaComposite instanceof Map ){
//              def rule = getAlphaCompositeRule(alphaComposite.op)
//              def alpha = alphaComposite.alpha
//              if( alpha != null ){
//                 node.alphaComposite = AlphaComposite.getInstance(rule,alpha as float)
//              }else{
//                 node.alphaComposite = AlphaComposite.getInstance(rule)
//              }
//           }
//        }
//     }

//     private def getAlphaCompositeRule( value ){
//        if( value == null ) {
//           return AlphaComposite.SRC_OVER
//        }else if( value instanceof Number ){
//           return rule as int
//        }else if( value instanceof String ){
//           return AlphaComposite.@"${value.toUpperCase()}"
//        }
//        return AlphaComposite.SRC_OVER
//     }
}