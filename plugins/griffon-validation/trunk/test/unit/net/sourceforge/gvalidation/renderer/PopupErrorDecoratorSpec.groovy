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

import griffon.spock.UnitSpec
import net.sourceforge.gvalidation.Errors
import java.awt.Color
import javax.swing.JComponent
import org.springframework.context.MessageSource

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class PopupErrorDecoratorSpec extends UnitSpec {

    def 'Popup error decorator should create popup while decorating'(){
        given:
        PopupErrorDecorator decorator = new PopupErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def node = [:] as JComponent
        def messageResource = [getMessage:{code, args -> 'Error message'}]

        decorator.register(model, node, errorField, messageResource)

        when:
        decorator.decorate(errors, errors.getFieldError(errorField))

        then:
        decorator.popup != null
        decorator.messageLabel.text == "Error message"
        decorator.popup.visible == true
    }

    def 'Popup error decorator should hide popup while undecorating'(){
        given:
        PopupErrorDecorator decorator = new PopupErrorDecorator()

        def errorField = "email"
        def errors = new Errors()
        errors.rejectValue(errorField, 'emailErrorCode')

        def model = [errors:errors]
        def node = [:] as JComponent
        def messageResource = [getMessage:{code, args -> 'Error message'}]

        decorator.register(model, node, errorField, messageResource)

        when:
        decorator.decorate(errors, errors.getFieldError(errorField))
        decorator.undecorate()

        then:
        decorator.popup != null
        decorator.popup.visible == false
    }

}
