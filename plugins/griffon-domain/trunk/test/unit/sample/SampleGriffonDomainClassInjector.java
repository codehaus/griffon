package sample;

import java.util.Arrays;
import java.util.Collection;

import griffon.domain.artifacts.GriffonDomainClassProperty;
import griffon.domain.metaclass.DefaultPersistentDynamicMethod;
import griffon.domain.metaclass.MethodSignature;
import org.codehaus.griffon.persistence.DefaultGriffonDomainClassInjector;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class SampleGriffonDomainClassInjector extends DefaultGriffonDomainClassInjector {
    public MethodSignature[] getProvidedMethods() {
        Collection<MethodSignature> signatures = DefaultGroovyMethods.plus(
            Arrays.asList(DefaultPersistentDynamicMethod.FETCH.getMethodSignatures()),
            Arrays.asList(DefaultPersistentDynamicMethod.FIND_ALL.getMethodSignatures())
        ); 
        return (MethodSignature[]) signatures.toArray(new MethodSignature[signatures.size()]);
    }

    protected String getMappingValue() {
        return "sample";
    }

    protected Class getDomainHandlerClass() {
        return SampleDomainHandler.class;
    }

    protected Class getDomainHandlerHolderClass() {
        return SampleDomainHandlerHolder.class;
    }

    protected void performInjection(ClassNode classNode) {
        injectIdProperty(classNode);
        injectToStringMethod(classNode, GriffonDomainClassProperty.IDENTITY);
    }
}
