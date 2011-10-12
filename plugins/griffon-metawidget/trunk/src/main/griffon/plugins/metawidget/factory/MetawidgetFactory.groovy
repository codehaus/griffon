/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.metawidget.factory

import groovy.swing.factory.ComponentFactory
import griffon.util.Metadata
import org.metawidget.inspector.annotation.*
import org.metawidget.inspector.composite.*
import org.metawidget.inspector.impl.*
import org.metawidget.inspector.impl.propertystyle.*
import org.metawidget.inspector.impl.propertystyle.groovy.*
import org.metawidget.inspector.java5.*
import org.metawidget.inspector.propertytype.*
import org.metawidget.swing.*
import org.metawidget.inspector.impl.propertystyle.javabean.GriffonArtifactPropertyStyle as JGriffonArtifactPropertyStyle

/**
 * @author Andres Almiray
 */
class MetawidgetFactory extends ComponentFactory {
    static final String NAME = 'metawidget'
    
    static create(FactoryBuilderSupport builder, Object value = null, Map attributes = [:]) {
        newInstance(builder, NAME, value, attributes)
    }
    
    MetawidgetFactory() {
        super(SwingMetawidget)
    }
    
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        SwingMetawidget metawidget = null
        if(value instanceof SwingMetawidget) {
            metawidget = value
        } else {
            metawidget = new SwingMetawidget()
            if(value != null) metawidget.toInspect = value
        }
        
        String config = null
        boolean hasConfig = false
        boolean hasConfigInAttributes = attributes.config != null
        boolean configured = hasConfigInAttributes
        
        if(!hasConfigInAttributes) {
            if(value != null && value != metawidget) {
                config = '/' + value.getClass().name.replace('.', '/') + '-metawidget.xml'
            }
            hasConfig = config != null && MetawidgetFactory.getResource(config) != null
        
            if(!hasConfig) { 
                config = builder.variables[FactoryBuilderSupport.SCRIPT_CLASS_NAME]
                config = '/' + config.replace('.', '/') + '-metawidget.xml'
                hasConfig = MetawidgetFactory.getResource(config) != null
            }
        
            if(!hasConfig) {
                config = '/' + Metadata.current['app.name'] + '-metawidget.xml'
                hasConfig = MetawidgetFactory.getResource(config) != null
            }
        }
        
        boolean hasInspectorInAttributes = attributes.inspector != null
        
        if(!hasConfigInAttributes && hasConfig) {
            metawidget.config = config
            configured = true
        }
        if(!configured && !hasInspectorInAttributes) metawidget.inspector = MetawidgetFactory.groovyInspector()
            
        metawidget
    }
    
    static groovyInspector() {
        def groovyConfig = new BaseObjectInspectorConfig().setPropertyStyle(new GriffonArtifactPropertyStyle())
        new CompositeInspector(new CompositeInspectorConfig().setInspectors(
            new PropertyTypeInspector(groovyConfig),
            new MetawidgetAnnotationInspector(groovyConfig),
            new Java5Inspector(groovyConfig)
        ))
    }
    
    static javaInspector() {
        def javaConfig = new BaseObjectInspectorConfig().setPropertyStyle(new JGriffonArtifactPropertyStyle())
        new CompositeInspector(new CompositeInspectorConfig().setInspectors(
            new PropertyTypeInspector(javaConfig),
            new MetawidgetAnnotationInspector(javaConfig),
            new Java5Inspector(javaConfig)
        ))
    }
}
