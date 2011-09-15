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

package griffon.plugins.rest

import griffon.util.ApplicationHolder
import griffon.util.RunnableWithArgs
import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import java.util.concurrent.ConcurrentHashMap

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
@Singleton
class RestConnector { 
    private final Map BUILDERS = new ConcurrentHashMap()
    
    static void enhance(MetaClass mc, Object instance = null) {
        mc.withAsyncHttp = {Map args, Closure closure ->
            RestConnector.instance.withAsyncHttp(instance, args, closure)   
        }
        mc.withAsyncHttp << {Map args, RunnableWithArgs runnable ->
            RestConnector.instance.withAsyncHttp(instance, args, runnable)   
        }
        mc.withHttp = {Map args, Closure closure ->
            RestConnector.instance.withHttp(instance, args, closure)   
        }
        mc.withHttp << {Map args, RunnableWithArgs runnable ->
            RestConnector.instance.withHttp(instance, args, runnable)   
        }
        mc.withRest = {Map args, Closure closure ->
            RestConnector.instance.withRest(instance, args, closure)   
        }
        mc.withRest << {Map args, RunnableWithArgs runnable ->
            RestConnector.instance.withRest(instance, args, runnable)   
        }        
    }
    
    // ======================================================
    
    def withAsyncHttp(Object instance, Map params, Closure closure) {
        doWithBuilder(AsyncHTTPBuilder, instance, params, closure)
    }
       
    def withHttp(Object instance, Map params, Closure closure) {
        doWithBuilder(HTTPBuilder, instance, params, closure)
    } 
       
    def withRest(Object instance, Map params, Closure closure) {
        doWithBuilder(RESTClient, instance, params, closure)
    }
    
    void withAsyncHttp(Object instance, Map params, RunnableWithArgs runnable) {
        doWithBuilder(AsyncHTTPBuilder, instance, params, runnable)
    } 
  
    void withHttp(Object instance, Map params, RunnableWithArgs runnable) {
        doWithBuilder(HTTPBuilder, instance, params, runnable)
    } 

    void withRest(Object instance, Map params, RunnableWithArgs runnable) {
        doWithBuilder(RESTClient, instance, params, runnable)
    }

    // ======================================================
    // context free versions of previous definitions, i.e,
    // the builder will not be saved in a local variable
    // attached to a MetaClass
    // ======================================================
    
    def withAsyncHttp(Map params, Closure closure) {
        doWithBuilder(AsyncHTTPBuilder, null, params, closure)
    }
       
    def withHttp(Map params, Closure closure) {
        doWithBuilder(HTTPBuilder, null, params, closure)
    } 
       
    def withRest(Map params, Closure closure) {
        doWithBuilder(RESTClient, null, params, closure)
    }
    
    void withAsyncHttp(Map params, RunnableWithArgs runnable) {
        doWithBuilder(AsyncHTTPBuilder, null, params, runnable)
    } 
  
    void withHttp(Map params, RunnableWithArgs runnable) {
        doWithBuilder(HTTPBuilder, null, params, runnable)
    } 

    void withRest(Map params, RunnableWithArgs runnable) {
        doWithBuilder(RESTClient, null, params, runnable)
    }

    // ======================================================

    private doWithBuilder(Class klass, Object instance, Map params, Closure closure) {
        def builder = configureBuilder(klass, instance, params)

        if (closure) {
            closure.delegate = builder
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()
        }
    }

    private void doWithBuilder(Class klass, Object instance, Map params, RunnableWithArgs runnable) {
        def builder = configureBuilder(klass, instance, params)

        if (runnable) {
            runnable.args = [builder] as Object[]
            runnable.run()
        }
    }

    private configureBuilder(Class klass, Object instance, Map params) {
        def builder = null
        if (params.id) {
            String id = params.remove('id').toString()
            if(instance != null) {
                MetaClass mc = ApplicationHolder.application.artifactManager.findGriffonClass(instance).metaClass
                if (mc.hasProperty(instance, id)) {
                    builder = instance."$id"
                } else {
                    builder = makeBuilder(klass, params)
                    mc."$id" = builder
                }
            } else {
                builder = BUILDERS[id]
                if(builder == null) {
                    builder = makeBuilder(klass, params)
                    BUILDERS[id] = builder 
                }
            }
        } else {
            builder = makeBuilder(klass, params)
        }

        if (params.containsKey('proxy')) {
            Map proxyArgs = [scheme: 'http', port: 80] + params.remove('proxy')
            if (!proxyArgs.host) throw new IllegalArgumentException('proxy.host cannot be null!')
            builder.setProxy(proxyArgs.host, proxyArgs.port as int, proxyArgs.scheme)
        }
        
        builder
    }

    private makeBuilder(Class klass, Map params) {
        if (klass == AsyncHTTPBuilder) {
            try {
                Map args = [:]
                ['threadPool', 'poolSize', 'uri', 'contentType', 'timeout'].each { arg ->
                    if (params[(arg)] != null) args[(arg)] = params[(arg)]
                }
                return klass.newInstance(args)
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to create async http builder, reason: $e", e)
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException("Failed to create async http builder, reason: $e", e)
            }
        }
        try {
            def builder = klass.newInstance()
            if (params.uri) builder.uri = params.remove('uri')
            if (params.contentType) builder.contentType = params.remove('contentType')
            return builder
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create ${(klass == HTTPBuilder ? 'http' : 'rest')} builder, reason: $e", e)
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to create ${(klass == HTTPBuilder ? 'http' : 'rest')} builder, reason: $e", e)
        }
    }
}
