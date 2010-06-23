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
package griffon.domain.artifacts;

/**
 * A property of a GriffonDomainArtifactClass instance
 * 
 * @author Graeme Rocher (Grails)
 */
public interface GriffonDomainArtifactClassProperty {
    String IDENTITY = "id";
    String VERSION = "version";
    String TRANSIENT = "transients";
    String CONSTRAINTS = "constraints";
//    String EVANESCENT = "evanescent";
    String RELATES_TO_MANY = "relatesToMany";
    String META_CLASS = "metaClass";
    String CLASS = "class";
    String MAPPING_STRATEGY = "mapWith";
    String MAPPED_BY = "mappedBy";    
    String BELONGS_TO = "belongsTo";
    String HAS_MANY = "hasMany";
    String HAS_ONE = "hasOne";
    String FETCH_MODE = "fetchMode";
    String DATE_CREATED = "dateCreated";
    String MAPPING = "mapping";
    String NAMED_QUERIES = "namedQueries";
    String LAST_UPDATED = "lastUpdated";
    String SORT = "sort";
//    String EMBEDDED = "embedded";
    int FETCH_EAGER = 1;
    int FETCH_LAZY = 0;

    /**
     * Returns the configured fetch mode for the property
     */
    public int getFetchMode();

    /**
     * Returns the name of the property
     * @return The property name
     */
    public String getName();

    /**
     * Returns the type for the domain class
     * @return  The property type
     */
    public Class getType();
    
    /**
     * <p>Returns the referenced property type. This differs from getType() in that in
     * the case of an Association it will return the type of the elements contained within the Collection,
     * otherwise it will delegate to getType();</p>
     * 
     * @return The referenced type
     */
    public Class getReferencedPropertyType();
    
    /**
     * <p>Returns the other side of a bidirectional association
     * 
     * @return The other side of the relationship or null if not known
     */
    public GriffonDomainArtifactClassProperty getOtherSide();
    
    /**
     * Returns the class type as a property name representation
     * 
     * @return The property name representation
     */
    public String getTypePropertyName();
    
    /**
     * Returns the parent domain class of the property instance
     * @return The parent domain class
     */
    public GriffonDomainArtifactClass getDomainArtifactClass();

    /**
     * Returns true if the domain class property is a persistent property
     * @return Whether the property is persistent
     */
    public boolean isPersistent();

    /**
     * Returns true if the property is required
     * @return Whether the property is optional
     */
    public boolean isOptional();

    /**
     * Returns true of the property is an identifier
     * @return Whether the property is the identifier
     */
    public boolean isIdentity();
    
    /**
     * Returns true if the property is a one-to-many relationship
     * @return Whether it is a oneToMany
     */
    public boolean isOneToMany();
    
    /**
     * Returns true if the property is a many-to-one relationship
     * @return Whether it is a manyToOne
     */
    public boolean isManyToOne();
    
    /**
     * Returns true if the property is a many-to-many relationship
     * @return True if it is a manyToMany
     */    
    public boolean isManyToMany();
    
    /**
     * Returns true if the property is a bi-directional relationship
     * @return A boolean value
     */
    public boolean isBidirectional();

    /**
     * Returns the domain field name for this property
     */
    public String getFieldName();

    /**
     * Returns true if the property is a one-to-one relationship 
     * @return True if it is a one-to-one relationship
     */
    public boolean isOneToOne();

    /**
     * Returns the GriffonDomainArtifactClass of a relationship property or null 
     * if the property is not a relationship property
     * 
     * @return The GriffonDomainArtifactClass
     */
    public GriffonDomainArtifactClass getReferencedDomainArtifactClass();
    
    /**
     * Returns true if this property is a relationship property
     * @return True if it is an associative property
     */
    public boolean isAssociation();

    /**
     * Returns true if this properties type is an enum
     * @return True if it is
     */
    public boolean isEnum();

    /**
     * @return The natural name representation of the property (eg. 'lastName' becomes 'Last Name'
     */
    public String getNaturalName();

    /**
     * Sets the references domain class on the property
     * @param referencedGriffonDomainArtifactClass
     */
    void setReferencedDomainArtifactClass(GriffonDomainArtifactClass referencedGriffonDomainArtifactClass);

    /**
     * Sets the other side of an associative property
     * @param referencedProperty
     */
    void setOtherSide(GriffonDomainArtifactClassProperty referencedProperty);
    
    /**
     * Whether the property is inherited from a super class
     * @return True if its inherited
     */
    boolean isInherited();

    /**
     * Whether this side of the association is the "owning" side
     * 
     * @return True if it is the owning side
     */
    public boolean isOwningSide();

    /**
     * Whether the relationship is cirucular
     * 
     * @return True if it is
     */
    public boolean isCircular();

    /**
     * Retrieves the name of property referenced by this property if it is 
     * an association and is known, otherwise null
     * 
     * @return The name of the prop
     */
    public String getReferencedPropertyName();

    /**
     * Returns true if this propert is an embedded component
     *
     * @return True if it is, false otherwise
     */
//    boolean isEmbedded();

    /**
     * If #isEmbedded returns true then this method can be called to obtain a reference to the embedded component, which implements the GriffonDomainArtifactClass
     * interface
     *
     * @see org.codehaus.griffon.commons.GriffonDomainArtifactClass
     * @see GriffonDomainArtifactClassProperty#isEmbedded() 
     *
     * @return The component or null if it is not an embedded component
     */
//    GriffonDomainArtifactClass getComponent();

    void setOwningSide(boolean b);

    /**
     * Return whether this is a collection of basic types like String, Integer etc.
     * @return True if it is a collection of basic types
     */
    boolean isBasicCollectionType();

    boolean isHasOne();

    boolean isDerived();

    void setDerived(boolean isDerived);
}
