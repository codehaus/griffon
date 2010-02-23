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

import griffon.core.GriffonApplication;
import griffon.core.ArtifactInfo;
import griffon.domain.DynamicMethod;
import griffon.domain.orm.Criterion;
import griffon.domain.orm.CriteriaBuilder;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractFindPersistentMethod extends AbstractStaticPersistentMethod {
    private static final CriteriaBuilder BUILDER = new CriteriaBuilder();

    public AbstractFindPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass, Pattern.compile("^"+ DynamicMethod.FIND.getMethodName() +"$"));
    }

    protected Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            throw new MissingMethodException(methodName, clazz, arguments);
        }
        final Object arg = arguments[0];
        if(arg instanceof Criterion) {
            if(arguments.length == 1) {
                return findByCriterion((Criterion)arg, Collections.emptyMap());
            } else if(arguments[1] instanceof Map) {
                return findByCriterion((Criterion)arg, (Map) arguments[1]);
            }
        } else if(arg instanceof Closure) {
            return findByCriterion(buildCriterion((Closure)arg), Collections.emptyMap());
        } else if(arg instanceof Map) {
            if(arguments.length == 1) {
                return findByProperties((Map) arg);
            } else if(arguments[1] instanceof Closure) {
                return findByCriterion(buildCriterion((Closure)arguments[1]), (Map) arg);
            }
        } else if(getDomainClass().getKlass().isAssignableFrom(clazz) ) {
            return findByExample(arg);
        }
        throw new MissingMethodException(methodName, clazz, arguments);
    }

    protected Object findByProperties(Map properties) {
        return null;
    }

    protected Object findByExample(Object example) {
        return null;
    }

    protected Object findByCriterion(Criterion criterion, Map options) {
        return null;
    }

    protected final Criterion buildCriterion(Closure criteria) {
        criteria.setDelegate(BUILDER);
        return (Criterion) criteria.call();
    }
}
