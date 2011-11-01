/*
 * Copyright 2009-2011 the original author or authors.
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
 
package griffon.plugins.slideware;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Utilities for rendering images and graphics.
 *
 * @author Andres Almiray
 */
public final class GraphicsUtil {
    public static BufferedImage createCompatibleImage(int width, int height) {
        return createCompatibleImage(width, height, false);
    }

    public static BufferedImage createCompatibleImage(int width, int height, boolean withAlpha) {
        if (GraphicsEnvironment.isHeadless()) {
            return new BufferedImage(width,
                    height,
                    (withAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB));
        } else {
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            return gc.createCompatibleImage(width,
                    height,
                    (withAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE));
        }
    }

    public static Rectangle scaleBounds(int w, int h, Rectangle bounds) {
        return scaleBounds(w, h, bounds, 0.9f);
    }

    public static Rectangle scaleBounds(int w, int h, Rectangle bounds, float fitAmount) {
        fitAmount = fitAmount >= 0.0f && fitAmount <= 1.0f ? fitAmount : 0.9f;
        
        int width = w;
        int height = h;

        if (height > bounds.height) {
            height = (int) (bounds.height * fitAmount);
            width = (w * height) / h;
        }
        
        if(width > bounds.width) {
           width = (int) (bounds.height * fitAmount);
           height = (h * width) / w; 
        }

        int x = bounds.x + ((bounds.width - width) / 2);
        int y = bounds.y + ((bounds.height - height) / 2);

        return new Rectangle(x, y, width, height);
    }

    public static void scaleAndDrawImage(Graphics g, float scale, String imageResource) {
        try {
            scaleAndDrawImage(g, scale, ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(imageResource)));
        } catch (IOException e) {
            // ignore ??
        }
    }

    public static void scaleAndDrawImage(Graphics g, float scale, Image image) {
        float width = image.getWidth(null) * scale;
        float height = image.getHeight(null) * scale;
        g.drawImage(image, 0, 0, (int) width, (int) height, null);
    }
}
