/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.charva.factory

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.logging.Level
import java.util.logging.Logger
import griffon.util.GriffonExceptionHandler

/**
 *
 * @author shemnon
 */
public class RichActionWidgetFactory extends AbstractCharvaFactory {
    static final Class[] STRING_ARGS = [String] as Class[];

    final Constructor stringCtor;
    final Class klass;

    public RichActionWidgetFactory(Class klass) {
        try {
            stringCtor = klass.getConstructor(STRING_ARGS);
            this.klass = klass;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger("global").log(Level.INFO, null, GriffonExceptionHandler.sanitize(ex));
        } catch (SecurityException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, GriffonExceptionHandler.sanitize(ex));
        }
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        try {
            if (value instanceof GString) value = value.toString()
            if (value == null) {
                return klass.newInstance()
            } else if (value instanceof String) {
                return stringCtor.newInstance(value);
            } else if (klass.isAssignableFrom(value.getClass())) {
                return value
            } else {
                throw new RuntimeException("$name can only have a value argument of type charvax.swing.Action, charvax.swing.Icon, java.lang.String, or $klass.name");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create component for '$name' reason: $e", e);
        }
    }

}
