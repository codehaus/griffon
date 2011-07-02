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

package griffon.plugins.i18n;

import griffon.util.ApplicationHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import java.util.List;
import java.util.Locale;

/**
 * @author Andres Almiray
 */
public class DelegatingMessageSource implements ExtendedMessageSource {
    private final MessageSource messageSource;
    private static final Object[] EMPTY_ARGS = new Object[0];

    public DelegatingMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    private Locale locale() {
        return ApplicationHolder.getApplication().getLocale();
    }

    public String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return messageSource.getMessage(s, objects, s1, locale);
    }

    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(s, objects, locale);
    }

    public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(messageSourceResolvable, locale);
    }

    public String getMessage(String key) throws NoSuchMessageException {
        return messageSource.getMessage(key, EMPTY_ARGS, locale());
    }

    public String getMessage(String key, String defaultMessage) {
        return messageSource.getMessage(key, EMPTY_ARGS, defaultMessage, locale());
    }

    public String getMessage(String key, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(key, EMPTY_ARGS, locale);
    }

    public String getMessage(String key, String defaultMessage, Locale locale) {
        return messageSource.getMessage(key, EMPTY_ARGS, defaultMessage, locale);
    }

    public String getMessage(String key, List<Object> args) throws NoSuchMessageException {
        return messageSource.getMessage(key, args.toArray(), locale());
    }

    public String getMessage(String key, List<Object> args, String defaultMessage) {
        return messageSource.getMessage(key, args.toArray(), defaultMessage, locale());
    }

    public String getMessage(String key, List<Object> args, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(key, args.toArray(), locale);
    }

    public String getMessage(String key, List<Object> args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(key, args.toArray(), defaultMessage, locale);
    }

    public String getMessage(String key, Object[] args) throws NoSuchMessageException {
        return messageSource.getMessage(key, args, locale());
    }

    public String getMessage(String key, Object[] args, String defaultMessage) {
        return messageSource.getMessage(key, args, defaultMessage, locale());
    }

    public String getMessage(MessageSourceResolvable messageSourceResolvable) throws NoSuchMessageException {
        return messageSource.getMessage(messageSourceResolvable, locale());
    }
}
