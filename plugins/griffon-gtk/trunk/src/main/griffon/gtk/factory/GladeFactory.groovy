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
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Andres Almiray
 */
class GladeFactory extends AbstractGtkFactory {
    private static final Logger LOG = Logger.getLogger(GladeFactory.class.name)

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def source = null
        def text = null
        if(value instanceof GString) value = value.toString()
        if(value && value instanceof String) source = value
        if(!source && attributes.containsKey('source')) source = attributes.remove('source')
        if(!source && attributes.containsKey('text')) text = attributes.remove('text')

        if(source) {
            parseGlade(builder, GtkUtils.createTempResource(source, '.glade'), attributes.remove('root'), attributes.id)
        } else if(text) {
            def tmp = File.createTempFile('griffon', '.glade')
            tmp.deleteOnExit()
            tmp.text = text
            parseGlade(builder, tmp, attributes.remove('root'), attributes.id)
        } else {
            throw new IllegalArgumentException("In $name you must define a value for source: (a resource) or text: (an XML string).")
        }
        return new Object()
    }

    private void parseGlade(FactoryBuilderSupport builder, File source, String root, String rootId) {
        rootId = rootId ? rootId + '_' : ''
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
    }
}
