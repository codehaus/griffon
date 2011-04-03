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

import org.apache.commons.lang.StringUtils

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ConfigReader {
    private static final String ERROR_FIELD = 'error'
    private static final String STYLES = 'styles'
    private static final String DEFAULT_STYLE = 'default'

    def configMap

    def ConfigReader(config) {
        try {
            GroovyShell shell = new GroovyShell()
            configMap = shell.evaluate("[$config]")
        } catch (Exception ex) {
            configMap = [:]
        }
    }

    def getErrorField() {
        return configMap[ERROR_FIELD]
    }

    def getRenderStyles() {
        def styles = configMap[STYLES]

        if (!styles)
            styles = [DEFAULT_STYLE]

        return styles
    }

    boolean isConfigured() {
        return configMap[ERROR_FIELD]
    }
}
