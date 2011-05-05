/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Oct 12, 2007
 * Time: 2:12:44 PM
 * To change this template use File | Settings | File Templates.
 */
package groovy.swing.factory

import java.awt.Component
import java.awt.Window
import org.jdesktop.swingx.JXFrame


class JXFrameFactory extends JXRootPaneContainerFactory {

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        JXFrame frame
        if (FactoryBuilderSupport.checkValueIsType(value, name, JXFrame.class)) {
            frame = value
        } else {
            frame = new JXFrame()
        }

        handleRootPaneTasks(builder, frame, attributes)

        return frame;
    }

}