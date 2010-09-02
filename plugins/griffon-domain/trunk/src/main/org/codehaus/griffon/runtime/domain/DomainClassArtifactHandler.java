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

package org.codehaus.griffon.runtime.domain;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.core.ArtifactInfo;
import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainClassProperty;
import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;

import griffon.persistence.Entity;
import griffon.util.ClosureToMapPopulator;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.util.Eval;

import java.util.Map;

/**
 * Handler for 'Domain' artifacts.
 *
 * @author Andres Almiray
 */
public class DomainClassArtifactHandler extends ArtifactHandlerAdapter {
    @SuppressWarnings("unchecked")
    private Map defaultConstraints;

    public DomainClassArtifactHandler(GriffonApplication app) {
        super(app, GriffonDomainClass.TYPE, GriffonDomainClass.TRAILING);
        initConfiguration();
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        if (defaultConstraints != null) {
            return new DefaultGriffonDomainClass(getApp(), clazz, defaultConstraints);
        }
        return new DefaultGriffonDomainClass(getApp(), clazz);
    }

    @SuppressWarnings("unchecked")
    public Map getDefaultConstraints() {
        return defaultConstraints;
    }

    /**
     * Sets up the relationships between the domain classes, this has to be done after
     * the intial creation to avoid looping
     */
    @Override
    public void initialize(ArtifactInfo[] artifacts) {
        super.initialize(artifacts);
        postInitialize();
    }
    
    protected void postInitialize() {
        for(GriffonClass griffonClass: getClasses()) {
            ((DefaultGriffonDomainClass)griffonClass).initialize();
        }
        GriffonDomainConfigurationUtil.configureDomainClassRelationships(
                getClasses(),
                getClassesByName());
    }

    private void initConfiguration() {
        Object constraints = Eval.x(getApp().getConfig(), "x?.griffon?.domain?.default?.constraints");
        if (constraints instanceof Closure) {
            if (defaultConstraints != null) {
                // repopulate existing map
                defaultConstraints.clear();
                new ClosureToMapPopulator(defaultConstraints).populate((Closure) constraints);
            }
            else {
                ClosureToMapPopulator populator = new ClosureToMapPopulator();
                defaultConstraints = populator.populate((Closure) constraints);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean isDomainClass(Class clazz) {
        // it's not a closure
        if (clazz == null) return false;

        if (Closure.class.isAssignableFrom(clazz)) {
            return false;
        }

        if (Enum.class.isAssignableFrom(clazz)) return false;

        if (GriffonDomain.class.isAssignableFrom(clazz)) return true;

        if (clazz.getAnnotation(Entity.class) != null) {
            return true;
        }

        Class testClass = clazz;
        boolean result = false;
        while (testClass != null && !testClass.equals(GroovyObject.class) && !testClass.equals(Object.class)) {
            try {
                // make sure the identify and version field exist
                testClass.getDeclaredField(GriffonDomainClassProperty.IDENTITY);
                testClass.getDeclaredField(GriffonDomainClassProperty.VERSION);

                // passes all conditions return true
                result = true;
                break;
            }
            catch (SecurityException e) {
                // ignore
            }
            catch (NoSuchFieldException e) {
                // ignore
            }
            testClass = testClass.getSuperclass();
        }
        return result;
    }
}
