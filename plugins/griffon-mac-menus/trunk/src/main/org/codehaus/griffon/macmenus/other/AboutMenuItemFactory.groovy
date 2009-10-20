package org.codehaus.griffon.macmenus.other

/**
 * Created by IntelliJ IDEA.
 * User: shemnon
 * Date: Aug 27, 2009
 * Time: 8:40:04 PM
 */

import static griffon.util.GriffonApplicationUtils.isMacOSX
import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import griffon.util.IGriffonApplication
import com.apple.eawt.Application
import com.apple.eawt.ApplicationAdapter
import com.apple.eawt.ApplicationEvent
import griffon.util.GriffonApplicationHelper
import javax.swing.Action


class AboutMenuItemFactory extends AbstractFactory {

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        IGriffonApplication app = builder.app
        def attrs = new LinkedHashMap(attributes) // make a mutable copy
        attributes.clear() // we consume all attrs because of auto-setting later on
        return builder.menuItem(new AboutDialogAction(app, attrs))
    }
}

class AboutDialogAction extends AbstractAction {

    IGriffonApplication app
    Map attributes

    public AboutDialogAction(IGriffonApplication app, Map attributes) {
        this.app = app
        this.attributes = attributes
        putValue(Action.NAME, "About ${app.applicationProperties['app.name']}" as String)
        attributes.each {k, v -> putValue(k, v)}
    }

    void actionPerformed(ActionEvent e) {
        GriffonApplicationHelper.createMVCGroup(app, 'MacAboutDialog', attributes)
    }

}
