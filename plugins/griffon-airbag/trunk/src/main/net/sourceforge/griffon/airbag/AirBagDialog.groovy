package net.sourceforge.griffon.airbag

import javax.swing.JDialog
import java.awt.Window
import java.awt.Container
import groovy.swing.SwingBuilder
import javax.swing.JPanel
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JScrollPane
import java.awt.TextArea

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class AirBagDialog extends JDialog {

    def AirBagDialog(owner, String title, Throwable exception){
        super(owner, title)

        setModal true
        setLocationRelativeTo owner

        JPanel mainPanel = new JPanel()

        mainPanel.setLayout(new BorderLayout())

        mainPanel.add(new JLabel("<html><b>${exception.getLocalizedMessage()}</b></html>"), BorderLayout.NORTH)

        TextArea stackTraceTextArea = new TextArea()
        JScrollPane stackTraceScrollPane = new JScrollPane(stackTraceTextArea)
        StringWriter stackTraceWriter = new StringWriter()
        exception.printStackTrace(new PrintWriter(stackTraceWriter))
        stackTraceTextArea.setText stackTraceWriter.toString()
        mainPanel.add(stackTraceScrollPane, BorderLayout.CENTER)

        add mainPanel
    }

}
