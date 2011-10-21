/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.plugins.hessian

import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import java.util.concurrent.ConcurrentHashMap

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
@Singleton
class HessianConnector {
    private final Map PROXIES = new ConcurrentHashMap()
    private static final Class[] CTOR_ARGS1 = [String, Class] as Class[]
    private static final Class[] CTOR_ARGS2 = [String, String] as Class[]

    static void enhance(MetaClass mc, Object instance = null) {
        mc.withHessian = {Map params, Closure closure ->
            HessianConnector.instance.withHessian(instance, params, closure)
        }
        mc.withHessian = {Map params, CallableWithArgs callable ->
            HessianConnector.instance.withHessian(instance, params, callable)
        }
        mc.withBurlap = {Map params, Closure closure ->
            HessianConnector.instance.withBurlap(instance, params, closure)
        }
        mc.withBurlap = {Map params, CallableWithArgs callable ->
            HessianConnector.instance.withBurlap(instance, params, callable)
        }
    }

    Object withHessian(Object instance = null, Map params, Closure closure) {
        doWithProxy(HessianProxy, instance, params, closure)
    }

    public <T> T withHessian(Object instance = null, Map params, CallableWithArgs<T> callable) {
        doWithProxy(HessianProxy, instance, params, callable)
    }

    Object withBurlap(Object instance = null, Map params, Closure closure) {
        doWithProxy(BurlapProxy, instance, params, closure)
    }

    public <T> T withBurlap(Object instance = null, Map params, CallableWithArgs<T> callable) {
        doWithProxy(BurlapProxy, instance, params, callable)
    }
    
   // ======================================================

    private Object doWithProxy(Class klass, Object instance, Map params, Closure closure) {
        def proxy = configureProxy(klass, instance, params)

        if (closure) {
            closure.delegate = proxy
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            return closure()
        }
        return null
    }

    private <T> T doWithProxy(Class klass, Object instance, Map params, CallableWithArgs<T> callable) {
        def proxy = configureProxy(klass, instance, params)

        if (callable) {
            callable.args = [proxy] as Object[]
            return callable.run()
        }
        return null
    }

    private configureProxy(Class klass, Object instance, Map params) {
        def proxy = null
        if (params.id) {
            String id = params.remove('id').toString()
            if(instance != null) {
                MetaClass mc = ApplicationHolder.application.artifactManager.findGriffonClass(instance).metaClass
                if (mc.hasProperty(instance, id)) {
                    proxy = instance."$id"
                } else {
                    proxy = makeProxy(klass, params)
                    mc."$id" = proxy
                }
            } else {
                proxy = PROXIES[id]
                if(proxy == null) {
                    proxy = makeProxy(klass, params)
                    PROXIES[id] = proxy 
                }
            }
        } else {
            proxy = makeProxy(klass, params)
        }
        
        client
    }

    private makeProxy(Class klass, Map params) {
        def url = params.remove('url')
        if(!url) {
            throw new IllegalArgumentException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} proxy, url: parameter is null or invalid.")
        }

        def ctorArgs = CTOR_ARGS1
        def serviceClass = params.remove('service')
        if(!serviceClass) {
            throw new IllegalArgumentException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} proxy, service: parameter is null or invalid.")
        }
        if(!(serviceClass instanceof Class)) {
            serviceClass = serviceClass.toString()
            ctorArgs = CTOR_ARGS2
        }

        try {
            return klass.getDeclaredConstructor(ctorArgs).newInstance(url, serviceClass)
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} proxy, reason: $e", e)
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} proxy, reason: $e", e)
        }
    }
}
