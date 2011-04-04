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

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ErrorRendererSpec extends UnitSpec {

    def 'Error renderer should invoke decorator by style name'() {
        def highlighted = false
        def poppedUp = false

        ErrorRenderer.decorators = [
                highlight: [decorate: {builder, node, fieldError, messageSource -> highlighted = true}] as ErrorNodeDecorator,
                popup: [decorate: {builder, node, fieldError, messageSource -> poppedUp = true}] as ErrorNodeDecorator
        ]

        ErrorRenderer renderer = new ErrorRenderer()

        def builder = [:]
        def node = [:]
        def styles = ['highlight', 'popup']
        def fieldError = [:]
        def messageSource = [:]

        renderer.render(builder, node, styles, fieldError, messageSource)

        expect:
        highlighted == true
        poppedUp == true
    }

}
