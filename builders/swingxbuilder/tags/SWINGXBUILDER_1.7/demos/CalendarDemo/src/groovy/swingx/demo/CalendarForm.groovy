// Groovy source file
package groovy.swingx.demo

import java.awt.Color
import java.awt.Font
import java.awt.Insets
import java.awt.LinearGradientPaint
import java.awt.Point
import java.util.Date
import org.jdesktop.swingx.JXPanel
import org.jdesktop.swingx.JXFrame
import org.jdesktop.swingx.painter.CompoundPainter
import org.jdesktop.swingx.painter.PinstripePainter
import org.jdesktop.swingx.painter.RectanglePainter
import org.jdesktop.swingx.painter.effects.GlowPathEffect
import org.joda.time.DateTime
import groovy.swing.SwingXBuilder
import org.jdesktop.swingx.painter.*
import javax.swing.SwingConstants
import net.miginfocom.swing.MigLayout
import javax.swing.BorderFactory

public class CalendarForm extends JXFrame {
    private dayOfMonth, dayOfWeek, jButton1, jButton2
    private jPanel1, leftPanel, month, rightPanel
    def swing = new SwingXBuilder()
    private date = new DateTime()
    def m, rect,comp

    /** Creates new form CalendarForm */
    public CalendarForm() {
        initComponents()
        updateDate()
	  resizable=true
        title = 'BarCamp Calendar Demo'
	  size =[475,243]
        m = new MonthPainter(this)
        /*def panel = (JXPanel)jPanel1
        panel.setBackgroundPainter(m)*/
	 
    }
                          
    private void initComponents() {
        leftPanel = new JXPanel()
        jPanel1 = new JXPanel()
        rightPanel = new JXPanel()

        layout = new MigLayout()
        //javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS)
        background = new java.awt.Color(0, 0, 0)
        //opaque = false
        jPanel1.opaque = false
        JXPanel panel = (JXPanel)jPanel1;
        panel.size = [237,174]
        panel.setBackgroundPainter(new CompoundPainter(
                //new MattePainter(Color.WHITE),
                new MonthPainter(this)
                ))

    def grad = new LinearGradientPaint(new Point(61,13), new Point(62,160),
                [0.0, 0.33f, 0.88f, 1.0f] as float[],
                [new Color(0,0,0), new Color(153,0,0), new Color(255,0,0), new Color(251,177,177)]as Color[])

        comp = swing.compoundPainter(clipPreserved:true) {
            rectanglePainter(borderWidth:3f, borderPaint:Color.BLACK,
                areaEffects:new GlowPathEffect(), roundHeight:40, roundWidth:40, 
                insets: [3,3,3,3], paintStretched:true,style:AbstractAreaPainter.Style.FILLED,
                fillPaint:grad)
            pinstripePainter(paint:[204,0,0,36] as Color,angle:45, stripeWidth:8, spacing:8)
            
        }


    leftPanel = swing.panel(layout:new MigLayout(), backgroundPainter:comp) {
        jButton1 = button(foreground:[255,255,255] as Color, text:'<', border:BorderFactory.createEmptyBorder(4,4,4,4),
              actionPerformed: {setDate(getDate().minusMonths(1))}, constraints:"")

        month = label(foreground:[255,255,255] as Color, horizontalAlignment:SwingConstants.CENTER, text:'Month', 
              constraints:"h 16!,w 183!")

        jButton2 = button(foreground:[255,255,255] as Color, text:'>', border:BorderFactory.createEmptyBorder(4,4,4,4),
                contentAreaFilled:false,actionPerformed: {setDate(getDate().plusMonths(1))}, constraints:"")

        widget(jPanel1, constraints:"h 0:174:32767, w 0:200:32767, south, newline")

    } 
jButton1.setContentAreaFilled(false)
jButton2.setContentAreaFilled(false)


        

        rightPanel = swing.panel(layout:new MigLayout(), backgroundPainter:comp) {
            dayOfWeek = label(font:new Font("Lucida Grande", 0, 24), foreground:[255,255,255]as Color, 
                horizontalAlignment:SwingConstants.CENTER, text:'Wednesday', constraints:"w 180!, gap bottom 40, north")
                
            dayOfMonth = label(font:new Font("Lucida Grande", 0, 100), foreground:[255,255,255]as Color, 
                horizontalAlignment:SwingConstants.CENTER, text:'33', constraints:"w 180!, h 80!, south, newline")
        }


        add(leftPanel) 
        add(rightPanel, "growy")
		pack()
    }                                                        
    
    public void updateDate() {
        setDate(new DateTime(new Date()))
    }
    
    public DateTime getDate() {
        return date
    }
    
    public void setDate(DateTime date) {
        this.date = date
        month.setText(date.monthOfYear().getAsText() + " " +
                date.year().getAsText())
        dayOfWeek.setText(date.dayOfWeek().getAsText())//dayOfWeekFormat.format(date))
        dayOfMonth.setText(date.dayOfMonth().getAsText())//dayOfMonthFormat.format(date))
        jPanel1.repaint()
	  this.repaint()
        m?.dirty = true
    }       
}
