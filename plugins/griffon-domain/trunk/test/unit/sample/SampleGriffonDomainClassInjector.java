package sample;

import griffon.domain.metaclass.DynamicMethod;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.griffon.persistence.DefaultGriffonDomainClassInjector;
import griffon.domain.artifacts.GriffonDomainArtifactClassProperty;

public class SampleGriffonDomainClassInjector extends DefaultGriffonDomainClassInjector {
    public DynamicMethod[] getProvidedDynamicMethods() {
        return new DynamicMethod[] {
            DynamicMethod.FETCH,
            DynamicMethod.FIND_ALL
        };
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

    protected void injectMethods(ClassNode classNode) {
        injectIdProperty(classNode);
        injectToStringMethod(classNode, GriffonDomainArtifactClassProperty.IDENTITY);
    }
}
