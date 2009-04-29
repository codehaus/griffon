
/* Copyright 2006-2007 Graeme Rocher
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

package org.codehaus.griffon.commons.cfg;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.ResourceLoader
import org.apache.commons.logging.*
import org.codehaus.griffon.util.GriffonUtil
import org.codehaus.griffon.commons.ConfigurationHolder
import org.codehaus.griffon.commons.GriffonContext

/**
* Helper methods for initialising config object

* @author Graeme Rocher
* @since 1.0
*
* Created: Oct 9, 2007
*/
class ConfigurationHelper {

    private static final LOG = LogFactory.getLog(ConfigurationHelper)

    private static final String CONFIG_BINDING_USER_HOME = "userHome";
    private static final String CONFIG_BINDING_Griffon_HOME = "griffonHome";
    private static final String CONFIG_BINDING_APP_NAME = "appName";
    private static final String CONFIG_BINDING_APP_VERSION = "appVersion";

    static ConfigObject loadConfigFromClasspath(GriffonContext application=null) {

        ConfigSlurper configSlurper = new ConfigSlurper(GriffonUtil.getEnvironment())
        Map binding = new HashMap();

        // configure config slurper binding
        binding.put(CONFIG_BINDING_USER_HOME, System.getProperty("user.home"));
        binding.put(CONFIG_BINDING_Griffon_HOME, System.getProperty("griffon.home"));

        if (application) {
            binding.put(CONFIG_BINDING_APP_NAME, application.getMetadata().get("app.name"))
            binding.put(CONFIG_BINDING_APP_VERSION, application.getMetadata().get("app.version"))
        };


        configSlurper.setBinding(binding);
        ConfigObject co;
        ClassLoader classLoader = application != null ? application.getClassLoader() : ConfigurationHelper.getClassLoader()
        try {
            try {
                Class scriptClass = classLoader.loadClass(GriffonContext.CONFIG_CLASS);

                co = configSlurper.parse(scriptClass);
            } catch (ClassNotFoundException e) {
                LOG.debug("Could not find config class [" + GriffonContext.CONFIG_CLASS + "]. This is probably nothing to worry about, it is not required to have a config: " + e.getMessage());
                // ignore, it is ok not to have a configuration file
                co = new ConfigObject();
            }
            try {
                Class dataSourceClass = classLoader.loadClass(GriffonContext.DATA_SOURCE_CLASS);
                co.merge(configSlurper.parse(dataSourceClass));
            } catch (ClassNotFoundException e) {
                LOG.debug("Cound not find data source class [" + GriffonContext.DATA_SOURCE_CLASS + "]. This may be what you are expecting, but will result in Griffon loading with an in-memory database");
                // ignore
            }
        }
        catch (Throwable t) {
            GriffonUtil.deepSanitize(t)
            LOG.error("Error loading application Config: " + t.getMessage(),t)
            throw t
        }


        if (co == null) co = new ConfigObject();

        ConfigurationHolder.setConfig(co)
        initConfig(co, null, classLoader)
        return co
    }
    /**
     * Loads external configuration and merges with ConfigObject
     */
    static void initConfig(ConfigObject config, ResourceLoader resourceLoader = null, ClassLoader classLoader = null) {

        def resolver = resourceLoader ? new PathMatchingResourcePatternResolver(resourceLoader) : new PathMatchingResourcePatternResolver()

        def locations = config.griffon.config.locations
        if(locations) {
            for(location in locations) {
                if(location) {
                    try {
                        def resource = resolver.getResource(location)
                        def stream
                        try {
                            stream = resource.getInputStream()
                            ConfigSlurper configSlurper = new ConfigSlurper(GriffonUtil.getEnvironment())
                            configSlurper.setBinding(config) 
                            if(classLoader) {
                                if(classLoader instanceof GroovyClassLoader)
                                    configSlurper.classLoader = classLoader
                                else
                                    configSlurper.classLoader = new GroovyClassLoader(classLoader)                                
                            }
                            if(resource.filename.endsWith('.groovy')) {
                                def newConfig = configSlurper.parse(stream.text)
                                config.merge(newConfig)
                            }
                            else if(resource.filename.endsWith('.properties')) {
                                def props = new Properties()
                                props.load(stream)
                                def newConfig = configSlurper.parse(props)
                                config.merge(newConfig)
                            }
                       } finally {
                          stream?.close()
                       }

                } catch (Exception e) {
                        LOG.warn "Unable to load specified config location $location : ${e.message}"
                        LOG.debug "Unable to load specified config location $location : ${e.message}", e
                    }
                }
            }
        }

    }

}