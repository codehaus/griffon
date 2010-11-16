package eu.hansolo.custom.mbutton;

/**
 *
 * @author grunwald
 */
public class MButtonUI extends javax.swing.plaf.basic.BasicButtonUI implements java.awt.event.ComponentListener, java.beans.PropertyChangeListener
{
    // <editor-fold defaultstate="collapsed" desc="Variable definitions">
    private java.awt.image.BufferedImage backgroundImage;
    private java.awt.image.BufferedImage contentImage;
    private java.awt.image.BufferedImage disabledContentImage;   
    private java.awt.image.BufferedImage foregroundImage;
    final javax.swing.AbstractButton BUTTON;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public MButtonUI(final javax.swing.AbstractButton BUTTON)
    {
        this.BUTTON = BUTTON;
        this.BUTTON.setBorderPainted(false);
        this.BUTTON.setContentAreaFilled(false);
        init(BUTTON.getWidth(), BUTTON.getHeight());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UI related">
    @Override
    public void installUI(javax.swing.JComponent component)
    {
        super.installUI(component);
        component.addComponentListener(this);
        component.addPropertyChangeListener(this);
    }

    @Override
    public void uninstallUI(javax.swing.JComponent component)
    {
        super.uninstallUI(component);
        component.removeComponentListener(this);
        component.removePropertyChangeListener(this);
    }

    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent component)
    {
        return new MButtonUI((javax.swing.AbstractButton) component);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT)
    {
        backgroundImage = create_BACKGROUND_Image(WIDTH, HEIGHT);
        contentImage = create_CONTENT_Image(WIDTH, HEIGHT, true);
        disabledContentImage = create_CONTENT_Image(WIDTH, HEIGHT, false);       
        foregroundImage = create_FOREGROUND_Image(WIDTH, HEIGHT);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Painting methods">
    @Override
    public void paint(java.awt.Graphics g, javax.swing.JComponent comp)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw background image
        G2.drawImage(backgroundImage, 0, 0, null);

        // Draw foreground image
        if (comp.isEnabled())
        {
            G2.drawImage(foregroundImage, 0, 0, null);
        }

        // Free memory
        G2.dispose();

        super.paint(g, comp);
    }

    @Override
    protected void paintText(java.awt.Graphics g, javax.swing.AbstractButton button, java.awt.Rectangle textRect, String text)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw content image
        if (button.isEnabled())
        {
            G2.drawImage(contentImage, 0, 0, null);
        }
        else
        {
            G2.drawImage(disabledContentImage, 0, 0, null);
        }

        G2.dispose();
    }

    @Override
    protected void paintButtonPressed(java.awt.Graphics g, javax.swing.AbstractButton button)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw armed image
        G2.drawImage(backgroundImage, 0, 0, null);

        // Draw foreground image
        G2.drawImage(foregroundImage, 0, 0, null);

        G2.dispose();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Image creation methods">
    private java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.RoundRectangle2D FRAME = new java.awt.geom.RoundRectangle2D.Double(0.0, 0.0, IMAGE_WIDTH, IMAGE_HEIGHT, 12.0, 12.0);
        final java.awt.geom.Point2D FRAME_START = new java.awt.geom.Point2D.Double(0, FRAME.getBounds2D().getMinY() );
        final java.awt.geom.Point2D FRAME_STOP = new java.awt.geom.Point2D.Double(0, FRAME.getBounds2D().getMaxY() );
        final float[] FRAME_FRACTIONS =
        {
            0.0f,
            0.1f,
            1.0f
        };
        final java.awt.Color[] FRAME_COLORS =
        {
            new java.awt.Color(200, 200, 200, 255),
            new java.awt.Color(32, 32,32, 255),
            new java.awt.Color(32, 32, 32, 255)
        };
        final java.awt.LinearGradientPaint FRAME_GRADIENT = new java.awt.LinearGradientPaint(FRAME_START, FRAME_STOP, FRAME_FRACTIONS, FRAME_COLORS);
        G2.setPaint(FRAME_GRADIENT);
        G2.fill(FRAME);

        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(FRAME.getMinX() + 1, FRAME.getMinY() + 1, FRAME.getWidth() - 2, FRAME.getHeight() - 2, 11, 11);
        final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMinY() );
        final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, BACKGROUND.getBounds2D().getMaxY() );
        final float[] BACKGROUND_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] BACKGROUND_COLORS =
        {
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(48, 48, 48, 255)
        };
        final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);
        G2.setPaint(BACKGROUND_GRADIENT);
        G2.fill(BACKGROUND);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_CONTENT_Image(final int WIDTH, final int HEIGHT, final boolean ENABLED)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        if (ENABLED)
        {
            G2.setColor(BUTTON.getForeground());
        }
        else
        {
            G2.setColor(java.awt.Color.DARK_GRAY);
        }
        if (!BUTTON.getText().isEmpty())
        {
            G2.setFont(BUTTON.getFont().deriveFont((int) (0.1111111111111 * IMAGE_WIDTH)));
            final java.awt.font.TextLayout LAYOUT_TEXT = new java.awt.font.TextLayout(BUTTON.getText(), G2.getFont(), RENDER_CONTEXT);
            final java.awt.geom.Rectangle2D BOUNDARY_TEXT = LAYOUT_TEXT.getBounds();
            G2.drawString(BUTTON.getText(), (int) ((IMAGE_WIDTH - BOUNDARY_TEXT.getWidth()) / 2.0), (int) ((IMAGE_HEIGHT - BOUNDARY_TEXT.getHeight()) / 2.0 + BOUNDARY_TEXT.getHeight() - LAYOUT_TEXT.getDescent() / 2));
        }
        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_FOREGROUND_Image(final int WIDTH, final int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.Area HIGHLIGHT = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Double(1, 1, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, 11, 11));
        final java.awt.geom.Area PUNCH = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(IMAGE_WIDTH * (-0.2063492063), IMAGE_HEIGHT * 0.3928571429, IMAGE_WIDTH * 1.4285714286, IMAGE_HEIGHT * 1.9285714286));
        HIGHLIGHT.subtract(PUNCH);

        G2.setColor(new java.awt.Color(255, 255, 255, 35));
        G2.fill(HIGHLIGHT);

        G2.dispose();

        return IMAGE;
    }   
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ComponentListener methods">
    @Override
    public void componentResized(final java.awt.event.ComponentEvent EVENT)
    {
        EVENT.getComponent().setPreferredSize(new java.awt.Dimension(EVENT.getComponent().getWidth(), EVENT.getComponent().getHeight()));
        EVENT.getComponent().setSize(EVENT.getComponent().getPreferredSize());
        init(EVENT.getComponent().getWidth(), EVENT.getComponent().getHeight());
    }

    @Override
    public void componentMoved(final java.awt.event.ComponentEvent EVENT)
    {

    }

    @Override
    public void componentShown(final java.awt.event.ComponentEvent EVENT)
    {

    }

    @Override
    public void componentHidden(final java.awt.event.ComponentEvent EVENT)
    {

    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="PropertyChangeEventListener method">
    @Override
    public void propertyChange(final java.beans.PropertyChangeEvent EVENT)
    {
        init(BUTTON.getWidth(), BUTTON.getHeight());
        BUTTON.repaint();
    }
    //</editor-fold>

    @Override
    public String toString()
    {
        return "MButtonUI";
    }
}
