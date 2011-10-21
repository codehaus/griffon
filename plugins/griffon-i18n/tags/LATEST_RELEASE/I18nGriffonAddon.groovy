/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.context.MessageSource
import griffon.core.GriffonApplication
import griffon.plugins.i18n.MessageSourceHolder
import griffon.plugins.i18n.DelegatingMessageSource
import griffon.plugins.i18n.ExtendedResourceBundleMessageSource

/**
 * @author Andres Almiray
 */
class I18nGriffonAddon {
    private static final String DEFAULT_I18N_FILE = 'messages'

    void addonInit(GriffonApplication app) {
        List<String> basenames = app.config.i18n?.basenames ?: [DEFAULT_I18N_FILE]
        if (!basenames.contains(DEFAULT_I18N_FILE)) basenames = [DEFAULT_I18N_FILE] + basenames
        MessageSource messageSource = new ExtendedResourceBundleMessageSource()
        MessageSourceHolder.messageSource = new DelegatingMessageSource(messageSource)
        messageSource.basenames = basenames as String[]
        MetaClass mc = app.metaClass
        mc.messageSource = MessageSourceHolder.messageSource
        mc.i18n = MessageSourceHolder.messageSource
        mc.getMessage = { Object... args -> MessageSourceHolder.messageSource.getMessage(* args) }
    }

    def methods = [
        getMessage: { Object... args -> MessageSourceHolder.messageSource.getMessage(* args) }
    ]

    def props = [
        messageSource: [
                get: {-> MessageSourceHolder.messageSource }
        ]
    ]
}
