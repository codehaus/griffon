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

package net.sourceforge.gvalidation.renderer

import griffon.spock.UnitSpec
import org.springframework.context.MessageSource
import net.sourceforge.gvalidation.Errors

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorNodeDecoratorSpec extends UnitSpec {

    def 'Decorator should not decorate node when unmatching given field is raised'(){
        def resultErrors
        def resultFieldError

        given:
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator:{errors, fieldError -> resultErrors = errors; resultFieldError = fieldError}
        )

        def model = [errors:new Errors()]
        def node = [:]
        def errorField = "username"
        def messageSource = [:] as MessageSource

        decorator.register(model, node, errorField, messageSource)

        when:
        model.errors.rejectValue("email", "emailCode")
        Thread.sleep(300)

        then:
        resultErrors == null
        resultFieldError == null
    }

    def 'Decorator should decorate node when matching given field is raised'(){
        def resultErrors
        def resultFieldError

        given:
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator:{errors, fieldError -> resultErrors = errors; resultFieldError = fieldError}
        )

        def model = [errors:new Errors()]
        def node = [:]
        def errorField = "username"
        def messageSource = [:] as MessageSource

        decorator.register(model, node, errorField, messageSource)

        when:
        model.errors.rejectValue(errorField, "${errorField}Code")
        Thread.sleep(300)

        then:
        resultErrors != null
        resultFieldError != null
    }

    def 'Decorator should undecorate node when matching given field is removed'(){
        def resultErrors
        def resultFieldError

        given:
        ErrorDecorator decorator = new ClosureErrorDecorator(
                decorator:{errors, fieldError -> resultErrors = errors; resultFieldError = fieldError},
                undecorator:{resultErrors = null; resultFieldError = null}
        )

        def model = [errors:new Errors()]
        def node = [:]
        def errorField = "username"
        def messageSource = [:] as MessageSource

        decorator.register(model, node, errorField, messageSource)
        model.errors.rejectValue(errorField, "${errorField}Code")

        when:
        model.errors.clear()
        Thread.sleep(300)

        then:
        resultErrors == null
        resultFieldError == null
    }

}
