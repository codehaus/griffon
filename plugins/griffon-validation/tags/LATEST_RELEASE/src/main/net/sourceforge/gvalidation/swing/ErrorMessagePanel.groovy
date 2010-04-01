package net.sourceforge.gvalidation.swing

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import static javax.swing.SwingConstants.LEFT
import net.sourceforge.gvalidation.Errors

/**
 * Created by nick.zhu
 */
class ErrorMessagePanel extends JPanel {
    def messageSource
    Errors errors
    JPanel contentPanel

    def ErrorMessagePanel(def messageSource) {
        this.messageSource = messageSource

        setLayout new BorderLayout()

        contentPanel = new JPanel()

        contentPanel.setLayout new BoxLayout(contentPanel, BoxLayout.Y_AXIS)

        add(contentPanel)
    }

    public Errors getErrors() {
        return errors
    }

    public void setErrors(Errors errors) {
        this.errors = errors

        contentPanel.removeAll()

        if (errors && errors.hasErrors()) {
            setErrorMessageBorder()

            errors.each {error ->
                JLabel errorLabel = createErrorLabel(error)
                contentPanel.add(errorLabel)
            }
        } else {
            clearErrorMessageBorder()
        }

        revalidate()
    }

    private def setErrorMessageBorder() {
        def paddingBorder = BorderFactory.createEmptyBorder(2, 5, 2, 5)
        def errorHighlightBorder = BorderFactory.createLineBorder(Color.RED)
        contentPanel.setBorder BorderFactory.createCompoundBorder(errorHighlightBorder, paddingBorder)
    }

    private JLabel createErrorLabel(error) {
        def errorMessage = messageSource.getMessage(error.errorCode, error.arguments)
        def errorLabel = new JLabel(" - ${errorMessage}", LEFT)
        errorLabel.setForeground Color.RED
        return errorLabel
    }

    private def clearErrorMessageBorder() {
        contentPanel.setBorder BorderFactory.createEmptyBorder()
    }

}
