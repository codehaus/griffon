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

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

/**
 * The original code is from Romain Guy's example "A Music Shelf in Java2D".
 * It can be found here:
 *
 *   http://www.curious-creature.org/2005/07/09/a-music-shelf-in-java2d/
 *
 * Updated Code
 * This code has been updated by Kevin Long (codebeach.com) to make it more
 * generic and more component like.
 *
 * History:
 *
 * 2/17/2008
 * ---------
 * - Removed CD case drawing
 * 
 * @author Romain.Guy
 * @author Kevin.Long
 */
public class CrystalCaseFactory {

	private static CrystalCaseFactory instance = null;
	private static int IMAGE_WIDTH = 262;
	private static int IMAGE_HEIGHT = 233;
	private BufferedImage mask;

	public static CrystalCaseFactory getInstance() {
		if (instance == null) {
			instance = new CrystalCaseFactory();
		}
		return instance;
	}

	private CrystalCaseFactory() {
		mask = createGradientMask(IMAGE_WIDTH, IMAGE_HEIGHT);
	}

	public BufferedImage createCrystalCase(Image cover) {
		BufferedImage crystal = new BufferedImage(IMAGE_WIDTH,
				IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = crystal.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		int width = cover.getWidth(null);
		int height = cover.getHeight(null);

		float scale;

		if (width > height) {
			scale = (float) IMAGE_WIDTH / (float) width;
		} else {
			scale = (float) IMAGE_HEIGHT / (float) height;
		}

		int scaledWidth = (int) ((float) width * scale);
		int scaledHeight = (int) ((float) height * scale);

		int x = (IMAGE_WIDTH - scaledWidth) / 2;
		int y = (IMAGE_HEIGHT - scaledHeight) / 2;

		g2.drawImage(cover, x, y, scaledWidth, scaledHeight, null);

		g2.dispose();

		return crystal;
	}

	public BufferedImage createReflectedPicture(BufferedImage item) {
		return createReflectedPicture(item, mask);
	}

	public BufferedImage createReflectedPicture(BufferedImage item,
			BufferedImage alphaMask) {
		int itemWidth = item.getWidth();
		int itemHeight = item.getHeight();

		BufferedImage buffer = createReflection(item,
				itemWidth, itemHeight);

		applyAlphaMask(buffer, alphaMask, itemWidth, itemHeight);

		return buffer;
	}

	private void applyAlphaMask(BufferedImage buffer,
			BufferedImage alphaMask,
			int itemWidth, int itemHeight) {

		Graphics2D g2 = buffer.createGraphics();
		g2.setComposite(AlphaComposite.DstOut);
		g2.drawImage(alphaMask, null, 0, itemHeight);
		g2.dispose();
	}

	private BufferedImage createReflection(BufferedImage item,
			int itemWidth,
			int itemHeight) {

		BufferedImage buffer = new BufferedImage(itemWidth, itemHeight << 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();

		g.drawImage(item, null, null);
		g.translate(0, itemHeight << 1);

		AffineTransform reflectTransform = AffineTransform.getScaleInstance(1.0, -1.0);
		g.drawImage(item, reflectTransform, null);
		g.translate(0, -(itemHeight << 1));

		g.dispose();

		return buffer;
	}

	public BufferedImage createGradientMask(int itemWidth, int itemHeight) {
		BufferedImage gradient = new BufferedImage(itemWidth, itemHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = gradient.createGraphics();
		GradientPaint painter = new GradientPaint(0.0f, 0.0f,
				new Color(1.0f, 1.0f, 1.0f, 0.5f),
				0.0f, itemHeight / 2.0f,
				new Color(1.0f, 1.0f, 1.0f, 1.0f));
		g.setPaint(painter);
		g.fill(new Rectangle2D.Double(0, 0, itemWidth, itemHeight));

		g.dispose();

		return gradient;
	}
}
