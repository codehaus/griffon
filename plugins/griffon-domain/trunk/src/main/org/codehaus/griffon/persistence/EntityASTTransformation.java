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
package org.codehaus.griffon.persistence;

import griffon.persistence.Entity;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.IOException;
import java.net.URL;

import groovy.lang.Closure;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.codehaus.groovy.runtime.MethodClosure;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.eachLine;

/**
 * Injects the necessary fields and behaviors into a domain class in order to make it a property domain entity.
 *
 * @author Graeme Rocher (Grails 1.1)
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class EntityASTTransformation implements ASTTransformation {
    private static final ClassNode MY_TYPE = new ClassNode(Entity.class);
    private static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
    private static final String DOMAIN_CLASS_INJECTOR_KEY = "org.codehaus.griffon.runtime.domain-class-injector.";
    private static final Pattern INJECTOR_PATTERN = Pattern.compile(DOMAIN_CLASS_INJECTOR_KEY +"(\\w+)\\s+=\\s+([\\w+\\.]+)");
    private static final Map<String, GriffonDomainClassInjector> DOMAIN_INJECTORS = new LinkedHashMap<String, GriffonDomainClassInjector>();

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        if (!(astNodes[0] instanceof AnnotationNode) || !(astNodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
        }

        AnnotatedNode parent = (AnnotatedNode) astNodes[1];
        AnnotationNode node = (AnnotationNode) astNodes[0];
        if(!MY_TYPE.equals(node.getClassNode()) || !(parent instanceof ClassNode)) {
            return;
        }

        ClassNode cNode = (ClassNode) parent;
        String cName = cNode.getName();
        if(cNode.isInterface()) {
            throw new RuntimeException("Error processing interface '" + cName + "'. " +
                    MY_TYPE_NAME + " not allowed for interfaces.");
        }

        String implementation = null;
        final Expression member = node.getMember("value");
        if(member instanceof ConstantExpression) {
            implementation = (String) ((ConstantExpression) member).getValue();
        }
        if(implementation == null || implementation.trim().length() == 0) {
             throw new RuntimeException("Error processing " + MY_TYPE_NAME + " in " + cName + 
                    ". Implementation value '" + implementation + "' is invalid.");
        }

        GriffonDomainClassInjector domainInjector = findDomainClassInjector(implementation);
        if(domainInjector == null) {
             throw new RuntimeException("Error processing " + MY_TYPE_NAME + " in " + cName +
                    ". Implementation '"+ implementation +"' not found.");
        }

        domainInjector.performInjectionOnAnnotatedEntity(cNode);
    }

    private GriffonDomainClassInjector findDomainClassInjector(String implementation) {
        cacheDomainClassInjectors();
        return DOMAIN_INJECTORS.get(implementation);
    }

    private void cacheDomainClassInjectors() {
        if(!DOMAIN_INJECTORS.isEmpty()) return;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = null;
        
        try {
            urls = cl.getResources("META-INF/griffon-domain.properties");
        } catch(IOException ioe) {
            throw new RuntimeException("No implementations found for " + MY_TYPE_NAME + "." + ioe);
        }

        Closure extractDomainClassInjector = new MethodClosure(this, "extractDomainClassInjector");
        while(urls.hasMoreElements()) {
           try {
               URL url = urls.nextElement();
               eachLine(url, extractDomainClassInjector);
           } catch(IOException ioe) {
               System.err.println(ioe); 
           }
        } 

        if(DOMAIN_INJECTORS.isEmpty()) {
            throw new RuntimeException("No GriffonDomainClassInjectors found in classpath!");
        }
    }

    private void extractDomainClassInjector(String line) {
        Matcher matcher = INJECTOR_PATTERN.matcher(line);
        if(!matcher.matches()) return;

        String key = matcher.group(1).trim();
        String className = matcher.group(2).trim();
        try {
            Class clazz = Class.forName(className);
            DOMAIN_INJECTORS.put(key, (GriffonDomainClassInjector) clazz.newInstance());
        } catch(Exception e) {
            // can't instantiate injector, bail out immediately
            throw new RuntimeException("Can't instantiate GriffonDomainClassInjector for " + key +
                    " with class '" + className + "'. "+ e);
        }
    }
}