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
package griffon.domain.metaclass;

import griffon.domain.GriffonDomain;

/**
 * @author Andres Almiray
 */
public interface StaticMethodInvocation extends MethodInvocation {
    /**
     * <p>Invokes the actual method. The class, method name and arguments are provided.
     * If no arguments are passed the argument array is empty.
     * 
     * @param clazz the class the static method is called on
     * @param methodName the static method name
     * @param arguments the arguments supplied
     * @return the return value of the static method invocation
     */
    public Object invoke(Class<GriffonDomain> clazz, String methodName, Object[] arguments);
}