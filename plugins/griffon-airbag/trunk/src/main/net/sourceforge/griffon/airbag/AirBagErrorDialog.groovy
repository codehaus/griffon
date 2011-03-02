package net.sourceforge.griffon.airbag

import java.awt.BorderLayout
import java.awt.TextArea
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import groovy.swing.SwingBuilder
import javax.swing.ImageIcon

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class AirBagErrorDialog extends JDialog {

    def AirBagErrorDialog(owner, title, exception) {
        super(owner, title)

        StringWriter stackTraceWriter = new StringWriter()
        exception.printStackTrace(new PrintWriter(stackTraceWriter))
        def stackTrace = stackTraceWriter.toString()

        def builder = new SwingBuilder()

        def mainPanel = builder.panel {
            borderLayout()

            label(icon: new ImageIcon(getClass().getClassLoader().getResource('net/sourceforge/griffon/airbag/messagebox_warning.png')),
                    text: "<html><b>${exception.getLocalizedMessage()}</b></html>",
                    constraints: BorderLayout.NORTH)

            scrollPane(constraints: BorderLayout.CENTER) {
                textArea(text: stackTrace)
            }
        }

        add mainPanel

        setModal true
        setLocationRelativeTo owner
    }

}
