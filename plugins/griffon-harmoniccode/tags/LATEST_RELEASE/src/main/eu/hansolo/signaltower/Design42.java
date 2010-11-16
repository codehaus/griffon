package eu.hansolo.signaltower;


/**
 *
 * @author hansolo
 */
public class Design42 extends javax.swing.JComponent implements java.awt.event.ComponentListener
{
    private boolean redOn = false;
    private boolean yellowOn = false;
    private boolean greenOn = false;
    
    final float[] FRACTIONS =
    {
        0.0f,
        0.03f,
        0.05f,
        0.07f,
        0.09f,
        0.12f,
        0.14f,
        0.16f,
        0.19f,
        0.21f,
        0.23f,
        0.26f,
        0.28f,
        0.3f,
        0.33f,
        0.36f,
        0.38f,
        0.41f,
        0.44f,
        0.46f,
        0.48f,
        0.51f,
        0.54f,
        0.56f,
        0.59f,
        0.61f,
        0.63f,
        0.66f,
        0.68f,
        0.71f,
        0.73f,
        0.75f,
        0.78f,
        0.8f,
        0.83f,
        0.85f,
        0.88f,
        0.9f,
        0.92f,
        0.95f,
        0.97f,
        0.99f,
        1.0f
    };

    final java.awt.Color[] COLORS_OFF =
    {
        new java.awt.Color(233, 233, 233, 178),
        new java.awt.Color(186, 186, 186, 178),
        new java.awt.Color(233, 231, 232, 178),
        new java.awt.Color(174, 170, 171, 178),
        new java.awt.Color(208, 204, 205, 178),
        new java.awt.Color(144, 140, 141, 178),
        new java.awt.Color(237, 233, 234, 178),
        new java.awt.Color(124, 119, 123, 178),
        new java.awt.Color(255, 254, 255, 178),
        new java.awt.Color(172, 168, 165, 178),
        new java.awt.Color(255, 253, 252, 178),
        new java.awt.Color(198, 194, 193, 178),
        new java.awt.Color(254, 252, 253, 178),
        new java.awt.Color(213, 209, 206, 178),
        new java.awt.Color(147, 143, 140, 178),
        new java.awt.Color(184, 180, 177, 178),
        new java.awt.Color(122, 118, 115, 178),
        new java.awt.Color(194, 190, 189, 178),
        new java.awt.Color(147, 143, 140, 178),
        new java.awt.Color(215, 211, 210, 178),
        new java.awt.Color(120, 116, 117, 178),
        new java.awt.Color(199, 195, 196, 178),
        new java.awt.Color(68, 64, 65, 178),
        new java.awt.Color(213, 209, 210, 178),
        new java.awt.Color(178, 174, 175, 178),
        new java.awt.Color(215, 211, 212, 178),
        new java.awt.Color(214, 210, 211, 178),
        new java.awt.Color(153, 149, 150, 178),
        new java.awt.Color(212, 208, 209, 178),
        new java.awt.Color(94, 90, 91, 178),
        new java.awt.Color(208, 206, 207, 178),
        new java.awt.Color(145, 141, 140, 178),
        new java.awt.Color(227, 223, 220, 178),
        new java.awt.Color(136, 132, 129, 178),
        new java.awt.Color(186, 182, 181, 178),
        new java.awt.Color(141, 139, 140, 178),
        new java.awt.Color(187, 185, 186, 178),
        new java.awt.Color(119, 114, 118, 178),
        new java.awt.Color(242, 238, 239, 178),
        new java.awt.Color(143, 139, 140, 178),
        new java.awt.Color(194, 190, 191, 178),
        new java.awt.Color(172, 171, 169, 178),
        new java.awt.Color(172, 171, 169, 178)
    };

    final java.awt.Color[] COLORS_ON =
    {
        new java.awt.Color(233, 233, 233, 78),
        new java.awt.Color(186, 186, 186, 78),
        new java.awt.Color(233, 231, 232, 78),
        new java.awt.Color(174, 170, 171, 78),
        new java.awt.Color(208, 204, 205, 78),
        new java.awt.Color(144, 140, 141, 78),
        new java.awt.Color(237, 233, 234, 78),
        new java.awt.Color(124, 119, 123, 78),
        new java.awt.Color(255, 254, 255, 78),
        new java.awt.Color(172, 168, 165, 78),
        new java.awt.Color(255, 253, 252, 78),
        new java.awt.Color(198, 194, 193, 78),
        new java.awt.Color(254, 252, 253, 78),
        new java.awt.Color(213, 209, 206, 78),
        new java.awt.Color(147, 143, 140, 78),
        new java.awt.Color(184, 180, 177, 78),
        new java.awt.Color(122, 118, 115, 78),
        new java.awt.Color(194, 190, 189, 78),
        new java.awt.Color(147, 143, 140, 78),
        new java.awt.Color(215, 211, 210, 78),
        new java.awt.Color(120, 116, 117, 78),
        new java.awt.Color(199, 195, 196, 78),
        new java.awt.Color(68, 64, 65, 78),
        new java.awt.Color(213, 209, 210, 78),
        new java.awt.Color(178, 174, 175, 78),
        new java.awt.Color(215, 211, 212, 78),
        new java.awt.Color(214, 210, 211, 78),
        new java.awt.Color(153, 149, 150, 78),
        new java.awt.Color(212, 208, 209, 78),
        new java.awt.Color(94, 90, 91, 78),
        new java.awt.Color(208, 206, 207, 78),
        new java.awt.Color(145, 141, 140, 78),
        new java.awt.Color(227, 223, 220, 78),
        new java.awt.Color(136, 132, 129, 78),
        new java.awt.Color(186, 182, 181, 78),
        new java.awt.Color(141, 139, 140, 78),
        new java.awt.Color(187, 185, 186, 78),
        new java.awt.Color(119, 114, 118, 78),
        new java.awt.Color(242, 238, 239, 78),
        new java.awt.Color(143, 139, 140, 78),
        new java.awt.Color(194, 190, 191, 78),
        new java.awt.Color(172, 171, 169, 78),
        new java.awt.Color(172, 171, 169, 78)
    };

    private java.awt.image.BufferedImage columnImage = create_COLUMN_Image(100);
    private java.awt.image.BufferedImage redImageOn = create_RED_ON_Image(100);
    private java.awt.image.BufferedImage yellowImageOn = create_YELLOW_ON_Image(100);
    private java.awt.image.BufferedImage greenImageOn = create_GREEN_ON_Image(100);
    private java.awt.image.BufferedImage redImageOff = create_RED_OFF_Image(100);
    private java.awt.image.BufferedImage yellowImageOff = create_YELLOW_OFF_Image(100);
    private java.awt.image.BufferedImage greenImageOff = create_GREEN_OFF_Image(100);

    
    public Design42()
    {
        super();
        addComponentListener(this);
        setPreferredSize(getSize(new java.awt.Dimension((int) (300 * 0.3787878788), 300)));
        setSize(getPreferredSize());
        init(getWidth(), getHeight());        
    }

    private void init(final int WIDTH, final int HEIGHT)
    {
        columnImage = create_COLUMN_Image(WIDTH);
        redImageOn = create_RED_ON_Image(WIDTH);
        yellowImageOn = create_YELLOW_ON_Image(WIDTH);
        greenImageOn = create_GREEN_ON_Image(WIDTH);
        redImageOff = create_RED_OFF_Image(WIDTH);
        yellowImageOff = create_YELLOW_OFF_Image(WIDTH);
        greenImageOff = create_GREEN_OFF_Image(WIDTH);
    }

    @Override
    protected void paintComponent(final java.awt.Graphics G)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) G.create();

        if (redOn)
        {
            G2.drawImage(redImageOn, 0, 0, null);
        }
        else
        {
            G2.drawImage(redImageOff, 0, 0, null);
        }

        if (yellowOn)
        {
            G2.drawImage(yellowImageOn, 0, 0, null);
        }
        else
        {
            G2.drawImage(yellowImageOff, 0, 0, null);
        }

        if (greenOn)
        {
            G2.drawImage(greenImageOn, 0, 0, null);
        }
        else
        {
            G2.drawImage(greenImageOff, 0, 0, null);
        }

        G2.drawImage(columnImage, 0, 0, null);

        G2.dispose();
    }

    public boolean isRedOn()
    {
        return this.redOn;
    }

    public void setRedOn(final boolean RED_ON)
    {
        this.redOn = RED_ON;
        repaint();
    }

    public boolean isYellowOn()
    {
        return this.yellowOn;
    }

    public void setYellowOn(final boolean YELLOW_ON)
    {
        this.yellowOn = YELLOW_ON;
        repaint();
    }

    public boolean isGreenOn()
    {
        return this.greenOn;
    }

    public void setGreenOn(final boolean GREEN_ON)
    {
        this.greenOn = GREEN_ON;
        repaint();
    }

    private java.awt.image.BufferedImage create_RED_ON_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath RED_LIGHT = new java.awt.geom.GeneralPath();
        RED_LIGHT.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        RED_LIGHT.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_LIGHT.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.2967171717171717);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.2916666666666667, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11868686868686869);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.12247474747474747);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11742424242424243);
        RED_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11868686868686869, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_LIGHT.closePath();
        final java.awt.geom.Point2D RED_LIGHT_START = new java.awt.geom.Point2D.Double(RED_LIGHT.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D RED_LIGHT_STOP = new java.awt.geom.Point2D.Double(RED_LIGHT.getBounds2D().getMaxX(), 0);
        final float[] RED_LIGHT_FRACTIONS =
        {
            0.0f,
            0.28f,
            0.45f,
            0.6f,
            0.75f,
            1.0f
        };
        final java.awt.Color[] RED_LIGHT_COLORS =
        {
            new java.awt.Color(153, 0, 0, 255),
            new java.awt.Color(204, 0, 0, 255),
            new java.awt.Color(102, 0, 0, 255),
            new java.awt.Color(255, 0, 0, 255),
            new java.awt.Color(204, 0, 51, 255),
            new java.awt.Color(102, 0, 0, 255)
        };
        final java.awt.LinearGradientPaint RED_LIGHT_GRADIENT = new java.awt.LinearGradientPaint(RED_LIGHT_START, RED_LIGHT_STOP, RED_LIGHT_FRACTIONS, RED_LIGHT_COLORS);
        G2.setPaint(RED_LIGHT_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_RED_LIGHT;
        CLIP_IMAGE_RED_LIGHT = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) RED_LIGHT, (java.awt.Paint) RED_LIGHT_GRADIENT, null);
        G2.translate((int) -30.0, (int)-30.0);
        G2.drawImage(Shadow.INSTANCE.createDropShadow(CLIP_IMAGE_RED_LIGHT, (int) (0.0 * IMAGE_WIDTH), 1.0f, (int) 30.0, (int)315.0, new java.awt.Color(0xFF0000)), RED_LIGHT.getBounds().x, RED_LIGHT.getBounds().y, null);
        G2.translate((int) 30.0, (int)30.0);

        final java.awt.geom.GeneralPath RED_ON = new java.awt.geom.GeneralPath();
        RED_ON.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        RED_ON.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_ON.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        RED_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293);
        RED_ON.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.2967171717171717);
        RED_ON.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293);
        RED_ON.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.2916666666666667, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536);
        RED_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555);
        RED_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11868686868686869);
        RED_ON.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.12247474747474747);
        RED_ON.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11742424242424243);
        RED_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11868686868686869, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_ON.closePath();
        final java.awt.geom.Point2D RED_ON_START = new java.awt.geom.Point2D.Double(RED_ON.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D RED_ON_STOP = new java.awt.geom.Point2D.Double(RED_ON.getBounds2D().getMaxX(), 0);        
        final java.awt.LinearGradientPaint RED_ON_GRADIENT = new java.awt.LinearGradientPaint(RED_ON_START, RED_ON_STOP, FRACTIONS, COLORS_ON);
        G2.setPaint(RED_ON_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_RED_ON;
        CLIP_IMAGE_RED_ON = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) RED_ON, (java.awt.Paint) RED_ON_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_RED_ON, (java.awt.Shape) RED_ON, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), RED_ON.getBounds().x, RED_ON.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_YELLOW_ON_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath YELLOW_LIGHT = new java.awt.geom.GeneralPath();
        YELLOW_LIGHT.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        YELLOW_LIGHT.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_LIGHT.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.4936868686868687);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.48863636363636365, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3194444444444444);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.31691919191919193, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_LIGHT.closePath();
        final java.awt.geom.Point2D YELLOW_LIGHT_START = new java.awt.geom.Point2D.Double(YELLOW_LIGHT.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D YELLOW_LIGHT_STOP = new java.awt.geom.Point2D.Double(YELLOW_LIGHT.getBounds2D().getMaxX(), 0);
        final float[] YELLOW_LIGHT_FRACTIONS =
        {
            0.0f,
            0.28f,
            0.45f,
            0.6f,
            0.75f,
            1.0f
        };
        final java.awt.Color[] YELLOW_LIGHT_COLORS =
        {
            new java.awt.Color(153, 102, 51, 255),
            new java.awt.Color(255, 204, 0, 255),
            new java.awt.Color(153, 153, 0, 255),
            new java.awt.Color(255, 255, 0, 255),
            new java.awt.Color(255, 204, 0, 255),
            new java.awt.Color(153, 153, 0, 255)
        };
        final java.awt.LinearGradientPaint YELLOW_LIGHT_GRADIENT = new java.awt.LinearGradientPaint(YELLOW_LIGHT_START, YELLOW_LIGHT_STOP, YELLOW_LIGHT_FRACTIONS, YELLOW_LIGHT_COLORS);
        G2.setPaint(YELLOW_LIGHT_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_YELLOW_LIGHT;
        CLIP_IMAGE_YELLOW_LIGHT = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) YELLOW_LIGHT, (java.awt.Paint) YELLOW_LIGHT_GRADIENT, null);
        G2.translate((int) -30.0, (int)-30.0);
        G2.drawImage(Shadow.INSTANCE.createDropShadow(CLIP_IMAGE_YELLOW_LIGHT, (int) (0.0 * IMAGE_WIDTH), 1.0f, (int) 30.0, (int)315.0, new java.awt.Color(0xFFFF00)), YELLOW_LIGHT.getBounds().x, YELLOW_LIGHT.getBounds().y, null);
        G2.translate((int) 30.0, (int)30.0);

        final java.awt.geom.GeneralPath YELLOW_ON = new java.awt.geom.GeneralPath();
        YELLOW_ON.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        YELLOW_ON.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_ON.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.4936868686868687);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.48863636363636365, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3194444444444444);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.31691919191919193, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_ON.closePath();
        final java.awt.geom.Point2D YELLOW_ON_START = new java.awt.geom.Point2D.Double(YELLOW_ON.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D YELLOW_ON_STOP = new java.awt.geom.Point2D.Double(YELLOW_ON.getBounds2D().getMaxX(), 0);        
        final java.awt.LinearGradientPaint YELLOW_ON_GRADIENT = new java.awt.LinearGradientPaint(YELLOW_ON_START, YELLOW_ON_STOP, FRACTIONS, COLORS_ON);
        G2.setPaint(YELLOW_ON_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_YELLOW_ON;
        CLIP_IMAGE_YELLOW_ON = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) YELLOW_ON, (java.awt.Paint) YELLOW_ON_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_YELLOW_ON, (java.awt.Shape) YELLOW_ON, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), YELLOW_ON.getBounds().x, YELLOW_ON.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_GREEN_ON_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath GREEN_LIGHT = new java.awt.geom.GeneralPath();
        GREEN_LIGHT.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        GREEN_LIGHT.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_LIGHT.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.6906565656565656);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.6856060606060606, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5164141414141414);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.5138888888888888, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_LIGHT.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_LIGHT.closePath();
        final java.awt.geom.Point2D GREEN_LIGHT_START = new java.awt.geom.Point2D.Double(GREEN_LIGHT.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D GREEN_LIGHT_STOP = new java.awt.geom.Point2D.Double(GREEN_LIGHT.getBounds2D().getMaxX(), 0);
        final float[] GREEN_LIGHT_FRACTIONS =
        {
            0.0f,
            0.28f,
            0.45f,
            0.6f,
            0.75f,
            1.0f
        };
        final java.awt.Color[] GREEN_LIGHT_COLORS =
        {
            new java.awt.Color(48, 187, 24, 255),
            new java.awt.Color(158, 245, 78, 255),
            new java.awt.Color(68, 183, 0, 255),
            new java.awt.Color(166, 244, 143, 255),
            new java.awt.Color(41, 186, 7, 255),
            new java.awt.Color(98, 252, 94, 255)
        };
        final java.awt.LinearGradientPaint GREEN_LIGHT_GRADIENT = new java.awt.LinearGradientPaint(GREEN_LIGHT_START, GREEN_LIGHT_STOP, GREEN_LIGHT_FRACTIONS, GREEN_LIGHT_COLORS);
        G2.setPaint(GREEN_LIGHT_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_GREEN_LIGHT;
        CLIP_IMAGE_GREEN_LIGHT = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) GREEN_LIGHT, (java.awt.Paint) GREEN_LIGHT_GRADIENT, null);
        G2.translate((int) -30.0, (int)-30.0);
        G2.drawImage(Shadow.INSTANCE.createDropShadow(CLIP_IMAGE_GREEN_LIGHT, (int) (0.023333333333333334 * IMAGE_WIDTH), 1.0f, (int) 30.0, (int)315.0, new java.awt.Color(0x00FF00)), GREEN_LIGHT.getBounds().x, GREEN_LIGHT.getBounds().y, null);
        G2.translate((int) 30.0, (int)30.0);

        final java.awt.geom.GeneralPath GREEN_ON = new java.awt.geom.GeneralPath();
        GREEN_ON.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        GREEN_ON.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_ON.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.6906565656565656);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.6856060606060606, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5164141414141414);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.5138888888888888, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_ON.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_ON.closePath();
        final java.awt.geom.Point2D GREEN_ON_START = new java.awt.geom.Point2D.Double(GREEN_ON.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D GREEN_ON_STOP = new java.awt.geom.Point2D.Double(GREEN_ON.getBounds2D().getMaxX(), 0);        
        final java.awt.LinearGradientPaint GREEN_ON_GRADIENT = new java.awt.LinearGradientPaint(GREEN_ON_START, GREEN_ON_STOP, FRACTIONS, COLORS_ON);
        G2.setPaint(GREEN_ON_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_GREEN_ON;
        CLIP_IMAGE_GREEN_ON = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) GREEN_ON, (java.awt.Paint) GREEN_ON_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_GREEN_ON, (java.awt.Shape) GREEN_ON, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), GREEN_ON.getBounds().x, GREEN_ON.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_RED_OFF_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath RED_OFF = new java.awt.geom.GeneralPath();
        RED_OFF.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        RED_OFF.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_OFF.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.2967171717171717);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.29292929292929293);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.2916666666666667, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.11868686868686869);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.12247474747474747);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.12247474747474747, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.11994949494949494, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11742424242424243);
        RED_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.11868686868686869, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.18055555555555555);
        RED_OFF.closePath();
        final java.awt.geom.Point2D RED_OFF_START = new java.awt.geom.Point2D.Double(RED_OFF.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D RED_OFF_STOP = new java.awt.geom.Point2D.Double(RED_OFF.getBounds2D().getMaxX(), 0);
        final java.awt.LinearGradientPaint RED_OFF_GRADIENT = new java.awt.LinearGradientPaint(RED_OFF_START, RED_OFF_STOP, FRACTIONS, COLORS_OFF);
        G2.setPaint(RED_OFF_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_RED_ON;
        CLIP_IMAGE_RED_ON = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) RED_OFF, (java.awt.Paint) RED_OFF_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_RED_ON, (java.awt.Shape) RED_OFF, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), RED_OFF.getBounds().x, RED_OFF.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_YELLOW_OFF_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath YELLOW_OFF = new java.awt.geom.GeneralPath();
        YELLOW_OFF.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        YELLOW_OFF.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_OFF.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.4936868686868687);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.4898989898989899);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.48863636363636365, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.3194444444444444);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.3194444444444444, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.31691919191919193, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.3143939393939394);
        YELLOW_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.31565656565656564, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.37752525252525254);
        YELLOW_OFF.closePath();
        final java.awt.geom.Point2D YELLOW_OFF_START = new java.awt.geom.Point2D.Double(YELLOW_OFF.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D YELLOW_OFF_STOP = new java.awt.geom.Point2D.Double(YELLOW_OFF.getBounds2D().getMaxX(), 0);
        final java.awt.LinearGradientPaint YELLOW_OFF_GRADIENT = new java.awt.LinearGradientPaint(YELLOW_OFF_START, YELLOW_OFF_STOP, FRACTIONS, COLORS_OFF);
        G2.setPaint(YELLOW_OFF_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_YELLOW_OFF;
        CLIP_IMAGE_YELLOW_OFF = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) YELLOW_OFF, (java.awt.Paint) YELLOW_OFF_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_YELLOW_OFF, (java.awt.Shape) YELLOW_OFF, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), YELLOW_OFF.getBounds().x, YELLOW_OFF.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_GREEN_OFF_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath GREEN_OFF = new java.awt.geom.GeneralPath();
        GREEN_OFF.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        GREEN_OFF.moveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_OFF.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.21333333333333335, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.6906565656565656);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.6868686868686869);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.78, IMAGE_HEIGHT * 0.6856060606060606, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.7666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.5164141414141414);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.5164141414141414, IMAGE_WIDTH * 0.23, IMAGE_HEIGHT * 0.5138888888888888, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5113636363636364);
        GREEN_OFF.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5126262626262627, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.5744949494949495);
        GREEN_OFF.closePath();
        final java.awt.geom.Point2D GREEN_OFF_START = new java.awt.geom.Point2D.Double(GREEN_OFF.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D GREEN_OFF_STOP = new java.awt.geom.Point2D.Double(GREEN_OFF.getBounds2D().getMaxX(), 0);
        final java.awt.LinearGradientPaint GREEN_OFF_GRADIENT = new java.awt.LinearGradientPaint(GREEN_OFF_START, GREEN_OFF_STOP, FRACTIONS, COLORS_OFF);
        G2.setPaint(GREEN_OFF_GRADIENT);
        final java.awt.image.BufferedImage CLIP_IMAGE_GREEN_OFF;
        CLIP_IMAGE_GREEN_OFF = Shadow.INSTANCE.createSoftClipImage((java.awt.Shape) GREEN_OFF, (java.awt.Paint) GREEN_OFF_GRADIENT, null);
        G2.drawImage(Shadow.INSTANCE.createInnerShadow(CLIP_IMAGE_GREEN_OFF, (java.awt.Shape) GREEN_OFF, (int) (0.0 * IMAGE_WIDTH), 0.65f, new java.awt.Color(0x000000), (int) 4.0, (int) 315.0), GREEN_OFF.getBounds().x, GREEN_OFF.getBounds().y, null);

        G2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage create_COLUMN_Image(final int WIDTH)
    {
        if (WIDTH <= 0)
        {
            return null;
        }

        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, (int) (2.64 * WIDTH), java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();

        final java.awt.geom.GeneralPath BASE = new java.awt.geom.GeneralPath();
        BASE.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        BASE.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6868686868686869);
        BASE.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.9810606060606061);
        BASE.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.9810606060606061, IMAGE_WIDTH * 0.16666666666666666, IMAGE_HEIGHT * 0.9911616161616161, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.9949494949494949);
        BASE.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.9949494949494949, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 1.0, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 1.0);
        BASE.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 1.0, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.9936868686868687, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.9936868686868687);
        BASE.curveTo(IMAGE_WIDTH * 0.8333333333333334, IMAGE_HEIGHT * 0.9886363636363636, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.9810606060606061, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.9810606060606061);
        BASE.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.9810606060606061, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6868686868686869);
        BASE.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6818181818181818, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6805555555555556);
        BASE.curveTo(IMAGE_WIDTH * 0.8066666666666666, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.6906565656565656);
        BASE.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.6906565656565656, IMAGE_WIDTH * 0.19, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6805555555555556);
        BASE.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6818181818181818, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6868686868686869);
        BASE.closePath();
        final java.awt.geom.Point2D BASE_START = new java.awt.geom.Point2D.Double(BASE.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D BASE_STOP = new java.awt.geom.Point2D.Double(BASE.getBounds2D().getMaxX(), 0);
        final float[] BASE_FRACTIONS =
        {
            0.0f,
            0.06f,
            0.08f,
            0.12f,
            0.16f,
            0.18f,
            0.23f,
            0.28f,
            0.34f,
            0.41f,
            0.45f,
            0.52f,
            0.55f,
            0.59f,
            0.69f,
            0.71f,
            0.93f,
            0.95f,
            1.0f
        };
        final java.awt.Color[] BASE_COLORS =
        {
            new java.awt.Color(112, 112, 114, 255),
            new java.awt.Color(60, 60, 70, 255),
            new java.awt.Color(34, 37, 46, 255),
            new java.awt.Color(41, 41, 49, 255),
            new java.awt.Color(166, 160, 162, 255),
            new java.awt.Color(179, 175, 176, 255),
            new java.awt.Color(200, 198, 201, 255),
            new java.awt.Color(246, 246, 246, 255),
            new java.awt.Color(220, 219, 224, 255),
            new java.awt.Color(46, 48, 71, 255),
            new java.awt.Color(4, 4, 6, 255),
            new java.awt.Color(56, 57, 75, 255),
            new java.awt.Color(95, 96, 101, 255),
            new java.awt.Color(141, 138, 145, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(54, 53, 59, 255),
            new java.awt.Color(22, 21, 29, 255),
            new java.awt.Color(25, 23, 26, 255)
        };
        final java.awt.LinearGradientPaint BASE_GRADIENT = new java.awt.LinearGradientPaint(BASE_START, BASE_STOP, BASE_FRACTIONS, BASE_COLORS);
        G2.setPaint(BASE_GRADIENT);
        G2.fill(BASE);
        final java.awt.Color STROKE_COLOR_BASE = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_BASE);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(BASE);

        final java.awt.geom.GeneralPath BASE_TOP = new java.awt.geom.GeneralPath();
        BASE_TOP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        BASE_TOP.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6755050505050505, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.672979797979798, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.672979797979798);
        BASE_TOP.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6755050505050505);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6755050505050505, IMAGE_WIDTH * 0.21, IMAGE_HEIGHT * 0.678030303030303, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.22333333333333333, IMAGE_HEIGHT * 0.6805555555555556, IMAGE_WIDTH * 0.29, IMAGE_HEIGHT * 0.6856060606060606, IMAGE_WIDTH * 0.47, IMAGE_HEIGHT * 0.6856060606060606);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6843434343434344, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6805555555555556, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6805555555555556);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.79, IMAGE_HEIGHT * 0.678030303030303, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6767676767676768, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.6767676767676768);
        BASE_TOP.lineTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.672979797979798);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.672979797979798, IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.6742424242424242, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6792929292929293);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.6792929292929293, IMAGE_WIDTH * 0.84, IMAGE_HEIGHT * 0.6868686868686869, IMAGE_WIDTH * 0.49666666666666665, IMAGE_HEIGHT * 0.6906565656565656);
        BASE_TOP.curveTo(IMAGE_WIDTH * 0.20333333333333334, IMAGE_HEIGHT * 0.6881313131313131, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6805555555555556, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.6792929292929293);
        BASE_TOP.closePath();
        final java.awt.geom.Point2D BASE_TOP_START = new java.awt.geom.Point2D.Double(BASE_TOP.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D BASE_TOP_STOP = new java.awt.geom.Point2D.Double(BASE_TOP.getBounds2D().getMaxX(), 0);
        final float[] BASE_TOP_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.05f,
            0.07f,
            0.13f,
            0.16f,
            0.21f,
            0.29f,
            0.39f,
            0.44f,
            0.5f,
            0.55f,
            0.6f,
            0.65f,
            0.71f,
            0.76f,
            0.8f,
            0.83f,
            0.86f,
            0.89f,
            0.94f,
            1.0f
        };
        final java.awt.Color[] BASE_TOP_COLORS =
        {
            new java.awt.Color(110, 108, 109, 255),
            new java.awt.Color(110, 106, 107, 255),
            new java.awt.Color(4, 0, 0, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(229, 223, 227, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(115, 111, 110, 255),
            new java.awt.Color(205, 206, 208, 255),
            new java.awt.Color(121, 122, 124, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(112, 110, 113, 255),
            new java.awt.Color(197, 193, 194, 255),
            new java.awt.Color(124, 120, 119, 255),
            new java.awt.Color(203, 200, 195, 255),
            new java.awt.Color(111, 107, 106, 255),
            new java.awt.Color(124, 120, 121, 255),
            new java.awt.Color(189, 185, 186, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(115, 111, 112, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(93, 91, 92, 255)
        };
        final java.awt.LinearGradientPaint BASE_TOP_GRADIENT = new java.awt.LinearGradientPaint(BASE_TOP_START, BASE_TOP_STOP, BASE_TOP_FRACTIONS, BASE_TOP_COLORS);
        G2.setPaint(BASE_TOP_GRADIENT);
        G2.fill(BASE_TOP);
        final java.awt.Color STROKE_COLOR_BASE_TOP = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_BASE_TOP);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(BASE_TOP);

        final java.awt.geom.GeneralPath SEP2 = new java.awt.geom.GeneralPath();
        SEP2.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        SEP2.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.4898989898989899);
        SEP2.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.5063131313131313);
        SEP2.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.5063131313131313, IMAGE_WIDTH * 0.16666666666666666, IMAGE_HEIGHT * 0.51010101010101, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.5138888888888888);
        SEP2.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.5138888888888888, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.5176767676767676, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.5176767676767676);
        SEP2.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.5176767676767676, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.5138888888888888, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.5138888888888888);
        SEP2.curveTo(IMAGE_WIDTH * 0.85, IMAGE_HEIGHT * 0.51010101010101, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.5063131313131313, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.5063131313131313);
        SEP2.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.5063131313131313, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.4898989898989899);
        SEP2.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.48484848484848486, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.48358585858585856);
        SEP2.curveTo(IMAGE_WIDTH * 0.8066666666666666, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.4936868686868687);
        SEP2.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.4936868686868687, IMAGE_WIDTH * 0.19, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.48358585858585856);
        SEP2.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.48484848484848486, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.4898989898989899);
        SEP2.closePath();
        final java.awt.geom.Point2D SEP2_START = new java.awt.geom.Point2D.Double(SEP2.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D SEP2_STOP = new java.awt.geom.Point2D.Double(SEP2.getBounds2D().getMaxX(), 0);
        final float[] SEP2_FRACTIONS =
        {
            0.0f,
            0.06f,
            0.08f,
            0.12f,
            0.16f,
            0.18f,
            0.23f,
            0.28f,
            0.34f,
            0.41f,
            0.45f,
            0.52f,
            0.55f,
            0.59f,
            0.69f,
            0.71f,
            0.93f,
            0.95f,
            1.0f
        };
        final java.awt.Color[] SEP2_COLORS =
        {
            new java.awt.Color(112, 112, 114, 255),
            new java.awt.Color(60, 60, 70, 255),
            new java.awt.Color(34, 37, 46, 255),
            new java.awt.Color(41, 41, 49, 255),
            new java.awt.Color(166, 160, 162, 255),
            new java.awt.Color(179, 175, 176, 255),
            new java.awt.Color(200, 198, 201, 255),
            new java.awt.Color(246, 246, 246, 255),
            new java.awt.Color(220, 219, 224, 255),
            new java.awt.Color(46, 48, 71, 255),
            new java.awt.Color(4, 4, 6, 255),
            new java.awt.Color(56, 57, 75, 255),
            new java.awt.Color(95, 96, 101, 255),
            new java.awt.Color(141, 138, 145, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(54, 53, 59, 255),
            new java.awt.Color(22, 21, 29, 255),
            new java.awt.Color(25, 23, 26, 255)
        };
        final java.awt.LinearGradientPaint SEP2_GRADIENT = new java.awt.LinearGradientPaint(SEP2_START, SEP2_STOP, SEP2_FRACTIONS, SEP2_COLORS);
        G2.setPaint(SEP2_GRADIENT);
        G2.fill(SEP2);
        final java.awt.Color STROKE_COLOR_SEP2 = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_SEP2);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(SEP2);

        final java.awt.geom.GeneralPath SEP2_TOP = new java.awt.geom.GeneralPath();
        SEP2_TOP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        SEP2_TOP.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.47853535353535354, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.476010101010101, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.476010101010101);
        SEP2_TOP.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.47853535353535354);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.47853535353535354, IMAGE_WIDTH * 0.21, IMAGE_HEIGHT * 0.4810606060606061, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.22333333333333333, IMAGE_HEIGHT * 0.48358585858585856, IMAGE_WIDTH * 0.29, IMAGE_HEIGHT * 0.48863636363636365, IMAGE_WIDTH * 0.47, IMAGE_HEIGHT * 0.48863636363636365);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.48737373737373735, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.48358585858585856, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.48358585858585856);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.79, IMAGE_HEIGHT * 0.4810606060606061, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4797979797979798, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.4797979797979798);
        SEP2_TOP.lineTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.476010101010101);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.476010101010101, IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.4772727272727273, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.4823232323232323);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.4823232323232323, IMAGE_WIDTH * 0.84, IMAGE_HEIGHT * 0.4898989898989899, IMAGE_WIDTH * 0.49666666666666665, IMAGE_HEIGHT * 0.4936868686868687);
        SEP2_TOP.curveTo(IMAGE_WIDTH * 0.20333333333333334, IMAGE_HEIGHT * 0.4911616161616162, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.48358585858585856, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.4823232323232323);
        SEP2_TOP.closePath();
        final java.awt.geom.Point2D SEP2_TOP_START = new java.awt.geom.Point2D.Double(SEP2_TOP.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D SEP2_TOP_STOP = new java.awt.geom.Point2D.Double(SEP2_TOP.getBounds2D().getMaxX(), 0);
        final float[] SEP2_TOP_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.05f,
            0.07f,
            0.13f,
            0.16f,
            0.21f,
            0.29f,
            0.39f,
            0.44f,
            0.5f,
            0.55f,
            0.6f,
            0.65f,
            0.71f,
            0.76f,
            0.8f,
            0.83f,
            0.86f,
            0.89f,
            0.94f,
            1.0f
        };
        final java.awt.Color[] SEP2_TOP_COLORS =
        {
            new java.awt.Color(110, 108, 109, 255),
            new java.awt.Color(110, 106, 107, 255),
            new java.awt.Color(4, 0, 0, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(229, 223, 227, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(115, 111, 110, 255),
            new java.awt.Color(205, 206, 208, 255),
            new java.awt.Color(121, 122, 124, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(112, 110, 113, 255),
            new java.awt.Color(197, 193, 194, 255),
            new java.awt.Color(124, 120, 119, 255),
            new java.awt.Color(203, 200, 195, 255),
            new java.awt.Color(111, 107, 106, 255),
            new java.awt.Color(124, 120, 121, 255),
            new java.awt.Color(189, 185, 186, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(115, 111, 112, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(93, 91, 92, 255)
        };
        final java.awt.LinearGradientPaint SEP2_TOP_GRADIENT = new java.awt.LinearGradientPaint(SEP2_TOP_START, SEP2_TOP_STOP, SEP2_TOP_FRACTIONS, SEP2_TOP_COLORS);
        G2.setPaint(SEP2_TOP_GRADIENT);
        G2.fill(SEP2_TOP);
        final java.awt.Color STROKE_COLOR_SEP2_TOP = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_SEP2_TOP);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(SEP2_TOP);

        final java.awt.geom.GeneralPath SEP1 = new java.awt.geom.GeneralPath();
        SEP1.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        SEP1.moveTo(IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.20202020202020202);
        SEP1.lineTo(IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.20202020202020202);
        SEP1.closePath();
        SEP1.moveTo(IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.29419191919191917);
        SEP1.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.30934343434343436);
        SEP1.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.30934343434343436, IMAGE_WIDTH * 0.16666666666666666, IMAGE_HEIGHT * 0.31313131313131315, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.31691919191919193);
        SEP1.curveTo(IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.31691919191919193, IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.3207070707070707, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.3207070707070707);
        SEP1.curveTo(IMAGE_WIDTH * 0.64, IMAGE_HEIGHT * 0.3207070707070707, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.31691919191919193, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.31691919191919193);
        SEP1.curveTo(IMAGE_WIDTH * 0.85, IMAGE_HEIGHT * 0.31313131313131315, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.30934343434343436, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.30934343434343436);
        SEP1.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.30934343434343436, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.29292929292929293);
        SEP1.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.2878787878787879, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.2866161616161616);
        SEP1.curveTo(IMAGE_WIDTH * 0.8066666666666666, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.6666666666666666, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.2967171717171717);
        SEP1.curveTo(IMAGE_WIDTH * 0.32666666666666666, IMAGE_HEIGHT * 0.2967171717171717, IMAGE_WIDTH * 0.19, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.2866161616161616);
        SEP1.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.2878787878787879, IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.29419191919191917, IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.29419191919191917);
        SEP1.closePath();
        final java.awt.geom.Point2D SEP1_START = new java.awt.geom.Point2D.Double(SEP1.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D SEP1_STOP = new java.awt.geom.Point2D.Double(SEP1.getBounds2D().getMaxX(), 0);
        final float[] SEP1_FRACTIONS =
        {
            0.0f,
            0.06f,
            0.08f,
            0.12f,
            0.16f,
            0.18f,
            0.23f,
            0.28f,
            0.34f,
            0.41f,
            0.45f,
            0.52f,
            0.55f,
            0.59f,
            0.69f,
            0.71f,
            0.93f,
            0.95f,
            1.0f
        };
        final java.awt.Color[] SEP1_COLORS =
        {
            new java.awt.Color(112, 112, 114, 255),
            new java.awt.Color(60, 60, 70, 255),
            new java.awt.Color(34, 37, 46, 255),
            new java.awt.Color(41, 41, 49, 255),
            new java.awt.Color(166, 160, 162, 255),
            new java.awt.Color(179, 175, 176, 255),
            new java.awt.Color(200, 198, 201, 255),
            new java.awt.Color(246, 246, 246, 255),
            new java.awt.Color(220, 219, 224, 255),
            new java.awt.Color(46, 48, 71, 255),
            new java.awt.Color(4, 4, 6, 255),
            new java.awt.Color(56, 57, 75, 255),
            new java.awt.Color(95, 96, 101, 255),
            new java.awt.Color(141, 138, 145, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(54, 53, 59, 255),
            new java.awt.Color(22, 21, 29, 255),
            new java.awt.Color(25, 23, 26, 255)
        };
        final java.awt.LinearGradientPaint SEP1_GRADIENT = new java.awt.LinearGradientPaint(SEP1_START, SEP1_STOP, SEP1_FRACTIONS, SEP1_COLORS);
        G2.setPaint(SEP1_GRADIENT);
        G2.fill(SEP1);
        final java.awt.Color STROKE_COLOR_SEP1 = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_SEP1);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(SEP1);

        final java.awt.geom.GeneralPath SEP1_TOP = new java.awt.geom.GeneralPath();
        SEP1_TOP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        SEP1_TOP.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.2815656565656566, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.27904040404040403, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.27904040404040403);
        SEP1_TOP.lineTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.2815656565656566);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.2815656565656566, IMAGE_WIDTH * 0.21, IMAGE_HEIGHT * 0.2840909090909091, IMAGE_WIDTH * 0.21666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.22333333333333333, IMAGE_HEIGHT * 0.2866161616161616, IMAGE_WIDTH * 0.29, IMAGE_HEIGHT * 0.2916666666666667, IMAGE_WIDTH * 0.47, IMAGE_HEIGHT * 0.2916666666666667);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.2904040404040404, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.2866161616161616, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.2866161616161616);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.79, IMAGE_HEIGHT * 0.2840909090909091, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.2828282828282828, IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.2828282828282828);
        SEP1_TOP.lineTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.27904040404040403);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.7833333333333333, IMAGE_HEIGHT * 0.27904040404040403, IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.2803030303030303, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.28535353535353536);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.28535353535353536, IMAGE_WIDTH * 0.84, IMAGE_HEIGHT * 0.29292929292929293, IMAGE_WIDTH * 0.49666666666666665, IMAGE_HEIGHT * 0.2967171717171717);
        SEP1_TOP.curveTo(IMAGE_WIDTH * 0.20333333333333334, IMAGE_HEIGHT * 0.29419191919191917, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.2866161616161616, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.28535353535353536);
        SEP1_TOP.closePath();
        final java.awt.geom.Point2D SEP1_TOP_START = new java.awt.geom.Point2D.Double(SEP1_TOP.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D SEP1_TOP_STOP = new java.awt.geom.Point2D.Double(SEP1_TOP.getBounds2D().getMaxX(), 0);
        final float[] SEP1_TOP_FRACTIONS =
        {
            0.0f,
            0.03f,
            0.05f,
            0.07f,
            0.13f,
            0.16f,
            0.21f,
            0.29f,
            0.39f,
            0.44f,
            0.5f,
            0.55f,
            0.6f,
            0.65f,
            0.71f,
            0.76f,
            0.8f,
            0.83f,
            0.86f,
            0.89f,
            0.94f,
            1.0f
        };
        final java.awt.Color[] SEP1_TOP_COLORS =
        {
            new java.awt.Color(110, 108, 109, 255),
            new java.awt.Color(110, 106, 107, 255),
            new java.awt.Color(4, 0, 0, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(229, 223, 227, 255),
            new java.awt.Color(152, 148, 149, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(115, 111, 110, 255),
            new java.awt.Color(205, 206, 208, 255),
            new java.awt.Color(121, 122, 124, 255),
            new java.awt.Color(201, 201, 203, 255),
            new java.awt.Color(112, 110, 113, 255),
            new java.awt.Color(197, 193, 194, 255),
            new java.awt.Color(124, 120, 119, 255),
            new java.awt.Color(203, 200, 195, 255),
            new java.awt.Color(111, 107, 106, 255),
            new java.awt.Color(124, 120, 121, 255),
            new java.awt.Color(189, 185, 186, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(115, 111, 112, 255),
            new java.awt.Color(131, 127, 128, 255),
            new java.awt.Color(93, 91, 92, 255)
        };
        final java.awt.LinearGradientPaint SEP1_TOP_GRADIENT = new java.awt.LinearGradientPaint(SEP1_TOP_START, SEP1_TOP_STOP, SEP1_TOP_FRACTIONS, SEP1_TOP_COLORS);
        G2.setPaint(SEP1_TOP_GRADIENT);
        G2.fill(SEP1_TOP);
        final java.awt.Color STROKE_COLOR_SEP1_TOP = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_SEP1_TOP);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(SEP1_TOP);

        final java.awt.geom.GeneralPath TOP = new java.awt.geom.GeneralPath();
        TOP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        TOP.moveTo(IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.005050505050505051);
        TOP.lineTo(IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.005050505050505051);
        TOP.closePath();
        TOP.moveTo(IMAGE_WIDTH * 0.8166666666666667, IMAGE_HEIGHT * 0.005050505050505051);
        TOP.lineTo(IMAGE_WIDTH * 0.8166666666666667, IMAGE_HEIGHT * 0.005050505050505051);
        TOP.closePath();
        TOP.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.008838383838383838);
        TOP.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.11363636363636363);
        TOP.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.11363636363636363, IMAGE_WIDTH * 0.17333333333333334, IMAGE_HEIGHT * 0.11868686868686869, IMAGE_WIDTH * 0.28, IMAGE_HEIGHT * 0.12121212121212122);
        TOP.curveTo(IMAGE_WIDTH * 0.2833333333333333, IMAGE_HEIGHT * 0.12121212121212122, IMAGE_WIDTH * 0.31666666666666665, IMAGE_HEIGHT * 0.12373737373737374, IMAGE_WIDTH * 0.48, IMAGE_HEIGHT * 0.12373737373737374);
        TOP.curveTo(IMAGE_WIDTH * 0.65, IMAGE_HEIGHT * 0.12373737373737374, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.12121212121212122, IMAGE_WIDTH * 0.7133333333333334, IMAGE_HEIGHT * 0.12121212121212122);
        TOP.curveTo(IMAGE_WIDTH * 0.83, IMAGE_HEIGHT * 0.11868686868686869, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.11363636363636363, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.11363636363636363);
        TOP.lineTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.008838383838383838);
        TOP.lineTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.006313131313131313);
        TOP.lineTo(IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.012626262626262626);
        TOP.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.006313131313131313);
        TOP.lineTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.008838383838383838);
        TOP.closePath();
        final java.awt.geom.Point2D TOP_START = new java.awt.geom.Point2D.Double(TOP.getBounds2D().getMinX(), 0);
        final java.awt.geom.Point2D TOP_STOP = new java.awt.geom.Point2D.Double(TOP.getBounds2D().getMaxX(), 0);
        final float[] TOP_FRACTIONS =
        {
            0.0f,
            0.06f,
            0.08f,
            0.12f,
            0.16f,
            0.18f,
            0.23f,
            0.28f,
            0.34f,
            0.41f,
            0.45f,
            0.52f,
            0.55f,
            0.59f,
            0.69f,
            0.71f,
            0.93f,
            0.95f,
            1.0f
        };
        final java.awt.Color[] TOP_COLORS =
        {
            new java.awt.Color(112, 112, 114, 255),
            new java.awt.Color(60, 60, 70, 255),
            new java.awt.Color(34, 37, 46, 255),
            new java.awt.Color(41, 41, 49, 255),
            new java.awt.Color(166, 160, 162, 255),
            new java.awt.Color(179, 175, 176, 255),
            new java.awt.Color(200, 198, 201, 255),
            new java.awt.Color(246, 246, 246, 255),
            new java.awt.Color(220, 219, 224, 255),
            new java.awt.Color(46, 48, 71, 255),
            new java.awt.Color(4, 4, 6, 255),
            new java.awt.Color(56, 57, 75, 255),
            new java.awt.Color(95, 96, 101, 255),
            new java.awt.Color(141, 138, 145, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(194, 190, 191, 255),
            new java.awt.Color(54, 53, 59, 255),
            new java.awt.Color(22, 21, 29, 255),
            new java.awt.Color(25, 23, 26, 255)
        };
        final java.awt.LinearGradientPaint TOP_GRADIENT = new java.awt.LinearGradientPaint(TOP_START, TOP_STOP, TOP_FRACTIONS, TOP_COLORS);
        G2.setPaint(TOP_GRADIENT);
        G2.fill(TOP);
        final java.awt.Color STROKE_COLOR_TOP = new java.awt.Color(0x7F7E83);
        G2.setColor(STROKE_COLOR_TOP);
        G2.setStroke(new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER));
        G2.draw(TOP);

        final java.awt.geom.GeneralPath ROOFBACK = new java.awt.geom.GeneralPath();
        ROOFBACK.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        ROOFBACK.moveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFBACK.curveTo(IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.0025252525252525255, IMAGE_WIDTH * 0.32, IMAGE_HEIGHT * 0.0, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.0);
        ROOFBACK.curveTo(IMAGE_WIDTH * 0.6766666666666666, IMAGE_HEIGHT * 0.0, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.0025252525252525255, IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFBACK.curveTo(IMAGE_WIDTH * 0.8233333333333334, IMAGE_HEIGHT * 0.010101010101010102, IMAGE_WIDTH * 0.6766666666666666, IMAGE_HEIGHT * 0.012626262626262626, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.012626262626262626);
        ROOFBACK.curveTo(IMAGE_WIDTH * 0.32, IMAGE_HEIGHT * 0.012626262626262626, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.010101010101010102, IMAGE_WIDTH * 0.17666666666666667, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFBACK.closePath();
        final java.awt.geom.Point2D ROOFBACK_START = new java.awt.geom.Point2D.Double(0, ROOFBACK.getBounds2D().getMinY() );
        final java.awt.geom.Point2D ROOFBACK_STOP = new java.awt.geom.Point2D.Double(0, ROOFBACK.getBounds2D().getMaxY() );
        final float[] ROOFBACK_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] ROOFBACK_COLORS =
        {
            new java.awt.Color(235, 235, 237, 255),
            new java.awt.Color(51, 51, 51, 255)
        };
        final java.awt.LinearGradientPaint ROOFBACK_GRADIENT = new java.awt.LinearGradientPaint(ROOFBACK_START, ROOFBACK_STOP, ROOFBACK_FRACTIONS, ROOFBACK_COLORS);
        G2.setPaint(ROOFBACK_GRADIENT);
        G2.fill(ROOFBACK);

        final java.awt.geom.GeneralPath ROOFTOP = new java.awt.geom.GeneralPath();
        ROOFTOP.setWindingRule(java.awt.geom.GeneralPath.WIND_EVEN_ODD);
        ROOFTOP.moveTo(IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFTOP.curveTo(IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.0025252525252525255, IMAGE_WIDTH * 0.32, IMAGE_HEIGHT * 0.0012626262626262627, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.0012626262626262627);
        ROOFTOP.curveTo(IMAGE_WIDTH * 0.6766666666666666, IMAGE_HEIGHT * 0.0012626262626262627, IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.0025252525252525255, IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFTOP.curveTo(IMAGE_WIDTH * 0.82, IMAGE_HEIGHT * 0.010101010101010102, IMAGE_WIDTH * 0.6766666666666666, IMAGE_HEIGHT * 0.011363636363636364, IMAGE_WIDTH * 0.5, IMAGE_HEIGHT * 0.011363636363636364);
        ROOFTOP.curveTo(IMAGE_WIDTH * 0.32, IMAGE_HEIGHT * 0.011363636363636364, IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.010101010101010102, IMAGE_WIDTH * 0.18, IMAGE_HEIGHT * 0.006313131313131313);
        ROOFTOP.closePath();
        final java.awt.geom.Point2D ROOFTOP_START = new java.awt.geom.Point2D.Double(0, ROOFTOP.getBounds2D().getMinY() );
        final java.awt.geom.Point2D ROOFTOP_STOP = new java.awt.geom.Point2D.Double(0, ROOFTOP.getBounds2D().getMaxY() );
        final float[] ROOFTOP_FRACTIONS =
        {
            0.0f,
            1.0f
        };
        final java.awt.Color[] ROOFTOP_COLORS =
        {
            new java.awt.Color(138, 134, 135, 255),
            new java.awt.Color(117, 117, 119, 255)
        };
        final java.awt.LinearGradientPaint ROOFTOP_GRADIENT = new java.awt.LinearGradientPaint(ROOFTOP_START, ROOFTOP_STOP, ROOFTOP_FRACTIONS, ROOFTOP_COLORS);
        G2.setPaint(ROOFTOP_GRADIENT);
        G2.fill(ROOFTOP);

        G2.dispose();

        return IMAGE;
    }

    public void componentResized(java.awt.event.ComponentEvent event)
    {
        setPreferredSize(new java.awt.Dimension((int) (getHeight() * 0.3787878788), getHeight()));
        setSize(getPreferredSize());
        init(getWidth(), getHeight());
    }

    public void componentMoved(java.awt.event.ComponentEvent event)
    {

    }

    public void componentShown(java.awt.event.ComponentEvent event)
    {

    }

    public void componentHidden(java.awt.event.ComponentEvent event)
    {

    }

    @Override
    public String toString()
    {
        return "Design42";
    }
}
