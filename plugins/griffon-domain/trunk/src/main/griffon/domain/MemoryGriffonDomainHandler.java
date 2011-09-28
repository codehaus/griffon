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

package griffon.domain;

import griffon.core.GriffonApplication;
import griffon.domain.methods.*;
import griffon.domain.orm.Criterion;
import griffon.util.ApplicationHolder;
import org.codehaus.griffon.runtime.domain.methods.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

/**
 * @author Andres Almiray
 */
public class MemoryGriffonDomainHandler implements GriffonDomainHandler {
    private static final String MAPPING = "memory";
    private final Map<String, InstanceMethodInvocation> INSTANCE_METHODS = new LinkedHashMap<String, InstanceMethodInvocation>();
    private final Map<String, StaticMethodInvocation> STATIC_METHODS = new LinkedHashMap<String, StaticMethodInvocation>();
    private static final ConcurrentHashMapDatabase DEFAULT_DATABASE = new ConcurrentHashMapDatabase("default");
    private static final Map<String, ConcurrentHashMapDatabase> DATASOURCES = new ConcurrentHashMap<String, ConcurrentHashMapDatabase>();

    private static final MemoryGriffonDomainHandler INSTANCE = new MemoryGriffonDomainHandler();

    public static MemoryGriffonDomainHandler getInstance() {
        return INSTANCE;
    }

    public static final MethodSignature[] METHOD_SIGNATURES;

    static {
        Collection<MethodSignature> signatures = new ArrayList<MethodSignature>();
        signatures.addAll(asList(SaveMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(DeleteMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(MakeMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FetchMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(ExistsMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FetchAllMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(CountMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(CountByMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(ListMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindWhereMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindByMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindAllMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindAllByMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(FindAllWhereMethod.METHOD_SIGNATURES));
        signatures.addAll(asList(WithCriteriaMethod.METHOD_SIGNATURES));
        METHOD_SIGNATURES = signatures.toArray(new MethodSignature[signatures.size()]);

        DATASOURCES.put(DEFAULT_DATABASE.getName(), DEFAULT_DATABASE);
    }

    public MemoryGriffonDomainHandler() {
        INSTANCE_METHODS.put(SaveMethod.METHOD_NAME, new SaveMethod(this));
        INSTANCE_METHODS.put(DeleteMethod.METHOD_NAME, new DeleteMethod(this));

        STATIC_METHODS.put(MakeMethod.METHOD_NAME, new MakeMethod(this));
        STATIC_METHODS.put(ListMethod.METHOD_NAME, new ListMethod(this));
        STATIC_METHODS.put(FetchMethod.METHOD_NAME, new FetchMethod(this));
        STATIC_METHODS.put(ExistsMethod.METHOD_NAME, new ExistsMethod(this));
        STATIC_METHODS.put(FetchAllMethod.METHOD_NAME, new FetchAllMethod(this));
        STATIC_METHODS.put(CountMethod.METHOD_NAME, new CountMethod(this));
        STATIC_METHODS.put(CountByMethod.METHOD_NAME, new CountByMethod(this));
        STATIC_METHODS.put(FindMethod.METHOD_NAME, new FindMethod(this));
        STATIC_METHODS.put(FindWhereMethod.METHOD_NAME, new FindWhereMethod(this));
        STATIC_METHODS.put(FindByMethod.METHOD_NAME, new FindByMethod(this));
        STATIC_METHODS.put(FindAllMethod.METHOD_NAME, new FindAllMethod(this));
        STATIC_METHODS.put(FindAllByMethod.METHOD_NAME, new FindAllByMethod(this));
        STATIC_METHODS.put(FindAllWhereMethod.METHOD_NAME, new FindAllWhereMethod(this));
        STATIC_METHODS.put(WithCriteriaMethod.METHOD_NAME, new WithCriteriaMethod(this));
    }

    public GriffonApplication getApp() {
        return ApplicationHolder.getApplication();
    }

    public String getMapping() {
        return MAPPING;
    }

    public MethodSignature[] getMethodSignatures() {
        return METHOD_SIGNATURES;
    }

    public Object invokeInstance(Object target, String methodName, Object... args) {
        InstanceMethodInvocation method = INSTANCE_METHODS.get(methodName);
        if (method == null) {
            throw new IllegalArgumentException("Method " + methodName + " is undefined for domain classes mapped with '" + MAPPING + "'");
        }
        if (target == null) {
            throw new IllegalArgumentException("Cannot call " + methodName + "() on a null instance");
        }
        if (!GriffonDomain.class.isAssignableFrom(target.getClass())) {
            throw new IllegalArgumentException("Cannot call " + methodName + "() on non-domain class [" + target.getClass().getName() + "]");
        }
        return method.invoke((GriffonDomain) target, methodName, args);
    }

    public Object invokeStatic(Class<GriffonDomain> clazz, String methodName, Object... args) {
        StaticMethodInvocation method = STATIC_METHODS.get(methodName);
        if (method == null) {
            throw new IllegalArgumentException("Method " + methodName + " is undefined for domain classes mapped with '" + MAPPING + "'");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Cannot call " + methodName + "() on a null class");
        }
        if (!GriffonDomain.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Cannot call " + methodName + "() on non-domain class [" + clazz.getName() + "]");
        }
        return method.invoke(clazz, methodName, args);
    }

    public ConcurrentHashMapDatabase getDatabase() {
        return DEFAULT_DATABASE;
    }

    public ConcurrentHashMapDatabase getDatabase(String name) {
        return DATASOURCES.get(name);
    }

    private <T extends GriffonDomain> ConcurrentHashMapDatabase.Dataset<T> datasetOf(GriffonDomainClass domainClass) {
        if (domainClass == null) {
            throw new IllegalArgumentException("DomainClass is null!");
        }
        return DEFAULT_DATABASE.dataset(domainClass);
    }

    private class SaveMethod extends AbstractSavePersistentMethod {
        private final AtomicInteger identitySequence = new AtomicInteger(0);

        public SaveMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected boolean shouldInsert(GriffonDomainClass domainClass, GriffonDomain target, Object[] arguments) {
            GriffonDomainClassProperty identity = identityOf(target);
            Object identityValue = identity.getValue(target);
            return identityValue == null;
        }

        @Override
        protected GriffonDomain insert(GriffonDomainClass domainClass, GriffonDomain target, Object[] arguments) {
            GriffonDomainClassProperty identity = identityOf(target);
            identity.setValue(target, identitySequence.incrementAndGet());
            return datasetOf(domainClass).save(target);
        }

        @Override
        protected GriffonDomain save(GriffonDomainClass domainClass, GriffonDomain target, Object[] arguments) {
            return datasetOf(domainClass).save(target);
        }
    }

    private class DeleteMethod extends AbstractDeletePersistentMethod {
        public DeleteMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected GriffonDomain delete(GriffonDomainClass domainClass, GriffonDomain target) {
            return datasetOf(domainClass).remove(target);
        }
    }

    private class MakeMethod extends AbstractMakePersistentMethod {
        public MakeMethod(GriffonDomainHandler domainHandler) {
            super(domainHandler);
        }
    }

    private class FetchMethod extends AbstractFetchPersistentMethod {
        public FetchMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected GriffonDomain fetch(GriffonDomainClass domainClass, Object key) {
            return datasetOf(domainClass).fetch(key);
        }
    }

    private class ExistsMethod extends AbstractExistsPersistentMethod {
        public ExistsMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected boolean exists(GriffonDomainClass domainClass, Object key) {
            return datasetOf(domainClass).fetch(key) != null;
        }
    }

    private class FetchAllMethod extends AbstractFetchAllPersistentMethod {
        public FetchAllMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection<GriffonDomain> fetchAll(GriffonDomainClass domainClass) {
            return datasetOf(domainClass).list();
        }

        @Override
        protected Collection<GriffonDomain> fetchAllByIdentities(GriffonDomainClass domainClass, List<Object> identities) {
            List<GriffonDomain> entities = new ArrayList<GriffonDomain>();
            if (identities != null && identities.size() > 0) {
                ConcurrentHashMapDatabase.Dataset dataset = datasetOf(domainClass);
                for (Object identity : identities) {
                    GriffonDomain entity = dataset.fetch(identity);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
            }
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }

        @Override
        protected Collection<GriffonDomain> fetchAllByIdentities(GriffonDomainClass domainClass, Object[] identities) {
            List<GriffonDomain> entities = new ArrayList<GriffonDomain>();
            if (identities != null && identities.length > 0) {
                ConcurrentHashMapDatabase.Dataset dataset = datasetOf(domainClass);
                for (Object identity : identities) {
                    GriffonDomain entity = dataset.fetch(identity);
                    if (entity != null) {
                        entities.add(entity);
                    }
                }
            }
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }
    }

    private class ListMethod extends AbstractListPersistentMethod {
        public ListMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection<GriffonDomain> list(GriffonDomainClass domainClass) {
            return list(domainClass, Collections.<String, Object>emptyMap());
        }

        @Override
        protected Collection<GriffonDomain> list(GriffonDomainClass domainClass, Map<String, Object> options) {
            ConcurrentHashMapDatabase.Dataset dataset = datasetOf(domainClass);
            if (dataset == null) {
                return Collections.emptyList();
            } else {
                return dataset.list(options);
            }
        }
    }

    private class CountMethod extends AbstractCountPersistentMethod {
        public CountMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected int count(GriffonDomainClass domainClass) {
            return datasetOf(domainClass).size();
        }
    }

    private class FindAllWhereMethod extends AbstractFindAllWherePersistentMethod {
        public FindAllWhereMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection<GriffonDomain> findByParams(GriffonDomainClass domainClass, Map<String, Object> params) {
            List<GriffonDomain> entities = datasetOf(domainClass).query(params);
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }
    }

    private class FindWhereMethod extends AbstractFindWherePersistentMethod {
        public FindWhereMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected GriffonDomain findByParams(GriffonDomainClass domainClass, Map<String, Object> params) {
            return datasetOf(domainClass).first(params);
        }
    }

    private class FindMethod extends AbstractFindPersistentMethod {
        public FindMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected GriffonDomain findByProperties(GriffonDomainClass domainClass, Map<String, Object> properties) {
            return datasetOf(domainClass).first(properties);
        }

        @Override
        protected GriffonDomain findByExample(GriffonDomainClass domainClass, Object example) {
            return datasetOf(domainClass).first(example);
        }

        @Override
        protected GriffonDomain findByCriterion(GriffonDomainClass domainClass, Criterion criterion, Map<String, Object> options) {
            return datasetOf(domainClass).first(criterion);
        }
    }

    private class FindAllMethod extends AbstractFindAllPersistentMethod {
        public FindAllMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection<GriffonDomain> findAll(GriffonDomainClass domainClass) {
            List<GriffonDomain> entities = datasetOf(domainClass).list();
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }

        @Override
        protected Collection<GriffonDomain> findByProperties(GriffonDomainClass domainClass, Map<String, Object> properties) {
            List<GriffonDomain> entities = datasetOf(domainClass).query(properties);
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }

        @Override
        protected Collection<GriffonDomain> findByExample(GriffonDomainClass domainClass, Object example) {
            List<GriffonDomain> entities = datasetOf(domainClass).query(example);
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }

        @Override
        protected Collection<GriffonDomain> findByCriterion(GriffonDomainClass domainClass, Criterion criterion, Map<String, Object> options) {
            return datasetOf(domainClass).query(criterion, options);
        }
    }

    private class WithCriteriaMethod extends AbstractWithCriteriaPersistentMethod {
        public WithCriteriaMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection<GriffonDomain> withCriteria(GriffonDomainClass domainClass, Criterion criterion) {
            List<GriffonDomain> entities = datasetOf(domainClass).query(criterion);
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }
    }

    private class FindByMethod extends AbstractFindByPersistentMethod {
        public FindByMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Object findBy(GriffonDomainClass domainClass, String methodName, Criterion criterion) {
            return datasetOf(domainClass).first(criterion);
        }
    }

    private class FindAllByMethod extends AbstractFindAllByPersistentMethod {
        public FindAllByMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected Collection findAllBy(GriffonDomainClass domainClass, String methodName, Criterion criterion) {
            List<GriffonDomain> entities = datasetOf(domainClass).query(criterion);
            Collections.sort(entities, IDENTITY_COMPARATOR);
            return entities;
        }
    }

    private class CountByMethod extends AbstractCountByPersistentMethod {
        public CountByMethod(GriffonDomainHandler griffonDomainHandler) {
            super(griffonDomainHandler);
        }

        @Override
        protected int countBy(GriffonDomainClass domainClass, String methodName, Criterion criterion) {
            return datasetOf(domainClass).query(criterion).size();
        }
    }

    private final GriffonDomain.Comparator IDENTITY_COMPARATOR = new GriffonDomain.Comparator(GriffonDomainClassProperty.IDENTITY);

    private GriffonDomainClassProperty identityOf(GriffonDomain target) {
        GriffonDomainClass domainClass = (GriffonDomainClass) target.getGriffonClass();
        return domainClass.getPropertyByName(GriffonDomainClassProperty.IDENTITY);
    }
}
