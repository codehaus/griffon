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

import org.springframework.jmx.export.assembler.MethodExclusionMBeanInfoAssembler
import org.springframework.jmx.support.MBeanServerFactoryBean
import org.springframework.jmx.export.MBeanExporter

import griffon.util.GriffonClassUtils as GCU

/**
 * Based on griffon-jmx plugin by Ken Sipe.
 *
 * @author Andres Almiray
 */
class JmxGriffonAddon {
    def doWithSpring = {
        mbeanServer(MBeanServerFactoryBean) {
            locateExistingServerIfPossible = true
        }
        exporter(MBeanExporter) {
             server = ref(mbeanServer)
             beans = [:]
        }
    }

    def whenSpringReady = { app ->
        def ctx = app.applicationContext
        def domain = "${app.metadata['app.name']}"
        
        MBeanExporter exporter = ctx.getBean('exporter')
        
        // exporting mbeans
        exportServices(exporter, domain, ctx)
        exportAddonBeans(exporter, domain, ctx)
        exportConfiguredObjects(exporter, domain, ctx)
        registerMBeans(exporter)
    }

    private def exportAddonBeans(MBeanExporter exporter, domain, ctx) {
        app.addons.each { name, addon ->
            def addonMetaClass = addon.metaClass
            def exportWithJmx = addonMetaClass.getMetaProperty('exportWithJmx')
            if(exportWithJmx) {
                def export = addon.exportWithJmx
                if(export instanceof Closure) {
                    export(exporter, domain, ctx)
                }
            }
        }
    }

    private def exportServices(MBeanExporter exporter, domain, ctx) {
        Properties excludeMethods = new Properties()
        
        app.artifactManager.serviceClasses?.each { serviceClass ->
            def serviceClazz = serviceClass.clazz
        
            exportClass(exporter, domain, ctx, serviceClazz, serviceClass.propertyName, serviceClass.propertyName, excludeMethods, 'service')
        }

        handleExcludeMethods(exporter, excludeMethods)
    }
    
    private handleExcludeMethods(MBeanExporter exporter, excludeMethods) {
        if (excludeMethods.size() > 0) {
            def assembler = new MethodExclusionMBeanInfoAssembler();
            assembler.setIgnoredMethodMappings(excludeMethods)
            exporter.setAssembler(assembler)
        }
    }

    private exportClass(MBeanExporter exporter, domain, ctx, serviceClass, serviceName, propertyName, excludeMethods, type) {
        def objectName = "$type=$serviceName,type=$type"

        def exposeList = GCU.getStaticPropertyValue(serviceClass, 'expose')
        def exposeMap = GCU.getStaticPropertyValue(serviceClass, 'jmxexpose')
        // def scope = GCU.getStaticPropertyValue(serviceClass, 'scope')
        
        def jmxExposed = exposeList?.find { it.startsWith('jmx') }
        // boolean singleton = (scope == null || scope != 'singleton')
        
        if (jmxExposed /*&& singleton*/) {
            // change service name if provided by jmx:objectname
            def m = jmxExposed =~ 'jmx:(.*)'
            if(m) {
                objectName = "${m[0][1]}"
            }
            if (exposeMap != null && exposeMap['excludeMethods']) {
                excludeMethods.setProperty("${domain}:${objectName}", exposeMap['excludeMethods'])
            }
            
            exporter.beans."${domain}:${objectName}" = ctx.getBean(propertyName)
        } 
    }

    private exportConfiguredObjects(MBeanExporter exporter, domain, ctx) {
        // example config: 
        /*
             griffon {
                 jmx {
                     exportBeans = ['myBeanOne', 'myBeanTwo']
                 }
             }
        */
        def configuredObjectBeans = app.config?.griffon?.jmx?.exportBeans

        if(configuredObjectBeans) {
            if(configuredObjectBeans instanceof String) {
                // allow list or single class, e.g.
                //     exportBeans = ['myBeanOne', 'myBeanTwo']
                //      ... or ...
                //      exportBeans = 'myBeanOne'
                
                configuredObjectBeans = [configuredObjectBeans]
            }
            
            Properties excludeMethods = new Properties()
            
            configuredObjectBeans.each { jmxBeanName ->
                def bean = ctx.getBean(jmxBeanName)
                def jmxServiceClass = bean.class
                def serviceName = jmxServiceClass.simpleName

                exportClass(exporter, domain, ctx, jmxServiceClass, serviceName, jmxBeanName, excludeMethods, "utility")
            }

            handleExcludeMethods(exporter, excludeMethods)
        }
    }

    private def registerMBeans(MBeanExporter exporter) {
        exporter.unregisterBeans()
        exporter.registerBeans()
    }
}
