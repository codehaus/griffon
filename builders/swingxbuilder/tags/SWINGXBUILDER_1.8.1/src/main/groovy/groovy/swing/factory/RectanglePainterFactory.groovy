/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 12, 2007
 * Time: 9:54:34 AM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory
class RectanglePainterFactory extends AbstractAreaPainterFactory {

    public RectanglePainterFactory(Class klass) {
        super(klass)
    }

    public RectanglePainterFactory(Class klass, boolean leaf) {
        super(klass, leaf)
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (attributes.roundWidth && attributes.roundHeight)
            attributes.rounded = true

        return super.onHandleNodeAttributes(builder, node, attributes)
    }

}
