package eu.hansolo.custom.mbutton;


/**
 *
 * @author grunwald
 */
public class MButton extends javax.swing.JButton implements java.awt.event.ComponentListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener
{
    // <editor-fold defaultstate="collapsed" desc="Variable definitions">
    private java.awt.image.BufferedImage effectImageStandard;
    private java.awt.image.BufferedImage effectImageClicked;
    private java.awt.image.BufferedImage effectImage;
    private boolean mouseOver = false;
    private boolean mouseClicked = false;
    private boolean mousePressed = false;
    private boolean mouseMovedOutside = false;
    private float alpha = 0.0f;
    private final org.pushingpixels.trident.Timeline TIMELINE = new org.pushingpixels.trident.Timeline(this);
    private final org.pushingpixels.trident.callback.TimelineCallback TIMELINE_CALLBACK = new org.pushingpixels.trident.callback.TimelineCallback()
    {
        @Override
        public void onTimelineStateChanged(org.pushingpixels.trident.Timeline.TimelineState oldState, org.pushingpixels.trident.Timeline.TimelineState newState, float oldValue, float newValue)
        {
            if (newState == org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                effectImage = effectImageStandard;
                if (!mousePressed && !mouseOver)
                {
                    mouseMovedOutside = false;
                }
            }
        }

        @Override
        public void onTimelinePulse(float oldValue, float newValue)
        {
        }
    };
    private static final org.pushingpixels.trident.ease.TimelineEase STANDARD_EASING = new org.pushingpixels.trident.ease.Spline(0.3f);    
    private static final float[] EFFECT_FRACTIONS =
    {
        0.0f,
        0.15f,
        1.0f
    };
    private java.awt.Color effectColor = new java.awt.Color(255, 50, 0, 200);
    private java.awt.Color[] effectColors =
    {
        effectColor,
        effectColor,
        new java.awt.Color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0)
    };
    private java.awt.Color effectColorPressed = new java.awt.Color(50, 161, 0, 200);
    private java.awt.Color[] effectColorsPressed =
    {
        effectColorPressed,
        effectColorPressed,
        new java.awt.Color(effectColorPressed.getRed(), effectColorPressed.getGreen(), effectColorPressed.getBlue(), 0)
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public MButton()
    {
        this("MButton");
    }

    public MButton(final String text)
    {
        super(text);
        _init();
    }

    public MButton(javax.swing.Icon icon)
    {
        super(icon);
        _init();
    }
    
    public MButton(javax.swing.Action action)
    {
        super(action);
        _init();
    }
    
    private void _init() {
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFont(new java.awt.Font("Verdana", 0, 11));
        setForeground(new java.awt.Color(0xCCCCCC));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setPreferredSize(new java.awt.Dimension(73, 23));
        setSize(getPreferredSize());
        TIMELINE.setEase(STANDARD_EASING);
        TIMELINE.addCallback(TIMELINE_CALLBACK);
        init(getWidth(), getHeight());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT)
    {
        effectImageStandard = create_EFFECT_Image(WIDTH, HEIGHT, false);
        effectImageClicked = create_EFFECT_Image(WIDTH, HEIGHT, true);
        effectImage = effectImageStandard;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Painting method">
    @Override
    protected void paintComponent(final java.awt.Graphics G)
    {
        super.paintComponent(G);

        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) G.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        G2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        G2.drawImage(effectImage, 0, 0, null);
        G2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));

        G2.dispose();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public float getAlpha()
    {
        return this.alpha;
    }

    public void setAlpha(final float ALPHA)
    {
        this.alpha = ALPHA;
        repaint();
    }

    public java.awt.Color getEffectColor()
    {
        return this.effectColor;
    }

    public void setEffectColor(final java.awt.Color EFFECT_COLOR)
    {
        this.effectColor = EFFECT_COLOR;
        this.effectColors = new java.awt.Color[]
        {
            effectColor,
            effectColor,
            new java.awt.Color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0)
        };
        repaint();
    }

    public java.awt.Color getEffectColorPressed()
    {
        return this.effectColorPressed;
    }

    public void setEffectColorPressed(final java.awt.Color EFFECT_COLOR_PRESSED)
    {
        this.effectColorPressed = EFFECT_COLOR_PRESSED;
        effectColorsPressed = new java.awt.Color[]
        {
            effectColorPressed,
            effectColorPressed,
            new java.awt.Color(effectColorPressed.getRed(), effectColorPressed.getGreen(), effectColorPressed.getBlue(), 0)
        };
        repaint();
    }

    @Override
    public void setUI(final javax.swing.plaf.ButtonUI UI)
    {
        super.setUI(new MButtonUI(this));
    }

    @Override
    protected void setUI(final javax.swing.plaf.ComponentUI UI)
    {
        super.setUI(new MButtonUI(this));
    }

    public void setUi(final javax.swing.plaf.ComponentUI UI)
    {
        this.ui = new MButtonUI(this);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Image creation methods">
    private java.awt.image.BufferedImage create_EFFECT_Image(final int WIDTH, final int HEIGHT, final boolean CLICKED)
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

        final java.awt.Rectangle BOUNDS = new java.awt.Rectangle(0, (int) (IMAGE_HEIGHT * 0.6 / 2), IMAGE_WIDTH, (int) (IMAGE_HEIGHT * 1.4));
        final java.awt.RadialGradientPaint GRADIENT;

        if (CLICKED)
        {
            GRADIENT = new java.awt.RadialGradientPaint(BOUNDS, EFFECT_FRACTIONS, effectColorsPressed, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }
        else
        {
            GRADIENT = new java.awt.RadialGradientPaint(BOUNDS, EFFECT_FRACTIONS, effectColors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }

        G2.setPaint(GRADIENT);
        G2.fill(BOUNDS);

        G2.dispose();

        return IMAGE;
    }   
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ComponentListener methods">
    @Override
    public void componentResized(final java.awt.event.ComponentEvent EVENT)
    {
        setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
        setSize(getPreferredSize());
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
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MouseEventListener methods">
    @Override
    public void mouseClicked(final java.awt.event.MouseEvent EVENT)
    {
        mouseClicked = true;
        effectImage = effectImageClicked;
        if (isEnabled())
        {
            // Fades out the pressed effect image slowly (600 ms)            
            if (TIMELINE.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                TIMELINE.abort();
            }
            TIMELINE.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
            TIMELINE.setDuration(600);
            TIMELINE.playReverse();
        }
    }

    @Override
    public void mousePressed(final java.awt.event.MouseEvent EVENT)
    {
        mouseClicked = true;
        mousePressed = true;
        mouseMovedOutside = false;
        if (TIMELINE.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
        {
            TIMELINE.abort();
        }
        alpha = 1.0f;
        effectImage = effectImageClicked;
        repaint();
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent EVENT)
    {
        if (isEnabled())
        {
            // Fade out the pressed effect image slowly (600 ms)            
            if (TIMELINE.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                TIMELINE.abort();
            }
            TIMELINE.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
            TIMELINE.setDuration(600);
            TIMELINE.playReverse();
        }
        repaint();
        mousePressed = false;
    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent EVENT)
    {
        mouseOver = true;
        if (isEnabled() && alpha < 1.0f)
        {
            // Fade in the effect slowly (600 ms) if the mouse entered the button            
            if (TIMELINE.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE)
            {
                TIMELINE.abort();
            }            
            if (!mousePressed)
            {
                TIMELINE.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
                TIMELINE.setDuration(600);
                TIMELINE.play();
            }
        }
    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent EVENT)
    {
        mouseOver = false;
        if (isEnabled())
        {
            // Abort running timeline if it's not idle and button was not clicked
            if (TIMELINE.getState() != org.pushingpixels.trident.Timeline.TimelineState.IDLE && !mouseClicked)
            {
                TIMELINE.abort();     
            }

            /* If button was not clicked fade out the effect slowly (600 ms)
               Because it will always fade from 1.0f - 0.0f it looks like
               the effect will be fade in completely before it fades out slowly.
               You could see it if you simply move the mouse over a button and
               directly out again. In this case it will set the alpha value
               of the effect image to 1.0f (opaque) and fades out slowly.
            */
            if (!mouseClicked && !mousePressed && !mouseMovedOutside)
            {                
                TIMELINE.addPropertyToInterpolate("alpha", 0.0f, 1.0f);
                TIMELINE.setDuration(600);
                TIMELINE.playReverse();                
            }
            mouseClicked = false;
            if (mousePressed)
            {
                mouseMovedOutside = true;
            }
            else
            {
                mouseMovedOutside = false;
            }
        }        
    }

    @Override
    public void mouseDragged(final java.awt.event.MouseEvent EVENT)
    {

    }

    @Override
    public void mouseMoved(final java.awt.event.MouseEvent EVENT)
    {
    }
    //</editor-fold>
   
    @Override
    public String toString()
    {
        return "MButton";
    }
}
