/*
 * Copyright 2010 the original author or authors.
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

package org.codehaus.griffon.runtime.scaffolding;

import java.util.Date;
import java.util.Currency;
import java.util.Calendar;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;

import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import griffon.util.GriffonClassUtils;
import griffon.util.GriffonNameUtils;
import griffon.exceptions.GriffonException;
import griffon.plugins.scaffolding.*;

/**
 * @author Andres Almiray
 */
public final class ModelUtils {
    private static final Map<String, Map<String, Class<?>>> MODEL_CACHE = new LinkedHashMap<String, Map<String, Class<?>>>();

    private ModelUtils() {}

    public static final Class[] BASIC_TYPES = {
        boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class,
        Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
        String.class, BigDecimal.class, BigInteger.class, Locale.class, Class.class, URI.class, URL.class,
        java.util.Date.class, Time.class, Timestamp.class, java.sql.Date.class, Calendar.class, GregorianCalendar.class,
        TimeZone.class, java.util.Currency.class
    };

    public static <B> Model makeModelFor(Class<B> clazz, ValueObject<B> owner, String propertyName) {
        return makeModelFor(clazz, owner, propertyName, false);
    }

    public static <B> Model<?> makeModelFor(Class<B> clazz, ValueObject<B> owner, String propertyName, boolean isReference) {
        PropertyDescriptor pd = getPropertyDescriptorFor(clazz, propertyName);
        if(isBasicType(pd.getPropertyType())) {
            // return isReference? new AttributeModelReference<B,T>(clazz, propertyName) : new DefaultAttributeModel<B,T>(clazz, owner, propertyName);
            return new DefaultAttributeModel(clazz, owner, propertyName);
        }

        return fetchBeanModelFor(clazz, owner, propertyName, isReference);
         
        //return isReference? null : new BeanModel<B>(clazz, owner);
    }

    public static boolean isBasicType(Class<?> clazz) {
        if(clazz == null) return false;
        for(Class c : BASIC_TYPES) {
            if(c.equals(clazz)) return true;
        }
        return false;
    }
    
    private static PropertyDescriptor getPropertyDescriptorFor(Class<?> clazz, String propertyName) {
        PropertyDescriptor pd = null;
        try {
            pd = GriffonClassUtils.getPropertyDescriptor(clazz, propertyName); 
        } catch(IllegalAccessException iae) {
            throw new GriffonException(iae);
        } catch(InvocationTargetException ite) {
            throw new GriffonException(ite);
        } catch(NoSuchMethodException  nsme) {
            throw new GriffonException(nsme);
        }

        if(pd == null) {
            throw new IllegalArgumentException("There is no property named "+ propertyName +" for class "+ clazz.getName());
        }
        return pd;
    }

    private static <B> Model<?> fetchBeanModelFor(Class<B> ownerClass, ValueObject<B> owner, String propertyName, boolean isReference) {
        Map<String, Class<?>> beanAttributeMappings = MODEL_CACHE.get(ownerClass.getName());
        if(beanAttributeMappings == null) {
            beanAttributeMappings = new LinkedHashMap<String, Class<?>>();
            MODEL_CACHE.put(ownerClass.getName(), beanAttributeMappings);
        }

        Class<?> beanClass = getPropertyDescriptorFor(ownerClass, propertyName).getPropertyType();
        Class<?> beanModelClass = beanAttributeMappings.get(propertyName);
        beanModelClass = beanModelClass != null? beanModelClass : resolveBeanModelClassFor(beanClass);
        Model<?> model = null;

/*
        if(beanModelClass == null) {
            // use DefaultBeanModel
            beanModelClass = DefaultBeanModel.class;
            if(isReference) {
                // wrap with BeanModelReference
            } else {
                // create new instance
                model = new DefaultBeanModel(beanClass);
            }
        } else if(DefaultBeanModel.class.isAssignableFrom(beanModelClass)) {
            if(isReference) {
                // wrap with BeanModelReference
            } else {
                // create new instance
                model = (Model<?>) GriffonClassUtils.instantiate(beanModelClass, new Object[]{beanClass});
            }
        } else if(ModelReference.class.isAssignableFrom(beanModelClass)) {
            if(isReference) {
                // wrap with BeanModelReference
            } else {
                // create new instance
            }
        } else if(isReference) {
            // wrap with BeanModelReference
        } else {
            // create new instance
            model = (Model<?>) GriffonClassUtils.instantiateClass(beanModelClass);
        }
*/

        beanAttributeMappings.put(propertyName, beanModelClass);
        return model;
    }

    private static Class<?> resolveBeanModelClassFor(Class<?> beanClass) {
        String className = beanClass.getName() + "BeanModel";
        try {
            return Class.forName(className);
        } catch(Exception e) {
            // ignore
        }
        return null;
    }
}
