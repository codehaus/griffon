/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 12, 2007
 * Time: 9:48:11 AM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory

import org.jdesktop.swingx.painter.Painter

class CompoundPainterFactory extends BeanFactory {

    public CompoundPainterFactory(Class klass) {
        super(klass)
    }

    public CompoundPainterFactory(Class klass, boolean leaf) {
        super(klass, leaf)
    }

    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        def painters = builder.componentCollections.remove("painters")
        if (painters != null && painters.size() > 0) {
            node.setPainters(painters.toArray(new Painter[1]))
        }
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (!(child instanceof Painter)) {
            // perhaps throwing an error would be better?
            return
        }
        if (builder.componentCollections["painters"] == null) {
            builder.componentCollections.put("painters", [child])
        } else {
            builder.componentCollections["painters"] += child
        }
    }

}
