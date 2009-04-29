/*
 * Copyright 2004-2005 the original author or authors.
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

import groovy.lang.GroovyObjectSupport;
import groovy.util.slurpersupport.GPathResult;
//import org.codehaus.griffon.commons.AbstractGriffonClass;
import org.codehaus.griffon.commons.GriffonContext;
import org.codehaus.griffon.commons.GriffonClassUtils;
import org.codehaus.griffon.commons.AbstractGriffonClass;
//import org.codehaus.griffon.commons.spring.RuntimeSpringConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation that provides some default behaviours
 *
 * @author Graeme Rocher
 *
 */
public abstract class AbstractGriffonPlugin extends GroovyObjectSupport implements GriffonPlugin {

	/* (non-Javadoc)
	 * @see org.codehaus.groovy.griffon.plugins.GriffonPlugin#refresh()
	 */
	public void refresh() {
		// do nothing
	}

	protected GriffonContext application;
	protected String version = "1.0";
	protected Map dependencies = new HashMap();
	protected String[] dependencyNames = new String[0];
	protected Class pluginClass;
	protected ApplicationContext applicationContext;
	protected GriffonPluginManager manager;
	protected String[] evictionList = new String[0];

	/**
	 * Wrapper Griffon class for plugins
	 *
	 * @author Graeme Rocher
	 *
	 */
	class GriffonPluginClass extends AbstractGriffonClass {
		public GriffonPluginClass(Class clazz) {
			super(clazz, TRAILING_NAME);
		}

	}

	public AbstractGriffonPlugin(Class pluginClass, GriffonContext application) {
		if(pluginClass == null) {
			throw new IllegalArgumentException("Argument [pluginClass] cannot be null");
		}
		if(!pluginClass.getName().endsWith(TRAILING_NAME)) {
			throw new IllegalArgumentException("Argument [pluginClass] with value ["+pluginClass+"] is not a Griffon plugin (class name must end with 'GriffonPlugin')");
		}
		this.application = application;
		this.pluginClass = pluginClass;
	}

	public abstract void doWithApplicationContext(ApplicationContext applicationContext);


//	public abstract void doWithRuntimeConfiguration(RuntimeSpringConfiguration springConfig);

//	public abstract void doArtefactConfiguration();

//	public boolean checkForChanges() {
//	    return false;
//	}

	public void doWithWebDescriptor(GPathResult webXml) {
		// do nothing
	}
	public String[] getDependencyNames() {
		return this.dependencyNames;
	}

	public String getDependentVersion(String name) {
		return null;
	}

	public String getName() {
		return pluginClass.getName();
	}

	public String getVersion() {
		return this.version;
	}

    public String getPluginPath() {
        return PLUGINS_PATH + '/' + GriffonClassUtils.getScriptName(getName()) + '-' + getVersion();
    }

    public GriffonPluginManager getManager() {
		return this.manager;
	}

	public String[] getLoadAfterNames() {
		return new String[0];
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/* (non-Javadoc)
	 * @see org.codehaus.groovy.griffon.plugins.GriffonPlugin#setManager(org.codehaus.groovy.griffon.plugins.GriffonPluginManager)
	 */
	public void setManager(GriffonPluginManager manager) {
		this.manager = manager;
	}
	/* (non-Javadoc)
	 * @see org.codehaus.groovy.griffon.plugins.GriffonPlugin#setApplication(org.codehaus.groovy.griffon.commons.GriffonContext)
	 */
	public void setApplication(GriffonContext application) {
		this.application = application;
	}
	public String[] getEvictionNames() {
		return this.evictionList;
	}


}
