/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.weld;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BeanFactory<T> implements Bean<T> {
    private final T instance;
    private final Class<T> clazz;
    private final String name;
    private final Class<? extends Annotation> scope;
    private final AnnotatedType<T> annotatedType;
    private final InjectionTarget<T> injectionTarget;
    private final Set<Type> types = new HashSet<Type>();

    public BeanFactory(BeanManager beanManager, Class<T> clazz, String name, T instance) {
        this(beanManager, clazz, name, instance, Singleton.class);
    }

    public BeanFactory(BeanManager beanManager, Class<T> clazz, String name, T instance, Class<? extends Annotation> scope) {
        this.instance = instance;
        this.clazz = clazz;
        this.name = name;
        this.scope = scope;
        annotatedType = beanManager.createAnnotatedType(clazz);
        injectionTarget = beanManager.createInjectionTarget(annotatedType);

        collectTypes(clazz);
    }

    private void collectTypes(Class c) {
        if (c == null || types.contains(c)) {
            return;
        }

        types.add(c);
        for (Class i : c.getInterfaces()) {
            collectTypes(i);
        }
        collectTypes(c.getSuperclass());
    }

    public String toString() {
        return new StringBuilder("BeanFactory[name=")
                .append(name)
                .append(", class=")
                .append(clazz.getName())
                .append(", instance=")
                .append(instance)
                .append(", scope=")
                .append(scope.getName())
                .append("]")
                .toString();
    }

    public Set<Type> getTypes() {
        return types;
    }

    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<Annotation>();
        qualifiers.add(new AnnotationLiteral<Default>() {
        });
        qualifiers.add(new AnnotationLiteral<Any>() {
        });
        return qualifiers;
    }

    public Class<? extends Annotation> getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public Class<?> getBeanClass() {
        return clazz;
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() {
        return false;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return injectionTarget.getInjectionPoints();
    }

    public T create(CreationalContext<T> context) {
        injectionTarget.inject(instance, context);
        injectionTarget.postConstruct(instance);
        return instance;
    }

    public void destroy(T t, CreationalContext<T> context) {
        injectionTarget.preDestroy(instance);
        injectionTarget.dispose(instance);
        context.release();
    }
}