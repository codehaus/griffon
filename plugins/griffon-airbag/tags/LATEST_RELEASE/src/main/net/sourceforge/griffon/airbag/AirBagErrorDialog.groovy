package net.sourceforge.griffon.airbag

import java.awt.BorderLayout
import java.awt.TextArea
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import groovy.swing.SwingBuilder
import javax.swing.ImageIcon
import javax.swing.border.Border
import javax.swing.border.EmptyBorder
import javax.swing.border.EtchedBorder

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
        builder.setVariable('dialog', this)

        def mainPanel = builder.panel() {
            borderLayout(hgap: 5, vgap: 5)

            mainPanel = panel(border: new EmptyBorder(5, 10, 5, 10), constraints: BorderLayout.NORTH) {
                borderLayout(hgap: 10)

                label(icon: new ImageIcon(getClass().getClassLoader().getResource('net/sourceforge/griffon/airbag/messagebox_warning.png')),
                        text: "<html><b>Uncaught Exception: ${exception.getLocalizedMessage()}</b></html>",
                        constraints: BorderLayout.CENTER)

                stacktraceBtn = button(selected: false, action: action(name: 'Stack Trace', closure: {
                    stacktracePane.visible = !stacktraceBtn.selected
                    stacktraceBtn.selected = stacktracePane.visible
                    if(stacktracePane.visible)
                        dialog.setSize(620, 475)
                    else
                        dialog.setSize(575, 75)
                }), constraints: BorderLayout.EAST)
            }

            stacktracePane = scrollPane(visible: false, border: new EmptyBorder(3, 3, 3, 3), constraints: BorderLayout.CENTER) {
                    textArea(text: stackTrace)
                }
        }

        add mainPanel

        setSize(575, 75)
        setModal true
        setLocationRelativeTo owner
    }

}
