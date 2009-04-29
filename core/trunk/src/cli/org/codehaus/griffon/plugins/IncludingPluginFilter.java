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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of <code>PluginFilter</code> which ensures that only the supplied
 * plugins (identified by name) as well as their dependencies are included in the filtered plugin list
 * @author Phil Zoio
 */
public class IncludingPluginFilter extends BasePluginFilter {
	
	public IncludingPluginFilter(Set included) {
		super(included);
	}

	public IncludingPluginFilter(String[] included) {
		super(included);
	}

	protected List getPluginList(List original, List pluginList) {
		List newList = new ArrayList();
		newList.addAll(pluginList);
		return newList;
	}

	protected void addPluginDependencies(List additionalList, GriffonPlugin plugin) {
		String[] dependencyNames = plugin.getDependencyNames();
		for (int j = 0; j < dependencyNames.length; j++) {
			String name = dependencyNames[j];
			GriffonPlugin p = getNamedPlugin(name);
			registerDependency(additionalList, p);
		}
	}
}
