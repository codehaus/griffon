package org.codehaus.griffon.macmenus.other
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;

import java.util.Map;
import java.util.LinkedHashMap;

import static griffon.util.GriffonApplicationUtils.isMacOSX
import griffon.util.IGriffonApplication;
import griffon.util.GriffonApplicationHelper;
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
class PreferencesMenuItemFactory extends AbstractFactory {

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        return builder.menuItem(attributes, value)
    }
}
