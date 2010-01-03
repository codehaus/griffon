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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.ListModel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

/**
 * About this Code
 *
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
 * - Changed name of class to ImageFlow from CDShelf
 * - Removed hard coded strings for labels and images
 * - Removed requirement for images to be included in the jar
 * - Support for non-square images
 * - Support for loading images from thumbnails
 * - External methods to set and get currently selected item
 * - Added support for ListSelectionListener
 *
 * @author Romain.Guy
 * @author Kevin.Long
 * @author Andres.Almiray
 */
public class ImageFlow extends JPanel {

	private static final double ANIM_SCROLL_DELAY = 450;
	private static final int CD_SIZE = 148;
	private int displayWidth = CD_SIZE;
	private int displayHeight = (int) (CD_SIZE * 2 / 1.12);
	private String itemText = null;
	private Timer scrollerTimer = null;
	private Timer faderTimer = null;
	private float veilAlphaLevel = 0.0f;
	private float alphaLevel = 0.0f;
	private float textAlphaLevel = 0.0f;
	private int itemIndex = -1;
	private double itemPosition = 0.0;
	private double itemSpacing = 0.4;
	private int amount = 5;
	private double sigma;
	private double rho;
	private double exp_multiplier;
	private double exp_member;
	private boolean damaged = true;
	private DrawableItem[] drawableItems;
	private FocusGrabber focusGrabber;
	private ItemScroller itemScroller;
	private MouseItemSelector mouseItemSelector;
	private CursorChanger cursorChanger;
	private MouseWheelScroller wheelScroller;
	private KeyScroller keyScroller;
	private KeyItemSelector keyItemSelector;
	private Font itemFont = new Font("Dialog", Font.PLAIN, 24);
	private CrystalCaseFactory fx;
	private List<ListSelectionListener> listSelectionListeners;
	private ListModel dataModel;
	private Color itemTextColor = Color.WHITE;

	/**
	 * Constructs a <code>ImageFlow</code> with an empty, mutable, model.
	 */
	public ImageFlow() {
		this(new DefaultListModel());
	}

	/**
	 * Constructs a <code>ImageFlow</code> that displays the elements in
	 * the specified array. This constructor creates a read-only model
	 * for the given array, and then delegates to the constructor that
	 * takes a {@code ListModel}.
	 * <p>
	 * Attempts to pass a {@code null} value to this method results in
	 * undefined behavior and, most likely, exceptions. The created model
	 * references the given array directly. Attempts to modify the array
	 * after constructing the list results in undefined behavior.
	 *
	 * @param  listData  the array of ImageFlowItems to be loaded into the data model,
	 *                   {@code non-null}
	 */
	public ImageFlow(final ImageFlowItem[] listData) {
		this(
				new AbstractListModel() {

					public int getSize() {
						return listData.length;
					}

					public Object getElementAt(int i) {
						return listData[i];
					}
				});
	}

	/**
	 * Constructs a <code>ImageFlow</code> that displays the elements in
	 * the specified <code>List</code>. This constructor creates a read-only
	 * model for the given {@code List}, and then delegates to the constructor
	 * that takes a {@code ListModel}.
	 * <p>
	 * Attempts to pass a {@code null} value to this method results in
	 * undefined behavior and, most likely, exceptions. The created model
	 * references the given {@code List} directly. Attempts to modify the
	 * {@code List} after constructing the list results in undefined behavior.
	 *
	 * @param  listData  the <code>List</code> to be loaded into the
	 *                 data model, {@code non-null}
	 */
	public ImageFlow(final List<?> listData) {
		this(
				new AbstractListModel() {

					public int getSize() {
						return listData.size();
					}

					public Object getElementAt(int i) {
						return listData.get(i);
					}
				});
	}

	/**
	 * Constructs a {@code ImageFlow} that displays elements from the specified,
	 * {@code non-null}, model. All {@code ImageFlow} constructors delegate to
	 * this one.
	 * <p>
	 *
	 * @param dataModel the model for the list
	 * @exception IllegalArgumentException if the model is {@code null}
	 */
	public ImageFlow(ListModel dataModel) {
		if (dataModel == null) {
			throw new IllegalArgumentException("dataModel must be non null");
		}

		this.dataModel = dataModel;
		fx = CrystalCaseFactory.getInstance();
		setLayout(new GridBagLayout());
		setSigma(0.5);
		addComponentListener(new DamageManager());
		initInputListeners();
		addInputListeners();
		listSelectionListeners = new ArrayList<ListSelectionListener>();
		setItemIndex(amount / 2);
		startFader();
	}

	/**
	 * Returns the data model that holds the list of items displayed
	 * by the <code>ImageFlow</code> component.
	 *
	 * @return the <code>ListModel</code> that provides the displayed
	 *                list of items
	 * @see #setModel
	 */
	public ListModel getModel() {
		return dataModel;
	}

	/**
	 * Sets the model that represents the contents or "value" of the
	 * list, notifies property change listeners.
	 * <p>
	 * This is a JavaBeans bound property.
	 *
	 * @param model  the <code>ListModel</code> that provides the
	 *                        list of items for display
	 * @exception IllegalArgumentException  if <code>model</code> is
	 *                        <code>null</code>
	 * @see #getModel
	 * @beaninfo
	 *       bound: true
	 *   attribute: visualUpdate true
	 * description: The object that contains the data to be drawn by this ImageFlow.
	 */
	public void setModel(ListModel model) {
		if (model == null) {
			throw new IllegalArgumentException("model must be non null");
		}
		ListModel oldValue = dataModel;
		dataModel = model;
		firePropertyChange("model", oldValue, dataModel);
		adjust();
	}

	private void adjust() {
		int size = dataModel.getSize();
		if (amount > size) {
			setAmount(size);
			setItemIndex(amount / 2);
		}
	}

	/**
	 * Returns the <code>Font</code> used for drawing an item's label.
	 * @return the <code>Font</code> used for drawing an item's label.
	 * @see #setItemFont
	 */
	public Font getItemFont() {
		return itemFont;
	}

	/**
	 * Sets the <code>Font</code> that is used for drawing an item's label,
	 * notifies property change listeners.
	 * <p>
	 * This is a JavaBeans bound property.
	 *
	 * @param itemFont  the <code>Font</code> that will be used to draw
	 *                  an item's label.
	 * @see #getItemFont
	 * @beaninfo
	 *       bound: true
	 *   attribute: visualUpdate true
	 * description: The font for drawing item labels.
	 */
	public void setItemFont(Font itemFont) {
		Font oldValue = this.itemFont;
		if (itemFont != null && !oldValue.equals(itemFont)) {
			this.itemFont = itemFont;
			firePropertyChange("itemFont", oldValue, itemFont);
			repaint();
		}
	}

	/**
	 * Returns the <code>Color</code> used to draw an item's label.
	 * @return the <code>Color</code> used to draw an item's label.
	 * @see #setItemColor
	 */
	public Color getItemTextColor() {
		return itemTextColor;
	}

	/**
	 * Sets the <code>Color</code> that is used for drawing an item's label,
	 * notifies property change listeners.
	 * <p>
	 * This is a JavaBeans bound property.
	 *
	 * @param itemItextColor  the <code>Color</code> that will be used to draw
	 *                        an item's label.
	 * @see #getItemTextColor
	 * @beaninfo
	 *       bound: true
	 *   attribute: visualUpdate true
	 * description: The color for drawing item labels.
	 */
	public void setItemTextColor(Color itemTextColor) {
		Color oldValue = this.itemTextColor;
		if (itemTextColor != null && !oldValue.equals(itemTextColor)) {
			this.itemTextColor = itemTextColor;
			firePropertyChange("itemTextColor", oldValue, itemTextColor);
			repaint();
		}
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		if (amount > dataModel.getSize()) {
			throw new IllegalArgumentException("Amount cannot be greater than the current size: " + dataModel.getSize());
		}
		int oldValue = this.amount;
		if (this.amount != amount) {
			this.amount = amount;
			firePropertyChange("amount", oldValue, amount);
			repaint();
		}
	}

	private void setItemPosition(double itemPosition) {
		if (this.itemPosition != itemPosition) {
			this.itemPosition = itemPosition;
			this.damaged = true;
			repaint();
		}
	}

	public double getSigma() {
		return sigma;
	}

	public void setSigma(double sigma) {
		double oldValue = this.sigma;
		if (oldValue != sigma) {
			this.sigma = sigma;
			this.rho = 1.0;
			computeEquationParts();
			this.rho = computeModifierUnprotected(0.0);
			computeEquationParts();
			this.damaged = true;
			firePropertyChange("sigma", oldValue, sigma);
			repaint();
		}
	}

	public double getItemSpacing() {
		return itemSpacing;
	}

	public void setItemSpacing(double itemSpacing) {
		if (itemSpacing < 0.0 || itemSpacing > 1.0) {
			throw new IllegalArgumentException("Spacing must be < 1.0 and > 0.0");
		}
		double oldValue = this.itemSpacing;
		if (oldValue != itemSpacing) {
			this.itemSpacing = itemSpacing;
			this.damaged = true;
			firePropertyChange("itemSpacing", oldValue, itemSpacing);
			repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(displayWidth * 5, (int) (displayHeight * 3));
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public boolean isFocusable() {
		return true;
	}

	public void next() {
		scrollAndAnimateBy(1);
	}

	public void previous() {
		scrollAndAnimateBy(-1);
	}

	@Override
	protected void paintChildren(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				veilAlphaLevel));
		super.paintChildren(g);
		g2.setComposite(oldComposite);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (!isShowing()) {
			return;
		}

		super.paintComponent(g);

		if (faderTimer == null) {
			return;
		}

		Insets insets = getInsets();

		int x = insets.left;
		int y = insets.top;

		int width = getWidth() - insets.left - insets.right;
		int height = getHeight() - insets.top - insets.bottom;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Composite oldComposite = g2.getComposite();

		if (damaged) {
			drawableItems = sortItemsByDepth(x, y, width, height);
			damaged = false;
		}

		drawItems(g2, drawableItems);

		if (drawableItems.length > 0) {
			drawItemName(g2);
		}

		g2.setComposite(oldComposite);
	}

	private ImageFlowItem getItemAt(int index) {
		return (ImageFlowItem) dataModel.getElementAt(index);
	}

	private void drawItems(Graphics2D g2, DrawableItem[] drawableItems) {
		for (DrawableItem item : drawableItems) {
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) item.getAlpha());
			g2.setComposite(composite);
			g2.drawImage(getItemAt(item.getIndex()).getImage(),
					(int) item.getX(), (int) item.getY(),
					item.getWidth(), item.getHeight(), null);
		}
	}

	private DrawableItem[] sortItemsByDepth(int x, int y,
			int width, int height) {
		List<DrawableItem> drawables = new LinkedList<DrawableItem>();

		int size = dataModel.getSize();
		for (int i = 0; i < size; i++) {
			promoteItemToDrawable(drawables,
					x, y, width, height, i - itemIndex);
		}

		DrawableItem[] items = new DrawableItem[drawables.size()];
		items = drawables.toArray(items);
		Arrays.sort(items);
		return items;
	}

	private void drawItemName(Graphics2D g2) {
		if (itemText == null || itemText.length() == 0) {
			return;
		}
		Composite composite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				textAlphaLevel));

		FontRenderContext context = g2.getFontRenderContext();
		TextLayout layout = new TextLayout(itemText, itemFont, context);
		Rectangle2D bounds = layout.getBounds();

		double bulletWidth = bounds.getWidth() + 12;
		double bulletHeight = bounds.getHeight() + layout.getDescent() + 4;

		double x = (getWidth() - bulletWidth) / 2.0;
		double y = (getHeight() + CD_SIZE) / 2.0;

		BufferedImage textImage = new BufferedImage((int) bulletWidth,
				(int) bulletHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2text = textImage.createGraphics();
		g2text.setColor(new Color(0, 0, 0, 170));
		layout.draw(g2text, 6, layout.getAscent() + 1);
		g2text.setColor(itemTextColor);
		layout.draw(g2text, 6, layout.getAscent());
		g2text.dispose();

		g2.drawImage(fx.createReflectedPicture(textImage,
				fx.createGradientMask((int) bulletWidth,
				(int) bulletHeight)),
				(int) x, (int) y, null);
		g2.setComposite(composite);
	}

	private void promoteItemToDrawable(List<DrawableItem> drawables,
			int x, int y, int width, int height,
			int offset) {

		double spacing = offset * itemSpacing;
		double position = itemPosition + spacing;

		if (itemIndex + offset < 0 ||
				itemIndex + offset >= dataModel.getSize()) {
			return;
		}

		Image item = getItemAt(itemIndex + offset).getImage();

		int itemWidth = displayWidth;//item.getWidth(null);
		int itemHeight = displayHeight;//item.getHeight(null);

		double result = computeModifier(position);
		int newWidth = (int) (itemWidth * result);
		if (newWidth == 0) {
			return;
		}
		int newHeight = (int) (itemHeight * result);
		if (newHeight == 0) {
			return;
		}

		double item_x = x + (width - newWidth) / 2.0;
		double item_y = y + (height - newHeight / 2.0) / 2.0;

		double semiWidth = width / 2.0;
		item_x += position * semiWidth;

		if (item_x >= width || item_x < -newWidth) {
			return;
		}

		drawables.add(new DrawableItem(itemIndex + offset,
				item_x, item_y,
				newWidth, newHeight,
				position, result));
	}

	private void computeEquationParts() {
		exp_multiplier = Math.sqrt(2.0 * Math.PI) / sigma / rho;
		exp_member = 4.0 * sigma * sigma;
	}

	private double computeModifier(double x) {
		double result = computeModifierUnprotected(x);
		if (result > 1.0) {
			result = 1.0;
		} else if (result < -1.0) {
			result = -1.0;
		}
		return result;
	}

	private double computeModifierUnprotected(double x) {
		return exp_multiplier * Math.exp((-x * x) / exp_member);
	}

	private void addInputListeners() {
		addMouseListener(focusGrabber);
		addMouseListener(itemScroller);
		addMouseListener(mouseItemSelector);
		addMouseMotionListener(cursorChanger);
		addMouseWheelListener(wheelScroller);
		addKeyListener(keyScroller);
		addKeyListener(keyItemSelector);
	}

	private void initInputListeners() {
		// input listeners are all stateless
		// hence they can be instantiated once
		focusGrabber = new FocusGrabber();
		itemScroller = new ItemScroller();
		mouseItemSelector = new MouseItemSelector();
		cursorChanger = new CursorChanger();
		wheelScroller = new MouseWheelScroller();
		keyScroller = new KeyScroller();
		keyItemSelector = new KeyItemSelector();
	}

//    private void removeInputListeners() {
//        removeMouseListener(focusGrabber);
//        removeMouseListener(itemScroller);
//        removeMouseListener(mouseItemSelector);
//        removeMouseMotionListener(cursorChanger);
//        removeMouseWheelListener(wheelScroller);
//        removeKeyListener(keyScroller);
//        removeKeyListener(keyItemSelector);
//    }
	private void setItemIndex(int index) {
		if (index >= 0 && index < dataModel.getSize()) {
			itemIndex = index;
			itemText = getItemAt(index).getLabel();
			notifyListSelectionListener();
		}
	}

	private void scrollBy(int increment) {
		setItemIndex(itemIndex + increment);
		if (itemIndex < 0) {
			setItemIndex(0);
		} else if (itemIndex >= dataModel.getSize()) {
			setItemIndex(dataModel.getSize() - 1);
		}
		damaged = true;
		repaint();
	}

	private void scrollAndAnimateBy(int increment) {
		if (scrollerTimer == null || !scrollerTimer.isRunning()) {
			int index = itemIndex + increment;
			if (index < 0) {
				index = 0;
			} else if (index >= dataModel.getSize()) {
				index = dataModel.getSize() - 1;
			}

			if (index == itemIndex) {
				return;
			}

			DrawableItem drawable = null;

			if (drawableItems != null) {
				for (DrawableItem item : drawableItems) {
					if (item.index == index) {
						drawable = item;
						break;
					}
				}
			}


			if (drawable != null) {
				scrollAndAnimate(drawable);
			}
		}
	}

	private void scrollAndAnimate(DrawableItem item) {
		scrollerTimer = new Timer(33, new AutoScroller(item));
		scrollerTimer.start();
	}

	private DrawableItem getHitItem(int x, int y) {
		for (DrawableItem item : drawableItems) {
			Rectangle hit = new Rectangle((int) item.getX(), (int) item.getY(),
					item.getWidth(), item.getHeight() / 2);
			if (hit.contains(x, y)) {
				return item;
			}
		}
		return null;
	}

	private void startFader() {
		faderTimer = new Timer(35, new FaderAction());
		faderTimer.start();
	}

	/**
	 * List Methods
	 */
	/**
	 * Returns the largest selected cell index
	 *
	 * @return int the largest selected cell index
	 */
	public int getMaxSelectionIndex() {
		if (this.dataModel == null) {
			return 0;
		} else {
			return dataModel.getSize();
		}
	}

	/**
	 * Returns the smallets selected cell index
	 *
	 * @return int the largest selected cell index
	 */
	public int getMinSelectionIndex() {
		return 0;
	}

	/**
	 * Returns the first selected index; returns -1 if there is no selected item
	 * @return int
	 */
	public int getSelectedIndex() {
		return itemIndex;
	}

	/**
	 * Returns the first selected value, or null if the selection is empty.
	 * @return Object
	 */
	public Object getSelectedValue() {
		if (this.dataModel == null || itemIndex < 0) {
			return null;
		}

		return getItemAt(itemIndex);
	}

	/**
	 * Returns true if the specified index is selected.
	 * @param index int
	 * @return boolean
	 */
	public boolean isSelectedIndex(int index) {
		return (itemIndex == index);
	}

	/**
	 * Selects a single cell
	 * @param index int
	 */
	public void setSelectedIndex(int index) {
		this.scrollBy(index - itemIndex);
	}

	/**
	 * Adds a listener to the list that's notified each time a change to the selection occur
	 * @param listener ListSelectionListener
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		listSelectionListeners.add(listener);
	}

	/**
	 * Removes a listener from the list that's notified each time a change to the selection occurs
	 * @param listener ListSelectionListener
	 */
	public void removeListSelectionListener(ListSelectionListener listener) {
		listSelectionListeners.remove(listener);
	}

	/**
	 * Notify the listeners when a selection event has occured
	 */
	private void notifyListSelectionListener() {
		ListSelectionEvent event = new ListSelectionEvent(this, itemIndex, itemIndex, false);

		for (ListSelectionListener listener : listSelectionListeners) {
			listener.valueChanged(event);
		}
	}

	private class FaderAction implements ActionListener {

		private long start = 0;

		private FaderAction() {
			alphaLevel = 0.0f;
			textAlphaLevel = 0.0f;
		}

		public void actionPerformed(ActionEvent e) {
			if (!isShowing()) {
				return;
			}
			if (start == 0) {
				start = System.currentTimeMillis();
			}

			alphaLevel = (System.currentTimeMillis() - start) / 500.0f;
			textAlphaLevel = alphaLevel;
			if (alphaLevel > 1.0f) {
				alphaLevel = 1.0f;
				textAlphaLevel = 1.0f;
				faderTimer.stop();
			}

			repaint();
		}
	}

	private class DrawableItem implements Comparable {

		private int index;
		private double x;
		private double y;
		private int width;
		private int height;
		private double zOrder;
		private double position;

		private DrawableItem(int index,
				double x, double y, int width, int height,
				double position, double zOrder) {
			this.index = index;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.position = position;
			this.zOrder = zOrder;
		}

		public int compareTo(Object o) {
			double zOrder2 = ((DrawableItem) o).zOrder;
			if (zOrder < zOrder2) {
				return -1;
			} else if (zOrder > zOrder2) {
				return 1;
			}
			return 0;
		}

		public double getPosition() {
			return position;
		}

		public double getAlpha() {
			return zOrder * alphaLevel;
		}

		public int getHeight() {
			return height;
		}

		public int getIndex() {
			return index;
		}

		public int getWidth() {
			return width;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		@Override
		public String toString() {
			return super.toString() + "[index: " + index + ", item: " + getItemAt(index).toString() + "]";
		}
	}

	private class MouseWheelScroller implements MouseWheelListener {

		public void mouseWheelMoved(MouseWheelEvent e) {
			int increment = e.getWheelRotation();
			scrollAndAnimateBy(increment);
		}
	}

	private class KeyScroller extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_UP:
					scrollAndAnimateBy(-1);
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_DOWN:
					scrollAndAnimateBy(1);
					break;
				case KeyEvent.VK_END:
					scrollBy(dataModel.getSize() - itemIndex - 1);
					break;
				case KeyEvent.VK_HOME:
					scrollBy(-itemIndex - 1);
					break;
				case KeyEvent.VK_PAGE_UP:
					scrollAndAnimateBy(-amount / 2);
					break;
				case KeyEvent.VK_PAGE_DOWN:
					scrollAndAnimateBy(amount / 2);
					break;
			}
		}
	}

	private class FocusGrabber extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			requestFocus();
		}
	}

	private class ItemScroller extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if ((scrollerTimer != null && scrollerTimer.isRunning()) ||
					drawableItems == null) {
				return;
			}

			if (e.getButton() == MouseEvent.BUTTON1) {
				DrawableItem item = getHitItem(e.getX(), e.getY());
				if (item != null && item.getIndex() != itemIndex) {
					scrollAndAnimate(item);
				}
			}
		}
	}

	private class DamageManager extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			damaged = true;
		}
	}

	private class AutoScroller implements ActionListener {

		private double position;
		private int index;
		private long start;

		private AutoScroller(DrawableItem item) {
			this.index = item.getIndex();
			this.position = item.getPosition();
			this.start = System.currentTimeMillis();
		}

		public void actionPerformed(ActionEvent e) {
			if (!isShowing()) {
				return;
			}
			long elapsed = System.currentTimeMillis() - start;
			if (elapsed < ANIM_SCROLL_DELAY / 2.0) {
				textAlphaLevel = (float) (1.0 - 2.0 * (elapsed / ANIM_SCROLL_DELAY));
			} else {
				itemText = getItemAt(index).getLabel();
				textAlphaLevel = (float) (((elapsed / ANIM_SCROLL_DELAY) - 0.5) * 2.0);
				if (textAlphaLevel > 1.0f) {
					textAlphaLevel = 1.0f;
				}
			}
			if (textAlphaLevel < 0.1f) {
				textAlphaLevel = 0.1f;
			}
			double newPosition = (elapsed / ANIM_SCROLL_DELAY) * -position;

			if (elapsed >= ANIM_SCROLL_DELAY) {
				((Timer) e.getSource()).stop();
				setItemIndex(index);
				setItemPosition(0.0);
				return;
			}

			setItemPosition(newPosition);
		}
	}

	private class CursorChanger extends MouseMotionAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {
			if ((scrollerTimer != null && scrollerTimer.isRunning()) ||
					drawableItems == null) {
				return;
			}

			DrawableItem item = getHitItem(e.getX(), e.getY());
			if (item != null) {
				getParent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	private class KeyItemSelector extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if ((scrollerTimer == null || !scrollerTimer.isRunning()) &&
					drawableItems != null) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				}
			}
		}
	}

	private class MouseItemSelector extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if ((scrollerTimer == null || !scrollerTimer.isRunning()) &&
					drawableItems != null) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					DrawableItem item = getHitItem(e.getX(), e.getY());
					if (item != null && item.getIndex() == itemIndex) {
					}
				}
			}
		}
	}
}
