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
class ConfigReaderSpec extends UnitSpec {

    def 'Config reader should be able to retrieve error field and default style'(){
        ConfigReader reader = new ConfigReader('for: "email"')

        expect:
        reader.isConfigured() == true
        reader.getErrorField() == 'email'
        reader.getRenderStyles().size() == 1
        reader.getRenderStyles()[0] == 'default'
    }

    def 'Config reader should be able to retrieve particular style'(){
        ConfigReader reader = new ConfigReader('for: "url", styles: ["highlight", "popup"]')

        expect:
        reader.isConfigured() == true
        reader.getErrorField() == 'url'
        reader.getRenderStyles().size() == 2
        reader.getRenderStyles()[0] == 'highlight'
        reader.getRenderStyles()[1] == 'popup'
    }

    def 'Config reader should not be configured with empty input'(){
        expect:
        new ConfigReader(config).isConfigured() == false

        where:
        config << ['', null, 'styles: [popup]', 'incorrect config']
    }

    def 'Config reader should be configured with various valid input'(){
        expect:
        new ConfigReader(config).isConfigured() == true

        where:
        config << [
                'for: "email"',
                'for: email',
                'for: "url", styles: [ highlight, pop ]',
                'styles:[popup],for:url',
                'styles :   [  "popup",   highlight ],    for :url'
        ]
    }

    def 'Config reader should be able to read without string quotes'(){
        ConfigReader reader = new ConfigReader('for: url, styles : [highlight, popup]')

        expect:
        reader.isConfigured() == true
        reader.getErrorField() == 'url'
        reader.getRenderStyles().size() == 2
        reader.getRenderStyles()[0] == 'highlight'
        reader.getRenderStyles()[1] == 'popup'
    }

}
