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
import org.springframework.context.MessageSource

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererSpec extends UnitSpec {

    def 'Error renderer should invoke decorator by style name'() {
        ErrorRenderer.decoratorClassMap = [
                highlight: MockHighlightErrorNodeDecorator.class,
                popup: MockPopupErrorNodeDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def model = [:]
        def node = [:]
        def styles = ['highlight']
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        expect:
        results.size() == 1
        results[0] instanceof MockHighlightErrorNodeDecorator
        results[0].isRegistered() == true
        results[0].getModel() == model
        results[0].getErrorField() == errorField
        results[0].targetComponent == node
        results[0].messageSource == messageSource
    }

    def 'Error renderer should invoke default decorator if style is not given'() {
        ErrorRenderer.decoratorClassMap = [
                'default': MockDefaultErrorNodeDecorator.class,
                highlight: MockHighlightErrorNodeDecorator.class,
                popup: MockPopupErrorNodeDecorator.class
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def node = [:]
        def model = [:]
        def styles = []
        def errorField = "email"
        def messageSource = [:] as MessageSource

        def results = renderer.register(model, node, styles, errorField, messageSource)

        expect:
        results.size() == 1
        results[0] instanceof MockDefaultErrorNodeDecorator
        results[0].isRegistered() == true
    }

    public class MockHighlightErrorNodeDecorator extends BaseErrorNodeDecorator {}

    public class MockPopupErrorNodeDecorator extends BaseErrorNodeDecorator {}

    public class MockDefaultErrorNodeDecorator extends BaseErrorNodeDecorator {}
}
