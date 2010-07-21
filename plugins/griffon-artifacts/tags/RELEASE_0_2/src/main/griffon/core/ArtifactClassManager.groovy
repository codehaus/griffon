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

package griffon.core

import org.apache.commons.beanutils.ConstructorUtils

/**
 * Helper class capable of dealing with artifacts and their classes.
 *
 * @author Andres Almiray
 */
@Singleton
class ArtifactClassManager {
    GriffonApplication app
    private final Map artifactClassTypes = [:]
    private final Map artifactClasses = [:]

    private static final GriffonClass[] EMPTY_ARTIFACT_CLASS_ARRAY = new GriffonClass[0]

    /**
     * Reads the artifacts definitions file from the classpath.<p>
     */
    synchronized void loadArtifactClassMetadata() {
        def config = new ConfigSlurper().parse(app.class.getResource("/artifactClasses.properties"))
        config.each { artifactType, artifactClassType -> 
            artifactClassTypes[artifactType] = app.class.classLoader.loadClass(artifactClassType)
        }
        ArtifactManager.instance.allArtifacts.each { artifactInfo ->
            artifactInfo.metaClass.getArtifactClass = this.&getArtifactClass.curry(artifactInfo)
        }
    }

    /**
     * Retrieves an artifact class metadata by class name
     */
    GriffonClass getArtifactClass(Class clazz) {
        getArtifactClass(clazz.name)
    }

    /**
     * Retrieves an artifact class metadata by class name
     */
    GriffonClass getArtifactClass(String className) {
        getArtifactClass(ArtifactManager.instance.getArtifactInfo(className))
    }

    /**
     * Retrieves an artifact class metadata by class name
     */
    GriffonClass getArtifactClass(ArtifactInfo artifactInfo) {
        if(artifactInfo) {
            synchronized(this) {
                Class artifactClassClazz = artifactClassTypes[artifactInfo.type]
                Map classesPerType = artifactClasses.get(artifactInfo.type, [:])
                GriffonClass artifactClass = classesPerType[artifactInfo.klass.name]
                if(!artifactClass && artifactClassClazz) {
                    artifactClass = ConstructorUtils.invokeConstructor(artifactClassClazz, artifactInfo)
                    classesPerType[artifactInfo.klass.name] = artifactClass 
                }
                def getter = { a -> return a }
                artifactInfo.metaClass.getArtifactClass = getter.curry(artifactClass)
                artifactInfo.metaClass.artifactClass = artifactClass
                return artifactClass
            }
        }
        return null
    }

    /**
     * Retrieves an artifact class metadata by type and class name
     */
    GriffonClass getArtifactClass(String type, String className) {
        if(type && className) {
            synchronized(this) {
                Class artifactClassClazz = artifactClassTypes[type]
                Map classesPerType = artifactClasses.get(type, [:])
                GriffonClass artifactClass = classesPerType[className]
                if(!artifactClass && artifactClassClazz) {
                    ArtifactInfo artifactInfo = ArtifactManager.instance.getArtifactInfo(className)
                    artifactClass = ConstructorUtils.invokeConstructor(artifactClassClazz, artifactInfo)
                    classesPerType[artifactInfo.klass.name] = artifactClass 
                    def getter = { a -> return a }
                    artifactInfo.metaClass.getArtifactClass = getter.curry(artifactClass)
                    artifactInfo.metaClass.artifactClass = artifactClass
                }
                return artifactClass
            }
        }
        return null
    }

    /**
     * Returns all available artifacts of a particular type.<p>
     * Never returns null
     */
    GriffonClass[] getArtifactClassesOfType(String type) {
        List classes = []
        synchronized(this) {
            Map classesPerType = artifactClasses.get(type, [:])
            classes.addAll(classesPerType.values())
        }
        ArtifactInfo[] artifactInfos = ArtifactManager.instance.getArtifactsOfType(type)
        if(!classes || classes.size() != artifactInfos.size()) {
            classes = [] 
            for(ArtifactInfo artifactInfo: artifactInfos) {
                classes.add(getArtifactClass(artifactInfo))
            }
        }
        return classes.toArray(new GriffonClass[classes.size()])
    }

    def methodMissing(String methodName, args) {
        def artifactType = methodName =~ /^get(\w+)ArtifactClasses$/
        if(artifactType) {
            artifactType = normalize(artifactType)
        
            if(!args) {
                ArtifactClassManager.metaClass."$methodName" = this.&getArtifactClassesOfType.curry(artifactType)
                return getArtifactClassesOfType(artifactType)
            }
            return EMPTY_ARTIFACT_CLASS_ARRAY
        }

        throw new MissingMethodException(methodName, ArtifactClassManager, args)
    }

    def propertyMissing(String propertyName) {
        def artifactType = propertyName =~ /^(\w+)ArtifactClasses$/
        if(artifactType) {
            artifactType = artifactType[0][1]
            ArtifactClassManager.metaClass."$propertyName" = this.&getArtifactClassesOfType.curry(artifactType)
            return getArtifactClassesOfType(artifactType)
        }

        throw new MissingPropertyException(propertyName, ArtifactClassManager)
    }

    private String normalize(input) {
        input = input[0][1]
        input[0].toLowerCase() + input[1..-1]
    }
}
