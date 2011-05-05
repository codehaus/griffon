/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 16, 2007
 * Time: 3:47:12 PM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory

import java.awt.Window
import javax.swing.JButton
import javax.swing.JToolBar
import org.jdesktop.swingx.JXStatusBar
import javax.swing.JMenuBar

abstract class JXRootPaneContainerFactory extends RootPaneContainerFactory {

    public void handleRootPaneTasks(FactoryBuilderSupport builder, Window container, Map attributes) {
        container.rootPaneExt.cancelButton = null
        container.rootPaneExt.defaultButton = null
        builder.context.cancelButtonDelegate =
            builder.addAttributeDelegate {myBuilder, node, myAttributes ->
                if (myAttributes.cancelButton && (node instanceof JButton)) {
                    container.rootPaneExt.cancelButton = node
                    myAttributes.remove('cancelButton')
                }
            }

        super.handleRootPaneTasks(builder, container, attributes)
    }


    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        //println node.rootPaneExt.cancelButton
        //println node.rootPaneExt.defaultButton
        builder.removeAttributeDelegate(builder.context.cancelButtonDelegate)
        super.onNodeCompleted(builder, parent, node)
    }


    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof JMenuBar) {
            parent.JMenuBar = child
        } else if (child instanceof JToolBar) {
            parent.setToolBar(child)
        } else if (child instanceof JXStatusBar) {
            parent.setStatusBar(child)
        } else {
            super.setChild(builder, parent, child)
        }
    }
}
