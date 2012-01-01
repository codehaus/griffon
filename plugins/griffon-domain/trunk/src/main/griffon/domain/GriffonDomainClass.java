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

import griffon.core.GriffonClass;

/**
 * <p>Represents a persistable Griffon domain class</p>
 *
 * @author Andres Almiray
 */
public interface GriffonDomainClass extends GriffonClass {
    /**
     * "domain"
     */
    String TYPE = "domain";
    /**
     * "" (empty)
     */
    String TRAILING = "";

    /**
     * Returns all of the properties of the domain class
     *
     * @return The domain class properties
     */
    GriffonDomainClassProperty[] getProperties();

    /**
     * Returns all of the persistent properties of the domain class
     *
     * @return The domain class' persistent properties
     */
    GriffonDomainClassProperty[] getPersistentProperties();

    /**
     * Returns the property for the given name
     *
     * @param name The property for the name
     * @return The domain class property for the given name
     * @throws griffon.exceptions.domain.InvalidPropertyException
     *
     */
    GriffonDomainClassProperty getPropertyByName(String name);

    GriffonDomainClassProperty getIdentity();

    GriffonDomainHandler getDomainHandler();

    String getDatasourceName();
}