/*
 * Copyright 2010 the original author or authors.
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
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.support.ResourceBundleMessageSource

/**
 * @author Andres Almiray
 */
class I18nGriffonAddon {
    private final MessageSource messageSource = new ResourceBundleMessageSource()
    private static final String DEFAULT_I18N_FILE = 'messages'

    def addonInit(app) {
        def basenames = app.config?.i18n?.basenames ?: [DEFAULT_I18N_FILE]
        if(!basenames.contains(DEFAULT_I18N_FILE)) basenames = [DEFAULT_I18N_FILE] + basenames
        messageSource.basenames = basenames as String[]
        app.metaClass.messageSource = messageSource
        app.metaClass.i18n = messageSource
        MetaClass mc = MessageSource.metaClass
        mc.getMessage = {String message, List args, String defaultMessage, Locale locale ->
            messageSource.getMessage(message, args as Object[], defaultMessage, locale)
        }
        mc.getMessage = {String message, List args, String defaultMessage ->
            messageSource.getMessage(message, args as Object[], defaultMessage, app.locale)
        }
        mc.getMessage = {String message, List args, Locale locale ->
            messageSource.getMessage(message, args as Object[], locale)
        }
        mc.getMessage = {String message, List args ->
            messageSource.getMessage(message, args as Object[], app.locale)
        }
        mc.getMessage = {String message, Object[] args, String defaultMessage ->
            messageSource.getMessage(message, args, defaultMessage, app.locale)
        }
        mc.getMessage = {String message, Object[] args ->
            messageSource.getMessage(message, args, app.locale)
        }
        mc.getMessage = {String message, Locale locale ->
            messageSource.getMessage(message, new Object[0], locale)
        }
        mc.getMessage = {String message ->
            messageSource.getMessage(message, new Object[0], app.locale)
        }
        mc.getMessage = {MessageSourceResolvable resolvable ->
            messageSource.getMessage(resolvable, app.locale)
        }

        // decorate GriffonApplication too!
        mc = app.metaClass
        mc.getMessage = {String message, List args, String defaultMessage, Locale locale ->
            messageSource.getMessage(message, args as Object[], defaultMessage, locale)
        }
        mc.getMessage = {String message, List args, String defaultMessage ->
            messageSource.getMessage(message, args as Object[], defaultMessage, app.locale)
        }
        mc.getMessage = {String message, List args, Locale locale ->
            messageSource.getMessage(message, args as Object[], locale)
        }
        mc.getMessage = {String message, List args ->
            messageSource.getMessage(message, args as Object[], app.locale)
        }
        mc.getMessage = {String message, Object[] args, String defaultMessage, Locale locale ->
            messageSource.getMessage(message, args, defaultMessage, locale)
        }
        mc.getMessage = {String message, Object[] args, String defaultMessage ->
            messageSource.getMessage(message, args, defaultMessage, app.locale)
        }
        mc.getMessage = {String message, Object[] args, Locale locale ->
            messageSource.getMessage(message, args, locale)
        }
        mc.getMessage = {String message, Object[] args ->
            messageSource.getMessage(message, args, app.locale)
        }
        mc.getMessage = {String message, Locale locale ->
            messageSource.getMessage(message, new Object[0], locale)
        }
        mc.getMessage = {String message ->
            messageSource.getMessage(message, new Object[0], app.locale)
        }
        mc.getMessage = {MessageSourceResolvable resolvable, Locale locale ->
            messageSource.getMessage(resolvable, locale)
        }
        mc.getMessage = {MessageSourceResolvable resolvable ->
            messageSource.getMessage(resolvable, app.locale)
        }
    }

    def methods = [
        getMessage: { Object... args -> messageSource.getMessage(*args) }
    ]

    def props = [
        messageSource: [
            get: {-> messageSource }
        ]
    ]
}
