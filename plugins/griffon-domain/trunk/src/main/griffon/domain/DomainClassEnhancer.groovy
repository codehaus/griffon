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

/**
 * @author Andres Almiray
 */
@Singleton
class DomainClassEnhancer {
    final enhance(GriffonApplication app, DomainClassEnhancerDelegate enhancerDelegate) {
        Map methods = [
            "static": [
                make: { Map args = [:] ->
                    def instance = app.newInstance(app, delegate, DomainClassArtifactEnhancer.TYPE)
                    args?.each { k, v ->
                        try {
                            instance[k] = v
                        } catch(MissingPropertyException mpe) {
                            // ignore ??
                        }
                    }
                    return instance
                },
                count: { ->
                    enhancerDelegate.count() ?: 0
                },
                list: { ->
                    enhancerDelegate.list() ?: []
                },
                findAllWhere: { Map args ->
                    enhancerDelegate.findAllWhere(args) ?: []
                },
                findWhere: { Map args ->
                    enhancerDelegate.findWhere(args)
                },
                methodMissing: { String methodName, args ->
                    def propertyName = methodName =~ /^findAllBy(\w+)$/
                    if(propertyName) {
                        normalizePropertyName(propertyName)
                        return enhancerDelegate.findAllBy(delegate.getClass(), propertyName, args, methodName)
                    }
                    propertyName = methodName =~ /^findBy(\w+)$/
                    if(propertyName){
                        normalizePropertyName(propertyName)
                        return enhancerDelegate.findBy(delegate.getClass(), propertyName, args, methodName)
                    }
                    propertyName = methodName =~ /^countBy(\w+)$/
                    if(propertyName){
                        normalizePropertyName(propertyName)
                        return enhancerDelegate.countBy(delegate.getClass(), propertyName, args, methodName)
                    }
                    throw new MissingMethodException(methodName, Object, args)
                }
            ],
            save: { -> enhancerDelegate.saveOrUpdate(delegate) },
            delete: { -> enhancerDelegate.delete(delegate) }
        ]

        app.artifactManager.domainArtifacts.each { domain ->
            enhance(domain.klass, methods)
        }
    }

    // ----------------------------------------------------------

    private normalizePropertyName(propertyName) {
        propertyName = propertyName[0][1]
        return propertyName[0].toLowerCase() + propertyName[1..-1]
    }

    private static final String ENHANCED = "_ENHANCED_METACLASS_"

    private boolean hasBeenEnhanced(Class klass) {
        MetaClassRegistry mcr = GroovySystem.metaClassRegistry
        MetaClass mc = mcr.getMetaClass(klass)
        if(!(mc instanceof ExpandoMetaClass)) return false
        return mc.hasMetaProperty(ENHANCED)
    }

    private void enhance(Class klass, Map enhancedMethods) {
        MetaClassRegistry mcr = GroovySystem.metaClassRegistry
        MetaClass mc = mcr.getMetaClass(klass)
        boolean init = false
        if(!(mc instanceof ExpandoMetaClass) ||
            (mc instanceof ExpandoMetaClass && !mc.isModified())) {
            mcr.removeMetaClass klass
            mc = new ExpandoMetaClass(klass, true, true)
            init = true
        }
        // if mc is an EMC that was initialized previously
        // with additional methods/properties and it does
        // not allow modifications after init, then the next
        // block will throw an exception
        enhancedMethods.each {methodName, method ->
            if(methodName == "static") {
                method.each { staticMethodName, staticMethod ->
                    if (mc.getMetaMethod(staticMethodName) == null) {
                        mc.registerStaticMethod(staticMethodName, staticMethod)
                    }
                }
            } else {
                if (mc.getMetaMethod(methodName) == null) {
                    mc.registerInstanceMethod(methodName, method)
                }
            }
        }
        mc.registerBeanProperty(ENHANCED,true)
        if (init) {
            mc.initialize()
            mcr.setMetaClass(klass, mc)
        }
    }
}
