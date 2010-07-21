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
package griffon.util;

import groovy.lang.*;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import griffon.exceptions.*;

/**
 * @author Graeme Rocher
 * @since 08-Jul-2005
 * 
 * Class containing utility methods for dealing with Griffon class artifacts
 * 
 */
public final class GriffonClassUtils {
    private static final String PROPERTY_SET_PREFIX = "set";
    public static final Map PRIMITIVE_TYPE_COMPATIBLE_CLASSES = new HashMap();

    /**
     * Just add two entries to the class compatibility map
     * @param left
     * @param right
     */
    private static final void registerPrimitiveClassPair(Class left, Class right)
    {
        PRIMITIVE_TYPE_COMPATIBLE_CLASSES.put( left, right);
        PRIMITIVE_TYPE_COMPATIBLE_CLASSES.put( right, left);
    }

    static
    {
        registerPrimitiveClassPair(Boolean.class, boolean.class);
        registerPrimitiveClassPair(Integer.class, int.class);
        registerPrimitiveClassPair(Short.class, short.class);
        registerPrimitiveClassPair(Byte.class, byte.class);
        registerPrimitiveClassPair(Character.class, char.class);
        registerPrimitiveClassPair(Long.class, long.class);
        registerPrimitiveClassPair(Float.class, float.class);
        registerPrimitiveClassPair(Double.class, double.class);
        BeanUtilsBean.setInstance(new BeanUtilsBean2());
    }

    /**
     *
     * Returns true if the specified property in the specified class is of the specified type
     *
     * @param clazz The class which contains the property
     * @param propertyName The property name
     * @param type The type to check
     *
     * @return A boolean value
     */
    public static boolean isPropertyOfType( Class clazz, String propertyName, Class type ) {
        try {
            Class propType = getPropertyType( clazz, propertyName );
            return propType != null && propType.equals(type);
        }
        catch(Exception e) {
            return false;
        }
    }

    /**
     *
     *
     * @param clazz
     * @throws BeanInstantiationException
     *
     * @return
     */
    public static Object instantiateClass(Class clazz) {
       try {
           return clazz.newInstance();
       } catch(Exception e) {
           throw new BeanInstantiationException("Could not create an instance of "+ clazz, e);
       } 
    }

    /**
     * Returns the value of the specified property and type from an instance of the specified Griffon class
     *
     * @param clazz The name of the class which contains the property
     * @param propertyName The property name
     * @param propertyType The property type
     *
     * @return The value of the property or null if none exists
     */
    public static Object getPropertyValueOfNewInstance(Class clazz, String propertyName, Class propertyType) {
        // validate
        if(clazz == null || StringUtils.isBlank(propertyName))
            return null;

        Object instance = null;
        try {
            instance = instantiateClass(clazz);
        } catch (BeanInstantiationException e) {
            return null;
        }

        return getPropertyOrStaticPropertyOrFieldValue(instance, propertyName);
    }

    /**
     * Returns the value of the specified property and type from an instance of the specified Griffon class
     *
     * @param clazz The name of the class which contains the property
     * @param propertyName The property name
     *
     * @return The value of the property or null if none exists
     */
    public static Object getPropertyValueOfNewInstance(Class clazz, String propertyName) {
        // validate
        if(clazz == null || StringUtils.isBlank(propertyName))
            return null;

        Object instance = null;
        try {
            instance = instantiateClass(clazz);
        } catch (BeanInstantiationException e) {
            return null;
        }

        return getPropertyOrStaticPropertyOrFieldValue(instance, propertyName);
    }

    /**
     * Retrieves a PropertyDescriptor for the specified instance and property value
     *
     * @param instance The instance
     * @param propertyValue The value of the property
     * @return The PropertyDescriptor
     */
    public static PropertyDescriptor getPropertyDescriptorForValue(Object instance, Object propertyValue) {
        if(instance == null || propertyValue == null)
            return null;

        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(instance.getClass());

        for (int i = 0; i < descriptors.length; i++) {
            PropertyDescriptor pd = descriptors[i];
            if(isAssignableOrConvertibleFrom(pd.getPropertyType(), propertyValue.getClass())) {
                Object value;
                try {
                    value = PropertyUtils.getReadMethod(pd).invoke(instance, (Object[]) null);                    
                } catch (Exception e) {
                    throw new RuntimeException("Problem calling readMethod of " + pd, e);
                }
                if(propertyValue.equals(value))
                    return pd;
            }
        }
        return null;
    }
    
    /**
     * Returns the type of the given property contained within the specified class
     *
     * @param clazz The class which contains the property
     * @param propertyName The name of the property
     *
     * @return The property type or null if none exists
     */
    public static Class getPropertyType(Class clazz, String propertyName) {
        if(clazz == null || StringUtils.isBlank(propertyName))
            return null;

        try {
            PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(clazz, propertyName);
            if(desc != null) {
                return desc.getPropertyType();
            }
            else {
                return null;
            }
        } catch (Exception e) {
            // if there are any errors in instantiating just return null for the moment
            return null;
        }
    }

    /**
     * Retrieves all the properties of the given class for the given type
     *
     * @param clazz The class to retrieve the properties from
     * @param propertyType The type of the properties you wish to retrieve
     *
     * @return An array of PropertyDescriptor instances
     */
    public static PropertyDescriptor[] getPropertiesOfType(Class clazz, Class propertyType) {
        if(clazz == null || propertyType == null)
            return new PropertyDescriptor[0];

        Set properties = new HashSet();
        try {
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);

            for (int i = 0; i < descriptors.length; i++) {
                Class currentPropertyType = descriptors[i].getPropertyType();
                if(isTypeInstanceOfPropertyType(propertyType, currentPropertyType)) {
                    properties.add(descriptors[i]);
                }
            }

        } catch (Exception e) {
            // if there are any errors in instantiating just return null for the moment
            return new PropertyDescriptor[0];
        }
        return (PropertyDescriptor[])properties.toArray( new PropertyDescriptor[ properties.size() ] );
    }

    private static boolean isTypeInstanceOfPropertyType(Class type, Class propertyType) {
        return propertyType.isAssignableFrom(type) && !propertyType.equals(Object.class);
    }

    /**
     * Retrieves all the properties of the given class which are assignable to the given type
     *
     * @param clazz             The class to retrieve the properties from
     * @param propertySuperType The type of the properties you wish to retrieve
     * @return An array of PropertyDescriptor instances
     */
    public static PropertyDescriptor[] getPropertiesAssignableToType(Class clazz, Class propertySuperType) {
        if (clazz == null || propertySuperType == null) return new PropertyDescriptor[0];

        Set properties = new HashSet();
        try {
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);

            for (int i = 0; i < descriptors.length; i++) {
                if (propertySuperType.isAssignableFrom(descriptors[i].getPropertyType())) {
                    properties.add(descriptors[i]);
                }
            }
        } catch (Exception e) {
            return new PropertyDescriptor[0];
        }
        return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[properties.size()]);
    }

    /**
     * Retrieves a property of the given class of the specified name and type
     * @param clazz The class to retrieve the property from
     * @param propertyName The name of the property
     * @param propertyType The type of the property
     *
     * @return A PropertyDescriptor instance or null if none exists
     */
    public static PropertyDescriptor getProperty(Class clazz, String propertyName, Class propertyType) {
        if(clazz == null || propertyName == null || propertyType == null)
            return null;

        try {
            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(clazz, propertyName);
            if(pd.getPropertyType().equals( propertyType )) {
                return pd;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            // if there are any errors in instantiating just return null for the moment
            return null;
        }
    }

    /**
     * Convenience method for converting a collection to an Object[]
     * @param c The collection
     * @return  An object array
     */
    public static Object[] collectionToObjectArray(Collection c) {
        if(c == null) return new Object[0];

        return c.toArray(new Object[c.size()]);
    }


    /**
     * Detect if left and right types are matching types. In particular,
     * test if one is a primitive type and the other is the corresponding
     * Java wrapper type. Primitive and wrapper classes may be passed to
     * either arguments.
     *
     * @param leftType
     * @param rightType
     * @return true if one of the classes is a native type and the other the object representation
     * of the same native type
     */
    public static boolean isMatchBetweenPrimitiveAndWrapperTypes(Class leftType, Class rightType) {
        if (leftType == null) {
            throw new NullPointerException("Left type is null!");
        } else if (rightType == null) {
            throw new NullPointerException("Right type is null!");
        } else {
            Class r = (Class) PRIMITIVE_TYPE_COMPATIBLE_CLASSES.get(leftType);
            return r == rightType;
        }
    }

    /**
     * <p>Tests whether or not the left hand type is compatible with the right hand type in Groovy
     * terms, i.e. can the left type be assigned a value of the right hand type in Groovy.</p>
     * <p>This handles Java primitive type equivalence and uses isAssignableFrom for all other types,
     * with a bit of magic for native types and polymorphism i.e. Number assigned an int.
     * If either parameter is null an exception is thrown</p>
     *
     * @param leftType The type of the left hand part of a notional assignment
     * @param rightType The type of the right hand part of a notional assignment
     * @return True if values of the right hand type can be assigned in Groovy to variables of the left hand type.
     */
    public static boolean isGroovyAssignableFrom(
            Class leftType, Class rightType)
    {
        if (leftType == null) {
            throw new NullPointerException("Left type is null!");
        } else if (rightType == null) {
            throw new NullPointerException("Right type is null!");
        } else if (leftType == Object.class) {
            return true;
        } else if (leftType == rightType) {
            return true;
        } else {
            // check for primitive type equivalence
            Class r = (Class) PRIMITIVE_TYPE_COMPATIBLE_CLASSES.get(leftType);
            boolean result = r == rightType;

            if (!result)
            {
                // If no primitive <-> wrapper match, it may still be assignable
                // from polymorphic primitives i.e. Number -> int (AKA Integer)
                if (rightType.isPrimitive())
                {
                    // see if incompatible
                    r = (Class) PRIMITIVE_TYPE_COMPATIBLE_CLASSES.get(rightType);
                    if (r != null)
                    {
                        result = leftType.isAssignableFrom(r);
                    }
                } else
                {
                    // Otherwise it may just be assignable using normal Java polymorphism
                    result = leftType.isAssignableFrom(rightType);
                }
            }
            return result;
        }
    }

    private static Method findDeclaredMethod(Class clazz, String methodName, Class[] parameterTypes) {
        while(clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                if(method != null) return method;
            } catch(NoSuchMethodException e) {
                // skip
            } catch(SecurityException e) {
                // skip
            }
            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * <p>Work out if the specified property is readable and static. Java introspection does not
     * recognize this concept of static properties but Groovy does. We also consider public static fields
     * as static properties with no getters/setters</p>
     *
     * @param clazz The class to check for static property
     * @param propertyName The property name
     * @return true if the property with name propertyName has a static getter method
     */
    public static boolean isStaticProperty( Class clazz, String propertyName)
    {
        Method getter = findDeclaredMethod(clazz, getGetterName(propertyName), null);
        if (getter != null)
        {
            return isPublicStatic(getter);
        }
        else
        {
            try
            {
                Field f = clazz.getDeclaredField(propertyName);
                if (f != null)
                {
                    return isPublicStatic(f);
                }
            }
            catch (NoSuchFieldException e)
            {
            }
        }

        return false;
    }

    /**
     * Determine whether the method is declared public static
     * @param m
     * @return True if the method is declared public static
     */
    public static boolean isPublicStatic( Method m)
    {
        final int modifiers = m.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
    }

    /**
     * Determine whether the field is declared public static
     * @param f
     * @return True if the field is declared public static
     */
    public static boolean isPublicStatic( Field f)
    {
        final int modifiers = f.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
    }

    /**
     * Calculate the name for a getter method to retrieve the specified property
     * @param propertyName
     * @return The name for the getter method for this property, if it were to exist, i.e. getConstraints
     */
    public static String getGetterName(String propertyName)
    {
        return "get" + Character.toUpperCase(propertyName.charAt(0))
            + propertyName.substring(1);
    }

    /**
     * <p>Get a static property value, which has a public static getter or is just a public static field.</p>
     *
     * @param clazz The class to check for static property
     * @param name The property name
     * @return The value if there is one, or null if unset OR there is no such property
     */
    public static Object getStaticPropertyValue(Class clazz, String name)
    {
        Method getter = findDeclaredMethod(clazz, getGetterName(name), null);
        try
        {
            if (getter != null)
            {
                return getter.invoke(null, (Object[]) null);
            }
            else
            {
                Field f = clazz.getDeclaredField(name);
                if (f != null)
                {
                    return f.get(null);
                }
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    /**
     * <p>Looks for a property of the reference instance with a given name.</p>
     * <p>If found its value is returned. We follow the Java bean conventions with augmentation for groovy support
     * and static fields/properties. We will therefore match, in this order:
     * </p>
     * <ol>
     * <li>Standard public bean property (with getter or just public field, using normal introspection)
     * <li>Public static property with getter method
     * <li>Public static field
     * </ol>
     *
     * @return property value or null if no property found
     */
    public static Object getPropertyOrStaticPropertyOrFieldValue(Object obj, String name)
    {
        if (PropertyUtils.isReadable(obj, name)) {
            try {
                return PropertyUtils.getProperty(obj, name);
            } catch(Exception e) {
                throw new BeanException("Error while reading value of property/field "+name,e);
            }
        }
        else
        {
            // Look for public fields
            if (isPublicField(obj, name))
            {
                return getFieldValue(obj, name);
            }

            // Look for statics
            Class clazz = obj.getClass();
            if (isStaticProperty(clazz, name))
            {
                return getStaticPropertyValue(clazz, name);
            }
            else
            {
               return null;
            }
        }
    }

    /**
     * Get the value of a declared field on an object
     *
     * @param obj
     * @param name
     * @return The object value or null if there is no such field or access problems
     */
    public static Object getFieldValue(Object obj, String name)
    {
        Class clazz = obj.getClass();
        Field f = null;
        try
        {
            f = clazz.getDeclaredField(name);
            return f.get(obj);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Work out if the specified object has a public field with the name supplied.
     *
     * @param obj
     * @param name
     * @return True if a public field with the name exists
     */
    public static boolean isPublicField(Object obj, String name)
    {
        Class clazz = obj.getClass();
        Field f = null;
        try
        {
            f = clazz.getDeclaredField(name);
            return Modifier.isPublic(f.getModifiers());
        }
        catch (NoSuchFieldException e)
        {
            return false;
        }
    }

    /**
     * Checks whether the specified property is inherited from a super class
     * 
     * @param clz The class to check
     * @param propertyName The property name
     * @return True if the property is inherited
     */
    public static boolean isPropertyInherited(Class clz, String propertyName) {
        if(clz == null) return false;
        if(StringUtils.isBlank(propertyName))
            throw new IllegalArgumentException("Argument [propertyName] cannot be null or blank");
        
        Class superClass = clz.getSuperclass();
        
        PropertyDescriptor pd = null;
        try {
            pd = PropertyUtils.getPropertyDescriptor(superClass, propertyName);
        } catch(Exception e) {
            throw new BeanException("Could not read property descritptor for "+propertyName+" in "+superClass, e);
        }
        if (pd != null && pd.getReadMethod() != null) {
            return true;
        }
        return false;
    }

    /**
     * Creates a concrete collection for the suppied interface
     * @param interfaceType The interface
     * @return ArrayList for List, TreeSet for SortedSet, HashSet for Set etc.
     */
    public static Collection createConcreteCollection(Class interfaceType) {
        Collection elements;
        if(interfaceType.equals(List.class)) {
            elements = new ArrayList();
        }
        else if(interfaceType.equals(SortedSet.class)) {
            elements = new TreeSet();
        }
        else {
            elements = new HashSet();
        }
        return elements;
    }

    /**
     * Retrieves the name of a setter for the specified property name
     * @param propertyName The property name
     * @return The setter equivalent
     */
    public static String getSetterName(String propertyName) {
        return PROPERTY_SET_PREFIX+propertyName.substring(0,1).toUpperCase()+ propertyName.substring(1);
    }

    /**
     * Returns true if the name of the method specified and the number of arguments make it a javabean property
     * 
     * @param name True if its a Javabean property
     * @param args The arguments
     * @return True if it is a javabean property method
     */
    public static boolean isGetter(String name, Class[] args) {
        if(StringUtils.isBlank(name) || args == null)return false;
        if(args.length != 0)return false;

        if(name.startsWith("get")) {
            name = name.substring(3);
            if(name.length() > 0 && Character.isUpperCase(name.charAt(0))) return true;            
        }
        else if(name.startsWith("is")) {
            name = name.substring(2);
            if(name.length() > 0 && Character.isUpperCase(name.charAt(0))) return true;
        }
        return false;
    }

    /**
     * Returns a property name equivalent for the given getter name or null if it is not a getter
     * 
     * @param getterName The getter name
     * @return The property name equivalent
     */
    public static String getPropertyForGetter(String getterName) {
        if(StringUtils.isBlank(getterName))return null;
        
        if(getterName.startsWith("get")) {
            String prop = getterName.substring(3);
            return convertPropertyName(prop);
        }
        else if(getterName.startsWith("is")) {
            String prop = getterName.substring(2);
            return convertPropertyName(prop);
        }
        return null;
    }

    private static String convertPropertyName(String prop) {
        if(Character.isUpperCase(prop.charAt(0)) && Character.isUpperCase(prop.charAt(1))) {
            return prop;
        }
        else if(Character.isDigit(prop.charAt(0))) {
            return prop;
        }
        else {
            return Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
        }
    }
    
    /**
     * Returns a property name equivalent for the given setter name or null if it is not a getter
     * 
     * @param setterName The setter name
     * @return The property name equivalent
     */
    public static String getPropertyForSetter(String setterName) {
        if(StringUtils.isBlank(setterName))return null;
        
        if(setterName.startsWith(PROPERTY_SET_PREFIX)) {
            String prop = setterName.substring(3);
            return convertPropertyName(prop);
        }
        return null;
    }

    public static boolean isSetter(String name, Class[] args) {
        if(StringUtils.isBlank(name) || args == null)return false;
        
        if(name.startsWith(PROPERTY_SET_PREFIX)) {
            if(args.length != 1) return false;
            name = name.substring(3);
            if(name.length() > 0 && Character.isUpperCase(name.charAt(0))) return true;
        }
        
        return false;
    }

    public static MetaClass getExpandoMetaClass(Class clazz) {
        MetaClassRegistry registry = GroovySystem.getMetaClassRegistry();
        Validate.isTrue(registry.getMetaClassCreationHandler() instanceof ExpandoMetaClassCreationHandle, "Griffon requires an instance of [ExpandoMetaClassCreationHandle] to be set in Groovy's MetaClassRegistry!");
        MetaClass mc = registry.getMetaClass(clazz);
        AdaptingMetaClass adapter = null;
        if(mc instanceof AdaptingMetaClass) {
            adapter = (AdaptingMetaClass) mc;
            mc = ((AdaptingMetaClass)mc).getAdaptee();
        }

        if(!(mc instanceof ExpandoMetaClass)) {
            // removes cached version
            registry.removeMetaClass(clazz);
            mc = registry.getMetaClass(clazz);
            if(adapter != null) {
                adapter.setAdaptee(mc);
            }
        }
        Validate.isTrue(mc instanceof ExpandoMetaClass,"BUG! Method must return an instance of [ExpandoMetaClass]!");
        return mc;
    }    
    
    /**
     * Returns true if the specified clazz parameter is either the same as, or is a superclass or superinterface 
     * of, the specified type parameter. Converts primitive types to compatible class automatically.
     * 
     * @param clazz
     * @param type
     * @return True if the class is a taglib
     * @see java.lang.Class#isAssignableFrom(Class)
     */
    public static boolean isAssignableOrConvertibleFrom(Class clazz, Class type) {
        if (type == null || clazz == null) {
            return false;
        }
        else if (type.isPrimitive()) {
            // convert primitive type to compatible class 
            Class primitiveClass = (Class) PRIMITIVE_TYPE_COMPATIBLE_CLASSES.get(type);
            if (primitiveClass == null) {
                // no compatible class found for primitive type
                return false;
            }
            else {
                return clazz.isAssignableFrom(primitiveClass);
            }
        }
        else {
            return clazz.isAssignableFrom(type);
        }
    }

    /**
     * Retrieves a boolean value from a Map for the given key
     *
     * @param key The key that references the boolean value
     * @param map The map to look in
     * @return A boolean value which will be false if the map is null, the map doesn't contain the key or the value is false 
     */
    public static boolean getBooleanFromMap(String key, Map map) {
        if(map == null) return false;
        if(map.containsKey(key)) {
            Object o = map.get(key);
            if(o == null)return false;
            else if(o instanceof Boolean) {
                return ((Boolean)o).booleanValue();
            }
            else {
                 return Boolean.valueOf(o.toString()).booleanValue();
            }
        }
        return false;
    }

    /**
     * Locates the name of a property for the given value on the target object using Groovy's meta APIs.
     * Note that this method uses the reference so the incorrect result could be returned for two properties
     * that refer to the same reference. Use with caution.
     * 
     * @param target The target
     * @param obj The property value
     * @return The property name or null
     */
    public static String findPropertyNameForValue(Object target, Object obj) {
        MetaClass mc = GroovySystem.getMetaClassRegistry().getMetaClass(target.getClass());
        List<MetaProperty> metaProperties = mc.getProperties();
        for (MetaProperty metaProperty : metaProperties) {
            if(isAssignableOrConvertibleFrom(metaProperty.getType(), obj.getClass())) {
                Object val = metaProperty.getProperty(target);
                if (val != null && val.equals(obj))
                    return metaProperty.getName();
            }
        }
        return null;
    }

    /**
     * Returns whether the specified class is either within one of the specified packages or
     * within a subpackage of one of the packages
     *
     * @param theClass The class
     * @param packageList The list of packages
     * @return True if it is within the list of specified packages
     */
    public static boolean isClassBelowPackage(Class theClass, List packageList) {
        String classPackage = theClass.getPackage().getName();
        for (Object packageName : packageList) {
            if(packageName!=null) {
                if (classPackage.startsWith(packageName.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
