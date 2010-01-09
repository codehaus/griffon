/*
 * Copyright 2007-2010 Ben Galbraith.
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

package com.feature50.util;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

public class ReflectionUtils {
    private static final String[] PREFIXES_GETTER = { "get", "is", "has" };
    private static final String[] PREFIXES_SETTER = { "set" };

    public static boolean hasMethod(String methodName, Class[] args, Class baseClass, Class subClass) {
        Class currentClass = subClass;
        while (!currentClass.equals(baseClass)) {
            try {
                Method method = currentClass.getDeclaredMethod(methodName, args);
                if (method != null) return true;
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return false;
    }

    public static boolean hasMethod(String methodName, Class baseClass, Class subClass) {
        Class currentClass = subClass;
        while (!currentClass.equals(baseClass)) {
            Method[] methods = currentClass.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                String name = method.getName();
                if (name.equals(methodName)) return true;
            }
            currentClass = currentClass.getSuperclass();
        }

        return false;
    }

    public static Object invokeGetter(Object object, String field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getGetterMethod(object.getClass(), field);
        return method.invoke(object);
    }

    public static void invokeSetter(Object object, String field, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = null;
        if (value != null) {
            try {
                method = getSetterMethod(object.getClass(), field, value.getClass());
            } catch (NoSuchMethodException e) {
                Class primitiveClass = ReflectionUtils.getPrimitiveType(value.getClass());
                if (primitiveClass != null) {
                    try {
                        method = getSetterMethod(object.getClass(), field, primitiveClass);
                    } catch (NoSuchMethodException e1) {}
                }
            }
        }

        if (method == null) method = getSetterMethod(object.getClass(), field);

        method.invoke(object, new Object[] { value });
    }

    public static Method getGetterMethod(Class type, String property) throws NoSuchMethodException {
        String getName = getEtterMethodName(property, "get");
        try {
            return type.getMethod(getName);
        } catch (NoSuchMethodException e) {
            try {
                getName = getEtterMethodName(property, "is");
                return type.getMethod(getName);
            } catch (Exception e1) {
                throw new NoSuchMethodException("No getter found for property '" + property + "' using 'is', 'has', and 'get' prefixes");
            }
        }
    }

    public static String getEtterMethodName(String property, String type) {
        return type + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    public static Method getSetterMethod(Class type, String property) throws NoSuchMethodException {
        String setterName = getEtterMethodName(property, "set");

        Method setterMethod = null;
        Method[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals(setterName)) {
                if (setterMethod != null) throw new IllegalStateException(String.format("Type '%1$s' has more than one method named '%2$s'; use getSetterMethod(Class, String, Class) to obtain the right one", type, setterName));
                setterMethod = method;
            }
        }

        if (setterMethod == null) throw new NoSuchMethodException(String.format("No setter method for property '%1$s'", property));

        return setterMethod;
    }

    public static Method getSetterMethod(Class type, String property, Class... parameters) throws NoSuchMethodException {
        String setterName = getEtterMethodName(property, "set");
        return type.getMethod(setterName, parameters);
    }

    public static Class getPrimitiveType(Class wrapperType) {
        if (Boolean.class.equals(wrapperType)) {
            return boolean.class;
        } else if (Float.class.equals(wrapperType)) {
            return float.class;
        } else if (Double.class.equals(wrapperType)) {
            return double.class;
        } else if (Integer.class.equals(wrapperType)) {
            return int.class;
        } else if (Long.class.equals(wrapperType)) {
            return long.class;
        } else if (Short.class.equals(wrapperType)) {
            return short.class;
        } else if (Byte.class.equals(wrapperType)) {
            return byte.class;
        } else if (Character.class.equals(wrapperType)) {
            return char.class;
        } else {
            return null;
        }
    }

    public static String getFormattedNameForField(String field) {
        StringBuffer sb = new StringBuffer();
        sb.append(Character.toUpperCase(field.charAt(0)));
        for (int i = 1; i < field.length(); i++) {
            char c = field.charAt(i);
            if (Character.isUpperCase(c)) sb.append(" ");
            sb.append(c);
        }
        return sb.toString();
    }

    public static Method[] getGetterMethods(Class type) {
        Method[] methods = getEtterMethods(type, "get");
        List<Method> list = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++) {
            if (ArrayUtils.isNullOrEmpty(methods[i].getParameterTypes())) list.add(methods[i]);
        }
        return list.toArray(new Method[0]);
    }

    private static Method[] getEtterMethods(Class type, String prefix) {
        Method[] methods = type.getMethods();
        List etters = new ArrayList();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if ((name.startsWith(prefix)) && (name.length() > 3) && (Character.isUpperCase(name.charAt(3)))) {
                if (ReflectionUtils.hasMethod(method.getName(),
                        method.getParameterTypes(),
                        (type instanceof Object) ? Object.class : type.getSuperclass(),
                        type)) {
                    etters.add(method);
                }
            }
        }

        return (Method[]) etters.toArray(new Method[0]);
    }

    public static String getPropertyFromEtter(Method method) {
        return getPropertyFromEtter(method.getName());
    }

    public static String getPropertyFromEtter(String name) {
        for (int i = 0; i < PREFIXES_GETTER.length; i++) {
            if (name.startsWith(PREFIXES_GETTER[i])) {
                return Character.toLowerCase(name.charAt(PREFIXES_GETTER[i].length())) + name.substring(PREFIXES_GETTER[i].length() + 1);
            }
        }

        for (int i = 0; i < PREFIXES_SETTER.length; i++) {
            if (name.startsWith(PREFIXES_SETTER[i])) {
                return Character.toLowerCase(name.charAt(PREFIXES_SETTER[i].length())) + name.substring(PREFIXES_SETTER[i].length() + 1);
            }
        }

        return null;
    }
}