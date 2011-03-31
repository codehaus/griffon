/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.swt

import org.eclipse.swt.widgets.Shell
import griffon.core.GriffonApplication

/**
 * Implementation of a per window {@code WindowDisplayHandler} that can be configured via a DSL.<p>
 * This is the default {@code WindowDisplayHandler} used by {@code SWTApplication}. It expects a configuration
 * entry in <code>griffon-app/conf/Config.groovy</code> that looks like the following one<p>
 * <pre>
 * swt {
 *     windowManager {
 *         myWindowName = [
 *             show: {window, app -> ... },
 *             hide: {window, app -> ... }
 *         ]
 *         myOtherWindowName = [
 *             show: {window, app -> ... }
 *         ]
 *     }     
 * }
 * </pre>          
 *
 * For these settings to work you must specify a <code>name:</code> property on the Window/Frame instance. This
 * {@code WindowDisplayHandler} is smart enough to use the default show/hide behavior should any or both are not specified
 * or if a window name does not have a matching configuration. The default behavior will also be used if the Window/Frame
 * does not have a value for its <code>name:</code> property.<p>
 * There's a third option that can be set for each configured window, and that is a delegate {@code WindowDisplayHandler} that
 * will be used for that window alone. The following example shows how it can be configured<p>     
 * <pre>
 * swt {
 *     windowManager {
 *         myWindowName = [
 *             handler: new MyCustomWindowDisplayHandler()
 *         ]
 *         myOtherWindowName = [
 *             show: {window, app -> ... }
 *         ]
 *     }     
 * }
 * </pre> 
 *
 * Lastly, a global handler can be specified for all windows that have not been configured. If specified, this handler will
 * override the usage of the default one. It can be configured as follows<p>
 * <pre>
 * swt {
 *     windowManager {
 *         defaultHandler = new MyCustomWindowDisplayHandler()
 *         myOtherWindowName = [
 *             show: {window, app -> ... }
 *         ]
 *     }     
 * }
 * </pre> 
 *
 * @author Andres Almiray
 */
class ConfigurableWindowDisplayHandler implements WindowDisplayHandler {
    private static final WindowDisplayHandler DEFAULT_WINDOW_DISPLAY_HANDLER = new DefaultWindowDisplayHandler()
  
    void show(Shell window, SWTGriffonApplication application) {
        WindowManager windowManager = application.windowManager
        String windowName = windowManager.findWindowName(window)
        if(windowName) {
            def options = application.config.swt.windowManager[windowName]
            if(options.show instanceof Closure) {
                options.show(window, application)
                return
            } else if(options.handler instanceof WindowDisplayHandler) {
                options.handler.show(window, application)
                return
            }
        }
        fetchDefaultWindowDisplayHandler(application).show(window, application)
    }

    void hide(Shell window, SWTGriffonApplication application) {
        WindowManager windowManager = application.windowManager
        String windowName = windowManager.findWindowName(window)
        if(windowName) {
            def options = application.config.swt.windowManager[windowName]
            if(options.hide instanceof Closure) {
                options.hide(window, application)
                return
            } else if(options.handler instanceof WindowDisplayHandler) {
                options.handler.hide(window, application)
                return
            }
        }
        fetchDefaultWindowDisplayHandler(application).hide(window, application)
    }
    
    private WindowDisplayHandler fetchDefaultWindowDisplayHandler(SWTGriffonApplication application) {
        def handler = application.config.swt.windowManager.defaultHandler
        handler instanceof WindowDisplayHandler ? handler : DEFAULT_WINDOW_DISPLAY_HANDLER
    }    
}