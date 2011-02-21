/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation.swing

import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import static javax.swing.SwingConstants.LEFT
import net.sourceforge.gvalidation.Errors
import org.springframework.context.NoSuchMessageException

/**
 * Created by nick.zhu
 */
class ErrorMessagePanel extends JPanel {
    private ImageIcon errorIcon;
    private ImageIcon successIcon;

    def messageSource
    Errors errors
    JPanel contentPanel

    def ErrorMessagePanel(def messageSource) {
        this.messageSource = messageSource
        this.errorIcon = new ImageIcon(getClass().getClassLoader().getResource('net/sourceforge/gvalidation/error.png'))
        this.successIcon = new ImageIcon(getClass().getClassLoader().getResource('net/sourceforge/gvalidation/success.png'))

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

    def createErrorLabel(error) {
        def errorMessage = ""

        try {
            errorMessage = messageSource.getMessage(error.errorCode, error.arguments)
        } catch (NoSuchMessageException ex) {
            errorMessage = messageSource.getMessage(error.defaultErrorCode, error.arguments)
        }

        def errorLabel = new JLabel(" ${errorMessage}", LEFT)

        errorLabel.setIcon errorIcon
        errorLabel.setForeground Color.RED

        return errorLabel
    }

    private def clearErrorMessageBorder() {
        contentPanel.setBorder BorderFactory.createEmptyBorder()
    }

}
