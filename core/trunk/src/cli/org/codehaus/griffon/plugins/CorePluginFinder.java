/*
 * Copyright 2004-2007 the original author or authors.
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

package org.codehaus.griffon.plugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.griffon.commons.GriffonContext;
import org.codehaus.griffon.plugins.exceptions.PluginException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Class with responsibility for loading core plugin classes.
 * Contains functionality moved in from <code>DefaultGriffonPluginManager</code>
 * @author Graeme Rocher
 * @author Phil Zoio
 */
public class CorePluginFinder {

    //This class contains functionality originally found in
    //DefaultGriffonPluginManager, but moved after 0.5.6

    private static final Log LOG = LogFactory.getLog(CorePluginFinder.class);

    private final PathMatchingResourcePatternResolver resolver;

    private final GriffonContext application;

    private final Set foundPluginClasses;

    public CorePluginFinder(GriffonContext application) {
        super();
        this.resolver = new PathMatchingResourcePatternResolver();
        this.application = application;
        this.foundPluginClasses = new HashSet();
    }

    public Set getPluginClasses() {

        // just in case we try to use this twice
        foundPluginClasses.clear();

        try {
            Resource[] resources = resolver
                    .getResources("classpath*:org/codehaus/groovy/griffon/**/plugins/**/*GriffonPlugin.class");
            if (resources.length > 0) {
                loadCorePluginsFromResources(resources);
            } else {
                LOG.warn("WARNING: Griffon was unable to load core plugins dynamically. This is normally a problem with the container class loader configuration, see troubleshooting and FAQ for more info. ");
                loadCorePluginsStatically();
            }
        } catch (IOException e) {
            throw new PluginException(
                    "I/O exception configuring core plug-ins: "
                            + e.getMessage(), e);
        }
        return foundPluginClasses;
    }

    private void loadCorePluginsStatically() {

        // This is a horrible hard coded hack, but there seems to be no way to
        // resolve .class files dynamically
        // on OC4J. If anyones knows how to fix this shout
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.CoreGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.LoggingGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.CodecsGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.i18n.I18nGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.datasource.DataSourceGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.DomainClassGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.web.ServletsGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.web.ControllersGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.web.mapping.UrlMappingsGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.web.filters.FiltersGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.web.mimes.MimeTypesGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.webflow.WebFlowGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.orm.hibernate.HibernateGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.services.ServicesGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.converters.ConvertersGriffonPlugin");
//        loadCorePlugin("org.codehaus.groovy.griffon.plugins.scaffolding.ScaffoldingGriffonPlugin");
    }

    private void loadCorePluginsFromResources(Resource[] resources)
            throws IOException {

        LOG.debug("Attempting to load [" + resources.length + "] core plugins");
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            String url = resource.getURL().toString();
            int packageIndex = url.indexOf("org/codehaus/groovy/griffon");
            url = url.substring(packageIndex, url.length());
            url = url.substring(0, url.length() - 6);
            String className = url.replace('/', '.');

            loadCorePlugin(className);
        }
    }

    private Class attemptCorePluginClassLoad(String pluginClassName) {
        try {
            return application.getClassLoader().loadClass(pluginClassName);
        } catch (ClassNotFoundException e) {
            LOG.warn("[GriffonPluginManager] Core plugin [" + pluginClassName
                    + "] not found, resuming load without..");
            if (LOG.isDebugEnabled())
                LOG.debug(e.getMessage(), e);
        }
        return null;
    }

    private void loadCorePlugin(String pluginClassName) {
        Class pluginClass = attemptCorePluginClassLoad(pluginClassName);

        if (pluginClass != null) {
            addPlugin(pluginClass);
        }
    }

    private void addPlugin(Class plugin) {
        foundPluginClasses.add(plugin);
    }

}
