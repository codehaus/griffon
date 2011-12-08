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

package griffon.plugins.wsclient

import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import groovyx.net.ws.*
import groovyx.net.ws.cxf.*
import java.util.concurrent.ConcurrentHashMap

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
@Singleton
class WsclientConnector { 
    private final Map BUILDERS = new ConcurrentHashMap()
    
    static void enhance(MetaClass mc, Object instance = null) {
        mc.withWs = {Map args, Closure closure ->
            WsclientConnector.instance.withWs(instance, args, closure)   
        }
        mc.withWs << {Map args, CallableWithArgs callable ->
            WsclientConnector.instance.withWs(instance, args, callable)   
        }      
    }
    
    // ======================================================
    
    Object withWs(Object instance = null, Map params, Closure closure) {
        return doWithClient(instance, params, closure)
    }
    
    public <T> T withWs(Object instance = null, Map params, CallableWithArgs<T> callable) {
        return doWithClient(instance, params, callable)
    } 

    // ======================================================

    private Object doWithClient(Object instance, Map params, Closure closure) {
        def client = configureClient(instance, params)

        if (closure) {
            closure.delegate = client
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            return closure()
        }
        return null
    }

    private <T> T doWithClient(Object instance, Map params, CallableWithArgs<T> callable) {
        def client = configureClient(instance, params)

        if (callable) {
            callable.args = [client] as Object[]
            return callable.run()
        }
        return null
    }

    private configureClient(Object instance, Map params) {
        def client = null
        if (params.id) {
            String id = params.remove('id').toString()
            if(instance != null) {
                MetaClass mc = ApplicationHolder.application.artifactManager.findGriffonClass(instance).metaClass
                if (mc.hasProperty(instance, id)) {
                    client = instance."$id"
                } else {
                    client = makeClient(params)
                    mc."$id" = client
                }
            } else {
                client = BUILDERS[id]
                if(client == null) {
                    client = makeClient(params)
                    BUILDERS[id] = client 
                }
            }
        } else {
            client = makeClient(params)
        }

        if(params.containsKey('proxy')) client.setProxyProperties(params.remove('proxy'))
        if(params.containsKey('ssl')) client.setSSLProperties(params.remove('ssl'))
        if(params.containsKey('timeout')) client.setConnectionTimeout(params.remove('timeout'))
        if(params.containsKey('mtom')) client.setMtom(params.remove('mtom'))
        if(params.containsKey('basicAuth')) {
            Map basicAuth = params.remove('basicAuth')
            client.setBasicAuthentication(basicAuth.username ?: '', basicAuth.password ?: '')
        }
              
        client
    }

    private makeClient(Map params) {
        def wsdl = params.remove('wsdl')
        def classLoader = params.remove('classLoader') ?: getClass().classLoader
        if(!wsdl) {
            throw new RuntimeException("Failed to create ws client, wsdl: parameter is null or invalid.")
        }
        try {
            def soapVersion = params.remove('soapVersion') ?: '1.1'
            switch(soapVersion) {
                case '1.1': soapVersion = SoapVersion.SOAP_1_1; break
                case '1.2': soapVersion = SoapVersion.SOAP_1_2; break
                default: throw new IllegalArgumentException("Invalid soapVersion: value. Must be either '1.1' or '1.2'")
            }
            def wsclient = new WSClient(wsdl, classLoader, soapVersion)
            wsclient.initialize()
            return wsclient
        } catch(Exception e) {
            throw new RuntimeException("Failed to create ws client, reason: $e", e)
        }
    }
}
