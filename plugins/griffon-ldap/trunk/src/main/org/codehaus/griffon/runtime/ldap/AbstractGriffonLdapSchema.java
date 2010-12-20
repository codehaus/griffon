/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.runtime.ldap;

import griffon.plugins.ldap.GriffonLdapSchema;
import griffon.plugins.ldap.GriffonLdapSchemaClass;
import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;

/**
 * @author Andres Almiray
 */
public abstract class AbstractGriffonLdapSchema extends AbstractGriffonArtifact implements GriffonLdapSchema {
    protected String getArtifactType() {
        return GriffonLdapSchemaClass.TYPE;
    }
}
