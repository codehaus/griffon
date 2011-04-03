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


    def 'Delegate should trigger error decorator if error is there'() {
        ErrorRendererAttributeDelegator delegator = new ErrorRendererAttributeDelegator()

        def errors = new Errors()

        def builder = [model: [errors: errors]]
        def node = [:]
        def attributes = ['errorRenderer': 'errorField: email']
        def outcome = [:]

        delegator.errorRenderer = [render: {n, style ->
            outcome['invoked'] = true
            outcome['style'] = style
        }] as ErrorRenderer

        when:
        errors.rejectValue('email', 'emailError')
        delegator.delegate(builder, node, attributes)

        then:
        outcome['invoked'] == true
        outcome['style'] == 'default'
    }

}
