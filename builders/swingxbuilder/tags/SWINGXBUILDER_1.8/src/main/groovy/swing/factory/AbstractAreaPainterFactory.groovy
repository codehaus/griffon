/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 11, 2007
 * Time: 8:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory

import org.jdesktop.swingx.painter.effects.AreaEffect

class AbstractAreaPainterFactory extends BeanFactory {

    public AbstractAreaPainterFactory(Class klass) {
        super(klass)
    }

    public AbstractAreaPainterFactory(Class klass, boolean leaf) {
        super(klass, leaf)
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node)
        def effects = builder.componentCollections["effects"]
        if (effects != null && effects.size() > 0) {
            node.setAreaEffects(effects.toArray(new AreaEffect[1]))
            builder.componentCollections.remove("effects")
        }

    }

}
