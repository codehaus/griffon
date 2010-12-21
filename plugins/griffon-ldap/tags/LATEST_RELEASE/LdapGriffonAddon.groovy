/*
 * Copyright 2009-2010 the original author or authors.
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

import griffon.plugins.ldap.GriffonLdapSchemaClass
import org.codehaus.griffon.runtime.ldap.*

import griffon.core.GriffonApplication
import griffon.util.Environment

import gldapo.Gldapo

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Luke Daley
 * @author Andres Almiray
 */
class LdapGriffonAddon {
    private static final Logger log = LoggerFactory.getLogger('griffon.addon.ldap.LdapGriffonAddon')

    void addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new LdapSchemaArtifactHandler(app))
    }

    def doWithSpring = {
        gldapo(Gldapo)
    }

    def whenSpringReady = { app ->
        def config = mergeLdapClassesIntoConfig(app.artifactManager.ldapClasses, app.config.ldap)
        app.applicationContext.getBean('gldapo').consumeConfig(config)
    }

    def mergeLdapClassesIntoConfig(ldapClasses, config) {
        def mergedConfig = (config.size() > 0) ? config.clone() : [:]
        
        if (mergedConfig.schemas == null || mergedConfig.schemas instanceof ConfigObject) mergedConfig.schemas = []
        
        ldapClasses.clazz.each {
            if (mergedConfig.schemas.contains(it) == false) mergedConfig.schemas << it
        }
        
        mergedConfig
    }
}
