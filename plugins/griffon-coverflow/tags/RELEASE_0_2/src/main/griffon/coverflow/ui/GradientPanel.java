/*
 * Copyright 2005-2010 the original author or authors.
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
package griffon.coverflow.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * <code>JPanel</code> subclass that draws a background gradient.
 *
 * @author Romain.Guy
 * @author Andres.Almiray
 */
public class GradientPanel extends JPanel {

	private static final String GRADIENT_START = "gradientStart";
	private static final String GRADIENT_END = "gradientEnd";
	private BufferedImage gradientImage;
	private Color gradientStart = new Color(110, 110, 110);
	private Color gradientEnd = new Color(0, 0, 0);

	public GradientPanel() {
		this(new BorderLayout());
	}

	public GradientPanel(LayoutManager layout) {
		super(layout);
		addComponentListener(new GradientCacheManager());
	}

	/**
	 * Returns the starting <code>Color</code> of the gradient.
	 *
	 * @return the starting <code>Color</code> of the gradient.
	 * @see #setGradientStart
	 */
	public Color getGradientStart() {
		return gradientStart;
	}

	/**
	 * Returns the ending <code>Color</code> of the gradient.
	 *
	 * @return the ending <code>Color</code> of the gradient.
	 * @see #setGradientEnd
	 */
	public Color getGradientEnd() {
		return gradientEnd;
	}

	/**
	 * Sets the starting <code>Color</code> of the gradient.
	 * <p>
	 * This is a JavaBeans bound property.
	 *
	 * @param gradientStart  the starting <code>Color</code> of the gradient
	 * @see #getGradientStart
	 * @beaninfo
	 *       bound: true
	 *   attribute: visualUpdate true
	 * description: The gradient's start color
	 */
	public void setGradientStart(Color gradientStart) {
		if (!this.gradientStart.equals(gradientStart)) {
			Color oldValue = this.gradientStart;
			this.gradientStart = gradientStart;
			disposeImageCache();
			firePropertyChange(GRADIENT_START, oldValue, gradientStart);
		}
	}

	/**
	 * Sets the ending <code>Color</code> of the gradient.
	 * <p>
	 * This is a JavaBeans bound property.
	 *
	 * @param gradientEnd  the ending <code>Color</code> of the gradient
	 * @see #getGradientEnd
	 * @beaninfo
	 *       bound: true
	 *   attribute: visualUpdate true
	 * description: The gradient's end color
	 */
	public void setGradientEnd(Color gradientEnd) {
		if (!this.gradientEnd.equals(gradientEnd)) {
			Color oldValue = this.gradientEnd;
			this.gradientEnd = gradientEnd;
			disposeImageCache();
			firePropertyChange(GRADIENT_END, oldValue, gradientEnd);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		createImageCache();

		if (gradientImage != null) {
			Shape clip = g.getClip();
			Rectangle bounds = clip.getBounds();

			Image backgroundImage = gradientImage.getSubimage(bounds.x,
					bounds.y,
					bounds.width,
					bounds.height);
			g.drawImage(backgroundImage, bounds.x, bounds.y, null);
		}
	}

	private void createImageCache() {
		int width = getWidth();
		int height = getHeight();

		if (width == 0 || height == 0) {
			return;
		}

		if (gradientImage == null ||
				width != gradientImage.getWidth() ||
				height != gradientImage.getHeight()) {

			gradientImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g2 = gradientImage.createGraphics();
			GradientPaint painter = new GradientPaint(0, 0, gradientEnd,
					0, height / 2, gradientStart);
			g2.setPaint(painter);
			Rectangle2D rect = new Rectangle2D.Double(0, 0, width, height / 2.0);
			g2.fill(rect);

			painter = new GradientPaint(0, height / 2, gradientStart,
					0, height, gradientEnd);
			g2.setPaint(painter);
			rect = new Rectangle2D.Double(0, (height / 2.0) - 1.0, width, height);
			g2.fill(rect);

			g2.dispose();
		}
	}

	private synchronized void disposeImageCache() {
		if (gradientImage != null) {
			gradientImage.flush();
			gradientImage = null;
		}
	}

	private class GradientCacheManager implements ComponentListener {

		public void componentResized(ComponentEvent e) {
		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentShown(ComponentEvent e) {
		}

		public void componentHidden(ComponentEvent e) {
			disposeImageCache();
		}
	}
}
