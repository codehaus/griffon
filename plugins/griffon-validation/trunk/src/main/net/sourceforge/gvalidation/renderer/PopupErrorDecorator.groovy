/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.renderer

import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.FieldError
import javax.swing.JDialog
import java.awt.FlowLayout
import java.awt.Color
import javax.swing.JLabel
import javax.swing.ImageIcon

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class PopupErrorDecorator extends BaseErrorDecorator {

    JDialog popup
    Color bgColor = new Color(243, 255, 159)
    JLabel image
    JLabel messageLabel

    @Override
    void register(model, node, errorField, messageSource) {
        super.register(model, node, errorField, messageSource)

        image = new JLabel(new ImageIcon(getClass().getClassLoader().getResource('net/sourceforge/gvalidation/error.png')))
        messageLabel = new JLabel()

        popup = new JDialog()
        popup.getContentPane().setLayout(new FlowLayout());
        popup.setUndecorated(true);
        popup.getContentPane().setBackground(bgColor);
        popup.getContentPane().add(image);
        popup.getContentPane().add(messageLabel);
        popup.setFocusableWindowState(false);
    }



    @Override
    protected void decorate(Errors errors, FieldError fieldError) {
        messageLabel.text = messageSource.getMessage(fieldError.errorCode, fieldError.arguments)

        popup.setSize(0, 0);
        popup.setLocationRelativeTo(targetComponent)
        def popupLocation = popup.getLocation()
        def targetComponentSize = targetComponent.getSize()
        popup.setLocation((int)(popupLocation.x - targetComponentSize.getWidth() / 2),
                (int)(popupLocation.y + targetComponentSize.getHeight() / 2))
        popup.pack()
        popup.visible = true
    }

    @Override
    protected void undecorate() {
        popup.visible = false
    }


}
