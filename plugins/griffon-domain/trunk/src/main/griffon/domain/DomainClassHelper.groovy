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

package griffon.domain

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.artifacts.GriffonDomainArtifactClass
// import griffon.domain.artifacts.DefaultGriffonDomainArtifactClass

import java.beans.Introspector
import java.util.regex.Pattern

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class DomainClassHelper {
    private static final Log LOG = LogFactory.getLog(DomainClassHelper.class)
    private static final Pattern PLURAL_PATTERN = Pattern.compile(".*[^aeiouy]y", Pattern.CASE_INSENSITIVE)

    private final Map ARTIFACTS = [:]
    private final Map DOMAIN_CLASSES = [:]
    private final Map ENTITIES_NAMES = [:]

    void init(GriffonApplication app) {
        app.artifactManager.domainArtifacts.each { domain ->
            String entityName = fetchAndSetEntityNameFor(domain)
            ARTIFACTS[domain.klass.name] = domain
            // DOMAIN_CLASSES[domain.klass.name] = new DefaultGriffonDomainClass(domain, entityName)
        }
    }
    
    String getEntityNameFor(Class klass) {
        ENTITIES_NAMES[klass.name]
    }

    String getEntityNameFor(ArtifactInfo artifactInfo) {
        ENTITIES_NAMES[artifactInfo.klass.name]
    }

    ArtifactInfo getArtifactInfoFor(Class klass) {
        ARTIFACTS[klass.name]
    }

    GriffonDomainArtifactClass getDomainClassFor(Class klass) {
        DOMAIN_CLASSES[klass.name]
    }

    GriffonDomainArtifactClass getDomainClassFor(ArtifactInfo artifactInfo) {
        DOMAIN_CLASSES[artifactInfo.klass.name]
    }

    // ----------------------------------------------

    private final String fetchAndSetEntityNameFor(ArtifactInfo domain) {
        String key = domain.klass.name
        String entityName = domain.simpleName
        boolean matchesIESRule = PLURAL_PATTERN.matcher(entityName).matches()
        entityName = matchesIESRule ? entityName[0..-1] + "ies" : entityName + "s"
        ENTITIES_NAMES[key] = entityName
        return entityName
    }
}
