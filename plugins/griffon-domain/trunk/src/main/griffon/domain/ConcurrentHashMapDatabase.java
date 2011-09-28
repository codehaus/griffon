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

import griffon.core.ArtifactManager;
import griffon.core.GriffonClass;
import griffon.domain.orm.Criterion;
import griffon.domain.orm.CriterionEvaluator;
import griffon.util.ApplicationHolder;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andres Almiray
 */
public class ConcurrentHashMapDatabase {
    private final String name;
    private final Map<String, Dataset<? extends GriffonDomain>> DATASETS = new ConcurrentHashMap<String, Dataset<? extends GriffonDomain>>();

    public ConcurrentHashMapDatabase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public <T extends GriffonDomain> Dataset<T> dataset(String name) {
        Dataset dataset = DATASETS.get(name);
        if (dataset == null) {
            ArtifactManager artifactManager = ApplicationHolder.getApplication().getArtifactManager();
            GriffonClass griffonClass = artifactManager.findGriffonClass(name, GriffonDomainClass.TYPE);
            dataset = dataset((GriffonDomainClass) griffonClass);
        }
        return dataset;
    }

    public <T extends GriffonDomain> Dataset<T> dataset(GriffonDomainClass domainClass) {
        Dataset dataset = DATASETS.get(domainClass.getName());
        if (dataset == null) {
            dataset = new Dataset(domainClass);
            DATASETS.put(dataset.getName(), dataset);
        }
        return dataset;
    }

    public static class Dataset<T extends GriffonDomain> {
        private static final Logger LOG = LoggerFactory.getLogger(Dataset.class);
        private final Map<Object, T> ROWS = Collections.synchronizedSortedMap(new TreeMap<Object, T>());
        private final GriffonDomainClass domainClass;
        private final String name;

        public Dataset(GriffonDomainClass domainClass) {
            this.domainClass = domainClass;
            this.name = domainClass.getName();
        }

        public String getName() {
            return name;
        }

        public T save(T entity) {
            if (entity == null) {
                throw new IllegalArgumentException("Dataset is null!");
            }
            GriffonDomainClass griffonClass = (GriffonDomainClass) entity.getGriffonClass();
            GriffonDomainClassProperty identity = griffonClass.getPropertyByName(GriffonDomainClassProperty.IDENTITY);
            if (identity == null) {
                throw new IllegalArgumentException("Cannot save " + entity + " because it does not have an " + GriffonDomainClassProperty.IDENTITY + " property.");
            }
            Object identityValue = identity.getValue(entity);
            if (ROWS.containsKey(identityValue)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Updating entity with id = " + identityValue);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Saving entity with id = " + identityValue);
                }
            }
            ROWS.put(identityValue, entity);
            return entity;
        }

        public T remove(T entity) {
            if (entity == null) {
                throw new IllegalArgumentException("Dataset is null!");
            }
            GriffonDomainClass griffonClass = domainClassOf(entity);
            GriffonDomainClassProperty identity = griffonClass.getIdentity();
            if (identity == null) {
                throw new IllegalArgumentException("Cannot remove " + entity + " because it does not have an " + GriffonDomainClassProperty.IDENTITY + " property.");
            }
            Object identityValue = identity.getValue(entity);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Removing entity with id = " + identityValue);
            }
            ROWS.remove(identityValue);
            return entity;
        }

        public List<T> list() {
            List<T> entities = new ArrayList<T>();
            entities.addAll(ROWS.values());
            return entities;
        }

        private static final String KEY_MAX = "max";
        private static final String KEY_SORT = "sort";
        private static final String KEY_ORDER = "order";
        private static final String KEY_OFFSET = "offset";

        public List<T> list(Map<String, Object> options) {
            int max = determineMax(options);
            int offset = determineOffset(options);
            String sort = determineSort(options);
            GriffonDomain.Comparator.Order order = determineOrder(options);

            List<T> entities = new ArrayList<T>();
            synchronized (ROWS) {
                int size = ROWS.size();
                Iterator<T> iterator = ROWS.values().iterator();
                if (offset < size) {
                    int count = 0;
                    for (int row = 0; row < size && count < max; row++) {
                        T entity = iterator.next();
                        if (row < offset) continue;
                        entities.add(entity);
                        count++;
                    }
                }
            }

            Collections.sort(entities, new GriffonDomain.Comparator(sort, order));
            return entities;
        }

        private int determineMax(Map<String, Object> options) {
            Integer value = (Integer) options.get(KEY_MAX);
            return value != null ? value : Integer.MAX_VALUE;
        }

        private int determineOffset(Map<String, Object> options) {
            Integer value = (Integer) options.get(KEY_OFFSET);
            return value != null ? value : 0;
        }

        private String determineSort(Map<String, Object> options) {
            Object value = options.get(KEY_SORT);
            return value != null ? String.valueOf(value) : GriffonDomainClassProperty.IDENTITY;
        }

        private GriffonDomain.Comparator.Order determineOrder(Map<String, Object> options) {
            Object value = options.get(KEY_ORDER);
            String order = value != null ? String.valueOf(value) : "asc";
            return GriffonDomain.Comparator.Order.valueOf(order.toUpperCase());
        }

        public T fetch(Object identity) {
            if (identity == null) {
                throw new IllegalArgumentException("Cannot fetch entity because supplied identity is null");
            }

            GriffonDomainClassProperty property = domainClass.getPropertyByName(GriffonDomainClassProperty.IDENTITY);
            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    Object propertyValue = property.getValue(entity);
                    if (DefaultTypeTransformation.compareEqual(identity, propertyValue)) {
                        return entity;
                    }
                }
            }
            return null;
        }

        public List<T> query(Object example) {
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            if (example != null && domainClass.getClazz().isAssignableFrom(example.getClass())) {
                for (GriffonDomainClassProperty property : domainClassOf((GriffonDomain) example).getPersistentProperties()) {
                    Object value = property.getValue((GriffonDomain) example);
                    if (value != null) {
                        params.put(property.getName(), value);
                    }
                }
            }
            return query(params);
        }

        public List<T> query(Map<String, Object> params) {
            List<T> entities = new ArrayList<T>();
            if (params == null || params.isEmpty()) {
                return entities;
            }

            List<GriffonDomainClassProperty> properties = new ArrayList<GriffonDomainClassProperty>();
            for (String propertyName : params.keySet()) {
                GriffonDomainClassProperty property = domainClass.getPropertyByName(propertyName);
                if (property == null) {
                    throw new IllegalArgumentException("Property " + propertyName + " is not a persistent property of " + domainClass.getClazz());
                }
                properties.add(property);
            }

            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    boolean allMatch = true;
                    for (GriffonDomainClassProperty property : properties) {
                        Object exampleValue = params.get(property.getName());
                        Object propertyValue = property.getValue(entity);
                        allMatch &= DefaultTypeTransformation.compareEqual(exampleValue, propertyValue);
                    }
                    if (allMatch) {
                        entities.add(entity);
                    }
                }
            }

            return entities;
        }

        public List<T> query(Criterion criterion) {
            List<T> entities = new ArrayList<T>();
            CriterionEvaluator criterionEvaluator = new BeanCriterionEvaluator();

            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    if (criterionEvaluator.eval(entity, criterion)) {
                        entities.add(entity);
                    }
                }
            }

            return entities;
        }

        public List<T> query(Criterion criterion, Map<String, Object> options) {
            List<T> entities = new ArrayList<T>();
            CriterionEvaluator criterionEvaluator = new BeanCriterionEvaluator();

            int max = determineMax(options);
            String sort = determineSort(options);
            GriffonDomain.Comparator.Order order = determineOrder(options);

            int count = 0;
            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    if (criterionEvaluator.eval(entity, criterion)) {
                        entities.add(entity);
                        if (++count >= max) break;
                    }
                }
            }
            Collections.sort(entities, new GriffonDomain.Comparator(sort, order));

            return entities;
        }

        public T first(Object example) {
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            if (example != null && domainClass.getClazz().isAssignableFrom(example.getClass())) {
                for (GriffonDomainClassProperty property : domainClassOf((GriffonDomain) example).getPersistentProperties()) {
                    Object value = property.getValue((GriffonDomain) example);
                    if (value != null) {
                        params.put(property.getName(), value);
                    }
                }
            }
            return first(params);
        }

        public T first(Map<String, Object> params) {
            if (params == null || params.isEmpty()) {
                return null;
            }

            List<GriffonDomainClassProperty> properties = new ArrayList<GriffonDomainClassProperty>();
            for (String propertyName : params.keySet()) {
                GriffonDomainClassProperty property = domainClass.getPropertyByName(propertyName);
                if (property == null) {
                    throw new IllegalArgumentException("Property " + propertyName + " is not a persistent property of " + domainClass.getClazz());
                }
                properties.add(property);
            }

            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    boolean allMatch = true;
                    for (GriffonDomainClassProperty property : properties) {
                        Object exampleValue = params.get(property.getName());
                        Object propertyValue = property.getValue(entity);
                        allMatch &= DefaultTypeTransformation.compareEqual(exampleValue, propertyValue);
                    }
                    if (allMatch) {
                        return entity;
                    }
                }
            }

            return null;
        }

        public T first(Criterion criterion) {
            CriterionEvaluator criterionEvaluator = new BeanCriterionEvaluator();

            synchronized (ROWS) {
                for (T entity : ROWS.values()) {
                    if (criterionEvaluator.eval(entity, criterion)) {
                        return entity;
                    }
                }
            }

            return null;
        }

        public int size() {
            return ROWS.size();
        }

        private GriffonDomainClass domainClassOf(GriffonDomain entity) {
            return (GriffonDomainClass) entity.getGriffonClass();
        }
    }
}
