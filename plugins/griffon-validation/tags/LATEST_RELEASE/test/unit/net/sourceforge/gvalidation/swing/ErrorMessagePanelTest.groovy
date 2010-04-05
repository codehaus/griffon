package net.sourceforge.gvalidation.swing

import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import net.sourceforge.gvalidation.swing.ErrorMessagePanel
import net.sourceforge.gvalidation.Errors

/**
 * Created by nick.zhu
 */
class ErrorMessagePanelTest extends GroovyTestCase {

    public void testErrorPanelGeneration() {
        ErrorMessagePanel errorPanel = buildErrorPanel()

        def errorLabel = errorPanel.getContentPanel().getComponents()[0]

        assertTrue "Label type is incorrect", errorLabel instanceof JLabel
        assertEquals "Error message is incorrect", " - errorMessage", errorLabel.text
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof CompoundBorder
    }

    private ErrorMessagePanel buildErrorPanel() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        def messageSource = [getMessage: {code, args -> return "errorMessage" }]
        ErrorMessagePanel errorPanel = new ErrorMessagePanel(messageSource)

        errorPanel.errors = errors
        return errorPanel
    }

    public void testErrorReset(){
        ErrorMessagePanel errorPanel = buildErrorPanel()

        errorPanel.errors = new Errors()

        assertEquals "Error panel should be empty", 0, errorPanel.getContentPanel().components.size()
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof EmptyBorder
    }

    public void testErrorResetWithNull(){
        ErrorMessagePanel errorPanel = buildErrorPanel()

        errorPanel.errors = null

        assertEquals "Error panel should be empty", 0, errorPanel.getContentPanel().components.size()
        assertTrue "Error border was not generated", errorPanel.getContentPanel().getBorder() instanceof EmptyBorder
    }
}
