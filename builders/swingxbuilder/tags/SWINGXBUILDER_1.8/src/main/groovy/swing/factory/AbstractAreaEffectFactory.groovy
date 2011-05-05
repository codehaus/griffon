/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 11, 2007
 * Time: 8:49:22 PM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory

import org.jdesktop.swingx.painter.AbstractAreaPainter

class AbstractAreaEffectFactory extends BeanFactory {

    public AbstractAreaEffectFactory(Class klass) {
        super(klass)
    }

    public AbstractAreaEffectFactory(Class klass, boolean leaf) {
        super(klass, leaf)
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (parent instanceof AbstractAreaPainter) {
            def effects
            if (builder.componentCollections["effects"] == null) {
                builder.componentCollections.put("effects", [node])
            }
            else {
                builder.componentCollections["effects"] += node
            }
        }
    }

}
