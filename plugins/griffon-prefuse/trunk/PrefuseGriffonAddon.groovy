/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.core.GriffonApplication
import prefuse.render.*
import prefuse.data.Tuple
import groovy.swing.factory.BeanFactory
import griffon.plugins.prefuse.factory.*

/**
 * @author Andres Almiray
 */
class PrefuseGriffonAddon {
    void addonInit(GriffonApplicationapp) {
        Tuple.metaClass.getAt = {String propertyName ->
            delegate.get(propertyName)
        }
        Tuple.metaClass.putAt = {String propertyName, Object value ->
            delegate.set(propertyName, value) 
        }
    }

    def factories = [
        display: new DisplayFactory(),
        visualization: new VisualizationFactory(),
        graph: new GraphFactory(),
        table: new TableFactory(),
        tree: new TreeFactory(),
        axisRenderer: new BeanFactory(AxisRenderer),
        edgeRenderer: new BeanFactory(EdgeRenderer),
        labelRenderer: new BeanFactory(LabelRenderer),
        nullRenderer: new BeanFactory(NullRenderer),
        polygonRenderer: new BeanFactory(PolygonRenderer),
        shapeRenderer: new BeanFactory(ShapeRenderer)
    ]
}
