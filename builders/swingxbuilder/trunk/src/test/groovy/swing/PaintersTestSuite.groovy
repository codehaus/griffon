/*
 * Copyright 2007 the original author or authors.
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
package groovy.swing

import groovy.swing.SwingXBuilder
import org.jdesktop.swingx.*
import org.jdesktop.swingx.painter.*
import org.jdesktop.swingx.painter.effects.*
import java.awt.*
import java.awt.geom.*
import javax.imageio.ImageIO

/**
 * @author James Williams
 */
public class PaintersTest extends GroovyTestCase{
	
	void testPinstripePainter() {
		def builder = new SwingXBuilder()		
		def ps = builder.pinstripePainter(angle:135, paint:Color.GREEN, spacing:10.4d, stripeWidth:3.6d)
		assertTrue((ps instanceof PinstripePainter)&& (ps != null) )
	}

	void testMattePainter() {
		def builder = new SwingXBuilder()
		def mp = builder.mattePainter(fillPaint:Color.GREEN)
		assertTrue((mp instanceof MattePainter)&& (mp != null) )
	}

	void testGlossPainter() {
		def builder = new SwingXBuilder()
		def gp = builder.glossPainter(position: GlossPainter.GlossPosition.TOP)
		assertTrue((gp instanceof GlossPainter)&& (gp != null) )
	}

	void testCheckerboardPainter() {
		def builder = new SwingXBuilder()
		def cbp = builder.checkerboardPainter(squareSize:40.0d, lightPaint:Color.WHITE, darkPaint:Color.RED)
		assertTrue((cbp instanceof CheckerboardPainter) && (cbp != null))
	}

	void testCapsulePainter() {
		def builder = new SwingXBuilder()
		def cp = builder.capsulePainter(portion:CapsulePainter.Portion.Bottom)
		assertTrue((cp instanceof CapsulePainter) && (cp != null))
	}

	void testShapePainter() {
		def builder = new SwingXBuilder()
		def sp = builder.shapePainter(shape:new Rectangle2D.Double(0, 0, 50, 50),horizontalAlignment:AbstractLayoutPainter.HorizontalAlignment.RIGHT)
		assertTrue((sp instanceof ShapePainter) && (sp != null))
	}

	void testTextPainter() {
		def builder = new SwingXBuilder()
		def tp = builder.textPainter(font:new Font("Tahoma", Font.BOLD, 32), text:"Test Text")
		assertTrue((tp.getFont() != null) && (tp.getText() != null))
		assertTrue((tp instanceof TextPainter) && (tp != null))
	}

	void testRectanglePainter() {
		def builder = new SwingXBuilder()
		def rp = builder.rectanglePainter(width:10, height:100, rounded:true, roundWidth:144, roundHeight:33, fillPaint:Color.GREEN)
		assertTrue(rp instanceof RectanglePainter && rp != null)
	}

	void testRectanglePainterOmittingRounded() {
		def builder = new SwingXBuilder()
		def rp = builder.rectanglePainter(width:10, height:100, roundWidth:144, roundHeight:33, fillPaint:Color.GREEN)
		assertTrue(rp instanceof RectanglePainter && rp != null && rp.isRounded() == true)
	}

	void testImagePainter() {
		def builder = new SwingXBuilder()
		def ip = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif")))
		assertTrue(ip instanceof ImagePainter && ip.getImage() != null)
	}

	void testBusyPainter() {
		def builder = new SwingXBuilder()
                // swingx 0.9.2 barlength has been removed from BusyLabel
                // without deprecation mark, why?
		def bp = builder.busyPainter(/*barLength:1.0f,*/ baseColor: Color.BLUE)
		assertTrue(bp instanceof BusyPainter && bp != null)
	}

	// void testURLPainter() {
	//	def builder = new SwingXBuilder()
	//	def bp = builder.urlPainter(url:new java.net.URL("http://www.yahoo.com"))
	//	assertTrue(bp instanceof URLPainter && bp != null)
	// }
	
	void testCompoundPainter() {
		def builder = new SwingXBuilder()
		def compound = builder.compoundPainter() {
			mattePainter(fillPaint:Color.GREEN)
			glossPainter(position: GlossPainter.GlossPosition.TOP)
		}
		def painters = compound.getPainters()
		assertTrue(painters != null && painters[0] instanceof MattePainter && painters[1] instanceof GlossPainter)
	}

	void testAlphaPainter() {
		def builder = new SwingXBuilder()
		def ap = builder.alphaPainter(alpha:0.25f) {
			mattePainter(fillPaint:Color.GREEN)
			glossPainter(position: GlossPainter.GlossPosition.TOP)
		}
		def painters = ap.getPainters()
		assertTrue(painters != null && painters[0] instanceof MattePainter && painters[1] instanceof GlossPainter)
	}
	
	void testGlowPathEffect() {
		def builder = new SwingXBuilder()
		def ge = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif"))) {
			glowPathEffect()
		}
		assertTrue( ge.getAreaEffects() != null && ge.getAreaEffects()[0] instanceof GlowPathEffect )
	}
	
	void testInnerGlowPathEffect() {
		def builder = new SwingXBuilder()
		def ge = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif"))) {
			innerGlowPathEffect()
		}
		assertTrue( ge.getAreaEffects() != null && ge.getAreaEffects()[0] instanceof InnerGlowPathEffect )
	}
	
	void testInnerShadowPathEffect() {
		def builder = new SwingXBuilder()
		def ge = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif"))) {
			innerShadowPathEffect()
		}
		assertTrue( ge.getAreaEffects() != null && ge.getAreaEffects()[0] instanceof InnerShadowPathEffect )
	}
	
	void testNeonBorderEffect() {
		def builder = new SwingXBuilder()
		def ge = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif"))) {
			neonBorderEffect()
		}
		assertTrue( ge.getAreaEffects() != null && ge.getAreaEffects()[0] instanceof NeonBorderEffect )
	}
	
	void testShadowPathEffect() {
		def builder = new SwingXBuilder()
		def ge = builder.imagePainter(image:ImageIO.read(new URL("http://www.google.com/intl/en_ALL/images/logo.gif"))) {
			shadowPathEffect()
		}
		assertTrue( ge.getAreaEffects() != null && ge.getAreaEffects()[0] instanceof ShadowPathEffect )
	}
}
