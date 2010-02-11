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

package griffon.domain

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Andres Almiray
 */
@Singleton
class DomainClassEnhancer {
    private static DOMAIN_INITIALIZERS = new ConcurrentHashMap()
    private static initializeDomain(Class c) {
        synchronized(c) {
             // enhance domain class only once, initializer is removed after calling
             DOMAIN_INITIALIZERS.remove(c)?.call()
        }
    }

    final enhance(GriffonApplication app, DomainClassEnhancerDelegate enhancer) {
        def lazyInit = {ArtifactInfo dc ->
            registerDynamicMethods(dc, app, enhancer)
            MetaClass emc = GroovySystem.metaClassRegistry.getMetaClass(dc.klass)
        }

        def initializeDomainOnceClosure = {ArtifactInfo dc ->
            initializeDomain(dc.klass)
        }

        app.artifactManager.domainArtifacts.each {dc ->
            MetaClass mc = dc.klass.metaClass
            DOMAIN_INITIALIZERS[dc.klass] = lazyInit.curry(dc)
            def initDomainClassOnce = initializeDomainOnceClosure.curry(dc)
            // these need to be eagerly initialised here, otherwise Groovy's findAll from the DGM is called
            if(providesDynamicMethod(DynamicMethod.FIND_ALL, enhancer)) {
                def findAllMethod = fetchDynamicMethod(DynamicMethod.FIND_ALL, enhancer, app, dc)
                mc.static.findAll = {->
                    findAllMethod.invoke(dc.klass, 'findAll', [] as Object[])
                }
                mc.static.findAll = {Object example ->
                    findAllMethod.invoke(dc.klass, 'findAll', [example] as Object[])
                }
                // mc.static.findAll = {Object example, Map args -> findAllMethod.invoke(dc.klass, 'findAll', [example, args] as Object[])}
            }

            mc.methodMissing = { String name, args ->
                initDomainClassOnce()
                mc.invokeMethod(delegate, name, args)
            }
            mc.static.methodMissing = {String name, args ->
                initDomainClassOnce()
                def result
                if(delegate instanceof Class) {
                    result = mc.invokeStaticMethod(delegate, name, args)
                } else {
                    result = mc.invokeMethod(delegate, name, args)
                }
                result
            }
        }
    }

    private static providesDynamicMethod(DynamicMethod method, DomainClassEnhancerDelegate enhancer) {
        MetaClass mc = enhancer.metaClass
        mc.hasProperty(enhancer, method.methodName)
    }

    private static fetchDynamicMethod(DynamicMethod method, DomainClassEnhancerDelegate enhancer, GriffonApplication app, ArtifactInfo dc) {
        return enhancer."${method.methodName}"(app, dc)
    }

    private static registerDynamicMethods(ArtifactInfo dc, GriffonApplication app, DomainClassEnhancerDelegate enhancer) {
        addBasicPersistenceMethods(dc, app, enhancer)
        addQueryMethods(dc, app, enhancer)
        addDynamicFinderSupport(dc, app, enhancer)
        enhancer.enhance(dc, app)
    }

    private static addBasicPersistenceMethods(ArtifactInfo dc, GriffonApplication app, DomainClassEnhancerDelegate enhancer) {
        def mc = dc.klass.metaClass

        def makeMethod = fetchDynamicMethod(DynamicMethod.MAKE, enhancer, app, dc)
        mc.static.make = {->
            makeMethod.invoke(delegate, 'make', [] as Object[])
        }
        mc.static.make = {Map args->
            makeMethod.invoke(delegate, 'make', [args] as Object[])
        }
    
        if(providesDynamicMethod(DynamicMethod.SAVE, enhancer)) {
            def saveMethod = fetchDynamicMethod(DynamicMethod.SAVE, enhancer, app, dc)
            mc.save = {Boolean validate ->
                saveMethod.invoke(delegate, 'save', [validate] as Object[])
            }
            mc.save = {Map args ->
                saveMethod.invoke(delegate, 'save', [args] as Object[])
            }
            mc.save = {->
                saveMethod.invoke(delegate, 'save', [] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.DELETE, enhancer)) {
            def deleteMethod = fetchDynamicMethod(DynamicMethod.DELETE, enhancer, app, dc)
            mc.delete = {->
                deleteMethod.invoke(delegate, 'delete', [] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.COUNT, enhancer)) {
            def countMethod = fetchDynamicMethod(DynamicMethod.COUNT, enhancer, app, dc)
            mc.static.count = {->
                countMethod.invoke(delegate, 'count', [] as Object[])
            }
        }
    
        if(providesDynamicMethod(DynamicMethod.FETCH, enhancer)) {
            def fetchMethod = fetchDynamicMethod(DynamicMethod.FETCH, enhancer, app, dc)
            mc.static.fetch = {Object arg->
                fetchMethod.invoke(delegate, 'fetch', [arg] as Object[])
            }
        }
    }

    private static addQueryMethods(ArtifactInfo dc, GriffonApplication app, DomainClassEnhancerDelegate enhancer) {
        def mc = dc.klass.metaClass

        if(providesDynamicMethod(DynamicMethod.FIND_ALL, enhancer)) {
            def findAllMethod = fetchDynamicMethod(DynamicMethod.FIND_ALL, enhancer, app, dc)
            mc.static.findAll = {String query ->
                findAllMethod.invoke(dc.klass, 'findAll', [query] as Object[])
            }
            mc.static.findAll = {String query, Collection positionalParams ->
                findAllMethod.invoke(dc.klass, 'findAll', [query, positionalParams] as Object[])
            }
            mc.static.findAll = {String query, Collection positionalParams, Map paginateParams ->
                findAllMethod.invoke(dc.klass, 'findAll', [query, positionalParams, paginateParams] as Object[])
            }
            mc.static.findAll = {String query, Map namedArgs ->
                findAllMethod.invoke(dc.klass, 'findAll', [query, namedArgs] as Object[])
            }
            mc.static.findAll = {String query, Map namedArgs, Map paginateParams ->
                findAllMethod.invoke(dc.klass, 'findAll', [query, namedArgs, paginateParams] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.FIND, enhancer)) {
            def findMethod = fetchDynamicMethod(DynamicMethod.FIND, enhancer, app, dc)
            mc.static.find = {String query ->
                findMethod.invoke(dc.klass, 'find', [query] as Object[])
            }
            mc.static.find = {String query, Collection args ->
                findMethod.invoke(dc.klass, 'find', [query, args] as Object[])
            }
            mc.static.find = {String query, Map namedArgs ->
                findMethod.invoke(dc.klass, 'find', [query, namedArgs] as Object[])
            }
            mc.static.find = {Object example ->
                findMethod.invoke(dc.klass, 'find', [example] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.EXECUTE_QUERY, enhancer)) {
            def executeQueryMethod = fetchDynamicMethod(DynamicMethod.EXECUTE_QUERY, enhancer, app, dc)
            mc.static.executeQuery = {String query ->
                executeQueryMethod.invoke(dc.klass, 'executequery', [query] as Object[])
            }
            mc.static.executeQuery = {String query, Collection positionalParams ->
                executeQueryMethod.invoke(dc.klass, 'executequery', [query, positionalParams] as Object[])
            }
            mc.static.executeQuery = {String query, Collection positionalParams, Map paginateParams ->
                executeQueryMethod.invoke(dc.klass, 'executequery', [query, positionalParams, paginateParams] as Object[])
            }
            mc.static.executeQuery = {String query, Map namedParams ->
                executeQueryMethod.invoke(dc.klass, 'executequery', [query, namedParams] as Object[])
            }
            mc.static.executeQuery = {String query, Map namedParams, Map paginateParams ->
                executeQueryMethod.invoke(dc.klass, 'executequery', [query, namedParams, paginateParams] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.LIST, enhancer)) {
            def listMethod = fetchDynamicMethod(DynamicMethod.LIST, enhancer, app, dc)
            mc.static.list = {->
                listMethod.invoke(dc.klass, 'list', [] as Object[])
            }
            mc.static.list = {Map args ->
                listMethod.invoke(dc.klass, 'list', [args] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.FIND_WHERE, enhancer)) {
            def findWhereMethod = fetchDynamicMethod(DynamicMethod.FIND_WHERE, enhancer, app, dc)
            mc.static.findWhere = {Map args ->
                Map queryArgs = filterQueryArgumentMap(query)
                findWhereMethod.invoke(dc.klass, 'findWhere', [queryArgs] as Object[])
            }
        }

        if(providesDynamicMethod(DynamicMethod.FIND_ALL_WHERE, enhancer)) {
            def findWhereAllMethod = fetchDynamicMethod(DynamicMethod.FIND_ALL_WHERE, enhancer, app, dc)
            mc.static.findAllWhere = {Map args ->
                Map queryArgs = filterQueryArgumentMap(query)
                findWhereAllMethod.invoke(dc.klass, 'findAllWhere', [queryArgs] as Object[])
            }
        }
    }

    private static addDynamicFinderSupport(ArtifactInfo dc, GriffonApplication app, DomainClassEnhancerDelegate enhancer) {
/*
        def mc = dc.klass.metaClass

        def dynamicMethods = []
        if(providesDynamicMethod(DynamicMethod.FIND_BY, enhancer)) {
            dynamicMethods << fetchDynamicMethod(DynamicMethod.FIND_BY, enhancer, app, dc)
        }
        if(providesDynamicMethod(DynamicMethod.FIND_ALL_BY, enhancer)) {
            dynamicMethods << fetchDynamicMethod(DynamicMethod.FIND_ALL_BY, enhancer, app, dc)
        }
        if(providesDynamicMethod(DynamicMethod.COUNT_BY, enhancer)) {
            dynamicMethods << fetchDynamicMethod(DynamicMethod.COUNT_BY, enhancer, app, dc)
        }

        // This is the code that deals with dynamic finders. It looks up a static method, if it exists it invokes it
        // otherwise it trys to match the method invocation to one of the dynamic methods. If it matches it will
        // register a new method with the ExpandoMetaClass so the next time it is invoked it doesn't have this overhead.
        mc.static.methodMissing = {String methodName, args ->
            def result = null            
            def method = dynamicMethods.find {it.isMethodMatch(methodName)}
            if(method) {
                // register the method invocation for next time
                synchronized(this) {
                    mc.static."$methodName" = {List varArgs ->
                        method.invoke(dc.klass, methodName, varArgs)
                    }
                }
                result = method.invoke(dc.klass, methodName, args)
            } else {
                throw new MissingMethodException(methodName, delegate, args)
            }
            result
        }
*/
    }

    static Map filterQueryArgumentMap(Map query) {
        def queryArgs = [:]
        for (entry in query) {
            if (entry.value instanceof CharSequence) {
                queryArgs[entry.key] = entry.value.toString()
            }
            else {
                queryArgs[entry.key] = entry.value
            }
        }
        return queryArgs
    }
}
