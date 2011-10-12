/*
 * Copyright 2011 the original author or authors.
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
 
package org.metawidget.inspector.impl.propertystyle.groovy;

import griffon.core.GriffonClass;

/**
 * @author Andres Almiray
 */
public class GriffonArtifactPropertyStyle extends GroovyPropertyStyle {
	/**
	 * Whether to exclude the given property name when searching for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes GriffonClass.STANDARD_PROPERTIES, 'propertyChangeListeners' and 'vetoableChangeListeners'.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */
	@Override
	protected boolean isExcludedName(String name) {
		if (GriffonClass.STANDARD_PROPERTIES.contains(name)) {
			return true;
		}
		
		if ("propertyChangeListeners".equals(name)) {
			return true;
		}
		
		if ("vetoableChangeListeners".equals(name)) {
			return true;
		}

		return super.isExcludedName(name);
	}
}
