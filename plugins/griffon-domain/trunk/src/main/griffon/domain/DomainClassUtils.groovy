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

package griffon.domain

import griffon.domain.orm.CriteriaBuilder
import griffon.domain.orm.CriteriaVisitException
import griffon.domain.orm.CriteriaVisitor
import griffon.domain.orm.Criterion
import java.util.regex.Pattern
import static griffon.util.GriffonNameUtils.isBlank
import griffon.util.GriffonClassUtils

/**
 * @author Andres Almiray
 */
final class DomainClassUtils {
    private static final Pattern PLURAL_PATTERN = Pattern.compile(".*[^aeiouy]y", Pattern.CASE_INSENSITIVE)

    private final Map ENTITY_NAMES = [:]
    private static final DomainClassUtils INSTANCE = new DomainClassUtils()

    public static DomainClassUtils getInstance() {
        INSTANCE
    }

    private DomainClassUtils() {}

    /*
    void init(GriffonApplication app) {
        app.artifactManager.domainClasses.each { domain ->
            String entityName = fetchAndSetEntityNameFor(domain)
        }
    }

    String getEntityNameFor(Class klass) {
        ENTITY_NAMES[klass.name]
    }

    String getEntityNameFor(GriffonDomainClass domain) {
        ENTITY_NAMES[domain.clazz.name]
    }
    */

    Criterion buildCriterion(Closure criteria) {
        Criterion criterion = null;

        try {
            criterion = CriteriaVisitor.visit(criteria);
        } catch (CriteriaVisitException cve) {
            criteria.setDelegate(new CriteriaBuilder());
            criterion = (Criterion) criteria.call();
        } catch (ClassCastException cce) {
            criteria.setDelegate(new CriteriaBuilder());
            criterion = (Criterion) criteria.call();
        }

        return criterion;
    }

    void addTo(GriffonDomain owner, Map<String, Object> params, String relationshipName) {
        if (owner == null || params == null || params.isEmpty() || isBlank(relationshipName)) return;
        GriffonDomainClass ownerDomainClass = (GriffonDomainClass) = owner.getGriffonClass();
    }

    void addTo(GriffonDomain owner, GriffonDomain target, String relationshipName) {
        if (owner == null || target == null || isBlank(relationshipName)) return;
        GriffonDomainClass ownerDomainClass = (GriffonDomainClass) = owner.getGriffonClass();
        GriffonDomainClassProperty relationship = ownerDomainClass.getPropertyByName(relationshipName);
        Collection collection = (Collection) relationship.getValue(owner);
        GriffonClassUtils.invokeInstanceMethod(collection, "add", target);
        // set owning side on target
    }

    void addAllTo(GriffonDomain owner, Collection<Map<String, Object>> targets, String relationshipName) {
        if (targets == null || targets.isEmpty()) return;
        for (Map<String, Object> target: targets) {
            addTo(owner, target, relationshipName)
        }
    }

    void addAllTo(GriffonDomain owner, Collection<GriffonDomain> targets, String relationshipName) {
        if (targets == null || targets.isEmpty()) return;
        for (GriffonDomain target: targets) {
            addTo(owner, target, relationshipName)
        }
    }

    // ----------------------------------------------

    private final String fetchAndSetEntityNameFor(GriffonDomainClass domain) {
        String key = domain.clazz.name
        String entityName = domain.clazz.simpleName
        boolean matchesIESRule = PLURAL_PATTERN.matcher(entityName).matches()
        entityName = matchesIESRule ? entityName[0..-1] + "ies" : entityName + "s"
        ENTITY_NAMES[key] = entityName
        return entityName
    }
}