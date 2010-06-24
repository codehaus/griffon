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
package griffon.domain.metaclass;

import griffon.core.ArtifactInfo;
import griffon.domain.DomainHandler;
import griffon.domain.orm.Criterion;
import griffon.domain.orm.CriteriaBuilder;
import griffon.domain.orm.CriteriaVisitor;
import griffon.domain.orm.CriteriaVisitException;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Andres Almiray
 */
public abstract class AbstractFindAllPersistentMethod extends AbstractPersistentStaticMethodInvocation {
    private static final CriteriaBuilder BUILDER = new CriteriaBuilder();

    public AbstractFindAllPersistentMethod(DomainHandler domainHandler) {
        super(domainHandler);
    }

    protected Object invokeInternal(ArtifactInfo artifactInfo, Class clazz, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return findAll(artifactInfo, clazz);
        }
        final Object arg = arguments[0];
        if(arg instanceof Criterion) {
            if(arguments.length == 1) {
                return findByCriterion(artifactInfo, (Criterion)arg, Collections.emptyMap());
            } else if(arguments[1] instanceof Map) {
                return findByCriterion(artifactInfo, (Criterion)arg, (Map) arguments[1]);
            }
        } else if(arg instanceof Closure) {
            return findByCriterion(artifactInfo, buildCriterion((Closure)arg), Collections.emptyMap());
        } else if(arg instanceof Map) {
            if(arguments.length == 1) {
                return findByProperties(artifactInfo, (Map) arg);
            } else if(arguments[1] instanceof Closure) {
                return findByCriterion(artifactInfo, buildCriterion((Closure)arguments[1]), (Map) arg);
            }
        } else if(artifactInfo.getKlass().isAssignableFrom(clazz) ) {
            return findByExample(artifactInfo, arg);
        }
        throw new MissingMethodException(methodName, clazz, arguments);
    }

    protected Collection findAll(ArtifactInfo artifactInfo, Class clazz) {
        return Collections.emptyList();
    }

    protected Collection findByProperties(ArtifactInfo artifactInfo, Map properties) {
        return Collections.emptyList();
    }

    protected Collection findByExample(ArtifactInfo artifactInfo, Object example) {
        return Collections.emptyList();
    }

    protected Collection findByCriterion(ArtifactInfo artifactInfo, Criterion criterion, Map options) {
        return Collections.emptyList();
    }

    protected final Criterion buildCriterion(Closure criteria) {
        Criterion criterion = null;

        try {
            criterion = CriteriaVisitor.visit(criteria);
        } catch(CriteriaVisitException cve) {
            criteria.setDelegate(BUILDER);
            criterion = (Criterion) criteria.call();
        }

        return criterion;
    }
}
