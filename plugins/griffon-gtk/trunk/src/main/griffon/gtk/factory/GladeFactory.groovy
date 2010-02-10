/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.gtk.factory

import griffon.gtk.GtkUtils
import org.gnome.glade.Glade
import org.gnome.gtk.Container
import org.gnome.gtk.Widget
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Andres Almiray
 */
class GladeFactory extends GtkBeanFactory {
    private static final Logger LOG = Logger.getLogger(GladeFactory.class.name)

    GladeFactory() {
        super(Widget)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def source = null
        def text = null
        if(value instanceof GString) value = value.toString()
        if(value && value instanceof String) source = value
        if(!source && attributes.containsKey('source')) source = attributes.remove('source')
        if(!source && attributes.containsKey('text')) text = attributes.remove('text')

        if(source) {
            return parseGlade(builder, GtkUtils.createTempResource(source, '.glade'), attributes)
        } else if(text) {
            def tmp = File.createTempFile('griffon', '.glade')
            tmp.deleteOnExit()
            tmp.text = text
            return parseGlade(builder, tmp, attributes)
        } else {
            throw new IllegalArgumentException("In $name you must define a value for source: (a resource) or text: (an XML string).")
        }
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof Container && child instanceof Widget) parent.add(child)
        else super.setChild(builder, parent, child)
    }

    private parseGlade(FactoryBuilderSupport builder, File source, Map attributes) {
        String root = attributes.remove('root')
        String rootId = attributes.id ? attributes.id + '_' : ''
        def widgets = Glade.parse(source.absolutePath, root)
        def xml = new XmlSlurper().parseText(source.text)
        def ids = xml.'**'.grep{ it.@id != '' }.'@id'*.text()
        ids.each { id ->
            try {
                builder.setVariable(rootId + id, widgets.getWidget(id))
            } catch(Exception e) {
                LOG.log(Level.INFO, "Could not retrieve widget with id $id", e)
            }
        }
        if(attributes.embed) {
            return builder.getVariable(rootId + attributes.remove('embed'))
        } else {
            return new Object()
        } 
    }
}
