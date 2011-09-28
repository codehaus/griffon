/* 
 * Copyright 2004-2010 Graeme Rocher
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
package org.codehaus.griffon.compiler.support;

import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainHandler;
import griffon.persistence.Event;
import griffon.transform.Domain;
import griffon.util.GriffonExceptionHandler;
import groovy.util.ConfigObject;
import org.codehaus.griffon.compiler.GriffonCompilerContext;
import org.codehaus.griffon.compiler.SourceUnitCollector;
import org.codehaus.griffon.runtime.domain.AbstractGriffonDomain;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static org.codehaus.griffon.ast.GriffonASTUtils.addMethod;

/**
 * Handles generation of code for Griffon domain classes.
 * <p/>
 *
 * @author Andres Almiray
 * @since 0.9.1
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class GriffonDomainASTTransformation extends GriffonArtifactASTTransformation {
    private static final Logger LOG = LoggerFactory.getLogger(GriffonDomainASTTransformation.class);
    private static final String ARTIFACT_PATH = "domain";
    private static final ClassNode DOMAIN_CLASS = new ClassNode(Domain.class);
    private static final ClassNode GRIFFON_DOMAIN_CLASS = ClassHelper.makeWithoutCaching(GriffonDomain.class);
    private static final ClassNode ABSTRACT_GRIFFON_DOMAIN_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonDomain.class);
    private static final Map<String, GriffonDomainClassInjector> DOMAIN_INJECTORS = new LinkedHashMap<String, GriffonDomainClassInjector>();
    public static final String GRIFFON_DOMAIN_DEFAULT_MAPPING = "griffon.domain.default.mapping";
    public static final String GRIFFON_DOMAIN_DEFAULT_DATASOURCE = "griffon.domain.default.datasource";

    public static boolean isDomainArtifact(ClassNode classNode, SourceUnit source) {
        if (classNode == null || source == null) return false;
        return ARTIFACT_PATH.equals(GriffonCompilerContext.getArtifactPath(source));
    }

    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if (!isDomainArtifact(classNode, source) ||
                classNode.getAnnotations(DOMAIN_CLASS).isEmpty()) return;

        String implementation = "memory";
        Object defaultMapping = GriffonCompilerContext.getFlattenedBuildSettings().get(GRIFFON_DOMAIN_DEFAULT_MAPPING);
        if (defaultMapping != null && !(defaultMapping instanceof ConfigObject)) {
            implementation = String.valueOf(defaultMapping);
        }

        String datasource = "default";
        Object defaultDatasource = GriffonCompilerContext.getFlattenedBuildSettings().get(GRIFFON_DOMAIN_DEFAULT_DATASOURCE);
        if (defaultDatasource != null && !(defaultDatasource instanceof ConfigObject)) {
            datasource = String.valueOf(defaultDatasource);
        }

        inject(classNode, implementation, datasource);
    }

    public static void inject(ClassNode classNode, String implementation, String datasource) {
        injectBaseBehavior(classNode);
        injectBehavior(classNode, implementation, datasource);
    }

    public static void injectBaseBehavior(ClassNode classNode) {
        if (ClassHelper.OBJECT_TYPE.equals(classNode.getSuperClass())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting " + ABSTRACT_GRIFFON_DOMAIN_CLASS.getName() + " as the superclass of " + classNode.getName());
            }
            classNode.setSuperClass(ABSTRACT_GRIFFON_DOMAIN_CLASS);
        } else if (!classNode.implementsInterface(GRIFFON_DOMAIN_CLASS)) {
            ClassNode superClass = classNode.getSuperClass();
            SourceUnit superSource = SourceUnitCollector.getInstance().getSourceUnit(superClass);
            if (isDomainArtifact(superClass, superSource)) return;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Injecting " + GRIFFON_DOMAIN_CLASS.getName() + " behavior to " + classNode.getName());
            }
            // 1. add interface
            classNode.addInterface(GRIFFON_DOMAIN_CLASS);
            // 2. add methods
            ASTInjector injector = new GriffonArtifactASTInjector();
            injector.inject(classNode, GriffonDomainClass.TYPE);

            for (String eventName : Event.getAllEvents()) {
                addMethod(classNode, new MethodNode(
                        eventName,
                        Modifier.PUBLIC,
                        ClassHelper.VOID_TYPE,
                        Parameter.EMPTY_ARRAY,
                        ClassNode.EMPTY_ARRAY,
                        new EmptyStatement()
                ));
            }
        }
    }

    public static void injectBehavior(ClassNode classNode, String implementation, String datasource) {
        GriffonDomainClassInjector injector = findDomainClassInjector(implementation);
        injector.performInjectionOn(classNode, implementation, datasource);
    }

    private static GriffonDomainClassInjector findDomainClassInjector(String implementation) {
        cacheDomainClassInjectors();
        return DOMAIN_INJECTORS.get(implementation);
    }

    private static void cacheDomainClassInjectors() {
        if (!DOMAIN_INJECTORS.isEmpty()) return;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = null;

        try {
            urls = cl.getResources("META-INF/services/" + GriffonDomainHandler.class.getName());
        } catch (IOException ioe) {
            throw new RuntimeException("No implementations found for " + GriffonDomainHandler.class.getName() + "." + ioe);
        }

        while (urls.hasMoreElements()) {
            try {
                URL url = urls.nextElement();
                Properties p = new Properties();
                p.load(url.openStream());
                for (String key : p.stringPropertyNames()) {
                    String className = String.valueOf(p.get(key));
                    try {
                        Class clazz = Class.forName(className);
                        DOMAIN_INJECTORS.put(key, (GriffonDomainClassInjector) clazz.newInstance());
                    } catch (Exception e) {
                        // can't instantiate injector, bail out immediately
                        throw new IllegalArgumentException("Can't instantiate GriffonDomainClassInjector for " + key +
                                " with class '" + className + "'. " + e);
                    }
                }
            } catch (IOException ioe) {
                System.err.println(GriffonExceptionHandler.sanitize(ioe));
            }
        }

        if (DOMAIN_INJECTORS.isEmpty()) {
            throw new IllegalArgumentException("No GriffonDomainClassInjectors found in classpath!");
        }
    }
}