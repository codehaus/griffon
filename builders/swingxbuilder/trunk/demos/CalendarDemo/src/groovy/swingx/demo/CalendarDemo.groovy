// Groovy source file
package groovy.swingx.demo

import javax.swing.JLabel
import org.joda.time.DateTime
import groovy.swing.SwingXBuilder
import javax.swing.WindowConstants
/**
 *
 * @author jwill
 */
public class CalendarDemo{
    
    private form
    private dockLabel
    
    /** Creates a new instance of Main */
    public CalendarDemo() {
        form = new CalendarForm()
        dockLabel = new JLabel()
        def swing = new SwingXBuilder()
        def frame = new CalendarForm()
        form.show()

            while(true) {
               form.updateDate()
               DateTime date = form.getDate()
               dockLabel.setText(date.dayOfWeek().getAsText()+" "+
                  date.monthOfYear().getAsShortText() + "/" +
                  date.dayOfMonth().getAsShortText() + "/" +
                  date.year().getAsShortText())
               // update every minute
		if (!form.visible)
		    System.exit(0)
               try {
                   Thread.sleep(1000*60)
               } catch (InterruptedException ex) {
                   ex.printStackTrace()
               }
            }
    }
    public static void main(String[] args) {
        new CalendarDemo()
    }
    
}

