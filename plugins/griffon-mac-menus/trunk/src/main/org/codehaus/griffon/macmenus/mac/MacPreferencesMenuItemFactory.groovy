package org.codehaus.griffon.macmenus.mac

import griffon.util.IGriffonApplication;
import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent
import com.apple.eawt.ApplicationAdapter
import javax.swing.Action
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: dannoferrin
 * Date: Sep 13, 2009
 * Time: 19:50:56 AM
 */
class MacPreferencesMenuItemFactory extends AbstractFactory {

    MacPreferencesMenuItemFactory() {
        // expect that only we will turn this on
        Application.application.enabledPreferencesMenu = false
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        //FactoryBuilderSupport.checkValueIsTypeNotString()
        IGriffonApplication app = builder.app
        def actionClosure
        if (value instanceof Action) {
            actionClosure = value.&actionPerformed
        } else if (value instanceof Closure) {
            actionClosure = value
        } else if (attributes.containsKey('action')) {
            actionClosure = attributes.action.&actionPerformed
        } else if (attributes.containsKey('actionPerformed')) {
            actionClosure = attributes.actionPerformed
        } else {
            throw new RuntimeException("$name requires either a value of type Action, an action: attribute, or an actionPerformed: attribute")
        }

        Application.application.addApplicationListener([
            handlePreferences : {
                ApplicationEvent evt ->
                    actionClosure(new ActionEvent(evt.source,
                            ActionEvent.ACTION_PERFORMED, "Prefrences"))
                }
        ] as ApplicationAdapter)
        Application.application.enabledPreferencesMenu = true

        attributes.clear()
        return new Object()
    }
}
