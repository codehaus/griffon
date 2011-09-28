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
package griffon.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * A property of a GriffonDomainClass instance
 *
 * @author Andres Almiray
 */
public interface GriffonDomainClassProperty {
    String IDENTITY = "id";
    String VERSION = "version";
    String TRANSIENT = "transients";
    String CONSTRAINTS = "constraints";
    String BELONGS_TO = "belongsTo";
    String HAS_MANY = "hasMany";
    String HAS_ONE = "hasOne";
    String DATE_CREATED = "dateCreated";
    String LAST_UPDATED = "lastUpdated";

    Set<String> NON_CONFIGURATIONAL_PROPERTIES = new TreeSet<String>(
            Arrays.asList(TRANSIENT, CONSTRAINTS, BELONGS_TO, HAS_MANY, HAS_ONE));

    /**
     * Returns the name of the property
     *
     * @return The property name
     */
    String getName();

    /**
     * Returns the type for the domain class
     *
     * @return The property type
     */
    Class getType();

    /**
     * Returns the parent domain class of the property instance
     *
     * @return The parent domain class
     */
    GriffonDomainClass getDomainClass();

    /**
     * Returns true if the domain class property is a persistent property
     *
     * @return Whether the property is persistent
     */
    boolean isPersistent();

    Object getValue(GriffonDomain owner);

    void setValue(GriffonDomain owner, Object value);
}