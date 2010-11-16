package eu.hansolo.custom;


/**
 *
 * @author hansolo
 */
public class Counter extends javax.swing.JComponent implements java.awt.event.ComponentListener
{
    // <editor-fold defaultstate="collapsed" desc="Variable declaration">
    protected static final String INC_PROPERTY = "inc";
    protected static final String DEC_PROPERTY = "dec";
    protected static final String ZERO_PROPERTY = "zero";
    private boolean zero;
    private int maxValue;
    private boolean incFired;
    private boolean decFired;
    private int fireIncrementThreshold;
    private int fireDecrementThreshold;
    private Theme theme;
    private java.awt.Color backgroundColor;
    private java.awt.image.BufferedImage backgroundImage;
    private final java.util.ArrayList<java.awt.image.BufferedImage> DIGIT_STACK = new java.util.ArrayList<java.awt.image.BufferedImage>(10);    
    private java.awt.image.BufferedImage lenseImage;
    private java.awt.image.BufferedImage stackImage;
    private final java.awt.Point POS;    
    private final java.awt.Rectangle CLIP;
    private final java.awt.Rectangle DIGIT_CLIP;
    private long switchTime;
    private int offset0;
    private int offset1;
    private int oldOffsetIncrement;
    private int offsetIncrement;
    private int oldOffsetDecrement;
    private int offsetDecrement;
    private final org.pushingpixels.trident.Timeline TIMELINE_INCREMENT;
    private final org.pushingpixels.trident.Timeline TIMELINE_DECREMENT;
    private final org.pushingpixels.trident.callback.TimelineCallback TIMELINE_INCREMENT_CALLBACK;
    private final org.pushingpixels.trident.callback.TimelineCallback TIMELINE_DECREMENT_CALLBACK;
    private final org.pushingpixels.trident.ease.Linear LINEAR_EASING;
    private final org.pushingpixels.trident.ease.Spline SPLINE_EASING;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Counter()
    {
        this.zero = true;
        this.maxValue = 10;
        this.incFired = true;
        this.decFired = false;
        this.fireIncrementThreshold = 0;
        this.fireDecrementThreshold = 0;
        this.theme = Theme.BRIGHT;
        this.backgroundColor = java.awt.Color.RED;
        this.POS = new java.awt.Point(0, 0);
        this.CLIP = new java.awt.Rectangle(0, 0, 44, 110);
        this.DIGIT_CLIP = new java.awt.Rectangle(2, 2, 40, 106);
        this.switchTime = 500;
        this.offset0 = 0;
        this.offset1 = 0;
        this.oldOffsetIncrement = 0;
        this.offsetIncrement = 0;
        this.oldOffsetDecrement = 0;
        this.offsetDecrement = 0;
        this.TIMELINE_INCREMENT = new org.pushingpixels.trident.Timeline(this);
        this.TIMELINE_DECREMENT = new org.pushingpixels.trident.Timeline(this);
        this.TIMELINE_INCREMENT_CALLBACK = new org.pushingpixels.trident.callback.TimelineCallback()
        {
            @Override
            public void onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState oldState, org.pushingpixels.trident.Timeline.TimelineState newState, float oldValue, float newValue)
            {
                if (newState == org.pushingpixels.trident.Timeline.TimelineState.DONE)
                {
                    setOffsetIncrement(DIGIT_STACK.get(0).getHeight());
                    repaint();
                }
            }

            @Override
            public void onTimelinePulse(float oldValue, float newValue)
            {
                if (offset0 >= fireIncrementThreshold && offset0 < fireIncrementThreshold + 10)
                {
                    if (!incFired)
                    {
                        firePropertyChange(ZERO_PROPERTY, false, true);
                        firePropertyChange(INC_PROPERTY, false, true);                        
                        incFired = true;
                        zero = true;
                    }
                }

                if (offset1 >= fireIncrementThreshold && offset1 < fireIncrementThreshold + 10)
                {
                    if (!incFired)
                    {
                        firePropertyChange(ZERO_PROPERTY, false, true);
                        firePropertyChange(INC_PROPERTY, false, true);                        
                        incFired = true;
                        zero = true;
                    }
                }
                repaint();
            }
        };
        this.TIMELINE_DECREMENT_CALLBACK = new org.pushingpixels.trident.callback.TimelineCallback()
        {
            @Override
            public void onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState oldState, org.pushingpixels.trident.Timeline.TimelineState newState, float oldValue, float newValue)
            {
                if (newState == org.pushingpixels.trident.Timeline.TimelineState.DONE)
                {
                    setOffsetDecrement(DIGIT_STACK.get(0).getHeight());
                    repaint();
                }
            }

            @Override
            public void onTimelinePulse(float oldValue, float newValue)
            {
                if (offset0 <= fireDecrementThreshold + DIGIT_STACK.get(0).getHeight() && offset0 > fireDecrementThreshold + DIGIT_STACK.get(0).getHeight() - 10)
                {
                    firePropertyChange(ZERO_PROPERTY, false, true);
                    zero = true;
                }

                if (offset1 <= fireDecrementThreshold + DIGIT_STACK.get(0).getHeight() && offset1 > fireDecrementThreshold + DIGIT_STACK.get(0).getHeight() - 10)
                {
                    firePropertyChange(ZERO_PROPERTY, false, true);
                    zero = true;
                }

                if (offset0 <= fireDecrementThreshold && offset0 > fireDecrementThreshold - 10)
                {                    
                    if (!decFired)
                    {
                        firePropertyChange(DEC_PROPERTY, false, true);                        
                        decFired = true;                        
                    }                    
                }

                if (offset1 <= fireDecrementThreshold && offset1 > fireDecrementThreshold - 10)
                {
                    if (!decFired)
                    {
                        firePropertyChange(DEC_PROPERTY, false, true);                     
                        decFired = true;                        
                    }                    
                }
                repaint();
            }
        };
        this.TIMELINE_INCREMENT.addCallback(TIMELINE_INCREMENT_CALLBACK);
        this.TIMELINE_DECREMENT.addCallback(TIMELINE_DECREMENT_CALLBACK);
        this.LINEAR_EASING = new org.pushingpixels.trident.ease.Linear();
        this.SPLINE_EASING = new org.pushingpixels.trident.ease.Spline(0.9f);
        addComponentListener(this);
        setMinimumSize(new java.awt.Dimension(22, 55));
        setPreferredSize(new java.awt.Dimension(44,110));
        setSize(getPreferredSize());
        init(getWidth(), getHeight());        
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT)
    {
        backgroundImage = create_BACKGROUND_Image(WIDTH);
        DIGIT_STACK.clear();        
        for (int i = 0 ; i < maxValue ; i++)
        {
            DIGIT_STACK.add(create_DIGIT_Image(WIDTH, i));
        }
        lenseImage = create_LENSE_Image(WIDTH);
        stackImage = create_STACK_Image(WIDTH);        
        POS.setLocation(0, (-stackImage.getHeight() - stackImage.getHeight() + backgroundImage.getHeight()));        
        offset0 = ((int) (stackImage.getHeight() - backgroundImage.getHeight() * 0.3));
        offset1 = ((int) (stackImage.getHeight() + stackImage.getHeight() - backgroundImage.getHeight() * 0.3));
        fireIncrementThreshold = ((int) (stackImage.getHeight() + stackImage.getHeight() - backgroundImage.getHeight() * 0.3)) - DIGIT_STACK.get(0).getHeight();
        fireDecrementThreshold = ((int) (stackImage.getHeight() + stackImage.getHeight() - backgroundImage.getHeight() * 0.3));
        CLIP.setBounds(backgroundImage.getMinX(), backgroundImage.getMinY(), backgroundImage.getWidth(), backgroundImage.getHeight());
        DIGIT_CLIP.setBounds(backgroundImage.getMinX() + 2, backgroundImage.getMinY() + 2, backgroundImage.getWidth() - 4, backgroundImage.getHeight() - 4);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Painting method">
    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image
        G2.setClip(CLIP);
        G2.drawImage(backgroundImage, 0, 0, null);

        // Draw digits
        G2.setClip(DIGIT_CLIP);
        G2.drawImage(stackImage, 0, POS.y + offset0, null);
        G2.drawImage(stackImage, 0, POS.y + offset1, null);

        // Draw lense image
        G2.setClip(CLIP);
        G2.drawImage(lenseImage, 0, 0, null);

        G2.dispose();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getter/Setter">   
    public void increment()
    {        
        if (TIMELINE_INCREMENT.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
        {            
            TIMELINE_INCREMENT.abort();
            setOffsetIncrement(DIGIT_STACK.get(0).getHeight());
            repaint();
        }
        TIMELINE_INCREMENT.addPropertyToInterpolate("offsetIncrement", 0, DIGIT_STACK.get(0).getHeight());
        if (switchTime < 350)
        {
            TIMELINE_INCREMENT.setEase(LINEAR_EASING);
        }
        else
        {
            TIMELINE_INCREMENT.setEase(SPLINE_EASING);
        }
        zero = false;
        TIMELINE_INCREMENT.setDuration(switchTime);
        TIMELINE_INCREMENT.play();
    }

    public void decrement()
    {
        if (TIMELINE_DECREMENT.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
        {
            TIMELINE_DECREMENT.abort();
            setOffsetDecrement(DIGIT_STACK.get(0).getHeight());
            repaint();
        }
        TIMELINE_DECREMENT.addPropertyToInterpolate("offsetDecrement", 0, DIGIT_STACK.get(0).getHeight());
        if (switchTime < 350)
        {
            TIMELINE_DECREMENT.setEase(LINEAR_EASING);
        }
        else
        {
            TIMELINE_DECREMENT.setEase(SPLINE_EASING);
        }
        zero = false;
        TIMELINE_DECREMENT.setDuration(switchTime);
        TIMELINE_DECREMENT.play();
    }

    public Theme getTheme()
    {
        return this.theme;
    }

    public void setTheme(final Theme THEME)
    {
        this.theme = THEME;
        init(getWidth(), getHeight());
        repaint();
    }

    public java.awt.Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    public void setBackgroundColor(final java.awt.Color BACKGROUND_COLOR)
    {
        this.backgroundColor = BACKGROUND_COLOR;
        init(getWidth(), getHeight());
        repaint();
    }

    public int getOffsetIncrement()
    {
        return this.offsetIncrement;
    }

    public void setOffsetIncrement(final int OFFSET_INCREMENT)
    {
        this.oldOffsetIncrement = OFFSET_INCREMENT == 0 ? 0 : this.offsetIncrement;
        this.offsetIncrement = OFFSET_INCREMENT;
        
        offset0 += (this.offsetIncrement - this.oldOffsetIncrement);
        offset1 += (this.offsetIncrement - this.oldOffsetIncrement);
        
        if ((POS.y + offset0) >= backgroundImage.getHeight())
        {            
            offset0 -= (stackImage.getHeight() + stackImage.getHeight());
            incFired = false;
        }

        if ((POS.y + offset1) >= backgroundImage.getHeight())
        {
            offset1 -= (stackImage.getHeight() + stackImage.getHeight());
            incFired = false;
        }
    }

    public int getOffsetDecrement()
    {
        return this.offsetDecrement;
    }

    public void setOffsetDecrement(final int OFFSET_DECREMENT)
    {
        this.oldOffsetDecrement = OFFSET_DECREMENT == 0 ? 0 : this.offsetDecrement;
        this.offsetDecrement = OFFSET_DECREMENT;

        offset0 -= (this.offsetDecrement - this.oldOffsetDecrement);
        offset1 -= (this.offsetDecrement - this.oldOffsetDecrement);

        if ((POS.y + offset0) <= -stackImage.getHeight())
        {
            offset0 += (stackImage.getHeight() + stackImage.getHeight());
            decFired = false;
        }

        if ((POS.y + offset1) <= -stackImage.getHeight())
        {
            offset1 += (stackImage.getHeight() + stackImage.getHeight());
            decFired = false;
        }
    }

    public long getSwitchTime()
    {
        return this.switchTime;
    }

    public void setSwitchTime(final long SWITCH_TIME)
    {
        this.switchTime = SWITCH_TIME;
    }

    public int getMaxValue()
    {
        return this.maxValue;
    }

    public void setMaxValue(final int MAX_VALUE)
    {
        this.maxValue = MAX_VALUE > 10 ? 10 : MAX_VALUE;
        this.maxValue = MAX_VALUE < 3 ? 3 : MAX_VALUE;
        init(getWidth(), getHeight());
        repaint();
    }

    public boolean isZero()
    {
        return this.zero;
    }

    public void reset()
    {
        init(getWidth(), getHeight());
        repaint();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Image creation methods">
    private java.awt.image.BufferedImage create_BACKGROUND_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.5 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.RoundRectangle2D FRAME = new java.awt.geom.RoundRectangle2D.Double(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 11.0, 11.0);
        final java.awt.geom.Point2D FRAME_START = new java.awt.geom.Point2D.Double(0, FRAME.getBounds2D().getMinY() );
        final java.awt.geom.Point2D FRAME_STOP = new java.awt.geom.Point2D.Double(0, FRAME.getBounds2D().getMaxY() );
        final float[] FRAME_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.17f,
            0.28f,
            0.51f,
            0.71f,
            0.82f,
            0.96f,
            0.99f,
            1.0f
        };
        final java.awt.Color[] FRAME_COLORS =
        {
            new java.awt.Color(41, 44, 41, 255),
            new java.awt.Color(41, 40, 41, 255),
            new java.awt.Color(49, 48, 49, 255),
            new java.awt.Color(49, 44, 49, 255),
            new java.awt.Color(49, 48, 49, 255),
            new java.awt.Color(49, 44, 49, 255),
            new java.awt.Color(49, 48, 49, 255),
            new java.awt.Color(41, 44, 41, 255),
            new java.awt.Color(41, 48, 49, 255),
            new java.awt.Color(41, 48, 49, 255)
        };
        final java.awt.LinearGradientPaint FRAME_GRADIENT = new java.awt.LinearGradientPaint(FRAME_START, FRAME_STOP, FRAME_FRACTIONS, FRAME_COLORS);
        G2.setPaint(FRAME_GRADIENT);
        G2.fill(FRAME);

        final java.awt.geom.RoundRectangle2D INNER_BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(1, 1, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, 10.0, 10.0);
        final java.awt.geom.Point2D INNER_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, INNER_BACKGROUND.getBounds2D().getMinY() );
        final java.awt.geom.Point2D INNER_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, INNER_BACKGROUND.getBounds2D().getMaxY() );
        final float[] INNER_BACKGROUND_FRACTIONS =
        {
            0.0f,
            0.48f,
            1.0f
        };
        final java.awt.Color[] INNER_BACKGROUND_COLORS =
        {
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(107, 105, 99, 255),
            new java.awt.Color(0, 0, 0, 255)
        };
        final java.awt.LinearGradientPaint INNER_BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(INNER_BACKGROUND_START, INNER_BACKGROUND_STOP, INNER_BACKGROUND_FRACTIONS, INNER_BACKGROUND_COLORS);
        G2.setPaint(INNER_BACKGROUND_GRADIENT);
        G2.fill(INNER_BACKGROUND);

        final java.awt.geom.RoundRectangle2D BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(2, 2, IMAGE_WIDTH - 4, IMAGE_HEIGHT - 4, 9.0, 9.0);
        final java.awt.Color FILL_COLOR_BACKGROUND;
        switch(theme)
        {
            case BRIGHT:
                FILL_COLOR_BACKGROUND = new java.awt.Color(0xF9FBFA);
                break;

            case DARK:
                FILL_COLOR_BACKGROUND = new java.awt.Color(0x313439);
                break;

            case CUSTOM:
                FILL_COLOR_BACKGROUND = getBackgroundColor();
                break;
            default:
                FILL_COLOR_BACKGROUND = new java.awt.Color(0xF9FBFA);
                break;
        }
        G2.setColor(FILL_COLOR_BACKGROUND);
        G2.fill(BACKGROUND);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_DIGIT_Image(final int WIDTH, final int DIGIT)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, WIDTH, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.Font FONT = new java.awt.Font("Monospaced", java.awt.Font.BOLD, (int) (WIDTH * 1.0454545455));

        final java.awt.font.FontRenderContext RENDER_CONTEXT = new java.awt.font.FontRenderContext(null, true, true);
        final java.awt.font.TextLayout TEXT_LAYOUT = new java.awt.font.TextLayout(Integer.toString(DIGIT), FONT, RENDER_CONTEXT);
        final java.awt.geom.Rectangle2D TEXT_BOUNDARY = new java.awt.geom.Rectangle2D.Double(TEXT_LAYOUT.getBounds().getMinX(), TEXT_LAYOUT.getBounds().getMinY(), TEXT_LAYOUT.getBounds().getWidth(), TEXT_LAYOUT.getBounds().getHeight() + TEXT_LAYOUT.getDescent()/2);

        final java.awt.geom.Point2D TEXT_POS = new java.awt.geom.Point2D.Double((IMAGE_WIDTH - TEXT_BOUNDARY.getWidth()) / 2.0, (IMAGE_HEIGHT - TEXT_BOUNDARY.getHeight()) / 2.0 + TEXT_BOUNDARY.getHeight() - TEXT_LAYOUT.getDescent() / 3);
        final java.awt.Color SHADOW_COLOR;
        final java.awt.Color TEXT_COLOR;
        switch(theme)
        {
            case BRIGHT:
                SHADOW_COLOR = java.awt.Color.WHITE;
                TEXT_COLOR = java.awt.Color.BLACK;
                break;
            case DARK:
                SHADOW_COLOR = java.awt.Color.BLACK;
                TEXT_COLOR = java.awt.Color.WHITE;
                break;
            case CUSTOM:
                if (getLuminance(getBackgroundColor()) > 128)
                {
                    SHADOW_COLOR = java.awt.Color.WHITE;
                    TEXT_COLOR = java.awt.Color.BLACK;
                    break;
                }
                else
                {
                    SHADOW_COLOR = java.awt.Color.BLACK;
                    TEXT_COLOR = java.awt.Color.WHITE;
                    break;
                }
            default:
                SHADOW_COLOR = java.awt.Color.WHITE;
                TEXT_COLOR = java.awt.Color.BLACK;
                break;
        }
        G2.setFont(FONT);
        G2.setColor(SHADOW_COLOR);
        G2.drawString(Integer.toString(DIGIT), (int) TEXT_POS.getX() + 1, (int) TEXT_POS.getY() + 1);
        G2.setColor(TEXT_COLOR);
        G2.drawString(Integer.toString(DIGIT), (int) TEXT_POS.getX(), (int) TEXT_POS.getY());

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_LENSE_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.5 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.RoundRectangle2D ROLL_SHADOW = new java.awt.geom.RoundRectangle2D.Double(1, 1, IMAGE_WIDTH - 2, IMAGE_HEIGHT - 2, 10.0, 10.0);
        final java.awt.geom.Point2D ROLL_SHADOW_START = new java.awt.geom.Point2D.Double(0, ROLL_SHADOW.getBounds2D().getMinY() );
        final java.awt.geom.Point2D ROLL_SHADOW_STOP = new java.awt.geom.Point2D.Double(0, ROLL_SHADOW.getBounds2D().getMaxY() );
        final float[] ROLL_SHADOW_FRACTIONS =
        {
            0.0f,
            0.07f,
            0.0701f,
            0.14f,
            0.1401f,
            0.22f,
            0.23f,
            0.28f,
            0.49f,
            0.4901f,
            0.69f,
            0.7f,
            0.75f,
            0.7501f,
            0.85f,
            0.92f,
            0.93f,
            0.99f,
            1.0f
        };
        final java.awt.Color[] ROLL_SHADOW_COLORS =
        {
            new java.awt.Color(0, 0, 0, 255),
            new java.awt.Color(0, 0, 0, 216),
            new java.awt.Color(0, 0, 0, 211),
            new java.awt.Color(0, 0, 0, 153),
            new java.awt.Color(0, 0, 0, 147),
            new java.awt.Color(0, 0, 0, 56),
            new java.awt.Color(21, 21, 21, 51),
            new java.awt.Color(255, 255, 255, 40),
            new java.awt.Color(255, 255, 255, 0),
            new java.awt.Color(255, 255, 255, 0),
            new java.awt.Color(255, 255, 255, 0),
            new java.awt.Color(255, 255, 255, 4),
            new java.awt.Color(23, 23, 23, 51),
            new java.awt.Color(0, 0, 0, 55),
            new java.awt.Color(0, 0, 0, 147),
            new java.awt.Color(0, 0, 0, 211),
            new java.awt.Color(0, 0, 0, 216),
            new java.awt.Color(0, 0, 0, 252),
            new java.awt.Color(0, 0, 0, 255)
        };
        final java.awt.LinearGradientPaint ROLL_SHADOW_GRADIENT = new java.awt.LinearGradientPaint(ROLL_SHADOW_START, ROLL_SHADOW_STOP, ROLL_SHADOW_FRACTIONS, ROLL_SHADOW_COLORS);
        G2.setPaint(ROLL_SHADOW_GRADIENT);
        G2.fill(ROLL_SHADOW);

        final java.awt.geom.Rectangle2D GLASS = new java.awt.geom.Rectangle2D.Double(0, IMAGE_HEIGHT * 0.3, IMAGE_WIDTH, IMAGE_HEIGHT * 0.4);
        final java.awt.geom.Point2D GLASS_START = new java.awt.geom.Point2D.Double(0, GLASS.getBounds2D().getMinY() );
        final java.awt.geom.Point2D GLASS_STOP = new java.awt.geom.Point2D.Double(0, GLASS.getBounds2D().getMaxY() );
        final float[] GLASS_FRACTIONS =
        {
            0.0f,
            0.06f,
            0.49f,
            0.4901f,
            0.5f,
            1.0f
        };
        final java.awt.Color[] GLASS_COLORS;
        switch (theme)
        {
            case BRIGHT:
                GLASS_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(204, 204, 204, 102),
                    new java.awt.Color(0, 0, 0, 94),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 76),
                    new java.awt.Color(0, 0, 0, 76)
                };
                break;
            case DARK:
                GLASS_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(204, 204, 204, 102),
                    new java.awt.Color(0, 0, 0, 94),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 76),
                    new java.awt.Color(0, 0, 0, 76)
                };
                break;
            case CUSTOM:
                if (getLuminance(getBackgroundColor()) > 128)
                {
                    GLASS_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(204, 204, 204, 102),
                        new java.awt.Color(0, 0, 0, 94),
                        new java.awt.Color(0, 0, 0, 38),
                        new java.awt.Color(0, 0, 0, 38),
                        new java.awt.Color(0, 0, 0, 76),
                        new java.awt.Color(0, 0, 0, 76)
                    };
                    break;
                }
                else
                {
                    GLASS_COLORS = new java.awt.Color[]
                    {
                        new java.awt.Color(204, 204, 204, 102),
                        new java.awt.Color(0, 0, 0, 94),
                        new java.awt.Color(0, 0, 0, 38),
                        new java.awt.Color(0, 0, 0, 38),
                        new java.awt.Color(0, 0, 0, 76),
                        new java.awt.Color(0, 0, 0, 76)
                    };
                    break;
                }
            default:
                GLASS_COLORS = new java.awt.Color[]
                {
                    new java.awt.Color(204, 204, 204, 102),
                    new java.awt.Color(0, 0, 0, 94),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 38),
                    new java.awt.Color(0, 0, 0, 76),
                    new java.awt.Color(0, 0, 0, 76)
                };
                break;
        }
        final java.awt.LinearGradientPaint GLASS_GRADIENT = new java.awt.LinearGradientPaint(GLASS_START, GLASS_STOP, GLASS_FRACTIONS, GLASS_COLORS);
        G2.setPaint(GLASS_GRADIENT);
        G2.fill(GLASS);
        final java.awt.Color STROKE_COLOR_GLASS_FRAME = new java.awt.Color(0x898989);
        G2.setColor(STROKE_COLOR_GLASS_FRAME);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.drawLine(0, (int) GLASS.getMinY(), IMAGE_WIDTH, (int) GLASS.getMinY());
        G2.drawLine(0, (int) GLASS.getMaxY(), IMAGE_WIDTH, (int) GLASS.getMaxY());

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_STACK_Image(final int WIDTH)
    {
        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, DIGIT_STACK.get(0).getHeight() * maxValue, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = (maxValue - 1) ; i > -1 ; i--)
        {
            G2.drawImage(DIGIT_STACK.get(i), 0, 0, null);
            G2.translate(0, DIGIT_STACK.get(i).getHeight());
        }

        G2.dispose();

        return IMAGE;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Utility methods">
    private double getLuminance(final java.awt.Color COLOR)
    {
        final double RED = COLOR.getRed();
        final double GREEN = COLOR.getGreen();
        final double BLUE = COLOR.getBlue();
        return (RED * 0.3 + GREEN * 0.59 + BLUE * 0.11);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ComponentListener methods">
    @Override
    public void componentResized(final java.awt.event.ComponentEvent EVENT)
    {
        if (getWidth() < getMinimumSize().width || getHeight() < getMinimumSize().height)
        {
            setSize(getMinimumSize());
        }

        if (getHeight() != (int) (getWidth() * 2.5))
        {
            setSize((int) (getHeight() / 2.5), getHeight());
        }
        else
        {
            setSize(getWidth(), (int)(getWidth() * 2.5));
        }
        init(getWidth(), getHeight());
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
    // </editor-fold>

    @Override
    public String toString()
    {
        return "Counter";
    }
}
