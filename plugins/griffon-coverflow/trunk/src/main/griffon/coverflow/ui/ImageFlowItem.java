/*
 * Copyright 2008-2010 the original author or authors.
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

import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines an item that can be displayed in an <code>ImageFlow</code>.<p>
 * Provides lazy-loading capabilities of images.
 * 
 * @author Kevin.Long
 * @author Andres.Almiray
 */
public class ImageFlowItem {

	private final LazyImageLoader imageLoader;
	private final String label;

	/**
	 * Creates a new item using a classpath <code>Resource</code> as
	 * image source, and "" as label.
	 * 
	 * @param resource a classpath <code>Resource</code> pointing to
	 *                 an image that can be loaded with <code>ImageIO</code>.
	 */
	public ImageFlowItem(Resource resource) {
		this(resource, "");
	}

	/**
	 * Creates a new item using a classpath <code>Resource</code> as
	 * image source, and the provided label.
	 *
	 * @param resource a classpath <code>Resource</code> pointing to
	 *                 an image that can be loaded with <code>ImageIO</code>.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(Resource resource, String label) {
		this(new ResourceLazyImageLoader(resource), label);
	}

	/**
	 * Creates a new item using an <code>Image</code> as
	 * image source, and "" as label.
	 *
	 * @param image an <code>Image</code> object.
	 */
	public ImageFlowItem(Image image) {
		this(image, "");
	}

	/**
	 * Creates a new item using an <code>Image</code> as
	 * image source, and the provided label.
	 *
	 * @param image an <code>Image</code> object.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(Image image, String label) {
		this(new ImageLazyImageLoader(image), label);
	}

	/**
	 * Creates a new item using a <code>String</code> pointing to a
	 * filename as image source, and "" as label.
	 *
	 * @param fileName a fileName pointing to
	 *                 an image that can be loaded with <code>ImageIO</code>.
	 */
	public ImageFlowItem(String fileName) {
		this(new File(fileName), "");
	}

	/**
	 * Creates a new item using a <code>String</code> pointing to a
	 * filename as image source, and the provided label.
	 *
	 * @param fileName a fileName pointing to
	 *                 an image that can be loaded with <code>ImageIO</code>.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(String fileName, String label) {
		this(new File(fileName), label);
	}

	/**
	 * Creates a new item using a <code>File</code> as
	 * image source, and "" as label.
	 *
	 * @param file a file pointing to an image that can be loaded
	 *             with <code>ImageIO</code>.
	 */
	public ImageFlowItem(File file) {
		this(file, "");
	}

	/**
	 * Creates a new item using a <code>File</code> as
	 * image source, and the provided label.
	 *
	 * @param file a file pointing to an image that can be loaded
	 *             with <code>ImageIO</code>.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(File file, String label) {
		this(new FileLazyImageLoader(file), label);
	}

	/**
	 * Creates a new item using an <code>InputStream</code> as
	 * image source, and "" as label.
	 *
	 * @param is an inputStream pointing to an image that can be loaded
	 *           with <code>ImageIO</code>.
	 */
	public ImageFlowItem(InputStream is) {
		this(is, "");
	}

	/**
	 * Creates a new item using an <code>InputStream</code> as
	 * image source, and the provided label.
	 *
	 * @param is an inputStream pointing to an image that can be loaded
	 *           with <code>ImageIO</code>.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(InputStream is, String label) {
		this(new InputStreamLazyImageLoader(is), label);
	}

	/**
	 * Creates a new item using an <code>URL</code> as
	 * image source, and "" as label.
	 *
	 * @param url an url pointing to an image that can be loaded
	 *            with <code>ImageIO</code>.
	 */
	public ImageFlowItem(URL url) {
		this(url, "");
	}

	/**
	 * Creates a new item using an <code>URL</code> as
	 * image source, and the provided label.
	 *
	 * @param url an url pointing to an image that can be loaded
	 *            with <code>ImageIO</code>.
	 * @param label a non-null <code>String</code>.
	 */
	public ImageFlowItem(URL url, String label) {
		this(new URLLazyImageLoader(url), label);
	}

	private ImageFlowItem(LazyImageLoader imageLoader, String label) {
		this.imageLoader = imageLoader;
		this.label = label != null ? label : "";
	}

	/**
	 * Builds a list of <code>ImageFlowItem</code>s, one per each file
	 * in <code>directory</code>
	 * 
	 * @param directory a <code>File</code> that contains image files.
	 * @return a List of ImageFlowItems.
	 */
	public static List<ImageFlowItem> loadFromDirectory(File directory) {
		List<ImageFlowItem> list = new ArrayList<ImageFlowItem>();

		if (!directory.isDirectory()) {
			return list;
		}

		File[] files = directory.listFiles();

		for (int index = 0; index < files.length; index++) {
			ImageFlowItem item = new ImageFlowItem(files[index], files[index].getName());
			list.add(item);
		}

		return list;
	}

	/**
	 * Returns the <code>Image</code> of this item.<p>
	 * If the image is not loaded then it will atempt loading
	 * it from its source.
	 * 
	 * @return an <code>Image</code>.
	 */
	public Image getImage() {
		return imageLoader.getImage();
	}

	/**
	 * Returns the item's label.
	 * 
	 * @return a String as the item's label.
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return super.toString() + "[label: '" + label + "', image: " + imageLoader.toString() + "]";
	}

	/**
	 * Helper class to locate classpath resources.
	 * 
	 * @author Andres.Almiray
	 */
	public static class Resource {

		private final Class loadingClass;
		private final String resourceName;

		public Resource(String resourceName) {
			this(resourceName, ImageFlowItem.class);
		}

		public Resource(String resourceName, Class loadingClass) {
			this.resourceName = resourceName;
			this.loadingClass = loadingClass != null ? loadingClass : ImageFlowItem.class;
		}

		public String getResourceName() {
			return resourceName;
		}

		public Class getLoadingClass() {
			return loadingClass;
		}

		public URL getResource() {
			return loadingClass.getResource(resourceName);
		}

		public InputStream getResourceAsStream() {
			return loadingClass.getResourceAsStream(resourceName);
		}

		@Override
		public String toString() {
			return "[resourceName: " + resourceName + ", loadingClass: " + loadingClass.getName() + "]";
		}
	}

	private static abstract class LazyImageLoader {

		private Image image;
		private final Logger log = Logger.getLogger(LazyImageLoader.class.getName());

		public Image getImage() {
			if (image == null) {
				try {
					CrystalCaseFactory fx = CrystalCaseFactory.getInstance();
                    Image img = loadImage();
					image = fx.createReflectedPicture(fx.createCrystalCase(img));
				} catch (IOException ioe) {
					// ignore
					log.log(Level.INFO, "Unnable to load image for " + this, ioe);
				}
			}
			return image;
		}

		protected abstract Image loadImage() throws IOException;
	}

	private static class ResourceLazyImageLoader extends LazyImageLoader {

		private final Resource resource;

		public ResourceLazyImageLoader(Resource resource) {
			this.resource = resource;
		}

		protected Image loadImage() throws IOException {
			return ImageIO.read(resource.getResource());
		}

		@Override
		public String toString() {
			return "resource: " + resource.toString();
		}
	}

	private static class ImageLazyImageLoader extends LazyImageLoader {

		private final Image image;

		public ImageLazyImageLoader(Image image) {
			this.image = image;
		}

		protected Image loadImage() throws IOException {
			return image;
		}

		@Override
		public String toString() {
			return "image: " + image.toString();
		}
	}

	private static class URLLazyImageLoader extends LazyImageLoader {

		private final URL url;

		public URLLazyImageLoader(URL url) {
			this.url = url;
		}

		protected Image loadImage() throws IOException {
			return ImageIO.read(url);
		}

		@Override
		public String toString() {
			return "url: " + url.toString();
		}
	}

	private static class FileLazyImageLoader extends LazyImageLoader {

		private final File file;

		public FileLazyImageLoader(File file) {
			this.file = file;
		}

		protected Image loadImage() throws IOException {
			return ImageIO.read(file);
		}

		@Override
		public String toString() {
			return "file: " + file.getAbsolutePath().toString();
		}
	}

	private static class InputStreamLazyImageLoader extends LazyImageLoader {

		private final InputStream is;

		public InputStreamLazyImageLoader(InputStream is) {
			this.is = is;
		}

		protected Image loadImage() throws IOException {
			return ImageIO.read(is);
		}

		@Override
		public String toString() {
			return "inputStream:" + is;
		}
	}
}
