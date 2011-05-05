/*
 * $Id:  $
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
 *
 */

package groovy.swing.factory

import groovy.swing.GroovyPlot
import groovy.swing.SwingBuilder
import org.codehaus.groovy.runtime.InvokerHelper
import org.jdesktop.swingx.JXGraph


public class JXGraphFactory extends ComponentFactory {

    public JXGraphFactory() {
        super(JXGraph)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
        if (SwingBuilder.checkValueIsType(value, name, JXGraph.class)) {
            return value
        }
        def graph = new JXGraph()

        // Follow the form
        // [[color, plotClosure]...]
        def plots = properties.remove("plots")
        if (plots != null) {
            for (plot in plots) {
                graph.addPlots(plot[0], new GroovyPlot(plot[1]))
            }
        }
        // set the properties
        for (entry in properties.entrySet()) {
            String property = entry.getKey().toString()
            Object val = entry.getValue()
            InvokerHelper.setProperty(graph, property, val)
        }
        return graph
    }

}
