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
package griffon.core.artifacts;

import griffon.util.GriffonNameUtils;
import griffon.util.GriffonClassUtils;
import griffon.exceptions.NewInstanceCreationException;
import griffon.core.ArtifactInfo;
import groovy.lang.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for Griffon types that provides common functionality for
 * evaluating conventions within classes
 * 
 * @author Steven Devijver
 * @author Graeme Rocher
 * @author Andres Almiray
 * 
 * @since 0.1
 */
public abstract class AbstractGriffonArtifactClass implements GriffonArtifactClass {
    static final Log LOG = LogFactory.getLog(AbstractGriffonArtifactClass.class);

    private final Class clazz;
    private final Object reference;
    private final ArtifactInfo artifactInfo;
    private final String fullName;
    private final String name;
    private final String packageName;
    private final String naturalName;
    private final String shortName;
    private final String propertyName;
    private final String logicalPropertyName;
    private final ClassPropertyFetcher classPropertyFetcher;
    
    /**
     * <p>Contructor to be used by all child classes to create a
     * new instance and get the name right.
     *
     * @param artifactInfo the Griffon artifact
     */
    public AbstractGriffonArtifactClass(ArtifactInfo artifactInfo, String trailingName) {
        if (artifactInfo == null) {
            throw new IllegalArgumentException("Clazz parameter should not be null");
        }
        this.artifactInfo = artifactInfo;
        this.clazz = artifactInfo.getClazz();
        this.reference = GriffonClassUtils.instantiateClass(clazz);
        // this.reference = instantiateClass(clazz);
        this.fullName = clazz.getName();
        this.packageName = ClassUtils.getPackageName(clazz);
        this.naturalName = GriffonNameUtils.getNaturalName(clazz.getName());
        this.shortName = ClassUtils.getShortClassName(clazz);        
        this.name = GriffonNameUtils.getLogicalName(clazz, trailingName);
        this.propertyName = GriffonNameUtils.getPropertyNameRepresentation(this.shortName);
        if(StringUtils.isBlank(this.name)) {
            this.logicalPropertyName = this.propertyName;
        } else {
            this.logicalPropertyName = GriffonNameUtils.getPropertyNameRepresentation(this.name);
        }
        this.classPropertyFetcher = new ClassPropertyFetcher(clazz, new ClassPropertyFetcher.ReferenceInstanceCallback() {
            public Object getReferenceInstance() {
                return AbstractGriffonArtifactClass.this.getReferenceInstance();
            }
        });
    }

    // can't call GriffonClassUtils.instantiateClass(Class) WTF?!
//    private static Object instantiateClass(Class clazz) {
//       try {
//           return clazz.newInstance();
//       } catch(Exception e) {
//           throw new griffon.exceptions.BeanInstantiationException("Could not create an instance of "+ clazz, e);
//       }
//    }

    public String getShortName() {
        return this.shortName;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public ArtifactInfo getArtifactInfo() {
        return this.artifactInfo;
    }

    public Object newInstance() {
        try {
            return artifactInfo.newInstance();
        } catch (Exception e) {
            Throwable targetException = null;
            if (e instanceof InvocationTargetException) {
                targetException = ((InvocationTargetException)e).getTargetException();
            } else {
                targetException = e;
            }
            throw new NewInstanceCreationException("Could not create a new instance of class [" + getClazz().getName() + "]!", targetException);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getNaturalName() {
        return this.naturalName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public String getLogicalPropertyName() {
        return this.logicalPropertyName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public Object getReferenceInstance() {
        if(this.reference instanceof GroovyObject) {
            // GriffonClassUtils.getExpandoMetaClass(clazz) removed here
            ((GroovyObject)this.reference).setMetaClass(getMetaClass());
        }
        return this.reference;
    }
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        return PropertyUtils.getPropertyDescriptors(clazz);
    }
    
    public Class getPropertyType(String name) {
        return classPropertyFetcher.getPropertyType(name);
    }
    
    public boolean isReadableProperty(String name) {
        return classPropertyFetcher.isReadableProperty(name);
    }
    
    public boolean hasMetaMethod(String name) {
        return hasMetaMethod(name, null);
    }
    
    public boolean hasMetaMethod(String name, Object[] args) {
        return (getMetaClass().getMetaMethod(name, args) != null);
    }
    
    public boolean hasMetaProperty(String name) {
        return (getMetaClass().getMetaProperty(name) != null);
    }
    
    /**
     * <p>Looks for a property of the reference instance with a given name and type.</p>
     * <p>If found its value is returned. We follow the Java bean conventions with augmentation for groovy support
     * and static fields/properties. We will therefore match, in this order:
     * </p>
     * <ol>
     * <li>Public static field
     * <li>Public static property with getter method
     * <li>Standard public bean property (with getter or just public field, using normal introspection)
     * </ol>
     *
     *
     * @return property value or null if no property or static field was found
     */
    protected Object getPropertyOrStaticPropertyOrFieldValue(String name, Class type) {
        Object value = classPropertyFetcher.getPropertyValue(name);
        return returnOnlyIfInstanceOf(value, type);
    }
    
    /**
     * Get the value of the named property, with support for static properties in both Java and Groovy classes
     * (which as of Groovy JSR 1.0 RC 01 only have getters in the metaClass)
     * @param name
     * @param type
     * @return The property value or null
     */
    public Object getPropertyValue(String name, Class type) {
        Object value = classPropertyFetcher.getPropertyValue(name);
        if (value == null) {
            // Groovy workaround
            Object inst = getReferenceInstance();
            if (inst instanceof GroovyObject) {
                MetaProperty metaProperty = getMetaClass().getMetaProperty(name);
                if(metaProperty != null) {
                    value = metaProperty.getProperty(inst);
                }
            }
        }
        return returnOnlyIfInstanceOf(value, type);
    }
    
    public Object getPropertyValueObject(String name) {
        return getPropertyValue(name, Object.class);
    }

    private Object returnOnlyIfInstanceOf(Object value, Class type) {
        if ((value != null) && (type==Object.class || GriffonClassUtils.isGroovyAssignableFrom( type, value.getClass()))) {
            return value;
        } else {
            return null;
        }
    }

    public Object getPropertyValue(String name) {
        return getPropertyOrStaticPropertyOrFieldValue(name, Object.class);
    }

    public boolean hasProperty(String name) {
        return PropertyUtils.isReadable(reference, name);
    }

    /**
     * @return the metaClass
     */
    public MetaClass getMetaClass() {
        return GroovySystem.getMetaClassRegistry().getMetaClass(clazz);
        //return GriffonClassUtils.getExpandoMetaClass(clazz);
    }

    public String toString() {
        return "ArtifactClass > " + getName();
    }
}
