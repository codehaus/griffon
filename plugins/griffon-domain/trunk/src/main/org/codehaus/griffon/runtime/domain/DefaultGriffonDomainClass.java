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
package org.codehaus.griffon.runtime.domain;

// import org.springframework.validation.Validator;

import griffon.core.GriffonApplication;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainClassProperty;
import griffon.exceptions.domain.*;
import griffon.util.GriffonClassUtils;
import griffon.util.GriffonNameUtils;
import org.codehaus.griffon.runtime.core.DefaultGriffonClass;
import org.codehaus.griffon.runtime.core.ClassPropertyFetcher;

import groovy.lang.GroovyObject;
import org.apache.commons.lang.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultGriffonDomainClass extends DefaultGriffonClass implements GriffonDomainClass {
    private GriffonDomainClassProperty identifier;
    private GriffonDomainClassProperty version;
    private GriffonDomainClassProperty[] properties;
    private GriffonDomainClassProperty[] persistentProperties;
    private Map<String, GriffonDomainClassProperty> propertyMap;
    @SuppressWarnings("unchecked")
    private Map relationshipMap;
    @SuppressWarnings("unchecked")
    private Map hasOneMap;
    @SuppressWarnings("unchecked")
    private Map constraints;
    @SuppressWarnings("unchecked")
    private Map mappedBy;
    // private String mappingStrategy = GriffonDomainClass.GORM;
    private List<Class<?>> owners = new ArrayList<Class<?>>();
    private boolean root = true;
    @SuppressWarnings("unchecked")
    private Set subClasses = new HashSet();
    private Map<String, Object> defaultConstraints;

    public DefaultGriffonDomainClass(GriffonApplication app, Class<?> clazz) {
        this(app, clazz, null);
    }

    public DefaultGriffonDomainClass(GriffonApplication app, Class<?> clazz, Map<String, Object> defaultConstraints) {
        super(app, clazz, TYPE, TRAILING);

        if (!clazz.getSuperclass().equals(GroovyObject.class) &&
                !clazz.getSuperclass().equals(Object.class) &&
                !Modifier.isAbstract(clazz.getSuperclass().getModifiers())) {
            root = false;
        }
        propertyMap = new LinkedHashMap<String, GriffonDomainClassProperty>();
        relationshipMap = getAssociationMap();
        this.defaultConstraints = defaultConstraints;

        // get mapping strategy by setting
        // mappingStrategy = getStaticPropertyValue(GriffonDomainClassProperty.MAPPING_STRATEGY, String.class);
        //if (mappingStrategy == null) {
        //    mappingStrategy = GORM;
        //}

        // get any mappedBy settings
        mappedBy = getStaticPropertyValue(GriffonDomainClassProperty.MAPPED_BY, Map.class);
        hasOneMap = getStaticPropertyValue(GriffonDomainClassProperty.HAS_ONE, Map.class);
        if (hasOneMap == null) {
            hasOneMap = Collections.emptyMap();
        }

        if (mappedBy == null) {
            mappedBy = Collections.emptyMap();
        }
    }

    void initialize() {
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors();

        // establish the owners of relationships
        establishRelationshipOwners();

        // First go through the properties of the class and create domain properties
        // populating into a map
        populateDomainClassProperties(propertyDescriptors);

        // if no identifier property throw exception
        if (identifier == null) {
            throw new GriffonDomainException("Identity property not found, but required in domain class ["+getFullName()+"]");
        }

        // if no version property throw exception
//        if (version == null) {
//            throw new GriffonDomainException("Version property not found, but required in domain class ["+getFullName()+"]");
//        }
        // set properties from map values
        properties = propertyMap.values().toArray(new GriffonDomainClassProperty[propertyMap.size()]);

        // establish relationships
        establishRelationships();

        // set persistent properties
        establishPersistentProperties();
    }

    public boolean hasSubClasses() {
        return getSubClasses().size() > 0;
    }

    /**
     * calculates the persistent properties from the evaluated properties
     */
    private void establishPersistentProperties() {
        Collection<GriffonDomainClassProperty> tempList = new ArrayList<GriffonDomainClassProperty>();
        for (Object o : propertyMap.values()) {
            GriffonDomainClassProperty currentProp = (GriffonDomainClassProperty) o;
            if (currentProp.getType() != Object.class && currentProp.isPersistent() && !currentProp.isIdentity() && !currentProp.getName().equals(GriffonDomainClassProperty.VERSION)) {
                tempList.add(currentProp);
            }
        }
        persistentProperties = tempList.toArray(new GriffonDomainClassProperty[tempList.size()]);
    }

    /**
     * Evaluates the belongsTo property to find out who owns who
     */
    @SuppressWarnings("unchecked")
    private void establishRelationshipOwners() {
        Class<?> belongsTo = getStaticPropertyValue(GriffonDomainClassProperty.BELONGS_TO, Class.class);
        if (belongsTo == null) {
            List ownersProp = getStaticPropertyValue(GriffonDomainClassProperty.BELONGS_TO, List.class);
            if (ownersProp != null) {
                owners = ownersProp;
            }
            else {
                Map ownersMap = getStaticPropertyValue(GriffonDomainClassProperty.BELONGS_TO, Map.class);
                if (ownersMap!=null) {
                    owners = new ArrayList(ownersMap.values());
                }
            }
        }
        else {
            owners = new ArrayList();
            owners.add(belongsTo);
        }
    }

    /**
     * Populates the domain class properties map
     *
     * @param propertyDescriptors The property descriptors
     */
    private void populateDomainClassProperties(PropertyDescriptor[] propertyDescriptors) {
        for (PropertyDescriptor descriptor : propertyDescriptors) {

            if (descriptor.getPropertyType() == null) {
                // indexed property
                continue;
            }

            // ignore certain properties
            if (GriffonDomainConfigurationUtil.isNotConfigurational(descriptor)) {
                GriffonDomainClassProperty property = new DefaultGriffonDomainClassProperty(this, descriptor);
                propertyMap.put(property.getName(), property);

                if (property.isIdentity()) {
                    identifier = property;
                }
                else if (property.getName().equals(GriffonDomainClassProperty.VERSION)) {
                    version = property;
                }
            }
        }
    }

    /**
     * Retrieves the association map
     */
    @SuppressWarnings("unchecked")
    public Map getAssociationMap() {
        if (relationshipMap == null) {
            relationshipMap = getStaticPropertyValue(GriffonDomainClassProperty.HAS_MANY, Map.class);
            if (relationshipMap == null) {
                relationshipMap = new HashMap();
            }

            Class<?> theClass = getClazz();
            while (theClass != Object.class) {
                theClass = theClass.getSuperclass();
                ClassPropertyFetcher propertyFetcher = ClassPropertyFetcher.forClass(theClass);
                Map superRelationshipMap = propertyFetcher.getStaticPropertyValue(GriffonDomainClassProperty.HAS_MANY, Map.class);
                if (superRelationshipMap != null && !superRelationshipMap.equals(relationshipMap)) {
                    relationshipMap.putAll(superRelationshipMap);
                }
            }
        }
        return relationshipMap;
    }

    /**
     * Calculates the relationship type based other types referenced
     */
    private void establishRelationships() {
        for (Object o : propertyMap.values()) {
            DefaultGriffonDomainClassProperty currentProp = (DefaultGriffonDomainClassProperty) o;
            if (!currentProp.isPersistent()) continue;

            Class currentPropType = currentProp.getType();
            // establish if the property is a one-to-many
            // if it is a Set and there are relationships defined
            // and it is defined as persistent
            if(currentPropType != null) {
                if (Collection.class.isAssignableFrom(currentPropType) || Map.class.isAssignableFrom(currentPropType)) {
                    establishRelationshipForCollection(currentProp);
                }
                // otherwise if the type is a domain class establish relationship
                else if (DomainClassArtifactHandler.isDomainClass(currentPropType) &&
                        currentProp.isPersistent()) {
                    establishDomainClassRelationship(currentProp);
                }
            }
        }
    }

    /**
     * Establishes a relationship for a java.util.Set
     *
     * @param property The collection property
     */
    @SuppressWarnings("unchecked")
    private void establishRelationshipForCollection(DefaultGriffonDomainClassProperty property) {
        // is it a relationship
        Class<?> relatedClassType = getRelatedClassType(property.getName());

        if (relatedClassType != null) {
            // set the referenced type in the property
            property.setReferencedPropertyType(relatedClassType);

            // if the related type is a domain class
            // then figure out what kind of relationship it is
            if (DomainClassArtifactHandler.isDomainClass(relatedClassType)) {

                // check the relationship defined in the referenced type
                // if it is also a Set/domain class etc.
                Map relatedClassRelationships = GriffonDomainConfigurationUtil.getAssociationMap(relatedClassType);
                Class<?> relatedClassPropertyType = null;

                // First check whether there is an explicit relationship
                // mapping for this property (as provided by "mappedBy").
                String mappingProperty = (String)mappedBy.get(property.getName());
                if (!GriffonNameUtils.isBlank(mappingProperty)) {
                    // First find the specified property on the related class, if it exists.
                    PropertyDescriptor pd = findProperty(GriffonClassUtils.getPropertiesOfType(relatedClassType, getClazz()), mappingProperty);

                    // If a property of the required type does not exist, search
                    // for any collection properties on the related class.
                    if (pd == null) pd = findProperty(GriffonClassUtils.getPropertiesAssignableToType(relatedClassType, Collection.class), mappingProperty);

                    // We've run out of options. The given "mappedBy"
                    // setting is invalid.
                    if (pd == null) {
                        throw new GriffonDomainException("Non-existent mapping property ["+mappingProperty+"] specified for property ["+property.getName()+"] in class ["+getClazz()+"]");
                    }

                    // Tie the properties together.
                    relatedClassPropertyType = pd.getPropertyType();
                    property.setReferencePropertyName(pd.getName());
                }
                else {
                    // if the related type has a relationships map it may be a many-to-many
                    // figure out if there is a many-to-many relationship defined
                    if (isRelationshipManyToMany(property, relatedClassType, relatedClassRelationships)) {
                        String relatedClassPropertyName = null;
                        Map relatedClassMappedBy = GriffonDomainConfigurationUtil.getMappedByMap(relatedClassType);
                        // retrieve the relationship property
                        for (Object o : relatedClassRelationships.keySet()) {
                            String currentKey = (String) o;
                            String mappedByProperty = (String) relatedClassMappedBy.get(currentKey);
                            if (mappedByProperty != null && !mappedByProperty.equals(property.getName())) continue;
                            Class<?> currentClass = (Class<?>)relatedClassRelationships.get(currentKey);
                            if (currentClass.isAssignableFrom(getClazz())) {
                                relatedClassPropertyName = currentKey;
                                break;
                            }
                        }

                        // if there is one defined get the type
                        if (relatedClassPropertyName != null) {
                            relatedClassPropertyType = GriffonClassUtils.getPropertyType(relatedClassType, relatedClassPropertyName);
                        }
                    }
                    // otherwise figure out if there is a one-to-many relationship by retrieving any properties that are of the related type
                    // if there is more than one property then (for the moment) ignore the relationship
                    if (relatedClassPropertyType == null) {
                        PropertyDescriptor[] descriptors = GriffonClassUtils.getPropertiesOfType(relatedClassType, getClazz());

                        if (descriptors.length == 1) {
                            relatedClassPropertyType = descriptors[0].getPropertyType();
                            property.setReferencePropertyName(descriptors[0].getName());
                        }
                        else if (descriptors.length > 1) {
                            // try now to use the class name by convention
                            String classPropertyName = getPropertyName();
                            PropertyDescriptor pd = findProperty(descriptors, classPropertyName);
                            if (pd == null) {
                                throw new GriffonDomainException("Property ["+property.getName()+"] in class ["+getClazz()+"] is a bidirectional one-to-many with two possible properties on the inverse side. "+
                                        "Either name one of the properties on other side of the relationship ["+classPropertyName+"] or use the 'mappedBy' static to define the property " +
                                        "that the relationship is mapped with. Example: static mappedBy = ["+property.getName()+":'myprop']");
                            }
                            relatedClassPropertyType = pd.getPropertyType();
                            property.setReferencePropertyName(pd.getName());
                        }
                    }
                }

                establishRelationshipForSetToType(property,relatedClassPropertyType);
                // if its a many-to-many figure out the owning side of the relationship
                if (property.isManyToMany()) {
                    establishOwnerOfManyToMany(property, relatedClassType);
                }
            }
            // otherwise set it to not persistent as you can't persist
            // relationships to non-domain classes
            else {
                property.setBasicCollectionType(true);
            }
        }
        else if (!Map.class.isAssignableFrom(property.getType())) {
            // no relationship defined for set.
            // set not persistent
            property.setPersistent(false);
        }
    }

    /**
     * Finds a property type is an array of descriptors for the given property name
     *
     * @param descriptors The descriptors
     * @param propertyName The property name
     * @return The Class or null
     */
    private PropertyDescriptor findProperty(PropertyDescriptor[] descriptors, String propertyName) {
        PropertyDescriptor d = null;
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(propertyName)) {
                d = descriptor;
                break;
            }
        }
        return d;
    }

    /**
     * Find out if the relationship is a many-to-many
     *
     * @param property The property
     * @param relatedClassType The related type
     * @param relatedClassRelationships The related types relationships
     * @return <code>true</code> if the relationship is a many-to-many
     */
    @SuppressWarnings("unchecked")
    private boolean isRelationshipManyToMany(DefaultGriffonDomainClassProperty property,
            Class<?> relatedClassType, Map relatedClassRelationships) {
        return relatedClassRelationships != null &&
            !relatedClassRelationships.isEmpty() &&
            !relatedClassType.equals(property.getDomainClass().getClazz());
    }

    /**
     * Inspects a related classes' ownership settings against this properties class' ownership
     * settings to find out who owns a many-to-many relationship
     *
     * @param property The property
     * @param relatedClassType The related type
     */
    @SuppressWarnings("unchecked")
    private void establishOwnerOfManyToMany(DefaultGriffonDomainClassProperty property, Class<?> relatedClassType) {
        ClassPropertyFetcher cpf = ClassPropertyFetcher.forClass(relatedClassType);
        Object relatedBelongsTo = cpf.getPropertyValue(GriffonDomainClassProperty.BELONGS_TO);
        boolean owningSide = false;
        boolean relatedOwner = isOwningSide(relatedClassType, owners);
        final Class<?> propertyClass = property.getDomainClass().getClazz();
        if (relatedBelongsTo instanceof Collection) {
            final Collection associatedOwners = (Collection)relatedBelongsTo;
            owningSide = isOwningSide(propertyClass, associatedOwners);
        }
        else if (relatedBelongsTo instanceof Class) {
            final Collection associatedOwners = new ArrayList();
            associatedOwners.add(relatedBelongsTo);
            owningSide = isOwningSide(propertyClass, associatedOwners);
        }
        property.setOwningSide(owningSide);
        if (relatedOwner && property.isOwningSide()) {
            throw new GriffonDomainException("Domain classes [" + propertyClass +
                    "] and [" + relatedClassType +
                    "] cannot own each other in a many-to-many relationship. Both contain belongsTo definitions that reference each other.");
        }
        if (!relatedOwner && !property.isOwningSide() && !(property.isCircular() && property.isManyToMany())) {
            throw new GriffonDomainException("No owner defined between domain classes ["+propertyClass+"] and ["+relatedClassType+"] in a many-to-many relationship. Example: static belongsTo = "+relatedClassType.getName());
        }
    }

    private boolean isOwningSide(Class<?> relatedClassType, Collection<Class<?>> potentialOwners) {
        boolean relatedOwner = false;
        for (Class<?> relatedClass : potentialOwners) {
            if (relatedClass.isAssignableFrom(relatedClassType)) {
                relatedOwner = true;
                break;
            }
        }
        return relatedOwner;
    }

    /**
     * Establishes whether the relationship is a bi-directional or uni-directional one-to-many
     * and applies the appropriate settings to the specified property
     *
     * @param property The property to apply settings to
     * @param relatedClassPropertyType The related type
     */
    private void establishRelationshipForSetToType(DefaultGriffonDomainClassProperty property,
            Class<?> relatedClassPropertyType) {

        if (relatedClassPropertyType == null) {
            // uni-directional one-to-many
            property.setOneToMany(true);
            property.setBidirectional(false);
        }
        else if (Collection.class.isAssignableFrom(relatedClassPropertyType) || Map.class.isAssignableFrom(relatedClassPropertyType) ){
            // many-to-many
            property.setManyToMany(true);
            property.setBidirectional(true);
        }
        else if (DomainClassArtifactHandler.isDomainClass(relatedClassPropertyType)) {
            // bi-directional one-to-many
            property.setOneToMany(true);
            property.setBidirectional(true);
        }
    }

    /**
     * Establish relationship with related domain class
     *
     * @param property Establishes a relationship between this class and the domain class property
     */
    @SuppressWarnings("unchecked")
    private void establishDomainClassRelationship(DefaultGriffonDomainClassProperty property) {
        Class<?> propType = property.getType();

        // establish relationship to type
        Map relatedClassRelationships = GriffonDomainConfigurationUtil.getAssociationMap(propType);
        @SuppressWarnings("hiding")
        Map mappedBy = GriffonDomainConfigurationUtil.getMappedByMap(propType);

        Class<?> relatedClassPropertyType = null;

        // if there is a relationships map use that to find out
        // whether it is mapped to a Set
        if (relatedClassRelationships != null && !relatedClassRelationships.isEmpty()) {

            String relatedClassPropertyName = findOneToManyThatMatchesType(property, relatedClassRelationships);
            PropertyDescriptor[] descriptors = GriffonClassUtils.getPropertiesOfType(getClazz(), property.getType());

            // if there is only one property on many-to-one side of the relationship then
            // try to establish if it is bidirectional
            if (descriptors.length == 1 && isNotMappedToDifferentProperty(property,relatedClassPropertyName, mappedBy)) {
                if (!GriffonNameUtils.isBlank(relatedClassPropertyName)) {
                    property.setReferencePropertyName(relatedClassPropertyName);
                    // get the type of the property
                    relatedClassPropertyType = GriffonClassUtils.getPropertyType(propType, relatedClassPropertyName);
                }
            }
            // if there is more than one property on the many-to-one side then we need to either
            // find out if there is a mappedBy property or whether a convention is used to decide
            // on the mapping property
            else if (descriptors.length > 1) {
                if (mappedBy.containsValue(property.getName())) {
                    for (Object o : mappedBy.keySet()) {
                        String mappedByPropertyName = (String) o;
                        if (property.getName().equals(mappedBy.get(mappedByPropertyName))) {
                            Class<?> mappedByRelatedType = (Class<?>) relatedClassRelationships.get(mappedByPropertyName);
                            if (mappedByRelatedType != null && propType.isAssignableFrom(mappedByRelatedType))
                                relatedClassPropertyType = GriffonClassUtils.getPropertyType(propType, mappedByPropertyName);
                        }
                    }
                }
                else {
                    String classNameAsProperty = GriffonNameUtils.getPropertyName(propType);
                    if (property.getName().equals(classNameAsProperty) && !mappedBy.containsKey(relatedClassPropertyName)) {
                        relatedClassPropertyType = GriffonClassUtils.getPropertyType(propType, relatedClassPropertyName);
                    }
                }
            }
        }
        // otherwise retrieve all the properties of the type from the associated class
        if (relatedClassPropertyType == null) {
            PropertyDescriptor[] descriptors = GriffonClassUtils.getPropertiesOfType(propType, getClazz());

            // if there is only one then the association is established
            if (descriptors.length == 1) {
                relatedClassPropertyType = descriptors[0].getPropertyType();
            }
        }

        //    establish relationship based on this type
        establishDomainClassRelationshipToType(property, relatedClassPropertyType);
    }

    @SuppressWarnings("unchecked")
    private boolean isNotMappedToDifferentProperty(GriffonDomainClassProperty property,
            String relatedClassPropertyName, @SuppressWarnings("hiding") Map mappedBy) {

        String mappedByForRelation = (String)mappedBy.get(relatedClassPropertyName);
        if (mappedByForRelation == null) return true;
        if (!property.getName().equals(mappedByForRelation)) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    private String findOneToManyThatMatchesType(DefaultGriffonDomainClassProperty property, Map relatedClassRelationships) {
        String relatedClassPropertyName = null;

        for (Object o : relatedClassRelationships.keySet()) {
            String currentKey = (String) o;
            Class<?> currentClass = (Class<?>)relatedClassRelationships.get(currentKey);

            if (property.getDomainClass().getClazz().getName().equals(currentClass.getName())) {
                relatedClassPropertyName = currentKey;
                break;
            }
        }
        return relatedClassPropertyName;
    }

    private void establishDomainClassRelationshipToType(DefaultGriffonDomainClassProperty property, Class<?> relatedClassPropertyType) {
        // uni-directional one-to-one

        if (relatedClassPropertyType == null) {
            if (hasOneMap.containsKey(property.getName())) {
                property.setHasOne(true);
            }
            property.setOneToOne(true);
            property.setBidirectional(false);
        }
        // bi-directional many-to-one
        else if (Collection.class.isAssignableFrom(relatedClassPropertyType)||Map.class.isAssignableFrom(relatedClassPropertyType)) {
            property.setManyToOne(true);
            property.setBidirectional(true);
        }
        // bi-directional one-to-one
        else if (DomainClassArtifactHandler.isDomainClass(relatedClassPropertyType)) {
            if (hasOneMap.containsKey(property.getName())) {
                property.setHasOne(true);
            }

            property.setOneToOne(true);
            if (!getClazz().equals(relatedClassPropertyType)) {
                property.setBidirectional(true);
            }
        }
    }

    public boolean isOwningClass(Class domainClass) {
        return owners.contains(domainClass);
    }

    public GriffonDomainClassProperty[] getProperties() {
        return properties;
    }

    public GriffonDomainClassProperty getIdentifier() {
        return identifier;
    }

    public GriffonDomainClassProperty getVersion() {
        return version;
    }

    public GriffonDomainClassProperty[] getPersistentProperties() {
        return persistentProperties;
    }

    public GriffonDomainClassProperty getPropertyByName(String name) {
        if (propertyMap.containsKey(name)) {
            return propertyMap.get(name);
        }

        throw new InvalidPropertyException("No property found for name ["+name+"] for class ["+getClazz()+"]");
    }

    public String getFieldName(String propertyName) {
        return getPropertyByName(propertyName).getFieldName();
    }

    @Override
    public String getName() {
        return ClassUtils.getShortClassName(super.getName());
    }

    public boolean isOneToMany(String propertyName) {
        return getPropertyByName(propertyName).isOneToMany();
    }
    
    public boolean isManyToOne(String propertyName) {
        return getPropertyByName(propertyName).isManyToOne();
    }

    public Class<?> getRelatedClassType(String propertyName) {
        return (Class<?>)relationshipMap.get(propertyName);
    }

    @Override
    public String getPropertyName() {
        return GriffonNameUtils.getPropertyNameRepresentation(getClazz());
    }

    public boolean isBidirectional(String propertyName) {
        return getPropertyByName(propertyName).isBidirectional();
    }

    @SuppressWarnings("unchecked")
    public Map getConstrainedProperties() {
        if (constraints == null) {
            initializeConstraints();
        }
        return Collections.unmodifiableMap(constraints);
    }

    private void initializeConstraints() {
        // process the constraints
        // if (defaultConstraints != null) {
        //    constraints = GriffonDomainConfigurationUtil.evaluateConstraints(getClazz(), persistentProperties, defaultConstraints);
        // }  else {
        //    constraints = GriffonDomainConfigurationUtil.evaluateConstraints(getClazz(), persistentProperties);
        // }
    }

//    Validator getValidator();

    /**
     * Sets the validator for this domain class 
     * 
     * @param validator The domain class validator to set
     */
//    void setValidator(Validator validator);
    
//    public String getMappingStrategy() {
//        return mappingStrategy;
//    }
    
    public boolean isRoot() {
        return root;
    }

    @SuppressWarnings("unchecked")
    public Set<GriffonDomainClass> getSubClasses() {
        return subClasses;
    }

    public void refreshConstraints() {
        // if (defaultConstraints!=null) {
        //    constraints = GriffonDomainConfigurationUtil.evaluateConstraints(getClazz(), persistentProperties, defaultConstraints);
        // } else {
        //    constraints = GriffonDomainConfigurationUtil.evaluateConstraints(getClazz(), persistentProperties);
        // }
    }

    @SuppressWarnings("unchecked")
    public Map getMappedBy() {
        return mappedBy;
    }

    public boolean hasPersistentProperty(String propertyName) {
        for (GriffonDomainClassProperty persistentProperty : persistentProperties) {
            if (persistentProperty.getName().equals(propertyName)) return true;
        }
        return false;
    }

//    public void setMappingStrategy(String strategy) {}

    public void resetCaches() {
	    super.resetCaches();
    }
}