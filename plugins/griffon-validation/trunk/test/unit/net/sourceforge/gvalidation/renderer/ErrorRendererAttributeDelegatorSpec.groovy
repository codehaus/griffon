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
import org.springframework.context.MessageSource

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererAttributeDelegatorSpec extends UnitSpec {

    def 'Delegator should be able to detect errorRenderer attribute'() {
        expect:
        hasAttribute == expectedResult

        where:
        hasAttribute << [new ErrorRendererAttributeDelegator().isAttributeSet(['errorRenderer': 'something']),
                new ErrorRendererAttributeDelegator().isAttributeSet(['errorRenderer': null])]
        expectedResult << [true, false]
    }

    def 'Attribute should not be removed during checking'() {
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()
        def attributes = ['errorRenderer': 'something']

        expect:
        delegator.isAttributeSet(attributes) == true
        delegator.isAttributeSet(attributes) == true
    }

    def 'Delegate should not trigger error decorator if error config is not valid'() {
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'incorrect config']
        def outcome = [:]
        def app = [messageSource: [] as MessageSource]

        delegator.errorRenderer = [render: {n, styles ->
            outcome['invoked'] = true
            outcome['styles'] = styles
        }] as ErrorRenderer

        when:
        delegator.delegate(app, builder, node, attributes)

        then:
        outcome['invoked'] == null
    }

    def 'Delegate should not trigger error decorator if error is not there'() {
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'error: email']
        def outcome = [:]
        def app = [messageSource: [] as MessageSource]

        delegator.errorRenderer = [render: {n, styles ->
            outcome['invoked'] = true
            outcome['styles'] = styles
        }] as ErrorRenderer

        when:
        delegator.delegate(app, builder, node, attributes)

        then:
        outcome['invoked'] == null
    }

    def 'Delegate should trigger error decorator if error is there'() {
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'error: email']
        def outcome = [:]
        def app = [messageSource: [] as MessageSource]

        delegator.errorRenderer = [render: {b, n, styles, fieldError, messageSource ->
            outcome['invoked'] = true
            outcome['styles'] = styles
        }] as ErrorRenderer

        when:
        errors.rejectValue('email', 'emailError')
        delegator.delegate(app, builder, node, attributes)

        then:
        outcome['invoked'] == true
        outcome['styles'] == ['default']
    }

}
