/* 
 * Copyright 2009-2010 the original author or authors.
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
package org.codehaus.griffon.runtime.ldap;

import gldapo.schema.annotation.GldapoNamingAttribute;
import java.lang.reflect.Field;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.plugins.ldap.GriffonLdapSchemaClass;
import org.codehaus.griffon.runtime.core.SpringArtifactHandlerAdapter;

/**
 * Griffon artefact handler for LDAP schema classes.
 *
 * @author Luke Daley
 * @author Andres Almiray
 */
public class LdapSchemaArtifactHandler extends SpringArtifactHandlerAdapter {
    public LdapSchemaArtifactHandler(GriffonApplication app) {
        super(app, GriffonLdapSchemaClass.TYPE, GriffonLdapSchemaClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonLdapSchemaClass(getApp(), clazz);
    }

    public boolean isArtifactClass(Class clazz) {
        // class shouldn't be null and shoud ends with LdapSchema suffix
        if(clazz == null || !clazz.getName().endsWith(GriffonLdapSchemaClass.TRAILING)) return false;
        if (clazz == null) return false;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(GldapoNamingAttribute.class) != null)
                return true;
        }
        return false;
    }
}
