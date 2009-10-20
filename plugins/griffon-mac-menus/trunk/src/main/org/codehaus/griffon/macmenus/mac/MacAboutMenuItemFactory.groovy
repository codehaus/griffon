package org.codehaus.griffon.macmenus.mac

/**
 * Created by IntelliJ IDEA.
 * User: shemnon
 * Date: Aug 27, 2009
 * Time: 8:40:04 PM
 */

import griffon.util.IGriffonApplication
import com.apple.eawt.Application
import com.apple.eawt.ApplicationAdapter
import com.apple.eawt.ApplicationEvent
import griffon.util.GriffonApplicationHelper


class MacAboutMenuItemFactory extends AbstractFactory {

    MacAboutMenuItemFactory() {
        // expect that only we will turn this on
        Application.application.enabledAboutMenu = false        
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        IGriffonApplication app = builder.app
        def attrs = new LinkedHashMap(attributes) // make a mutable copy
        attributes.clear() // we consume all attrs because of auto-setting later on
        Application.application.addApplicationListener([
            handleAbout : {ApplicationEvent evt ->
                GriffonApplicationHelper.createMVCGroup(app, 'MacAboutDialog', attrs)
                evt.handled = true
            }
        ] as ApplicationAdapter)
        Application.application.enabledAboutMenu = true
        return new Object()
    }
}
