/* 
 * Copyright 2004-2010 the original author or authors.
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
package org.codehaus.griffon.runtime.domain;

// import org.springframework.validation.Validator;

import griffon.core.GriffonApplication;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainClassProperty;
import griffon.util.GriffonClassUtils;
import java.beans.PropertyDescriptor;
import org.codehaus.griffon.runtime.core.DefaultGriffonClass;

import groovy.lang.Closure;
import groovy.lang.MetaProperty;
import groovy.lang.MetaMethod;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.lang.reflect.Method;

public class DefaultGriffonDomainClass extends DefaultGriffonClass implements GriffonDomainClass {
    public DefaultGriffonDomainClass(GriffonApplication app, Class<?> clazz) {
        super(app, clazz, TYPE, TRAILING);
    }

    public void resetCaches() {
	    super.resetCaches();
    }

    public boolean isOwningClass(Class domainClass) {
	    return false;
    }

    public GriffonDomainClassProperty[] getProperties() {
	    return null;
    }

    public GriffonDomainClassProperty[] getPersistentProperties() {
	    return null;
    }

    public GriffonDomainClassProperty getIdentifier() {
	    return null;
    }

    public GriffonDomainClassProperty getVersion() {
	    return null;
    }
    
    public Map getAssociationMap() {
	    return null;
    }
    
    public GriffonDomainClassProperty getPropertyByName(String name) {
	    return null;
    }    
    
    public String getFieldName(String propertyName) {
	    return null;
    }
    
    public boolean isOneToMany(String propertyName) {
	    return false;
    }
    
    public boolean isManyToOne(String propertyName) {
	    return false;
    }
    
    public boolean isBidirectional(String propertyName) {
	    return false;
    }
    
    public Class getRelatedClassType(String propertyName) {
	    return null;
    }

    public Map getConstrainedProperties() {
	    return null;
    }

    /**
     * Retreives the validator for this domain class 
     * 
     * @return A validator instance or null if none exists
     */
//    Validator getValidator();

    /**
     * Sets the validator for this domain class 
     * 
     * @param validator The domain class validator to set
     */
//    void setValidator(Validator validator);
    
    public String getMappingStrategy() {
	    return null;
    }
    
    public boolean isRoot() {
	    return false;
    }
    
    public Set<GriffonDomainClass> getSubClasses() {
	    return null;
    }

    public void refreshConstraints() {}

    public boolean hasSubClasses() {
	    return false;
    }

    public Map getMappedBy() {
	    return null;
    }

    public boolean hasPersistentProperty(String propertyName) {
	    return false;
    }

    public void setMappingStrategy(String strategy) {}
}