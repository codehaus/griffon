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

package griffon.plugins.prefuse.factory

import groovy.swing.factory.ComponentFactory
import prefuse.Display
import prefuse.Visualization
import prefuse.controls.DragControl
import prefuse.controls.PanControl
import prefuse.controls.ZoomControl
import prefuse.data.expression.Predicate
import prefuse.visual.sort.ItemSorter

/**
 * @author Andres Almiray
 */
class DisplayFactory extends ComponentFactory {
    DisplayFactory() {
        super(Display)
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (attributes.drag) {
            def drag = attributes.remove('drag')
            node.addControlListener(drag instanceof DragControl ? drag : new DragControl())
        }
        if (attributes.pan) {
            def pan = attributes.remove('pan')
            node.addControlListener(pan instanceof PanControl ? pan : new PanControl())
        }
        if (attributes.zoom) {
            def zoom = attributes.remove('zoom')
            node.addControlListener(zoom instanceof ZoomControl ? zoom : new ZoomControl())
        }
        return super.onHandleNodeAttributes(builder, node, attributes)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Visualization) {
            parent.visualization = child
        } else if (child instanceof Predicate) {
            parent.predicate = child
        } else if (child instanceof ItemSorter) {
            parent.itemSorter = child
        } else {
            super.setChild(builder, parent, child)
        }
    }
}
