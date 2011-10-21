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

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.List;
import java.util.Locale;

/**
 * @author Andres Almiray
 */
public interface ExtendedMessageSource extends MessageSource {
    String getMessage(String key) throws NoSuchMessageException;

    String getMessage(String key, String defaultMessage);

    String getMessage(String key, Locale locale) throws NoSuchMessageException;

    String getMessage(String key, String defaultMessage, Locale locale);

    String getMessage(String key, List<?> args) throws NoSuchMessageException;

    String getMessage(String key, List<?> args, String defaultMessage);

    String getMessage(String key, List<?> args, Locale locale) throws NoSuchMessageException;

    String getMessage(String key, List<?> args, String defaultMessage, Locale locale);

    String getMessage(String key, Object[] args) throws NoSuchMessageException;

    String getMessage(String key, Object[] args, String defaultMessage);

    String getMessage(MessageSourceResolvable messageSourceResolvable) throws NoSuchMessageException;
}
