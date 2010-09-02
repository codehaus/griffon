/*
 * Copyright 2004-2010 the original author or authors.
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
package griffon.domain.metaclass;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

/**
 * @author Andres Almiray
 */
public class MethodSignature {
    private final Class returnType;
    private final String methodName;
    private final Class[] parameterTypes;
    private final boolean isStatic;

    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    public MethodSignature(String methodName) {
        this(false, Void.TYPE, methodName, EMPTY_CLASS_ARRAY);
    }

    public MethodSignature(Class returnType, String methodName) {
        this(false, returnType, methodName, EMPTY_CLASS_ARRAY);
    }

    public MethodSignature(String methodName, Class... parameterTypes) {
        this(false, Void.TYPE, methodName, parameterTypes);
    }

    public MethodSignature(Class returnType, String methodName, Class... parameterTypes) {
        this(false, returnType, methodName, parameterTypes);
    }

    public MethodSignature(boolean isStatic, String methodName) {
        this(isStatic, Void.TYPE, methodName, EMPTY_CLASS_ARRAY);
    }

    public MethodSignature(boolean isStatic, Class returnType, String methodName) {
        this(isStatic, returnType, methodName, EMPTY_CLASS_ARRAY);
    }

    public MethodSignature(boolean isStatic, String methodName, Class... parameterTypes) {
        this(isStatic, Void.TYPE, methodName, parameterTypes);
    }

    public MethodSignature(boolean isStatic, Class returnType, String methodName, Class... parameterTypes) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == EMPTY_CLASS_ARRAY ? EMPTY_CLASS_ARRAY : copyClassArray(parameterTypes);
    }

    public Class getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class[] getParameterTypes() {
        return copyClassArray(parameterTypes);
    }

    public boolean isStatic() {
        return isStatic;
    }

    private Class[] copyClassArray(Class[] array) {
        Class[] copy = new Class[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    } 

    public String toString() {
       return (isStatic? "static " : "") +
              returnType.getName() + " "+
              methodName + "(" +
              DefaultGroovyMethods.join(parameterTypes, ",") +
              ")";
    }
}