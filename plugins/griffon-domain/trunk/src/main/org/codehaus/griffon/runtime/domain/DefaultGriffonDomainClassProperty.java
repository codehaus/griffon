/* Copyright 2004-2005 the original author or authors.
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

import griffon.util.GriffonNameUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import griffon.core.ArtifactManager;
import griffon.util.GriffonClassUtils;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainClassProperty;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
// import org.codehaus.groovy.griffon.plugins.DomainClassGriffonPlugin;
// import org.codehaus.groovy.griffon.validation.ConstrainedProperty;
// import org.springframework.validation.Validator;

/**
 * Represents a property of a domain class and contains meta information about the
 * properties relationships, naming conventions and type.
 *
 * @author Graeme Rocher (Grails 0.1)
 */
public class DefaultGriffonDomainClassProperty implements GriffonDomainClassProperty {
    private GriffonDomainClass domainClass;
    private boolean persistent = true; // persistent by default
    private boolean identity;
    private boolean oneToMany;
    private String name;
    private Class<?> type;
    private boolean manyToMany;
    private boolean manyToOne;
    private boolean oneToOne;
    private boolean hasOne = false;
    private boolean bidirectional;

    private Class<?> referencedPropertyType;
    private GriffonDomainClass referencedDomainClass;
    private GriffonDomainClassProperty otherSide;
    private String naturalName;
    private boolean inherited;
    private int fetchMode = FETCH_LAZY;
    private boolean owningSide;
    private String referencePropertyName;
    // private boolean embedded;
    // private GriffonDomainClass component;
    private boolean basicCollectionType;

    /**
     * Constructor.
     * @param domainClass
     * @param descriptor
     */
    @SuppressWarnings("unchecked")
    public DefaultGriffonDomainClassProperty(GriffonDomainClass domainClass, PropertyDescriptor descriptor) {
        this.domainClass = domainClass;
        name = descriptor.getName();
        naturalName = GriffonNameUtils.getNaturalName(descriptor.getName());
        type = descriptor.getPropertyType();
        identity = descriptor.getName().equals(IDENTITY);

        // establish if property is persistent
        if (domainClass != null) {
            // figure out if this property is inherited
            if (!domainClass.isRoot()) {
                inherited = GriffonClassUtils.isPropertyInherited(domainClass.getClazz(), name);
            }
            List transientProps = getTransients();
            checkIfTransient(transientProps);

            establishFetchMode();
        }
    }

    /**
     * Evaluates the fetchmode.
     */
    @SuppressWarnings("unchecked")
    private void establishFetchMode() {
        Map fetchMap = domainClass.getPropertyValue(GriffonDomainClassProperty.FETCH_MODE, Map.class);
        if (fetchMap != null && fetchMap.containsKey(name)) {
            if ("eager".equals(fetchMap.get(name))) {
                fetchMode = FETCH_EAGER;
            }
        }
    }

    /**
     * Checks whether this property is transient
     *
     * @param transientProps The transient properties
     */
    @SuppressWarnings("unchecked")
    private void checkIfTransient(List transientProps) {
        if (transientProps == null) {
            return;
        }

        for (Object currentObj : transientProps) {
            // make sure its a string otherwise ignore. Note: Again maybe a warning?
            if (currentObj instanceof String) {
                String propertyName = (String)currentObj;
                // if the property name is on the not persistent list
                // then set persistent to false
                if (propertyName.equals(name)) {
                    persistent = false;
                    break;
                }
            }
        }
    }

    /**
     * Retrieves the transient properties
     *
     * @return A list of transient properties
     */
    @SuppressWarnings("unchecked")
    private List getTransients() {
        List allTransientProps = new ArrayList();
        List<GriffonDomainClass> allClasses = resolveAllDomainClassesInHierarchy();

        for (GriffonDomainClass currentDomainClass : allClasses) {
            List transientProps = currentDomainClass.getPropertyValue(TRANSIENT, List.class);
            if (transientProps != null) {
                allTransientProps.addAll(transientProps);
            }

            // Undocumented feature alert! Steve insisted on this :-)
//            List evanescent = currentDomainClass.getPropertyValue(EVANESCENT, List.class);
//            if (evanescent != null) {
//                allTransientProps.addAll(evanescent);
//            }
        }
        return allTransientProps;
    }

    /**
     * returns list of current domainclass and all of its superclasses.
     *
     * @return
     */
    private List<GriffonDomainClass> resolveAllDomainClassesInHierarchy() {
        List<GriffonDomainClass> allClasses = new ArrayList<GriffonDomainClass>();
        GriffonDomainClass currentDomainClass = domainClass;
        while (currentDomainClass != null) {
            allClasses.add(currentDomainClass);
            currentDomainClass = (GriffonDomainClass) ArtifactManager.getInstance().findGriffonClass(
                    currentDomainClass.getClazz().getSuperclass().getName(), GriffonDomainClass.TYPE);
        }
        return allClasses;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public Class getType() {
        return type;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isOptional() {
        // ConstrainedProperty constrainedProperty = (ConstrainedProperty) domainClass.getConstrainedProperties().get(name);
        // return (constrainedProperty != null) && constrainedProperty.isNullable();
        return false;
    }

    public boolean isIdentity() {
        return identity;
    }

    public boolean isOneToMany() {
        return oneToMany;
    }

    public boolean isManyToOne() {
        return manyToOne;
    }

    public String getFieldName() {
        return getName().toUpperCase();
    }

    public boolean isOneToOne() {
        return oneToOne;
    }

    public GriffonDomainClass getDomainClass() {
        return domainClass;
    }

    public boolean isManyToMany() {
        return manyToMany;
    }

    /**
     * @param manyToMany The manyToMany to set.
     */
    protected void setManyToMany(boolean manyToMany) {
        this.manyToMany = manyToMany;
    }

    /**
     * @param oneToMany The oneToMany to set.
     */
    protected void setOneToMany(boolean oneToMany) {
        this.oneToMany = oneToMany;
    }

    /**
     * @param manyToOne The manyToOne to set.
     */
    protected void setManyToOne(boolean manyToOne) {
        this.manyToOne = manyToOne;
    }

    /**
     * @param oneToOne The oneToOne to set.
     */
    protected void setOneToOne(boolean oneToOne) {
        this.oneToOne = oneToOne;
    }

    /**
     * Set whether the foreign key is stored in the parent or child in a one-to-one
     * @param isHasOne True if its stored in the parent
     */
    protected void setHasOne(boolean isHasOne) {
        this.hasOne = isHasOne;
    }

    /**
     * @return True if the foreign key in a one-to-one is stored in the parent
     */
    public boolean isHasOne() {
        return hasOne;
    }

    /**
     * @param persistent The persistent to set.
     */
    protected void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * Sets whether the relationship is bidirectional or not
     */
    protected void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    public String getTypePropertyName() {
        String shortTypeName = ClassUtils.getShortClassName(type);
        return shortTypeName.substring(0,1).toLowerCase(Locale.ENGLISH) + shortTypeName.substring(1);
    }

    @SuppressWarnings("unchecked")
    public Class getReferencedPropertyType() {
        if (isDomainAssociation()) {
            return referencedPropertyType;
        }

        return getType();
    }

    private boolean isDomainAssociation() {
        return (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) &&
            referencedPropertyType != null;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    protected void setReferencedPropertyType(Class<?> referencedPropertyType) {
        this.referencedPropertyType = referencedPropertyType;
    }

    public GriffonDomainClass getReferencedDomainClass() {
        return referencedDomainClass;
    }

    public void setReferencedDomainClass(GriffonDomainClass referencedDomainClass) {
        if (referencedDomainClass != null) {
            this.referencedDomainClass = referencedDomainClass;
            this.referencedPropertyType = referencedDomainClass.getClazz();
        }
    }

    public boolean isAssociation() {
        return isOneToMany() ||
               isOneToOne() ||
               isManyToOne() ||
               isManyToMany(); // ||
//               isEmbedded();
    }

    public boolean isEnum() {
        return Enum.class.isAssignableFrom(getType());
    }

    public String getNaturalName() {
        return naturalName;
    }

    @Override
    public String toString() {
        String assType = null;
        if (isManyToMany()) {
            assType = "many-to-many";
        }
        else if (isOneToMany()) {
            assType = "one-to-many";
        }
        else if (isOneToOne()) {
            assType = "one-to-one";
        }
        else if (isManyToOne()) {
            assType = "many-to-one";
        }
//        else if (isEmbedded()) {
//            assType = "embedded";
//        }
        return new ToStringBuilder(this)
                   .append("name", name)
                   .append("type", type)
                   .append("persistent", isPersistent())
                   .append("optional", isOptional())
                   .append("association", isAssociation())
                   .append("bidirectional", isBidirectional())
                   .append("association-type", assType)
                   .toString();
    }

    public GriffonDomainClassProperty getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(GriffonDomainClassProperty property) {
        if (!equals(property)) {
            setBidirectional(true);
            if (isOneToOne() && property.isOneToMany()) {
                setOneToOne(false);
                setManyToOne(true);
            }
        }
        otherSide = property;
    }

    public boolean isInherited() {
        return inherited;
    }

    public int getFetchMode() {
        return fetchMode ;
    }

    public boolean isOwningSide() {
        return isHasOne() || owningSide;
    }

    public void setOwningSide(boolean b) {
        owningSide = b;
    }

    @SuppressWarnings("unchecked")
    public boolean isCircular() {
        if (otherSide != null) {
            if (otherSide.getDomainClass().getClazz().isAssignableFrom(domainClass.getClazz())) {
                return true;
            }
        }
        else if (getReferencedPropertyType().isAssignableFrom(domainClass.getClazz())) {
            return true;
        }
        return false;
    }

    public void setReferencePropertyName(String name) {
        referencePropertyName = name;
    }

    public String getReferencedPropertyName() {
        return referencePropertyName;
    }

//    public boolean isEmbedded() {
//        return embedded;
//    }

//    public GriffonDomainClass getComponent() {
//        return component;
//    }

//    public void setEmbedded(boolean isEmbedded) {
//        embedded = isEmbedded;
//        if (isEmbedded) {
//            component = new ComponentDomainClass(getType());
//        }
//    }

    /**
     * Overriddent equals to take into account inherited properties
     * e.g. childClass.propertyName is equal to parentClass.propertyName if the types match and
     * childClass.property.isInherited
     *
     * @param o the Object to compare this property to
     * @return boolean indicating equality of the two objects
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof GriffonDomainClassProperty) {
            if (!super.equals(o)){
                GriffonDomainClassProperty otherProp = (GriffonDomainClassProperty) o;
                boolean namesMatch = otherProp.getName().equals(getName());
                boolean typesMatch = otherProp.getReferencedPropertyType().equals(getReferencedPropertyType());
                Class<?> myActualClass = getDomainClass().getClazz();
                Class<?> otherActualClass = otherProp.getDomainClass().getClazz() ;
                boolean classMatch = otherActualClass.isAssignableFrom(myActualClass) ||
                    myActualClass.isAssignableFrom(otherActualClass);
                return namesMatch && typesMatch && classMatch;
            }

            return true;
        }

        return false;
    }

    public void setBasicCollectionType(boolean b) {
        basicCollectionType = b;
    }

    public boolean isBasicCollectionType() {
        return basicCollectionType;
    }
}