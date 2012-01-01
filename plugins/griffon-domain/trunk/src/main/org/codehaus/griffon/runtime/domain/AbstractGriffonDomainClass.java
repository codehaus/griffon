/*
 * Copyright 2010-2011 the original author or authors.
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

import griffon.core.GriffonApplication;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainClassProperty;
import griffon.domain.GriffonDomainHandler;
import org.codehaus.griffon.runtime.core.AbstractGriffonClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static griffon.util.GriffonClassUtils.EMPTY_ARGS;
import static griffon.util.GriffonClassUtils.invokeExactStaticMethod;

/**
 * Base implementation of the {@code GriffonDomainClass} interface
 *
 * @author Andres Almiray
 */
public abstract class AbstractGriffonDomainClass extends AbstractGriffonClass implements GriffonDomainClass {
    protected Map<String, GriffonDomainClassProperty> domainProperties = new LinkedHashMap<String, GriffonDomainClassProperty>();

    public AbstractGriffonDomainClass(GriffonApplication app, Class<?> clazz) {
        super(app, clazz, TYPE, TRAILING);
        initialize();
    }

    protected abstract void initialize();

    public GriffonDomainClassProperty[] getProperties() {
        return domainProperties.values().toArray(new GriffonDomainClassProperty[domainProperties.size()]);
    }

    public GriffonDomainClassProperty[] getPersistentProperties() {
        List<GriffonDomainClassProperty> persistent = new ArrayList<GriffonDomainClassProperty>();
        for (GriffonDomainClassProperty property : domainProperties.values()) {
            if (property.isPersistent()) {
                persistent.add(property);
            }
        }
        return persistent.toArray(new GriffonDomainClassProperty[persistent.size()]);
    }

    public GriffonDomainClassProperty getPropertyByName(String name) {
        return domainProperties.get(name);
    }

    public GriffonDomainClassProperty getIdentity() {
        return getPropertyByName(GriffonDomainClassProperty.IDENTITY);
    }

    public GriffonDomainHandler getDomainHandler() {
        return (GriffonDomainHandler) invokeExactStaticMethod(getClazz(), "domainHandler", EMPTY_ARGS);
    }

    public String getDatasourceName() {
        return (String) invokeExactStaticMethod(getClazz(), "datasource", EMPTY_ARGS);
    }
}
